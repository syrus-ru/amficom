/*-
 * $Id: Heap.java,v 1.90 2005/07/21 17:13:15 saa Exp $
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
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.Marker;
import com.syrus.AMFICOM.Client.Analysis.UI.ReflectogrammLoadDialog;
import com.syrus.AMFICOM.Client.General.Command.Analysis.AnalysisCommand;
import com.syrus.AMFICOM.Client.General.Event.AnalysisParametersListener;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Event.CurrentEventChangeListener;
import com.syrus.AMFICOM.Client.General.Event.CurrentTraceChangeListener;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryMTAEListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryRefAnalysisListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryTraceListener;
import com.syrus.AMFICOM.Client.General.Event.RefMismatchListener;
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.Etalon;
import com.syrus.AMFICOM.analysis.EventAnchorer;
import com.syrus.AMFICOM.analysis.SimpleApplicationException;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.IncompatibleTracesException;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEvents;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatch;
import com.syrus.AMFICOM.analysis.dadara.ReliabilitySimpleReflectogramEventImpl;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEventComparer;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.AMFICOM.measurement.Result;
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
 * refMismatch.
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
 * 1. перед установкой эталона (setEtalon, setMTMEtalon)
 * должен устанавливаться setBSEtalonTrace
 * 
 * @author $Author: saa $
 * @version $Revision: 1.90 $, $Date: 2005/07/21 17:13:15 $
 * @module
 */
public class Heap
{
    // constants

	public static final String PRIMARY_TRACE_KEY = "primarytrace";
	public static final String ETALON_TRACE_KEY =  "etalon";
	public static final String REFERENCE_TRACE_KEY = "referencetrace"; // XXX - is referencetrace really required?
	public static final String MODELED_TRACE_KEY = "modeledtrace"; // trace got from modelling module

    // properties

    private static AnalysisParameters currentAP;
	private static AnalysisParameters defaultAP;
	private static AnalysisParameters initialAP;
	private static Map<String,Trace> traces = new HashMap<String,Trace>();
	private static RefAnalysis refAnalysisPrimary = null; // "refanalysis", PRIMARY_TRACE_KEY
	private static MeasurementSetup contextMeasurementSetup;	// AnalysisUtil.CONTEXT, "MeasurementSetup"
	private static Map<String,ReflectogrammLoadDialog> dialogHash = new HashMap<String,ReflectogrammLoadDialog>();	// "dialog", "*"

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

    private static SimpleReflectogramEventComparer rComp = null;

	private static String newMSName = null; // the name for newly created (unsaved) MeasurementSetup; null if no new MS

    private static Marker markerObject = null;

    private static ReflectogramMismatch refMismatch = null; // это еще не аларм, это - "несоответствие".

    // listeners

    private static LinkedList<BsHashChangeListener> bsHashChangedListeners = new LinkedList<BsHashChangeListener>();
    private static LinkedList<PrimaryTraceListener> primaryTraceListeners = new LinkedList<PrimaryTraceListener>();
    private static LinkedList<PrimaryMTAEListener> primaryMTAEListeners = new LinkedList<PrimaryMTAEListener>();
    private static LinkedList<PrimaryRefAnalysisListener> primaryRefAnalysisListeners = new LinkedList<PrimaryRefAnalysisListener>();
    private static LinkedList<EtalonMTMListener> etalonMTMListeners = new LinkedList<EtalonMTMListener>();
    private static LinkedList<CurrentTraceChangeListener> currentTraceChangeListeners = new LinkedList<CurrentTraceChangeListener>();
    private static LinkedList<CurrentEventChangeListener> currentEventChangeListeners = new LinkedList<CurrentEventChangeListener>();
    private static LinkedList<AnalysisParametersListener> analysisParametersListeners = new LinkedList<AnalysisParametersListener>();
    private static LinkedList<RefMismatchListener> refMismatchListeners = new LinkedList<RefMismatchListener>();

    // constructor is not available
    private Heap() {
        // not instantiable 
    }

    // methods

    public static ReflectogrammLoadDialog getRLDialogByKey(String key) {
        return dialogHash.get(key);
    }

