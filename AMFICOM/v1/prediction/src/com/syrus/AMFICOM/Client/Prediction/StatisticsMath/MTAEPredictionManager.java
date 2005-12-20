/*-
 * $Id: MTAEPredictionManager.java,v 1.3 2005/12/20 15:54:33 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Prediction.StatisticsMath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEvents;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEventComparer;
import com.syrus.AMFICOM.analysis.dadara.events.DetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.HavingLoss;
import com.syrus.AMFICOM.analysis.dadara.events.LinearDetailedEvent;
import com.syrus.AMFICOM.measurement.MonitoredElement;

/**
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.3 $, $Date: 2005/12/20 15:54:33 $
 * @module prediction
 */
public class MTAEPredictionManager implements PredictionManager {
	public static class PredictionMtaeAndDate {
		private ModelTraceAndEvents mtae;
		private long date;

		private static final Comparator<PredictionMtaeAndDate> COMPARATOR =
			new Comparator<PredictionMtaeAndDate>() {
			public int compare(PredictionMtaeAndDate o1, PredictionMtaeAndDate o2) {
				long delta = o1.getDate() - o2.getDate();
				return delta <= 0 ? delta < 0 ? -1 : 0 : 1; // int is narrower than long
			}
		};

		public PredictionMtaeAndDate(ModelTraceAndEvents mtae, long date) {
			this.mtae = mtae;
			this.date = date;
		}
		public long getDate() {
			return this.date;
		}
		public ModelTraceAndEvents getMtae() {
			return this.mtae;
		}
		public static Comparator<PredictionMtaeAndDate> getComparator() {
			return COMPARATOR;
		}
	}

	private ModelTraceAndEvents base;
	private PredictionMtaeAndDate[] data; // length >= 1
	private long lowerTime;
	private long upperTime;
	private MonitoredElement me;
	private double[] temp; // temporary array for output data

	private SimpleReflectogramEventComparer[] srecCache;
	private Statistics[] lossStatsCache;
	private Statistics[] attenuationStatsCache;

	private final InfoExtractor lossExtractor = new InfoExtractor() {
		public boolean hasParameter(DetailedEvent event) {
			return event instanceof HavingLoss;
		}
		public double getParameter(DetailedEvent event) {
			return ((HavingLoss)event).getLoss();
		}
	};

	private final InfoExtractor attenuationExtractor = new InfoExtractor() {
		public boolean hasParameter(DetailedEvent event) {
			return event instanceof LinearDetailedEvent;
		}
		public double getParameter(DetailedEvent event) {
			return ((LinearDetailedEvent)event).getAttenuation();
		}
	};

