package com.syrus.AMFICOM.mcm;

import java.util.Hashtable;
import java.io.IOException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreReader;
import com.syrus.util.ByteArray;
import com.syrus.util.Log;

import java.text.SimpleDateFormat;
import java.io.FileOutputStream;

public class DadaraAnalysisManager extends AnalysisManager {
	public static final String CODENAME_REFLECTOGRAMMA = "reflectogramma";
	public static final String CODENAME_TACTIC = "ref_uselinear";
	public static final String CODENAME_EVENT_SIZE = "ref_eventsize";
	public static final String CODENAME_CONN_FALL_PARAMS = "ref_conn_fall_params";
	public static final String CODENAME_MIN_LEVEL = "ref_min_level";
	public static final String CODENAME_MAX_LEVEL_NOISE = "ref_max_level_noise";
	public static final String CODENAME_MIN_LEVEL_TO_FIND_END = "ref_min_level_to_find_end";
	public static final String CODENAME_MIN_WELD = "ref_min_weld";
	public static final String CODENAME_MIN_CONNECTOR = "ref_min_connector";
	public static final String CODENAME_STRATEGY = "ref_strategy";
	public static final String CODENAME_DADARA_EVENT_ARRAY = "dadara_event_array";

  static {
    try {
      System.loadLibrary("dadara");
    }
    catch (UnsatisfiedLinkError ule) {
      Log.errorMessage(ule.getMessage());
    }
  }

  public DadaraAnalysisManager() {
  }

  public native double[] ana(int waveletType,
														 double[] y,                  //the refl. itself
														 double d_x,                  //dx
														 double connFallParams,       // ?
														 double min_level,            // ?
														 double max_level_noise,      // ?
														 double min_level_to_find_end,// ?
														 double min_weld,             // ?
														 double min_connector,        // ?
														 int strategy);               // the minuit strategy

  public Hashtable analyse(Hashtable criterias, Hashtable resultparameters, Hashtable etalonparameters) {
    BellcoreReader br = new BellcoreReader();    
    BellcoreStructure bs = br.getData((byte[])resultparameters.get(CODENAME_REFLECTOGRAMMA));
    double[] y = new double[bs.dataPts.TNDP];
    for (int i = 0; i < bs.dataPts.TPS[0]; i++)
      y[i] = (double)(65535 - bs.dataPts.DSF[0][i])/1000d;

//----------------
	try {
		FileOutputStream fos;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
		String tstr = sdf.format(new Date(System.currentTimeMillis()));
		fos = new FileOutputStream("./logs/" + tstr + "-reflectogramma.sor");
		Object refl = resultparameters.get(CODENAME_REFLECTOGRAMMA);
		fos.write((byte[])refl);
		fos.close();
	}
	catch (IOException ioe) {
		System.out.println(ioe.getMessage());
		ioe.printStackTrace();
	}
//----------------

    double d_x = (double)(bs.fxdParams.AR - bs.fxdParams.AO)*3d*1000d / ((double)bs.dataPts.TNDP * (double)bs.fxdParams.GI);

    int useLinear;
		int eventsize;
		double connFallParams;
		double min_level;
		double max_level_noise;
		double min_level_to_find_end;
		double min_weld;
		double min_connector;
		int strategy;
		try {
			useLinear = (new ByteArray((byte[])criterias.get(CODENAME_TACTIC))).toInt();
			eventsize = (new ByteArray((byte[])criterias.get(CODENAME_EVENT_SIZE))).toInt();
			connFallParams = (new ByteArray((byte[])criterias.get(CODENAME_CONN_FALL_PARAMS))).toDouble();
			min_level = (new ByteArray((byte[])criterias.get(CODENAME_MIN_LEVEL))).toDouble();
			max_level_noise = (new ByteArray((byte[])criterias.get(CODENAME_MAX_LEVEL_NOISE))).toDouble();
			min_level_to_find_end = (new ByteArray((byte[])criterias.get(CODENAME_MIN_LEVEL_TO_FIND_END))).toDouble();
			min_weld = (new ByteArray((byte[])criterias.get(CODENAME_MIN_WELD))).toDouble();
			min_connector = (new ByteArray((byte[])criterias.get(CODENAME_MIN_CONNECTOR))).toDouble();
			strategy = (new ByteArray((byte[])criterias.get(CODENAME_STRATEGY))).toInt();
		}
		catch(IOException e) {
			Log.errorException(e);
			return null;
		}
//-------------------------------------------------------------
Log.debugMessage("$$$$$$$$$ DadaraAnalysisManager.analyse:", Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ Number of points: " + y.length, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ d_x == " + d_x, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ useLinear == " + useLinear, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ eventsize == " + eventsize, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ connFallParams == " + connFallParams, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ min_level == " + min_level, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ max_level_noise == " + max_level_noise, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ min_level_to_find_end == " + min_level_to_find_end, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ min_weld == " + min_weld, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ min_connector == " + min_connector, Log.DEBUGLEVEL05);
Log.debugMessage("$$$$$$$$$ strategy == " + strategy, Log.DEBUGLEVEL05);
//-------------------------------------------------------------

		double[] tmp = ana(useLinear,
											 y,
											 d_x,
											 connFallParams,
											 min_level,
											 max_level_noise,
											 min_level_to_find_end,
											 min_weld,
											 min_connector,
											 strategy);
		int n_events = tmp.length / ReflectogramEvent.NUMBER_OF_PARAMETERS;
		ReflectogramEvent[] revents = new ReflectogramEvent[n_events];
		for(int i = 0; i < n_events; i++) {
			revents[i] = new ReflectogramEvent();
			revents[i].setParams(tmp, i * ReflectogramEvent.NUMBER_OF_PARAMETERS);
			revents[i].setDeltaX(d_x);
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
						ReflectogramEvent[] new_revents = new ReflectogramEvent[i+1];
						for (int j = 0; j < i + 1; j++)
							new_revents[j] = revents[j];
						revents = new_revents;
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
		analysisresultparameters.put(CODENAME_DADARA_EVENT_ARRAY, ReflectogramEvent.toByteArray(revents));
		return analysisresultparameters;
  }
}