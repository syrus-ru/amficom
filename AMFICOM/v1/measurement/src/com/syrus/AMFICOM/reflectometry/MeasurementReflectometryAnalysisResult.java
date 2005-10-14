/*-
 * $Id: MeasurementReflectometryAnalysisResult.java,v 1.4 2005/10/14 13:45:08 bob Exp $
 * 
 * Copyright � 2005 Syrus Systems.
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
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.io.DataFormatException;
import com.syrus.util.ByteArray;

/**
 * ������������� ����������� �������, ��������� �� ������ measurement
 * (��� �������� Pool), � ���� ReflectometryAnalysisResult.
 * <p>
 * ������ ����� ������� ����������:<ul>
 * <li>ApplicationException - thrown by getStorableObjectsByCondition
 * <li>DataFormatException - ��� ������ �������������� double �� byte[]
 * </ul>
 * 
 * @author $Author: bob $
 * @author saa
 * @version $Revision: 1.4 $, $Date: 2005/10/14 13:45:08 $
 * @module measurement
 */
public final class MeasurementReflectometryAnalysisResult
implements ReflectometryAnalysisResult {
	private byte[] analysisResultBytes = null;
	private byte[] reflectogramMismatchBytes = null;
	private byte[] evaluationPerEventResultBytes = null;
	private ReflectometryEvaluationOverallResult evaluationOverall = null;

	/**
	 * ���� analysis ��� ������� measurement'�.
	 * ���� �������, ���������� ������ ��������� analysis.
	 * ���� �� �������, ���������� null.
	 * XXX: ��������, ����������� ����� ���� � � ������ �������, ������, AnalysisUtil � analysis
	 * XXX: ������ ������, �� ��������� reflectometry ����� ���� ������������� � ���, ��� analysis ��� measurement'� �� ����� ��� ����
	 * @param measurement Measurement
	 * @return Analysis or null
	 * @throws ApplicationException @see MeasurementReflectometryAnalysisResult
	 */
	public static Analysis getAnalysisForMeasurement(final Measurement measurement)
	throws ApplicationException {
		assert measurement != null : ErrorMessages.NON_NULL_EXPECTED;
		final Set<Analysis> analyse =
			StorableObjectPool.getStorableObjectsByCondition(
				new LinkedIdsCondition(measurement.getId(), ObjectEntities.ANALYSIS_CODE), true); // XXX: performance: the most slow part of loading trace that has no analysis
		return analyse.isEmpty() ? null : analyse.iterator().next();
	}

	
	
	/**
	 * ��������� �� ������� ���������.
	 * ���� ������� �� ����, �� ���������� ��������
	 * ����� ����� ��� �������� � �������� null, ���, ��������
	 * ��������� {@link ReflectometryAnalysisResult}, �������������
	 * ����, ��� �� ������, �� ��������� �� ���� ���������.
	 * @param measurement ���������
	 * @throws ApplicationException @see MeasurementReflectometryAnalysisResult
	 * @throws DataFormatException @see MeasurementReflectometryAnalysisResult
	 */
	public MeasurementReflectometryAnalysisResult(final Measurement measurement)
	throws ApplicationException, DataFormatException {
		this(getAnalysisForMeasurement(measurement), true);
	}

	/**
	 * ��������� �� ����������� ������� analysis.
	 * @param analysis ������ Analysis, �� ���������� �������� ���������,
	 *   not null
	 * @throws ApplicationException @see MeasurementReflectometryAnalysisResult
	 * @throws DataFormatException @see MeasurementReflectometryAnalysisResult
	 */
	public MeasurementReflectometryAnalysisResult(final Analysis analysis)
	throws ApplicationException, DataFormatException {
		this(analysis, false);
	}

	/**
	 * ��������� �� ����������� ������� analysis, � ���������� null.
	 * @param analysis ������ Analysis, �� ���������� �������� ���������.
	 * @param allowNull ��������� �� ����� null � �������� �������� analysis,
	 *   � ����� ������ ��� �������� ���� ����� null
	 *   (��������� "������ �� ����������")
	 * @throws ApplicationException @see MeasurementReflectometryAnalysisResult
	 * @throws DataFormatException @see MeasurementReflectometryAnalysisResult
	 */
	private MeasurementReflectometryAnalysisResult(final Analysis analysis,
			final boolean allowNull)
	throws ApplicationException, DataFormatException {
		if (!allowNull) {
			assert analysis != null : ErrorMessages.NON_NULL_EXPECTED;
		} else {
			if (analysis == null) {
				return; // leave null fields
			}
		}
		final LinkedIdsCondition condition = new LinkedIdsCondition(
				analysis.getId(), ObjectEntities.RESULT_CODE);
		final Set<Result> results =
			StorableObjectPool.getStorableObjectsByCondition(condition, true);
		boolean qualityHasQd = false;
		double qualityQ = 0.0;
		double qualityD = 0.0;
		for (final Result result1 : results) {
			for (final Parameter parameter : result1.getParameters()) {
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
		// ���� EvaluationPerEventResultBytes ������������, ������,
		// ��������� �����������
		if (this.evaluationPerEventResultBytes != null) {
			if (qualityHasQd) { // ��������� Q � d ������������
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
			} else { // ��������� ���� ���������, �� ���������� Q � d ���
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
