package com.syrus.AMFICOM.Client.Analysis;

import java.util.Date;
import java.util.HashSet;
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
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Action;
import com.syrus.AMFICOM.measurement.ActionParameter;
import com.syrus.AMFICOM.measurement.ActionParameterTypeBinding;
import com.syrus.AMFICOM.measurement.ActionResultParameter;
import com.syrus.AMFICOM.measurement.ActionTemplate;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.AnalysisTypeCodename;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementPortType;
import com.syrus.AMFICOM.measurement.MeasurementResultParameter;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.ModelingResultParameter;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.reflectometry.MeasurementReflectometryAnalysisResult;
import com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisCriteria;
import com.syrus.AMFICOM.reflectometry.ReflectometryMeasurementSetup;
import com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreWriter;
import com.syrus.io.DataFormatException;
import com.syrus.util.EasyDateFormatter;
import com.syrus.util.Log;

/**
 Class with methods used to save/load measuring parameters onto server
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Syrus Systems</p>
 * @author Stanislav Kholshin
 */

public class AnalysisUtil {
	private AnalysisUtil()
	{ // non-instantiable
	}

	/**
	 * ���������� action �� ��� ����������.
	 * XXX: ���� �� ������ ����� ApplicationException?
	 */
	public static <T extends Action> T getActionByResult(
			ActionResultParameter<T> result) {
		try {
			return result.getAction();
		} catch (ApplicationException e) {
			// XXX: ������������, ��� ����� ������ ���� �� ������
			Log.errorMessage(e);
			throw new InternalError(e.toString());
		}
	}

	/**
	 * ������� �������������� �� ���������� ��������� �
	 * ���������� � ��� title, meId, measId
	 * @throws SimpleApplicationException, ���� �������������� � ��������� ���������� ���
	 */
	public static BellcoreStructure getBellcoreStructureFromMeasurementResult(
			MeasurementResultParameter result1) throws SimpleApplicationException {
		return getBellcoreStructureFromResult(result1);
	}

	/**
	 * ������� �������������� �� ���������� ������������� �
	 * ���������� � ��� title, meId, measId
	 * @throws SimpleApplicationException, ���� �������������� � ��������� ���������� ���
	 */
	public static BellcoreStructure getBellcoreStructureFromModelingResult(
			ModelingResultParameter result1) throws SimpleApplicationException {
		return getBellcoreStructureFromResult(result1);
	}

