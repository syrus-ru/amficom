/*
 * $Id: DadaraAnalysisManager.java,v 1.3 2004/07/20 12:54:10 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.agent;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreReader;
import com.syrus.util.ByteArray;
import com.syrus.util.Log;

import java.util.Date;
import java.io.FileOutputStream;

/**
 * @version $Revision: 1.3 $, $Date: 2004/07/20 12:54:10 $
 * @author $Author: arseniy $
 * @module agent_v1
 */
public class DadaraAnalysisManager extends AnalysisManager {

	static {
		try {
			System.loadLibrary("dadara");
		}
		catch (UnsatisfiedLinkError ule) {
			Log.errorMessage(ule.getMessage());
		}
	}

	protected DadaraAnalysisManager() {}

	public native double[] ana(int waveletType,
														 double[] y,                  //the refl. itself
														 double dx,                  //dx
														 double connFallParams,       // ?
														 double min_level,            // ?
														 double max_level_noise,      // ?
														 double min_level_to_find_end,// ?
														 double min_weld,             // ?
														 double min_connector,        // ?
														 int strategy,               // the minuit strategy
														 int reflectiveSize,
														 int nonReflectiveSize);

	public Hashtable analyse(Hashtable criterias, Hashtable resultparameters, Hashtable etalonparameters) {
		BellcoreReader br = new BellcoreReader();
		BellcoreStructure bs = br.getData((byte[])resultparameters.get("reflectogramm"));
		double[] reflectogramma = new double[bs.dataPts.TNDP];
		for (int i = 0; i < bs.dataPts.TPS[0]; i++)
			reflectogramma[i] = (double)(65535 - bs.dataPts.DSF[0][i])/1000d;

//----------------
	try {
		FileOutputStream fos;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
		String tstr = sdf.format(new Date(System.currentTimeMillis()));
		fos = new FileOutputStream("./logs/" + tstr + "-reflectogramma.sor");
		Object refl = resultparameters.get("reflectogramm");
		fos.write((byte[])refl);
		fos.close();
	}
	catch (IOException ioe) {
		System.out.println(ioe.getMessage());
		ioe.printStackTrace();
	}
//----------------

		double dx = (double)(bs.fxdParams.AR - bs.fxdParams.AO)*3d*1000d / ((double)bs.dataPts.TNDP * (double)bs.fxdParams.GI);

		int useLinear;
		int eventsize;
		double connFallParams;
		double minLevel;
		double maxLevelNoise;
		double minLevelToFindEnd;
		double minWeld;
		double minConnector;
		int strategy;
		try {
			useLinear = (new ByteArray((byte[])criterias.get("ref_uselinear"))).toInt();
			eventsize = (new ByteArray((byte[])criterias.get("ref_eventsize"))).toInt();
			connFallParams = (new ByteArray((byte[])criterias.get("ref_conn_fall_params"))).toDouble();
			minLevel = (new ByteArray((byte[])criterias.get("ref_min_level"))).toDouble();
			maxLevelNoise = (new ByteArray((byte[])criterias.get("ref_max_level_noise"))).toDouble();
			minLevelToFindEnd = (new ByteArray((byte[])criterias.get("ref_min_level_to_find_end"))).toDouble();
			minWeld = (new ByteArray((byte[])criterias.get("ref_min_weld"))).toDouble();
			minConnector = (new ByteArray((byte[])criterias.get("ref_min_connector"))).toDouble();
			strategy = (new ByteArray((byte[])criterias.get("ref_strategy"))).toInt();
		}
		catch(IOException e) {
			Log.errorException(e);
			return null;
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
Log.debugMessage("$$$$$$$$$ useLinear == " + useLinear, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ eventsize == " + eventsize, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ connFallParams == " + connFallParams, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ minLevel == " + minLevel, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ maxLevelNoise == " + maxLevelNoise, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ minLevelToFindEnd == " + minLevelToFindEnd, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ minWeld == " + minWeld, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ minConnector == " + minConnector, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ strategy == " + strategy, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ reflSize == " + reflSize, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ nReflSize == " + nReflSize, Log.DEBUGLEVEL05);
//-------------------------------------------------------------
		//y = WorkWithReflectoEventsArray.correctReflectoArraySavingNoise(y);
		double[] tmp = ana(useLinear,
											 reflectogramma,
											 dx,
											 connFallParams,
											 minLevel,
											 maxLevelNoise,
											 minLevelToFindEnd,
											 minWeld,
											 minConnector,
											 strategy,
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
		ReflectogramEvent[] etalon = ReflectogramEvent.fromByteArray((byte[])etalonparameters.get("dadara_etalon_event_array"));

		int delta = 5;
		//correct end of trace
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
		Hashtable analysisresultparameters = new Hashtable(1);
		analysisresultparameters.put("dadara_event_array", ReflectogramEvent.toByteArray(revents));
		return analysisresultparameters;
	}
}
