package com.syrus.AMFICOM.Client.Analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.StringFieldSort;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.measurement.corba.SetSort;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreWriter;
import com.syrus.util.ByteArray;

/**
 Class with methods used to save/load measuring parameters onto server
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Syrus Systems</p>
 * @author Stanislav Kholshin
 * @version 1.0
 */

public class AnalysisUtil
{
	public static final String ETALON = "etalon";
	public static final String CONTEXT = "analysiscontext";
	private static final String OT_analysisparameters = "analysisparameters";
	private static final String OID_minuitanalysis = "minuitanalysis";
	private static final String OID_minuitinitials = "minuitinitials";
	private static final String OID_minuitdefaults = "minuitdefaults";

	private AnalysisUtil()
	{
	}

	public static ParameterType getParameterType(Identifier userId, String codename, DataType dataType){
		StorableObjectCondition pTypeCondition = new TypicalCondition(
				codename,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);

		ParameterType parameterType = null;
		try	{
				try{
			Collection pTypes = MeasurementStorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);
			for (Iterator it = pTypes.iterator(); it.hasNext();){
				ParameterType type = (ParameterType)it.next();
				if (type.getCodename().equals(codename)){
						parameterType = type;
						break;
				}
			}
				}catch(ApplicationException roe){
				// ...
					System.err.println("Exception searching ParameterType. Creating new one.");
				roe.printStackTrace();
				}

			if (parameterType == null){
					parameterType = ParameterType.createInstance(
					userId,
					codename,
					codename + "_Description",
					codename + "_Name", // by saa after a talk with bob
					dataType); 
					MeasurementStorableObjectPool.putStorableObject(parameterType);
			}
		}
		catch(ApplicationException ex)
		{
			// ...
				System.err.println("Exception handling ParameterType. Giving up.");
			ex.printStackTrace();
		}
		return parameterType;
	}

	/*
	public static CharacteristicType getCharacteristicType(Identifier userId, String codename, CharacteristicTypeSort sort, DataType dataType)
	{
		StorableObjectCondition pTypeCondition = new StringFieldCondition(
				codename,
				ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE,
				StringFieldSort.STRINGSORT_BASE);

		try
		{
			List pTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);
			for (Iterator it = pTypes.iterator(); it.hasNext();)
			{
				CharacteristicType type = (CharacteristicType)it.next();
				if (type.getCodename().equals(codename))
					return type;
			}
		}
		catch(ApplicationException ex)
		{
			System.err.println("Exception searching ParameterType. Creating new one.");
			ex.printStackTrace();
		}

		try
		{
				return CharacteristicType.createInstance(
				userId,
				codename,
				"",
				dataType.value(),
				sort);
		}
		catch(CreateObjectException e)
		{
				// FIXME
				System.err.println("AnalysisUtil.getCharacteristicType: Exception in createInstance. Wanna die.");
				e.printStackTrace();
				return null;
		}
	}
	*/

	public static AnalysisType getAnalysisType(Identifier userId, String codename)
	{
		StorableObjectCondition aTypeCondition =
			new TypicalCondition(
				codename,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.ANALYSISTYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);			

		try
		{
			Collection aTypes = MeasurementStorableObjectPool.getStorableObjectsByCondition(aTypeCondition, true);
			for (Iterator it = aTypes.iterator(); it.hasNext();)
			{
				AnalysisType type = (AnalysisType)it.next();
				if (type.getCodename().equals(codename))
					return type;
			}
		}
		catch(ApplicationException ex)
		{
			System.err.println("Exception searching ParameterType. Creating new one.");
			ex.printStackTrace();
		}

		List inParameterTypes = new ArrayList();
		ParameterType ptype = getParameterType(userId, ParameterTypeCodenames.REFLECTOGRAMMA, DataType.DATA_TYPE_RAW);
		inParameterTypes.add(ptype);

		List outParameterTypes = new ArrayList();
		ptype = getParameterType(userId, ParameterTypeCodenames.TRACE_EVENTS, DataType.DATA_TYPE_RAW);
		outParameterTypes.add(ptype);

		try
		{
				return AnalysisType.createInstance(
				userId,
				codename,
				"",
				inParameterTypes,
				new ArrayList(0),
				new ArrayList(0),
				outParameterTypes);
		}
		catch(CreateObjectException e)
		{
				// FIXME
				System.err.println("AnalysisUtil.getAnalysisType: Exception in createInstance. Wanna die.");
				e.printStackTrace();
				return null;
		}
	}

	/**
	 * Method for loading CriteriaSet for certain TestSetup to Pool. If there is no
	 * CriteriaSet attached to TestSetup new CriteriaSet created by call method
	 * createDefaultCriteriaSet(ms);
	 *
	 * @param ms MeasurementSetup
	 */

	public static void load_CriteriaSet(Identifier userId, MeasurementSetup ms)
	{
		/*
		 * <ul>
		 * <li>{@link com.syrus.AMFICOM.Client.Resource.Result.CriteriaSet CriteriaSet}
		 * <li>{@link #load_CriteriaSet(MeasurementSetup) load_CriteriaSet()}
		 * <li>{@link #load_CriteriaSet load_CriteriaSet()}
		 * </ul>
		 */

		Set criteriaSet = ms.getCriteriaSet();
		if (criteriaSet != null)
			setParamsFromCriteriaSet(criteriaSet);
//		else
//		{
//			criteriaSet = createCriteriaSetFromParams(userId, ms.getMonitoredElementIds());
//			ms.setCriteriaSet(criteriaSet);
//		}
	}

	/**
	 * Method for loading ThresholdSet for certain TestSetup to Pool. If there is no
	 * ThresholdSet attached to TestSetup default ThresholdSet created by call method
	 * createDefaultThresholdSet(userId, ms);
	 * @param userId Identifier
	 * @param ms MeasurementSetup
	 */
	public static void load_Thresholds(Identifier userId, MeasurementSetup ms)
	{
		Set thresholdSet = ms.getThresholdSet();

		ModelTraceManager mtm = (ModelTraceManager )Pool.get(ModelTraceManager.CODENAME, AnalysisUtil.ETALON);
		if (mtm == null)
			return;

		if (thresholdSet != null)
			setParamsFromThresholdsSet(thresholdSet,  mtm);
	}

	/**
	 * Method for loading Etalon for certain TestSetup to Pool. If there is no
	 * Etalon attached to TestSetup method returns.
	 * @param ms MeasurementSetup
	 * @see com.syrus.AMFICOM.Client.Resource.Result.Etalon
	 */
	public static void load_Etalon(MeasurementSetup ms)
	{
		Set etalon = ms.getEtalon();
		Set metas = ms.getParameterSet();

		ModelTraceManager mtm = null;
		BellcoreStructure bsEt=null;

		SetParameter[] params = etalon.getParameters();
		for (int i = 0; i < params.length; i++)
		{
			ParameterType type = (ParameterType)params[i].getType();
			if (type.getCodename().equals(ParameterTypeCodenames.DADARA_ETALON_EVENTS))
			{
				mtm = ModelTraceManager.eventsAndTraceFromByteArray(params[i].getValue());
				Pool.put(ModelTraceManager.CODENAME, ETALON, mtm);
				Pool.put("etalon", ETALON, metas);
			}
			else if (type.getCodename().equals(ParameterTypeCodenames.REFLECTOGRAMMA))
			{
				bsEt = new BellcoreReader().getData(params[i].getValue());
				Pool.put("bellcorestructure", AnalysisUtil.ETALON, bsEt);
				bsEt.title = "������ (" + (ms.getDescription().equals("") ? ms.getId().getIdentifierString() : ms.getDescription()) + ")";
			}
		}

		if(bsEt!=null && mtm!=null)
		{
			double deltaX = bsEt.getResolution();
			mtm.setDeltaX(deltaX);
		}
	}

	public static Set createCriteriaSetFromParams(Identifier userId, Collection meIds)
	{
		SetParameter[] params = new SetParameter[8];

		double[] defaultMinuitParams;
		defaultMinuitParams = (double[])Pool.get(OT_analysisparameters, OID_minuitanalysis);
		if (defaultMinuitParams == null)
			defaultMinuitParams = (double[])Pool.get(OT_analysisparameters, OID_minuitdefaults);

		String[] parameterCodenames = new String[] {
				ParameterTypeCodenames.MIN_EVENT_LEVEL,//0
				ParameterTypeCodenames.MIN_SPLICE,//1
				ParameterTypeCodenames.MIN_CONNECTOR,//2
				ParameterTypeCodenames.MIN_END_LEVEL,//3
				ParameterTypeCodenames.MAX_NOISE_LEVEL,//4
				ParameterTypeCodenames.CONNECTOR_FORM_FACTOR,//5
				ParameterTypeCodenames.STRATEGY,//6
				ParameterTypeCodenames.WAVELET_TYPE//7
		};

		try
		{
			for (int i = 0; i < 6; i++)
			{
				ParameterType ptype = getParameterType(userId, parameterCodenames[i], DataType.DATA_TYPE_DOUBLE);
				params[i] = SetParameter.createInstance(ptype,
						ByteArray.toByteArray(defaultMinuitParams[i]));
			}
			for (int i = 6; i < 8; i++)
			{
				ParameterType ptype = getParameterType(userId, parameterCodenames[i], DataType.DATA_TYPE_INTEGER);
				params[i] = SetParameter.createInstance(ptype,
						ByteArray.toByteArray((int)defaultMinuitParams[i]));
			}
		}
		catch (CreateObjectException e)
		{
				// FIXME
				System.err.println("AnalysisUtil.createCriteriaSetFromParams: CreateObjectException...");
			e.printStackTrace();
		}

		try
		{
				Set criteriaSet = Set.createInstance(
				userId,
				SetSort.SET_SORT_ANALYSIS_CRITERIA,
				"",
				params,
				meIds);

			return criteriaSet;
		}
		catch (CreateObjectException e)
		{
				// FIXME
				System.err.println("AnalysisUtil.createCriteriaSetFromParams: CreateObjectException -- wanna die.");
			e.printStackTrace();
			return null;
		}

	}

	public static Set createEtalon(Identifier userId, Collection meIds, ModelTraceManager mtm)
	{
		try
		{
			SetParameter[] params = new SetParameter[2];

			ParameterType ptype = getParameterType(userId, ParameterTypeCodenames.DADARA_ETALON_EVENTS, DataType.DATA_TYPE_RAW);
			params[0] = SetParameter.createInstance(ptype,
					mtm.eventsAndTraceToByteArray());

			BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", "primarytrace");

			ptype = getParameterType(userId, ParameterTypeCodenames.REFLECTOGRAMMA, DataType.DATA_TYPE_RAW);
			params[1] = SetParameter.createInstance(ptype,
					new BellcoreWriter().write(bs));

			Set etalon = Set.createInstance(
					userId,
					SetSort.SET_SORT_ETALON,
					"",
					params,
					meIds);
			return etalon;
		}
		catch (CreateObjectException e)
		{
			// FIXME
			System.err.println("AnalysisUtil.createEtalon: CreateObjectException -- wanna die.");
		e.printStackTrace();
		return null;
		}
	}

	public static Set createThresholdSet(Identifier userId, Collection meIds, ModelTraceManager mtm)
	{
		SetParameter[] params = new SetParameter[2];

		try
		{
			ParameterType ptype = getParameterType(userId, ParameterTypeCodenames.DADARA_THRESHOLDS, DataType.DATA_TYPE_RAW);
			params[0] = SetParameter.createInstance(ptype,
					mtm.toThresholdsByteArray());

			byte[] minLevel;
			try
			{
				Double level = (Double)Pool.get("min_trace_level", "primarytrace");
				minLevel = ByteArray.toByteArray(level == null ? 0d : level.doubleValue());
			}
			catch(Exception ex)
			{
				minLevel = new byte[0];
			}

			ptype = getParameterType(userId, ParameterTypeCodenames.DADARA_MIN_TRACE_LEVEL, DataType.DATA_TYPE_DOUBLE);
			params[1] = SetParameter.createInstance(ptype,
					minLevel);

			Set thresholdSet = Set.createInstance(
					userId,
					SetSort.SET_SORT_EVALUATION_THRESHOLDS,
					"",
					params,
					meIds);

			return thresholdSet;
		}
		catch (CreateObjectException e)
		{
			// FIXME
			System.err.println("AnalysisUtil.createThresholdSet: CreateObjectException -- wanna die.");
			e.printStackTrace();
			return null;
		}
}

	public static void setParamsFromThresholdsSet(Set thresholdSet, ModelTraceManager mtm)
	{
		SetParameter[] params = thresholdSet.getParameters();

		for (int i = 0; i < params.length; i++)
		{
			ParameterType type = (ParameterType)params[i].getType();
			if (type.getCodename().equals(ParameterTypeCodenames.DADARA_MIN_TRACE_LEVEL))
			{
				try
				{
					Double minLevel = new Double(new ByteArray(params[i].getValue()).toDouble());
					Pool.put("min_trace_level", "primarytrace", minLevel);
				}
				catch (IOException ex)
				{
				}
			}
			else if (type.getCodename().equals(ParameterTypeCodenames.DADARA_THRESHOLDS))
			{
				mtm.setThresholdsFromByteArray(params[i].getValue());
			}
		}
	}
