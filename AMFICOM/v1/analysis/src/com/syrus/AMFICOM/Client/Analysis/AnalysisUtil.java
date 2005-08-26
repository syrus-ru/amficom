package com.syrus.AMFICOM.Client.Analysis;

import java.util.Date;
import java.util.Set;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.Etalon;
import com.syrus.AMFICOM.analysis.SimpleApplicationException;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.AnalysisResult;
import com.syrus.AMFICOM.analysis.dadara.DataFormatException;
import com.syrus.AMFICOM.analysis.dadara.DataStreamableUtil;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.SpliceDetailedEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.IdlParameterSetPackage.ParameterSetSort;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreWriter;
import com.syrus.util.EasyDateFormatter;

/**
 Class with methods used to save/load measuring parameters onto server
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Syrus Systems</p>
 * @author Stanislav Kholshin
 */

public class AnalysisUtil
{
	private AnalysisUtil()
	{ // non-instantiable
	}

	/**
	 * проверяет, измерения ли это результат
	 * @return true, если это результат изменения
	 */
	public static boolean hasMeasurementByResult(Result result) {
		return result.getSort().equals(ResultSort.RESULT_SORT_MEASUREMENT);
	}
	/**
	 * определяет measurement по его результату. Перед вызовом проверьте
	 * {@link #hasMeasurementByResult(Result)}
	 */
	public static Measurement getMeasurementByResult(Result result) {
		return (Measurement)result.getAction();
	}

	/**
	 * достаем собственно рефлектограмму из параметры результатов.
	 * Если рефлектограмма получена в результате измерения, выставляем
	 * у нее title, meId, measId
	 * @throws SimpleApplicationException, если рефлектограммы в указанном результате нет
	 */
	public static BellcoreStructure getBellcoreStructureFromResult(
			Result result1)
	throws SimpleApplicationException {
		BellcoreStructure bs = null;
		Parameter[] parameters = result1.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			Parameter param = parameters[i];
			ParameterType type = param.getType();
			if (type.equals(ParameterType.REFLECTOGRAMMA)) {
				bs = new BellcoreReader().getData(param.getValue());
			}
		}
		if (bs == null)
			throw new SimpleApplicationException(SimpleApplicationException.KEY_NULL_REFLECTOGRAMMA);

		if (hasMeasurementByResult(result1)) {
			Measurement m = getMeasurementByResult(result1);
			bs.title = m.getName();
			bs.monitoredElementId = m.getMonitoredElementId().getIdentifierString();
			bs.measurementId = m.getId().getIdentifierString();
		}

