/*-
 * $Id: Statistics.java,v 1.4 2006/03/22 10:52:30 stas Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Prediction.StatisticsMath;

import com.syrus.AMFICOM.analysis.dadara.Histogramm;

/**
 * ������������ ��������� ��� (�� ������-�� ��������� ������-�� �������).
 * 
 * @author saa
 * @author $Author: stas $
 * @version $Revision: 1.4 $, $Date: 2006/03/22 10:52:30 $
 * @module prediction
 */
public class Statistics {
	private TimeDependenceData[] timeDependence;
	private String dimension;
	private Histogramm histo;
	private LinearCoeffs lc;

	public Statistics(TimeDependenceData[] timeDependence,
			String dimension, Histogramm histo, LinearCoeffs lc) {
		this.timeDependence = timeDependence;
		this.dimension = dimension;
		this.histo = histo;
		this.lc = lc;
	}

	/**
	 * �����������, String.
	 * ��������� ��������:
	 * "connector_db", "weld_db", "linear_db", "linear_db/km", "db", "db/km"
	 * @todo �������� ������ "db" � "db/km".
	 * @return �����������, String
	 */
	public String getDimension() {
		return this.dimension;
	}

	/**
	 * ����������� ������������� ��������.
	 * � ��������� ����� �� ������������.
	 * @return Histogramm
	 */
	public Histogramm getHisto() {
		return this.histo;
	}

	/**
	 * �������� �������������.
	 * @return ��������� �������� �������������
	 */
	public LinearCoeffs getLc() {
		return this.lc;
	}

	/**
	 * ���������� ��� ��������� �����������.
	 * ������(?) ���� ������������ �� ����������� �������.
	 * @return ��� ��������� �����������
	 */
	public TimeDependenceData[] getTimeDependence() {
		return this.timeDependence;
	}
}
