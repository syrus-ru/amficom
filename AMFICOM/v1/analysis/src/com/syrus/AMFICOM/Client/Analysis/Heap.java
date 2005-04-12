/*-
 * $Id: Heap.java,v 1.22 2005/04/12 18:05:31 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.UI.ReflectogrammLoadDialog;
import com.syrus.AMFICOM.Client.General.Event.CurrentTraceChangeListener;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryMTMListener;
import com.syrus.AMFICOM.Client.General.Event.bsHashChangeListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryTraceListener;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.io.BellcoreStructure;

/**
 * ��������� ������ ����������� ���� com.syrus.AMFICOM.Client.Resource.Pool
 * ���� ��� API ���� ������ ��������� - ��������, getAllBSMap() ������ ��������������
 * ������������� ��������� ������� ������ � BS
 * 
 * @author $Author: saa $
 * @version $Revision: 1.22 $, $Date: 2005/04/12 18:05:31 $
 * @module
 */
public class Heap
{
	public static final String PRIMARY_TRACE_KEY = "primarytrace";
	public static final String ETALON_TRACE_KEY =  "etalon";
	public static final String REFERENCE_TRACE_KEY = "referencetrace"; // XXX - is really required
	public static final String MODELED_TRACE_KEY = "modeledtrace"; // trace got from modelling module

	private static double[] minuitDefaultParams;	// OT_analysisparameters, OID_minuitdefaults
	private static double[] minuitAnalysisParams;	// OT_analysisparameters, OID_minuitanalysis
	private static double[] minuitInitialParams;	// OT_analysisparameters, OID_minuitinitials
	private static HashMap bsHash = new HashMap();	// "bellcorestructure", *
	private static HashMap refAnalysisHash = new HashMap();		// "refanalysis", *
	private static MeasurementSetup contextMeasurementSetup;	// AnalysisUtil.CONTEXT, "MeasurementSetup"
	private static Map bsBellCoreMap;				// "bellcoremap", "current": GUI-level BS hash; do not confuse with bsHash
	private static Double minTraceLevel;			// "min_trace_level", PRIMARY_TRACE_KEY
	private static HashMap dialogHash = new HashMap();	// "dialog", "*"
	private static ModelTraceAndEventsImpl primaryMTAE = null;
	private static ModelTraceManager etalonMTM = null;

	private static Map idColorMap = new HashMap();

	private static String currentTrace = ""; // XXX: initialize to avoid crushes

	public static ReflectogrammLoadDialog getRLDialogByKey(String key)
	{
		return (ReflectogrammLoadDialog) dialogHash.get(key);
	}
	public static void setRLDialogByKey(String key, ReflectogrammLoadDialog dialog)
	{
		dialogHash.put(key, dialog);
	}

	public static ModelTraceAndEventsImpl getMTAEPrimary() {
		return primaryMTAE;
	}
	public static ModelTraceManager getMTMEtalon() {
		return etalonMTM;
	}
	public static RefAnalysis getRefAnalysisByKey(String key) {
		return (RefAnalysis)refAnalysisHash.get(key);
	}
	public static void setRefAnalysisByKey(String key, RefAnalysis ra) {
		refAnalysisHash.put(key, ra);
	}
	public static BellcoreStructure getAnyBSTraceByKey(String key) {
		return (BellcoreStructure )bsHash.get(key);
	}
	public static BellcoreStructure getBSPrimaryTrace() {
		return (BellcoreStructure )bsHash.get(PRIMARY_TRACE_KEY);
	}
	public static void setBSPrimaryTrace(BellcoreStructure primaryTrace) {
		bsHash.put(PRIMARY_TRACE_KEY, primaryTrace);
	}
	public static BellcoreStructure getBSEtalonTrace() {
		return (BellcoreStructure )bsHash.get(ETALON_TRACE_KEY);
	}
	public static void setBSEtalonTrace(BellcoreStructure etalonTrace) {
		bsHash.put(ETALON_TRACE_KEY, etalonTrace);
	}
	public static BellcoreStructure getBSReferenceTrace() {
		return (BellcoreStructure )bsHash.get(REFERENCE_TRACE_KEY);
	}
	public static void setBSReferenceTrace(BellcoreStructure primaryTrace)
	{
		bsHash.put(REFERENCE_TRACE_KEY, primaryTrace);
	}
	public static boolean hasSecondaryBSKey(String id) {
		return bsHash.containsKey(id);
	}
	public static boolean hasEmptyAllBSMap() {
		return bsHash.isEmpty();
	}
	
