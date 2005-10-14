/*-
 * $Id: ReflectometryAnalysisResultImpl.java,v 1.2 2005/10/14 08:07:52 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.analysis.EtalonComparison;
import com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult;
import com.syrus.AMFICOM.reflectometry.ReflectometryEvaluationOverallResult;
import com.syrus.io.DataFormatException;

/**
 * ������������� ��������������� ����������� ������� � ���������
 * ����� ReflectometryAnalysisResult � ��������� �������������� dadara.
 * ������������ ������ ������ ������ - � �� �����, � �� ������.
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.2 $, $Date: 2005/10/14 08:07:52 $
 * @module dadara
 */
public class ReflectometryAnalysisResultImpl implements
		ReflectometryAnalysisResult {

	private AnalysisResult ar; // not null
	private ReflectogramMismatchImpl[] rma; // may be null
	private EvaluationPerEventResultImpl epe; // may be null
	private ReflectometryEvaluationOverallResultImpl eOverall; // may be null

	/**
	 * ��������� �� ������ ����������� ������� � ��������� dadara
	 * ������� ����� ������� ������.
	 * ������� ��������� �� ������ ���� null.
	 */
	public ReflectometryAnalysisResultImpl(AnalysisResult ar,
			EtalonComparison ec) {
		assert ar != null;
		this.ar = new AnalysisResult(ar);

		final List<ReflectogramMismatchImpl> alarms = ec.getAlarms();
		this.rma = alarms.toArray(new ReflectogramMismatchImpl[alarms.size()]);
		// ���� ������������ ������, �.�. ��� modifiable
		for (int i = 0; i < this.rma.length; i++) {
			this.rma[i] = this.rma[i].copy();
		}

		this.epe = new EvaluationPerEventResultImpl(ec.getPerEventResult());

		this.eOverall = new ReflectometryEvaluationOverallResultImpl(
				ec.getOverallResult());
	}

	/**
	 * ���������(�����������) ������ �������
	 * {@link ReflectometryAnalysisResult}
	 * (copy-�����������)
	 * @param that ���������� {@link ReflectometryAnalysisResult}
	 * @throws DataFormatException 
	 */
	public ReflectometryAnalysisResultImpl(ReflectometryAnalysisResult that)
	throws DataFormatException {
		// ������������� AR
		this.ar = (AnalysisResult)
				DataStreamableUtil.readDataStreamableFromBA(
						that.getDadaraAnalysisResultBytes(),
						AnalysisResult.getDSReader());
		// ������������� RM
		this.rma = ReflectogramMismatchImpl.alarmsFromByteArray(
			that.getDadaraReflectogramMismatchBytes());
		// ������������� PE
		this.epe = (EvaluationPerEventResultImpl)
				DataStreamableUtil.readDataStreamableFromBA(
						that.getDadaraEvaluationPerEventResultBytes(),
						EvaluationPerEventResultImpl.getDSReader());
		// ������ ����� Overall
		this.eOverall = new ReflectometryEvaluationOverallResultImpl(
			that.getReflectometryEvaluationOverallResult());
	}

	/**
	 * ���������� ����� ������ AnalysisResult
	 * @return ����� ������ AnalysisResult
	 */
	public AnalysisResult getDadaraAnalysisResult() {
		return new AnalysisResult(this.ar); // ������ �����
	}

	/**
	 * ���������� ����� ������ EtalonComparison
	 */
	public EtalonComparison getDadaraEtalonComparison() {
		// �������� �������� ������� rma � ������, �������� �� ��� ����
		final ArrayList<ReflectogramMismatchImpl> rmArray =
			new ArrayList<ReflectogramMismatchImpl>(this.rma.length);
		for (int i = 0; i < this.rma.length; i++) {
			rmArray.add(this.rma[i].copy());
		}
		// ��������� ���� - immutable
		final ReflectometryEvaluationOverallResult overall = this.eOverall;
		final EvaluationPerEventResultImpl perEvent = this.epe;
		// ������� EvalonComparison
		return new EtalonComparison() {
			public List<ReflectogramMismatchImpl> getAlarms() {
				return rmArray;
			}
			public ReflectometryEvaluationOverallResult getOverallResult() {
				return overall;
			}
			public EvaluationPerEventResult getPerEventResult() {
				return perEvent;
			}
		};
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult#getDadaraAnalysisResultBytes()
	 */
	public byte[] getDadaraAnalysisResultBytes() {
		return this.ar.toByteArray();
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult#getDadaraReflectogramMismatchBytes()
	 */
	public byte[] getDadaraReflectogramMismatchBytes() {
		return ReflectogramMismatchImpl.alarmsToByteArray(this.rma);
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult#getDadaraEvaluationPerEventResultBytes()
	 */
	public byte[] getDadaraEvaluationPerEventResultBytes() {
		return DataStreamableUtil.writeDataStreamableToBA(this.epe);
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult#getReflectometryEvaluationOverallResult()
	 */
	public ReflectometryEvaluationOverallResult
	getReflectometryEvaluationOverallResult() {
		return this.eOverall; // ��� unmodifiable-����������
	}
}
