/*-
 * $Id: Heap.java,v 1.126 2005/11/17 14:52:00 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
 
package com.syrus.AMFICOM.Client.Analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.Marker;
import com.syrus.AMFICOM.Client.Analysis.UI.TraceLoadDialog;
import com.syrus.AMFICOM.Client.General.Event.AnalysisParametersListener;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Event.CurrentEventChangeListener;
import com.syrus.AMFICOM.Client.General.Event.CurrentTraceChangeListener;
import com.syrus.AMFICOM.Client.General.Event.EtalonComparisonListener;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryMTAEListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryRefAnalysisListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryTraceListener;
import com.syrus.AMFICOM.Client.General.Event.RefMismatchListener;
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.Etalon;
import com.syrus.AMFICOM.analysis.EtalonComparison;
import com.syrus.AMFICOM.analysis.EventAnchorer;
import com.syrus.AMFICOM.analysis.PFTrace;
import com.syrus.AMFICOM.analysis.SimpleApplicationException;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.AnalysisResult;
import com.syrus.AMFICOM.analysis.dadara.EvaluationPerEventResult;
import com.syrus.AMFICOM.analysis.dadara.IncompatibleTracesException;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEvents;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatchImpl;
import com.syrus.AMFICOM.analysis.dadara.ReliabilitySimpleReflectogramEventImpl;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.reflectometry.ReflectometryEvaluationOverallResult;
import com.syrus.io.BellcoreStructure;
import com.syrus.util.Log;

/**
 * Замена клиентскому пулу com.syrus.AMFICOM.Client.Resource.Pool.
 * Выполняет роль модели.
 * пока что API этой замены избыточна - например, getAllBSMap() делает необязательным
 * использование остальных методов работы с BS
 * 
 * Обозначение: {} = хеш.
 * 
 * Обладает свойствами, по которым не обеспечивает уведомлений:
 * minuitDefaultParams, minuitInitialParams;
 * contextMeasurementSetup;
 * minTraceLevelt;
 * eventAnchorer;
 * rLDialog{};
 * backupEtalonMTM;
 * newMSName;
 * setMarkerObject() / hasMarkerPosition() / getMarkerPosition();
 * etalon - уведомления косвенные, по составляющей etalonMTM
 * 
 * Свойства, по которым уведомления предусмотрены, но не систематизированы
 * (и не гарантированы):
 * minuitAnalysisParams
 * bsHash{}
 * 
 * Свойства с полным отслеживанием и уведомлениями:
 * refAnalysisPrimary;
 * etalonMTM (но не его свойства);
 * currentTrace;
 * currentEvent, currentEtalonEvent;
 * etalonComparison - с той оговоркой, что предполагается, что клиент не будет явно изменять эти объекты;
 * refMismatch (read only) - часть etalonComparison
 * evaluationPerEventResult (read only) - часть etalonComparison
 * evaluationOverallResult (read only) - часть etalonComparison
 *
 * Кроме того, есть свойство primaryMTAE, которое изменяется вместе и только
 * вместе с refAnalysisPrimary; по его изменению тоже рассылаются уведомления.
 * Рассылка происходит одновременно с рассылкой для refAnalysisPrimary,
 * т.е. достаточно подписаться только на одно из них. Рекомендуется
 * подписываться на primaryMTAE в тех случаях, когда refAnalysisPrimary не
 * нужен, а на refAnalysisPrimary - в случаях, когда refAnalysisPrimary нужен.
 * Фактически, primaryMTAE - это часть refAnalysisPrimary.
 * 
 * Замечания:
 * 1. перед установкой эталона (setEtalonPair(?), setMTMEtalon)
 * должен устанавливаться setBSEtalonTrace
 * 
 * 2. любое изменение эталона сбрасывает etalonComparison (и refMismatch)
 * 
 * @author $Author: saa $
 * @version $Revision: 1.126 $, $Date: 2005/11/17 14:52:00 $
 * @module analysis
 */
public class Heap
{
	// constants

	public static final String PRIMARY_TRACE_KEY = "<primarytrace>";
	public static final String ETALON_TRACE_KEY =  "<etalon>";
	public static final String REFERENCE_TRACE_KEY = "<referencetrace>"; // XXX - is referencetrace really required?
	public static final String MODELED_TRACE_KEY = "<modeledtrace>"; // trace got from modelling module

	// properties

	private static AnalysisParameters currentAP;
	private static AnalysisParameters defaultAP;
	private static AnalysisParameters initialAP;
	private static Map<String,Trace> traces = new HashMap<String,Trace>();
	private static RefAnalysis refAnalysisPrimary = null; // "refanalysis", PRIMARY_TRACE_KEY
	private static MeasurementSetup contextMeasurementSetup;	// AnalysisUtil.CONTEXT, "MeasurementSetup"
	private static Map<String,TraceLoadDialog> dialogHash = new HashMap<String,TraceLoadDialog>();	// "dialog", "*"

	// etalon
	private static double minTraceLevel;			// (negative value)
	private static ModelTraceManager etalonMTM = null;
	private static EventAnchorer anchorer = null;

	private static ModelTraceManager backupEtalonMTM = null; // saved 'initial' state of etalon MTM for 'restore' command

	private static String currentTrace = ""; // XXX: initialize to avoid crushes
	//private static int currentEv = -1;
	private static CompositeEventList eventList = new CompositeEventList();
	private static CompositeEventList.Walker currentEvent =
		eventList.new Walker();

	private static String newMSName = null; // the name for newly created (unsaved) MeasurementSetup; null if no new MS

	private static String etalonName;

	private static Marker markerObject = null;

	// результаты сравнения с эталоном - загруженные либо вычисленные
	private static EtalonComparison etalonComparison = null;

