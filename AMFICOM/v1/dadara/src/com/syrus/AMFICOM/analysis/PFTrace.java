/*-
 * $Id: PFTrace.java,v 1.5 2005/11/21 13:22:19 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import com.syrus.io.BellcoreStructure;

/**
 * ���������� ��� {@link BellcoreStructure}, ����������� ���������������
 * ���������� � ���������� getTraceData() � ��������� ����������.
 * ������ � ����� ���������� �������������� ����� ����� ����� ������
 * ������� ����� ������; �������������� ������ - ����� getBS()
 * @author $Author: saa $
 * @version $Revision: 1.5 $, $Date: 2005/11/21 13:22:19 $
 * @module
 */
public class PFTrace {
	private static final double FILTER_DECAY = 0.5; // dB/m (0.4 .. 0.7)
	private BellcoreStructure bs;
	private double[] rawTrace; // lazy �������� �������� ��������������
	private double[] filteredTrace; // lazy ����-������������ ��������������

	public PFTrace(BellcoreStructure bs) {
		assert bs != null;
		this.bs = bs;
		this.rawTrace = null;
		this.filteredTrace = null;
	}

	/**
	 * �������� ����� �������� ������
	 * (������������ ������ ��� ����������� ������ ����)
	 * <p>
	 * NB: ����� ������ protected, ����� �������� ���������� ��������� � ����
	 * �����: ��� ������� � ����-������������� �/� �����������
	 * {@link #getFilteredTraceClone()}.
	 * ���� �� ������ � ����� ������ ��� �� ���������, ���, � ��������,
	 * ����� �������.
	 * </p>
	 * <p>
	 * �� ������ ������ ���� ����� �� ������������.
	 * </p>
	 * @return ������ �����. ������ ����� �������� ���������� ������.
	 */
	protected double[] getRawTraceClone() {
		return getRawTrace().clone();
	}

	/**
	 * �������� ����� ������������� ������
	 * (������������ ��� ������� � �����������).
	 * @return ������ �����. ������ ����� �������� ���������� ������.
	 */
	public double[] getFilteredTraceClone() {
		return getFilteredTrace().clone();
	}

	/**
	 * ���������� {@link #getRawTraceClone()}, �� �� ������ �����.
	 * ������ �� ������ ����� �������� ���������� ������.
	 */
	protected double[] getRawTrace() {
		if (this.rawTrace == null) {
			this.rawTrace = this.bs.getTraceData();
		}
		return this.rawTrace;
	}

	/**
	 * ���������� {@link #getFilteredTraceClone()}, �� �� ������ �����.
	 * ������ �� ������ ����� �������� ���������� ������.
	 */
	protected double[] getFilteredTrace() {
		if (this.filteredTrace == null) {
			this.filteredTrace = preFilter(getRawTrace(), getResolution());
		}
		return this.filteredTrace;
	}

	private static double[] preFilter(double[] y, double resolution) {
		double[] ret = y.clone();
		double delta = resolution * FILTER_DECAY;
		for (int i = 1; i < ret.length; i++) {
			double level = ret[i - 1] - delta;
			if (ret[i] < level) {
				ret[i] = level;
			}
		}
		return ret;
	}

	/**
	 * @see BellcoreStructure#getIOR()
	 */
	public double getIOR() {
		return this.bs.getIOR();
	}

	/**
	 * @see BellcoreStructure#getPulsewidth()
	 */
	public int getPulsewidth() {
		return this.bs.getPulsewidth();
	}

	/**
	 * @see BellcoreStructure#getResolution()
	 */
	public double getResolution() {
		return this.bs.getResolution();
	}

	/**
	 * @see BellcoreStructure#getWavelength()
	 */
	public int getWavelength() {
		return this.bs.getWavelength();
	}

	/**
	 * ���������� ����������� BellcoreStructure ��� �����
	 * ����������/�������������� � ������� � ������������� ����������.
	 * �������, ��� ������ �� ����� �������� ���������� BellcoreStructure. 
	 * @return BellcoreStructure
	 */
	public BellcoreStructure getBS() {
		return this.bs;
	}

	@Override
	public int hashCode() {
		double[] y = getRawTrace();
		int result = 17;
		for (int i = 0; i < y.length; i++) {
			long bits = Double.doubleToLongBits(y[i]);
			result = 37 * result + (int)(bits ^ (bits >>> 32));
		}
		return result;
	}

	@Override
	public String toString() {
		return "PFTrace(" + getRawTrace().length
			+ ":" + hashCode() + ")";
	}
}
