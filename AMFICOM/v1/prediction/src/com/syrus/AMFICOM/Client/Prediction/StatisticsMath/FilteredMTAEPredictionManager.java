/*-
 * $Id: FilteredMTAEPredictionManager.java,v 1.2 2006/04/03 09:25:28 saa Exp $
 * 
 * Copyright © 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Prediction.StatisticsMath;

import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.MTAEPredictionManager.PredictionMtaeAndDate;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEvents;
import com.syrus.AMFICOM.measurement.MonitoredElement;

/**
 * Дополняет функции {@link MTAEPredictionManager} возможностью
 * выбирать, какие из уже загруженных рефлектограмм использовать
 * для прогнозирования, а какие нет.
 * Рефлектограммы идентифицируются по ключу типа String.
 * Функции прогнозирования должны вызываться только если
 * текущий набор разрешенных для прогнозирования рефлектограмм
 * достаточен для прогнозирования. В противном случае 
 * {@link MTAEPredictionManager} генерирует
 * исключения {@link IllegalStateException}, {@link IllegalArgumentException}.
 * 
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2006/04/03 09:25:28 $
 * @module prediction
 */
public class FilteredMTAEPredictionManager implements PredictionManager {

	/**
	 * текущий predictionManager либо null, если набор рефлектограмм
	 * изменился, а предсказание еще не запрошено.
	 * (фактически что-то типа кэша)
	 */
	private PredictionManager internalManager;

	/**
	 * Все загруженные р/г
	 */
	private Map<String, PredictionMtaeAndDate> allTraces;
	/**
	 * Активные р/г
	 */
	private Map<String, PredictionMtaeAndDate> activeTraces;

	private ModelTraceAndEvents base;
	private long lowerTime;
	private long upperTime;
	private MonitoredElement me;

	/**
	 * Создает предсказатель с заданными параметрами рефлектограмм
	 * и флажками использования рефлектограмм "все использовать".
	 * @param data карта рефлектограмм {ключ => пара "рефлектограмма-время"}
	 * @param lowerTime начальный момент временного интервала
	 * @param upperTime конечный момент временного интервала
	 * @param me MonitoredElement
	 * @throws IllegalArgumentException входной набор пар пуст
	 */
	public FilteredMTAEPredictionManager(
			Map<String, PredictionMtaeAndDate> traces,
			ModelTraceAndEvents base,
			long lowerTime,
			long upperTime,
			MonitoredElement me) {
		// делаем копию исходного набора рефлектограмм
		this.allTraces = new HashMap<String, PredictionMtaeAndDate>(
				traces.size());
		this.allTraces.putAll(traces);
		// создаем набор текущих активных рефлектограмм, включив туда все
		this.activeTraces = new HashMap<String, PredictionMtaeAndDate>(
				allTraces.size());
		this.activeTraces.putAll(allTraces);
		// инициализируем данные для PredictionManager
		this.internalManager = null;
		this.base = base;
		this.lowerTime = lowerTime;
		this.upperTime = upperTime;
		this.me = me;
	}

	/**
	 * Устанавливает, использовать ли данную рефлектограмму.
	 * @param key ключ рефлектограммы в исходной карте рефлектограмм
	 * @param active true, чтобы разрешить использование рефлектограммы;
	 * false, чтобы запретить.
	 */
	public void setActive(String key, boolean active) {
		if (!this.allTraces.containsKey(key)) {
			throw new IllegalArgumentException("Invalid key");
		}
		if (this.activeTraces.containsKey(key) == active) {
			return;
		}
		if (active) {
			this.activeTraces.put(key, allTraces.get(key));
		} else {
			this.activeTraces.remove(key);
		}
		this.resetManager();
	}

	private void resetManager() {
		this.internalManager = null;
	}

	private PredictionManager getManager() {
		if (this.internalManager == null) {
			this.internalManager = new MTAEPredictionManager(
					this.activeTraces.values(),
					this.base,
					this.lowerTime,
					this.upperTime,
					this.me);
		}
		return this.internalManager;
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getMinTime()
	 */
	public long getMinTime() {
		return getManager().getMinTime();
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getMaxTime()
	 */
	public long getMaxTime() {
		return getManager().getMaxTime();
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getLowerTime()
	 */
	public long getLowerTime() {
		return this.lowerTime;
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getUpperTime()
	 */
	public long getUpperTime() {
		return this.upperTime;
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getMonitoredElement()
	 */
	public MonitoredElement getMonitoredElement() {
		return this.me;
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#hasAttenuationInfo(int)
	 */
	public boolean hasAttenuationInfo(int nEvent) {
		return getManager().hasAttenuationInfo(nEvent);
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getAttenuationInfo(int)
	 */
	public Statistics getAttenuationInfo(int nEvent) {
		return getManager().getAttenuationInfo(nEvent);
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#hasLossInfo(int)
	 */
	public boolean hasLossInfo(int nEvent) {
		return getManager().hasLossInfo(nEvent);
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getLossInfo(int)
	 */
	public Statistics getLossInfo(int nEvent) {
		return getManager().getLossInfo(nEvent);
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#hasReflectiveAmplitudeInfo(int)
	 */
	public boolean hasReflectiveAmplitudeInfo(int nEvent) {
		return getManager().hasReflectiveAmplitudeInfo(nEvent);
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getReflectiveAmplitudeInfo(int)
	 */
	public Statistics getReflectiveAmplitudeInfo(int nEvent) {
		return getManager().getReflectiveAmplitudeInfo(nEvent);
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#hasY0Info(int)
	 */
	public boolean hasY0Info(int nEvent) {
		return getManager().hasY0Info(nEvent);
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getY0Info(int)
	 */
	public Statistics getY0Info(int nEvent) {
		return getManager().getY0Info(nEvent);
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#hasReflectanceInfo(int)
	 */
	public boolean hasReflectanceInfo(int nEvent) {
		return getManager().hasReflectanceInfo(nEvent);
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getReflectanceInfo(int)
	 */
	public Statistics getReflectanceInfo(int nEvent) {
		return getManager().getReflectanceInfo(nEvent);
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getPredictedReflectogram(long)
	 */
	public double[] getPredictedReflectogram(long date) {
		return getManager().getPredictedReflectogram(date);
	}

}
