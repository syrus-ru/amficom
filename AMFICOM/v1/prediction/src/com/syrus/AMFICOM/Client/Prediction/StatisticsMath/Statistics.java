/*-
 * $Id: Statistics.java,v 1.4 2006/03/22 10:52:30 stas Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Prediction.StatisticsMath;

import com.syrus.AMFICOM.analysis.dadara.Histogramm;

/**
 * Отображаемый временной ряд (по какому-то параметру какого-то события).
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
}
