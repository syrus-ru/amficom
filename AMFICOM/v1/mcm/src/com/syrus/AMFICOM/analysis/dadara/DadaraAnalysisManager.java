package com.syrus.AMFICOM.analysis.dadara;

import java.util.Hashtable;
import java.io.IOException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.mcm.AnalysisManager;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreReader;
import com.syrus.util.ByteArray;
import com.syrus.util.Log;

public class DadaraAnalysisManager extends AnalysisManager {

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

  public native double[] ana(int useLinear,
                             double[] y,                  //the refl. itself
                             double d_x,                  //dx
                             int eventsize,              //char. event size
                             double connFallParams,       // ?
                             double min_level,            // ?
                             double max_level_noise,      // ?
                             double min_level_to_find_end,// ?
                             double min_weld,             // ?
                             double min_connector,        // ?
                             int strategy);               // the minuit strategy

  public Hashtable analyse(Hashtable criterias, Hashtable resultparameters) {
    BellcoreReader br = new BellcoreReader();    
    BellcoreStructure bs = br.getData((byte[])resultparameters.get("reflectogramma"));
    double[] y = new double[bs.dataPts.TNDP];
    for (int i = 0; i < bs.dataPts.TPS[0]; i++)
      y[i] = (double)(65535 - bs.dataPts.DSF[0][i])/1000d;

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
      useLinear = (new ByteArray((byte[])criterias.get("dadara_tactic"))).toInt();
      eventsize = (new ByteArray((byte[])criterias.get("dadara_eventsize"))).toInt();
      connFallParams = (new ByteArray((byte[])criterias.get("dadara_conn_fall_params"))).toDouble();
      min_level = (new ByteArray((byte[])criterias.get("dadara_min_level"))).toDouble();
      max_level_noise = (new ByteArray((byte[])criterias.get("dadara_max_level_noise"))).toDouble();
      min_level_to_find_end = (new ByteArray((byte[])criterias.get("dadara_min_level_to_find_end"))).toDouble();
      min_weld = (new ByteArray((byte[])criterias.get("dadara_min_weld"))).toDouble();
      min_connector = (new ByteArray((byte[])criterias.get("dadara_min_connector"))).toDouble();
      strategy = (new ByteArray((byte[])criterias.get("dadara_strategy"))).toInt();
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
    ReflectogramEvent[] revents = this.megaHalyava(useLinear,
																									 y,
																									 d_x,
																									 eventsize,
																									 connFallParams,
																									 min_level,
																									 max_level_noise,
																									 min_level_to_find_end,
																									 min_weld,
																									 min_connector,
																									 strategy);
    //ReflectogramEvent[] revents = this.megaHalyava(0, y, d_x, 20, 0.2, 0.04, 0.1, 3.0, 0.06, 0.2, 4);
//----------
Log.debugMessage("$$$$$$$$$ Number of events == " + revents.length, Log.DEBUGLEVEL05); 
//----------
    Hashtable analysisresultparameters = new Hashtable(1);
    analysisresultparameters.put(new Identifier("dadara_event_array"), ReflectogramEvent.toByteArray(revents));
    return analysisresultparameters;
  }

  private ReflectogramEvent[] megaHalyava(int useLinear, 
                                          double[] y, 
                                          double d_x,
                                          int eventsize,
                                          double connFallParams,
                                          double min_level,
                                          double max_level_noise,
                                          double min_level_to_find_end,
                                          double min_weld,
                                          double min_connector,
                                          int strategy) {
    double min_y = y[10];
    for(int i=10; i < y.length; i++)
      if(y[i] < min_y) 
        min_y = y[i];

    for(int i=0; i < y.length; i++)
      y[i]-=min_y;

    for(int i=0; i<=10; i++)
      if(y[i]<0.)
        y[i]=0.;

    double[] tmp = null;
    try {
      tmp = ana(useLinear,// index of the wavelets to be used
                y,        // array of the amplitudes
                d_x,      // dx
                eventsize,       // event Size
                connFallParams,      // conn fall
                min_level,     // min_level
                max_level_noise,      // max. level of noise
                min_level_to_find_end,      // min. level for the end
                min_weld,     // min. level for the weld
                min_connector,      // min. level for the connector
                strategy);       // STRATEGY
    }
    catch (Exception e) {
      tmp = ana(useLinear,// index of the wavelets to be used
                y,        // array of the amplitudes
                d_x,      // dx
                eventsize,       // event Size
                connFallParams,      // conn fall
                min_level,     // min_level
                max_level_noise,      // max. level of noise
                min_level_to_find_end,      // min. level for the end
                min_weld,     // min. level for the weld
                min_connector,      // min. level for the connector
                strategy);       // STRATEGY
    }

		ReflectogramEvent[] revents = null;
		if (tmp != null) {
			int n_events = tmp.length/ReflectogramEvent.NUMBER_OF_PARAMETERS;
			revents = new ReflectogramEvent[n_events];
			for(int i = 0; i < n_events; i++) {
				revents[i] = new ReflectogramEvent();
				revents[i].setParams(tmp, i * 44);
			}
		}
    return revents;
  }
}