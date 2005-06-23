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
import com.syrus.AMFICOM.analysis.dadara.events.DetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.SpliceDetailedEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.corba.IdlParameterSetPackage.ParameterSetSort;
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
	private AnalysisUtil()
	{ // non-instantiable
	}

	public static ParameterType getParameterType(String codename, DataType dataType) throws ApplicationException {
		TypicalCondition pTypeCondition = new TypicalCondition(
				codename,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.PARAMETER_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);

			java.util.Set parameterTypeSet = StorableObjectPool.getStorableObjectsByCondition(pTypeCondition, true);
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
				ObjectEntities.ANALYSIS_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);			

		Collection aTypes = StorableObjectPool.getStorableObjectsByCondition(aTypeCondition, true);
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

		ParameterSet criteriaSet = ms.getCriteriaSet();
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
		ParameterSet etalon = ms.getEtalon();
		ParameterSet metas = ms.getParameterSet();

		BellcoreStructure bsEt = null;

		Parameter[] params = etalon.getParameters();
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
            else if (type.getCodename().equals(ParameterTypeCodenames.REFLECTOGRAMMA_ETALON))
            {
                bsEt = new BellcoreReader().getData(params[i].getValue());
                Heap.setBSEtalonTrace(bsEt);
                bsEt.title = "Эталон (" + (ms.getDescription().equals("") ? ms.getId().getIdentifierString() : ms.getDescription()) + ")"; // XXX: externalized string
            } else if (type.getCodename().equals(ParameterTypeCodenames.DADARA_MIN_TRACE_LEVEL))
            {
                try {
                    Heap.setMinTraceLevel(new ByteArray(params[i].getValue()).toDouble());
                } catch (IOException e) {
                    throw new DataFormatException("cannot read double value minTraceLevel");
                }
            }
		}
	}

	public static ParameterSet createCriteriaSet(Identifier userId, java.util.Set meIds)
    throws ApplicationException
	{
		AnalysisParameters analysisParams = Heap.getMinuitAnalysisParams();
		if (analysisParams == null)
			analysisParams = Heap.getMinuitDefaultParams();

        Parameter[] params = new Parameter[1];

		{
			ParameterType ptype = getParameterType(
				ParameterTypeCodenames.DADARA_CRITERIA, DataType.DATA_TYPE_RAW);
			params[0] = Parameter.createInstance(ptype,
				DataStreamableUtil.writeDataStreamableToBA(analysisParams));
		}

		{
			ParameterSet criteriaSet = ParameterSet.createInstance(
				userId,
				ParameterSetSort.SET_SORT_ANALYSIS_CRITERIA,
				"",
				params,
				meIds);

			return criteriaSet;
		}
	}

	public static ParameterSet createEtalon(Identifier userId, java.util.Set meIds,
            ModelTraceManager mtm) throws ApplicationException
	{
		Parameter[] params = new Parameter[3];

		ParameterType ptype = getParameterType(ParameterTypeCodenames.DADARA_ETALON_MTM, DataType.DATA_TYPE_RAW);
		params[0] = Parameter.createInstance(ptype,
				DataStreamableUtil.writeDataStreamableToBA(mtm));

		BellcoreStructure bs = Heap.getBSPrimaryTrace();

		ptype = getParameterType(ParameterTypeCodenames.REFLECTOGRAMMA_ETALON, DataType.DATA_TYPE_RAW);
		params[1] = Parameter.createInstance(ptype,
				new BellcoreWriter().write(bs));

        ptype = getParameterType(ParameterTypeCodenames.DADARA_MIN_TRACE_LEVEL, DataType.DATA_TYPE_DOUBLE);
        params[2] = Parameter.createInstance(ptype,
                ByteArray.toByteArray(Heap.getMinTraceLevel()));

		ParameterSet etalon = ParameterSet.createInstance(
				userId,
				ParameterSetSort.SET_SORT_ETALON,
				"",
				params,
				meIds);
		return etalon;
	}

	public static void setParamsFromCriteriaSet(ParameterSet criteriaSet)
    throws DataFormatException
	{
		AnalysisParameters analysisParams = null;
        Parameter[] params = criteriaSet.getParameters();
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
        Heap.notifyAnalysisParametersUpdated();
	}

	/**
	 * @param ev событие DetailedEvent
	 * @return уточненное описание такого события (напр., потери на изгибе)
	 */
	public static String getDetailedEventName(DetailedEvent ev)
	{
		final double CRIT_LOSS = 0.5;
		final double CRIT_GAIN = CRIT_LOSS;
		switch(ev.getEventType())
		{
		case SimpleReflectogramEvent.GAIN:
			if (-((SpliceDetailedEvent)ev).getLoss() > CRIT_GAIN)
				return LangModelAnalyse.getString("eventTypeGain");
			else
				return LangModelAnalyse.getString("eventTypeGainAtWeld");
		case SimpleReflectogramEvent.LOSS:
			if (((SpliceDetailedEvent)ev).getLoss() > CRIT_LOSS)
				return LangModelAnalyse.getString("eventTypeLossAtBend");
			else
				return LangModelAnalyse.getString("eventTypeLossAtWeld");
		default:
			return getSimpleEventNameByType(ev.getEventType());
		}
	}

	/**
	 * @param eventType тип SimpleReflectogramEvent события
	 * @return общий тип такого события (не уточненный)
	 */
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
		//case SimpleReflectogramEvent.RESERVED:
		default:
		    eventTypeName = LangModelAnalyse.getString("dash");
			break;
		}
		return eventTypeName;
	}
}
