/*
 * $Id: ModelTraceComparer.java,v 1.29 2005/07/11 08:24:11 bass Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

import java.util.logging.Level;

import com.syrus.AMFICOM.analysis.Etalon;
import com.syrus.AMFICOM.analysis.EventAnchorer;
import com.syrus.AMFICOM.analysis.SOAnchor;
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
 * @author $Author: bass $
 * @version $Revision: 1.29 $, $Date: 2005/07/11 08:24:11 $
 * @module
 */
public class ModelTraceComparer
{
    private static final int ALARM_LEVEL_FOR_EVENT_CHANGE =
        ReflectogramMismatch.SEVERITY_SOFT;
    
    private ModelTraceComparer() {
        // non-instantiable
    }

    /**
     * Сравнивает по маскам и по событиям. Выбирает доминирующий аларм.
     * Обеспечивает корректную дистанцию аларма (соответствующему
     * началу события в эталоне). Не проводит SOAnchor-привязку.
     * @param mtae сравниваемая текущая "а/к с событиями"
     * @param mtm эталонная "а/к с событиями" с порогами
     * @return аларм либо null
     */
    public static ReflectogramMismatch compareMTAEToMTM(
            ReliabilityModelTraceAndEvents mtae,
            ModelTraceManager mtm)
    {
        ReflectogramMismatch alarmTrace =
            compareTraceToMTM(mtae.getModelTrace(), mtm);
        ReflectogramMismatch alarmEvents =
            compareEventsToMTM(
                (ReliabilitySimpleReflectogramEvent[])mtae.getSimpleEvents(),
                mtm);
        // FIXME: debug sysout
        System.out.println(
        		"ModelTraceComparer.compareToMTM: comparing mtae to mtm:");
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
     * Выходной аларм имеет дистанцию начала отличающегося события.
     * @param events сравниваемый список событий
     * @param mtm эталон
     * @return soft type ReflectogramMismatch, если найдены значимые
     * отличия в списке событий, либо null, если значимых различий не найдено.
     */
    public static ReflectogramMismatch compareEventsToMTM(
            ReliabilitySimpleReflectogramEvent[] events,
            ModelTraceManager mtm)
    {
        ReliabilitySimpleReflectogramEvent[] etEvents =
            (ReliabilitySimpleReflectogramEvent[])mtm.getMTAE().getSimpleEvents();
        SimpleReflectogramEventComparer rc = new SimpleReflectogramEventComparer(events, etEvents);
        ReflectogramMismatch out = new ReflectogramMismatch();
        ReflectogramMismatch cur = new ReflectogramMismatch();
        cur.setAlarmType(ReflectogramMismatch.TYPE_EVENTLISTCHANGED);
        cur.setSeverity(ALARM_LEVEL_FOR_EVENT_CHANGE);
        cur.setDeltaX(mtm.getMTAE().getDeltaX());
        int i;
        for (i = 0; i < etEvents.length; i++)
        {
        	// в принципе, проверка "событие не лин." не нужна, т.к. потеря лин. события все равно достоверным не считается
            if (rc.isEtalonEventReliablyLost(i)
            	&& etEvents[i].getEventType() != SimpleReflectogramEvent.LINEAR)
            {
                cur.setCoord(etEvents[i].getBegin());
                cur.setEndCoord(etEvents[i].getEnd());
                System.out.println("MTC: compareEventsToMTM: etalon event #"
                        + i + " ( " + cur.getCoord() + " .. "
                        + cur.getEndCoord() + ") is reliably lost");
                out.toHardest(cur);
            }
        }
        for (i = 0; i < events.length; i++)
        {
        	// в принципе, проверка "событие не лин." не нужна, т.к. появление лин. события все равно достоверным не считается
            if (rc.isProbeEventReliablyNew(i)
            	&& events[i].getEventType() != SimpleReflectogramEvent.LINEAR)
            {
                cur.setCoord(events[i].getBegin());
                cur.setEndCoord(events[i].getEnd());
                System.out.println("MTC: compareEventsToMTM: probe event #"
                        + i + " ( " + cur.getCoord() + " .. "
                        + cur.getEndCoord() + ") is reliably new");
                out.toHardest(cur);
            }
        }
        return out.getAlarmType() > ReflectogramMismatch.SEVERITY_NONE
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
    		ModelTraceManager mtm, double level, ReflectogramMismatch alarm)
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
    		ModelTraceManager mtm, ReflectogramMismatch alarm)
    {
		ReflectogramMismatch tmpAlarm = new ReflectogramMismatch();
		final int N = 10;
		for (int i = 0; i <= N; i++) {
			double level = i * 1.0 / N;
			double levelNext = i == N ? 1.0 : (i + 1) * 1.0 / N;
			if (compareTraceToMTMAtLevel(y, mtm, level, tmpAlarm)) {
				alarm.setMismatch(level, levelNext);
			}
		}
    }

