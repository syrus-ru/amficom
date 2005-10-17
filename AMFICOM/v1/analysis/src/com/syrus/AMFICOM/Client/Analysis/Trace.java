/*-
 * $Id: Trace.java,v 1.13 2005/10/17 14:20:09 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis;

import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.PFTrace;
import com.syrus.AMFICOM.analysis.SimpleApplicationException;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.AnalysisResult;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.DataFormatException;

/**
 * ������������ ����������� ��������������.
 * ����������:
 * <ul>
 * <li> {@link PFTrace} pfTrace - ���������� ��������������, �� ��� ����-�������������
 * <li> double[] traceData - ��� pfTrace.getFilteredTrace() (����������, ��� ����� �� ����� �������� ���� ������) 
 * <li> ar - ���������� ������� ������� (��� ����������� �/� �� ������; ��� primarytrace ����� �������������)
 *   (ar ����� ���� �������� � ����� ������ �� �������� {@link Trace} �� ��������
 *   getAR/getMTAE, � ������������ � ������ ������������ �� ���� ������� �������
 *   ���������� �������; ����� ����� ���� ��������, ���� ����� ������ ���
 *   �������� �� ������)
 * <li> Object key - ������������� ��� �������� ������������, ��� ����� ����
 *   <ul>
 *   <li> String ���������� ���� - ��� �����
 *   <li> String measurementId - ��� �/� �� ��
 *   </ul>
 * <li> Result (null, ���� ��� ��������� ����) - �� ���� ����� ���������� ������, � ������� ���� ����� �/�
 * @author $Author: saa $
 * @version $Revision: 1.13 $, $Date: 2005/10/17 14:20:09 $
 * @module
 */
public class Trace {
	private PFTrace pfTrace;
	private AnalysisParameters ap; // ����� �������������� ��� ���������� mtae
	private String key;
	private Result result; // may be null

	private double[] traceData = null; // null if not cached yet
	private AnalysisResult ar = null;

	private boolean analysisLoaded; // true ���� ��������� ������� ����� �����

	public Trace(BellcoreStructure bs, String key, AnalysisParameters ap) {
		this.pfTrace = new PFTrace(bs);
		this.ap = ap;
		this.key = key;
		this.result = null;
		this.analysisLoaded = false;
	}

	public Trace(PFTrace trace, String key, AnalysisParameters ap) {
		this.pfTrace = trace;
		this.ap = ap;
		this.key = key;
		this.result = null;
		this.analysisLoaded = false;
	}
	/**
	 * one of ap and mtae may be null
	 */
	private Trace(Result result,
			AnalysisParameters ap, AnalysisResult ar)
	throws SimpleApplicationException {
		this.pfTrace = new PFTrace(
				AnalysisUtil.getBellcoreStructureFromResult(result));
		this.ap = ap;
		this.key = result.getId().getIdentifierString();
		this.result = result;
		this.ar = ar;
		this.analysisLoaded = ar != null;
	}
	/**
	 * ��������� �������������� ��� �������������� ���������� ����������� �������
	 * XXX try to use getTraceWithARIfPossible instead
	 * @param result ��������� ���������
	 * @param ap ��������� ������� (����� ������������ � ������ getMTAE())
	 * @throws SimpleApplicationException ���� ���������� �������
	 * ���������, �� ���������� ��������������
	 */
	public Trace(Result result, AnalysisParameters ap)
	throws SimpleApplicationException {
		this(result, ap, null);
	}
	/**
	 * ��������� �������������� � �������������� ����������� ������������ �������
	 * XXX try to use getTraceWithARIfPossible instead
	 * @param result ��������� ���������
	 * @param ar ��������� �������
	 * @throws SimpleApplicationException ���� ���������� �������
	 * ���������, �� ���������� �������������� 
	 */
	public Trace(Result result, AnalysisResult ar)
	throws SimpleApplicationException {
		this(result, null, ar);
	}

	/**
	 * ������� {@link Trace} �� ������ ���������� ���������.
	 * <p>
	 * ����������, ���� �� � ���������, ���������� ������ ���������,
	 * ��������� �������.
	 * <ul>
	 * <li> ���� ��������� ������� ����, �� ������� {@link Trace} c ���� �����������.
	 * <li> ���� ���������� ������� ���, �� ������� {@link Trace} � ����������� �������
	 *      � �������������� ��������� AnalysisParameters.
	 * </ul>
	 * @param result ��������� ���������, ���������� ����������� ��������������
	 * @param ap ��������� �������, ������� ����� ������������ � ������,
	 *   ���� � ��������� ��� ������� ����������� �������
	 * @return {@link Trace}
	 * @throws ApplicationException ������ ������ � pool'�� ��� ��������
	 * @throws DataFormatException ��������� ����������� ����������� ������
	 * @throws SimpleApplicationException ���������� �������
	 *   ���������, �� ���������� ��������������
	 */
	public static Trace getTraceWithARIfPossible(Result result,
			AnalysisParameters ap)
	throws DataFormatException, ApplicationException, SimpleApplicationException {
		AnalysisResult ar =
			AnalysisUtil.getAnalysisResultForResultIfPresent(result);
		if (ar != null)
			return new Trace(result, ar);
		else
			return new Trace(result, ap);
	}

	public AnalysisResult getAR() {
		if (this.ar == null) {
			this.ar = CoreAnalysisManager.performAnalysis(this.pfTrace, this.ap);
		}
		return this.ar;
	}

	public ModelTraceAndEventsImpl getMTAE() {
		return getAR().getMTAE();
	}
	/**
	 * ������������ ������ ���� � ��� �� ������,
	 * � ��� ����� ����� ���������� �� '=='.
	 * (��� ����� ��� ������ mostTypical {@link Trace} �� mostTypical PFTrace)
	 * @return ������ PFTrace, ���� � ��� �� ��� ��������� �������
	 */
	public PFTrace getPFTrace() {
		return this.pfTrace;
	}
	public double[] getTraceData() {
		if (this.traceData == null) {
			this.traceData = this.pfTrace.getFilteredTraceClone();
		}
		return this.traceData;
	}
	public String getKey() {
		return this.key;
	}
	public double getDeltaX() {
		return this.pfTrace.getResolution();
	}
	public Result getResult() {
		return this.result;
	}
	/**
	 * @return true, ���� ���������� ������� ���� ������ � ������ ��������
	 * ����� �������. ���������� ��� ������ ��������,
	 * ��� ������ ��� �������� �� ������.
	 */
	public boolean hasAnalysisLoaded() {
		return this.analysisLoaded;
	}
}
