/*-
 * $Id: DadaraReflectometryAnalysisResult.java,v 1.2 2005/10/14 16:53:48 saa Exp $
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
 * <p>������������ ������ ������ ������ - � �� �����, � �� ������.
 * <p>������������ ��� ������� ��������:
 * �� ������ {@link ReflectometryAnalysisResult}
 * �
 * �� ������ ���� {@link AnalysisResult} � {@link EtalonComparison}.
 * <p>��� �������� �� ������ {@link ReflectometryAnalysisResult}
 * �������� ������� �������� ����� �������, ��������� ��� �����������
 * ����������� ������� ������ {@link ReflectometryAnalysisResult}.
 * <p>��� ��������
 * �� ������ ���� {@link AnalysisResult} � {@link EtalonComparison}
 * ����� ��������� null-��������, ������, ���� ������� ar � ec ����� not null,
 * �� �������������, ��� � ��� �������� ���������� ������� ����� not null.
 * <p>XXX: ������������ null-�������� ������� ������� �� ������� ��������.
 * 
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.2 $, $Date: 2005/10/14 16:53:48 $
 * @module dadara
 */
public class DadaraReflectometryAnalysisResult implements
		ReflectometryAnalysisResult {

	private AnalysisResult ar; // may be null
	private ReflectogramMismatchImpl[] rma; // may be null
	private EvaluationPerEventResultImpl epe; // may be null
	private ReflectometryEvaluationOverallResultImpl eOverall; // may be null

	/**
	 * ��������� �� ������ ����������� ������� � ��������� dadara
	 * ������� ����� ������� ������.
	 * ���� ar != null, �� �������������, ��� �������� �������� ����� �����
	 * not null. ���� ar == null, �� �������������, ���
	 * ��� �������� �������� ����� null ;-)
	 * @param ar may be null.
	 * @param ec not null unless ar == null.
	 */
	public DadaraReflectometryAnalysisResult(AnalysisResult ar,
			EtalonComparison ec) {
		if (ar == null) {
			this.ar = null;
			this.rma = null;
			this.epe = null;
			this.eOverall = null;
			return;
		}
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
	public DadaraReflectometryAnalysisResult(ReflectometryAnalysisResult that)
	throws DataFormatException {
		// ������������� AR
		final byte[] arBytes = that.getDadaraAnalysisResultBytes();
		this.ar = arBytes == null ? null
				: (AnalysisResult)DataStreamableUtil.readDataStreamableFromBA(
					arBytes,
					AnalysisResult.getDSReader());
		// ������������� RM
		final byte[] rmBytes = that.getDadaraReflectogramMismatchBytes();
		this.rma = rmBytes == null ? null
				: ReflectogramMismatchImpl.alarmsFromByteArray( rmBytes);
		// ������������� PE
		final byte[] epeBytes = that.getDadaraEvaluationPerEventResultBytes();
		this.epe = epeBytes == null ? null
				: (EvaluationPerEventResultImpl)
					DataStreamableUtil.readDataStreamableFromBA(
						epeBytes,
						EvaluationPerEventResultImpl.getDSReader());
		// ������ ����� Overall
		final ReflectometryEvaluationOverallResult eoResult =
			that.getReflectometryEvaluationOverallResult();
		this.eOverall = eoResult == null ? null
				: new ReflectometryEvaluationOverallResultImpl(eoResult);
	}

	/**
	 * ���������� ����� ������ AnalysisResult, may be null
	 * @return ����� ������ AnalysisResult, may be null
	 */
	public AnalysisResult getDadaraAnalysisResult() {
		return this.ar == null ? null
				: new AnalysisResult(this.ar); // ������ �����
	}

	/**
	 * ���������� ������������� � ���� {@link EtalonComparison}, may be null.
	 * ���� �� ���������� ���� �� ���� ��������, ����������� ���
	 * {@link EtalonComparison}, ����� ���������� null.
	 */
	public EtalonComparison getDadaraEtalonComparison() {
		if (this.rma == null || this.epe == null || this.eOverall == null) {
			return null;
		}
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
		return this.ar == null ? null
				: this.ar.toByteArray();
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult#getDadaraReflectogramMismatchBytes()
	 */
	public byte[] getDadaraReflectogramMismatchBytes() {
		return this.rma == null ? null
				: ReflectogramMismatchImpl.alarmsToByteArray(this.rma);
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult#getDadaraEvaluationPerEventResultBytes()
	 */
	public byte[] getDadaraEvaluationPerEventResultBytes() {
		return this.epe == null ? null
				: DataStreamableUtil.writeDataStreamableToBA(this.epe);
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult#getReflectometryEvaluationOverallResult()
	 */
	public ReflectometryEvaluationOverallResult
	getReflectometryEvaluationOverallResult() {
		return this.eOverall; // ��� unmodifiable-���������� ���� null
	}
}
