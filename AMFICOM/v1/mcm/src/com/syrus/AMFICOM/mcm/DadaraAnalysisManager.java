package com.syrus.AMFICOM.mcm;

import java.util.Map;
import java.util.Hashtable;
import java.io.IOException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.Threshold;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramComparer;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramAlarm;
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.AMFICOM.measurement.ParameterTypeDatabase;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.event.corba.AlarmLevel;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreReader;
import com.syrus.util.ByteArray;
import com.syrus.util.Log;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.FileOutputStream;

public class DadaraAnalysisManager implements AnalysisManager, EvaluationManager {
	public static final String CODENAME_REFLECTOGRAMMA = "reflectogramma";
	public static final String CODENAME_DADARA_TACTIC = "ref_uselinear";
	public static final String CODENAME_DADARA_EVENT_SIZE = "ref_eventsize";
	public static final String CODENAME_DADARA_CONN_FALL_PARAMS = "ref_conn_fall_params";
	public static final String CODENAME_DADARA_MIN_LEVEL = "ref_min_level";
	public static final String CODENAME_DADARA_MAX_LEVEL_NOISE = "ref_max_level_noise";
	public static final String CODENAME_DADARA_MIN_LEVEL_TO_FIND_END = "ref_min_level_to_find_end";
	public static final String CODENAME_DADARA_MIN_WELD = "ref_min_weld";
	public static final String CODENAME_DADARA_MIN_CONNECTOR = "ref_min_connector";
	public static final String CODENAME_DADARA_STRATEGY = "ref_strategy";
	public static final String CODENAME_DADARA_EVENT_ARRAY = "dadara_event_array";
	public static final String CODENAME_DADARA_ETALON_EVENT_ARRAY = "dadara_etalon_event_array";
	public static final String CODENAME_DADARA_THRESHOLDS = "dadara_thresholds";
	public static final String CODENAME_DADARA_ALARM_ARRAY = "dadara_alarm_array";

	private static Map inParameterTypes;
	private static Map outParameterTypes;

	private Map parameters;
	private AlarmLevel alarmLevel;

	static {
    try {
      System.loadLibrary("dadara");
    }
    catch (UnsatisfiedLinkError ule) {
      Log.errorMessage(ule.getMessage());
    }

		inParameterTypes = new Hashtable(13);
		addInParameterType(CODENAME_REFLECTOGRAMMA);
		addInParameterType(CODENAME_DADARA_TACTIC);
		addInParameterType(CODENAME_DADARA_EVENT_SIZE);
		addInParameterType(CODENAME_DADARA_CONN_FALL_PARAMS);
		addInParameterType(CODENAME_DADARA_MIN_LEVEL);
		addInParameterType(CODENAME_DADARA_MAX_LEVEL_NOISE);
		addInParameterType(CODENAME_DADARA_MIN_LEVEL_TO_FIND_END);
		addInParameterType(CODENAME_DADARA_MIN_WELD);
		addInParameterType(CODENAME_DADARA_MIN_CONNECTOR);
		addInParameterType(CODENAME_DADARA_STRATEGY);
		addInParameterType(CODENAME_DADARA_EVENT_ARRAY);
		addInParameterType(CODENAME_DADARA_ETALON_EVENT_ARRAY);
		addInParameterType(CODENAME_DADARA_THRESHOLDS);
		outParameterTypes = new Hashtable(2);
		addOutParameterType(CODENAME_DADARA_EVENT_ARRAY);
		addOutParameterType(CODENAME_DADARA_ALARM_ARRAY);
	}

	public DadaraAnalysisManager(Result measurementResult,
															 Analysis analysis,
															 Evaluation evaluation,
															 Set etalon) throws AnalysisException {
		this.parameters = new Hashtable(13);
		this.addSetParameters(measurementResult.getParameters());
		this.addSetParameters(analysis.getCriteriaSet().getParameters());
		this.addSetParameters(evaluation.getThresholdSet().getParameters());
		this.addSetParameters(etalon.getParameters());
  }

