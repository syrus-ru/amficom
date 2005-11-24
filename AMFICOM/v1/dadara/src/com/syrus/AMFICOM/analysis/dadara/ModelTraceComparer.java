/*
 * $Id: ModelTraceComparer.java,v 1.44 2005/11/24 08:35:51 saa Exp $
 * 
 * Copyright � Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

import static com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.AlarmType.TYPE_EVENTLISTCHANGED;
import static com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.AlarmType.TYPE_OUTOFMASK;
import static com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity.SEVERITY_NONE;
import static com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity.SEVERITY_SOFT;
import static java.util.logging.Level.INFO;

import java.util.logging.Level;

import com.syrus.AMFICOM.analysis.Etalon;
import com.syrus.AMFICOM.analysis.EventAnchorer;
import com.syrus.AMFICOM.analysis.SOAnchorImpl;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;
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
 * @version $Revision: 1.44 $, $Date: 2005/11/24 08:35:51 $
 * @module
 */
public class ModelTraceComparer
{
	private static final Severity ALARM_LEVEL_FOR_EVENT_CHANGE =
		SEVERITY_SOFT;
	private static final Severity ALARM_LEVEL_FOR_SOFT_MASKS =
		SEVERITY_SOFT;
	private static final Severity ALARM_LEVEL_FOR_HARD_MASKS =
		SEVERITY_SOFT;

	private ModelTraceComparer() {
		// non-instantiable
	}

	/**
	 * ���������� �� ������ � �� ��������, ��������
	 * ReflectogramComparer ��������������.
	 * @see #compareMTAEToMTM(ReliabilityModelTraceAndEvents, ModelTraceManager, SimpleReflectogramEventComparer)
	 * @param mtae ������������ ������� "�/� � ���������"
	 * @param mtm ��������� "�/� � ���������" � ��������
	 * @return ����� ���� null
	 */
	public static ReflectogramMismatchImpl compareMTAEToMTM(
			ReliabilityModelTraceAndEvents mtae,
			ModelTraceManager mtm) {
		return compareMTAEToMTM(mtae, mtm, null);
	}

	/**
	 * ���������� �� ������ � �� ��������; ����� ������������
	 * �������� ����� ReflectogramComparer'��.
	 * �������� ������������ �����.
	 * ������������ ���������� ��������� ������ (����������������
	 * ������ ������� � �������). �� �������� SOAnchor-��������.
	 * @param mtae ������������ ������� "�/� � ���������"
	 * @param mtm ��������� "�/� � ���������" � ��������
	 * @param rcomp ������� ����������� ��������� ������������� �������
	 *   mtae � mtm ���� null, ���� ���� ����������� ��������������
	 *   XXX: ������������ �������� rcomp ����� �������� � ����
	 * @return ����� ���� null
	 */
	public static ReflectogramMismatchImpl compareMTAEToMTM(
			ReliabilityModelTraceAndEvents mtae,
			ModelTraceManager mtm,
			SimpleReflectogramEventComparer rcomp) {
		ReflectogramMismatchImpl alarmTrace =
			compareTraceToMTM(mtae.getModelTrace(), mtm);
		ReflectogramMismatchImpl alarmEvents =
			compareEventsToMTM(
				(ReliabilitySimpleReflectogramEvent[])mtae.getSimpleEvents(),
				mtm,
				rcomp);
		Log.debugMessage(
				"ModelTraceComparer.compareToMTM: comparing mtae to mtm:",
				INFO);
		Log.debugMessage(
				"ModelTraceComparer.compareToMTM: trace alarm: " + alarmTrace,
				INFO);
		Log.debugMessage(
				"ModelTraceComparer.compareToMTM: event alarm: " + alarmEvents,
				INFO);
		if (alarmTrace != null)
			return alarmTrace;
		return alarmEvents;
	}