	private static LinkedList<BsHashChangeListener> bsHashChangedListeners = new LinkedList<BsHashChangeListener>();
	private static LinkedList<PrimaryTraceListener> primaryTraceListeners = new LinkedList<PrimaryTraceListener>();
	private static LinkedList<PrimaryMTAEListener> primaryMTAEListeners = new LinkedList<PrimaryMTAEListener>();
	private static LinkedList<PrimaryRefAnalysisListener> primaryRefAnalysisListeners = new LinkedList<PrimaryRefAnalysisListener>();
	private static LinkedList<EtalonMTMListener> etalonMTMListeners = new LinkedList<EtalonMTMListener>();
	private static LinkedList<CurrentTraceChangeListener> currentTraceChangeListeners = new LinkedList<CurrentTraceChangeListener>();
	private static LinkedList<CurrentEventChangeListener> currentEventChangeListeners = new LinkedList<CurrentEventChangeListener>();
	private static LinkedList<AnalysisParametersListener> analysisParametersListeners = new LinkedList<AnalysisParametersListener>();
	private static LinkedList<RefMismatchListener> refMismatchListeners = new LinkedList<RefMismatchListener>();
	private static LinkedList<EtalonComparisonListener> etalonComparisonListeners = new LinkedList<EtalonComparisonListener>();

	// constructor is not available
	private Heap() {
		// not instantiable 
	}

	// methods

	public static TraceLoadDialog getRLDialogByKey(String key) {
		return dialogHash.get(key);
	}

	public static void setRLDialogByKey(String key,
			TraceLoadDialog dialog) {
		dialogHash.put(key, dialog);
	}
	public static ModelTraceAndEventsImpl getMTAEPrimary() {
		return refAnalysisPrimary != null ? refAnalysisPrimary.getMTAE()
				: null;
	}

	public static int getNumberOfEvents() {
		return refAnalysisPrimary == null ?
				0 : refAnalysisPrimary.getMTAE().getNEvents();
	}

	public static ModelTraceManager getMTMEtalon() {
		return etalonMTM;
	}

	public static RefAnalysis getRefAnalysisPrimary() {
		return refAnalysisPrimary;
	}

	public static Trace getAnyTraceByKey(String key) {
		return traces.get(key);
	}
	private static void setAnyTraceByKey(String key, Trace trace) {
		traces.put(key, trace);
	}
	private static void removeAnyTraceByKey(String key) {
		traces.remove(key);
	}

	public static PFTrace getAnyPFTraceByKey(String key) {
		Trace trace = getAnyTraceByKey(key);
		return trace != null ? trace.getPFTrace() : null;
	}

	public static PFTrace getPFTracePrimary() {
		return getAnyPFTraceByKey(PRIMARY_TRACE_KEY);
	}

	/**
	 * Устанавливает (заменяет) первичную рефлектограмму.
	 * Предполагается использование этого метода для выбора первичной
	 * р/г среди уже загруженных.
	 */ 
	@Deprecated
	public static void setPrimaryTrace(Trace tr) {
		setAnyTraceByKey(PRIMARY_TRACE_KEY, tr);
	}

	/**
	 * Открывает рефлектограмму как первичную.
	 * Предварительно автоматически закрывает все ранее
	 * открытые рефлектограммы.
	 * Анализ не проводит. Уведомлений не обеспечивает.
	 * Поскольку GUI рассчитывает, что primaryTrace и primaryAnalysis
	 * появятся одновременно, необходимо после вызова этого метода
	 * провести анализ либо как-то инане установить refAnalysis,
	 * а затем разослать уведомления о смене/появлении primary trace&analysis.
	 */ 
	public static void openPrimaryTrace(Trace tr) {
		closeAll();
		setAnyTraceByKey(PRIMARY_TRACE_KEY, tr);
	}

	/**
	 * Закрывает все рефлектограммы, открывает в качестве первичной
	 * рефлектограмму заданного измерения и ее готовый анализ,
	 * рассылает все необходимые оповещения.
	 * <p>
	 * На данный момент метод используется в модуле survey.
	 * @todo пользоваться этим методом и в модуле analysis при открытии
	 * рефлектограммы с уже проведенным на агенте анализе.
	 * </p>
	 * @param result результат измерения, содержащий рефлектограмму
	 * @param ar результат анализа этой рефлектограммы (not null)
	 * @throws SimpleApplicationException,
	 *    если в загружаемом результате нет рефлектограммы.
	 */
	public static void openPrimaryTraceAndNotify(Result result,
			AnalysisResult ar)
	throws SimpleApplicationException {
		Trace tr = new Trace(result, ar);
		openPrimaryTrace(tr);
		setRefAnalysisPrimary(new RefAnalysis(tr.getPFTrace(), ar));
		primaryTraceOpened();
	}

	/**
	 * Закрывает все рефлектограммы, открывает в качестве первичной
	 * рефлектограмму заданного измерения, проводит анализ,
	 * рассылает все необходимые оповещения.
	 * <p>
	 * На данный момент метод используется в модуле survey.
	 * @todo быть может, перейти на пользование этим методом и в analysis.
	 * </p>
	 * @param result результат измерения, содержащий рефлектограмму
	 * @throws SimpleApplicationException,
	 *    если в загружаемом результате нет рефлектограммы.
	 */
	public static void openPrimaryTraceAndNotify(Result result)
	throws SimpleApplicationException {
		Trace tr = new Trace(result, getMinuitAnalysisParams());
		openPrimaryTrace(tr);
		setRefAnalysisPrimary(new RefAnalysis(tr.getPFTrace()));
		primaryTraceOpened();
	}

	/**
	 * Открывает рефлектограмму как первичную.
	 * Автоматически закрывает все ранее открытые рефлектограммы.
	 * @param primaryTrace Рефлектограмма
	 * @param key ключ (будет использоваться как ключ, если первичной станет другая р/г)
	 */
	public static void openPrimaryTraceFromBS(BellcoreStructure primaryTrace,
			String key) {
		openPrimaryTrace(new Trace(primaryTrace,
				key,
				getMinuitAnalysisParams()));
	}

