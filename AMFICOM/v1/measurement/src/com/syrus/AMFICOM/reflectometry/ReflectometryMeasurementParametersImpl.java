/*-
 * $Id: ReflectometryMeasurementParametersImpl.java,v 1.4.2.3 2006/04/05 12:00:14 arseniy Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

import java.io.IOException;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.ActionParameter;
import com.syrus.AMFICOM.measurement.ActionTemplate;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.io.DataFormatException;
import com.syrus.util.ByteArray;
import com.syrus.util.Log;

/**
 * Параметры измерения в рефлектометрии.
 * @see ReflectometryMeasurementParameters
 * <p>
 * На данный момент modifier-методы не поддерживаются.
 * </p>
 * @author saa
 * @author $Author: arseniy $
 * @version $Revision: 1.4.2.3 $, $Date: 2006/04/05 12:00:14 $
 * @module
 */
public final class ReflectometryMeasurementParametersImpl implements ReflectometryMeasurementParameters {
	private ActionTemplate<Measurement> measurementTemplate;

	private int waveLength;
	private double traceLength;
	private double resolution;
	private int pulseWidth;
	private double refractionIndex;
	private int numberOfAverages;
	private boolean pulseWidthLowRes;
	private boolean gainSplice;
	private boolean liveFiberDetection;

	/**
	 * Этот конструктор предназначен для использования в
	 * {@link ReflectometryMeasurementSetup}, а не для непосредственного вызова.
	 * @param measurementTemplateId Должен быть шаблоном для измерений.
	 * @throws DataFormatException Неправильный формат входных данных
	 */
	 ReflectometryMeasurementParametersImpl(final Identifier measurementTemplateId) throws DataFormatException, ApplicationException {
		this.measurementTemplate = StorableObjectPool.getStorableObject(measurementTemplateId, true);
		this.unpack();
	}

	 /**
	  * @throws DataFormatException данные не распознаны
	  */
	 private void unpack() throws DataFormatException, ApplicationException {
		 final Set<ActionParameter> actionParameters = this.measurementTemplate.getActionParameters();
		 for (final ActionParameter actionParameter : actionParameters) {
			 final String parameterTypeCodename = actionParameter.getTypeCodename();
			if (parameterTypeCodename.equals(ReflectometryParameterTypeCodename.WAVE_LENGTH.stringValue())) {
				try {
					this.waveLength = new ByteArray(actionParameter.getValue()).toInt();
				} catch (IOException ioe) {
					throw new DataFormatException(ioe.toString()); // not enough bytes
				}
			} else if (parameterTypeCodename.equals(ReflectometryParameterTypeCodename.TRACE_LENGTH.stringValue())) {
				try {
					this.traceLength = new ByteArray(actionParameter.getValue()).toDouble();
				} catch (IOException ioe) {
					throw new DataFormatException(ioe.toString()); // not enough bytes
				}
			} else if (parameterTypeCodename.equals(ReflectometryParameterTypeCodename.RESOLUTION.stringValue())) {
				try {
					this.resolution = new ByteArray(actionParameter.getValue()).toDouble();
				} catch (IOException ioe) {
					throw new DataFormatException(ioe.toString()); // not enough bytes
				}
			} else if (parameterTypeCodename.equals(ReflectometryParameterTypeCodename.PULSE_WIDTH_M.stringValue())
					|| parameterTypeCodename.equals(ReflectometryParameterTypeCodename.PULSE_WIDTH_NS.stringValue())) {
				try {
					this.pulseWidth = new ByteArray(actionParameter.getValue()).toInt();
				} catch (IOException ioe) {
					throw new DataFormatException(ioe.toString()); // not enough bytes
				}
			} else if (parameterTypeCodename.equals(ReflectometryParameterTypeCodename.INDEX_OF_REFRACTION.stringValue())) {
				try {
					this.refractionIndex = new ByteArray(actionParameter.getValue()).toDouble();
				} catch (IOException ioe) {
					throw new DataFormatException(ioe.toString()); // not enough bytes
				}
			} else if (parameterTypeCodename.equals(ReflectometryParameterTypeCodename.AVERAGE_COUNT.stringValue())) {
				try {
					this.numberOfAverages = new ByteArray(actionParameter.getValue()).toInt();
				} catch (IOException ioe) {
					throw new DataFormatException(ioe.toString()); // not enough bytes
				}
			} else if (parameterTypeCodename.equals(ReflectometryParameterTypeCodename.FLAG_PULSE_WIDTH_LOW_RES.stringValue())) {
				try {
					this.pulseWidthLowRes = new ByteArray(actionParameter.getValue()).toBoolean();
				} catch (IOException ioe) {
					throw new DataFormatException(ioe.toString()); // not enough bytes
				}
			} else if (parameterTypeCodename.equals(ReflectometryParameterTypeCodename.FLAG_GAIN_SPLICE_ON.stringValue())) {
				try {
					this.gainSplice = new ByteArray(actionParameter.getValue()).toBoolean();
				} catch (IOException ioe) {
					throw new DataFormatException(ioe.toString()); // not enough bytes
				}
			} else if (parameterTypeCodename.equals(ReflectometryParameterTypeCodename.FLAG_LIFE_FIBER_DETECT.stringValue())) {
				try {
					this.liveFiberDetection = new ByteArray(actionParameter.getValue()).toBoolean();
				} catch (IOException ioe) {
					throw new DataFormatException(ioe.toString()); // not enough bytes
				}
			} else {
				Log.errorMessage("Unknown codename: " + parameterTypeCodename);
			}
		}
		// @todo: добавить проверку полноты, неизбыточности и корректности полученных данных
		// в случае нарушения - бросать DataFormatException
	}

	public boolean hasGainSplice() {
		return this.gainSplice;
	}

	public boolean hasPulseWidthLowRes() {
		return this.pulseWidthLowRes;
	}

	/**
	 * @deprecated Use {@link #hasPulseWidthLowRes()} instead.
	 */
	@Deprecated
	public boolean hasHighResolution() {
		return !this.hasPulseWidthLowRes();
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
		return this.waveLength;
	}

	public void setGainSplice(boolean gainSplice) {
//		this.gainSplice = gainSplice;
		throw new UnsupportedOperationException(); // @todo: implement
	}

	/**
	 * @deprecated Use {@link #setPulseWidthLowRes(boolean)} instead.
	 * @param highResolution
	 */
	@Deprecated
	public void setHighResolution(boolean highResolution) {
//		this.highResolution = highResolution;
		throw new UnsupportedOperationException(); // @todo: implement
	}

	public void setPulseWidthLowRes(boolean pulseWidthLowRes) {
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
