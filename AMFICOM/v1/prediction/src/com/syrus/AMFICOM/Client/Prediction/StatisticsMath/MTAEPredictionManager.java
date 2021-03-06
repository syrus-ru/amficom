/*-
 * $Id: MTAEPredictionManager.java,v 1.9 2006/03/28 10:37:06 saa Exp $
 * 
 * Copyright ? 2005 Syrus Systems.
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
import com.syrus.AMFICOM.analysis.dadara.events.DeadZoneDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.HavingAmpl;
import com.syrus.AMFICOM.analysis.dadara.events.HavingLoss;
import com.syrus.AMFICOM.analysis.dadara.events.HavingY0;
import com.syrus.AMFICOM.analysis.dadara.events.LinearDetailedEvent;
import com.syrus.AMFICOM.measurement.MonitoredElement;

/**
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.9 $, $Date: 2006/03/28 10:37:06 $
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

	private SimpleReflectogramEventComparer[] srecCache; // comparer for each trace

	// stats for every event
	private Statistics[] lossStatsCache; 
	private Statistics[] attenuationStatsCache;
	private Statistics[] y0StatsCache;
	private Statistics[] amplStatsCache;

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

	private final InfoExtractor amplExtractor = new InfoExtractor() {

		public boolean hasParameter(DetailedEvent event) {
			return event instanceof HavingAmpl;
		}

		public double getParameter(DetailedEvent event) {
			return ((HavingAmpl)event).getAmpl();
		}
	};

	private final InfoExtractor y0Extractor = new InfoExtractor() {

		public boolean hasParameter(DetailedEvent event) {
			return event instanceof HavingY0
				|| event instanceof DeadZoneDetailedEvent;
		}

		public double getParameter(DetailedEvent event) {
			return event instanceof HavingY0
					? ((HavingY0)event).getY0()
					: ((DeadZoneDetailedEvent)event).getPo();
		}
	};

	/**
	 * @param coll ???????? ????? ??? "??????????????-?????"
	 * @param lowerTime ????????? ?????? ?????????? ?????????
	 * @param upperTime ???????? ?????? ?????????? ?????????
	 * @param me MonitoredElement
	 * @throws IllegalArgumentException ??????? ????? ??? ????
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
		this.srecCache = new SimpleReflectogramEventComparer[this.data.length];
		this.lossStatsCache = new Statistics[this.base.getNEvents()];
		this.attenuationStatsCache = new Statistics[this.base.getNEvents()];
		this.y0StatsCache = new Statistics[this.base.getNEvents()];
		this.amplStatsCache = new Statistics[this.base.getNEvents()];
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
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#hasY0Info(int)
	 */
	public boolean hasY0Info(int nEvent) {
		return hasInfo(nEvent, y0Extractor);
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getY0Info(int)
	 */
	public Statistics getY0Info(int nEvent) {
		return getInfo(nEvent, y0Extractor, this.y0StatsCache, "db");
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#hasReflectiveAmplitudeInfo(int)
	 */
	public boolean hasReflectiveAmplitudeInfo(int nEvent) {
		return hasInfo(nEvent, amplExtractor);
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getReflectiveAmplitudeInfo(int)
	 */
	public Statistics getReflectiveAmplitudeInfo(int nEvent) {
		return getInfo(nEvent, amplExtractor, this.amplStatsCache, "db");
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#hasAttenuationInfo(int)
	 */
	public boolean hasAttenuationInfo(int nEvent) {
		return hasInfo(nEvent, attenuationExtractor);
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getAttenuationInfo(int)
	 */
	public Statistics getAttenuationInfo(int nEvent) {
		return getInfo(nEvent, attenuationExtractor, this.attenuationStatsCache, "db/km");
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#hasLossInfo(int)
	 */
	public boolean hasLossInfo(int nEvent) {
		return hasInfo(nEvent, lossExtractor);
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getLossInfo(int)
	 */
	public Statistics getLossInfo(int nEvent) {
		return getInfo(nEvent, lossExtractor, this.lossStatsCache, "db");
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#hasReflectanceInfo(int)
	 */
	public boolean hasReflectanceInfo(int nEvent) {
		return false;
	}

	/**
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getReflectanceInfo(int)
	 * @throws UnsupportedOperationException not supported
	 */
	public Statistics getReflectanceInfo(int nEvent) {
		throw new UnsupportedOperationException();
	}

	/**
	 * XXX: ?? ?????????
	 * @param date
	 * @throws IllegalStateException ???? ??? ????????? ?????? ????????? ? ?????? ??????? ???????
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getPredictedReflectogram(long)
	 */
	public double[] getPredictedReflectogram(long target) {
		// ?????????, ??? ???????? ??????? ?? ???????,
		// ????? ????????? ??????? ?? ????????? ???????
		if (getMinTime() == getMaxTime()) {
			throw new IllegalStateException();
		}

		// ???? ?????????? ?????? lowercase ???????? ??? N, ??????? ???.
		// -- saa
		final int count = this.data.length;

		// ?????????? ??????? ????????????? ???????? ???.
		// ????? ?? ????????????? ???????????, ??????? ?????????? ?????? ??????,
		// ? ?????? ????? - ?????????.
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

		// ?????????? ????? ?????????????? ?/? ??? ??????????? ????? ?? ?????
		int resLen = 0; // ????????????? ?????? ??? ???????? ???????????
		for (int i = 0; i < count; i++) {
			final int len = this.data[i].getMtae().getModelTrace().getLength();
			if (i == 0 || len < resLen) {
				resLen = len;
			}
		}

		// ??????????????

		// XXX: ????? ????? ????????? ??? ?/? ???????? ? ????? mtae,
		// ?, ? ??????????, ?????? ???????????? ????? ??????.
		// ????? ????, ? ??????? ?????????? MTAEI, ??? ??????????
		// ???? ?? ???? ????????? - ??? ? ?????? ???????? mtae,
		// ?.?. ??? ?????????? ????-???????? ????.

		double[] result = new double[resLen]; // ???????????????? ??????
		for (int i = 0; i < count; i++) {
			// ???????? ?? ???? ????? ?/?, ?.?. ??? ????? ???? ????? ??? mtae.
			double[] trace = this.data[i].getMtae().getModelTrace().getYArray(
					0, resLen);
			// ?????????
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

	private boolean hasInfo(int nEvent,
			InfoExtractor extractor) {
		if (nEvent < 0 || nEvent >= this.base.getNEvents()) {
			throw new IllegalArgumentException(
					"nEvent " + nEvent + " / " + this.base.getNEvents());
		}
		return extractor.hasParameter(this.base.getDetailedEvent(nEvent));
	}

	private Statistics getInfo(int nEvent,
			InfoExtractor extractor,
			Statistics[] statsCache,
			String dimension) {
		if (nEvent < 0 || nEvent >= this.base.getNEvents()) {
			throw new IllegalArgumentException(
					"nEvent " + nEvent + " / " + this.base.getNEvents());
		}
		if (statsCache[nEvent] == null) {
			List<TimeDependenceData> tdd = new ArrayList<TimeDependenceData>();
			for (int i = 0; i < this.data.length; i++) {
				int nEv2 = getSREC(i).getProbeIdByEtalonId(nEvent);
				if (nEv2 < 0)
					continue; // no event
				final DetailedEvent event =
					this.data[i].getMtae().getDetailedEvent(nEv2);
				if (!extractor.hasParameter(event))
					continue;
				final double value = extractor.getParameter(event);
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
