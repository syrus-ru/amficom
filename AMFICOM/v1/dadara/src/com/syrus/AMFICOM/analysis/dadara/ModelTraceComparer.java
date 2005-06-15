/*
 * $Id: ModelTraceComparer.java,v 1.16 2005/06/15 15:08:40 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.util.Log;

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
 * @version $Revision: 1.16 $, $Date: 2005/06/15 15:08:40 $
 * @module
 */
public class ModelTraceComparer
{
    private static final int ALARM_LEVEL_FOR_EVENT_CHANGE =
        ReflectogramAlarm.LEVEL_SOFT;
    
    private ModelTraceComparer() {
        // non-instantiable
    }

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
        cur.deltaX = mtm.getMTAE().getDeltaX();
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

    /**
     * Сравнивает кривую с порогами заданного уровня.
     * @param yProbe сравниваемая кривая
     * @param mtm эталонный mtm, по которому определяются пороги
     * @param level уровень сравнения (0.0 - SOFT, 1.0 - HARD)
     * @param alarm хранилище выходных параметров начала и конца аларма.
     *   Модифицируется, если обнаружено превышение порогов.
     * @return true, если обнаружено превышение порогов,
     *   false, если превышения порогов заданного уровня нет.
     */
    private static boolean compareTraceToMTMAtLevel(double[] yProbe,
    		ModelTraceManager mtm, double level, ReflectogramAlarm alarm)
    {
    	ModelTrace thMTU = mtm.getThresholdMTUpperByLevel(level);
    	ModelTrace thMTL = mtm.getThresholdMTLowerByLevel(level);
    	int alarmBegin = -1;
    	int alarmEnd = -1;
    	// compare to upper MT
    	{
			double[] yTh = thMTU.getYArray();
			final int sign = 1;
			int len = Math.min(yProbe.length, yTh.length);
			int i;
			for (i = 0; i < len; i++) {
				if (yProbe[i] * sign > yTh[i] * sign)
					break;
			}
			int beg2 = i;
			for (; i < len; i++) {
				if (yProbe[i] * sign <= yTh[i] * sign)
					break;
			}
			int end2 = i;
			if (beg2 < len && (alarmBegin < 0 || beg2 < alarmBegin)) {
				alarmBegin = beg2;
				alarmEnd = end2;
			}
    	}
    	// compare to lower MT
    	{
			double[] yTh = thMTL.getYArray();
			final int sign = -1;
			int len = Math.min(yProbe.length, yTh.length);
			int i;
			for (i = 0; i < len; i++) {
				if (yProbe[i] * sign > yTh[i] * sign)
					break;
			}
			int beg2 = i;
			for (; i < len; i++) {
				if (yProbe[i] * sign <= yTh[i] * sign)
					break;
			}
			int end2 = i;
			if (beg2 < len && (alarmBegin < 0 || beg2 < alarmBegin)) {
				alarmBegin = beg2;
				alarmEnd = end2;
			}
    	}
    	// apply results
    	if (alarmBegin < 0)
    		return false;
    	alarm.pointCoord = alarmBegin;
    	alarm.endPointCoord = alarmEnd;
    	return true;
    }

    /**
     * Определяет степень превышения кривой порогов
     * @param y кривая
     * @param mtm пороги
     * @param alarm аларм, в котором будет вписана степень превышения
     *   порогов  
     */
    private static void fillAlarmMismatch(double[] y,
    		ModelTraceManager mtm, ReflectogramAlarm alarm)
    {
		ReflectogramAlarm tmpAlarm = new ReflectogramAlarm();
		final int N = 10;
		for (int i = 0; i <= N; i++) {
			double level = i * 1.0 / N;
			double levelNext = i == N ? 1.0 : (i + 1) * 1.0 / N;
			if (compareTraceToMTMAtLevel(y, mtm, level, tmpAlarm)) {
				alarm.setMismatch(level, levelNext);
			}
		}
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
                    alarm.deltaX = mtm.getMTAE().getDeltaX();
				}
			}
		}
		if (alarm.level > ReflectogramAlarm.LEVEL_NONE) {
			fillAlarmMismatch(y, mtm, alarm);
			Log.debugMessage("ModelTraceComparer.compareTraceToMTM | level " + alarm.level
					+ " mismatch "
					+ (alarm.hasMismatch()
						? "" + alarm.getMinMismatch() + ".." + alarm.getMaxMismatch()
						: "<absent>"),
					Log.FINEST);
			return alarm;
		}
		else
			return null;
	}

	/**
	 * Проверяет обрыв по указанному уровню.
	 * <ul>
	 * <li>
	 * если кривая пересекает порог сверху вниз - возвращаем
	 * ближайшую справа точку первого такого пересечения
	 * <li>
	 * если кривая заканчивается выше порога - возвращаем -1
	 * <li>
	 * если кривая целиком ниже порога - возвращаем 0
	 * </ul>
	 * 
	 * @param mt модельная кривая
	 * @param th пороговое значение Y, отсчитанное от уровня абс. максимума.
     *   Значение меньше нуля.
     * @return дистанция обрыва либо -1
	 */
	public static int compareToMinLevel(ModelTrace mt, double th)
	{
		// XXX: проводим выравнивание, которое, вероятно, более не нужно
		double[] y = mt.getYArray();
        double yMax = ReflectogramMath.getArrayMax(y);
        int i;
        // ищем первую точку выше порога
		for (i = 0; i < y.length; i++) {
			if (y[i] >= yMax + th)
				break;
		}
		if (i == y.length) // не найдено ни одной точки выше порога
			return 0;
		// ищем переход сверху вниз
		for (; i < y.length; i++) {
			if (y[i] < yMax + th)
				return i; // нашли
		}
		return -1; // не нашли
	}
}
