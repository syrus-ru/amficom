/*
 * $Id: DadaraAnalysisManager.java,v 1.21 2004/12/21 17:12:58 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.Threshold;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramComparer;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramAlarm;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
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
import com.syrus.AMFICOM.measurement.ParameterTypeCodenames;

/**
 * @version $Revision: 1.21 $, $Date: 2004/12/21 17:12:58 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public class DadaraAnalysisManager implements AnalysisManager, EvaluationManager {
	public static final String CODENAME_REFLECTOGRAMMA = "reflectogramma";
//	public static final String CODENAME_DADARA_TACTIC = "ref_uselinear";
//	public static final String CODENAME_DADARA_EVENT_SIZE = "ref_eventsize";
//	public static final String CODENAME_DADARA_CONN_FALL_PARAMS = "ref_conn_fall_params";
//	public static final String CODENAME_DADARA_MIN_LEVEL = "ref_min_level";
//	public static final String CODENAME_DADARA_MAX_LEVEL_NOISE = "ref_max_level_noise";
//	public static final String CODENAME_DADARA_MIN_LEVEL_TO_FIND_END = "ref_min_level_to_find_end";
//	public static final String CODENAME_DADARA_MIN_WELD = "ref_min_weld";
//	public static final String CODENAME_DADARA_MIN_CONNECTOR = "ref_min_connector";
//	public static final String CODENAME_DADARA_STRATEGY = "ref_strategy";
	public static final String CODENAME_DADARA_EVENT_ARRAY = "dadara_event_array";
	public static final String CODENAME_DADARA_ETALON_EVENT_ARRAY = "dadara_etalon_event_array";
	public static final String CODENAME_DADARA_THRESHOLDS = "dadara_thresholds";
	public static final String CODENAME_DADARA_ALARM_ARRAY = "dadara_alarm_array";

	private static Map outParameterTypeIds;	//Map <String codename, ParameterType parameterType>

	private Map parameters;	//Map <String codename, SetParameter parameter>
//	private AlarmLevel alarmLevel;

	private ReflectogramEvent[] rEvents = null;
	private ReflectogramAlarm[] rAlarms = null;

	static
	{
		outParameterTypeIds = new HashMap();
		addOutParameterTypeId(CODENAME_DADARA_EVENT_ARRAY);
		addOutParameterTypeId(CODENAME_DADARA_ALARM_ARRAY);
	}

	public DadaraAnalysisManager(Result measurementResult,
			Analysis analysis,
			Evaluation evaluation,
			Set etalon) throws AnalysisException
	{
		this.parameters = new HashMap(13);
		this.addSetParameters(measurementResult.getParameters());
		this.addSetParameters(analysis.getCriteriaSet().getParameters());
		this.addSetParameters(evaluation.getThresholdSet().getParameters());
		this.addSetParameters(etalon.getParameters());
		
		analyseAndEvaluate(true, true, true);
	}

	public DadaraAnalysisManager(Result measurementResult,
			Evaluation evaluation,
			Set etalon) throws AnalysisException
	{
		this.parameters = new HashMap(13);
		this.addSetParameters(measurementResult.getParameters());
		this.addSetParameters(evaluation.getThresholdSet().getParameters());
		this.addSetParameters(etalon.getParameters());

		analyseAndEvaluate(false, false, true);
	}

	public DadaraAnalysisManager(Result measurementResult,
			Analysis analysis,
			Set etalon) throws AnalysisException
	{
		this.parameters = new HashMap(12);
		this.addSetParameters(measurementResult.getParameters());
		this.addSetParameters(analysis.getCriteriaSet().getParameters());
		this.addSetParameters(etalon.getParameters());

		analyseAndEvaluate(true, false, false);
	}

	public SetParameter[] analyse()
		throws AnalysisException
	{
		return getAnalysisResult();
	}

	public SetParameter[] evaluate()
		throws EvaluationException
	{
		return getEvaluationResults();
	}

	private ByteArray getParBA(String codename)
	{
		return new ByteArray((byte[] )parameters.get(codename));
	}
	
	private BellcoreStructure obtainReflectogram()
		throws AnalysisException
	{
		byte[] rawData = (byte[])this.parameters.get(CODENAME_REFLECTOGRAMMA);
		if (rawData == null) 
			throw new AnalysisException("Cannot get parameter of codename: '" + CODENAME_REFLECTOGRAMMA + "'  from map");
		BellcoreStructure bs = (new BellcoreReader()).getData(rawData);
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
		return bs;
	}

	private ReflectogramEvent[] obtainEtalon()
		throws AnalysisException
	{
		// read etalon r/g and its thresholds
		byte[] etalonData = (byte[] )parameters.get(CODENAME_DADARA_ETALON_EVENT_ARRAY);
		if (etalonData == null)
			throw new AnalysisException("Cannot get etalonData");
		return
			ReflectogramEvent.fromByteArray(etalonData);
	}

	private Threshold[] obtainThresholds()
		throws AnalysisException
	{
		byte[] thresholdData = (byte[] )parameters.get(CODENAME_DADARA_THRESHOLDS);
		if (thresholdData == null)
			throw new AnalysisException("Cannot get thresholdData");
		return
			Threshold.fromByteArray(thresholdData);
	}

	private SetParameter[] getAnalysisResult()
		throws AnalysisException 
	{
		ParameterType parTypEventArray = null;
		try {
			parTypEventArray = (ParameterType)MeasurementStorableObjectPool
			.getStorableObject((Identifier)outParameterTypeIds.get(CODENAME_DADARA_EVENT_ARRAY), true);
		}
		catch (ApplicationException ae) {
			throw new AnalysisException("Cannot find parameter type of codename: '"
				+ CODENAME_DADARA_EVENT_ARRAY + "' -- " + ae.getMessage(), ae);
		}
		SetParameter[] arParameters = new SetParameter[1];
		try {
			arParameters[0] = SetParameter.createInstance(parTypEventArray,
				ReflectogramEvent.toByteArray(rEvents != null ? rEvents : new ReflectogramEvent[0]));
		}
		catch (Exception e) {
			throw new AnalysisException("Cannot create parameter -- " + e.getMessage(), e);
		}
		return arParameters;
		//this.addSetParameters(arParameters);
	}

	private void analyseAndEvaluate(
			boolean analysisResultsRequired,
			boolean eventListChangeAlarmsRequired,
			boolean modelFunctionsRangeBeyondThresholdsAlarmsRequired
			)
		throws AnalysisException//, EvaluationException
	{
		// make flags what we need to do
		boolean needToAnalyse = analysisResultsRequired | eventListChangeAlarmsRequired;
		boolean needsAnything = modelFunctionsRangeBeyondThresholdsAlarmsRequired | needToAnalyse;

		if (!needsAnything)
			return; // nothing to do!

		// get reflectogram
		BellcoreStructure bs = obtainReflectogram();

		if (needToAnalyse)
			makeAnalysis(bs);
		// else, rEvents stays null

		List alarmList = new ArrayList();

		ReflectogramEvent[] etEvents = obtainEtalon();

		if (eventListChangeAlarmsRequired)
			addEventListChangeAlarms(alarmList, etEvents);

		if (modelFunctionsRangeBeyondThresholdsAlarmsRequired)
			addModelFunctionRangeBeyondThresholdsAlarms(alarmList, bs, etEvents, obtainThresholds());

		rAlarms = (ReflectogramAlarm[] )alarmList.toArray(); 
	}

	private void makeAnalysis(BellcoreStructure bs)
	throws AnalysisException
	{
		int strategy;
		double[] pars;
		try
		{
			strategy = getParBA(ParameterTypeCodenames.STRATEGY).toInt();
			pars = new double[] {
					getParBA(ParameterTypeCodenames.MIN_EVENT_LEVEL).toDouble(),
					getParBA(ParameterTypeCodenames.MIN_SPLICE).toDouble(),
					getParBA(ParameterTypeCodenames.MIN_CONNECTOR).toDouble(),
					getParBA(ParameterTypeCodenames.MIN_END_LEVEL).toDouble(),
					getParBA(ParameterTypeCodenames.MAX_NOISE_LEVEL).toDouble(),
					0.0, // connector fall parameter: unused
					strategy,
					getParBA(ParameterTypeCodenames.WAVELET_TYPE).toInt()
			};
		}
		catch(IOException e)
		{
			throw new AnalysisException("Error converting analysis parameters");
		}

		ReflectogramEvent[] ret = CoreAnalysisManager.makeAnalysis(strategy, bs, pars, null);

//			----------
		Log.debugMessage("$$$$$$$$$ Number of events == " + ret.length, Log.DEBUGLEVEL05);
//			----------

		rEvents = correctAnalyseEvents(ret);
	}

	// TODO: rewrite!
	private void addEventListChangeAlarms(
			List alarmList,
			ReflectogramEvent[] etEvents)
	{
			ReflectogramComparer comparer = new ReflectogramComparer(
				null,
				rEvents,
				etEvents,
				null);

//			try
//			{
				final int LEVEL_HARD = ReflectogramAlarm.LEVEL_HARD;
				final int LEVEL_SOFT = ReflectogramAlarm.LEVEL_SOFT;
				for (int i = 0; i < etEvents.length; i++)
				{
					ReflectogramEvent et = etEvents[i];
					if (comparer.isEtalonEventLost(i))
					{
						alarmList.add(new ReflectogramAlarm(et.getBegin(), LEVEL_HARD));
					}
					else if (comparer.isEtalonEventChanged(i, ReflectogramComparer.CHANGETYPE_TYPE, 0.0))
					{
						alarmList.add(new ReflectogramAlarm(et.getBegin(), LEVEL_HARD));
					}
					else if(comparer.isEtalonEventChanged(i, ReflectogramComparer.CHANGETYPE_AMPL, 2.0) //TODO
							|| comparer.isEtalonEventChanged(i, ReflectogramComparer.CHANGETYPE_LOSS, 0.5)) //TODO
					{
						alarmList.add(new ReflectogramAlarm(et.getBegin(), LEVEL_SOFT));
					}
					// TODO: add comparison of beginning of events
				}
				for (int i = 0; i < rEvents.length; i++)
				{
					ReflectogramEvent pr = rEvents[i];
					if (comparer.isProbeEventNew(i))
					{
						alarmList.add(new ReflectogramAlarm(pr.getBegin(), LEVEL_HARD));
					}
				}
//			}
//			catch ()
//			{
//			}			
	}
	
	private void addModelFunctionRangeBeyondThresholdsAlarms(
			List alarmList,
			BellcoreStructure bs,
			ReflectogramEvent[] etEvents,
			Threshold[] etThresholds)
	{
		// make a working copy of etalon events.

		// we must be sure that etEvents.length == etThresholds.length
		ReflectogramEvent[] etalon = ReflectogramEvent.copyArray(etEvents);
		int length = Math.min(etEvents.length, etThresholds.length);
		for (int i = 0; i < length; i++)
			etalon[i].setThreshold(etThresholds[i]);

		//perform comparison
		double[] y = bs.getTraceData();
		for (int i = 0; i < length; i++)
		{
			ReflectogramEvent probe = etalon[i].createLinearlyFitted(y);
			// check for hard alarms
			int rc = etalon[i].isThatWithinMyThresholds(probe, false);
			if (rc >= 0)
			{
				alarmList.add(new ReflectogramAlarm(i, ReflectogramAlarm.LEVEL_HARD));
				continue;
			}
			// check for soft alarms
			rc = etalon[i].isThatWithinMyThresholds(probe, true);
			if (rc >= 0)
			{
				alarmList.add(new ReflectogramAlarm(i, ReflectogramAlarm.LEVEL_SOFT));
				continue;
			}
		}
	}

	private ReflectogramEvent[] correctAnalyseEvents(ReflectogramEvent[] rev)
	{
		// FIXME: now it is just a copy-pase + adaptation of old code

		//Here we get etalon parameters
		byte[] etalonData = (byte[])this.parameters.get(CODENAME_DADARA_ETALON_EVENT_ARRAY);
		ReflectogramEvent[] etalon = ReflectogramEvent.fromByteArray(etalonData);

		//correct end of trace
		int delta = 5;
		if (rev.length > etalon.length)	{
			ReflectogramEvent endEvent = etalon[etalon.length - 1];
			for (int i = rev.length - 1; i >= 0; i--) {
				if (rev[i].getEventType() == ReflectogramEvent.CONNECTOR &&
				        Math.abs(rev[i].getBegin() - endEvent.getBegin()) < delta &&
				        Math.abs(rev[i].getEnd() - endEvent.getEnd()) < delta) {
				    ReflectogramEvent[] newRevents = new ReflectogramEvent[i+1];
				    for (int j = 0; j < i + 1; j++)
				        newRevents[j] = rev[j];
				    rev = newRevents; // substitute new array
				    break;
				}
			}
		}

		//correct event types
		if (rev.length == etalon.length) {
			for (int i = 0; i < etalon.length; i++)	{
				if (Math.abs(rev[i].getBegin() - etalon[i].getBegin()) < delta &&
						Math.abs(rev[i].getEnd() - etalon[i].getEnd()) < delta) {
					rev[i].setEventType(etalon[i].getEventType());
				}
			}
		}

		return rev;
	}

	public AlarmLevel getAlarmLevel()
	{
		boolean haveSoftAlarms = false;
		for (int i = 0; i < rAlarms.length; i++)
		{
			if (rAlarms[i].level == ReflectogramAlarm.LEVEL_HARD)
				return AlarmLevel.ALARM_LEVEL_HARD;
			if (rAlarms[i].level == ReflectogramAlarm.LEVEL_SOFT)
				haveSoftAlarms = true;
		}
		if (haveSoftAlarms)
			return AlarmLevel.ALARM_LEVEL_SOFT;
		return AlarmLevel.ALARM_LEVEL_NONE;
	}

	public SetParameter[] getEvaluationResults()
		throws EvaluationException
	{
		SetParameter[] erParameters;
		if (rAlarms.length > 0)
		{
			ParameterType parTypAlarmArray = null;
			try {
				parTypAlarmArray = (ParameterType)MeasurementStorableObjectPool.getStorableObject((Identifier)outParameterTypeIds.get(CODENAME_DADARA_ALARM_ARRAY), true);
			}
			catch (ApplicationException ae) {
				throw new EvaluationException("Cannot find parameter type of codename: '" + CODENAME_DADARA_ALARM_ARRAY + "' -- " + ae.getMessage(), ae);
			}
			erParameters = new SetParameter[1];
			try {
				erParameters[0] = SetParameter.createInstance(parTypAlarmArray, ReflectogramAlarm.toByteArray(rAlarms));
			}
			catch (Exception e) {
				throw new EvaluationException("Cannot create parametrer -- " + e.getMessage(), e);
			}
		}
		else {
			erParameters = new SetParameter[0];
		}

		return erParameters;
	}

	private void addSetParameters(SetParameter[] setParameters) throws AnalysisException {
		for (int i = 0; i < setParameters.length; i++)
			this.addParameter(setParameters[i]);
	}

	private void addParameter(SetParameter parameter) throws AnalysisException {
		String codename = parameter.getType().getCodename();
		if (codename != null) {
			if (! this.parameters.containsKey(codename))
				this.parameters.put(codename, parameter.getValue());
			else
				Log.errorMessage("Parameter of codename '" + codename + "' already added to map; id: '" + parameter.getId() + "'");
		}
		throw new AnalysisException("Codename of parameter: '" + parameter.getId() + "' is NULL");
	}

	private static void addOutParameterTypeId(String codename) {
		ParameterTypeDatabase parameterTypeDatabase = ((ParameterTypeDatabase)MeasurementDatabaseContext.getParameterTypeDatabase());
		try {
			ParameterType parameterType = parameterTypeDatabase.retrieveForCodename(codename);
			Identifier id = parameterType.getId();
			if (! outParameterTypeIds.containsKey(codename)) {
				outParameterTypeIds.put(codename, id);
				MeasurementStorableObjectPool.putStorableObject(parameterType);
			}
			else
				Log.errorMessage("Out parameter type of codename '" + codename + "' already added to map; id: '" + parameterType.getId() + "'");
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}
}