    public static ReflectogramMismatch compareTraceToMTM(ModelTrace mt,
            ModelTraceManager mtm)
	{
        // create initial 'no alarm' alarm
		ReflectogramMismatch alarm = new ReflectogramMismatch();
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
				// привязываем к началу события
				int alarmCoord = mtm.fixAlarmPos(alarmStart, true);
				if (alarmEnd < alarmCoord)
					alarmEnd = alarmCoord;
				int level = Thresh.IS_KEY_HARD[key]
					? ReflectogramMismatch.SEVERITY_HARD
					: ReflectogramMismatch.SEVERITY_SOFT;
				if (level == alarm.getSeverity() && alarmCoord < alarm.getCoord()
						|| level > alarm.getSeverity())
				{
					alarm.setSeverity(level);
					alarm.setCoord(alarmCoord);
					alarm.setEndCoord(alarmEnd);
					alarm.setAlarmType(ReflectogramMismatch.TYPE_OUTOFMASK);
                    alarm.setDeltaX(mtm.getMTAE().getDeltaX());
				}
			}
		}
		if (alarm.getSeverity() > ReflectogramMismatch.SEVERITY_NONE) {
			fillAlarmMismatch(y, mtm, alarm);
			Log.debugMessage("ModelTraceComparer.compareTraceToMTM | level " + alarm.getSeverity()
					+ " mismatch "
					+ (alarm.hasMismatch()
						? "" + alarm.getMinMismatch() + ".." + alarm.getMaxMismatch()
						: "<absent>"),
					Level.FINEST);
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

	/**
	 * Устанавливает привязку EventAnchorer результатов анализа на основе
	 * эталонной привязки и сравнения событий результатов анализа и эталона.
	 * @param ar результаты анализа (modify)
	 * @param etalon эталон (read only)
	 */
	public static void createEventAnchor(AnalysisResult ar, Etalon etalon) {
		// берем привязку эталона
		EventAnchorer etAnc = etalon.getAnc();
		if (etAnc == null) { // если ее нет - отвязываем ar
			ar.setAnchorer(null);
			return;
		}

		// берем события ar, проводим сопоставление
		SimpleReflectogramEvent[] events = ar.getMTAE().getSimpleEvents();
		SimpleReflectogramEvent[] etEv = etalon.getMTM().getMTAE().getSimpleEvents();
		SimpleReflectogramEventComparer rc =
				new SimpleReflectogramEventComparer(
						events,
						etEv);

		// устанавливаем "пустую" привязку для ar
		EventAnchorer anc = new EventAnchorer(events.length);
		ar.setAnchorer(anc);

		// привязываем каждое событие ar, имеющее пару в эталоне
		// XXX: надо ли здесь сверять тип?
		for (int i = 0; i < events.length; i++) {
			int etId = rc.getEtalonIdByProbeId(i);
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
	public static void setAlarmAnchors(ReflectogramMismatch ra, Etalon et) {
		int distance = ra.getCoord();
		SOAnchor ref1Id = null; // устанавливаем в "пока не найдено"
		SOAnchor ref2Id = null;
		int ref1Coord = 0; // это делать не обязательно
		int ref2Coord = 0;
		ModelTraceAndEvents mtae = et.getMTM().getMTAE();
		EventAnchorer anc = et.getAnc();
		int len = mtae.getNEvents();
		if (anc != null) {
			for (int i = 0; i < len; i++) {
				SOAnchor ea = anc.getEventAnchor(i);
				SimpleReflectogramEvent se = mtae.getSimpleEvent(i);
	
				// пропускаем события, по которым не может быть привязки
	
				// XXX: вынести проверку типа события во внешний код
				switch (se.getEventType()) {
				case SimpleReflectogramEvent.DEADZONE:   // fall through
				case SimpleReflectogramEvent.ENDOFTRACE: // fall through
				case SimpleReflectogramEvent.CONNECTOR:  // fall through
				case SimpleReflectogramEvent.GAIN: // fall through
				case SimpleReflectogramEvent.LOSS:
					break; // break switch
				default:
					continue; // continue for
				}
	
				// пропускаем события, для которых привязка не определена
	
				if (ea == SOAnchor.VOID_ANCHOR)
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
