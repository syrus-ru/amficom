/*-
 * $Id: OverallStats.java,v 1.15 2006/01/12 13:00:09 saa Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatchImpl;
import com.syrus.AMFICOM.analysis.dadara.TraceEvent;
import com.syrus.AMFICOM.reflectometry.ReflectometryEvaluationOverallResult;

/**
 * @author $Author: saa $
 * @version $Revision: 1.15 $, $Date: 2006/01/12 13:00:09 $
 * @module analysis
 */

public class OverallStats {
	private String totalLength;
	private String totalLoss;
	private String totalAttenuation;
	private String totalReturnLoss;
	private String totalNoiseLevel;
	private String totalNoiseDD;
	private String totalNoiseDDRMS;
	private String totalEvents;

	private String etalonLength;
	private String maxDeviation;
	private String meanDeviation;
	private String dLoss;

	private String mismatch;

	private String qualityQ;
	private String qualityD;

	private List<PropertyChangeListener> propertyChangeListeners;
	
	public void initGeneralStatistics(TraceEvent ev, PFTrace pf) {
		double range_km = ev.last_point * pf.getResolution() / 1000.0;
		double loss = ev.overallStatsLoss();
		double attenuation = loss / range_km;
		double orl = MathRef.calcORL(ev.overallStatsY0(), ev.overallStatsY1());
		double noise = ev.overallStatsNoiseLevel98Pct();
		double DD98 = ev.overallStatsDD98pct();
		double DDRMS = ev.overallStatsDDRMS();
		int evNum = ev.overallStatsEvNum();

		setTotalLength(MathRef.round_3(range_km) + " " + LangModelAnalyse.getString("km"));
		setTotalLoss(MathRef.round_2(loss) + " " + LangModelAnalyse.getString("dB"));
		setTotalAttenuation(MathRef.round_4(attenuation) + " " + LangModelAnalyse.getString("dB/km"));
		setTotalReturnLoss(MathRef.round_2(orl) + " " + LangModelAnalyse.getString("dB"));
		setTotalNoiseLevel(MathRef.round_2(noise) + " " + LangModelAnalyse.getString("dB"));
		setTotalNoiseDD(MathRef.round_2(DD98) + " " + LangModelAnalyse.getString("dB"));
		setTotalNoiseDDRMS(MathRef.round_2(DDRMS) + " " + LangModelAnalyse.getString("dB"));
		setTotalEvents(String.valueOf(evNum));
	}

	/**
	 * 
	 * @param maxDeviation1
	 * @param meanDeviation1
	 * @param etalonLength1
	 * @param lossDifference1 потери, may be NaN
	 * @param alarm
	 * @param evaluationOverall
	 */
	public void initCompareStatistics(double maxDeviation1,
			double meanDeviation1,
			double etalonLength1,
			double lossDifference1,
			ReflectogramMismatchImpl alarm,
			ReflectometryEvaluationOverallResult evaluationOverall) {
		setEtalonLength(String.valueOf(MathRef.round_3(etalonLength1)) + " " + LangModelAnalyse.getString("km"));
		setMaxDeviation(String.valueOf(MathRef.round_4(maxDeviation1)) + " " + LangModelAnalyse.getString("dB"));
		setMeanDeviation(String.valueOf(MathRef.round_4(meanDeviation1)) + " " + LangModelAnalyse.getString("dB"));
		if (Double.isNaN(lossDifference1)) {
			setDLoss("");
		} else {
			setDLoss(String.valueOf(MathRef.round_4(lossDifference1))
					+ " " + LangModelAnalyse.getString("dB"));
		}
		if (alarm == null) {
			setMismatch(LangModelAnalyse.getString(("mismatchNo")));
		} else {
			setMismatch(String.valueOf(MathRef.round_3(alarm.getDistance() / 1000))
					+ " "
					+ LangModelAnalyse.getString(("mismatchDistance")));
		}
		if (evaluationOverall == null) {
			setQualityD("");
			setQualityQ("");
		} else {
			if (evaluationOverall.hasDQ()) {
				setQualityD(
						String.valueOf(MathRef.round_3(
								evaluationOverall.getD())) + " " +
						LangModelAnalyse.getString("qualityDSuffix"));
				setQualityQ(
						String.valueOf(MathRef.round_2(
								evaluationOverall.getQ())));
			} else {
				setQualityD("");
				setQualityQ("");
			}
		}
	}

