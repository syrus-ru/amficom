package com.syrus.AMFICOM.mcm;

import java.util.Hashtable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.event.corba.AlarmLevel;
import com.syrus.util.Log;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;

public class DadaraEvaluationManager extends EvaluationManager {
	public static final String CODENAME_DADARA_THRESHOLDS = "dadara_thresholds";
	public static final String CODENAME_DADARA_ALARM_ARRAY = "dadara_alarm_array";

  public DadaraEvaluationManager() {
  }

  public void evaluate(Hashtable thresholds, Hashtable etalonparameters, Hashtable analysisresultparameters, Hashtable resultparameters) {
    ReflectogramEvent[] etalon = ReflectogramEvent.fromByteArray((byte[])etalonparameters.get("dadara_etalon_event_array"));
    Threshold[] ts = Threshold.fromByteArray((byte[])thresholds.get("dadara_thresholds"));

		ReflectogramComparer reflComparer;
		if (analysisresultparameters != null) {
			ReflectogramEvent[] revents = ReflectogramEvent.fromByteArray((byte[])analysisresultparameters.get("dadara_event_array"));
			reflComparer = new ReflectogramComparer(revents, etalon, ts);
		}
		else {
			Log.debugMessage("No analysis result, evaluating reflectogram itself", Log.DEBUGLEVEL05);
			BellcoreReader br = new BellcoreReader();
			BellcoreStructure bs =  br.getData((byte[])resultparameters.get("reflectogramma"));
			double[] y = new double[bs.dataPts.DSF[0].length];
			for (int i = 0; i < y.length; i++)
				y[i] = (double)(65535 - bs.dataPts.DSF[0][i])/1000d;
			reflComparer = new ReflectogramComparer(y, etalon, ts);
		}

		super.hevaluationResultParameters = new Hashtable(0);
		super.alarmLevel = AlarmLevel.ALARM_LEVEL_NONE;
		ReflectogramAlarm[] ralarms = reflComparer.getAlarms();
		if (ralarms.length > 0) {
			super.alarmLevel = AlarmLevel.ALARM_LEVEL_SOFT;
			for (int i = 0; i < ralarms.length; i++)
				if (ralarms[i].level == ReflectogramAlarm.LEVEL_HARD) {
					super.alarmLevel = AlarmLevel.ALARM_LEVEL_HARD;
					break;
				}
		  super.hevaluationResultParameters.put(new Identifier("dadara_alarm_array"), ReflectogramAlarm.toByteArray(ralarms));
		}
  }
}