		return bs;
	}

	/**
	 * Определяет результаты анализа того измерения, результаты которого даны
	 * на входе
	 * @param result результаты измерения
	 * @return результаты анализа либо null, если таковые не были сохранены
	 * @throws ApplicationException при ошибке работы с пулом или сервером (?)
	 * @throws DataFormatException при ошибки восстановления данных их потока
	 */
	public static AnalysisResult getAnalysisResultForResultIfPresent(Result result)
	throws DataFormatException, ApplicationException {
		assert hasMeasurementByResult(result) : "AU.getAnalysisResultForResultIfPresent(): arg must be a result of a measurement";
		Measurement m = getMeasurementByResult(result);
		assert m != null : "AU.getAnalysisResultForResultIfPresent(): null measurement";
		return getAnalysisForMeasurementIfPresent(m); // may return null
	}

	public static AnalysisResult getAnalysisForMeasurementIfPresent(Measurement m)
	throws DataFormatException, ApplicationException {
		LinkedIdsCondition condition1 = new LinkedIdsCondition(m.getId(), ObjectEntities.ANALYSIS_CODE);
		Set<Analysis> analyse = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
		for (Analysis analysis : analyse) {
			LinkedIdsCondition condition2 = new LinkedIdsCondition(analysis.getId(), ObjectEntities.RESULT_CODE);
			Set<Result> results = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
			for (Result result1 : results) {
				for (Parameter parameter : result1.getParameters()) {
					if (parameter.getType().equals(ParameterType.DADARA_ANALYSIS_RESULT)) {
						return (AnalysisResult) DataStreamableUtil.readDataStreamableFromBA(parameter.getValue(), AnalysisResult.getDSReader());
					}
				}
			}
		}
		return null;
	}

	public static AnalysisResult getAnalysisForMeasurement(Measurement m)
	throws DataFormatException, ApplicationException {
		AnalysisResult ar = getAnalysisForMeasurementIfPresent(m);
		if (ar == null) {
			throw new ApplicationException("No AnalysisResult found for Measurement " + m.getName());
		}
		return ar;
	}

	/**
	 * Method for loading CriteriaSet for certain TestSetup to Pool.
	 * If there is no CriteriaSet attached to TestSetup new CriteriaSet
	 * created by call method createDefaultCriteriaSet(ms);
	 *
	 * @param ms MeasurementSetup
	 * @throws DataFormatException 
	 */

	public static void loadCriteriaSet(Identifier userId, MeasurementSetup ms)
	throws DataFormatException
	{
		/*
		 * <ul>
		 * <li>{@link com.syrus.AMFICOM.Client.Resource.Result.CriteriaSet CriteriaSet}
		 * <li>{@link #load_CriteriaSet(MeasurementSetup) load_CriteriaSet()}
		 * <li>{@link #load_CriteriaSet load_CriteriaSet()}
		 * </ul>
		 */

		ParameterSet criteriaSet = ms.getCriteriaSet();
		if (criteriaSet != null) {
			setParamsFromCriteriaSet(criteriaSet);
		}
//      else
//      {
//          criteriaSet = createCriteriaSetFromParams(userId, ms.getMonitoredElementIds());
//          ms.setCriteriaSet(criteriaSet);
//      }
	}

	/**
	 * Убеждается, что в данном ms есть один и только один me,
	 * и возвращает этот me. Предназначен для использования
	 * @param ms
	 * #return если у данного ms один me, то me данного ms. Eсли у данного ms
	 * нет me либо есть более одного me, то null.
	 * @throws ApplicationException ошибки getStorableObject()
	 */
	public static MonitoredElement getMEbyMS(MeasurementSetup ms)
	throws ApplicationException {
		if (ms.getMonitoredElementIds().size() != 1) {
			return null;
		}
		return StorableObjectPool.getStorableObject(
				ms.getMonitoredElementIds().iterator().next(), true);
	}

	/**
	 * Конструирует строку вида 'ЭР-путь-дата'
	 * @param me путь тестирования, may be null
	 * @param date дата шаблона,  may be null
	 */
	public static String makeEtalonRefName(MonitoredElement me, Date date) {
		StringBuilder sb = new StringBuilder();
		sb.append(LangModelAnalyse.getString("ER"));
		if (me != null) {
			// если MonitoredElement у данного ms есть и единственный,
			// то взять его имя
			sb.append("-");
			sb.append(me.getName());
		}
		if (date != null) {
			sb.append("-");
			sb.append(EasyDateFormatter.formatDate(date));
		}
		return sb.toString();
	}

	/**
	 * Load Etalon for certain MeasurementSetup to Heap.
	 * If there is no Etalon attached to MeasurementSetup, do nothing.
	 * @param ms MeasurementSetup to be loaded
	 * @throws DataFormatException Etalon object decoding failed
	 * @throws ApplicationException Error while getting ME
	 */
	public static void loadEtalon(MeasurementSetup ms)
	throws DataFormatException, ApplicationException
	{
		ParameterSet etalonSet = ms.getEtalon();
		//ParameterSet metas = ms.getParameterSet();

		BellcoreStructure etalonBS = null;
		Etalon etalonObj = null;

		Parameter[] params = etalonSet.getParameters();
		for (int i = 0; i < params.length; i++)
		{
			ParameterType type = params[i].getType();
			if (type.equals(ParameterType.DADARA_ETALON))
			{
				etalonObj = (Etalon) DataStreamableUtil.
					readDataStreamableFromBA(params[i].getValue(),
							Etalon.getDSReader());
			}
			else if (type.equals(ParameterType.REFLECTOGRAMMA_ETALON))
			{
				etalonBS = new BellcoreReader().getData(params[i].getValue());
//				etalonBS.title = "etalon (" + (ms.getDescription().equals("")
//							? ms.getId().getIdentifierString()
//							: ms.getDescription())
//						+ ")"; // FIXME: generate etalon name some other way
			}
		}
		if (etalonObj == null || etalonBS == null) {
			System.err.println("Malformed etalon: "
					+ (etalonObj == null ? "no etalonObj" : "")
					+ (etalonBS == null ? "no etalonBS" : ""));
			GUIUtil.showErrorMessage(GUIUtil.MSG_ERROR_MALFORMED_ETALON);
			return;
		}
		etalonBS.title =  makeEtalonRefName(getMEbyMS(ms), ms.getModified());// XXX: ms.getCreated()?
		Heap.setEtalonPair(etalonBS, etalonObj, etalonBS.title);
		//Heap.setEtalonEtalonMetas(metas);
	}

	public static ParameterSet createCriteriaSet(Identifier userId,
			java.util.Set<Identifier> meIds)
	throws ApplicationException
	{
		AnalysisParameters analysisParams = Heap.getMinuitAnalysisParams();
		if (analysisParams == null)
			analysisParams = Heap.getMinuitDefaultParams();

		Parameter[] params = new Parameter[1];

		{
			ParameterType ptype = ParameterType.DADARA_CRITERIA;
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

	public static ParameterSet createEtalon(Identifier userId,
			java.util.Set<Identifier> meIds)
	throws ApplicationException
	{
		Parameter[] params = new Parameter[2];

		ParameterType ptype = ParameterType.DADARA_ETALON;
		params[0] = Parameter.createInstance(ptype,
				DataStreamableUtil.writeDataStreamableToBA(
						Heap.getEtalon()));

		BellcoreStructure bs = Heap.getBSEtalonTrace();

		ptype = ParameterType.REFLECTOGRAMMA_ETALON;
		params[1] = Parameter.createInstance(ptype,
				new BellcoreWriter().write(bs));

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
			ParameterType p = params[i].getType();
			if (p.equals(ParameterType.DADARA_CRITERIA))
				analysisParams = (AnalysisParameters)
					DataStreamableUtil.readDataStreamableFromBA(
							params[i].getValue(),
							AnalysisParameters.getReader());
		}
		if (analysisParams == null) {
			throw new DataFormatException(
					"No" + ParameterType.DADARA_CRITERIA.getCodename());
		}
		Heap.setMinuitAnalysisParams(analysisParams);
		Heap.setMinuitInitialParamsFromCurrentAP();
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
			if (-((SpliceDetailedEvent)ev).getLoss() > CRIT_GAIN) {
				return LangModelAnalyse.getString("eventTypeGain");
			}
			return LangModelAnalyse.getString("eventTypeGainAtWeld");
		case SimpleReflectogramEvent.LOSS:
			if (((SpliceDetailedEvent)ev).getLoss() > CRIT_LOSS) {
				return LangModelAnalyse.getString("eventTypeLossAtBend");
			}
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
