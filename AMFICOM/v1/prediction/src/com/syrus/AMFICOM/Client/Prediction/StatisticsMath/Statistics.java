/*-
 * $Id: Statistics.java,v 1.3 2005/12/20 15:41:10 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Prediction.StatisticsMath;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.dadara.Histogramm;

/**
 * Отображаемый временной ряд (по какому-то параметру какого-то события).
 * 
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.3 $, $Date: 2005/12/20 15:41:10 $
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
	 * Размерность, String.
	 * Возможные значения:
	 * "connector_db", "weld_db", "linear_db", "linear_db/km", "db", "db/km"
	 * @todo оставить только "db" и "db/km".
	 * @return размерность, String
	 */
	public String getDimension() {
		return this.dimension;
	}

	/**
	 * Гистограмма распределения значений.
	 * В настоящее время не используется.
	 * @return Histogramm
	 */
	public Histogramm getHisto() {
		return this.histo;
	}

	/**
	 * Линейная аппроксимация.
	 * @return результат линейной аппроксимации
	 */
	public LinearCoeffs getLc() {
		return this.lc;
	}

	/**
	 * Собственно ряд временной зависимости.
	 * Должен(?) быть отсортирован по возрастанию времени.
	 * @return ряд временной зависимости
	 */
	public TimeDependenceData[] getTimeDependence() {
		return this.timeDependence;
	}

	/**
	 * Сохранить в Pool.
	 * (Этот метод неплохо бы перенести куда-нибудь из этого класса.)
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
