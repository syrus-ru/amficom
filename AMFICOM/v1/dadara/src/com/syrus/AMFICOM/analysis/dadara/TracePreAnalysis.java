/*-
 * $Id: TracePreAnalysis.java,v 1.7 2006/01/17 12:22:28 saa Exp $
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
 * @version $Revision: 1.7 $, $Date: 2006/01/17 12:22:28 $
 * @module
 */
public class TracePreAnalysis {
	// ������ ��������������
	public double[] yTrace; // trace data to display, never null
	public double deltaX = 0; // units = m
	public double ior = 0; // units = 1
	public double pulseWidth = 0; // units = ns

	/*
	 * ������ ���-�������.
	 * ��� ����� �/� - ����� �� ����� �/� � ���.
	 * length <= yTrace.length
	 */
	public int traceLength;

	/*
	 * ������� ���� (����������������) ��� ������� (<noise>).
	 * length == traceLength
	 */
	public double[] avNoise;

	/*
	 * ������� ���� (�������� �������������) ��� ��������� (noise<>),
	 * ����� ��������� �� ��� �� ������, ��� � avNoise.
	 * length == traceLength
	 */
	public double[] noiseAv; 

	/*
	 * trace data to analyse,
	 * ����� ���� null,
	 * ���� not null, �� length >= traceLength � ������ length = yTrace.length
	 */
	public double[] yCorr;
	

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