	/**
	 * Открывает набор рефлектограмм.
	 * Выбирает одну наиболее типичную р/г среди входного набора,
	 * и открывает ее как первичную
	 * (с предварительным закрытием всех открытых).
	 * Затем открывает все остальные входные р/г как вторичные.
	 * Note: Объект RefAnalysis пока не создается, и caller должен
	 * сделать это самостоятельно с помощью
	 * последовательности { setRefAnalysisPrimary(); primaryTraceOpened(); }
	 * либо makePrimaryAnalysis(). 
	 * @param traceColl
	 */
	public static void openManyTraces(Collection<Trace> traceColl) {
		// Создаем {@link Trace} и Bellcore по каждому входному результату
		Collection<PFTrace> pfColl = new ArrayList<PFTrace>(traceColl.size());
		for (Trace tr: traceColl) {
			pfColl.add(tr.getPFTrace());
		}

		// пытаемся выбрать самую типичную рефлектограмму.
		// если набор несовместен, используем первую попавшуюся.
		// XXX: performance: 20-50% of method time when loading many traces (не знаю где именно)
		Trace tracePrimary = null;
		try {
			PFTrace pf =
				CoreAnalysisManager.getMostTypicalTrace(pfColl);
			for (Trace tr: traceColl) {
				if (tr.getPFTrace() == pf) {
					tracePrimary = tr;
					break;
				}
			}
			if (tracePrimary == null) {
				Log.debugMessage("Failed to choose most typical trace as primary",
						Log.DEBUGLEVEL03);
				System.err.println("Failed to choose most typical trace as primary"); // FIXME: debug-time message
			} else {
				Log.debugMessage("chosed most typical trace as primary",
					Log.DEBUGLEVEL07);
			}
		} catch (IncompatibleTracesException e) {
			// ignore for now: tracePrimary == null check will do processing
			Log.debugMessage("incompatible traces, using first one",
					Log.DEBUGLEVEL07);
		}
		if (tracePrimary == null) {
			tracePrimary = traceColl.iterator().next();
		}

		// Открываем самую типичную как первичную
		openPrimaryTrace(tracePrimary);

		// Открываем все остальные как вторичные
		// XXX: performance: 50-80% of method time when loading many traces (на рассылке сообщений?)
		for (Trace trace: traceColl) {
			if (trace != tracePrimary) {
				putSecondaryTrace(trace);
			}
		}
	}

	public static ModelTraceAndEvents getAnyMTAE(String key) {
		if (key.equals(PRIMARY_TRACE_KEY)) {
			return getMTAEPrimary();
		} else if (key.equals(ETALON_TRACE_KEY)) {
			return getMTMEtalon() != null ? getMTMEtalon().getMTAE() : null;
		} else {
			return getAnyTraceByKey(key) != null ? getAnyTraceByKey(key).getMTAE() : null;
		}
	}

	public static PFTrace getPFTraceEtalon() {
		return getAnyPFTraceByKey(ETALON_TRACE_KEY);
	}

	public static void setEtalonTraceFromPFTrace(PFTrace etalonTrace, String name) {
		// @todo - эталон должен храниться отдельно от остальных (первичных и вторичных) рефлектограмм, и вообще не как Trace, а отдельно
		if (etalonTrace != null) {
			etalonName = name;
			setAnyTraceByKey(ETALON_TRACE_KEY, new Trace(
					etalonTrace,
					ETALON_TRACE_KEY,
					getMinuitAnalysisParams()));
		}
		else {
			removeAnyTraceByKey(ETALON_TRACE_KEY);
			etalonName = null;
		}
	}

	public static Trace getReferenceTrace() {
		return getAnyTraceByKey(REFERENCE_TRACE_KEY);
	}

	public static Trace getPrimaryTrace() {
		return getAnyTraceByKey(PRIMARY_TRACE_KEY);
	}

	public static boolean hasSecondaryBSKey(String id) {
		return traces.containsKey(id);
	}

	/**
	 * @return номер в списке пар событий, сооветствующий текущему.
	 * Значение -1 может быть только в случае, если не выбрано ни одно событие
	 */
	public static int getCurrentCompositeEvent() {
		return currentEvent.getCompositeEvent();
	}

	/**
	 * @return номер события, который точно (взаимно однозначно)
	 * соответствует текущему, либо -1, если такого события не нашлось
	 */
	public static int getCurrentEvent1() {
		return currentEvent.getEvent1();
	}

	/**
	 * @return номер события эталона, точно соответствующего текущему,
	 *   либо -1, если такого события не нашлось
	 */
	public static int getCurrentEtalonEvent1() {
		return currentEvent.getEtalonEvent1();
	}


	/**
	 * @return номер события, который грубо соответствует текущему
	 * (может быть -1, но это нечасто)
	 */
	public static int getCurrentEvent2() {
		return currentEvent.getEvent2();
	}

	/**
	 * @return номер события эталона, грубо соответствующего текущему
	 * (может быть -1, но это нечасто)
	 */
	public static int getCurrentEtalonEvent2() {
		return currentEvent.getEtalonEvent2();
	}

	public static void gotoNextEtalonEvent() {
		currentEvent.toNextEtalonEvent();
		notifyCurrentEventChanged();
	}

	public static void gotoPreviousEtalonEvent() {
		currentEvent.toPrevEtalonEvent();
		notifyCurrentEventChanged();
	}

	public static String getNewMSName() {
		return newMSName;
	}

	public static void setNewMSName(String newMSName) {
		Heap.newMSName = newMSName;
	}

	// --------

	public static boolean hasEmptyAllBSMap() {
		return traces.isEmpty();
	}

