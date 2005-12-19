/*-
 * $Id: Statistics.java,v 1.1 2005/12/19 14:53:48 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Prediction.StatisticsMath;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.dadara.Histogramm;

public class Statistics {
	private TimeDependenceData[] timeDependence;
	private String dimension;
	private Histogramm histo;
	private LinearCoeffs lc;

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

	public void putIntoPool() {
		putDimensionIntoPool(getDimension());
		putHistoIntoPool(getHisto());
		putLinearCoeffsIntoPool(getLc());
		putTimeDependenceDataIntoPool(getTimeDependence());
	}
	
	public Statistics(TimeDependenceData[] timeDependence,
			String dimension, Histogramm histo, LinearCoeffs lc) {
		this.timeDependence = timeDependence;
		this.dimension = dimension;
		this.histo = histo;
		this.lc = lc;
	}
	public String getDimension() {
		return this.dimension;
	}
	public Histogramm getHisto() {
		return this.histo;
	}
	public LinearCoeffs getLc() {
		return this.lc;
	}
	public TimeDependenceData[] getTimeDependence() {
		return this.timeDependence;
	}
}
