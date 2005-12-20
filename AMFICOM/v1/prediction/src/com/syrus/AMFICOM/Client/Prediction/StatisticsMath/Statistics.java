/*-
 * $Id: Statistics.java,v 1.2 2005/12/20 10:52:25 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Prediction.StatisticsMath;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.dadara.Histogramm;

/**
 * ������������ ��������� ��� (�� ������-�� ��������� ������-�� �������).
 * 
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/12/20 10:52:25 $
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
	 * "connector_db", "weld_db", "linear_db", "linear_db/km".
	 * @todo �������� �� "db" � "db/km".
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

	/**
	 * ��������� � Pool.
	 * (���� ����� ������� �� ��������� ����-������ �� ����� ������.)
	 */
	public void putIntoPool() {
		putDimensionIntoPool(getDimension());
		putHistoIntoPool(getHisto());
		putLinearCoeffsIntoPool(getLc());
		putTimeDependenceDataIntoPool(getTimeDependence());
	}

	private void putHistoIntoPool(Histogramm histo) {
		Pool.put("myHisto", "Histogramm", histo);
	}
	private void putTimeDependenceDataIntoPool(TimeDependenceData []tdd) {
		Pool.put("timeDependentDataId", "timeDependentDataId", tdd);
	}
	private void putLinearCoeffsIntoPool(LinearCoeffs linearCoeffs) {
		Pool.put("linearCoeffs", "MyLinearCoeffs", linearCoeffs);
	}
	private void putDimensionIntoPool(String dimension) {
		Pool.put("dimension", "dimension", dimension);
	}
}
