/*-
 * $Id: FilteredMTAEPredictionManager.java,v 1.1 2006/04/03 08:19:14 saa Exp $
 * 
 * Copyright � 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Prediction.StatisticsMath;

import java.util.List;

import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.MTAEPredictionManager.PredictionMtaeAndDate;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEvents;
import com.syrus.AMFICOM.measurement.MonitoredElement;

/**
 * ��������� ������� {@link MTAEPredictionManager} ������������
 * ��������, ����� �� ��� ����������� ������������� ������������
 * ��� ���������������, � ����� ���.
 * ������� ��������������� ������ ���������� ������ ����
 * ������� ����� ����������� ������������� ���������� ��� ���������������.
 * @todo ������ ���������, ���� ��� ����������.
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2006/04/03 08:19:14 $
 * @module prediction
 */
public class FilteredMTAEPredictionManager implements PredictionManager {

	/**
	 * ������� predictionManager ���� null, ���� ����� �������������
	 * ���������, � ������������ ��� �� ���������.
	 * (���������� ���-�� ���� ����)
	 */
	private PredictionManager internalManager = null;
	private boolean[] active;

	/**
	 * ������� ������������� � ��������� ����������� �������������
	 * � �������� ������������� ������������� "��� ������������".
	 * @param data �������� ������ ��� "��������������-�����"
	 * @param lowerTime ��������� ������ ���������� ���������
	 * @param upperTime �������� ������ ���������� ���������
	 * @param me MonitoredElement
	 * @throws IllegalArgumentException ������� ����� ��� ����
	 */
	public FilteredMTAEPredictionManager(List<PredictionMtaeAndDate> data,
			ModelTraceAndEvents base,
			long lowerTime,
			long upperTime,
			MonitoredElement me) {
		this.active = new boolean[data.size()];
		for(int i = 0; i < this.active.length; i++) {
			this.active[i] = true;
		}
		throw new UnsupportedOperationException(); // FIXME
	}

	/**
	 * �������������, ������������ �� ������ ��������������.
	 * @param i ����� �������������� � �������� ���������� ������ �������������
	 * @param active true, ����� ��������� ������������� ��������������,
	 * false, ����� ���������.
	 */
	public void setActive(int i, boolean active) {
		this.active[i] = active;
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getMinTime()
	 */
	public long getMinTime() {
		// @todo Auto-generated method stub
		return 0;
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getMaxTime()
	 */
	public long getMaxTime() {
		// @todo Auto-generated method stub
		return 0;
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getLowerTime()
	 */
	public long getLowerTime() {
		// @todo Auto-generated method stub
		return 0;
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getUpperTime()
	 */
	public long getUpperTime() {
		// @todo Auto-generated method stub
		return 0;
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getMonitoredElement()
	 */
	public MonitoredElement getMonitoredElement() {
		// @todo Auto-generated method stub
		return null;
	}

	/**
	 * @param nEvent
	 * @return
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#hasAttenuationInfo(int)
	 */
	public boolean hasAttenuationInfo(int nEvent) {
		// @todo Auto-generated method stub
		return false;
	}

	/**
	 * @param nEvent
	 * @return
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getAttenuationInfo(int)
	 */
	public Statistics getAttenuationInfo(int nEvent) {
		// @todo Auto-generated method stub
		return null;
	}

	/**
	 * @param nEvent
	 * @return
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#hasLossInfo(int)
	 */
	public boolean hasLossInfo(int nEvent) {
		// @todo Auto-generated method stub
		return false;
	}

	/**
	 * @param nEvent
	 * @return
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getLossInfo(int)
	 */
	public Statistics getLossInfo(int nEvent) {
		// @todo Auto-generated method stub
		return null;
	}

	/**
	 * @param nEvent
	 * @return
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#hasReflectiveAmplitudeInfo(int)
	 */
	public boolean hasReflectiveAmplitudeInfo(int nEvent) {
		// @todo Auto-generated method stub
		return false;
	}

	/**
	 * @param nEvent
	 * @return
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getReflectiveAmplitudeInfo(int)
	 */
	public Statistics getReflectiveAmplitudeInfo(int nEvent) {
		// @todo Auto-generated method stub
		return null;
	}

	/**
	 * @param nEvent
	 * @return
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#hasY0Info(int)
	 */
	public boolean hasY0Info(int nEvent) {
		// @todo Auto-generated method stub
		return false;
	}

	/**
	 * @param nEvent
	 * @return
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getY0Info(int)
	 */
	public Statistics getY0Info(int nEvent) {
		// @todo Auto-generated method stub
		return null;
	}

	/**
	 * @param nEvent
	 * @return
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#hasReflectanceInfo(int)
	 */
	public boolean hasReflectanceInfo(int nEvent) {
		// @todo Auto-generated method stub
		return false;
	}

	/**
	 * @param nEvent
	 * @return
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getReflectanceInfo(int)
	 */
	public Statistics getReflectanceInfo(int nEvent) {
		// @todo Auto-generated method stub
		return null;
	}

	/**
	 * @param date
	 * @return
	 * @see com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager#getPredictedReflectogram(long)
	 */
	public double[] getPredictedReflectogram(long date) {
		// @todo Auto-generated method stub
		return null;
	}

}