	public static Color getColor(String id) {
		Color color = null;
//		System.out.println("id is '" + id + "'");
		color = (Color) idColorMap.get(id);
		if (color == null) {
			color = UIManager.getColor(id);
			if (color != null) {
				idColorMap.put(id, color);
			}
		}
		if (color == null) {
			int i = 0;
			String id1 = null;
			while (id1 == null) {
				id1 = AnalysisResourceKeys.COLOR_TRACE_PREFIX + i++;
//				System.out.println("search by " + id1);
				color = (Color) idColorMap.get(id1);
				if (color != null)
					id1 = null;
			}
//			System.out.println("by id:" + id1);
			color = UIManager.getColor(id1);
			if (color == null) {
				Random random = new Random();
//				System.out.println("by random");
				color = new Color(Math.abs(random.nextInt()) % 256, Math.abs(random.nextInt()) % 256, Math
						.abs(random.nextInt()) % 256);
			}
			idColorMap.put(id1, color);
			if (!id1.equals(id))
				idColorMap.put(id, color);
		}
//		System.out.println(color);
//		System.out.println();
		return color;
	}
	
	private static String getFirstSecondaryBSKey() {
		Iterator it = bsHash.keySet().iterator();
		while (it.hasNext())
		{
			String key = (String)it.next();
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
	}
	public static boolean hasSecondaryBS() {
		return getFirstSecondaryBSKey() == null ? false : true; 
	}

	public static double[] getMinuitInitialParams() {
		return minuitInitialParams;
	}
	public static void setMinuitInitialParams(double[] minuitInitialParams) {
		Heap.minuitInitialParams = minuitInitialParams;
	}
	public static double[] getMinuitDefaultParams() {
		return minuitDefaultParams;
	}
	public static void setMinuitDefaultParams(double[] minuitDefaults) {
		Heap.minuitDefaultParams = minuitDefaults;
	}
	public static double[] getMinuitAnalysisParams() {
		return minuitAnalysisParams;
	}
	public static void setMinuitAnalysisParams(double[] minuitAnalysisParams) {
		Heap.minuitAnalysisParams = minuitAnalysisParams;
	}
	public static MeasurementSetup getContextMeasurementSetup() {
		return contextMeasurementSetup;
	}
	public static void setContextMeasurementSetup(
			MeasurementSetup contextMeasurementSetup) {
		Heap.contextMeasurementSetup = contextMeasurementSetup;
	}
	public static Map getBsBellCoreMap() {
		return bsBellCoreMap;
	}
	public static void setBsBellCoreMap(Map bsBellCoreMap) {
		Heap.bsBellCoreMap = bsBellCoreMap;
	}
	public static Double getMinTraceLevel() {
		return minTraceLevel;
	}
	public static void setMinTraceLevel(Double minTraceLevel) {
		Heap.minTraceLevel = minTraceLevel;
	}
	public static void removeAllBS() {
		bsHash = new HashMap();
	}
	public static void removeAnyBSByName(String id) {
		bsHash.remove(id);
	}
	public static void setEtalonEtalonMetas(Set metas) {
		// @todo: may be required in Survey
		//Pool.put("etalon", ETALON, metas);
	}
	public static void setActiveContextActivePathIDToEmptyString() {
		// @todo: may be required in Survey
		//Pool.put("activecontext", "activepathid", "");
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

	private static LinkedList bsHashChangedListeners = new LinkedList();
	private static LinkedList primaryTraceListeners = new LinkedList();
	private static LinkedList primaryMTMListeners = new LinkedList();
	private static LinkedList etalonMTMListeners = new LinkedList();
	private static LinkedList CurrentTraceChangeListeners = new LinkedList();
	
	// XXX: change each notify method to private as soon as bsHash will become private

	// NB: if the primary trace is opened, then there are
	// two events generated:
	// notifyBsHashAdd -> bsHashAdded() and
	// notifyPrimaryTraceChanged -> primaryTraceCUpdated()
	public static void notifyBsHashAdd(String key, BellcoreStructure bs)
	{
		for (Iterator it = bsHashChangedListeners.iterator(); it.hasNext(); )
			((bsHashChangeListener)it.next()).bsHashAdded(key, bs);
	}
	public static void notifyBsHashRemove(String key) // primary trace ������ ���������
	{
		for (Iterator it = bsHashChangedListeners.iterator(); it.hasNext(); )
			((bsHashChangeListener)it.next()).bsHashRemoved(key);
	}
	public static void notifyBsHashRemoveAll() // ������� � primary trace, � ��� ���������
	{
		for (Iterator it = bsHashChangedListeners.iterator(); it.hasNext(); )
			((bsHashChangeListener)it.next()).bsHashRemovedAll();
	}
	public static void notifyPrimaryTraceChanged()
	{
		for (Iterator it = primaryTraceListeners.iterator(); it.hasNext(); )
		{
			if (bsHash.containsKey(PRIMARY_TRACE_KEY))
				((PrimaryTraceListener)it.next()).primaryTraceCUpdated();
			else
				((PrimaryTraceListener)it.next()).primaryTraceRemoved();
		}
	}
	public static void notifyPrimaryTraceOpened()
	{
		for (Iterator it = primaryTraceListeners.iterator(); it.hasNext(); )
			((PrimaryTraceListener)it.next()).primaryTraceCUpdated();
	}
	public static void notifyPrimaryTraceClosed()
	{
		for (Iterator it = primaryTraceListeners.iterator(); it.hasNext(); )
			((PrimaryTraceListener)it.next()).primaryTraceRemoved();
	}
	private static void notifyPrimaryMTMCUpdated()
	{
		for (Iterator it = primaryMTMListeners.iterator(); it.hasNext(); )
				((PrimaryMTMListener)it.next()).primaryMTMCUpdated();
	}
	private static void notifyPrimaryMTMRemoved()
	{
		for (Iterator it = primaryMTMListeners.iterator(); it.hasNext(); )
				((PrimaryMTMListener)it.next()).primaryMTMRemoved();
	}
	private static void notifyEtalonMTMCUpdated()
	{
		for (Iterator it = etalonMTMListeners.iterator(); it.hasNext(); )
				((EtalonMTMListener)it.next()).etalonMTMCUpdated();
	}
	private static void notifyEtalonMTMRemoved()
	{
		for (Iterator it = etalonMTMListeners.iterator(); it.hasNext(); )
				((EtalonMTMListener)it.next()).etalonMTMRemoved();
	}
	private static void notifyCurrentTraceChanged()
	{
		for (Iterator it = CurrentTraceChangeListeners.iterator(); it.hasNext(); )
			((CurrentTraceChangeListener)it.next()).currentTraceChanged(currentTrace);
	}

	private static void addListener(Collection c, Object listener)
	{
		if (!c.contains(listener))
			c.add(listener);
	}
	private static void removeListener(Collection c, Object listener)
	{
		if (c.contains(listener))
			c.remove(listener);
	}

	public static void addBsHashListener(bsHashChangeListener listener)
	{
		addListener(bsHashChangedListeners, listener);
	}
	public static void removeBsHashListener(bsHashChangeListener listener)
	{
		removeListener(bsHashChangedListeners, listener);
	}
	public static void addPrimaryTraceListener(PrimaryTraceListener listener)
	{
		addListener(primaryTraceListeners, listener);
	}
	public static void removePrimaryTraceListener(PrimaryTraceListener listener)
	{
		removeListener(primaryTraceListeners, listener);
	}
	public static void addPrimaryMTMListener(PrimaryMTMListener listener)
	{
		addListener(primaryMTMListeners, listener);
	}
	public static void removePrimaryMTMListener(PrimaryMTMListener listener)
	{
		removeListener(primaryMTMListeners, listener);
	}
	public static void addEtalonMTMListener(EtalonMTMListener listener)
	{
		addListener(etalonMTMListeners, listener);
	}
	public static void removeEtalonMTMListener(EtalonMTMListener listener)
	{
		removeListener(etalonMTMListeners, listener);
	}
	public static void addCurrentTraceChangeListener(CurrentTraceChangeListener listener)
	{
		addListener(CurrentTraceChangeListeners, listener);
	}
	public static void removeCurrentTraceChangeListener(CurrentTraceChangeListener listener)
	{
		removeListener(CurrentTraceChangeListeners, listener);
	}

	public static void primaryTraceOpened(BellcoreStructure bs)
	{
		notifyBsHashAdd(PRIMARY_TRACE_KEY, bs);
		notifyPrimaryTraceOpened();
	}
	public static void traceClosed(String key)
	{
		notifyBsHashRemove(key);
		if (key.equals(PRIMARY_TRACE_KEY))
			notifyPrimaryTraceClosed();
	}
	public static void traceOpened(String key, BellcoreStructure bs)
	{
		notifyBsHashAdd(key, bs);
		if (key.equals(PRIMARY_TRACE_KEY))
			notifyPrimaryTraceOpened();
	}
    /*
     * methods that both make changed and notify appropriate listeners 
     */
	public static void setCurrentTrace(String id)
	{
		currentTrace = id;
		notifyCurrentTraceChanged();
	}
	public static void setCurrentTracePrimary()
	{
		currentTrace = PRIMARY_TRACE_KEY;
		notifyCurrentTraceChanged();
	}
	public static void setMTAEPrimary(ModelTraceAndEventsImpl mtae) {
		primaryMTAE = mtae;
		if (mtae == null)
			notifyPrimaryMTMCUpdated();
		else
			notifyPrimaryMTMRemoved();
	}
	public static void setMTMEtalon(ModelTraceManager mtm)
	{
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
}
