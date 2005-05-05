/*-
 * $Id: SimpleReflectogramEventComparer.java,v 1.3 2005/05/05 11:45:28 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.analysis.dadara.events.DetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DetailedEventUtil;

/**
 * Сравнение событий.
 * <p>
 * Пользователь создает объект-компаратор
 * этого класса, дав на входе два массива событий
 * SimpleReflectogramEvent[] - один - пробные события, второй - эталонные.
 * Созданный объект находит соответствия событий, и после этого
 * умеет отвечать на вопросы об изменении состава и параметров событий.
 * <p>
 * Допускает полиморфизм - некоторые вопросы применимы для любых
 * SimpleReflectogramEvent,
 * некоторые - только для ComplexReflectogramEvent,
 * а некоторые - только для ReliabilitySimpleReflectogramEvent
 * <p>
 * NB: результат сопоставления может зависеть от того, являются ли входные
 * события instanceof {@link ReliabilitySimpleReflectogramEvent}.
 * <p>
 * Есть и статические методы, которые сами создают объект-компаратор,
 * и получают из него список изменившихся событий.
 * <p>
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.3 $, $Date: 2005/05/05 11:45:28 $
 * @module
 */
public class SimpleReflectogramEventComparer {
    private SimpleReflectogramEvent[] probeEvents;
    private SimpleReflectogramEvent[] etalonEvents;
    
    private static final int UNPAIRED = -1;
    private static final int RELIABLY_UNPAIRED = -2;

    // Отображение событий пробной р/г на события эталонной
    // и наоборот.
    // значения < 0 - соотв. события не найдено.
    // причем UNPAIRED: пара не найдена, а RELIABLY_UNPAIRED: событие достоверно новое
    // По мере обработки, достигается взаимная однозначность отображений
    // probe2etalon и etalon2probe.
    // NB: входные списки событий должны быть отсортированы заранее.
    private int[] probe2etalon = null;
    private int[] etalon2probe = null;

    //public static long COMPARE_ANALYSE = 0x1;
    //public static long COMPARE_EVALUATE = 0x2;

    // This constructor was added just for slight performance reasons:
    // this way we need not copy 'events' arrays as it would happen
    // if the caller used 'mtaeProbe.getSimpleEvents()'
    public SimpleReflectogramEventComparer(
            ModelTraceAndEventsImpl mtaeProbe,
            ModelTraceAndEventsImpl mtaeEtalon)
    {
        this(mtaeProbe != null ? mtaeProbe.getRSE() : null,
                mtaeEtalon.getRSE(),
                true);
    }

    public SimpleReflectogramEventComparer(
            SimpleReflectogramEvent[] _probeEvents,
            SimpleReflectogramEvent[] _etalonEvents
            )
    {
        this(_probeEvents, _etalonEvents, true);
    }

    /**
     * @param strict укажите false, если допускается, чтобы одному событию
     * эталона соответствовало несколько событий пробного набора, и наоборот.
     * true = нормальное поведение.
     */
    public SimpleReflectogramEventComparer(
            SimpleReflectogramEvent[] _probeEvents,
            SimpleReflectogramEvent[] _etalonEvents,
            boolean strict
            )
    {
        probeEvents = _probeEvents;
        etalonEvents = _etalonEvents;

        // начальный анализ - ищем соответствие событий эталона и пробы
        if (probeEvents != null)
        {
            probe2etalon = findNearestOverlappingEvent(probeEvents, etalonEvents);
            etalon2probe = findNearestOverlappingEvent(etalonEvents, probeEvents);
            if (strict) {
                removeNonPaired(probe2etalon, etalon2probe);
                removeNonPaired(etalon2probe, probe2etalon);
            }
        }
    }
    
    /**
     * Find corresponding etalon event for the specified probe event
     * @param probeId probe event #
     * @return >=0: etalon event #; -1: no etalon event for this probe
     */
    public int getEtalonIdByProbeId(int probeId)
    {
        return probe2etalon[probeId] >= 0 ? probe2etalon[probeId] : -1;
    }
    
    /**
     * Find corresponding probe event for the specified etalon event
     * @param etalonId etalon event #
     * @return >=0: probe event #; -1: no probe event for this etalon
     */
    public int getProbeIdByEtalonId(int etalonId)
    {
        return etalon2probe[etalonId] >= 0 ? etalon2probe[etalonId] : -1;
    }

    public boolean isProbeEventNew(int probeId)
    {
        int etalonId = probe2etalon[probeId];
        return etalonId < 0;
    }

