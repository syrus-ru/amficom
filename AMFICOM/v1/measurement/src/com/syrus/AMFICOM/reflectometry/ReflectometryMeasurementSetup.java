/*-
 * $Id: ReflectometryMeasurementSetup.java,v 1.1 2005/09/01 16:48:31 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.io.DataFormatException;

/**
 * надстройка над MeasurementSetup для естественного представления
 * параметров, специфичных для рефлектометрии
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.1 $, $Date: 2005/09/01 16:48:31 $
 * @module
 */
public class ReflectometryMeasurementSetup {
	private MeasurementSetup ms;
	private ReflectometryMeasurementParameters measurementParameters;
	private ReflectometryAnalysisCriteria analysisCriteria;
	private ReflectometryEtalon etalon;

	public ReflectometryMeasurementSetup(MeasurementSetup ms)
	throws DataFormatException {
		this.ms = ms;

		this.measurementParameters = new ReflectometryMeasurementParameters(
				this.ms.getParameterSet());

		ParameterSet set;
		set = this.ms.getCriteriaSet();
		this.analysisCriteria = set != null
				? new ReflectometryAnalysisCriteria(set)
				: null;

		set = this.ms.getEtalon();
		this.etalon = set != null
				? new ReflectometryEtalon(set)
				: null;
	}

	/**
	 * @return параметры теста для данного шаблона, not null
	 */
	public ReflectometryMeasurementParameters getMeasurementParameters() {
		return this.measurementParameters;
	}

	/**
	 * @return критерии анализа данного шаблона, may be null
	 */
	public ReflectometryAnalysisCriteria getAnalysisCriteria() {
		return this.analysisCriteria;
	}

	/**
	 * @return эталон данного шаблона, may be null
	 */
	public ReflectometryEtalon getEtalon() {
		return this.etalon;
	}
}