    public static void setRLDialogByKey(String key,
            ReflectogrammLoadDialog dialog) {
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

    public static BellcoreStructure getAnyBSTraceByKey(String key) {
    	Trace trace = getAnyTraceByKey(key);
        return trace != null ? trace.getBS() : null;
    }

    public static BellcoreStructure getBSPrimaryTrace() {
        return getAnyBSTraceByKey(PRIMARY_TRACE_KEY);
    }

    /**
     * Устанавливает (заменяет) первичную рефлектограмму.
     * Предполагается использование этого метода для выбора первичной
     * р/г среди уже загруженных.
     */ 
    public static void setPrimaryTrace(Trace tr) {
    	setAnyTraceByKey(PRIMARY_TRACE_KEY, tr);
    }

    /**
     * Открывает рефлектограмму как первичную.
     * Автоматически закрывает все ранее открытые рефлектограммы.
     */ 
    private static void openPrimaryTrace(Trace tr) {
    	closeAll();
    	setAnyTraceByKey(PRIMARY_TRACE_KEY, tr);
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
     * Открывает рефлектограмму из результата.
     * Автоматически закрывает все ранее открытые рефлектограммы.
     * @param result Результат измерения с рефлектограммой
     * @throws SimpleApplicationException в результате нет рефлектограммы.
     *   В таком случае открытые на данный момент р/г не закрываются
     */
    public static void openPrimaryTraceFromResult(Result result)
    throws SimpleApplicationException {
    	openPrimaryTrace(new Trace(result,
    			getMinuitAnalysisParams()));
    }

    public static void openManyTracesFromResult(Set<Result> results)
    throws SimpleApplicationException {
    	// проверяем, что входной список непуст
    	if (results.isEmpty()) {
    		throw new IllegalArgumentException("empty set of results");
    	}

    	// Создаем Trace и Bellcore по каждому входному результату
    	Collection<Trace> traceColl = new ArrayList<Trace>(results.size());
    	Collection<BellcoreStructure> bsColl = new ArrayList<BellcoreStructure>(results.size());
    	for (Result res: results) {
    		Trace tr = new Trace(res, getMinuitAnalysisParams());
    		traceColl.add(tr);
    		bsColl.add(tr.getBS());
    	}

    	// пытаемся выбрать самую типичную рефлектограмму.
    	// если набор несовместен, используем первую попавшуюся.
    	Trace tracePrimary = null;
    	try {
			BellcoreStructure bs =
				CoreAnalysisManager.getMostTypicalTrace(bsColl);
			for (Trace tr: traceColl) {
				if (tr.getBS() == bs) {
					tracePrimary = tr;
					break;
				}
			}
			if (tracePrimary == null) {
				Log.debugMessage("Heap.openManyTracesFromResult | Failed to choose most typical trace as primary",
						Log.DEBUGLEVEL03);
				System.err.println("Failed to choose most typical trace as primary"); // FIXME: debug-time message
			} else {
			Log.debugMessage("Heap.openManyTracesFromResult | chosed most typical trace as primary",
					Log.DEBUGLEVEL07);
			}
		} catch (IncompatibleTracesException e) {
			// ignore for now: tracePrimary == null check will do processing
			Log.debugMessage("Heap.openManyTracesFromResult | incompatible traces, using first one",
					Log.DEBUGLEVEL07);
		}
		if (tracePrimary == null) {
			tracePrimary = traceColl.iterator().next();
		}

		// Открываем самую типичную как первичную
		openPrimaryTrace(tracePrimary);

		// Открываем все остальные как вторичные
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

    public static BellcoreStructure getBSEtalonTrace() {
        return getAnyBSTraceByKey(ETALON_TRACE_KEY);
    }

    public static void setEtalonTraceFromBS(BellcoreStructure etalonTrace) {
    	// @todo - эталон должен храниться отдельно от остальных (первичных и вторичных) рефлектограмм, и вообще не как Trace, а отдельно
        if (etalonTrace != null)
        	setAnyTraceByKey(ETALON_TRACE_KEY, new Trace(
        			etalonTrace,
        			ETALON_TRACE_KEY,
        			getMinuitAnalysisParams()));
        else
        	removeAnyTraceByKey(ETALON_TRACE_KEY);
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

    private static void updateCurrentTraceWhenBSRemoved() {
        if (!traces.containsKey(currentTrace))
            currentTrace = getFirstSecondaryBSKey();
        if (currentTrace == null)
            currentTrace = PRIMARY_TRACE_KEY;
        notifyCurrentTraceChanged();
    }

    public static boolean hasSecondaryBS() {
        return getFirstSecondaryBSKey() == null ? false : true;
    }

    public static AnalysisParameters getMinuitInitialParams() {
        return initialAP;
    }

    public static void setMinuitInitialParams(AnalysisParameters minuitInitialParams) {
        Heap.initialAP = minuitInitialParams;
    }

    public static AnalysisParameters getMinuitDefaultParams() {
        return defaultAP;
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

    public static Collection getBSCollection() {
    	Collection<BellcoreStructure> coll = new ArrayList<BellcoreStructure>(traces.size());
    	for (Trace tr: traces.values()) {
    		coll.add(tr.getBS());
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

    public static EventAnchorer getAnchorer() {
		return anchorer;
	}
	public static void setAnchorer(EventAnchorer anchorer) {
		Heap.anchorer = anchorer;
	}

    private static void removeAllBS() {
    	traces = new HashMap<String, Trace>();
    }

    public static void removeAnyBSByName(String id) {
    	removeAnyTraceByKey(id);
    }

    public static void setEtalonEtalonMetas(ParameterSet metas) {
        // @todo: may be required in Survey
        // Pool.put("etalon", ETALON, metas);
    }

    public static void setActiveContextActivePathIDToEmptyString() {
        // @todo: may be required in Survey
        // Pool.put("activecontext", "activepathid", "");
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

    public static SimpleReflectogramEventComparer getEventComparer() {
        return rComp;
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
    // notifyPrimaryTraceChanged -> primaryTraceCUpdated()
    private static void notifyBsHashAdd(String key) {
        Log.debugMessage("Heap.notifyBsHashAdd | key " + key, Level.FINEST);
        for (BsHashChangeListener listener: bsHashChangedListeners)
            listener.bsHashAdded(key);
    }

    //  primary trace всегда останется, пока есть другие
    private static void notifyBsHashRemove(String key) {
        Log.debugMessage("Heap.notifyBsHashRemove | key " + key, Level.FINEST);
        for (BsHashChangeListener listener: bsHashChangedListeners)
            listener.bsHashRemoved(key);
    }

    private static void notifyBsHashRemoveAll() {
        Log.debugMessage("Heap.notifyBsHashRemoveAll | ", Level.FINEST);
        for (BsHashChangeListener listener: bsHashChangedListeners)
            listener.bsHashRemovedAll();
    }

    /**
     * should also be suitable if primary trace completely replaced
     */
    public static void notifyPrimaryTraceOpened() {
        Log.debugMessage("Heap.notifyPrimaryTraceOpened | ", Level.FINEST);
        for (PrimaryTraceListener listener: primaryTraceListeners)
            listener.primaryTraceCUpdated();
    }

    public static void notifyPrimaryTraceClosed() {
        Log.debugMessage("Heap.notifyPrimaryTraceClosed | ", Level.FINEST);
        for (PrimaryTraceListener listener: primaryTraceListeners)
            listener.primaryTraceRemoved();
    }

    public static void notifyAnalysisParametersUpdated() {
        Log.debugMessage("Heap.notifyAnalysisParametersUpdated | ", Level.FINEST);
        // do not notify traces
        // notify subscribers
        for (AnalysisParametersListener listener: analysisParametersListeners)
            listener.analysisParametersUpdated();
    }

    private static void notifyRefMismatchCUpdated() {
        Log.debugMessage("Heap.notifyRefMismatchCUpdated | ", Level.FINEST);
        for (RefMismatchListener listener: refMismatchListeners)
            listener.refMismatchCUpdated();
    }

    private static void notifyRefMismatchRemoved() {
        Log.debugMessage("Heap.notifyRefMismatchRemoved | ", Level.FINEST);
        for (RefMismatchListener listener: refMismatchListeners)
            listener.refMismatchRemoved();
    }

    private static void notifyPrimaryMTAECUpdated() {
        Log.debugMessage("Heap.notifyPrimaryMTAECUpdated | ", Level.FINEST);
        for (PrimaryMTAEListener listener: primaryMTAEListeners)
            listener.primaryMTMCUpdated();
    }

    private static void notifyPrimaryMTAERemoved() {
        Log.debugMessage("Heap.notifyPrimaryMTAERemoved | ", Level.FINEST);
        for (PrimaryMTAEListener listener: primaryMTAEListeners)
            listener.primaryMTMRemoved();
    }

    private static void notifyPrimaryRefAnalysisCUpdated() {
        Log.debugMessage("Heap.notifyPrimaryRefAnalysisCUpdated | ",
                        Level.FINEST);
        for (PrimaryRefAnalysisListener listener: primaryRefAnalysisListeners)
            listener.primaryRefAnalysisCUpdated();
    }

    private static void notifyPrimaryRefAnalysisRemoved() {
        Log.debugMessage("Heap.notifyPrimaryRefAnalysisRemoved | ", Level.FINEST);
        for (PrimaryRefAnalysisListener listener: primaryRefAnalysisListeners)
            listener.primaryRefAnalysisRemoved();
    }

    private static void notifyEtalonMTMCUpdated() {
        Log.debugMessage("Heap.notifyEtalonMTMCUpdated | ", Level.FINEST);
        for (EtalonMTMListener listener: etalonMTMListeners)
            listener.etalonMTMCUpdated();
    }

    private static void notifyEtalonMTMRemoved() {
        Log.debugMessage("Heap.notifyEtalonMTMRemoved | ", Level.FINEST);
        for (EtalonMTMListener listener: etalonMTMListeners)
            listener.etalonMTMRemoved();
    }

    private static void notifyCurrentTraceChanged() {
        Log.debugMessage("Heap.notifyCurrentTraceChanged | currentTrace = " + currentTrace, Level.FINEST);
        for (CurrentTraceChangeListener listener: currentTraceChangeListeners)
            listener.currentTraceChanged(currentTrace);
    }

    private static void notifyCurrentEventChanged() {
        Log.debugMessage("Heap.notifyCurrentEventChanged | nEvent = (" + getCurrentEvent1() + ", " + getCurrentEtalonEvent1() + ")", Level.FINEST);
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

    public static void primaryTraceOpened() {
        notifyBsHashAdd(PRIMARY_TRACE_KEY);
        notifyPrimaryTraceOpened();
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
        ModelTraceAndEventsImpl pri = getMTAEPrimary();
        ModelTraceAndEventsImpl et = getMTMEtalon() != null ?
                getMTMEtalon().getMTAE() : null;
        rComp = pri != null && et != null
            ? new SimpleReflectogramEventComparer(pri, et)
            : null;
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
        notifyPrimaryRefAnalysisCUpdated();
        notifyPrimaryMTAECUpdated();
    }

    /**
     * Если устанавливается ненулевой MTM, к этому моменту уже должна
     * быть установлена рефлектограмма эталона, иначе возможны проблемы
     * в окне анализа, которое пока что полагается на то, что у отображенного
     * эталона непременно будет BS.
     * @param mtm may be null
     */
    public static void setMTMEtalon(ModelTraceManager mtm) {
        etalonMTM = mtm;
        fixEventList();
        setMTMBackupEtalon(mtm);
        if (mtm == null) {
            backupEtalonMTM = null;
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

    public static void putSecondaryTraceByKey(String key, BellcoreStructure bs) {
    	putSecondaryTrace(new Trace(
    			bs,
    			key,
    			getMinuitAnalysisParams()));
    }

    public static void setBSReferenceTrace(BellcoreStructure bs, String key) {
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
        // close Etalon MTM
        etalonMTM = null;
        backupEtalonMTM = null;
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
        BellcoreStructure bs = getBSPrimaryTrace();
        ModelTraceAndEventsImpl newMtae = ModelTraceAndEventsImpl.replaceRSE(
                mtae, out, bs.getTraceData());
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
        BellcoreStructure bs = getBSPrimaryTrace();
        ModelTraceAndEventsImpl newMtae = ModelTraceAndEventsImpl.replaceRSE(
                mtae, out, bs.getTraceData());
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
        BellcoreStructure bs = getBSPrimaryTrace();
        ModelTraceAndEventsImpl newMtae = ModelTraceAndEventsImpl.replaceRSE(
                mtae, out, bs.getTraceData());
        replacePrimaryAnalysisMTAE(newMtae);
    }
    
    private static void replacePrimaryAnalysisMTAE(
    		ModelTraceAndEventsImpl mtae) {
        setRefAnalysisPrimary(new RefAnalysis(getRefAnalysisPrimary(), mtae));
    }


	public static ReflectogramMismatch getRefMismatch() {
		return refMismatch;
	}

	public static void setRefMismatch(ReflectogramMismatch refMismatch) {
		Heap.refMismatch = refMismatch;
		if (refMismatch == null)
			notifyRefMismatchRemoved();
		else
			notifyRefMismatchCUpdated();
	}

	/*
     * ===============================================================
     * Other methods
     * ===============================================================
     */

	public static void makePrimaryAnalysis() {
		new AnalysisCommand().execute();
		primaryTraceOpened();
        if (refAnalysisPrimary.getMTAE().getNEvents() >= 0)
            currentEvent.toEvent(0); // (1)
        notifyCurrentEventChanged(); // (2)
        // операторы (1) и (2) делают текущим событие #0
		setCurrentTracePrimary();
	}

	public static void makeAnalysis() {
		BellcoreStructure bs = getBSPrimaryTrace();
		if (bs != null)
		{
	        RefAnalysis a = new RefAnalysis(bs);
			setRefAnalysisPrimary(a);
		}
	}

	public static void unSetEtalonPair() {
		setEtalonTraceFromBS(null);
		Heap.setAnchorer(null);
		Heap.setMTMEtalon(null);
	}

	public static void setEtalonPair(BellcoreStructure bs,
			Etalon etalonObj) {
		setEtalonTraceFromBS(bs);
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
}
