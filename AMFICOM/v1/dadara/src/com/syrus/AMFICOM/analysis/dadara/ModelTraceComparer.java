/*
 * $Id: ModelTraceComparer.java,v 1.1 2005/03/15 13:43:29 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/03/15 13:43:29 $
 * @module
 */
public class ModelTraceComparer
{
	public static ReflectogramAlarm compare(ModelTrace mt, ModelTraceManager mtm)
	{
		ReflectogramAlarm alarm = new ReflectogramAlarm(); // create 'no alarm' alarm
		double[] y = mt.getYArray();
		for (int key = 0; key < 4; key++)
		{
			ModelTrace thMT = mtm.getThresholdMT(key);
			double[] thY = thMT.getYArray();
			int len = Math.min(y.length, thY.length);
			int sign = Thresh.IS_KEY_UPPER[key] ? 1 : -1;
			int i;
			for (i = 0; i < len; i++)
			{
				if (y[i] * sign > thY[i] * sign)
					break;
			}
			int alarmBegin = i;
			for (; i < len; i++)
			{
				if (y[i] * sign <= thY[i] * sign)
					break;
			}
			int alarmEnd = i;
			if (alarmBegin < len)
			{
				int level = Thresh.IS_KEY_HARD[key]
					? ReflectogramAlarm.LEVEL_HARD
					: ReflectogramAlarm.LEVEL_SOFT;
				if (level == alarm.level && alarmBegin > alarm.pointCoord
						|| level > alarm.level)
				{
					alarm.level = level;
					alarm.pointCoord = alarmBegin;
					alarm.endPointCoord = alarmEnd;
					alarm.alarmType = ReflectogramAlarm.TYPE_OUTOFMASK;
				}
			}
		}
		if (alarm.level > ReflectogramAlarm.LEVEL_NONE)
			return alarm;
		else
			return null;
	}
}