  public DadaraAnalysisManager(Result measurementResult,
															 Analysis analysis,
															 Set etalon) throws AnalysisException {
		this.parameters = new Hashtable(12);
		this.addSetParameters(measurementResult.getParameters());
		this.addSetParameters(analysis.getCriteriaSet().getParameters());
		this.addSetParameters(etalon.getParameters());
  }

  public native double[] ana(int dadaraTactic,
														 double[] reflectogramma,
														 double dx,
														 double dadaraConnFallParams,
														 double dadaraMinLevel,
														 double dadaraMaxLevelNoise,
														 double dadaraMinLevelToFindEnd,
														 double dadaraMinWeld,
														 double dadaraMinConnector,
														 int dadaraStrategy);       

	public SetParameter[] analyse() throws AnalysisException {
		byte[] rawData = (byte[])this.parameters.get(CODENAME_REFLECTOGRAMMA);
		if (rawData == null) 
			throw new AnalysisException("Cannot get parameter of codename: '" + CODENAME_REFLECTOGRAMMA + "'  from map");
		BellcoreStructure bs = (new BellcoreReader()).getData(rawData);
		double[] reflectogramma = new double[bs.dataPts.TNDP];
    for (int i = 0; i < bs.dataPts.TPS[0]; i++)
      reflectogramma[i] = (double)(65535 - bs.dataPts.DSF[0][i])/1000d;
		double dx = (double)(bs.fxdParams.AR - bs.fxdParams.AO)*3d*1000d / ((double)bs.dataPts.TNDP * (double)bs.fxdParams.GI);
//----------------
try {
	FileOutputStream fos;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
	String tstr = sdf.format(new Date(System.currentTimeMillis()));
	fos = new FileOutputStream("./logs/" + tstr + "-reflectogramma.sor");
	fos.write((byte[])rawData);
	fos.close();
}
catch (IOException ioe) {
	System.out.println(ioe.getMessage());
	ioe.printStackTrace();
}
//----------------
		int dadaraTactic;
		int dadaraEventSize;
		double dadaraConnFallParams;
		double dadaraMinLevel;
		double dadaraMaxLevelNoise;
		double dadaraMinLevelToFindEnd;
		double dadaraMinWeld;
		double dadaraMinConnector;
		int dadaraStrategy;
		try {
			dadaraTactic = (new ByteArray((byte[])this.parameters.get(CODENAME_DADARA_TACTIC))).toInt();
			dadaraEventSize = (new ByteArray((byte[])this.parameters.get(CODENAME_DADARA_EVENT_SIZE))).toInt();
			dadaraConnFallParams = (new ByteArray((byte[])this.parameters.get(CODENAME_DADARA_CONN_FALL_PARAMS))).toDouble();
			dadaraMinLevel = (new ByteArray((byte[])this.parameters.get(CODENAME_DADARA_MIN_LEVEL))).toDouble();
			dadaraMaxLevelNoise = (new ByteArray((byte[])this.parameters.get(CODENAME_DADARA_MAX_LEVEL_NOISE))).toDouble();
			dadaraMinLevelToFindEnd = (new ByteArray((byte[])this.parameters.get(CODENAME_DADARA_MIN_LEVEL_TO_FIND_END))).toDouble();
			dadaraMinWeld = (new ByteArray((byte[])this.parameters.get(CODENAME_DADARA_MIN_WELD))).toDouble();
			dadaraMinConnector = (new ByteArray((byte[])this.parameters.get(CODENAME_DADARA_MIN_CONNECTOR))).toDouble();
			dadaraStrategy = (new ByteArray((byte[])this.parameters.get(CODENAME_DADARA_STRATEGY))).toInt();
		}
		catch(IOException ioe) {
			Log.errorException(ioe);
			throw new AnalysisException("Cannot get some of analysis criteria", ioe);
		}
//-------------------------------------------------------------
Log.debugMessage("$$$$$$$$$ DadaraAnalysisManager.analyse:", Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ Number of points: " + reflectogramma.length, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ dx == " + dx, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ dadaraTactic == " + dadaraTactic, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ dadaraEventSize == " + dadaraEventSize, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ dadaraConnFallParams == " + dadaraConnFallParams, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ dadaraMinLevel == " + dadaraMinLevel, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ dadaraMaxLevelNoise == " + dadaraMaxLevelNoise, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ dadaraMinLevelToFindEnd == " + dadaraMinLevelToFindEnd, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ dadaraMinWeld == " + dadaraMinWeld, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ dadaraMinConnector == " + dadaraMinConnector, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ dadaraStrategy == " + dadaraStrategy, Log.DEBUGLEVEL05);
//-------------------------------------------------------------
		double[] tmp = ana(dadaraTactic,
											 reflectogramma,
											 dx,
											 dadaraConnFallParams,
											 dadaraMinLevel,
											 dadaraMaxLevelNoise,
											 dadaraMinLevelToFindEnd,
											 dadaraMinWeld,
											 dadaraMinConnector,
											 dadaraStrategy);
		int nEvents = tmp.length / ReflectogramEvent.NUMBER_OF_PARAMETERS;
		ReflectogramEvent[] revents = new ReflectogramEvent[nEvents];
		for(int i = 0; i < nEvents; i++) {
			revents[i] = new ReflectogramEvent();
			revents[i].setParams(tmp, i * ReflectogramEvent.NUMBER_OF_PARAMETERS);
			revents[i].setDeltaX(dx);
		}
//----------
Log.debugMessage("$$$$$$$$$ Number of events == " + revents.length + "; tmp.length == " + tmp.length + ", N of pars == " + ReflectogramEvent.NUMBER_OF_PARAMETERS, Log.DEBUGLEVEL05);
//----------
//******************
		//Here we get etalon parameters
		byte[] etalonData = (byte[])this.parameters.get(CODENAME_DADARA_ETALON_EVENT_ARRAY);
		ReflectogramEvent[] etalon = ReflectogramEvent.fromByteArray(etalonData);

		//correct end of trace
		int delta = 5;
		if (revents.length > etalon.length)	{
			ReflectogramEvent endEvent = etalon[etalon.length - 1];
			for (int i = revents.length - 1; i >= 0; i--)	{
				if (revents[i].getType() == ReflectogramEvent.CONNECTOR &&
						Math.abs(revents[i].begin - endEvent.begin) < delta &&
						Math.abs(revents[i].end - endEvent.end) < delta) {
						ReflectogramEvent[] newRevents = new ReflectogramEvent[i+1];
						for (int j = 0; j < i + 1; j++)
							newRevents[j] = revents[j];
						revents = newRevents;
						break;
					}
			}
		}

		//correct event types
		if (revents.length == etalon.length) {
			for (int i = 0; i < etalon.length; i++)	{
				if (Math.abs(revents[i].begin - etalon[i].begin) < delta &&
						Math.abs(revents[i].end - etalon[i].end) < delta)	{
					revents[i].setType(etalon[i].getType());
				}
			}
		}
//******************
		Identifier identifier = MeasurementControlModule.getNewIdentifier(ObjectEntities.RESULTPARAMETER_ENTITY);
		ParameterType parTypEventArray = (ParameterType)outParameterTypes.get(CODENAME_DADARA_EVENT_ARRAY);
		if (parTypEventArray == null)
			throw new AnalysisException("Cannot find in output map parameter type of codename: '" + CODENAME_DADARA_EVENT_ARRAY + "'");
		SetParameter[] arParameters = new SetParameter[1];
		arParameters[0] = new SetParameter(identifier,
																			 parTypEventArray.getId(),
																			 ReflectogramEvent.toByteArray(revents));

		this.addSetParameters(arParameters);

		return arParameters;
	}

	public SetParameter[] evaluate() throws EvaluationException {
		byte[] etalonData = (byte[])this.parameters.get(CODENAME_DADARA_ETALON_EVENT_ARRAY);
		ReflectogramEvent[] etalon = ReflectogramEvent.fromByteArray(etalonData);
		byte[] thresholdData = (byte[])this.parameters.get(CODENAME_DADARA_THRESHOLDS);
		Threshold[] ts = Threshold.fromByteArray(thresholdData);

		ReflectogramComparer reflComparer;
		byte[] reventsData = (byte[])this.parameters.get(CODENAME_DADARA_EVENT_ARRAY);
		if (reventsData != null) {
			ReflectogramEvent[] revents = ReflectogramEvent.fromByteArray(reventsData);
			reflComparer = new ReflectogramComparer(revents,
																							etalon,
																							ts,
																							false);
		}
		else {
			Log.debugMessage("No analysis result, evaluating reflectogramma itself", Log.DEBUGLEVEL05);
			byte[] rawData = (byte[])this.parameters.get(CODENAME_REFLECTOGRAMMA);
			if (rawData == null) 
				throw new EvaluationException("Cannot get parameter of codename: '" + CODENAME_REFLECTOGRAMMA + "'  from map");
			BellcoreStructure bs = (new BellcoreReader()).getData(rawData);
			double[] reflectogramma = new double[bs.dataPts.TNDP];
			for (int i = 0; i < bs.dataPts.TPS[0]; i++)
				reflectogramma[i] = (double)(65535 - bs.dataPts.DSF[0][i])/1000d;
			reflComparer = new ReflectogramComparer(reflectogramma,
																							etalon,
																							ts,
																							false);
		}

		ReflectogramAlarm[] ralarms = reflComparer.getAlarms();

		SetParameter[] erParameters;
		if (ralarms.length > 0) {
			this.alarmLevel = AlarmLevel.ALARM_LEVEL_SOFT;
			for (int i = 0; i < ralarms.length; i++)
				if (ralarms[i].level == ReflectogramAlarm.LEVEL_HARD) {
					this.alarmLevel = AlarmLevel.ALARM_LEVEL_HARD;
					break;
				}
			Identifier identifier = MeasurementControlModule.getNewIdentifier(ObjectEntities.RESULTPARAMETER_ENTITY);
			ParameterType parTypAlarmArray = (ParameterType)outParameterTypes.get(CODENAME_DADARA_ALARM_ARRAY);
			if (parTypAlarmArray == null)
				throw new EvaluationException("Cannot find in output map parameter type of codename: '" + CODENAME_DADARA_ALARM_ARRAY + "'");
			erParameters = new SetParameter[1];
			erParameters[0] = new SetParameter(identifier,
																				 parTypAlarmArray.getId(),
																				 ReflectogramAlarm.toByteArray(ralarms));
		}
		else {
			this.alarmLevel = AlarmLevel.ALARM_LEVEL_NONE;
			erParameters = new SetParameter[0];
		}

		return erParameters;
	}

	public AlarmLevel getAlarmLevel() {
		return this.alarmLevel;
	}

	private void addSetParameters(SetParameter[] setParameters) throws AnalysisException {
		for (int i = 0; i < setParameters.length; i++)
			this.addParameter(setParameters[i]);
	}

	private void addParameter(SetParameter parameter) throws AnalysisException {
		Identifier parTypId = parameter.getTypeId();
		ParameterType parTyp = (ParameterType)inParameterTypes.get(parTypId);
		if (parTyp != null) {
			String parTypCodename = parTyp.getCodename();
			if (!this.parameters.containsKey(parTypCodename))
				this.parameters.put(parTypCodename, parameter.getValue());
			else
				Log.errorMessage("Parameter of codename '" + parTypCodename + "' already added to map; id: '" + parameter.getId() + "'");
		}
		throw new AnalysisException("Cannot find in input map parameter type of id: '" + parTypId + "'");
	}

	private static void addInParameterType(String codename) {
		try {
			ParameterType parTyp = ParameterTypeDatabase.retrieveForCodename(codename);
			Identifier parTypId = parTyp.getId();
			if (!inParameterTypes.containsKey(parTypId))
				inParameterTypes.put(parTypId, parTyp);
			else
				Log.errorMessage("Input parameter type of codename '" + codename + "' and id '" + parTypId.toString() + "' already added to map");
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
		}
	}

	private static void addOutParameterType(String codename) {
		try {
			ParameterType parTyp = ParameterTypeDatabase.retrieveForCodename(codename);
			if (!outParameterTypes.containsKey(codename))
				outParameterTypes.put(codename, parTyp);
			else
				Log.errorMessage("Output parameter type of codename '" + codename + "' and id '" + parTyp.getId().toString() + "' already added to map");
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
		}
	}
}