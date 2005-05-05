package com.syrus.AMFICOM.Client.Analysis;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.DataFormatException;
import com.syrus.AMFICOM.analysis.dadara.DataStreamableUtil;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.TraceEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.OperationSort;
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
	public static final String ETALON = Heap.ETALON_TRACE_KEY;

	private AnalysisUtil()
	{ // empty
	}

	public static ParameterType getParameterType(String codename, DataType dataType) throws ApplicationException {
		TypicalCondition pTypeCondition = new TypicalCondition(
				codename,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);

			java.util.Set parameterTypeSet = GeneralStorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);
			if (parameterTypeSet.isEmpty())
				throw new RetrieveObjectException("AnalysisUtil.getParameterType | parameter type with codename " + pTypeCondition.getValue() + " not found");

			//return (ParameterType) parameterTypeSet.iterator().next();
			ParameterType ret = (ParameterType) parameterTypeSet.iterator().next();
			if (ret.getDataType() != dataType)
				throw new ApplicationException("unexpected dataType");
			return ret;
	}

	public static AnalysisType getAnalysisType(String codename) throws ApplicationException
	{
		StorableObjectCondition aTypeCondition =
			new TypicalCondition(
				codename,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.ANALYSISTYPE_ENTITY_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);			

		Collection aTypes = MeasurementStorableObjectPool.getStorableObjectsByCondition(aTypeCondition, true);
		for (Iterator it = aTypes.iterator(); it.hasNext();)
		{
			AnalysisType type = (AnalysisType)it.next();
			if (type.getCodename().equals(codename))
				return type;
		}
		throw new ApplicationException("getAnalysisType parametertype not found");
	}

	/**
	 * Method for loading CriteriaSet for certain TestSetup to Pool. If there is no
	 * CriteriaSet attached to TestSetup new CriteriaSet created by call method
	 * createDefaultCriteriaSet(ms);
	 *
	 * @param ms MeasurementSetup
	 * @throws DataFormatException 
	 */

	public static void load_CriteriaSet(Identifier userId, MeasurementSetup ms) throws DataFormatException
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
        {
		    setParamsFromCriteriaSet(criteriaSet);
        }
