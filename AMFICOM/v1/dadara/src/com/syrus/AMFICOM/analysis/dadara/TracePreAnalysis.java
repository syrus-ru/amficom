/*-
 * $Id: TracePreAnalysis.java,v 1.3 2005/07/22 06:39:51 saa Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/07/22 06:39:51 $
 * @module
 */
public class TracePreAnalysis {
	// ������ ��������������
	public double[] y; // trace data
	public double deltaX = 0; // units = m
	public double ior = 0; // units = 1
	public double pulseWidth = 0; // units = ns

	// ������ ���-�������
	public int traceLength; // ����� �� ����� �/� � ���
	public double[] avNoise; // ������� ���� (����������������) ��� ������� (<noise>)
	public double[] noiseAv; // ������� ���� (�������� �������������) ��� ��������� (noise<>), ����� ��������� �� ��� �� ������, ��� � avNoise

	public TracePreAnalysis() {
		// just empty
	}

	public TracePreAnalysis(TracePreAnalysis that, double[] y) {
		this.deltaX = that.deltaX;
		this.ior = that.ior;
		this.pulseWidth = that.pulseWidth;
		this.traceLength = that.traceLength;
		this.y = y;
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
		if (deltaX != that.deltaX)
			throw new IncompatibleTracesException("different deltaX");
		if (pulseWidth != that.pulseWidth)
			throw new IncompatibleTracesException("different pulse width");
		if (ior != that.ior)
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
