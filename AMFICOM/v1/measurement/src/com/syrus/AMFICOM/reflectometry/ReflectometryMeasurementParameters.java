/*-
 * $Id: ReflectometryMeasurementParameters.java,v 1.2 2005/09/21 16:21:34 bob Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

import java.io.IOException;

import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.io.DataFormatException;
import com.syrus.util.ByteArray;
import com.syrus.util.Log;

/**
 * ��������� ��������� � ��������������.
 * ������� ��:
 * <ul>
 * <li> {@link #getWavelength()} ����� �����
 * <li> {@link #getRefractionIndex()} ���������� �����������
 * <li> {@link #getPulseWidth()} ������������ ��������
 * <li> {@link #getTraceLength()} ����� ��������������
 * <li> {@link #getResolution()} ���������� �� ���������
 * <li> {@link #getNumberOfAverages()} ����� ����������
 * </ul>
 * <p>
 * �� ������ ������ modifier-������ �� ��������������.
 * </p>
 * @author saa
 * @author $Author: bob $
 * @version $Revision: 1.2 $, $Date: 2005/09/21 16:21:34 $
 * @module
 */
public final class ReflectometryMeasurementParameters {
	private ParameterSet msPars;

	private double traceLength;
	private boolean gainSplice;
	private boolean liveFiberDetection;
	private double refractionIndex;
	private int wavelength;
	private int numberOfAverages;
	private int pulseWidth;
	private boolean highResolution;
	private double resolution;

	/**
	 * ���� ����������� ������������ ��� ������������� �
	 * {@link ReflectometryMeasurementSetup}, � �� ��� ����������������� ������.
	 * @param msPars ������ ���� parameterSet'��
	 *   {@link com.syrus.AMFICOM.measurement.MeasurementSetup}
	 * @throws DataFormatException ������������ ������ ������� ������
	 */
	 ReflectometryMeasurementParameters(final ParameterSet msPars)
	 throws DataFormatException {
		this.msPars = msPars;
		this.unpack();
	 }

	 /**
	  * @throws DataFormatException ������ �� ����������
	  * @throws NumberFormatException ������ �� ����������
	  */
	 private void unpack() throws DataFormatException {
		final Parameter[] setParameters = this.msPars.getParameters();
		for (int i = 0; i < setParameters.length; i++) {
			final ParameterType parameterType = setParameters[i].getType();
			if (parameterType.equals(ParameterType.REF_FLAG_GAIN_SPLICE_ON)) {
				try {
					final boolean b = new ByteArray(setParameters[i].getValue()).toBoolean();
					this.gainSplice = b;
				} catch (IOException e) {
					throw new DataFormatException(); // not enough bytes
				}
				continue;
			}
			if (parameterType.equals(ParameterType.REF_FLAG_LIFE_FIBER_DETECT)) {
				try {
					final boolean b = new ByteArray(setParameters[i].getValue()).toBoolean();
					this.liveFiberDetection = b;
				} catch (IOException e) {
					throw new DataFormatException(); // not enough bytes
				}
				continue;
			}
			final String stringValue = setParameters[i].getStringValue();
			try {
				if (parameterType.equals(ParameterType.REF_TRACE_LENGTH)) {
					this.traceLength = Double.parseDouble(stringValue);
				} else if (parameterType.equals(ParameterType.REF_INDEX_OF_REFRACTION)) {
					this.refractionIndex = Double.parseDouble(stringValue);
				} else if (parameterType.equals(ParameterType.REF_WAVE_LENGTH)) {
					this.wavelength = Integer.parseInt(stringValue);
				} else if (parameterType.equals(ParameterType.REF_AVERAGE_COUNT)) {
					this.numberOfAverages = (int) Double.parseDouble(stringValue);
				} else if (parameterType.equals(ParameterType.REF_RESOLUTION)) {
					this.resolution = Double.parseDouble(stringValue);
				} else if (parameterType.equals(ParameterType.REF_PULSE_WIDTH_HIGH_RES)) {
					this.pulseWidth = Integer.parseInt(stringValue);
					this.highResolution = true;
				} else if (parameterType.equals(ParameterType.REF_PULSE_WIDTH_LOW_RES)) {
					this.pulseWidth = Integer.parseInt(stringValue);
					this.highResolution = false;
				}
			} catch (NumberFormatException ex) {
				Log.errorException(ex);
				throw new DataFormatException(ex.toString());
			}
		}
		// @todo: �������� �������� �������, �������������� � ������������ ���������� ������
		// � ������ ��������� - ������� DataFormatException
	}

	public boolean hasGainSplice() {
		return this.gainSplice;
	}

	public boolean hasHighResolution() {
		return this.highResolution;
	}

	public boolean hasLiveFiberDetection() {
		return this.liveFiberDetection;
	}

	public int getNumberOfAverages() {
		return this.numberOfAverages;
	}

	public int getPulseWidth() {
		return this.pulseWidth;
	}

	public double getRefractionIndex() {
		return this.refractionIndex;
	}

	public double getResolution() {
		return this.resolution;
	}

	public double getTraceLength() {
		return this.traceLength;
	}

	public int getWavelength() {
		return this.wavelength;
	}

	public void setGainSplice(boolean gainSplice) {
//		this.gainSplice = gainSplice;
		throw new UnsupportedOperationException(); // @todo: implement
	}

	public void setHighResolution(boolean highResolution) {
//		this.highResolution = highResolution;
		throw new UnsupportedOperationException(); // @todo: implement
	}

	public void setLiveFiberDetection(boolean liveFiberDetection) {
//		this.liveFiberDetection = liveFiberDetection;
		throw new UnsupportedOperationException(); // @todo: implement
	}

	public void setNumberOfAverages(int numberOfAverages) {
//		this.numberOfAverages = numberOfAverages;
		throw new UnsupportedOperationException(); // @todo: implement
	}

	public void setPulseWidth(int pulseWidth) {
//		this.pulseWidth = pulseWidth;
		throw new UnsupportedOperationException(); // @todo: implement
	}

	public void setRefractionIndex(double refractionIndex) {
//		this.refractionIndex = refractionIndex;
		throw new UnsupportedOperationException(); // @todo: implement
	}

	public void setResolution(double resolution) {
//		this.resolution = resolution;
		throw new UnsupportedOperationException(); // @todo: implement
	}

	public void setTraceLength(double traceLength) {
//		this.traceLength = traceLength;
		throw new UnsupportedOperationException(); // @todo: implement
	}

	public void setWavelength(int wavelength) {
//		this.wavelength = wavelength;
		throw new UnsupportedOperationException(); // @todo: implement
	}
}
