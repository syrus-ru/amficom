/*-
 * $Id: Trace.java,v 1.9 2005/09/01 12:08:33 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis;

import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
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
 * <li> BellcoreStructure bs - ���������� ��������������
 * <li> double[] traceData - ��� bs.getTraceData() (����������, ��� ����� �� ����� �������� ���� ������) 
 * <li> ar - ���������� ������� ������� (��� ����������� �/� �� ������; ��� primarytrace ����� �������������)
 *   (ar ����� ���� �������� � ����� ������ �� �������� Trace �� ��������
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
 * @version $Revision: 1.9 $, $Date: 2005/09/01 12:08:33 $
 * @module
 */
public class Trace {
	private BellcoreStructure bs;
	private AnalysisParameters ap; // ����� �������������� ��� ���������� mtae
	private String key;
	private Result result; // may be null

	private double[] traceData = null; // null if not cached yet
	private AnalysisResult ar = null;

	public Trace(BellcoreStructure bs, String key, AnalysisParameters ap) {
		this.bs = bs;
		this.ap = ap;
		this.key = key;
		this.result = null;
	}
	/**
	 * one of ap and mtae may be null
	 */
	private Trace(Result result,
			AnalysisParameters ap, AnalysisResult ar)
	throws SimpleApplicationException {
		this.bs = AnalysisUtil.getBellcoreStructureFromResult(result);
		this.ap = ap;
		this.key = result.getId().getIdentifierString();
		this.result = result;
		this.ar = ar;
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
	 * ������� Trace �� ������ ���������� ���������.
	 * <p>
	 * ����������, ���� �� � ���������, ���������� ������ ���������,
	 * ��������� �������.
	 * <ul>
	 * <li> ���� ��������� ������� ����, �� ������� Trace c ���� �����������.
	 * <li> ���� ���������� ������� ���, �� ������� Trace � ����������� �������
	 *      � �������������� ��������� AnalysisParameters.
	 * </ul>
	 * @param result ��������� ���������, ���������� ����������� ��������������
	 * @param ap ��������� �������, ������� ����� ������������ � ������,
	 *   ���� � ��������� ��� ������� ����������� �������
	 * @return Trace
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
		if (ar == null) {
			ar = CoreAnalysisManager.performAnalysis(bs, ap);
		}
		return ar;
	}

	public ModelTraceAndEventsImpl getMTAE() {
		return getAR().getMTAE();
	}
	/**
	 * ������������ ������ ���� � ��� �� ������,
	 * � ��� ����� ����� ���������� �� '=='.
	 * (��� ����� ��� ������ mostTypical Trace �� mostTypical BS)
	 * @return ������ BellcoreStructure, ���� � ��� �� ��� ��������� �������
	 */
	public BellcoreStructure getBS() {
		return bs;
	}
	public double[] getTraceData() {
		if (traceData == null) {
			traceData = bs.getTraceData();
		}
		return traceData;
	}
	public String getKey() {
		return key;
	}
	public double getDeltaX() {
		return this.bs.getResolution();
	}
	public Result getResult() {
		return result;
	}
}
