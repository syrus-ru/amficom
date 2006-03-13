package com.syrus.AMFICOM.Client.Analysis;

import java.util.Date;
import java.util.Set;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.Etalon;
import com.syrus.AMFICOM.analysis.EtalonComparison;
import com.syrus.AMFICOM.analysis.PFTrace;
import com.syrus.AMFICOM.analysis.SimpleApplicationException;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.AnalysisResult;
import com.syrus.AMFICOM.analysis.dadara.DadaraReflectometryAnalysisResult;
import com.syrus.AMFICOM.analysis.dadara.DataStreamableUtil;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.SpliceDetailedEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Action;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.IdlParameterSetPackage.ParameterSetSort;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.AMFICOM.reflectometry.MeasurementReflectometryAnalysisResult;
import com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisCriteria;
import com.syrus.AMFICOM.reflectometry.ReflectometryEtalon;
import com.syrus.AMFICOM.reflectometry.ReflectometryMeasurementSetup;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreWriter;
import com.syrus.io.DataFormatException;
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
	 * ���������, ��������� �� ��� ���������
	 * @return true, ���� ��� ��������� ���������
	 */
	public static boolean hasMeasurementByResult(Result result) {
		return result.getSort().equals(ResultSort.RESULT_SORT_MEASUREMENT);
	}
	/**
	 * ���������� measurement �� ��� ����������. ����� ������� ���������
	 * {@link #hasMeasurementByResult(Result)}
	 */
	public static Measurement getMeasurementByResult(Result result) {
		return (Measurement) result.getAction();
	}

	/**
	 * ������� ���������� �������������� �� ��������� �����������.
	 * ���� �������������� �������� � ���������� ���������, ����������
	 * � ��� title, meId, measId
	 * @throws SimpleApplicationException, ���� �������������� � ��������� ���������� ���
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
	 * ���������� ���������� ������� ���� ���������, ���������� �������� ����
	 * �� �����
	 * @param result ���������� ���������
	 * @return ���������� ������� ���� null, ���� ������� �� ���� ���������
	 * @throws ApplicationException ��� ������ ������ � ����� ��� �������� (?)
	 * @throws DataFormatException ��� ������ �������������� ������ �� ������
	 */
	public static AnalysisResult getAnalysisResultForResultIfPresent(Result result)
	throws DataFormatException, ApplicationException {
		assert hasMeasurementByResult(result) : "AU.getAnalysisResultForResultIfPresent(): arg must be a result of a measurement";
		Measurement m = getMeasurementByResult(result);
		assert m != null : "AU.getAnalysisResultForResultIfPresent(): null measurement";
		return getAnalysisForMeasurementIfPresent(m); // may return null
	}

	/**
	 * ��������� ��������� ������� ������� ���������.
	 * @param m ������ ���������
	 * @return ��������� ������� ������� ��������� ���� null
	 * @throws DataFormatException
	 * @throws ApplicationException
	 */
	public static AnalysisResult getAnalysisForMeasurementIfPresent(Measurement m)
	throws DataFormatException, ApplicationException {
		/*
		 * �����������:
		 * ���� ��������� ������� ��� ���� � ����, �� useLoader ����� �� � ����,
		 * see bug 481.
		 */
		final AnalysisResult result1 = getAnalysisForMeasurementIfPresent0(m, false);
		if (result1 != null) {
			return result1;
		}
		return getAnalysisForMeasurementIfPresent0(m, true);
	}

	private static AnalysisResult getAnalysisForMeasurementIfPresent0(Measurement m, boolean useLoader)
	throws DataFormatException, ApplicationException {
		LinkedIdsCondition condition1 = new LinkedIdsCondition(m.getId(), ObjectEntities.ANALYSIS_CODE);
		Set<Analysis> analyse = StorableObjectPool.getStorableObjectsByCondition(condition1, useLoader); // XXX: performance: the most slow part of loading trace that has no analysis
		for (Analysis analysis : analyse) {
			LinkedIdsCondition condition2 = new LinkedIdsCondition(analysis.getId(), ObjectEntities.RESULT_CODE);
			Set<Result> results = StorableObjectPool.getStorableObjectsByCondition(condition2, useLoader);
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

	/**
	 * @param m
	 * @throws DataFormatException 
	 * @throws ApplicationException 
	 */
	public static void loadEtalonComparison(Measurement m)
	throws ApplicationException, DataFormatException {
		final EtalonComparison etalonComparison = getEtalonComparison(m);
		System.out.println("AnalysisUtil.loadEtalonComparison(): setting eComp = " + etalonComparison);
		Heap.setEtalonComparison(etalonComparison);
	}

	/**
	 * @param m
	 * @return may be null
	 * @throws ApplicationException 
	 * @throws DataFormatException 
	 */
	public static EtalonComparison getEtalonComparison(Measurement m)
	throws ApplicationException, DataFormatException {
		// �������� ������ ��� ��������� (����� ���� null)
		Analysis analysis =
			MeasurementReflectometryAnalysisResult.getAnalysisForMeasurement(m);
		if (analysis == null) {
			return null; // ��� �������
		}
		// ��������� ������ � ���� DRAR
		final DadaraReflectometryAnalysisResult drar =
			new DadaraReflectometryAnalysisResult(
					new MeasurementReflectometryAnalysisResult(analysis));

		return drar.getDadaraEtalonComparison(); // may be null
	}

	/**
	 * ��������� �������� ������� ��������� �������.
	 * ���� � ������� ��� ��������� �������, ������ �� ������.
	 *
	 * @param ms MeasurementSetup ������
	 * @throws DataFormatException �������� ������ ������
	 * ���������� � ������� ��������� �������
	 */
	public static void loadCriteriaSet(Identifier userId, MeasurementSetup ms)
	throws DataFormatException {
		AnalysisParameters analysisParams = getCriteriaSetByMeasurementSetup(ms);
		if (analysisParams != null) {
			Heap.setMinuitAnalysisParams(analysisParams);
			Heap.setMinuitInitialParamsFromCurrentAP();
			Heap.notifyAnalysisParametersUpdated();
		}
	}

	/**
	 * ���������� �������� ������� ������� MeasurementSetup ����
	 * null, ���� �������� ���.
	 * @param ms MeasurementSetup
	 * @return �������� ������� ������� MeasurementSetup ����
	 * null, ���� �������� ���
	 * @throws DataFormatException ��� ������� ������� ������
	 */
	public static AnalysisParameters getCriteriaSetByMeasurementSetup(
			MeasurementSetup ms)
	throws DataFormatException {
		ReflectometryAnalysisCriteria ac =
				new ReflectometryMeasurementSetup(ms).getAnalysisCriteria();
		if (ac != null) {
			AnalysisParameters analysisParams = (AnalysisParameters)
					DataStreamableUtil.readDataStreamableFromBA(
							ac.getDadaraCriteria(),
							AnalysisParameters.getReader());
			if (analysisParams == null) {
				throw new DataFormatException(
						"No" + ParameterType.DADARA_CRITERIA.getCodename());
			}
			return analysisParams;
		}
		return null;
	}

	/**
	 * ����������, ��� � ������ ms ���� ���� � ������ ���� me,
	 * � ���������� ���� me. ������������ ��� �������������
	 * ��� ��������� ����� �������
	 * @return me ������� ms, ���� � ������� ms ���� me;
	 *  null, ���� � ������� ms ��� me ���� ���� ����� ������ me
	 * @throws ApplicationException ������ getStorableObject()
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
	 * ������������ ������ ���� '��-����-����'
	 * @param me ���� ������������, may be null
	 * @param date ���� �������,  may be null
	 */
	public static String makeEtalonRefName(MonitoredElement me, Date date) {
		StringBuilder sb = new StringBuilder();
		sb.append(LangModelAnalyse.getString("ER"));
		if (me != null) {
			// ���� MonitoredElement � ������� ms ���� � ������������,
			// �� ����� ��� ���
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
	 * @return true - �������� �������; false - �������� �� �������, �
	 *   ��������� �� ������ �������� �������� ������������.
	 * @throws DataFormatException Etalon object decoding failed
	 * @throws ApplicationException Error while getting ME
	 */
	public static boolean loadEtalon(MeasurementSetup ms)
	throws DataFormatException, ApplicationException {
		return loadEtalonEx(ms, false);
	}

	/**
	 * Load Etalon of certain MeasurementSetup <b>as a primary trace</b>.
	 * If there is no Etalon attached to MeasurementSetup, do nothing.
	 * @param ms MeasurementSetup to be loaded
	 * @return true - �������� �������; false - �������� �� �������, �
	 *   ��������� �� ������ �������� �������� ������������.
	 * @throws DataFormatException Etalon object decoding failed
	 * @throws ApplicationException Error while getting ME
	 */
	public static boolean loadEtalonAsPrimary(MeasurementSetup ms)
	throws DataFormatException, ApplicationException {
		return loadEtalonEx(ms, true);
	}

	/**
	 * ��������� � Heap ������ �� ������� MeasurementSetup.
	 * ���� �������� �������, ��������� true.
	 * ���� �������� �� �������, ������� ������������ ��������� �� ������
	 * � ���������� false.
	 * @todo ��������, ����� �� ���������� false, � ������� ����������
	 * @param ms ������ MeasurementSetup
	 * @param asPrimary
	 *   true, ����� ��������� ��� ��������� �/� (����������� �� ������ ���������),
	 *   false, ����� ��������� ��� ������ (���������).
	 * @return true - �������� �������; false - �������� �� �������, �
	 *   ��������� �� ������ �������� �������� ������������.
	 * @throws DataFormatException Etalon object decoding failed
	 * @throws ApplicationException Error while getting ME
	 */
	private static boolean loadEtalonEx(MeasurementSetup ms, boolean asPrimary)
	throws DataFormatException, ApplicationException {
		ReflectometryMeasurementSetup rms = new ReflectometryMeasurementSetup(ms);

		ReflectometryEtalon re = rms.getEtalon();

		BellcoreStructure etalonBS = null;
		Etalon etalonObj = null;

		if (re != null) {
			etalonObj = (Etalon) DataStreamableUtil.readDataStreamableFromBA(
					re.getDadaraEtalon(),
					Etalon.getDSReader());
			etalonBS = new BellcoreReader().getData(re.getReflectogramma());
		}

		if (etalonObj == null || etalonBS == null) {
			System.err.println("Malformed etalon: "
					+ (etalonObj == null ? "no etalonObj" : "")
					+ (etalonBS == null ? "no etalonBS" : ""));
			GUIUtil.showErrorMessage(GUIUtil.MSG_ERROR_MALFORMED_ETALON);
			return false;
		}
		etalonBS.title =  makeEtalonRefName(getMEbyMS(ms), ms.getModified());// XXX: ms.getCreated()?
		if (asPrimary) {
			// ��������� ��� ��������� �/� (�� ������ ���������)
			ModelTraceAndEventsImpl mtae = etalonObj.getMTM().getMTAE();
			int traceLen = mtae.getModelTrace().getLength();
			Heap.openPrimaryTrace(new Trace(new PFTrace(etalonBS),
					etalonBS.title,
					new AnalysisResult(traceLen, traceLen, mtae) // XXX: �� ����� ����� �������� ������������� � ���������� ��, ��� ����� �������� �� MTAE
					));
			// ������������� refAnalysis � ��������� ��������� �� ��������
			// primary trace + ref analysis
			Heap.updatePrimaryAnalysis();
		} else {
			Heap.setEtalonPair(new PFTrace(etalonBS), etalonObj, etalonBS.title);
			//Heap.setEtalonEtalonMetas(metas);
		}
		return true;
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

		PFTrace trace = Heap.getPFTraceEtalon();

		ptype = ParameterType.REFLECTOGRAMMA_ETALON;
		params[1] = Parameter.createInstance(ptype,
				new BellcoreWriter().write(trace.getBS()));

		ParameterSet etalon = ParameterSet.createInstance(
				userId,
				ParameterSetSort.SET_SORT_ETALON,
				"",
				params,
				meIds);
		return etalon;
	}

	/**
	 * @param ev ������� DetailedEvent
	 * @return ���������� �������� ������ ������� (����., ������ �� ������)
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
	 * @param eventType ��� SimpleReflectogramEvent �������
	 * @return ����� ��� ������ ������� (�� ����������)
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