    public boolean isEtalonEventLost(int etalonId)
    {
        int probeId = etalon2probe[etalonId];
        return probeId < 0;
    }

    public boolean isProbeEventReliablyNew(int probeId)
    {
        int etalonId = probe2etalon[probeId];
        return etalonId == RELIABLY_UNPAIRED;
    }

    public boolean isEtalonEventReliablyLost(int etalonId)
    {
        int probeId = etalon2probe[etalonId];
        return probeId == RELIABLY_UNPAIRED;
    }

    // XXX: нужен ли такой метод?
    public int[] getNewEventsList()
    {
        // считаем число новых событий
        int count = 0;
        for (int i = 0; i < probe2etalon.length; i++)
            if (isProbeEventNew(i))
                count++;
        // создаем и заполняем массив новых событий
        int[] ret = new int[count];
        count = 0;
        for (int i = 0; i < probe2etalon.length; i++)
            if (isProbeEventNew(i))
                ret[count++] = i;

        return ret;
    }

    /* Comparison results table ('+' is true, '-' is false)
     * AMPL LOSS TYPE NOL REL_NOL Check type / change type
     *   -    -    -   -     -       no significant change
     *   +    -    -   -     -       changed amplitude
     *   -    +    -   -     -       changed loss
     *   -    -    +   -     -       changed type
     *   +    +    +   +     -       non-reliably new or lost
     *   +    +    +   +     +       reliably new or lost
     */

    public static final int CHANGETYPE_AMPL = 0x1; // requires ComplexReflectogramEvents
    public static final int CHANGETYPE_LOSS = 0x2; // requires ComplexReflectogramEvents
    public static final int CHANGETYPE_TYPE = 0x4; // requires SimpleReflectogramEvents
    public static final int CHANGETYPE_NEW_OR_LOST = 0x8;  // requires ComplexReflectogramEvents
    public static final int CHANGETYPE_RELIABLY_NEW_OR_LOST = 0x10;  // requires ReliabilityReflectogramEvents
    
    /**
     * @throws ClassCastException if comparison type is not supported by input events type
     */
    public static boolean eventsAreDifferent(
            SimpleReflectogramEvent a, // not null
            SimpleReflectogramEvent b, // not null
            int changeType, // one of CHANGETYPE's
            double changeThreshold) // may be zero; may be unused
    {
        switch(changeType)
        {
        case CHANGETYPE_AMPL:
            if (a instanceof ComplexReflectogramEvent)
                return Math.abs(((ComplexReflectogramEvent)a).getALet() - ((ComplexReflectogramEvent)b).getALet()) > changeThreshold;
            else {
                try {
                    return Math.abs(DetailedEventUtil.getAmplDiff(
                                (DetailedEvent)a, (DetailedEvent)b))
                            > changeThreshold;
                } catch (NoSuchFieldException e) {
                    return false; // treat no difference if parameter is not present
                }
            }

        case CHANGETYPE_LOSS:
            if (a instanceof ComplexReflectogramEvent)
                return Math.abs(((ComplexReflectogramEvent)a).getMLoss() - ((ComplexReflectogramEvent)b).getMLoss()) > changeThreshold;
            else {
                try {
                    return Math.abs(DetailedEventUtil.getLossDiff(
                                (DetailedEvent) a, (DetailedEvent) b))
                            > changeThreshold;
                } catch (NoSuchFieldException e) {
                    return false; // treat no difference if parameter is not present
                }
            }

        case CHANGETYPE_TYPE:
            // при changeThreshold == 0 и 1 работать должно одинаково хорошо
            return a.getEventType() != b.getEventType();

        case CHANGETYPE_NEW_OR_LOST:
            // fall through
        case CHANGETYPE_RELIABLY_NEW_OR_LOST:
            return false; // пара событий уже дана - значит изменения нет

        }
        // unknown criterion
        return false;
    }

    // чтобы узнать, что событие изменилось,
    // надо сначала проверить isEtalonEventLost.
    // в противном случае ответ зависит от реализации
    public boolean isEtalonEventChanged(int etalonId, int changeType, double changeThreshold)
    {
        int probeId = etalon2probe[etalonId];

        if (changeType == CHANGETYPE_RELIABLY_NEW_OR_LOST)
            return probeId == RELIABLY_UNPAIRED;

        if (probeId < 0) // событие исчезло
            return true;

        return eventsAreDifferent(
            etalonEvents[etalonId],
            probeEvents[probeId],
            changeType,
            changeThreshold);
    }

