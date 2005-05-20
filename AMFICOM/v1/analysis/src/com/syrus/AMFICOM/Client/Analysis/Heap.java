/*-
 * $Id: Heap.java,v 1.60 2005/05/20 18:03:39 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
 
package com.syrus.AMFICOM.Client.Analysis;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.Marker;
import com.syrus.AMFICOM.Client.Analysis.UI.ReflectogrammLoadDialog;
import com.syrus.AMFICOM.Client.General.Event.BsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Event.CurrentEventChangeListener;
import com.syrus.AMFICOM.Client.General.Event.CurrentTraceChangeListener;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryMTAEListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryRefAnalysisListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryTraceListener;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.AMFICOM.analysis.dadara.ReliabilitySimpleReflectogramEventImpl;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEventComparer;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.io.BellcoreStructure;

/**
 * Временная замена клиентскому пулу com.syrus.AMFICOM.Client.Resource.Pool
 * пока что API этой замены избыточна - например, getAllBSMap() делает необязательным
 * использование остальных методов работы с BS
 * 
 * Обозначение: {} = хеш.
 * 
 * Обладает свойствами, по которым не обеспечивает уведомлений:
 * minuitAnalysisParams, minuitDefaultParams, minuitInitialParams;
 * contextMeasurementSetup;
 * minTraceLevelt;
 * rLDialog{};
 * backupEtalonMTM;
 * newMSName
 * setMarkerObject() / hasMarkerPosition() / getMarkerPosition()
 * 
 * Свойства, по которым уведомления предусмотрены, но не систематизированы
 * (и не гарантированы):
 * bsHash{}
 * 
 * Свойства с полным отслеживанием и уведомлениями:
 * refAnalysisPrimary;
 * etalonMTM;
 * currentTrace;
 * currentEvent, currentEtalonEvent;
 *
 * Кроме того, есть свойство primaryMTAE, которое изменяется вместе и только
 * вместе с refAnalysisPrimary; по его изменению тоже рассылаются уведомления.
 * Рассылка происходит одновременно с рассылкой для refAnalysisPrimary,
 * т.е. достаточно подписаться только на одно из них. Рекомендуется
 * подписываться на primaryMTAE в тех случаях, когда refAnalysisPrimary не
 * нужен, а на refAnalysisPrimary - в случаях, когда refAnalysisPrimary нужен.
 * Фактически, primaryMTAE - это часть refAnalysisPrimary.
 * 
 * @author $Author: saa $
 * @version $Revision: 1.60 $, $Date: 2005/05/20 18:03:39 $
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
	private static HashMap bsHash = new HashMap();	// "bellcorestructure", *
	private static RefAnalysis refAnalysisPrimary = null; // "refanalysis", PRIMARY_TRACE_KEY
	private static MeasurementSetup contextMeasurementSetup;	// AnalysisUtil.CONTEXT, "MeasurementSetup"
	private static double minTraceLevel;			// (negative value)
	private static HashMap dialogHash = new HashMap();	// "dialog", "*"
    private static ModelTraceManager etalonMTM = null;
    private static ModelTraceManager backupEtalonMTM = null; // 'initial' state of etalon MTM

	private static String currentTrace = ""; // XXX: initialize to avoid crushes
	//private static int currentEv = -1;
    private static CompositeEventList eventList = new CompositeEventList(false);
    private static CompositeEventList.Walker currentEvent =
        eventList.new Walker();

    private static SimpleReflectogramEventComparer rComp = null;

	private static String newMSName = null; // the name for newly created (unsaved) MeasurementSetup; null if no new MS

    private static Marker markerObject = null;

    // listeners

    private static LinkedList bsHashChangedListeners = new LinkedList();
    private static LinkedList primaryTraceListeners = new LinkedList();
    private static LinkedList primaryMTAEListeners = new LinkedList();
    private static LinkedList primaryRefAnalysisListeners = new LinkedList();
    private static LinkedList etalonMTMListeners = new LinkedList();
    private static LinkedList currentTraceChangeListeners = new LinkedList();
    private static LinkedList currentEventChangeListeners = new LinkedList();
    
    // methods

    public static ReflectogrammLoadDialog getRLDialogByKey(String key) {
        return (ReflectogrammLoadDialog) dialogHash.get(key);
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

    public static BellcoreStructure getAnyBSTraceByKey(String key) {
        return (BellcoreStructure) bsHash.get(key);
    }

    public static BellcoreStructure getBSPrimaryTrace() {
        return (BellcoreStructure) bsHash.get(PRIMARY_TRACE_KEY);
    }

    public static void setBSPrimaryTrace(BellcoreStructure primaryTrace) {
        bsHash.put(PRIMARY_TRACE_KEY, primaryTrace);
    }

    public static BellcoreStructure getBSEtalonTrace() {
        return (BellcoreStructure) bsHash.get(ETALON_TRACE_KEY);
    }

    public static void setBSEtalonTrace(BellcoreStructure etalonTrace) {
        if (etalonTrace != null)
            bsHash.put(ETALON_TRACE_KEY, etalonTrace);
        else
            bsHash.remove(ETALON_TRACE_KEY);
    }

    public static BellcoreStructure getBSReferenceTrace() {
        return (BellcoreStructure) bsHash.get(REFERENCE_TRACE_KEY);
    }

    public static boolean hasSecondaryBSKey(String id) {
        return bsHash.containsKey(id);
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
        return bsHash.isEmpty();
    }

    private static String getFirstSecondaryBSKey() {
        Iterator it = bsHash.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (key == PRIMARY_TRACE_KEY)
                continue;
            return key;
        }
        return null;
    }

    public static void updateCurrentTraceWhenBSRemoved() {
        if (!bsHash.containsKey(currentTrace))
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
    	return Collections.unmodifiableCollection(bsHash.values());
    }

    public static double getMinTraceLevel() {
        return minTraceLevel;
    }

    public static void setMinTraceLevel(double minTraceLevel) {
        Heap.minTraceLevel = minTraceLevel;
    }

    private static void removeAllBS() {
        bsHash = new HashMap();
    }

    public static void removeAnyBSByName(String id) {
        bsHash.remove(id);
    }

    public static void setEtalonEtalonMetas(Set metas) {
        // @todo: may be required in Survey
        // Pool.put("etalon", ETALON, metas);
    }

    public static void setActiveContextActivePathIDToEmptyString() {
        // @todo: may be required in Survey
        // Pool.put("activecontext", "activepathid", "");
    }

    public static boolean hasEventParamsForPrimaryTrace()
    {
        return getMTAEPrimary() != null; // XXX
    }

    public static boolean hasEventParamsForEtalonTrace()
    {
        return getMTMEtalon() != null; // XXX
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

    // XXX: change each notify method to private as soon as bsHash will become
    // private

    // NB: if the primary trace is opened, then there are
    // two events generated:
    // notifyBsHashAdd -> bsHashAdded() and
    // notifyPrimaryTraceChanged -> primaryTraceCUpdated()
    public static void notifyBsHashAdd(String key, BellcoreStructure bs) {
        for (Iterator it = bsHashChangedListeners.iterator(); it.hasNext();)
            ((BsHashChangeListener) it.next()).bsHashAdded(key, bs);
    }

    //  primary trace всегда останется
    public static void notifyBsHashRemove(String key) {
        for (Iterator it = bsHashChangedListeners.iterator(); it.hasNext();)
            ((BsHashChangeListener) it.next()).bsHashRemoved(key);
    }

    private static void notifyBsHashRemoveAll() {
        for (Iterator it = bsHashChangedListeners.iterator(); it.hasNext();)
            ((BsHashChangeListener) it.next()).bsHashRemovedAll();
    }

    /**
     * should also be suitable if primary trace completely replaced
     */
    public static void notifyPrimaryTraceOpened() {
        for (Iterator it = primaryTraceListeners.iterator(); it.hasNext();)
            ((PrimaryTraceListener) it.next()).primaryTraceCUpdated();
    }

    public static void notifyPrimaryTraceClosed() {
        for (Iterator it = primaryTraceListeners.iterator(); it.hasNext();)
            ((PrimaryTraceListener) it.next()).primaryTraceRemoved();
    }

    private static void notifyPrimaryMTAECUpdated() {
        for (Iterator it = primaryMTAEListeners.iterator(); it.hasNext();)
            ((PrimaryMTAEListener) it.next()).primaryMTMCUpdated();
    }

    private static void notifyPrimaryMTAERemoved() {
        for (Iterator it = primaryMTAEListeners.iterator(); it.hasNext();)
            ((PrimaryMTAEListener) it.next()).primaryMTMRemoved();
    }

    private static void notifyPrimaryRefAnalysisCUpdated() {
        for (Iterator it = primaryRefAnalysisListeners.iterator(); it.hasNext();)
            ((PrimaryRefAnalysisListener)it.next()).primaryRefAnalysisCUpdated();
    }

    private static void notifyPrimaryRefAnalysisRemoved() {
        for (Iterator it = primaryRefAnalysisListeners.iterator(); it.hasNext();)
            ((PrimaryRefAnalysisListener)it.next()).primaryRefAnalysisRemoved();
    }

    private static void notifyEtalonMTMCUpdated() {
        for (Iterator it = etalonMTMListeners.iterator(); it.hasNext();)
            ((EtalonMTMListener) it.next()).etalonMTMCUpdated();
    }

    private static void notifyEtalonMTMRemoved() {
        for (Iterator it = etalonMTMListeners.iterator(); it.hasNext();)
            ((EtalonMTMListener) it.next()).etalonMTMRemoved();
    }

    private static void notifyCurrentTraceChanged() {
        for (Iterator it = currentTraceChangeListeners.iterator(); it.hasNext();)
            ((CurrentTraceChangeListener) it.next())
                    .currentTraceChanged(currentTrace);
    }

    private static void notifyCurrentEventChanged() {
        for (Iterator it = currentEventChangeListeners.iterator(); it.hasNext();)
            ((CurrentEventChangeListener) it.next())
                    .currentEventChanged();
    }

    private static void addListener(Collection c, Object listener) {
        if (!c.contains(listener))
            c.add(listener);
    }

    private static void removeListener(Collection c, Object listener) {
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

    public static void primaryTraceOpened(BellcoreStructure bs) {
        notifyBsHashAdd(PRIMARY_TRACE_KEY, bs);
        notifyPrimaryTraceOpened();
    }

    public static void traceClosed(String key) {
        notifyBsHashRemove(key);
        if (key.equals(PRIMARY_TRACE_KEY))
            notifyPrimaryTraceClosed();
    }

    /*
     * ===============================================================
     * methods that both make changed and notify appropriate listeners
     * =============================================================== 
     */

    public static void setCurrentTrace(String id) {
        currentTrace = id;
        notifyCurrentTraceChanged();
    }

    public static void setCurrentTracePrimary() {
        currentTrace = PRIMARY_TRACE_KEY;
        notifyCurrentTraceChanged();
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
     * 
     * @param mtm may be null
     */
    public static void setMTMEtalon(ModelTraceManager mtm) {
        etalonMTM = mtm;
        fixEventList();
        setMTMBackupEtalon(mtm);
        if (mtm == null)
            notifyEtalonMTMRemoved();
        else
            notifyEtalonMTMCUpdated();
    }

    public static void putSecondaryTraceByKey(String key, BellcoreStructure bs) {
        bsHash.put(key, bs);
        notifyBsHashAdd(key, bs);
    }

    public static void setBSReferenceTrace(BellcoreStructure bs) {
        bsHash.put(REFERENCE_TRACE_KEY, bs);
        notifyBsHashAdd(REFERENCE_TRACE_KEY, bs);
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
                        in[i].getEventType(),
                        -1.0)
                : in[i + 1];
        }
        BellcoreStructure bs = getBSPrimaryTrace();
        ModelTraceAndEventsImpl newMtae = ModelTraceAndEventsImpl.replaceRSE(
                mtae, out, bs.getTraceData());
        setRefAnalysisPrimary(new RefAnalysis(getBSPrimaryTrace(), newMtae));
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
                    in[nEvent].getEventType(),
                    -1.0);
        }
        for (int i = nEvent + 1; i < in.length; i++) {
                out[i + n - 1] = in[i];
        }
        BellcoreStructure bs = getBSPrimaryTrace();
        ModelTraceAndEventsImpl newMtae = ModelTraceAndEventsImpl.replaceRSE(
                mtae, out, bs.getTraceData());
        setRefAnalysisPrimary(new RefAnalysis(getBSPrimaryTrace(), newMtae));
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
                        newType,
                        -1.0));
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
                        mtae.getSimpleEvent(nEvent).getEventType(),
                        -1.0));
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
                        mtae.getSimpleEvent(nEvent).getEventType(),
                        -1.0));
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
                        in[i].getEventType(),
                        -1.0);
            } else if (i == nEvent + 1 && in[i].getBegin() != ev.getEnd()) {
                out[i] = new ReliabilitySimpleReflectogramEventImpl(
                        ev.getEnd(),
                        in[i].getEnd(),
                        in[i].getEventType(),
                        -1.0);
            } else {
                out[i] = in[i];
            }
        }
        BellcoreStructure bs = getBSPrimaryTrace();
        ModelTraceAndEventsImpl newMtae = ModelTraceAndEventsImpl.replaceRSE(
                mtae, out, bs.getTraceData());
        setRefAnalysisPrimary(new RefAnalysis(getBSPrimaryTrace(), newMtae));
    }

    /*
     * ===============================================================
     * Other methods
     * ===============================================================
     */
}