//      else
//      {
//          criteriaSet = createCriteriaSetFromParams(userId, ms.getMonitoredElementIds());
//          ms.setCriteriaSet(criteriaSet);
//      }
	}

	/**
	 * Method for loading Etalon for certain TestSetup to Pool. If there is no
	 * Etalon attached to TestSetup method returns.
	 * @param ms MeasurementSetup
	 * @throws DataFormatException 
	 * @see com.syrus.AMFICOM.Client.Resource.Result.Etalon
	 */
	public static void load_Etalon(MeasurementSetup ms) throws DataFormatException
	{
		Set etalon = ms.getEtalon();
		Set metas = ms.getParameterSet();

		BellcoreStructure bsEt = null;

		SetParameter[] params = etalon.getParameters();
		for (int i = 0; i < params.length; i++)
		{
			ParameterType type = (ParameterType)params[i].getType();
			if (type.getCodename().equals(ParameterTypeCodenames.DADARA_ETALON_MTM))
			{
				ModelTraceManager mtm = (ModelTraceManager)DataStreamableUtil.
					readDataStreamableFromBA(params[i].getValue(),
						ModelTraceManager.getReader());
				Heap.setMTMEtalon(mtm);
				Heap.setEtalonEtalonMetas(metas);
			}
			else if (type.getCodename().equals(ParameterTypeCodenames.REFLECTOGRAMMA))
			{
				bsEt = new BellcoreReader().getData(params[i].getValue());
				Heap.setBSEtalonTrace(bsEt);
				bsEt.title = "Эталон (" + (ms.getDescription().equals("") ? ms.getId().getIdentifierString() : ms.getDescription()) + ")";
			}
		}
	}

	public static Set createCriteriaSet(Identifier userId, java.util.Set meIds)
    throws ApplicationException
	{
		AnalysisParameters analysisParams = Heap.getMinuitAnalysisParams();
		if (analysisParams == null)
			analysisParams = Heap.getMinuitDefaultParams();

        SetParameter[] params = new SetParameter[1];

		{
			ParameterType ptype = getParameterType(
				ParameterTypeCodenames.DADARA_CRITERIA, DataType.DATA_TYPE_RAW);
			params[0] = SetParameter.createInstance(ptype,
				DataStreamableUtil.writeDataStreamableToBA(analysisParams));
		}

		{
			Set criteriaSet = Set.createInstance(
				userId,
				SetSort.SET_SORT_ANALYSIS_CRITERIA,
				"",
				params,
				meIds);

			return criteriaSet;
		}
	}

	public static Set createEtalon(Identifier userId, java.util.Set meIds,
            ModelTraceManager mtm) throws ApplicationException
	{
		SetParameter[] params = new SetParameter[2];

		ParameterType ptype = getParameterType(ParameterTypeCodenames.DADARA_ETALON_MTM, DataType.DATA_TYPE_RAW);
		params[0] = SetParameter.createInstance(ptype,
				DataStreamableUtil.writeDataStreamableToBA(mtm));

		BellcoreStructure bs = Heap.getBSPrimaryTrace();

		ptype = getParameterType(ParameterTypeCodenames.REFLECTOGRAMMA, DataType.DATA_TYPE_RAW);
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

	// FIXME: не используется? - а должен бы...
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
					Heap.setMinTraceLevel(new Double(new ByteArray(params[i].getValue()).toDouble()));
				}
				catch (IOException ex)
				{
				}
			}
		}
	}

	public static void setParamsFromCriteriaSet(Set criteriaSet)
    throws DataFormatException
	{
		AnalysisParameters analysisParams = null;
        SetParameter[] params = criteriaSet.getParameters();
        for (int i = 0; i < params.length; i++)
        {
            ParameterType p = (ParameterType)params[i].getType();
            if (p.getCodename().equals(ParameterTypeCodenames.DADARA_CRITERIA))
        analysisParams = (AnalysisParameters)
        	DataStreamableUtil.readDataStreamableFromBA(
        		params[i].getValue(),
        		AnalysisParameters.getReader());
        }
        if (analysisParams == null)
        throw new DataFormatException(
                "No" + ParameterTypeCodenames.DADARA_CRITERIA);
		Heap.setMinuitAnalysisParams(
			(AnalysisParameters)analysisParams.clone());
		Heap.setMinuitInitialParams(analysisParams);
	}

	public static String getSimpleEventNameByType(int eventType)
	{
		String eventTypeName;
		switch(eventType)
		{
		case SimpleReflectogramEvent.CONNECTOR:
		    eventTypeName = LangModelAnalyse.getString("eventTypeReflective");
			break;
		case SimpleReflectogramEvent.DEADZONE:
		    eventTypeName = LangModelAnalyse.getString("eventTypeInitiate");
			break;
		case SimpleReflectogramEvent.ENDOFTRACE:
		    eventTypeName = LangModelAnalyse.getString("eventTypeTerminate");
			break;
		case SimpleReflectogramEvent.GAIN:
			eventTypeName = LangModelAnalyse.getString("eventTypeGain");
			break;
		case SimpleReflectogramEvent.LOSS:
			eventTypeName = LangModelAnalyse.getString("eventTypeLoss");
			break;
		case SimpleReflectogramEvent.LINEAR:
		    eventTypeName = LangModelAnalyse.getString("eventTypeLinear");
			break;
		case SimpleReflectogramEvent.NOTIDENTIFIED:
		    eventTypeName = LangModelAnalyse.getString("eventTypeNonIdentified");
			break;
		case SimpleReflectogramEvent.RESERVED:
		    eventTypeName = LangModelAnalyse.getString("eventTypeNoType");
			break;
		default:
		    eventTypeName = LangModelAnalyse.getString("eventTypeUnk"); // @todo: отличать от eventTypeNonIdentified или нет?
		}
		return eventTypeName;
	}
}