    // чтобы узнать, что событие изменилось,
    // надо сначала проверить isProbeEventNew.
    // В противном случае - ошибка (для отладки)
    public boolean isProbeEventChanged(int probeId, int changeType, double changeThreshold)
    {
        int etalonId = probe2etalon[probeId];

        if (changeType == CHANGETYPE_RELIABLY_NEW_OR_LOST)
            return etalonId == RELIABLY_UNPAIRED;

        if (etalonId < 0) // событие появилось
            return true;

        return eventsAreDifferent(
            etalonEvents[etalonId],
            probeEvents[probeId],
            changeType,
            changeThreshold);
    }

    // internal events comparison

    private boolean eventsOverlaps(SimpleReflectogramEvent x, SimpleReflectogramEvent y)
    {
        return Math.max(x.getBegin(), y.getBegin()) <= Math.min(x.getEnd(), y.getEnd());
    }

    private int calcEventsDistance(SimpleReflectogramEvent x, SimpleReflectogramEvent y)
    {
        return Math.abs(x.getBegin() - y.getBegin()) + Math.abs(x.getEnd() - y.getEnd());
    }

    private void removeNonPaired(int[] fwd, int[] backwd)
    {
        for (int i = 0; i < fwd.length; i++)
        {
            int j = fwd[i];
            if (j >= 0 && backwd[j] != i)
                fwd[i] = -1;
        }
    }

    private int[] findNearestOverlappingEvent(SimpleReflectogramEvent[] X, SimpleReflectogramEvent[] Y)
    {
        int[] ret = new int[X.length];
        for (int i = 0; i < X.length; i++)
        {
            // наилучшее по нашей метрике парное событие
            double bestDistance = UNPAIRED; // Stands for +inf
            int bestJ = -1;
            // значимые нелин. события - потенциальные кандидаты в новые/потерянные
            boolean reliablyNewOrLost = X[i].getEventType() != SimpleReflectogramEvent.LINEAR
                && (X[i] instanceof ReliabilitySimpleReflectogramEvent)
                && ((ReliabilitySimpleReflectogramEvent)X[i]).hasReliability()
                && ((ReliabilitySimpleReflectogramEvent)X[i]).getReliability() > ReliabilitySimpleReflectogramEvent.RELIABLE; 
            for (int j = 0; j < Y.length; j++)
            {
                if (eventsOverlaps(X[i], Y[j]))
                {
                    double distance = calcEventsDistance(X[i], Y[j]); 
                    if (bestDistance < 0 || distance < bestDistance)
                    {
                        bestJ = j;
                        bestDistance = distance;
                    }
                    // у значимого нел. соб. нашли хоть какую-то пару -- оно не "значимо новое/потерянное"
                    if (Y[j].getEventType() != SimpleReflectogramEvent.LINEAR)
                        reliablyNewOrLost = false;
                }
            }
            if (reliablyNewOrLost)
                ret[i] = RELIABLY_UNPAIRED;
            else
                ret[i] = bestJ;
        }
        return ret;
    }

    //

    private static int[] getChangedProbeEventsList(ComplexReflectogramEvent[] data,
            ComplexReflectogramEvent[] etalon,
            int changeType,
            double changeThreshold)
    {
        if (data == null || etalon == null)
            return new int[0];
        SimpleReflectogramEventComparer comparer =
            new SimpleReflectogramEventComparer(data, etalon, true);
        int count = 0;
        for (int i = 0; i < data.length; i++)
            if (comparer.isProbeEventChanged(i, changeType, changeThreshold))
                count++;
        int[] ret = new int[count];
        count = 0;
        for (int i = 0; i < data.length; i++)
            if (comparer.isProbeEventChanged(i, changeType, changeThreshold))
                ret[count++] = i;
        return ret;
    }

    // NEW IMPLEMENTATION OF OLD CONTRACT (as far as I guess it)
    public static int[] getNewEventsList(
            ComplexReflectogramEvent[] data,
            ComplexReflectogramEvent[] etalon)
    {
        return getChangedProbeEventsList(data, etalon, CHANGETYPE_NEW_OR_LOST, 0);
    }

    public static int[] getChangedAmplitudeEventsList(
            ComplexReflectogramEvent[] data,
            ComplexReflectogramEvent[] etalon,
            double threshold)
    {
        return getChangedProbeEventsList(data, etalon, CHANGETYPE_AMPL, threshold);
    }
    
    public static int[] getChangedLossEventsList(
            ComplexReflectogramEvent[] data,
            ComplexReflectogramEvent[] etalon,
            double threshold)
    {
        return getChangedProbeEventsList(data, etalon, CHANGETYPE_LOSS, threshold);
    }
}