	public String getTotalAttenuation() {
		return this.totalAttenuation;
	}
	
	public void setTotalAttenuation(String totalAttenuation) {
		if (this.totalAttenuation == null || !this.totalAttenuation.equals(totalAttenuation)) {
			String oldValue = this.totalAttenuation;
			this.totalAttenuation = totalAttenuation;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, OverallStatsWrapper.KEY_ATTENUATION, oldValue, totalAttenuation));
		}
	}
	
	public String getTotalEvents() {
		return this.totalEvents;
	}
	
	public void setTotalEvents(String totalEvents) {
		if (this.totalEvents == null || !this.totalEvents.equals(totalEvents)) {
			String oldValue = this.totalEvents;
			this.totalEvents = totalEvents;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, OverallStatsWrapper.KEY_EVENTS, oldValue, totalEvents));
		}
	}
	
	public String getTotalLength() {
		return this.totalLength;
	}
	
	public void setTotalLength(String totalLength) {
		if (this.totalLength == null || !this.totalLength.equals(totalLength)) {
			String oldValue = this.totalLength;
			this.totalLength = totalLength;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, OverallStatsWrapper.KEY_LENGTH, oldValue, totalLength));
		}
	}
	
	public String getTotalLoss() {
		return this.totalLoss;
	}
	
	public void setTotalLoss(String totalLoss) {
		if (this.totalLoss == null || !this.totalLoss.equals(totalLoss)) {
			String oldValue = this.totalLoss;
			this.totalLoss = totalLoss;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, OverallStatsWrapper.KEY_LOSS, oldValue, totalLoss));
		}
	}
	
	public String getTotalNoiseDD() {
		return this.totalNoiseDD;
	}
	
	public void setTotalNoiseDD(String totalNoiseDD) {
		if (this.totalNoiseDD == null || !this.totalNoiseDD.equals(totalNoiseDD)) {
			String oldValue = this.totalNoiseDD;
			this.totalNoiseDD = totalNoiseDD;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, OverallStatsWrapper.KEY_NOISE_DD, oldValue, totalNoiseDD));
		}
	}
	
	public String getTotalNoiseDDRMS() {
		return this.totalNoiseDDRMS;
	}
	
	public void setTotalNoiseDDRMS(String totalNoiseDDRMS) {
		if (this.totalNoiseDDRMS == null || !this.totalNoiseDDRMS.equals(totalNoiseDDRMS)) {
			String oldValue = this.totalNoiseDDRMS;
			this.totalNoiseDDRMS = totalNoiseDDRMS;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, OverallStatsWrapper.KEY_NOISE_DDRMS, oldValue, totalNoiseDDRMS));
		}
	}
	
	public String getTotalNoiseLevel() {
		return this.totalNoiseLevel;
	}
	
	public void setTotalNoiseLevel(String totalNoiseLevel) {
		if (this.totalNoiseLevel == null || !this.totalNoiseLevel.equals(totalNoiseLevel)) {
			String oldValue = this.totalNoiseLevel;
			this.totalNoiseLevel = totalNoiseLevel;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, OverallStatsWrapper.KEY_NOISE_LEVEL, oldValue, totalNoiseLevel));
		}
	}
	
	public String getTotalReturnLoss() {
		return this.totalReturnLoss;
	}
	
	public void setTotalReturnLoss(String totalReturnLoss) {
		if (this.totalReturnLoss == null || !this.totalReturnLoss.equals(totalReturnLoss)) {
			String oldValue = this.totalReturnLoss;
			this.totalReturnLoss = totalReturnLoss;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, OverallStatsWrapper.KEY_RETURN_LOSS, oldValue, totalReturnLoss));
		}
	}
	
	public String getDLoss() {
		return this.dLoss;
	}
	
	public void setDLoss(String loss) {
		if (this.dLoss == null || !this.dLoss.equals(loss)) {
			String oldValue = this.dLoss;
			this.dLoss = loss;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, OverallStatsWrapper.KEY_D_LOSS, oldValue, this.dLoss));
		}
	}
	
	public String getEtalonLength() {
		return this.etalonLength;
	}
	
	public void setEtalonLength(String etalonLength) {
		if (this.etalonLength == null || !this.etalonLength.equals(etalonLength)) {
			String oldValue = this.etalonLength;
			this.etalonLength = etalonLength;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, OverallStatsWrapper.KEY_ETALON_LENGTH, oldValue, etalonLength));
		}
	}
	
	public String getMaxDeviation() {
		return this.maxDeviation;
	}
	
	public void setMaxDeviation(String maxDeviation) {
		if (this.maxDeviation == null || !this.maxDeviation.equals(maxDeviation)) {
			String oldValue = this.maxDeviation;
			this.maxDeviation = maxDeviation;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, OverallStatsWrapper.KEY_MAX_DEVIATION, oldValue, maxDeviation));
		}
	}
	
	public String getMeanDeviation() {
		return this.meanDeviation;
	}
	
	public void setMeanDeviation(String meanDeviation) {
		if (this.meanDeviation == null || !this.meanDeviation.equals(meanDeviation)) {
			String oldValue = this.meanDeviation;
			this.meanDeviation = meanDeviation;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, OverallStatsWrapper.KEY_MEAN_DEVIATION, oldValue, meanDeviation));
		}
	}

	public String getMismatch() {
		return this.mismatch;
	}

	public void setMismatch(String mismatch) {
		if (this.mismatch == null || !this.mismatch.equals(mismatch)) {
			String oldValue = this.mismatch;
			this.mismatch = mismatch;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, OverallStatsWrapper.KEY_MISMATCH, oldValue, mismatch));
		}
	}

	private void firePropertyChangeEvent(PropertyChangeEvent event) {
		if (this.propertyChangeListeners != null && !this.propertyChangeListeners.isEmpty()) {
			for (Iterator<PropertyChangeListener> iterator = this.propertyChangeListeners.iterator(); iterator.hasNext();) {
				PropertyChangeListener listener = iterator.next();
				listener.propertyChange(event);
			}
		}
	}
	
	public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		if (this.propertyChangeListeners == null) {
			this.propertyChangeListeners = new LinkedList<PropertyChangeListener>();
		}
		if (!this.propertyChangeListeners.contains(propertyChangeListener)) {
			this.propertyChangeListeners.add(propertyChangeListener);
		}
	}

	public synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		if (this.propertyChangeListeners != null && !this.propertyChangeListeners.contains(propertyChangeListener)) {
			this.propertyChangeListeners.remove(propertyChangeListener);
		}
	}

	public String getQualityD() {
		return this.qualityD;
	}

	public void setQualityD(String qualityD) {
		if (this.qualityD == null || !this.qualityD.equals(qualityD)) {
			String oldValue = this.qualityD;
			this.qualityD = qualityD;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, OverallStatsWrapper.KEY_QUALITY_D, oldValue, qualityD));
		}
	}

	public String getQualityQ() {
		return this.qualityQ;
	}

	public void setQualityQ(String qualityQ) {
		if (this.qualityQ == null || !this.qualityQ.equals(qualityQ)) {
			String oldValue = this.qualityQ;
			this.qualityQ = qualityQ;
			this.firePropertyChangeEvent(new PropertyChangeEvent(this, OverallStatsWrapper.KEY_QUALITY_Q, oldValue, qualityQ));
		}
	}
}
