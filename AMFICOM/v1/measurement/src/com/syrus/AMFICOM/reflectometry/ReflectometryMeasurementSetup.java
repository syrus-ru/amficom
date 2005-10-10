/*-
 * $Id: ReflectometryMeasurementSetup.java,v 1.2 2005/10/10 07:38:51 saa Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/10/10 07:38:51 $
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

		this.measurementParameters = new ReflectometryMeasurementParametersImpl(
				this.ms.getParameterSet());

		ParameterSet set;
		set = this.ms.getCriteriaSet();
		this.analysisCriteria = set != null
				? new ReflectometryAnalysisCriteria(set)
				: null;

		set = this.ms.getEtalon();
		this.etalon = set != null
				? new ReflectometryEtalonImpl(set)
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
