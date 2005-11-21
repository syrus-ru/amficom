/*-
 * $Id: TracePreAnalysis.java,v 1.6 2005/11/21 13:23:34 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * ������ �� �������������� � ����-�������,
 * ����������� ��� ������� � ���������.
 * ������� ��� ����������� ������� ����������� �������������� �
 * �����������������, ������������ ����� ����� ��������������.
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.6 $, $Date: 2005/11/21 13:23:34 $
 * @module
 */
public class TracePreAnalysis {
	// ������ ��������������
	public double[] yTrace; // trace data to display, never null
	public double deltaX = 0; // units = m
	public double ior = 0; // units = 1
	public double pulseWidth = 0; // units = ns

	// ������ ���-�������
	public int traceLength; // ��� ����� �/� - ����� �� ����� �/� � ���; ��� ������������ - ����� ���� ������������� ��������� �� ����� ��������� ������
	public double[] avNoise; // ������� ���� (����������������) ��� ������� (<noise>)
	public double[] noiseAv; // ������� ���� (�������� �������������) ��� ��������� (noise<>), ����� ��������� �� ��� �� ������, ��� � avNoise
	public double[] yCorr; // trace data to analyse, may be null

	public TracePreAnalysis() {
		// just empty
	}

	// �����, � ������������� yRaw, yCorr � ������ ��� ����������� ����
	public TracePreAnalysis(TracePreAnalysis that) {
		this.deltaX = that.deltaX;
		this.ior = that.ior;
		this.pulseWidth = that.pulseWidth;
		this.traceLength = that.traceLength;
		this.yTrace = that.yTrace.clone();
		this.yCorr = that.yCorr != null ? that.yCorr.clone() : null;
		// noise �� ��������
	}

	public void setMinLength(int len2) {
		if (this.traceLength > len2)
			this.traceLength = len2;
	}

	/**
	 * ��������� ������������� ���������� ��������������, ����������� ���
	 *   �������
	 * @throws IncompatibleTracesException ����
	 *   ��������������� ��������� ������� ������������� �����������
	 */
	public void checkTracesCompatibility(TracePreAnalysis that)
	throws IncompatibleTracesException {
		if (this.deltaX != that.deltaX)
			throw new IncompatibleTracesException("different deltaX");
		if (this.pulseWidth != that.pulseWidth)
			throw new IncompatibleTracesException("different pulse width");
		if (this.ior != that.ior)
			throw new IncompatibleTracesException("different IOR");
	}

	/**
	 * ������������ ������ ��� ������ � ����� ���������������,
	 * ����� avNoise � noiseAv �����.
	 */
	public void setNoise(double[] noise) {
		this.avNoise = noise;
		this.noiseAv = noise;
	}
	/**
	 * ������������ ������ ��� ������ � ����� ���������������,
	 * ����� avNoise � noiseAv �����.
	 */
	public double[] getNoise() {
		return this.avNoise;
	}
}