	/**
	 * @param coll непустой набор пар "рефлектограмма-время"
	 * @param lowerTime начальный момент временного интервала
	 * @param upperTime конечный момент временного интервала
	 * @param me MonitoredElement
	 * @throws IllegalArgumentException входной набор пар пуст
	 */
	public MTAEPredictionManager(Collection<PredictionMtaeAndDate> coll,
			ModelTraceAndEvents base,
			long lowerTime,
			long upperTime,
			MonitoredElement me) {
		this.data = coll.toArray(new PredictionMtaeAndDate[coll.size()]);
		if (this.data.length < 1)
			throw new IllegalArgumentException();
		Arrays.sort(this.data, PredictionMtaeAndDate.getComparator());
		this.base = base;
		this.lowerTime = lowerTime;
		this.upperTime = upperTime;
		this.me = me;
		this.temp = new double[this.data.length];
		this.srecCache = new SimpleReflectogramEventComparer[this.data.length];
		this.lossStatsCache = new Statistics[this.data.length];
		this.attenuationStatsCache = new Statistics[this.data.length];
		assert(getMinTime() <= getMaxTime());
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getMinTime()
	 */
	public long getMinTime() {
		return this.data[0].getDate();
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getMaxTime()
	 */
	public long getMaxTime() {
		return this.data[this.data.length - 1].getDate();
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
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getAmplitudeInfo(int)
	 * @throws UnsupportedOperationException not supported
	 */
	public Statistics getAmplitudeInfo(int nEvent) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getSplashAmplitudeInfo(int)
	 * @throws UnsupportedOperationException not supported
	 */
	public Statistics getSplashAmplitudeInfo(int nEvent) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getAttenuationInfo(int)
	 */
	public Statistics getAttenuationInfo(int nEvent) {
		return getInfo(nEvent, attenuationExtractor, this.attenuationStatsCache, "db/km");
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getEnergyLossInfo(int)
	 */
	public Statistics getEnergyLossInfo(int nEvent) {
		return getInfo(nEvent, lossExtractor, this.lossStatsCache, "db");
	}

	/**
	 * @throws UnsupportedOperationException not supported
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getReflectanceInfo(int)
	 */
	public Statistics getReflectanceInfo(int nEvent) {
		throw new UnsupportedOperationException();
	}

	/**
	 * XXX: не проверено
	 * @param date
	 * @throws IllegalStateException если все имеющиеся данные относятся к одному моменту времени
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getPredictedReflectogram(long)
	 */
	public double[] getPredictedReflectogram(long target) {
		// проверяем, что интервал времени не нулевой,
		// чтобы дисперсия времени не оказалась нулевой
		if (getMinTime() == getMaxTime()) {
			throw new IllegalStateException();
		}

		// Если придумаете лучшее lowercase название для N, скажите мне.
		// -- saa
		final int count = this.data.length;

		// Определяем моменты распределения исходных дат.
		// Чтобы не накапливалась погрешность, сначала определяем первый момент,
		// а только затем - дисперсию.
		double mx = 0.0;
		for (PredictionMtaeAndDate pmad: this.data) {
			mx += pmad.getDate();
		}
		mx /= count;
		double mxx = 0.0;
		for (PredictionMtaeAndDate pmad: this.data) {
			final double d = pmad.getDate() - mx;
			mxx += d * d;
		}
		mxx /= count;

		// Определяем длину результирующей р/г как минимальную длину на входе
		int resLen = 0; // инициализация только для утешения компилятора
		for (int i = 0; i < count; i++) {
			final int len = this.data[i].getMtae().getModelTrace().getLength();
			if (i == 0 || len < resLen) {
				resLen = len;
			}
		}

		// Экстраполируем

		// XXX: после такой процедуры все р/г окажутся в кэшах mtae,
		// и, в результате, займут относительно много памяти.
		// Кроме того, в текущей реализации MTAEI, это произойдет
		// даже до этой процедуры - уже в момент загрузки mtae,
		// т.к. они используют пред-загрузку кэша.

		double[] result = new double[resLen]; // инициализируется нулями
		for (int i = 0; i < count; i++) {
			// получаем на всей длине р/г, т.к. это может быть проще для mtae.
			double[] trace = this.data[i].getMtae().getModelTrace().getYArray(
					0, resLen);
			// суммируем
			final double sx = (target - mx) * (this.data[i].getDate() - mx);
			double wei = (sx / mxx + 1.0) / count;
			for (int k = 0; k < resLen; k++) {
				result[k] += wei * trace[k];
			}
		}

		return result;
	}

	private static interface InfoExtractor {
		boolean hasParameter(DetailedEvent event);
		double getParameter(DetailedEvent event);
	}

	public Statistics getInfo(int nEvent,
			InfoExtractor extractor,
			Statistics[] statsCache,
			String dimension) {
		if (nEvent < 0 || nEvent >= this.base.getNEvents()) {
			throw new IllegalArgumentException(
					"nEvent " + nEvent + " / " + this.base.getNEvents());
		}
		if (statsCache[nEvent] == null) {
			List<TimeDependenceData> tdd = new ArrayList<TimeDependenceData>();
			int pos = 0;
			for (int i = 0; i < this.data.length; i++) {
				int nEv2 = getSREC(i).getProbeIdByEtalonId(nEvent);
				if (nEv2 < 0)
					continue; // no event
				final DetailedEvent event =
					this.data[i].getMtae().getDetailedEvent(nEvent);
				if (!extractor.hasParameter(event))
					continue;
				final double value = extractor.getParameter(event);
				this.temp[pos++] = value;
				tdd.add(new TimeDependenceData(this.data[i].getDate(), value));
			}
			final TimeDependenceData[] tddArray = tdd.toArray(
					new TimeDependenceData[tdd.size()]);
			LinearCoeffs lc = Fitting.performLinearFitting(tddArray);
			statsCache[nEvent] = new Statistics(
					tddArray, dimension, null, lc);
		}
		return statsCache[nEvent];
	}

	private SimpleReflectogramEventComparer getSREC(int i) {
		if (srecCache[i] == null) {
			srecCache[i] = new SimpleReflectogramEventComparer(
						this.data[i].getMtae().getSimpleEvents(),
						this.base.getSimpleEvents());
		}
		return srecCache[i];
	}
}
