/*-
 * $Id: Heap.java,v 1.58 2005/05/06 11:27:41 saa Exp $
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
 * @version $Revision: 1.58 $, $Date: 2005/05/06 11:27:41 $
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

    /*
     * ===============================================================
     * Other methods
     * ===============================================================
     */
}
