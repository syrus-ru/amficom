/*
 * $Id: ModelTraceComparer.java,v 1.11 2005/04/30 11:54:28 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * Performs comparison:
 * <ul>
 * <li> ModelTrace to MTM
 * <li> ModelTrace to MinTraceLevel
 * <li> ReliabilitySimpleReflectogramEvent[] to MTM 
 * </ul>
 * and, based on all of the above,
 * <ul>
 * <li> ReliabilityModelTraceAndEvents to MTM
 * </ul>
 * @author $Author: saa $
 * @version $Revision: 1.11 $, $Date: 2005/04/30 11:54:28 $
 * @module
 */
public class ModelTraceComparer
{
    private static final int ALARM_LEVEL_FOR_EVENT_CHANGE =
        ReflectogramAlarm.LEVEL_SOFT;

    public static ReflectogramAlarm compareMTAEToMTM(
            ReliabilityModelTraceAndEvents mtae,
            ModelTraceManager mtm)
    {
        System.out.println(
                "ModelTraceComparer.compareToMTM: comparing mtae to mtm:");
        ReflectogramAlarm alarmTrace =
            compareTraceToMTM(mtae.getModelTrace(), mtm);
        ReflectogramAlarm alarmEvents =
            compareEventsToMTM(
                (ReliabilitySimpleReflectogramEvent[])mtae.getSimpleEvents(),
                mtm);
        System.out.println(
                "ModelTraceComparer.compareToMTM: trace alarm: " + alarmTrace);
        System.out.println(
                "ModelTraceComparer.compareToMTM: event alarm: " + alarmEvents);
        if (alarmTrace != null)
            return alarmTrace;
        else
            return alarmEvents;
    }

    /**
     * Сравнивает события ReliabilitySimpleReflectogramEvent[] с эталоном.
     * @param events сравниваемый список событий
     * @param mtm эталон
     * @return soft type ReflectogramAlarm, если найдены значимые
     * отличия в списке событий, либо null, если значимых различий не найдено.
     */
    public static ReflectogramAlarm compareEventsToMTM(
            ReliabilitySimpleReflectogramEvent[] events,
            ModelTraceManager mtm)
    {
        ReliabilitySimpleReflectogramEvent[] etEvents =
            (ReliabilitySimpleReflectogramEvent[])mtm.getMTAE().getSimpleEvents();
        SimpleReflectogramEventComparer rc = new SimpleReflectogramEventComparer(events, etEvents);
        ReflectogramAlarm out = new ReflectogramAlarm();
        ReflectogramAlarm cur = new ReflectogramAlarm();
        cur.alarmType = ReflectogramAlarm.TYPE_EVENTLISTCHANGED;
        cur.level = ALARM_LEVEL_FOR_EVENT_CHANGE;
        int i;
        for (i = 0; i < etEvents.length; i++)
        {
            if (rc.isEtalonEventReliablyLost(i))
            {
                cur.pointCoord = etEvents[i].getBegin();
                cur.endPointCoord = etEvents[i].getEnd();
                System.out.println("MTC: compareEventsToMTM: etalon event #"
                        + i + " ( " + cur.pointCoord + " .. "
                        + cur.endPointCoord + ") is reliably lost");
                out.toHardest(cur);
            }
        }
        for (i = 0; i < events.length; i++)
        {
            if (rc.isProbeEventReliablyNew(i))
            {
                cur.pointCoord = events[i].getBegin();
                cur.endPointCoord = events[i].getEnd();
                System.out.println("MTC: compareEventsToMTM: probe event #"
                        + i + " ( " + cur.pointCoord + " .. "
                        + cur.endPointCoord + ") is reliably new");
                out.toHardest(cur);
            }
        }
        return out.alarmType > ReflectogramAlarm.LEVEL_NONE
            ? out
            : null;
    }

    public static ReflectogramAlarm compareTraceToMTM(ModelTrace mt,
            ModelTraceManager mtm)
	{
        // create initial 'no alarm' alarm
		ReflectogramAlarm alarm = new ReflectogramAlarm();
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

	/**
	 * Проверяет, не опускается ли модельная кривая ниже указанного уровня.
	 * Если опускается, возвращает соотв. координату.
	 * Если не опускается
	 * @param mt модельная кривая
	 * @param th пороговое значение Y
	 * @return самая левая точка, в которой кривая ниже порога,
	 * либо -1, если кривая везде выше порога
	 */
	public static int compareToMinLevel(ModelTrace mt, double th)
	{
		double[] y = mt.getYArray();
		for (int i = 0; i < y.length; i++)
		{
			if (y[i] < th)
				return i;
		}
		return -1;
	}
}
