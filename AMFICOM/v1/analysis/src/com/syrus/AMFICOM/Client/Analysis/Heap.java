/*-
 * $Id: Heap.java,v 1.41 2005/04/29 10:51:46 saa Exp $
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
import com.syrus.AMFICOM.Client.General.Event.PrimaryTraceListener;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramComparer;
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
 * refAnalysisPrimary;
 * contextMeasurementSetup;
 * minTraceLevelt;
 * rLDialog{};
 * newMSName
 * 
 * Свойства, по которым уведомления предусмотрены, но не систематизированы
 * (и не гарантированы):
 * bsHash{}
 * 
 * Свойства с полным отслеживанием и уведомлениями:
 * primaryMTAE;
 * etalonMTM;
 * currentTrace;
 * currentEvent, currentEtalonEvent;
 * 
 * @author $Author: saa $
 * @version $Revision: 1.41 $, $Date: 2005/04/29 10:51:46 $
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

    private static AnalysisParameters minuitAnalysisParams;
	private static AnalysisParameters minuitDefaultParams;
	private static AnalysisParameters minuitInitialParams;
	private static HashMap bsHash = new HashMap();	// "bellcorestructure", *
	private static RefAnalysis refAnalysisPrimary = null; // "refanalysis", PRIMARY_TRACE_KEY
	private static MeasurementSetup contextMeasurementSetup;	// AnalysisUtil.CONTEXT, "MeasurementSetup"
	private static Double minTraceLevel;			// "min_trace_level", PRIMARY_TRACE_KEY
	private static HashMap dialogHash = new HashMap();	// "dialog", "*"
	private static ModelTraceAndEventsImpl primaryMTAE = null;
	private static ModelTraceManager etalonMTM = null;

	private static String currentTrace = ""; // XXX: initialize to avoid crushes
	private static int currentEv = -1; // XXX: is this initialization good?

	private static String newMSName = null; // the name for newly created (unsaved) MeasurementSetup; null if no new MS

    // listeners 

    private static LinkedList bsHashChangedListeners = new LinkedList();
    private static LinkedList primaryTraceListeners = new LinkedList();
    private static LinkedList primaryMTAEListeners = new LinkedList();
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
        return primaryMTAE;
    }

    private static int getNumberOfEvents() {
    	return primaryMTAE == null ? 0 : primaryMTAE.getNEvents();
    }

    public static ModelTraceManager getMTMEtalon() {
        return etalonMTM;
    }

    public static RefAnalysis getRefAnalysisPrimary() {
        return refAnalysisPrimary;
    }

    public static void setRefAnalysisPrimary(RefAnalysis ra) {
        refAnalysisPrimary = ra;
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
        bsHash.put(ETALON_TRACE_KEY, etalonTrace);
    }

    public static BellcoreStructure getBSReferenceTrace() {
        return (BellcoreStructure) bsHash.get(REFERENCE_TRACE_KEY);
    }

    public static boolean hasSecondaryBSKey(String id) {
        return bsHash.containsKey(id);
    }

    public static int getCurrentEvent() {
    	return currentEv;
    }

	// XXX: rather slow...
    public static int getCurrentEtalonEvent() {
    	if (primaryMTAE == null || etalonMTM == null || currentEv < 0 || currentEv >= primaryMTAE.getNEvents())
    		return -1;
    	// reliability comparison is actually performed
    	// @todo: ModelTraceAndEventsImpl: add getReliabilitySimpleEvents()
    	ReflectogramComparer rcomp = new ReflectogramComparer(primaryMTAE.getSimpleEvents(),
    		etalonMTM.getRSE());
    	return rcomp.getEtalonIdByProbeId(currentEv);
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
        return minuitInitialParams;
    }

    public static void setMinuitInitialParams(AnalysisParameters minuitInitialParams) {
        Heap.minuitInitialParams = minuitInitialParams;
    }

    public static AnalysisParameters getMinuitDefaultParams() {
        return minuitDefaultParams;
    }

    public static void setMinuitDefaultParams(AnalysisParameters minuitDefaults) {
        Heap.minuitDefaultParams = minuitDefaults;
    }

    public static AnalysisParameters getMinuitAnalysisParams() {
        return minuitAnalysisParams;
    }

    public static void setMinuitAnalysisParams(AnalysisParameters minuitAnalysisParams) {
        Heap.minuitAnalysisParams = minuitAnalysisParams;
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

    public static Double getMinTraceLevel() {
        return minTraceLevel;
    }

    public static void setMinTraceLevel(Double minTraceLevel) {
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

    public static boolean hasEventParamsForPrimaryTrace() // XXX
    {
        return getMTAEPrimary() != null; // XXX
    }

    public static boolean hasEventParamsForEtalonTrace() // XXX
    {
        return getMTMEtalon() != null; // XXX
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

    private static void notifyPrimaryMTMCUpdated() {
        for (Iterator it = primaryMTAEListeners.iterator(); it.hasNext();)
            ((PrimaryMTAEListener) it.next()).primaryMTMCUpdated();
    }

    private static void notifyPrimaryMTAERemoved() {
        for (Iterator it = primaryMTAEListeners.iterator(); it.hasNext();)
            ((PrimaryMTAEListener) it.next()).primaryMTMRemoved();
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
     * methods that both make changed and notify appropriate listeners 
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
    	if (nEvent < -1 || nEvent >= getNumberOfEvents())
    		return;
    	currentEv = nEvent;
    	notifyCurrentEventChanged();
    }

    public static void setCurrentEtalonEvent(int nEtEvent) {
    	// @todo: store a pair {currentEvent, currentEtalonEvent} instead of lossy converting etalonEvent to event 
    	if (primaryMTAE == null || etalonMTM == null || nEtEvent < 0 || nEtEvent >= etalonMTM.getMTAE().getNEvents()) {
    		setCurrentEvent(-1);
    		return;
    	}
    	else {
        	ReflectogramComparer rcomp = new ReflectogramComparer(primaryMTAE.getSimpleEvents(),
        		etalonMTM.getRSE());
        	setCurrentEvent(rcomp.getProbeIdByEtalonId(nEtEvent));
    	}
    }

    // @todo: invoke this automatically
    private static void fixCurrentEvent() {
    	if (currentEv >= getNumberOfEvents())
    	{
    		currentEv = -1;
    		notifyCurrentEventChanged();
    	}
    }

    /**
     * @param mtae  must not be null
     */
    public static void setMTAEPrimary(ModelTraceAndEventsImpl mtae) {
        primaryMTAE = mtae;
        fixCurrentEvent();
        notifyPrimaryMTMCUpdated();
    }

    /**
     * 
     * @param mtm may be null
     */
    public static void setMTMEtalon(ModelTraceManager mtm) {
        etalonMTM = mtm;
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
     * closes all BS traces, primary MTAE and etalon MTM 
     */
    public static void closeAll() {
        // close Etalon MTM
        etalonMTM = null;
        notifyEtalonMTMRemoved();

        // close all BS
        removeAllBS();
        notifyBsHashRemoveAll();
        
        // close Primary MTAE
        primaryMTAE = null;
        fixCurrentEvent();
        notifyPrimaryMTAERemoved();
    }
}
