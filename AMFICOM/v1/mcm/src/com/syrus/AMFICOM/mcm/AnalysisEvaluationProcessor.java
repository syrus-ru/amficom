/*
 * $Id: AnalysisEvaluationProcessor.java,v 1.33 2005/06/16 10:54:57 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisDatabase;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.33 $, $Date: 2005/06/16 10:54:57 $
 * @author $Author: bass $
 * @module mcm_v1
 */

public class AnalysisEvaluationProcessor {
	private static final String CODENAME_ANALYSIS_TYPE_DADARA = "dadara";

	private static final String CLASS_NAME_ANALYSIS_MANAGER_DADARA = "DadaraAnalysisManager";

	private static final String ANALYSIS_NAME = "Analysis of measurement";

	private static AnalysisManager analysisManager;

	private AnalysisEvaluationProcessor() {
		//singleton
		assert false;
	}

	public static Result[] analyseEvaluate(Result measurementResult) throws AnalysisException {
		Measurement measurement = (Measurement) measurementResult.getAction();
		Test test = null;
		try {
			test = (Test) StorableObjectPool.getStorableObject(measurement.getTestId(), true);
		}
		catch (ApplicationException ae) {
			throw new AnalysisException("Cannot find test -- " + ae.getMessage(), ae);
		}
		Identifier monitoredElementId = test.getMonitoredElement().getId();
		MeasurementSetup measurementSetup = measurement.getSetup();

		Identifier analysisTypeId = test.getAnalysisTypeId();
		AnalysisType analysisType = null;
		try {
			if (analysisTypeId != null)
				analysisType = (AnalysisType) StorableObjectPool.getStorableObject(analysisTypeId, true);
		}
		catch (ApplicationException ae) {
			throw new AnalysisException("Cannot load analysis type '" + analysisTypeId
					+ "' for test '" + test.getId() + "' -- " + ae.getMessage(), ae);
		}
		if (analysisType != null) {
			Analysis analysis = createAnalysis(analysisType, monitoredElementId, measurement, measurementSetup.getCriteriaSet());
			return new Result[] { analyseAndEvaluate(measurementResult, analysis, measurementSetup.getEtalon()) };
		}
		return new Result[0];
	}

	private static Analysis createAnalysis(AnalysisType analysisType,
			Identifier monitoredElementId,
			Measurement measurement,
			ParameterSet criteriaSet) throws AnalysisException {
		if (criteriaSet == null)
			throw new AnalysisException("Criteria set is NULL");

		try {
			Analysis analysis = Analysis.createInstance(LoginManager.getUserId(),
					analysisType,
					monitoredElementId,
					measurement,
					ANALYSIS_NAME + " " + measurement.getId(),
					criteriaSet);
			((AnalysisDatabase) DatabaseContext.getDatabase(ObjectEntities.ANALYSIS_ENTITY_CODE)).insert(analysis);
			return analysis;
		}
		catch (ApplicationException ae) {
			throw new AnalysisException("Cannot create analysis", ae);
		}
	}

    // @todo: rename to loadAnalysisManager
	private static void loadAnalysisAndEvaluationManager(String analysisCodename,
			Result measurementResult,
			Analysis analysis,
			ParameterSet etalon) throws AnalysisException {
		String className = null;
		Constructor constructor = null;

		if (analysisCodename.equals(CODENAME_ANALYSIS_TYPE_DADARA))
			className = "com.syrus.AMFICOM.mcm." + CLASS_NAME_ANALYSIS_MANAGER_DADARA;
		else
			throw new AnalysisException("Cannot find analysis manager for analysis of codename '" + analysisCodename + "'");

		try {
			constructor = Class.forName(className).getDeclaredConstructor(new Class[] {Result.class,
					Analysis.class,
					ParameterSet.class});
			constructor.setAccessible(true);
			analysisManager = (AnalysisManager) constructor.newInstance(new Object[] {measurementResult, analysis, etalon});
		}
		catch (SecurityException e) {
			throw new AnalysisException("Cannot get constructor -- " + e.getMessage(), e);
		}
		catch (NoSuchMethodException e) {
			throw new AnalysisException("Cannot get constructor -- " + e.getMessage(), e);
		}
		catch (ClassNotFoundException e) {
			throw new AnalysisException("Cannot get constructor -- " + e.getMessage(), e);
		}
		catch (IllegalArgumentException e) {
			throw new AnalysisException("Cannot get constructor -- " + e.getMessage(), e);
		}
		catch (InstantiationException e) {
			throw new AnalysisException("Cannot get constructor -- " + e.getMessage(), e);
		}
		catch (IllegalAccessException e) {
			throw new AnalysisException("Cannot get constructor -- " + e.getMessage(), e);
		}
		catch (InvocationTargetException ite) {
			Throwable cause = ite.getCause();
			if (cause instanceof AnalysisException)
				throw (AnalysisException) cause;
			throw new AnalysisException(ite.getMessage(), ite);
		}
	}

	private static Result analyseAndEvaluate(Result measurementResult, Analysis analysis, ParameterSet etalon)
			throws AnalysisException {

		String analysisCodename = analysis.getType().getCodename();

		loadAnalysisAndEvaluationManager(analysisCodename, measurementResult, analysis, etalon);

		Parameter[] arParameters = analysisManager.analyse();
		Result analysisResult;
		try {
			analysisResult = analysis.createResult(LoginManager.getUserId(), arParameters);
		}
		catch (CreateObjectException coe) {
			Log.errorException(coe);
			analysisResult = null;
		}

		return analysisResult;
	}
}
