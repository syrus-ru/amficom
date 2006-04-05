/*-
 * $Id: ReflectometryMeasurementSetup.java,v 1.2.2.3 2006/04/05 14:03:22 arseniy Exp $
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
 * надстройка над MeasurementSetup для естественного представления
 * параметров, специфичных для рефлектометрии
 * @author $Author: arseniy $
 * @author saa
 * @version $Revision: 1.2.2.3 $, $Date: 2006/04/05 14:03:22 $
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
	 * @return критерии анализа данного шаблона, may be null
	 */
	public ReflectometryAnalysisCriteria getAnalysisCriteria() {
		return this.analysisCriteria;
	}
}