	private static <T extends Action> BellcoreStructure getBellcoreStructureFromResult(
			ActionResultParameter<T> result1)
	throws SimpleApplicationException {
		BellcoreStructure bs = new BellcoreReader().getData(result1.getValue());

		if (bs == null)
			throw new SimpleApplicationException(SimpleApplicationException.KEY_NULL_REFLECTOGRAMMA);

		T m = getActionByResult(result1);
		bs.title = m.getName();
		bs.monitoredElementId = m.getMonitoredElementId().getIdentifierString();
		bs.measurementId = m.getId().getIdentifierString();

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
	public static AnalysisResult getAnalysisResultForMeasurementResultIfPresent(MeasurementResultParameter result)
	throws DataFormatException, ApplicationException {
		Measurement m = getActionByResult(result);
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
		// ������� ������(�):
		Analysis analysis = m.getAnalysis();
		if (analysis == null) {
			return null;
		}
		// �������� ��������� �������(-��):
		final Parameter parameter = analysis.getActionResultParameter(ParameterType.valueOf(ReflectometryParameterTypeCodename.DADARA_ANALYSIS_RESULT));
		if (parameter == null) {
			return null;
		}
		return (AnalysisResult) DataStreamableUtil.readDataStreamableFromBA(
				parameter.getValue(),
				AnalysisResult.getDSReader());
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
	 * ��������� ��������� ������� ��������� �������.
	 * ���� � ������� ��� ��������� �������, ������ �� ������.
	 *
	 * @param ms MeasurementSetup ������
	 * @throws DataFormatException �������� ������ ������
	 * ���������� � ������� ��������� �������
	 * @throws ApplicationException ��� ������� ����
	 */
	public static void loadAnalysisParameters(MeasurementSetup ms)
	throws DataFormatException, ApplicationException {
		AnalysisParameters analysisParams = getCriteriaSetByMeasurementSetup(ms);
		if (analysisParams != null) {
			loadAnalysisParameters(analysisParams);
		}
	}

	/**
	 * ��������� ��������� ��������� �������
	 * @param analysisParams ��������� ��������� �������, not null
	 */
	public static void loadAnalysisParameters(AnalysisParameters analysisParams) {
		assert analysisParams != null;
		Heap.setMinuitAnalysisParams(analysisParams);
		Heap.setMinuitInitialParamsFromCurrentAP();
		Heap.notifyAnalysisParametersUpdated();
	}

	/**
	 * ���������� �������� ������� ������� MeasurementSetup ����
	 * null, ���� �������� ���.
	 * @param ms MeasurementSetup
	 * @return �������� ������� ������� MeasurementSetup ����
	 * null, ���� �������� ���
	 * @throws DataFormatException ��� ������� ������� ������
	 * @throws ApplicationException ��� ������� ����
	 */
	public static AnalysisParameters getCriteriaSetByMeasurementSetup(
			MeasurementSetup ms)
	throws DataFormatException, ApplicationException {
		ReflectometryAnalysisCriteria ac =
				new ReflectometryMeasurementSetup(ms).getAnalysisCriteria();
		if (ac != null) {
			AnalysisParameters analysisParams = (AnalysisParameters)
					DataStreamableUtil.readDataStreamableFromBA(
							ac.getDadaraCriteria(),
							AnalysisParameters.getReader());
			return analysisParams;
		}
		return null;
	}

	/**
	 * ����������, ��� � ������ ms ���� ���� � ������ ���� me,
	 * � ���������� ���� me. ������������ ��� �������������
	 * ��� ��������� ����� �������, ���� ���-��� ������������ �� �� ����������.
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
		return loadEtalonEx(ms, true, false);
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
		return loadEtalonEx(ms, false, true);
	}

	/**
	 * ��������� ������ ������ - ��� ��������� �/� � ��� ������.
	 * � ������������� �� ������������� (�� ����������).
	 * @param ms
	 * @throws ApplicationException 
	 * @throws DataFormatException 
	 */
	public static boolean loadEtalonAsEtalonAndAsPrimary(MeasurementSetup ms)
	throws DataFormatException, ApplicationException {
		return loadEtalonEx(ms, true, true);
	}

	/**
	 * ��������� � Heap ������ �� ������� MeasurementSetup.
	 * ���� �������� �������, ��������� true.
	 * ���� �������� �� �������, ������� ������������ ��������� �� ������
	 * � ���������� false.
	 * @todo ��������, ����� �� ���������� false, � ������� ����������
	 * @todo �� ������� ������, ������������ �� ����� �� MeasurementSetup, � ReflectometryAnalysisCriteria
	 * @param ms ������ MeasurementSetup
	 * @param asPrimary
	 *   true, ����� ��������� ��� ��������� �/� (����������� �� ������ ���������),
	 *   false, ����� ��������� ��� ������ (���������).
	 * @return true - �������� �������; false - �������� �� �������, �
	 *   ��������� �� ������ �������� �������� ������������.
	 * @throws DataFormatException Etalon object decoding failed
	 * @throws ApplicationException Error while getting ME
	 */
	private static boolean loadEtalonEx(MeasurementSetup ms,
			boolean asEtalon,
			boolean asPrimary)
	throws DataFormatException, ApplicationException {
		final ReflectometryMeasurementSetup rms =
			new ReflectometryMeasurementSetup(ms);
		final ReflectometryAnalysisCriteria criteria = rms.getAnalysisCriteria();

		BellcoreStructure etalonBS = null;
		Etalon etalonObj = null;

		if (criteria != null && criteria.hasEtalon()) {
			etalonObj = (Etalon) DataStreamableUtil.readDataStreamableFromBA(
					criteria.getDadaraEtalon(),
					Etalon.getDSReader());
			etalonBS = new BellcoreReader().getData(
					criteria.getReflectogrammaEtalon());
		}

		if (etalonObj == null || etalonBS == null) {
			System.err.println("Malformed etalon: "
					+ (etalonObj == null ? "no etalonObj" : "")
					+ (etalonBS == null ? "no etalonBS" : ""));
			GUIUtil.showErrorMessage(GUIUtil.MSG_ERROR_MALFORMED_ETALON);
			return false;
		}
		etalonBS.title =  makeEtalonRefName(getMEbyMS(ms), ms.getModified());// XXX: ms.getCreated()?
		final PFTrace pfTrace = new PFTrace(etalonBS);
		if (asPrimary) {
			// ��������� ��� ��������� �/� (�� ������ ���������)
			ModelTraceAndEventsImpl mtae = etalonObj.getMTM().getMTAE();
			int traceLen = mtae.getModelTrace().getLength();
			Heap.openPrimaryTrace(new Trace(pfTrace,
					etalonBS.title,
					new AnalysisResult(traceLen, traceLen, mtae) // XXX: �� ����� ����� �������� ������������� � ���������� ��, ��� ����� �������� �� MTAE
					));
			// ������������� refAnalysis � ��������� ��������� �� ��������
			// primary trace + ref analysis
			Heap.updatePrimaryAnalysis();
		}
		if (asEtalon) {
			Heap.setEtalonPair(pfTrace, etalonObj, etalonBS.title);
			//Heap.setEtalonEtalonMetas(metas);
		}
		return true;
	}

	private static ActionParameter createParameterForAnalysisParameters(
			Identifier userId,
			MeasurementPortType portType,
			AnalysisParameters analysisParams) throws ApplicationException {
		return ActionParameter.createInstance(userId,
				DataStreamableUtil.writeDataStreamableToBA(analysisParams),
				ActionParameterTypeBinding.valueOf(
						ParameterType.valueOf(ReflectometryParameterTypeCodename.DADARA_CRITERIA),
						AnalysisType.valueOf(AnalysisTypeCodename.DADARA),
					portType).getId());
	}

	private static ActionParameter createParameterForEtalon(
			Identifier userId,
			MeasurementPortType portType,
			Etalon etalon) throws ApplicationException {
		return ActionParameter.createInstance(userId,
				DataStreamableUtil.writeDataStreamableToBA(etalon),
				ActionParameterTypeBinding.valueOf(
						ParameterType.valueOf(ReflectometryParameterTypeCodename.DADARA_ETALON),
						AnalysisType.valueOf(AnalysisTypeCodename.DADARA),
					portType).getId());
	}

	private static ActionParameter createParameterForEtalonTrace(
			Identifier userId,
			MeasurementPortType portType,
			BellcoreStructure bellcore) throws ApplicationException {
		return ActionParameter.createInstance(userId,
				new BellcoreWriter().write(bellcore),
				ActionParameterTypeBinding.valueOf(
						ParameterType.valueOf(ReflectometryParameterTypeCodename.REFLECTOGRAMMA_ETALON),
						AnalysisType.valueOf(AnalysisTypeCodename.DADARA),
					portType).getId());
	}

	/**
	 * ������� ������ �������, �������, ���� ���������, � ��������� � ��������.
	 * ��������� ������� � ������ ������� �� Heap.
	 * @param userId
	 * @param portType ��� �����, �� ������� ����� ����������� ���� ������
	 *  (� ������ ������� �� ������� �� ���� �����, ����������������
	 *  �������, �� �������� ��������� ���������)
	 * @param meIds ��������� MonitoredElements (���������� ������� ����
	 *   ������ ������� �� ���������������� ����� ��������� � ����� �����)
	 * @param withEtalon true, ����� � ����������� ������ �������� ������
	 * @return ��������� ������ �������
	 * @throws ApplicationException ������ SOF.
	 */
	public static ActionTemplate<Analysis> createAnalysisTemplate(Identifier userId,
			MeasurementPortType portType,
			Set<Identifier> meIds,
			boolean withEtalon) throws ApplicationException {

		Set<Identifier> parameterIds = new HashSet<Identifier>();

		{
			AnalysisParameters analysisParams = Heap.getMinuitAnalysisParams();
			if (analysisParams == null)
				analysisParams = Heap.getMinuitDefaultParams();
			
			parameterIds.add(createParameterForAnalysisParameters(userId,
					portType,
					analysisParams).getId());
		}

		if (withEtalon) {
			parameterIds.add(createParameterForEtalon(userId,
					portType,
					Heap.getEtalon()).getId());
			parameterIds.add(createParameterForEtalonTrace(userId,
					portType,
					Heap.getPFTraceEtalon().getBS()).getId());
		}

		ActionTemplate<Analysis> analysisTemplate = ActionTemplate.createInstance(
				userId,
				"",
				0, // XXX: estimated duration: no estimation by the moment
				parameterIds,
				meIds);
		return analysisTemplate;
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

	/**
	 * ���������� ������, �� �������� ���� ��������� ������ ���������, not null
	 * @param m ������ ���������
	 * @return ������, �� �������� ���� ��������� ������ ���������, not null
	 * @throws ApplicationException ������ �������� �� Pool'�
	 */
	public static MeasurementSetup getMSbyM(Measurement m)
	throws ApplicationException {
		assert m != null : ErrorMessages.NON_NULL_EXPECTED;
		Test test = m.getTest();
		final MeasurementSetup ms = test.getCurrentMeasurementSetup();
		assert(ms != null);
		return ms;
	}
}
