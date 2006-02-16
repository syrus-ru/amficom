/*-
 * $Id: MeasurementReflectometryAnalysisResult.java,v 1.5.2.1 2006/02/16 12:45:58 arseniy Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

import java.io.IOException;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisResultParameter;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.io.DataFormatException;
import com.syrus.util.ByteArray;

/**
 * Представление результатов анализа, доступных из модуля measurement
 * (как объектов Pool), в виде ReflectometryAnalysisResult.
 * <p>
 * Методы могут бросать исключения:<ul>
 * <li>ApplicationException - thrown by getStorableObjectsByCondition
 * <li>DataFormatException - при ошибке восстановления double из byte[]
 * </ul>
 * 
 * @author $Author: arseniy $
 * @author saa
 * @version $Revision: 1.5.2.1 $, $Date: 2006/02/16 12:45:58 $
 * @module measurement
 */
public final class MeasurementReflectometryAnalysisResult
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
	 * @param measurement Measurement
	 * @return Analysis or null
	 * @throws ApplicationException @see MeasurementReflectometryAnalysisResult
	 */
	public static Analysis getAnalysisForMeasurement(
			final Measurement measurement)
	throws ApplicationException {
		assert measurement != null : ErrorMessages.NON_NULL_EXPECTED;
		final Set<Analysis> analyse =
			StorableObjectPool.getStorableObjectsByCondition(
				new LinkedIdsCondition(measurement.getId(), ObjectEntities.ANALYSIS_CODE), true); // XXX: performance: the most slow part of loading trace that has no analysis
		return analyse.isEmpty() ? null : analyse.iterator().next();
	}

	
	
	/**
	 * Создается по данному измерению.
	 * Если анализа не было, то получаемое значение
	 * будет иметь все свойства в значении null, что, согласно
	 * контракту {@link ReflectometryAnalysisResult}, соответствует
	 * тому, что ни анализ, ни сравнение не было проведено.
	 * @param measurement измерение
	 * @throws ApplicationException @see MeasurementReflectometryAnalysisResult
	 * @throws DataFormatException @see MeasurementReflectometryAnalysisResult
	 */
	public MeasurementReflectometryAnalysisResult(final Measurement measurement)
	throws ApplicationException, DataFormatException {
		this(getAnalysisForMeasurement(measurement), true);
	}

	/**
	 * Создается по результатам данного analysis.
	 * @param analysis объект Analysis, по параметрам которого создаемся,
	 *   not null
	 * @throws ApplicationException @see MeasurementReflectometryAnalysisResult
	 * @throws DataFormatException @see MeasurementReflectometryAnalysisResult
	 */
	public MeasurementReflectometryAnalysisResult(final Analysis analysis)
	throws ApplicationException, DataFormatException {
		this(analysis, false);
	}

	/**
	 * Создается по результатам данного analysis, с поддержкой null.
	 * @param analysis объект Analysis, по параметрам которого создаемся.
	 * @param allowNull разрешить на входе null в качестве значения analysis,
	 *   в таком случае все свойства тоже будут null
	 *   (состояние "анализ не проводился")
	 * @throws ApplicationException @see MeasurementReflectometryAnalysisResult
	 * @throws DataFormatException @see MeasurementReflectometryAnalysisResult
	 */
	private MeasurementReflectometryAnalysisResult(final Analysis analysis, final boolean allowNull)
			throws ApplicationException,
				DataFormatException {
		if (!allowNull) {
			assert analysis != null : ErrorMessages.NON_NULL_EXPECTED;
		} else {
			if (analysis == null) {
				return; // leave null fields
			}
		}
		final LinkedIdsCondition condition = new LinkedIdsCondition(analysis.getId(), ObjectEntities.ANALYSISRESULTPARAMETER_CODE);
		final Set<AnalysisResultParameter> results = StorableObjectPool.getStorableObjectsByCondition(condition, true);
		boolean qualityHasQd = false;
		double qualityQ = 0.0;
		double qualityD = 0.0;
		for (final AnalysisResultParameter parameter : results) {
			if (parameter.getTypeCodename().equals(ParameterTypeCodename.DADARA_ANALYSIS_RESULT.stringValue())) {
				this.analysisResultBytes = parameter.getValue();//XXX removed clone()
			} else if (parameter.getTypeCodename().equals(ParameterTypeCodename.DADARA_ALARMS.stringValue())) {//XXX added else
				this.reflectogramMismatchBytes = parameter.getValue();//XXX removed clone()
			} else if (parameter.getTypeCodename().equals(ParameterTypeCodename.DADARA_QUALITY_PER_EVENT)) {//XXX added else
				this.evaluationPerEventResultBytes = parameter.getValue();//XXX removed clone()
			} else if (parameter.getTypeCodename().equals(ParameterTypeCodename.DADARA_QUALITY_OVERALL_D.stringValue())) {//XXX added else
				qualityHasQd = true;
				try {
					qualityD = new ByteArray(parameter.getValue()).toDouble();
				} catch (IOException e) {
					throw new DataFormatException();
				}
			} else if (parameter.getTypeCodename().equals(ParameterTypeCodename.DADARA_QUALITY_OVERALL_Q)) {//XXX added else
				qualityHasQd = true;
				try {
					qualityQ = new ByteArray(parameter.getValue()).toDouble();
				} catch (IOException e) {
					throw new DataFormatException();
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
		return this.analysisResultBytes == null
				? null
				: this.analysisResultBytes.clone();
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult#getDadaraReflectogramMismatchBytes()
	 */
	public byte[] getDadaraReflectogramMismatchBytes() {
		return this.reflectogramMismatchBytes == null
				? null
				: this.reflectogramMismatchBytes.clone();
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult#getDadaraEvaluationPerEventResultBytes()
	 */
	public byte[] getDadaraEvaluationPerEventResultBytes() {
		return this.evaluationPerEventResultBytes == null
				? null
				: this.evaluationPerEventResultBytes.clone();
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult#getReflectometryEvaluationOverallResult()
	 */
	public ReflectometryEvaluationOverallResult
	getReflectometryEvaluationOverallResult() {
		return this.evaluationOverall; // need not clone, it is immutable
	}
}
