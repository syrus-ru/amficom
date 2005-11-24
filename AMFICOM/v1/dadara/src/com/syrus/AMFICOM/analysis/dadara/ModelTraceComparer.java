/*
 * $Id: ModelTraceComparer.java,v 1.44 2005/11/24 08:35:51 saa Exp $
 * 
 * Copyright © Syrus Systems.
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
	 * Сравнивает по маскам и по событиям, создавая
	 * ReflectogramComparer самостоятельно.
	 * @see #compareMTAEToMTM(ReliabilityModelTraceAndEvents, ModelTraceManager, SimpleReflectogramEventComparer)
	 * @param mtae сравниваемая текущая "а/к с событиями"
	 * @param mtm эталонная "а/к с событиями" с порогами
	 * @return аларм либо null
	 */
	public static ReflectogramMismatchImpl compareMTAEToMTM(
			ReliabilityModelTraceAndEvents mtae,
			ModelTraceManager mtm) {
		return compareMTAEToMTM(mtae, mtm, null);
	}

	/**
	 * Сравнивает по маскам и по событиям; может пользоваться
	 * заданным извне ReflectogramComparer'ом.
	 * Выбирает доминирующий аларм.
	 * Обеспечивает корректную дистанцию аларма (соответствующему
	 * началу события в эталоне). Не проводит SOAnchor-привязку.
	 * @param mtae сравниваемая текущая "а/к с событиями"
	 * @param mtm эталонная "а/к с событиями" с порогами
	 * @param rcomp заранее вычисленный результат сопоставления событий
	 *   mtae с mtm либо null, если надо сопоставить самостоятельно
	 *   XXX: неправильное значение rcomp может привести к сбою
	 * @return аларм либо null
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
	 * Сравнивает события ReliabilitySimpleReflectogramEvent[] с эталоном.
	 * Выходной аларм имеет дистанцию начала отличающегося события.
	 * @param events сравниваемый список событий
	 * @param mtm эталон
	 * @param rcomp заранее вычисленный результат сопоставления событий
	 *   mtae с mtm либо null, если надо сопоставить самостоятельно
	 * @return soft type ReflectogramMismatch, если найдены значимые
	 * отличия в списке событий, либо null, если значимых различий не найдено.
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
			// в принципе, проверка "событие не лин." не нужна, т.к. потеря лин. события все равно достоверным не считается
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
			// в принципе, проверка "событие не лин." не нужна, т.к. появление лин. события все равно достоверным не считается
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
		alarm.setCoord(mtm.fixAlarmPos(alarmBegin, true)); // XXX: кажется, эта дистанция не используется
		if (alarmEnd < alarm.getCoord())
			alarmEnd = alarm.getCoord();
		alarm.setEndCoord(alarmEnd);
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
		// определяем точку выхода за пороги (еще без привязки)
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
			// привязываем к началу события
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
		double[] y = mt.getYArray();
		int i;
		// XXX: проводим выравнивание, которое, вероятно, более не нужно
		double yMax = y.length > 0 ? ReflectogramMath.getArrayMax(y) : 0;
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

	/**
	 * Устанавливает привязку EventAnchorer результатов анализа на основе
	 * эталонной привязки и сравнения событий результатов анализа и эталона.
	 * @param ar результаты анализа (modify)
	 * @param etalon эталон (read only)
	 * @param rcomp заранее вычисленный результат сопоставления событий
	 *   mtae с mtm либо null, если надо сопоставить самостоятельно
	 */
	public static void createEventAnchor(AnalysisResult ar,
			Etalon etalon, SimpleReflectogramEventComparer rcomp) {
		// берем привязку эталона
		EventAnchorer etAnc = etalon.getAnc();
		if (etAnc == null) { // если ее нет - отвязываем ar
			ar.setAnchorer(null);
			return;
		}

		// берем события ar, проводим сопоставление
		SimpleReflectogramEvent[] events = ar.getMTAE().getSimpleEvents();
		SimpleReflectogramEvent[] etEv = etalon.getMTM().getMTAE().getSimpleEvents();
		if (rcomp == null) {
			rcomp = new SimpleReflectogramEventComparer(
						events,
						etEv);
		}

		// устанавливаем "пустую" привязку для ar
		EventAnchorer anc = new EventAnchorer(events.length);
		ar.setAnchorer(anc);

		// привязываем каждое событие ar, имеющее пару в эталоне
		// XXX: надо ли здесь сверять тип?
		for (int i = 0; i < events.length; i++) {
			int etId = rcomp.getEtalonIdByProbeId(i);
			if (etId >= 0) {
				anc.setEventAnchor(i, etAnc.getEventAnchor(etId));
			}
		}
	}

	/**
	 * Устанавливает ref1/ref2 параметры привязки для аларма
	 * на основании ближайших "точечных"
	 * (сварки, отражения) объектов эталона, имеющих данные о привязке.
	 * Если привязанных "точечных" объектов нет, либо в эталоне нет объекта
	 * EventAnchorer, устанавливает параметры в состояние "не определено".
	 * Если 
	 * @param ra Аларм
	 * @param et Эталон
	 */
	public static void setAlarmAnchors(ReflectogramMismatchImpl ra, Etalon et) {
		int distance = ra.getCoord();
		SOAnchorImpl ref1Id = null; // устанавливаем в "пока не найдено"
		SOAnchorImpl ref2Id = null;
		int ref1Coord = 0; // это делать не обязательно
		int ref2Coord = 0;
		ModelTraceAndEvents mtae = et.getMTM().getMTAE();
		EventAnchorer anc = et.getAnc();
		int len = mtae.getNEvents();
		if (anc != null) {
			for (int i = 0; i < len; i++) {
				SOAnchorImpl ea = anc.getEventAnchor(i);
				SimpleReflectogramEvent se = mtae.getSimpleEvent(i);

				// пропускаем события, по которым не может быть привязки

				if (!ReflectogramMath.isEventAnchorable(se)) {
					continue;
				}

				// пропускаем события, для которых привязка не определена
	
				if (ea.getValue() == SOAnchorImpl.VOID_ANCHOR.getValue())
					continue;
	
				// итак, это событие может быть использовано для привязки
	
				int pos = se.getBegin();
	
				// выбираем самое близкое pos слева
				if (pos <= distance) {
					if (ref1Id == null || pos > ref1Coord) {
						ref1Id = ea;
						ref1Coord = pos;
					}
				}
				// выбираем самое близкое pos справа
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
