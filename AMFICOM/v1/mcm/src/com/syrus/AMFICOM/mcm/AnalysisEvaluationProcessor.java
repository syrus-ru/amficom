/*
 * $Id: AnalysisEvaluationProcessor.java,v 1.57.2.5 2006/03/29 09:22:31 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import static com.syrus.AMFICOM.measurement.AnalysisTypeCodename.DADARA;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.DADARA_ALARMS;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatchImpl;
import com.syrus.AMFICOM.eventv2.DefaultReflectogramMismatchEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisResultParameter;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementResultParameter;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch;
import com.syrus.io.DataFormatException;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.57.2.5 $, $Date: 2006/03/29 09:22:31 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */

final class AnalysisEvaluationProcessor {
	private static final String CLASS_NAME_ANALYSIS_MANAGER_DADARA = "DadaraAnalysisManager";
	private static final String ANALYSIS_NAME = "Analysis of measurement";

	private static AnalysisManager analysisManager;

	private AnalysisEvaluationProcessor() {
		//singleton
		assert false;
	}

	public static Set<AnalysisResultParameter> analyseEvaluate(final MeasurementResultParameter measurementResultParameter) throws AnalysisException {
		final Identifier measurementId = measurementResultParameter.getMeasurementId();
		try {
			final Measurement measurement = measurementResultParameter.getAction();

			final Test test = measurement.getTest();

			final Identifier analysisTypeId = test.getAnalysisTypeId();
			if (analysisTypeId.isVoid()) {
				return Collections.emptySet();
			}

			final Identifier analysisTemplateId = test.getCurrentMeasurementSetup().getAnalysisTemplateId();

			final Analysis analysis = Analysis.createInstance(LoginManager.getUserId(),
					analysisTypeId,
					test.getMonitoredElementId(),
					analysisTemplateId,
					ANALYSIS_NAME + " " + measurementId,
					new Date(),
					0L,
					measurementId);
			StorableObjectPool.flush(analysis, LoginManager.getUserId(), false);

			return analyseAndEvaluate(measurementResultParameter, analysis);
		} catch (ApplicationException ae) {
				throw new AnalysisException("Cannot process analysis for measurement '" + measurementId + "'", ae);
		}
	}

	private static void loadAnalysisManager(final String analysisCodename,
			final MeasurementResultParameter measurementResultParameter,
			final Analysis analysis) throws AnalysisException {
		String className = null;
		Constructor<?> constructor = null;

		if (analysisCodename.equals(DADARA.stringValue())) {
			className = "com.syrus.AMFICOM.mcm." + CLASS_NAME_ANALYSIS_MANAGER_DADARA;
		} else {
			throw new AnalysisException("Cannot find analysis manager for analysis of codename '" + analysisCodename + "'");
		}

		try {
			constructor = Class.forName(className).getDeclaredConstructor(new Class[] { MeasurementResultParameter.class,
					Analysis.class });
			constructor.setAccessible(true);
			analysisManager = (AnalysisManager) constructor.newInstance(new Object[] { measurementResultParameter, analysis });
		} catch (SecurityException e) {
			throw new AnalysisException("Cannot get constructor -- " + e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			throw new AnalysisException("Cannot get constructor -- " + e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			throw new AnalysisException("Cannot get constructor -- " + e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new AnalysisException("Cannot get constructor -- " + e.getMessage(), e);
		} catch (InstantiationException e) {
			throw new AnalysisException("Cannot get constructor -- " + e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new AnalysisException("Cannot get constructor -- " + e.getMessage(), e);
		} catch (InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AnalysisException) {
				throw (AnalysisException) cause;
			}
			throw new AnalysisException(ite.getMessage(), ite);
		}
	}

	/**
	 * @todo Move DADARA-specific code to DadaraAnalysisManager
	 * @param measurementResultParameter
	 * @param analysis
	 * @return
	 * @throws AnalysisException
	 */
	private static Set<AnalysisResultParameter> analyseAndEvaluate(final MeasurementResultParameter measurementResultParameter,
			final Analysis analysis) throws AnalysisException {
		try {
			loadAnalysisManager(analysis.getTypeCodename(), measurementResultParameter, analysis);

			final Set<AnalysisResultParameter> analysisResultParameters = analysisManager.analyse();
			final Identifier resultId = measurementResultParameter.getId();
			final Identifier monitoredElementId = measurementResultParameter.getAction().getMonitoredElementId();

			int dadaraAlarmsOccurenceCount = 0;
			for (final AnalysisResultParameter analysisResultParameter : analysisResultParameters) {
				if (!analysisResultParameter.getTypeCodename().equals(DADARA_ALARMS.stringValue())) {
					continue;
				}

				if (++dadaraAlarmsOccurenceCount != 1) {
					Log.debugMessage("WARNING: dadaraAlarmsOccurenceCount = " + dadaraAlarmsOccurenceCount + "; should be 1",
							WARNING);
				}

				for (final ReflectogramMismatch reflectogramMismatch : ReflectogramMismatchImpl.alarmsFromByteArray(analysisResultParameter.getValue())) {
					MeasurementControlModule.getInstance().addEventToQueue(DefaultReflectogramMismatchEvent.newInstance(LoginManager.getUserId(),
							reflectogramMismatch,
							resultId,
							monitoredElementId));
				}
			}

			return analysisResultParameters;
		} catch (final DataFormatException dfe) {
			Log.debugMessage(dfe, SEVERE);
			throw new AnalysisException(dfe);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			throw new AnalysisException(ae);
		}
	}
}
