/*-
 * $Id: ReflectometryMeasurementSetup.java,v 1.2.2.5 2006/04/06 09:20:30 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.measurement.ActionTemplate;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.io.DataFormatException;

/**
 * Надстройка над MeasurementSetup для естественного представления параметров,
 * специфичных для рефлектометрии.
 * 
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.2.2.5 $, $Date: 2006/04/06 09:20:30 $
 * @module
 */
public class ReflectometryMeasurementSetup {
	private MeasurementSetup measurementSetup;
	private ReflectometryMeasurementParameters measurementParameters;
	private ReflectometryAnalysisCriteria analysisCriteria;

	public ReflectometryMeasurementSetup(final MeasurementSetup measurementSetup)
	throws DataFormatException, ApplicationException {
		this.measurementSetup = measurementSetup;

		this.measurementParameters = new ReflectometryMeasurementParametersImpl(this.measurementSetup.getMeasurementTemplate());

		final ActionTemplate<Analysis> analysisTemplate = this.measurementSetup.getAnalysisTemplate();
		this.analysisCriteria = analysisTemplate == null ? null : new ReflectometryAnalysisCriteria(analysisTemplate);
	}

	/**
	 * @return параметры теста для данного шаблона, not null
	 */
	public ReflectometryMeasurementParameters getMeasurementParameters() {
		return this.measurementParameters;
	}

	/**
	 * @return критерии анализа и эталон данного шаблона, may be null
	 */
	public ReflectometryAnalysisCriteria getAnalysisCriteria() {
		return this.analysisCriteria;
	}
}
