/*
 * $Id: DadaraAnalysisManager.java,v 1.10 2004/07/28 16:19:14 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Map;
import java.util.Hashtable;
import java.io.IOException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.NewIdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.Threshold;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramComparer;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramAlarm;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.AMFICOM.measurement.MeasurementObjectTypePool;
import com.syrus.AMFICOM.measurement.ParameterType;
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

/**
 * @version $Revision: 1.10 $, $Date: 2004/07/28 16:19:14 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

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

	private Map parameters;//Map <String codename, SetParameter parameter>
	private AlarmLevel alarmLevel;

	static {
    try {
      System.loadLibrary("dadara");
    }
    catch (UnsatisfiedLinkError ule) {
      Log.errorMessage(ule.getMessage());
    }
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
														 int dadaraStrategy,
														 int reflectiveSize,
														 int nonReflectiveSize);       

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

		int reflSize = ReflectogramMath.getReflectiveEventSize(reflectogramma, 0.5);
		int nReflSize = ReflectogramMath.getNonReflectiveEventSize(reflectogramma,
																															 1000,
																															 ((double)bs.fxdParams.GI) / 100000d,
																															 dx);
		if (nReflSize > 3 * reflSize / 5)
			nReflSize = 3 * reflSize / 5;
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
Log.debugMessage("$$$$$$$$$ reflSize == " + reflSize, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ nReflSize == " + nReflSize, Log.DEBUGLEVEL05);
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
											 dadaraStrategy,
											 reflSize,
											 nReflSize);
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
		Identifier identifier;
		try {
			identifier = NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.RESULTPARAMETER_ENTITY, 10);
		}
		catch (Exception e) {
			throw new AnalysisException("Cannot generate identifier for events array -- " + e.getMessage(), e);
		}
		ParameterType parTypEventArray = (ParameterType)MeasurementObjectTypePool.getObjectType(CODENAME_DADARA_EVENT_ARRAY);
		if (parTypEventArray == null)
			throw new AnalysisException("Cannot find parameter type of codename: '" + CODENAME_DADARA_EVENT_ARRAY + "'");
		SetParameter[] arParameters = new SetParameter[1];
		try {
			arParameters[0] = new SetParameter(identifier,
																				 parTypEventArray,
																				 ReflectogramEvent.toByteArray(revents));
		}
		catch (Exception e) {
			throw new AnalysisException("Cannot create parametrer -- " + e.getMessage(), e);
		}
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
			Identifier identifier;
			try {
				identifier = NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.RESULTPARAMETER_ENTITY, 10);
			}
			catch (Exception e) {
				throw new EvaluationException("Cannot generate identifier for events array -- " + e.getMessage(), e);
			}
			ParameterType parTypAlarmArray = (ParameterType)MeasurementObjectTypePool.getObjectType(CODENAME_DADARA_ALARM_ARRAY);
			if (parTypAlarmArray == null)
				throw new EvaluationException("Cannot find parameter type of codename: '" + CODENAME_DADARA_ALARM_ARRAY + "'");
			erParameters = new SetParameter[1];
			try {
				erParameters[0] = new SetParameter(identifier,
																					 parTypAlarmArray,
																					 ReflectogramAlarm.toByteArray(ralarms));
			}
			catch (Exception e) {
				throw new EvaluationException("Cannot create parametrer -- " + e.getMessage(), e);
			}
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
		String codename = parameter.getType().getCodename();
		if (codename != null) {
			if (!this.parameters.containsKey(codename))
				this.parameters.put(codename, parameter.getValue());
			else
				Log.errorMessage("Parameter of codename '" + codename + "' already added to map; id: '" + parameter.getId() + "'");
		}
		throw new AnalysisException("Codename of parameter: '" + parameter.getId() + "' is NULL");
	}
}