	private static String getFirstSecondaryBSKey() {
		for (String key: traces.keySet()) {
			if (key != PRIMARY_TRACE_KEY)
				return key;
		}
		return null;
	}

	public static boolean hasSecondaryBS() {
		return getFirstSecondaryBSKey() == null ? false : true;
	}

	public static AnalysisParameters getMinuitInitialParams() {
		return (AnalysisParameters) initialAP.clone();
	}

	public static void setMinuitInitialParamsFromCurrentAP() {
		Heap.initialAP = (AnalysisParameters) currentAP.clone();
	}

	public static AnalysisParameters getMinuitDefaultParams() {
		return (AnalysisParameters) defaultAP.clone();
	}

	public static void setMinuitDefaultParams(AnalysisParameters minuitDefaults) {
		Heap.defaultAP = minuitDefaults;
	}

	public static AnalysisParameters getMinuitAnalysisParams() {
		return currentAP;
	}

	// the caller should remember to invoke notifyAnalysisParametersUpdated()
	public static void setMinuitAnalysisParams(AnalysisParameters minuitAnalysisParams) {
		Heap.currentAP = minuitAnalysisParams;
	}

	public static MeasurementSetup getContextMeasurementSetup() {
		return contextMeasurementSetup;
	}

	public static void setContextMeasurementSetup(
			MeasurementSetup contextMeasurementSetup) {
		Heap.contextMeasurementSetup = contextMeasurementSetup;
	}

	public static Collection<PFTrace> getPFTraceCollection() {
		Collection<PFTrace> coll = new ArrayList<PFTrace>(traces.size());
		for (Trace tr: traces.values()) {
			coll.add(tr.getPFTrace());
		}
		return coll;
	}

	public static Collection<Trace> getTraceCollection() {
		Collection<Trace> coll = new ArrayList<Trace>(traces.size());
		for (Trace tr: traces.values()) {
			coll.add(tr);
		}
		return coll;
	}

	public static boolean hasMinTraceLevel() {
		return hasEtalon();
	}

	public static double getMinTraceLevel() {
		return minTraceLevel;
	}

	public static void setMinTraceLevel(double minTraceLevel) {
		minTraceLevel = Math.round(minTraceLevel);
		Heap.minTraceLevel = minTraceLevel;
	}

	public static boolean hasEtalon() {
		return Heap.getMTMEtalon() != null;
	}

	public static Etalon getEtalon() {
		return new Etalon(
				Heap.getMTMEtalon(),
				Heap.getMinTraceLevel(),
				Heap.getAnchorer());
	}

	/**
	 * Возвращает текущий привязчик EventAnchorer либо null, если такового нет.
	 */
	public static EventAnchorer getAnchorer() {
		return anchorer;
	}
	/**
	 * Устанавливает привязчик EventAnchorer
	 * @param anchorer устанавливаемый привязчик, may be null
	 */
	public static void setAnchorer(EventAnchorer anchorer) {
		Heap.anchorer = anchorer;
	}
	/**
	 * Возвращает non-null привязчик EventAnchorer, создавая его, если его еще нет.
	 * @return non-null привязчик EventAnchorer, создавая его, если его еще нет
	 * @throw IllegalStateException если эталона нет.
	 */
	public static EventAnchorer obtainAnchorer() {
		if (!hasEtalon()) {
			throw new IllegalStateException("no etalon");
		}
		if (anchorer == null) {
			anchorer = new EventAnchorer(
					getMTMEtalon().getMTAE().getNEvents());
		}
		return anchorer;
	}

	private static void removeAllBS() {
		traces = new HashMap<String, Trace>();
	}

	public static void removeAnyBSByName(String id) {
		removeAnyTraceByKey(id);
	}

