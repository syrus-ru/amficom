/*-
 * $Id: Heap.java,v 1.1 2005/03/29 16:00:51 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis;

import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.Client.Analysis.UI.ReflectogrammLoadDialog;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.RefAnalysis;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.io.BellcoreStructure;

/**
 * Временная замена клиентскому пулу com.syrus.AMFICOM.Client.Resource.Pool
 * пока что API этой замены избыточна - например, getAllBSMap() делает необязательным
 * использование остальных методов работы с BS
 * 
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/03/29 16:00:51 $
 * @module
 */
public class Heap
{
	public static final String PRIMARY_TRACE_KEY = "primarytrace";
	public static final String ETALON_TRACE_KEY =  "etalon";
	public static final String REFERENCE_TRACE_KEY = "referencetrace"; // XXX - is really required

	private static double[] minuitDefaultParams;	// OT_analysisparameters, OID_minuitdefaults
	private static double[] minuitAnalysisParams;	// OT_analysisparameters, OID_minuitanalysis
	private static double[] minuitInitialParams;	// OT_analysisparameters, OID_minuitinitials
	private static HashMap bsHash = new HashMap();	// "bellcorestructure", *
	private static HashMap refAnalysisHash = new HashMap();		// "refanalysis", *
	private static HashMap MTMHash = new HashMap();	// ModelTraceManager.CODENAME, *
	private static MeasurementSetup contextMeasurementSetup;	// AnalysisUtil.CONTEXT, "MeasurementSetup"
	private static Map bsBellCoreMap; // "bellcoremap", "current": GUI-level BS hash; do not confuse with bsHash
	private static Double minTraceLevel;			// "min_trace_level", PRIMARY_TRACE_KEY
	private static HashMap dialogHash = new HashMap();	// "dialog", "*"
	
	public static ReflectogrammLoadDialog getRLDialogByKey(String key)
	{
		return (ReflectogrammLoadDialog) dialogHash.get(key);
	}
	public static void setRLDialogByKey(String key, ReflectogrammLoadDialog dialog)
	{
		dialogHash.put(key, dialog);
	}

	public static ModelTraceManager getMTMByKey(String key)
	{
		return (ModelTraceManager)MTMHash.get(key);
	}
	public static void setMTMByKey(String key, ModelTraceManager mtm)
	{
		MTMHash.put(key, mtm);
	}
	public static RefAnalysis getRefAnalysisByKey(String key)
	{
		return (RefAnalysis)refAnalysisHash.get(key);
	}
	public static void setRefAnalysisByKey(String key, RefAnalysis ra)
	{
		refAnalysisHash.put(key, ra);
	}
	public static BellcoreStructure getAnyBSTraceByKey(String key)
	{
		return (BellcoreStructure )bsHash.get(key);
	}
	public static void setAnyBSTraceByKey(String key, BellcoreStructure bs)
	{
		bsHash.put(key, bs);
	}
	public static void putSecondaryTraceByKey(String key, BellcoreStructure bs)
	{
		bsHash.put(key, bs);
	}
	public static BellcoreStructure getBSPrimaryTrace()
	{
		return (BellcoreStructure )bsHash.get(PRIMARY_TRACE_KEY);
	}
	public static void setBSPrimaryTrace(BellcoreStructure primaryTrace)
	{
		bsHash.put(PRIMARY_TRACE_KEY, primaryTrace);
	}
	public static BellcoreStructure getBSReferenceTrace()
	{
		return (BellcoreStructure )bsHash.get(REFERENCE_TRACE_KEY);
	}
	public static void setBSReferenceTrace(BellcoreStructure primaryTrace)
	{
		bsHash.put(REFERENCE_TRACE_KEY, primaryTrace);
	}
	public static void setBSEtalonTrace(BellcoreStructure etalonTrace)
	{
		bsHash.put(ETALON_TRACE_KEY, etalonTrace);
	}
	public static Map getAllBSMap()
	{
		return bsHash;
	}
	public static boolean hasEmptyAllBSMap()
	{
		return getAllBSMap().isEmpty();
	}

	public static double[] getMinuitInitialParams()
	{
		return minuitInitialParams;
	}
	public static void setMinuitInitialParams(double[] minuitInitialParams)
	{
		Heap.minuitInitialParams = minuitInitialParams;
	}
	public static double[] getMinuitDefaultParams()
	{
		return minuitDefaultParams;
	}
	public static void setMinuitDefaultParams(double[] minuitDefaults)
	{
		Heap.minuitDefaultParams = minuitDefaults;
	}
	public static double[] getMinuitAnalysisParams()
	{
		return minuitAnalysisParams;
	}
	public static void setMinuitAnalysisParams(double[] minuitAnalysisParams)
	{
		Heap.minuitAnalysisParams = minuitAnalysisParams;
	}
	public static MeasurementSetup getContextMeasurementSetup()
	{
		return contextMeasurementSetup;
	}
	public static void setContextMeasurementSetup(
			MeasurementSetup contextMeasurementSetup)
	{
		Heap.contextMeasurementSetup = contextMeasurementSetup;
	}
	public static Map getBsBellCoreMap()
	{
		return bsBellCoreMap;
	}
	public static void setBsBellCoreMap(Map bsBellCoreMap)
	{
		Heap.bsBellCoreMap = bsBellCoreMap;
	}
	public static Double getMinTraceLevel()
	{
		return minTraceLevel;
	}
	public static void setMinTraceLevel(Double minTraceLevel)
	{
		Heap.minTraceLevel = minTraceLevel;
	}
	public static void removeAllBS()
	{
		bsHash = new HashMap();
	}
	public static void removeAnyBSByName(String id)
	{
		bsHash.remove(id);
	}
	public static void setEtalonEtalonMetas(Set metas)
	{
		// @todo: may be required in Survey
		//Pool.put("etalon", ETALON, metas);
	}
	public static void setActiveContextActivePathIDToEmptyString()
	{
		// @todo: may be required in Survey
		//Pool.put("activecontext", "activepathid", "");
	}
	public static boolean hasEventParamsForPrimaryTrace() // XXX
	{
		return getMTMByKey(PRIMARY_TRACE_KEY) != null; // XXX
	}
	public static boolean hasEventParamsForEtalonTrace() // XXX
	{
		return getMTMByKey(ETALON_TRACE_KEY) != null; // XXX
	}
	public static boolean hasEventParamsForTrace(String key) // XXX
	{
		return getMTMByKey(key) != null; // XXX
	}
}