	/**
	 * ���������� ������� ReliabilitySimpleReflectogramEvent[] � ��������.
	 * �������� ����� ����� ��������� ������ ������������� �������.
	 * @param events ������������ ������ �������
	 * @param mtm ������
	 * @param rcomp ������� ����������� ��������� ������������� �������
	 *   mtae � mtm ���� null, ���� ���� ����������� ��������������
	 * @return soft type ReflectogramMismatch, ���� ������� ��������
	 * ������� � ������ �������, ���� null, ���� �������� �������� �� �������.
	 */
	public static ReflectogramMismatchImpl compareEventsToMTM(
			ReliabilitySimpleReflectogramEvent[] events,
			ModelTraceManager mtm,
			SimpleReflectogramEventComparer rcomp)
	{
		ReliabilitySimpleReflectogramEvent[] etEvents =
			mtm.getMTAE().getSimpleEvents();
		if (rcomp == null) {
			rcomp = new SimpleReflectogramEventComparer(
					events,
					etEvents);
		}
		ReflectogramMismatchImpl out = new ReflectogramMismatchImpl();
		ReflectogramMismatchImpl cur = new ReflectogramMismatchImpl();
		cur.setAlarmType(TYPE_EVENTLISTCHANGED);
		cur.setSeverity(ALARM_LEVEL_FOR_EVENT_CHANGE);
		cur.setDeltaX(mtm.getMTAE().getDeltaX());
		int i;
		for (i = 0; i < etEvents.length; i++) {
			// � ��������, �������� "������� �� ���." �� �����, �.�. ������ ���. ������� ��� ����� ����������� �� ���������
			if (rcomp.isEtalonEventReliablyLost(i)
				&& etEvents[i].getEventType() != SimpleReflectogramEvent.LINEAR) {
				cur.setCoord(etEvents[i].getBegin());
				cur.setEndCoord(etEvents[i].getEnd());
				Log.debugMessage("MTC: compareEventsToMTM: etalon event #"
						+ i + " ( " + cur.getCoord() + " .. "
						+ cur.getEndCoord() + ") is reliably lost",
						INFO);
				out.toHardest(cur);
			}
		}
		for (i = 0; i < events.length; i++) {
			// � ��������, �������� "������� �� ���." �� �����, �.�. ��������� ���. ������� ��� ����� ����������� �� ���������
			if (rcomp.isProbeEventReliablyNew(i)
				&& events[i].getEventType() != SimpleReflectogramEvent.LINEAR) {
				cur.setCoord(events[i].getBegin());
				cur.setEndCoord(events[i].getEnd());
				Log.debugMessage("MTC: compareEventsToMTM: probe event #"
						+ i + " ( " + cur.getCoord() + " .. "
						+ cur.getEndCoord() + ") is reliably new",
						INFO);
				out.toHardest(cur);
			}
		}
		/**
		 * @bug Is it ok to compare AlarmType and Severity?
		 */
		return out.getAlarmType().ordinal() > SEVERITY_NONE.ordinal()
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
			ModelTraceManager mtm, double level, ReflectogramMismatchImpl alarm)
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
		alarm.setCoord(mtm.fixAlarmPos(alarmBegin, true)); // XXX: �������, ��� ��������� �� ������������
		if (alarmEnd < alarm.getCoord())
			alarmEnd = alarm.getCoord();
		alarm.setEndCoord(alarmEnd);
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
			ModelTraceManager mtm, ReflectogramMismatchImpl alarm)
	{
		ReflectogramMismatchImpl tmpAlarm = new ReflectogramMismatchImpl();
		final int N = 10;
		for (int i = 0; i <= N; i++) {
			double level = i * 1.0 / N;
			double levelNext = i == N ? 1.0 : (i + 1) * 1.0 / N;
			if (compareTraceToMTMAtLevel(y, mtm, level, tmpAlarm)) {
				alarm.setMismatch(level, levelNext);
			}
		}
	}

	/**
	 * @return ReflectogramMismatchImpl, may be null
	 */
	public static ReflectogramMismatchImpl compareTraceToMTM(ModelTrace mt,
			ModelTraceManager mtm) {
		// create initial 'no alarm' alarm 
		ReflectogramMismatchImpl alarm = new ReflectogramMismatchImpl();
		// ���������� ����� ������ �� ������ (��� ��� ��������)
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
			int alarmStart = i;
			for (; i < len; i++)
			{
				if (y[i] * sign <= thY[i] * sign)
					break;
			}
			int alarmEnd = i;
			if (alarmStart < len)
			{
				Severity level = Thresh.IS_KEY_HARD[key]
					? ALARM_LEVEL_FOR_HARD_MASKS
					: ALARM_LEVEL_FOR_SOFT_MASKS;
				if (level == alarm.getSeverity() && alarmStart < alarm.getCoord()
						|| level.compareTo(alarm.getSeverity()) > 0) {
					alarm.setSeverity(level);
					alarm.setCoord(alarmStart);
					alarm.setEndCoord(alarmEnd);
					alarm.setAlarmType(TYPE_OUTOFMASK);
					alarm.setDeltaX(mtm.getMTAE().getDeltaX());
				}
			}
		}
		if (alarm.getSeverity().compareTo(SEVERITY_NONE) > 0) {
			// ����������� � ������ �������
			alarm.setCoord(mtm.fixAlarmPos(alarm.getCoord(), true));
			if (alarm.getEndCoord() < alarm.getCoord())
				alarm.setEndCoord(alarm.getCoord());

			fillAlarmMismatch(y, mtm, alarm);
			Log.debugMessage("level " + alarm.getSeverity()
					+ " mismatch "
					+ (alarm.hasMismatch()
						? "" + alarm.getMinMismatch() + ".." + alarm.getMaxMismatch()
						: "<absent>"),
					Level.FINEST);
			return alarm;
		}
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
		double[] y = mt.getYArray();
		int i;
		// XXX: �������� ������������, �������, ��������, ����� �� �����
		double yMax = y.length > 0 ? ReflectogramMath.getArrayMax(y) : 0;
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
	 * @param rcomp ������� ����������� ��������� ������������� �������
	 *   mtae � mtm ���� null, ���� ���� ����������� ��������������
	 */
	public static void createEventAnchor(AnalysisResult ar,
			Etalon etalon, SimpleReflectogramEventComparer rcomp) {
		// ����� �������� �������
		EventAnchorer etAnc = etalon.getAnc();
		if (etAnc == null) { // ���� �� ��� - ���������� ar
			ar.setAnchorer(null);
			return;
		}

		// ����� ������� ar, �������� �������������
		SimpleReflectogramEvent[] events = ar.getMTAE().getSimpleEvents();
		SimpleReflectogramEvent[] etEv = etalon.getMTM().getMTAE().getSimpleEvents();
		if (rcomp == null) {
			rcomp = new SimpleReflectogramEventComparer(
						events,
						etEv);
		}

		// ������������� "������" �������� ��� ar
		EventAnchorer anc = new EventAnchorer(events.length);
		ar.setAnchorer(anc);

		// ����������� ������ ������� ar, ������� ���� � �������
		// XXX: ���� �� ����� ������� ���?
		for (int i = 0; i < events.length; i++) {
			int etId = rcomp.getEtalonIdByProbeId(i);
			if (etId >= 0) {
				anc.setEventAnchor(i, etAnc.getEventAnchor(etId));
			}
		}
	}

	/**
	 * ������������� ref1/ref2 ��������� �������� ��� ������
	 * �� ��������� ��������� "��������"
	 * (������, ���������) �������� �������, ������� ������ � ��������.
	 * ���� ����������� "��������" �������� ���, ���� � ������� ��� �������
	 * EventAnchorer, ������������� ��������� � ��������� "�� ����������".
	 * ���� 
	 * @param ra �����
	 * @param et ������
	 */
	public static void setAlarmAnchors(ReflectogramMismatchImpl ra, Etalon et) {
		int distance = ra.getCoord();
		SOAnchorImpl ref1Id = null; // ������������� � "���� �� �������"
		SOAnchorImpl ref2Id = null;
		int ref1Coord = 0; // ��� ������ �� �����������
		int ref2Coord = 0;
		ModelTraceAndEvents mtae = et.getMTM().getMTAE();
		EventAnchorer anc = et.getAnc();
		int len = mtae.getNEvents();
		if (anc != null) {
			for (int i = 0; i < len; i++) {
				SOAnchorImpl ea = anc.getEventAnchor(i);
				SimpleReflectogramEvent se = mtae.getSimpleEvent(i);

				// ���������� �������, �� ������� �� ����� ���� ��������

				if (!ReflectogramMath.isEventAnchorable(se)) {
					continue;
				}

				// ���������� �������, ��� ������� �������� �� ����������
	
				if (ea.getValue() == SOAnchorImpl.VOID_ANCHOR.getValue())
					continue;
	
				// ����, ��� ������� ����� ���� ������������ ��� ��������
	
				int pos = se.getBegin();
	
				// �������� ����� ������� pos �����
				if (pos <= distance) {
					if (ref1Id == null || pos > ref1Coord) {
						ref1Id = ea;
						ref1Coord = pos;
					}
				}
				// �������� ����� ������� pos ������
				if (pos >= distance) {
					if (ref2Id == null || pos < ref2Coord) {
						ref2Id = ea;
						ref2Coord = pos;
					}
				}
			}
			if (ref1Id != null && ref2Id != null) {
				ra.setAnchors(ref1Id, ref1Coord, ref2Id, ref2Coord);
			} else {
				ra.unSetAnchors();
			}
		}
	}
}
