/*-
 * $Id: TracePreAnalysis.java,v 1.6 2005/11/21 13:23:34 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * Данные из рефлектограммы и пред-анализа,
 * необходимые для анализа и фитировки.
 * Обобщен для возможности анализа усредненной рефлектограммы с
 * чувствительностью, определяемой шумом одной рефлектограммы.
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.6 $, $Date: 2005/11/21 13:23:34 $
 * @module
 */
public class TracePreAnalysis {
	// данные рефлектограммы
	public double[] yTrace; // trace data to display, never null
	public double deltaX = 0; // units = m
	public double ior = 0; // units = 1
	public double pulseWidth = 0; // units = ns

	// данные пре-анализа
	public int traceLength; // для одной р/г - длина до ухода р/г у шум; для совокупности - может быть дополнительно уменьшена до длины модельной кривой
	public double[] avNoise; // уровень шума (чувствительность) для анализа (<noise>)
	public double[] noiseAv; // уровень шума (точность аппроксимации) для фитировки (noise<>), может указывать на тот же массив, что и avNoise
	public double[] yCorr; // trace data to analyse, may be null

	public TracePreAnalysis() {
		// just empty
	}

	// копия, с клонированием yRaw, yCorr и совсем без копирования шума
	public TracePreAnalysis(TracePreAnalysis that) {
		this.deltaX = that.deltaX;
		this.ior = that.ior;
		this.pulseWidth = that.pulseWidth;
		this.traceLength = that.traceLength;
		this.yTrace = that.yTrace.clone();
		this.yCorr = that.yCorr != null ? that.yCorr.clone() : null;
		// noise не копируем
	}

	public void setMinLength(int len2) {
		if (this.traceLength > len2)
			this.traceLength = len2;
	}

	/**
	 * Проверяет совместимость параметров рефлектограммы, необходимых для
	 *   анализа
	 * @throws IncompatibleTracesException если
	 *   соответствующие параметры входных рефлектограмм различаются
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
	 * используется только при работе с одной рефлектограммой,
	 * когда avNoise и noiseAv равны.
	 */
	public void setNoise(double[] noise) {
		this.avNoise = noise;
		this.noiseAv = noise;
	}
	/**
	 * используется только при работе с одной рефлектограммой,
	 * когда avNoise и noiseAv равны.
	 */
	public double[] getNoise() {
		return this.avNoise;
	}
}