//
//	public static boolean save_CriteriaSet(DataSourceInterface dataSource, BellcoreStructure bs)
//	{
//		try
//		{
//			TestSetup ts = (TestSetup)Pool.get(TestSetup.TYPE, bs.test_setup_id);
//			CriteriaSet cs;
//
//			if (ts.getCriteriaSetId().equals(""))
//			{
//				cs = createDefaultCriteriaSet(dataSource);
//				Pool.put(CriteriaSet.TYPE, cs.getId(), cs);
//
//				ts.setAnalysisTypeId(AnalysisUtil.DADARA);
//				ts.setCriteriaSetId(cs.getId());
//			}
//			else
//			{
//				cs = (CriteriaSet)Pool.get(CriteriaSet.TYPE, ts.getCriteriaSetId());
//				setCriteriaSetFromParams(cs);
//			}
//			dataSource.saveCriteriaSet(cs.getId());
//			dataSource.attachCriteriaSetToME(cs.getId(), bs.monitored_element_id);
//		}
//		catch (Exception ex)
//		{
//			System.out.println("Error saving CriteriaSet. ME not set");
//			ex.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//
//	public static boolean save_Etalon(DataSourceInterface dataSource, BellcoreStructure bs, ReflectogramEvent[] ep)
//	{
//		try
//		{
//			TestSetup ts = (TestSetup)Pool.get(TestSetup.TYPE, bs.test_setup_id);
//
//			// save etalon and attach to monitored element
//			Etalon et;
//
//			if (ts.getEthalonId().length() == 0)
//			{
//				et = createEtalon(dataSource, ep);
//				Pool.put(Etalon.TYPE, et.getId(), et);
//
//				ts.setEthalonId(et.getId());
//				ts.setEvaluationTypeId(AnalysisUtil.DADARA);
//			}
//			else
//			{
//				et = (Etalon)Pool.get(Etalon.TYPE, ts.getEthalonId());
//				if (et == null)
//				{
//					et = createEtalon(dataSource, ep);
//					Pool.put(Etalon.TYPE, et.getId(), et);
//
//					ts.setEthalonId(et.getId());
//					ts.setEvaluationTypeId(AnalysisUtil.DADARA);
//				}
//				else
//					updateEtalon(et, ep);
//			}
//			dataSource.saveEtalon(et.getId());
//			dataSource.attachEtalonToME(et.getId(), bs.monitored_element_id);
//		}
//
//		catch (Exception ex)
//		{
//			System.out.println("Error saving Etalon. ME not set");
//			ex.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//
//	public static boolean save_Thresholds(DataSourceInterface dataSource, BellcoreStructure bs, ReflectogramEvent[] ep)
//	{
//		try
//		{
//			TestSetup tset = (TestSetup)Pool.get(TestSetup.TYPE, bs.test_setup_id);
//			ThresholdSet ts;
//
//			if (tset.getThresholdSetId().length() == 0)
//			{
//				ts = createDefaultThresholdSet(dataSource, ep);
//				Pool.put(ThresholdSet.TYPE, ts.getId(), ts);
//				setThresholdsSetFromParams(ts);
//				tset.setThresholdSetId(ts.getId());
//				tset.setEvaluationTypeId(AnalysisUtil.DADARA);
//			}
//			else
//			{
//				ts = (ThresholdSet)Pool.get(ThresholdSet.TYPE, tset.getThresholdSetId());
//				setThresholdsSetFromParams(ts);
//			}
//			dataSource.saveThresholdSet(ts.getId());
//			dataSource.attachThresholdSetToME(ts.getId(), bs.monitored_element_id);
//		}
//		catch (Exception ex)
//		{
//			System.out.println("Error saving saveThresholdSet. ME not set");
//			ex.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//
//	public static void setThresholdsSetFromParams(ThresholdSet ts)
//	{
//		ReflectogramEvent[] ep = (ReflectogramEvent[])Pool.get("eventparams", "primarytrace");
//		Double min_level = (Double)Pool.get("min_trace_level", "primarytrace");
//		if (ep == null || min_level == null)
//			return;
//
//		Threshold[] threshs = new Threshold[ep.length];
//		for (int i = 0; i < ep.length; i++)
//			threshs[i] = ep[i].getThreshold();
//
//		for (Iterator it = ts.getThresholdList().iterator(); it.hasNext();)
//		{
//			Parameter p = (Parameter)it.next();
//			if (p.getCodename().equals("dadara_thresholds"))
//			{
//				p.setValue(Threshold.toByteArray(threshs));
//			}
//			if (p.getCodename().equals("dadara_min_trace_level"))
//			{
//				try
//				{
//					p.setValue(ByteArray.toByteArray(min_level.doubleValue()));
//				}
//				catch (IOException ex)
//				{
//					ex.printStackTrace();
//				}
//			}
//		}
//	}
//
//	static void updateEtalon(Etalon et, ReflectogramEvent[] ep)
//	{
//		for (Iterator it = et.getEthalonParameterList().iterator(); it.hasNext();)
//		{
//			Parameter p = (Parameter)it.next();
//			if (p.getCodename().equals(DADARA_ETALON_ARRAY))
//			{
//				p.setValue(ReflectogramEvent.toByteArray(ep));
//			}
//			if (p.getCodename().equals(REFLECTOGRAMM))
//			{
//				BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", "primarytrace");
//				p.setValue(new BellcoreWriter().write(bs));
//			}
//		}
//	}
//
//	public static void setCriteriaSetFromParams(Set criteriaSet)
//	{
//		double[] defaultMinuitParams;
//		defaultMinuitParams = (double[])Pool.get(xxx "analysisparameters", "minuitanalysis");
//		if (defaultMinuitParams == null)
//			defaultMinuitParams = (double[])Pool.get(xxx "analysisparameters", "minuitdefaults");
//
//		try
//		{
//			SetParameter[] params = criteriaSet.getParameters();
//			for (int i = 0; i < params.length; i++)
//			{
//				ParameterType p = (ParameterType)params[i].getType();
//				if (p.getCodename().equals("ref_uselinear"))
//					params[i].setValue(ByteArray.toByteArray((int)defaultMinuitParams[7]));
//				else if (p.getCodename().equals("ref_strategy"))
//					params[i].setValue(ByteArray.toByteArray((int)defaultMinuitParams[6]));
//				else if (p.getCodename().equals("ref_conn_fall_params"))
//					params[i].setValue(ByteArray.toByteArray(defaultMinuitParams[5]));
//				else if (p.getCodename().equals("ref_min_level"))
//					params[i].setValue(ByteArray.toByteArray(defaultMinuitParams[0]));
//				else if (p.getCodename().equals("ref_max_level_noise"))
//					params[i].setValue(ByteArray.toByteArray(defaultMinuitParams[4]));
//				else if (p.getCodename().equals("ref_min_level_to_find_end"))
//					params[i].setValue(ByteArray.toByteArray(defaultMinuitParams[3]));
//				else if (p.getCodename().equals("ref_min_weld"))
//					params[i].setValue(ByteArray.toByteArray(defaultMinuitParams[1]));
//				else if (p.getCodename().equals("ref_min_connector"))
//					params[i].setValue(ByteArray.toByteArray(defaultMinuitParams[2]));
//			}
//		}
//		catch (IOException ex)
//		{
//			ex.printStackTrace();
//		}
//	}

	public static void setParamsFromCriteriaSet(Set criteriaSet)
	{
		double[] minuitParams = (double[])Pool.get(OT_analysisparameters, OID_minuitanalysis);

		try
		{
			SetParameter[] params = criteriaSet.getParameters();
			for (int i = 0; i < params.length; i++)
			{
				ParameterType p = (ParameterType)params[i].getType();
				if (p.getCodename().equals(ParameterTypeCodenames.WAVELET_TYPE))
					minuitParams[7] = new ByteArray(params[i].getValue()).toInt();
				else if (p.getCodename().equals(ParameterTypeCodenames.STRATEGY))
					minuitParams[6] = new ByteArray(params[i].getValue()).toInt();
				else if (p.getCodename().equals(ParameterTypeCodenames.CONNECTOR_FORM_FACTOR))
					minuitParams[5] = new ByteArray(params[i].getValue()).toDouble();
				else if (p.getCodename().equals(ParameterTypeCodenames.MIN_EVENT_LEVEL))
					minuitParams[0] = new ByteArray(params[i].getValue()).toDouble();
				else if (p.getCodename().equals(ParameterTypeCodenames.MAX_NOISE_LEVEL))
					minuitParams[4] = new ByteArray(params[i].getValue()).toDouble();
				else if (p.getCodename().equals(ParameterTypeCodenames.MIN_END_LEVEL))
					minuitParams[3] = new ByteArray(params[i].getValue()).toDouble();
				else if (p.getCodename().equals(ParameterTypeCodenames.MIN_SPLICE))
					minuitParams[1] = new ByteArray(params[i].getValue()).toDouble();
				else if (p.getCodename().equals(ParameterTypeCodenames.MIN_CONNECTOR))
					minuitParams[2] = new ByteArray(params[i].getValue()).toDouble();
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		double[] minuitInitialParams = (double[])Pool.get(OT_analysisparameters, OID_minuitinitials);
		for (int i = 0; i < minuitParams.length; i++)
			minuitInitialParams[i] = minuitParams[i];
	}
}