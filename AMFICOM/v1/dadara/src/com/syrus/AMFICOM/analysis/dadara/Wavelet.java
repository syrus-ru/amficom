/*-
 * $Id: Wavelet.java,v 1.3 2005/03/28 11:34:37 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * Класс предоставляет самый минимальный набор функций работы с вейвлетами,
 * которые нужны в GUI анализа. Введен для того, чтобы можно было заменить
 * медленный Java-код на native-вызов.
 * @author $Author: saa $
 * @version $Revision: 1.3 $, $Date: 2005/03/28 11:34:37 $
 * @module
 */
public class Wavelet
{
	public static final int TYPE_SINX = 0;
	public static final int TYPE_ABSXSINX = 1;

	private static native double[] nMakeTransform(int type, int scale, double[] input, int iFrom, int iTo, double norm);
	private static native double nGetNormStep(int type, int scale); 
	private static native double nGetNormMx(int type, int scale);

	public static double getNormStep(int type, int scale)
	{
		return nGetNormStep(type, scale);
	}
	public static double getNormMx(int type, int scale)
	{
		return nGetNormMx(type, scale);
	}
	public static double[] makeTransform(int type, int scale, double[] input, int iFrom, int iTo, double norm)
	{
		return nMakeTransform(type, scale, input, iFrom, iTo, norm);
		//return waveletTransform(input, scale, norm, type, iFrom, iTo + 1);
	}

	private Wavelet()
	{ // empty
	}

	// outdated code that should be removed
/*
	private static double wLet(int arg, int freq, double norma, int waveletType)
	{
		switch (waveletType)
		{
			case  0: return  wLet1(arg, freq, norma);
			case  1: return  wLet2(arg, freq, norma);
			case  2: return  wLet3(arg, freq, norma);
			case  3: return  wLet4(arg, freq, norma);
			case  4: return  wLet5(arg, freq, norma);
			case  5: return  wLet6(arg, freq, norma);
			case  6: return  wLet7(arg, freq, norma);
			case  7: return  wLet8(arg, freq, norma);
			case  8: return  wLet9(arg, freq, norma);
			case  9: return wLet10(arg, freq, norma);
			default: return  wLet1(arg, freq, norma);
		}
	}

	private static double wLet1(int arg, int freq, double norma)
	{
		return (Math.sin(arg * Math.PI / freq)) / norma;
	}

	private static double wLet2(int arg, int freq, double norma)
	{
		return (Math.sin(arg * Math.PI / freq) * Math.abs((double)arg)) / norma;
	}

	private static double wLet3(int arg, int freq, double norma)
	{
		return (Math.sin(arg * Math.PI / freq) / (1. + Math.abs((double)arg))) / norma;
	}

	private static double wLet4(int arg, int freq, double norma)
	{
		return (Math.sin(arg * Math.PI / freq) / (1. + Math.sqrt(Math.abs((double)arg)))) / norma;
	}

	private static double wLet5(int arg, int freq, double norma)
	{
		if(arg < 0)
			return -1. / norma;
		if(arg > 0)
			return 1. / norma;
		return 0.;
	}

	private static double wLet6(int arg, int freq, double norma)
	{
		return (((double)arg) / freq) / norma;
	}

	private static double wLet7(int arg, int freq, double norma)
	{
		if(arg > 0)
			return ((double)(freq - arg)) / norma;
		if(arg < 0)
			return -((double)(freq + arg)) / norma;
		return 0.;
	}

	private static double wLet8(int arg, int freq, double norma)
	{
		return (Math.sin(arg * Math.PI / freq) + ((double)arg) / freq / 2.) / norma;
	}

	private static double wLet9(int arg, int freq, double norma)
	{
		return (Math.sin(arg * Math.PI / freq / 2.))/norma;
	}

	private static double wLet10(int arg, int freq, double norma)
	{
		return (Math.sin(arg * Math.PI / freq / 2.) - ((double)arg) / freq / 2.) / norma;
	}
*/
}
