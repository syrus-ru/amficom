/*
 * $Id: AnalysisEvaluationProcessor.java,v 1.59 2006/03/17 15:26:29 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import static com.syrus.AMFICOM.general.ParameterType.DADARA_ALARMS;
import static com.syrus.AMFICOM.general.ParameterType.DADARA_QUALITY_OVERALL_Q;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatchImpl;
import com.syrus.AMFICOM.eventv2.DefaultReflectogramMismatchEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch;
import com.syrus.io.DataFormatException;
import com.syrus.util.ByteArray;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.59 $, $Date: 2006/03/17 15:26:29 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */

final class AnalysisEvaluationProcessor {
	private static final String CODENAME_ANALYSIS_TYPE_DADARA = "dadara";

	private static final String CLASS_NAME_ANALYSIS_MANAGER_DADARA = "DadaraAnalysisManager";

	private static final String ANALYSIS_NAME = "Analysis of measurement";

	private static AnalysisManager analysisManager;

	private static Result[] EMPTY_RESULTS = new Result[0];

	private AnalysisEvaluationProcessor() {
		//singleton
		assert false;
	}

	public static AnalysisResult analyseEvaluate(final Result measurementResult) throws AnalysisException {
		final Measurement measurement = (Measurement) measurementResult.getAction();
		final Test test;
		try {
			test = (Test) StorableObjectPool.getStorableObject(measurement.getTestId(), true);
		} catch (ApplicationException ae) {
			throw new AnalysisException("Cannot find test -- " + ae.getMessage(), ae);
		}
		final Identifier monitoredElementId = test.getMonitoredElement().getId();
		final MeasurementSetup measurementSetup = measurement.getSetup();

		final AnalysisType analysisType = test.getAnalysisType();
		if (!analysisType.equals(AnalysisType.UNKNOWN)) {
			final Analysis analysis = createAnalysis(analysisType, monitoredElementId, measurement.getId(), measurementSetup.getCriteriaSet());
			return analyseAndEvaluate(measurementResult, analysis, measurementSetup.getEtalon());
		}

		Log.debugMessage("UNKNOWN AnalysisType for test '" + test.getId() + "'", SEVERE);
		return new AnalysisResult(EMPTY_RESULTS);
	}

	private static Analysis createAnalysis(final AnalysisType analysisType,
			final Identifier monitoredElementId,
			final Identifier measurementId,
			final ParameterSet criteriaSet) throws AnalysisException {
		if (criteriaSet == null) {
			throw new AnalysisException("Criteria set is NULL");
		}

		try {
			final Analysis analysis = Analysis.createInstance(LoginManager.getUserId(),
					analysisType,
					monitoredElementId,
					measurementId,
					ANALYSIS_NAME + " " + measurementId,
					criteriaSet);
			StorableObjectPool.flush(analysis, LoginManager.getUserId(), false);
			return analysis;
		} catch (ApplicationException ae) {
			throw new AnalysisException("Cannot create analysis", ae);
		}
	}

    // @todo: rename to loadAnalysisManager
	private static void loadAnalysisAndEvaluationManager(final String analysisCodename,
			final Result measurementResult,
			final Analysis analysis,
			final ParameterSet etalon) throws AnalysisException {
		String className = null;
		Constructor<?> constructor = null;

		if (analysisCodename.equals(CODENAME_ANALYSIS_TYPE_DADARA)) {
			className = "com.syrus.AMFICOM.mcm." + CLASS_NAME_ANALYSIS_MANAGER_DADARA;
		} else {
			throw new AnalysisException("Cannot find analysis manager for analysis of codename '" + analysisCodename + "'");
		}

		try {
			constructor = Class.forName(className).getDeclaredConstructor(new Class[] { Result.class,
					Analysis.class,
					ParameterSet.class });
			constructor.setAccessible(true);
			analysisManager = (AnalysisManager) constructor.newInstance(new Object[] { measurementResult, analysis, etalon });
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

	private static AnalysisResult analyseAndEvaluate(final Result measurementResult,
			final Analysis analysis,
			final ParameterSet etalon)
	throws AnalysisException {
		try {
			loadAnalysisAndEvaluationManager(
					analysis.getType().getCodename(),
					measurementResult, analysis, etalon);
	
			final Parameter[] arParameters = analysisManager.analyse();
			final Identifier resultId = measurementResult.getId();
			final Identifier monitoredElementId = measurementResult.getAction().getMonitoredElementId();

			double q = Double.NaN;
			int dadaraAlarmsOccurenceCount = 0;
			for (final Parameter parameter : arParameters) {
				if (parameter.getType() == DADARA_QUALITY_OVERALL_Q) {
					try {
						q = (new ByteArray(parameter.getValue())).toDouble();
					} catch (IOException ioe) {
						Log.errorMessage(ioe);
					}
				} else if (parameter.getType() == DADARA_ALARMS) {
					if (++dadaraAlarmsOccurenceCount != 1) {
						Log.debugMessage("WARNING: dadaraAlarmsOccurenceCount = "
								+ dadaraAlarmsOccurenceCount
								+ "; should be 1", WARNING);
					}
					for (final ReflectogramMismatch reflectogramMismatch : ReflectogramMismatchImpl.alarmsFromByteArray(parameter.getValue())) {
						MeasurementControlModule.eventQueue.addEvent(
								DefaultReflectogramMismatchEvent.valueOf(
										reflectogramMismatch,
										resultId,
										monitoredElementId));
					}

				} else {
					continue;
				}
			}

			final Result result = analysis.createResult(LoginManager.getUserId(), arParameters);
			final Result[] results = new Result[] { result };

			final AnalysisResult analysisResult;
			if (!Double.isNaN(q)) {
				analysisResult = new AnalysisResult(results, q);
			} else {
				analysisResult = new AnalysisResult(results);
			}

			return analysisResult;
		} catch (final EventQueueFullException eqfe) {
			Log.debugMessage(eqfe, SEVERE);
			throw new AnalysisException(eqfe);
		} catch (final DataFormatException dfe) {
			Log.debugMessage(dfe, SEVERE);
			throw new AnalysisException(dfe);
		} catch (final CreateObjectException coe) {
			Log.debugMessage(coe, SEVERE);
			throw new AnalysisException(coe);
		}
	}
}
