/*-
 * $Id: MeasurementReflectometryAnalysisResult.java,v 1.1 2005/10/14 11:19:45 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

import java.io.IOException;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.io.DataFormatException;
import com.syrus.util.ByteArray;

/**
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.1 $, $Date: 2005/10/14 11:19:45 $
 * @module measurement
 */
public class MeasurementReflectometryAnalysisResult
implements ReflectometryAnalysisResult {
	private byte[] analysisResultBytes = null;
	private byte[] reflectogramMismatchBytes = null;
	private byte[] evaluationPerEventResultBytes = null;
	private ReflectometryEvaluationOverallResult evaluationOverall = null;

	/**
	 * Ищет analysis для данного measurement'а.
	 * Если находит, возвращает первый найденный analysis.
	 * Если не находит, возвращает null.
	 * XXX: вероятно, аналогичный метод есть и в других классах, скажем, AnalysisUtil в analysis
	 * XXX: вообще говоря, из специфики reflectometry здесь лишь предположение о том, что analysis для measurement'а не более чем один
	 * @param m Measurement
	 * @return Analysis or null
	 * @throws ApplicationException thrown by getStorableObjectsByCondition
	 */
	public static Analysis getAnalysisForMeasurement(Measurement m)
	throws ApplicationException {
		assert m != null;
		LinkedIdsCondition condition1 =
			new LinkedIdsCondition(m.getId(), ObjectEntities.ANALYSIS_CODE);
		Set<Analysis> analyse =
			StorableObjectPool.getStorableObjectsByCondition(condition1, true); // XXX: performance: the most slow part of loading trace that has no analysis
		return analyse.isEmpty() ? null : analyse.iterator().next();
	}

	/**
	 * Создается по результатам данного analysis.
	 * @param analysis объект Analysis, по параметрам которого создаемся.
	 *   Может быть null, в таком случае все свойства тоже будут null
	 *   (состояние "анализ не проводился")
	 * @throws ApplicationException thrown by getStorableObjectsByCondition
	 * @throws DataFormatException при ошибке восстановления double из byte[]
	 */
	public MeasurementReflectometryAnalysisResult(Analysis analysis)
	throws ApplicationException, DataFormatException {
		if (analysis == null) {
			return; // leave null fields
		}
		LinkedIdsCondition condition = new LinkedIdsCondition(
				analysis.getId(), ObjectEntities.RESULT_CODE);
		Set<Result> results = StorableObjectPool.getStorableObjectsByCondition(
				condition, true);
		boolean qualityHasQd = false;
		double qualityQ = 0.0;
		double qualityD = 0.0;
		for (Result result1 : results) {
			for (Parameter parameter : result1.getParameters()) {
				if (parameter.getType().equals(ParameterType.DADARA_ANALYSIS_RESULT)) {
					this.analysisResultBytes = parameter.getValue().clone();
				}
				if (parameter.getType().equals(ParameterType.DADARA_ALARMS)) {
					this.reflectogramMismatchBytes = parameter.getValue().clone();
				}
				if (parameter.getType().equals(ParameterType.DADARA_QUALITY_PER_EVENT)) {
					this.evaluationPerEventResultBytes = parameter.getValue().clone();
				}
				if (parameter.getType().equals(ParameterType.DADARA_QUALITY_OVERALL_D)) {
					qualityHasQd = true;
					try {
						qualityD = new ByteArray(parameter.getValue()).toDouble();
					} catch (IOException e) {
						throw new DataFormatException();
					}
				}
				if (parameter.getType().equals(ParameterType.DADARA_QUALITY_OVERALL_Q)) {
					qualityHasQd = true;
					try {
						qualityQ = new ByteArray(parameter.getValue()).toDouble();
					} catch (IOException e) {
						throw new DataFormatException();
					}
				}
			}
		}
		// если EvaluationPerEventResultBytes присутствует, значит,
		// сравнение проводилось
		if (this.evaluationPerEventResultBytes != null) {
			if (qualityHasQd) { // параметры Q и d присутствуют
				final double q = qualityQ;
				final double d = qualityD;
				this.evaluationOverall =
						new ReflectometryEvaluationOverallResult() {
					public boolean hasDQ() {
						return true;
					}
					public double getD() {
						return d;
					}
					public double getQ() {
						return q;
					}
				};
			} else { // сравнение было проведено, но параметров Q и d нет
				this.evaluationOverall =
						new ReflectometryEvaluationOverallResult() {
					public boolean hasDQ() {
						return false;
					}
					public double getD() {
						throw new IllegalStateException();
					}
					public double getQ() {
						throw new IllegalStateException();
					}
				};
			}
		}
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult#getDadaraAnalysisResultBytes()
	 */
	public byte[] getDadaraAnalysisResultBytes() {
		return this.analysisResultBytes.clone();
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult#getDadaraReflectogramMismatchBytes()
	 */
	public byte[] getDadaraReflectogramMismatchBytes() {
		return this.reflectogramMismatchBytes.clone();
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult#getDadaraEvaluationPerEventResultBytes()
	 */
	public byte[] getDadaraEvaluationPerEventResultBytes() {
		return this.evaluationPerEventResultBytes.clone();
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult#getReflectometryEvaluationOverallResult()
	 */
	public ReflectometryEvaluationOverallResult getReflectometryEvaluationOverallResult() {
		return this.evaluationOverall; // need not clone, it is immutable
	}
}
