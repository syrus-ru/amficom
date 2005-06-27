/*
 * $Id: ModelTraceComparer.java,v 1.19 2005/06/27 09:28:53 saa Exp $
 * 
 * Copyright � Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.analysis.Etalon;
import com.syrus.AMFICOM.analysis.EventAnchorer;
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
 * <p>
 * Performs EventAnchorer processing:
 * <ul>
 * <li> createEventAnchor
 * </ul>
 * @author $Author: saa $
 * @version $Revision: 1.19 $, $Date: 2005/06/27 09:28:53 $
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
     * ���������� ������� ReliabilitySimpleReflectogramEvent[] � ��������.
     * @param events ������������ ������ �������
     * @param mtm ������
     * @return soft type ReflectogramAlarm, ���� ������� ��������
     * ������� � ������ �������, ���� null, ���� �������� �������� �� �������.
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
     * ���������� ������ � �������� ��������� ������.
     * @param yProbe ������������ ������
     * @param mtm ��������� mtm, �� �������� ������������ ������
     * @param level ������� ��������� (0.0 - SOFT, 1.0 - HARD)
     * @param alarm ��������� �������� ���������� ������ � ����� ������.
     *   ��������������, ���� ���������� ���������� �������.
     * @return true, ���� ���������� ���������� �������,
     *   false, ���� ���������� ������� ��������� ������ ���.
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
     * ���������� ������� ���������� ������ �������
     * @param y ������
     * @param mtm ������
     * @param alarm �����, � ������� ����� ������� ������� ����������
     *   �������  
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
				if (level == alarm.level && alarmBegin < alarm.pointCoord
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
	 * ��������� ����� �� ���������� ������.
	 * <ul>
	 * <li>
	 * ���� ������ ���������� ����� ������ ���� - ����������
	 * ��������� ������ ����� ������� ������ �����������
	 * <li>
	 * ���� ������ ������������� ���� ������ - ���������� -1
	 * <li>
	 * ���� ������ ������� ���� ������ - ���������� 0
	 * </ul>
	 * 
	 * @param mt ��������� ������
	 * @param th ��������� �������� Y, ����������� �� ������ ���. ���������.
     *   �������� ������ ����.
     * @return ��������� ������ ���� -1
	 */
	public static int compareToMinLevel(ModelTrace mt, double th)
	{
		// XXX: �������� ������������, �������, ��������, ����� �� �����
		double[] y = mt.getYArray();
        double yMax = ReflectogramMath.getArrayMax(y);
        int i;
        // ���� ������ ����� ���� ������
		for (i = 0; i < y.length; i++) {
			if (y[i] >= yMax + th)
				break;
		}
		if (i == y.length) // �� ������� �� ����� ����� ���� ������
			return 0;
		// ���� ������� ������ ����
		for (; i < y.length; i++) {
			if (y[i] < yMax + th)
				return i; // �����
		}
		return -1; // �� �����
	}

	/**
	 * ������������� �������� EventAnchorer ����������� ������� �� ������
	 * ��������� �������� � ��������� ������� ����������� ������� � �������.
	 * @param ar ���������� ������� (modify)
	 * @param etalon ������ (read only)
	 */
	public static void createEventAnchor(AnalysisResult ar, Etalon etalon) {
		// ����� �������� �������
		EventAnchorer etAnc = etalon.getAnc();
		if (etAnc == null) { // ���� �� ��� - ���������� ar
			ar.setAnchorer(null);
			return;
		}

		// ����� ������� ar, �������� �������������
		SimpleReflectogramEvent[] events = ar.getMTAE().getSimpleEvents();
		SimpleReflectogramEvent[] etEv = etalon.getMTM().getMTAE().getSimpleEvents();
		SimpleReflectogramEventComparer rc =
				new SimpleReflectogramEventComparer(
						events,
						etEv);

		// ������������� "������" �������� ��� ar
		EventAnchorer anc = new EventAnchorer(events.length);
		ar.setAnchorer(anc);

		// ����������� ������ ������� ar, ������� ���� � �������
		// XXX: ���� �� ����� ������� ���?
		for (int i = 0; i < events.length; i++) {
			int etId = rc.getEtalonIdByProbeId(i);
			if (etId >= 0) {
				anc.setEventAnchor(i, etAnc.getEventAnchor(etId));
			}
		}
	}
}
