/*
 * $Id: DadaraEvaluationManager.java,v 1.4 2004/07/21 08:18:10 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.agent;

import java.text.SimpleDateFormat;
import java.util.Hashtable;
import com.syrus.AMFICOM.CORBA.General.AlarmLevel;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.Threshold;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramAlarm;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramComparer;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.BellcoreReader;
import com.syrus.util.Log;

import java.util.Date;
import java.io.FileOutputStream;

/**
 * @version $Revision: 1.4 $, $Date: 2004/07/21 08:18:10 $
 * @author $Author: arseniy $
 * @module agent_v1
 */

public class DadaraEvaluationManager extends EvaluationManager  {
	public void evaluate(Hashtable thresholds,
											 Hashtable etalonparameters,
											 Hashtable analysisresultparameters,
											 Hashtable resultparameters) throws Exception {
    ReflectogramEvent[] etalon = ReflectogramEvent.fromByteArray((byte[])etalonparameters.get("dadara_etalon_event_array"));
    Threshold[] ts = Threshold.fromByteArray((byte[])thresholds.get("dadara_thresholds"));
//--------
try {
	FileOutputStream fos;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
	String tstr = sdf.format(new Date(System.currentTimeMillis()));

	fos = new FileOutputStream("./logs/" + tstr + "-thresholds");
	fos.write((byte[])thresholds.get("dadara_thresholds"));
	fos.close();

	fos = new FileOutputStream("./logs/" + tstr + "-etalon");
	fos.write((byte[])etalonparameters.get("dadara_etalon_event_array"));
	fos.close();

	fos = new FileOutputStream("./logs/" + tstr + "-revents");
	fos.write((byte[])analysisresultparameters.get("dadara_event_array"));
	fos.close();

	fos = new FileOutputStream("./logs/" + tstr + "-reflectogramma.sor");
	Object refl = resultparameters.get("reflectogramm");
	fos.write((byte[])refl);
	fos.close();
}
catch (Exception e) {
	System.out.println(e.getMessage());
	e.printStackTrace();
}
//--------

		ReflectogramComparer reflComparer = null;

		if (analysisresultparameters != null) {
			ReflectogramEvent[] revents = ReflectogramEvent.fromByteArray((byte[])analysisresultparameters.get("dadara_event_array"));
			reflComparer = new ReflectogramComparer(revents, etalon, ts, false);
		}
		else {
			if (resultparameters != null) {
				Log.debugMessage("No analysis result, evaluating reflectogram itself", Log.DEBUGLEVEL05);
				Object refl = resultparameters.get("reflectogramm");
				BellcoreReader br = new BellcoreReader();
				BellcoreStructure bs =  br.getData((byte[])refl);
				double[] y = new double[bs.dataPts.DSF[0].length];
				for (int i = 0; i < y.length; i++)
					y[i] = (double)(65535 - bs.dataPts.DSF[0][i])/1000d;
				reflComparer = new ReflectogramComparer(y, etalon, ts, false);
			}
			else
				throw new Exception("ERROR: Neither analysis result nor test result is provided -- cannot evaluate!");
		}

		super.evaluationresultparameters = new Hashtable(0);
		super.alarmLevel = AlarmLevel.ALARM_LEVEL_NONE;
		ReflectogramAlarm[] ralarms = reflComparer.getAlarms();
		if (ralarms.length > 0) {
			super.alarmLevel = AlarmLevel.ALARM_LEVEL_SOFT;
			for (int i = 0; i < ralarms.length; i++)
				if (ralarms[i].level == ReflectogramAlarm.LEVEL_HARD) {
					super.alarmLevel = AlarmLevel.ALARM_LEVEL_HARD;
					break;
				}
		  super.evaluationresultparameters.put("dadara_alarm_array", ReflectogramAlarm.toByteArray(ralarms));
		}
  }
}