	public static ModelTraceManager getMTMBackupEtalon() {
		try {
			return (ModelTraceManager)backupEtalonMTM.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e.getMessage());
		}
	}
	private static void setMTMBackupEtalon(ModelTraceManager etalonMTM) {
		try {
			Heap.backupEtalonMTM = etalonMTM == null ? null : (ModelTraceManager)etalonMTM.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e.getMessage());
		}
	}

	public static CompositeEventList getEventList() {
		return eventList;
	}

	public static void setMarkerObject(Marker marker) {
		markerObject = marker;
	}
	public static boolean hasMarkerPosition() {
		return markerObject != null;
	}
	public static int getMarkerPosition() {
		return markerObject.getPos();
	}

	// dispatcher stuff

	// NB: if the primary trace is opened, then there are
	// two events generated:
	// notifyBsHashAdd -> bsHashAdded() and
	// notifyPrimaryTraceOpened -> primaryTraceCUpdated()
	private static void notifyBsHashAdd(String key) {
		Log.debugMessage("key " + key, Level.FINEST);
		for (BsHashChangeListener listener: bsHashChangedListeners)
			listener.bsHashAdded(key);
	}

	//  primary trace всегда останется, пока есть другие
	private static void notifyBsHashRemove(String key) {
		Log.debugMessage("key " + key, Level.FINEST);
		for (BsHashChangeListener listener: bsHashChangedListeners)
			listener.bsHashRemoved(key);
	}

	private static void notifyBsHashRemoveAll() {
		Log.debugMessage(Level.FINEST);
		for (BsHashChangeListener listener: bsHashChangedListeners)
			listener.bsHashRemovedAll();
	}

	/**
	 * should also be suitable if primary trace completely replaced
	 */
	private static void notifyPrimaryTraceOpened() {
		Log.debugMessage(Level.FINEST);
		for (PrimaryTraceListener listener: primaryTraceListeners)
			listener.primaryTraceCUpdated();
	}

	private static void notifyPrimaryTraceClosed() {
		Log.debugMessage(Level.FINEST);
		for (PrimaryTraceListener listener: primaryTraceListeners)
			listener.primaryTraceRemoved();
	}

	public static void notifyAnalysisParametersUpdated() {
		Log.debugMessage(Level.FINEST);
		// do not notify traces
		// notify subscribers
		for (AnalysisParametersListener listener: analysisParametersListeners)
			listener.analysisParametersUpdated();
	}

	private static void notifyRefMismatchCUpdated() {
		Log.debugMessage(Level.FINEST);
		for (RefMismatchListener listener: refMismatchListeners)
			listener.refMismatchCUpdated();
	}

	private static void notifyEtalonComparisonRemoved() {
		Log.debugMessage(Level.FINEST);
		for (EtalonComparisonListener listener: etalonComparisonListeners)
			listener.etalonComparisonRemoved();
	}

	private static void notifyEtalonComparisonCUpdated() {
		Log.debugMessage(Level.FINEST);
		for (EtalonComparisonListener listener: etalonComparisonListeners)
			listener.etalonComparisonCUpdated();
	}

	private static void notifyRefMismatchRemoved() {
		Log.debugMessage(Level.FINEST);
		for (RefMismatchListener listener: refMismatchListeners)
			listener.refMismatchRemoved();
	}

	private static void notifyPrimaryMTAECUpdated() {
		Log.debugMessage(Level.FINEST);
		for (PrimaryMTAEListener listener: primaryMTAEListeners)
			listener.primaryMTAECUpdated();
	}

	private static void notifyPrimaryMTAERemoved() {
		Log.debugMessage(Level.FINEST);
		for (PrimaryMTAEListener listener: primaryMTAEListeners)
			listener.primaryMTAERemoved();
	}

	private static void notifyPrimaryRefAnalysisCUpdated() {
		Log.debugMessage(Level.FINEST);
		for (PrimaryRefAnalysisListener listener: primaryRefAnalysisListeners)
			listener.primaryRefAnalysisCUpdated();
	}

	private static void notifyPrimaryRefAnalysisRemoved() {
		Log.debugMessage(Level.FINEST);
		for (PrimaryRefAnalysisListener listener: primaryRefAnalysisListeners)
			listener.primaryRefAnalysisRemoved();
	}

	private static void notifyEtalonMTMCUpdated() {
		Log.debugMessage(Level.FINEST);
		removeEtalonComparison();
		for (EtalonMTMListener listener: etalonMTMListeners)
			listener.etalonMTMCUpdated();
	}

	private static void notifyEtalonMTMRemoved() {
		Log.debugMessage(Level.FINEST);
		removeEtalonComparison();
		for (EtalonMTMListener listener: etalonMTMListeners)
			listener.etalonMTMRemoved();
	}

	private static void notifyCurrentTraceChanged() {
		Log.debugMessage("currentTrace = " + currentTrace, Level.FINEST);
		for (CurrentTraceChangeListener listener: currentTraceChangeListeners)
			listener.currentTraceChanged(currentTrace);
	}

	private static void notifyCurrentEventChanged() {
		Log.debugMessage("nEvent = (" + getCurrentEvent1() + ", " + getCurrentEtalonEvent1() + ")", Level.FINEST);
		for (CurrentEventChangeListener listener: currentEventChangeListeners)
			listener.currentEventChanged();
	}

	private static <T> void addListener(Collection<T> c, T listener) {
		if (!c.contains(listener))
			c.add(listener);
	}

	private static <T> void removeListener(Collection<T> c, T listener) {
		if (c.contains(listener))
			c.remove(listener);
	}

	public static void addBsHashListener(BsHashChangeListener listener) {
		addListener(bsHashChangedListeners, listener);
	}

	public static void removeBsHashListener(BsHashChangeListener listener) {
		removeListener(bsHashChangedListeners, listener);
	}

	public static void addPrimaryTraceListener(PrimaryTraceListener listener) {
		addListener(primaryTraceListeners, listener);
	}

	public static void removePrimaryTraceListener(PrimaryTraceListener listener) {
		removeListener(primaryTraceListeners, listener);
	}

	public static void addPrimaryMTMListener(PrimaryMTAEListener listener) {
		addListener(primaryMTAEListeners, listener);
	}

	public static void removePrimaryMTMListener(PrimaryMTAEListener listener) {
		removeListener(primaryMTAEListeners, listener);
	}

	public static void addPrimaryRefAnalysisListener(PrimaryRefAnalysisListener listener) {
		addListener(primaryRefAnalysisListeners, listener);
	}

	public static void removePrimaryRefAnalysisListener(PrimaryRefAnalysisListener listener) {
		removeListener(primaryRefAnalysisListeners, listener);
	}

	public static void addEtalonMTMListener(EtalonMTMListener listener) {
		addListener(etalonMTMListeners, listener);
	}

	public static void removeEtalonMTMListener(EtalonMTMListener listener) {
		removeListener(etalonMTMListeners, listener);
	}

	public static void addCurrentTraceChangeListener(
			CurrentTraceChangeListener listener) {
		addListener(currentTraceChangeListeners, listener);
	}

	public static void removeCurrentTraceChangeListener(
			CurrentTraceChangeListener listener) {
		removeListener(currentTraceChangeListeners, listener);
	}

	public static void addCurrentEventChangeListener(
			CurrentEventChangeListener listener) {
		addListener(currentEventChangeListeners, listener);
	}

	public static void removeCurrentEventChangeListener(
			CurrentEventChangeListener listener) {
		removeListener(currentEventChangeListeners, listener);
	}

	public static void addAnalysisParametersListener(
			AnalysisParametersListener listener) {
		addListener(analysisParametersListeners, listener);
	}

	public static void removeAnalysisParametersListener(
			AnalysisParametersListener listener) {
		removeListener(analysisParametersListeners, listener);
	}

	public static void addRefMismatchListener(
			RefMismatchListener listener) {
		addListener(refMismatchListeners, listener);
	}

	public static void removeRefMismatchListener(
			RefMismatchListener listener) {
		removeListener(refMismatchListeners, listener);
	}

	public static void removeEtalonComparisonListener(
			EtalonComparisonListener listener) {
		removeListener(etalonComparisonListeners, listener);
	}

	public static void addEtalonComparisonListener(
			EtalonComparisonListener listener) {
		addListener(etalonComparisonListeners, listener);
	}

	/**
	 * вызывайте этот метод после установки нового primary trace и его анализа
	 */
	public static void primaryTraceOpened() {
		notifyBsHashAdd(PRIMARY_TRACE_KEY);
		notifyPrimaryTraceOpened();
		if (refAnalysisPrimary.getMTAE().getNEvents() > 0)
			currentEvent.toEvent(0); // (1)
		notifyCurrentEventChanged(); // (2)
		// операторы (1) и (2) делают текущим событие #0
		setCurrentTracePrimary();
	}

	private static void traceClosed(String key) {
		notifyBsHashRemove(key);
		if (key.equals(PRIMARY_TRACE_KEY))
			notifyPrimaryTraceClosed();
	}

	/*
	 * ===============================================================
	 * methods that both make changed and notify appropriate listeners
	 * =============================================================== 
	 */

	/**
	 *  годится для закрытия любой рефлектограммы, кроме первичной
	 */
	public static void closeTrace(String key) {
		if (ETALON_TRACE_KEY.equals(key)) {
			unSetEtalonPair();
		} else {
			removeAnyBSByName(key);
		}
		traceClosed(key);
		setCurrentTracePrimary();
		//updateCurrentTraceWhenBSRemoved();
//    	System.err.println("Heap.closeTrace: " + key + "; now there are " + traces.size() + " traces left");
//    	for (String k: traces.keySet()) {
//    		System.err.println("  Key: " + k);
//    	}
	}

	public static void setCurrentTrace(String id) {
		currentTrace = id;
		notifyCurrentTraceChanged();
	}

	public static void setCurrentTracePrimary() {
		currentTrace = PRIMARY_TRACE_KEY;
		notifyCurrentTraceChanged();
	}

	public static String getCurrentTrace() {
		return currentTrace;
	}

	// value of -1 means 'no event selected'
	public static void setCurrentEvent(int nEvent) {
		currentEvent.toEvent(nEvent);
		notifyCurrentEventChanged();
	}

	public static void setCurrentEtalonEvent(int nEtEvent) {
		currentEvent.toEtalonEvent(nEtEvent);
		notifyCurrentEventChanged();
	}

	public static void setCurrentCompositeEvent(int nEvent) {
		currentEvent.toCompositeEvent(nEvent);
		notifyCurrentEventChanged();
	}

	private static void fixEventList() {
		eventList.dataUpdated();
		currentEvent.fixNEvent();
	}

	/**
	 * 
	 * @param ra not null
	 */
	public static void setRefAnalysisPrimary(RefAnalysis ra) {
		refAnalysisPrimary = ra;
		fixEventList();
		setEtalonComparison(null); // необходимо убрать результаты сравнения предыдущей р/г
		notifyPrimaryRefAnalysisCUpdated();
		notifyPrimaryMTAECUpdated();
	}

	/**
	 * Устанавливает эталонный MTM.
	 * Если устанавливается ненулевой MTM, к этому моменту уже должна
	 * быть установлена рефлектограмма эталона, иначе возможны проблемы
	 * в окне анализа, которое пока что полагается на то, что у отображенного
	 * эталона непременно будет BS.
	 * Безусловно удаляет etalonComparison.
	 * Если устанавливается новый MTM (ссылка указывает не на тот MTM,
	 * который установлен к моменту данного вызова), то также удаляет anchorer.
	 * @param mtm may be null
	 */
	public static void setMTMEtalon(ModelTraceManager mtm) {
		if (etalonMTM != mtm)
			setAnchorer(null);
		etalonMTM = mtm;
		fixEventList();
		setMTMBackupEtalon(mtm);
		if (mtm == null) {
			backupEtalonMTM = null; // не нужно, он уже и так null
			notifyEtalonMTMRemoved();
		} else {
			ClientAnalysisManager.setDefaultMinTraceLevel(); 
			notifyEtalonMTMCUpdated();
		}
	}

	public static void putSecondaryTrace(Trace tr) {
		String key = tr.getKey();
		traces.put(key, tr);
		notifyBsHashAdd(key);
	}

	public static void putSecondaryTraceByKeyFromBS(String key, BellcoreStructure bs) {
		putSecondaryTrace(new Trace(
				bs,
				key,
				getMinuitAnalysisParams()));
	}

	public static void setReferenceTraceFromBS(BellcoreStructure bs, String key) {
		traces.put(REFERENCE_TRACE_KEY, new Trace(
				bs,
				key,
				getMinuitAnalysisParams()));
		notifyBsHashAdd(REFERENCE_TRACE_KEY);
	}

	/**
	 * closes all BS traces, primary RefAnalysis&MTAE and etalon MTM 
	 */
	public static void closeAll() {
		// close Etalon MTM & anchorer
		etalonMTM = null;
		backupEtalonMTM = null;
		etalonName = null;
		anchorer = null;
		fixEventList();
		notifyEtalonMTMRemoved();

		// close all BS
		removeAllBS();
		notifyBsHashRemoveAll();

		// close Primary MTAE
		refAnalysisPrimary = null;
		fixEventList();
		notifyPrimaryRefAnalysisRemoved();
		notifyPrimaryMTAERemoved();
	}

	// объединяет два события
	// rather slow: replaces the whole analysis
	public static void joinCurrentEventWithPrevious() {
		int nEvent = getCurrentEvent2();
		if (nEvent < 0)
			return; // no event selected
		int joinPoint = nEvent - 1;
		if (joinPoint < 0)
			return; // current event is at the beginning of list
		ModelTraceAndEventsImpl mtae = getMTAEPrimary();
		ReliabilitySimpleReflectogramEventImpl[] in =
			(ReliabilitySimpleReflectogramEventImpl[])mtae.getSimpleEvents();
		ReliabilitySimpleReflectogramEventImpl[] out =
			new ReliabilitySimpleReflectogramEventImpl[in.length - 1];
		for (int i = 0; i < out.length; i++) {
			out[i] =
				i <= joinPoint ? i < joinPoint // tri-case: <, =, >
				? in[i]
				: new ReliabilitySimpleReflectogramEventImpl(
						in[i].getBegin(),
						in[i + 1].getEnd(),
						in[i].getEventType())
				: in[i + 1];
		}
		PFTrace pfTrace = getPFTracePrimary();
		ModelTraceAndEventsImpl newMtae = ModelTraceAndEventsImpl.replaceRSE(
				mtae, out, pfTrace.getFilteredTraceClone());
		replacePrimaryAnalysisMTAE(newMtae);
	}

	// rather slow: replaces the whole analysis
	// n must be be >= 1 (only >=2 are useless)
	public static void splitCurrentEventToN(int n) {
		int nEvent = getCurrentEvent2();
		if (nEvent < 0)
			return;
		if (n < 1)
			throw new IllegalArgumentException("n < 1");
		ModelTraceAndEventsImpl mtae = getMTAEPrimary();
		ReliabilitySimpleReflectogramEventImpl[] in =
			(ReliabilitySimpleReflectogramEventImpl[])mtae.getSimpleEvents();
		ReliabilitySimpleReflectogramEventImpl[] out =
			new ReliabilitySimpleReflectogramEventImpl[in.length + n - 1];
		for (int i = 0; i < nEvent; i++) {
			out[i] = in[i];
		}
		int evLen = in[nEvent].getEnd() - in[nEvent].getBegin();
		for (int i = 0; i < n; i++) {
			out[nEvent + i] = new ReliabilitySimpleReflectogramEventImpl(
					in[nEvent].getBegin() + evLen * i / n,
					in[nEvent].getBegin() + evLen * (i + 1) / n,
					in[nEvent].getEventType());
		}
		for (int i = nEvent + 1; i < in.length; i++) {
				out[i + n - 1] = in[i];
		}
		PFTrace pf = getPFTracePrimary();
		ModelTraceAndEventsImpl newMtae = ModelTraceAndEventsImpl.replaceRSE(
				mtae, out, pf.getFilteredTraceClone());
		replacePrimaryAnalysisMTAE(newMtae);
	}

	// rather slow: replaces the whole analysis
	public static void changeCurrentEventType(int newType) {
		int nEvent = getCurrentEvent2();
		if (nEvent < 0)
			return; // no event selected
		ModelTraceAndEventsImpl mtae = getMTAEPrimary();
		changeOneEventAndFixNeighbours(nEvent, new ReliabilitySimpleReflectogramEventImpl(
						mtae.getSimpleEvent(nEvent).getBegin(),
						mtae.getSimpleEvent(nEvent).getEnd(),
						newType));
	}

	// rather slow: replaces the whole analysis
	// also change prev. event end
	public static void changeCurrentEventBegin(int begin) {
		int nEvent = getCurrentEvent2();
		if (nEvent < 0)
			return; // no event selected
		ModelTraceAndEventsImpl mtae = getMTAEPrimary();
		if (nEvent > 0
				&& begin <= mtae.getSimpleEvent(nEvent - 1).getBegin())
			return; // begin <= prev.begin
		if (nEvent < mtae.getNEvents() - 1
				&& begin >= mtae.getSimpleEvent(nEvent + 1).getEnd())
			return; // begin >= next.end
		changeOneEventAndFixNeighbours(nEvent, new ReliabilitySimpleReflectogramEventImpl(
						begin,
						mtae.getSimpleEvent(nEvent).getEnd(),
						mtae.getSimpleEvent(nEvent).getEventType()));
	}

	// rather slow: replaces the whole analysis
	// also change prev. event end
	public static void changeCurrentEventEnd(int end) {
		int nEvent = getCurrentEvent2();
		if (nEvent < 0)
			return; // no event selected
		ModelTraceAndEventsImpl mtae = getMTAEPrimary();
		if (nEvent > 0
				&& end <= mtae.getSimpleEvent(nEvent - 1).getBegin())
			return; // end <= prev.begin
		if (nEvent < mtae.getNEvents() - 1
				&& end >= mtae.getSimpleEvent(nEvent + 1).getEnd())
			return; // end >= next.end
		changeOneEventAndFixNeighbours(nEvent,
				new ReliabilitySimpleReflectogramEventImpl(
						mtae.getSimpleEvent(nEvent).getBegin(),
						end,
						mtae.getSimpleEvent(nEvent).getEventType()));
	}

	// rather slow: replaces the whole analysis
	// moves nearby events if required (and resets their reliability)
	private static void changeOneEventAndFixNeighbours(int nEvent,
			ReliabilitySimpleReflectogramEventImpl ev) {
		ModelTraceAndEventsImpl mtae = getMTAEPrimary();
		ReliabilitySimpleReflectogramEventImpl[] in =
			(ReliabilitySimpleReflectogramEventImpl[])mtae.getSimpleEvents();
		ReliabilitySimpleReflectogramEventImpl[] out =
			new ReliabilitySimpleReflectogramEventImpl[in.length];
		for (int i = 0; i < in.length; i++) {
			if (i == nEvent) {
				out[i] = ev;
			} else if (i == nEvent - 1 && in[i].getEnd() != ev.getBegin()) {
				out[i] = new ReliabilitySimpleReflectogramEventImpl(
						in[i].getBegin(),
						ev.getBegin(),
						in[i].getEventType());
			} else if (i == nEvent + 1 && in[i].getBegin() != ev.getEnd()) {
				out[i] = new ReliabilitySimpleReflectogramEventImpl(
						ev.getEnd(),
						in[i].getEnd(),
						in[i].getEventType());
			} else {
				out[i] = in[i];
			}
		}
		PFTrace trace = getPFTracePrimary();
		ModelTraceAndEventsImpl newMtae = ModelTraceAndEventsImpl.replaceRSE(
				mtae, out, trace.getFilteredTraceClone());
		replacePrimaryAnalysisMTAE(newMtae);
	}
	
	private static void replacePrimaryAnalysisMTAE(
			ModelTraceAndEventsImpl mtae) {
		setRefAnalysisPrimary(new RefAnalysis(getRefAnalysisPrimary(), mtae));
	}


	/**
	 * XXX: Возвращаемое значение null может означать как соответствие эталону,
	 * так и то, что сравнение не проводилось. Надо переделать.
	 */
	@Deprecated
	public static ReflectogramMismatchImpl getRefMismatch() {
		if (etalonComparison != null) {
			List<ReflectogramMismatchImpl> alarms =
				etalonComparison.getAlarms();
			if (alarms.size() > 0) {
				return alarms.iterator().next();
			}
		}
		return null; // либо сравнения не было, либо алармов нет
	}

	public static void setEtalonComparison(EtalonComparison ec) {
		Log.debugMessage((ec == null ? null : "not null"),
				Level.FINEST);
		Heap.etalonComparison = ec;
		if (ec == null)
			notifyEtalonComparisonRemoved();
		else
			notifyEtalonComparisonCUpdated();
		if (getRefMismatch() == null)
			notifyRefMismatchRemoved();
		else
			notifyRefMismatchCUpdated();
	}

	public static EtalonComparison getEtalonComparison() {
		return Heap.etalonComparison;
	}

	public static EvaluationPerEventResult getEvaluationPerEventResult() {
		return Heap.etalonComparison == null ? null
				: Heap.etalonComparison.getPerEventResult();
	}

	public static ReflectometryEvaluationOverallResult getEvaluationOverallResult() {
		return Heap.etalonComparison == null ? null
				: Heap.etalonComparison.getOverallResult();
	}
	/*
	 * ===============================================================
	 * Other methods
	 * ===============================================================
	 */

	/**
	 * Создает RefAnalysis по загруженной primaryTrace.
	 * XXX: API: API избыточно - см. makeAnalysis, makePrimaryAnalysis()
	 */
	public static void updatePrimaryAnalysis() {
		Trace trace = getPrimaryTrace();
		if (trace != null)
		{
			RefAnalysis a = new RefAnalysis(trace);
			setRefAnalysisPrimary(a);
		}
		primaryTraceOpened();
	}

	public static void makePrimaryAnalysis() {
		makeAnalysis();
		primaryTraceOpened();
	}

	public static void makeAnalysis() {
		PFTrace pf = getPFTracePrimary();
		if (pf != null)
		{
			RefAnalysis a = new RefAnalysis(pf);
			setRefAnalysisPrimary(a);
		}
	}

	public static void unSetEtalonPair() {
		setEtalonTraceFromPFTrace(null, null);
		Heap.setAnchorer(null);
		Heap.setMTMEtalon(null);
	}

	public static void setEtalonPair(PFTrace pf,
			Etalon etalonObj, String etalonName) {
		setEtalonTraceFromPFTrace(pf, etalonName);
		Heap.setMinTraceLevel(etalonObj.getMinTraceLevel());
		Heap.setAnchorer(etalonObj.getAnc());
		Heap.setMTMEtalon(etalonObj.getMTM());
	}

	public static boolean isTraceSecondary(String key) {
		// первичная и эталон - не вторичные
		if (ETALON_TRACE_KEY.equals(key) || PRIMARY_TRACE_KEY.equals(key))
			return false;
		// дополнительная проверка (сейчас не обязательная) - чтобы key совпадал с trace.getKey()
		Trace trace = Heap.getAnyTraceByKey(key);
		return trace != null && key.equals(trace.getKey());
	}

	/**
	 * Делает указанную вторичную рефлектограму первичной,
	 * а старую первичную рефлектограмму - вторичной.
	 * @throws IllegalArgumentException если указанной вторичной рефлектограммы нет
	 */ 
	public static void setSecondaryTraceAsPrimary(String key) {
		Trace trace = Heap.getAnyTraceByKey(key);
		if (! isTraceSecondary(key))
			throw new IllegalArgumentException("There is no such secondary trace: " + key);
		Trace oldPrimary = getPrimaryTrace();

		// убираем вторичную из вторичных с оповещениями
		removeAnyTraceByKey(key);
		notifyBsHashRemove(key);

		// убираем первичную с оповещениями
		setAnyTraceByKey(PRIMARY_TRACE_KEY, null);
		notifyBsHashRemove(PRIMARY_TRACE_KEY);
		notifyPrimaryTraceClosed();

		// внимание - в этот момент нет первичной рефлектограммы
		// во избежание проблем, в этот момент ничего не отрисовываем

		// устанавливаем первичную
		setAnyTraceByKey(PRIMARY_TRACE_KEY, trace);

		// проводим анализ первичной с оповещениеями
		makePrimaryAnalysis();

		// устанавливаем вторичную
		putSecondaryTrace(oldPrimary);
	}

	public static String getEtalonName() {
		return etalonName;
	}

	// сбрасывает результат сравнения - вызывается при каждом
	// изменении эталона
	private static void removeEtalonComparison() {
		if (getEtalonComparison() != null) {
			setEtalonComparison(null);
		}
	}
}
