/*
 * $Id: CoreAnalysisManager.java,v 1.2 2004/12/17 18:16:20 arseniy Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2004/12/17 18:16:20 $
 * @module
 */

import java.util.Map;

import com.syrus.io.BellcoreStructure;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;

public class CoreAnalysisManager
{
	private CoreAnalysisManager() {
	}

	private static native double[] gauss(double[] y, double center,
			double amplitude, double sigma);

	/**
	 * @param waveletType число - номер вейвлета
	 * @param y входная рефлектограмма
	 * @param d_x шаг дискретизации в метрах
	 * @param formFactor ?
	 * @param min_level мин. уровень события
	 * @param max_level_noise макс. уровень шума?
	 * @param min_level_to_find_end мин. уровень конца
	 * @param min_weld мин. сварка
	 * @param min_connector мин. коннектор
	 * @param out_meanAttenuation резерв для возврата значения
	 * среднего затухания, сейчас не заполяется
	 * @param reflectiveSize хар. размер отраж. события (в точках?)
	 * @param nonReflectiveSize хар. размер неотраж. события (в точках?)
	 * @return массив событий
	 */
	private static native ReflectogramEvent[] analyse2(int waveletType,
			double[] y, //the refl. itself
			double d_x, //dx
			double formFactor, // 1. форм-фактор
			double min_level, // 2. мин уровень события
			double max_level_noise, // 3. макс уровень шума
			double min_level_to_find_end,// 4. мин. уровень конца
			double min_weld, // 5. минимальная сварка
			double min_connector, // 6. минимальный коннектор
			double[] out_meanAttenuation, // для возврата значения среднего затухания
			int reflectiveSize, int nonReflectiveSize);
	
	/**
	 * Метод вычисляет уровень шума на рефлектограмме по уровню 3 sigma,
	 * предполагая природу шума, независимую от величины измеряемого сигнала.
	 * Шум определяется на основе гистограммы распределения величины
	 * fabs(2*data[n] - data[n-8] - data[n+8]) для троек n-8,n,n+8, где
	 * нет монотонности data[], а data = 10^(y/5) - линейная
	 * (не логарифмическая) величина сигнала. Также исключаются участки
	 * низкого шума и сильного квантования уровней y[]. Шаг 8 выбран
	 * на основе специфики NetTest(tm) как ноль автокорреляционной функции
	 * фильтра BC.
	 * 
	 * Вычисляемая sigma предназначена для использования в алгоритмах 
	 * аппроксимации и обнаружения событий на фоне шума.
	 * 
	 * В случае претензий "в рефлектометрии уровнем шума принято
	 * называть другую величину", изменять код этого метода не следует,
	 * а сделать для своих целей другую величину.
	 *      
	 * @param y входная рефлектограмма
	 * @return 3*sigma гауссового шума, выраженное
	 * в единицах рефлектометрических децибелл
	 */
	private static native double nCalcNoise3s(double[] y);

	static {
		try {
			System.loadLibrary("dadara");
		} catch (java.lang.UnsatisfiedLinkError ex) {
			ex.printStackTrace();
		}
	}

	public static double[] calcGaussian(double[] y, int max_index) {
		double[] gauss = new double[y.length];
		int width = 0;

		double max_value = y[max_index];

		max_value *= .2;
		for (int i = 0; i < y.length; i++)
			if (y[i] > max_value)
				width++;
		max_value /= .2;

		double[] d = gauss(y, max_index, max_value, width);
		double center = d[0];
		max_value = d[1];
		double sigma_squared = d[2] * d[2];

		for (int i = 0; i < gauss.length; i++)
			gauss[i] = max_value
					* Math.exp(-(i - center) * (i - center) / sigma_squared);

		return gauss;
	}

	public static double[] calcThresholdCurve(double[] y, int max_index) {
		double[] threshold = new double[y.length];
		double max = 0;

		for (int i = 0; i < threshold.length; i++) {
			threshold[i] = 0;
			for (int j = 0; j < y.length; j++)
				if (j <= max_index - i || j >= max_index + i)
					threshold[i] += y[j];

			if (max < threshold[i])
				max = threshold[i];
		}

		for (int i = 0; i < threshold.length; i++)
			threshold[i] /= max;

		return threshold;
	}

	/**
	 * Делает анализ. Скрывает от UI сложности, связанные с правильным
	 * порядком вызова IA, fit, calcMutualParameters и выставлением нач. порогов.
	 * 
	 * @param strategy степень аппроксимации. Недокументировано.
	 * @param bs рефлектограмма
	 * @param pars недокументированный набор параметров для IA
	 * @param bellcoreTraces хэш всех открытых р/г для определения пределов колебаний
	 * @return массив событий
	 */

	// strategy == 0: min fitting
	public static ReflectogramEvent[] makeAnalysis(
			int strategy, BellcoreStructure bs,
			double[] pars, Map bellcoreTraces)
	{
	    // достаем данные
	    double y[] = bs.getTraceData();
	    double delta_x = bs.getResolution(); // метры

		int reflSize = ReflectogramMath.getReflectiveEventSize(y, 0.5);
		int nReflSize = ReflectogramMath.getNonReflectiveEventSize(
				y,
				1000,
				bs.getIOR(),
				delta_x);
		if (nReflSize > 3 * reflSize / 5)
			nReflSize = 3 * reflSize / 5;

		// формирование событий
		double[] meanAttenuation = { 0 }; // storage for meanAttenuation -- unused: XXX
		ReflectogramEvent[] ep = analyse2((int) pars[7], y, delta_x, pars[5],
				pars[0], pars[4], pars[3], pars[1], pars[2], meanAttenuation,
				reflSize, nReflSize);

		// определяем уровень шума для фитировки
		double noiseLevel = calcNoise3s(y);

		// установка параметров фитировки и фитировка
		// (с определением параметров нужных для расчета потерь и отражения)
		fitTrace(y, delta_x, ep, strategy, meanAttenuation[0], noiseLevel);

		// определение параметров потерь и отражения (по смежным событиям)
		ReflectogramEvent.calcMutualParameters(ep, y);

		// теперь еще и формируем пороги
		//for (int i = 0; i < ep.length; i++)
		//	ep[i].setDefaultThreshold(y);
		for (int i = 0; i < ep.length; i++)
			ep[i].setDefaultThreshold(bs, bellcoreTraces);

		return ep;
	}

	private static ReflectogramEvent[] fitTrace(double[] y, double delta_x,
			ReflectogramEvent[] events, int strategy, double meanAttenuation, double noiseLevel) {
		long t0 = System.currentTimeMillis();

		System.out.println("fitTrace: strategy = " + strategy);

		ReflectogramEvent.FittingParameters fPars =
		    new ReflectogramEvent.FittingParameters(
				y,
				2, // error mode = 2: XXX
				0.01 + 0.001, // XXX: 0.01: user-set; 0.001: rg precision 
				noiseLevel);

		for (int i = 0; i < events.length; i++)
		    events[i].setFittingParameters(fPars);

		for (int i = 0; i < events.length; i++)
			events[i].doFit();

		long t1 = System.currentTimeMillis();

		System.out.println("delta time: fit: " + (t1 - t0) + " ms");

		return events;
	}
	
	// Определяет уровень шума в р/г
	public static double calcNoise3s(double[] y)
	{
		return nCalcNoise3s(y);
	}
}
