/*-
 * $Id: Wavelet.java,v 1.2 2005/03/28 08:55:11 saa Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/03/28 08:55:11 $
 * @module
 */
public class Wavelet
{
	// native declarations

	private static native double[] nMakeTransform(int type, int scale, double[] input, int iFrom, int iTo, double norm);
	private static native double nGetNormStep(int type, int scale);
	private static native double nGetNormMx(int type, int scale);

	// export declarations

	public static final int TYPE_SINX = 0;
	public static final int TYPE_ABSXSINX = 1;
	public static double getNormStep(int type, int scale)
	{
		return getWLetNorma(scale, type);
	}
	public static double getNormMx(int type, int scale)
	{
		//return getWLetNormMx(scale, type);
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

	// the code that should be removed

	private static double[] waveletTransform(double[] y, int freq, double norma, int wLet, int start, int end)
	{
		double[] trans = new double[end - start];
		for(int i = start; i < end; i++)
		{
			double tmp = 0.;
			for(int j = Math.max(i - freq, 0); j < Math.min(i + freq + 1, end); j++)
				tmp = tmp + y[j] * wLet(j - i, freq, norma, wLet);
			trans[i] = tmp;
		}
		return trans;
	}

	private static double getWLetNorma(int freq, int waveletType)
	{
		double n = 0;
		switch (waveletType)
		{
			case  0:
				for(int i = -freq; i <= freq; i++)
					n = n + Math.abs(wLet1(i, freq, 1.));
				break;
			case  1:
				for(int i = -freq; i <= freq; i++)
					n = n + Math.abs(wLet2(i, freq, 1.));
				break;
			case  2:
				for(int i = -freq; i <= freq; i++)
					n = n + Math.abs(wLet3(i, freq, 1.));
				break;
			case  3:
				for(int i = -freq; i <= freq; i++)
					n = n + Math.abs(wLet4(i, freq, 1.));
				break;
			case  4:
				for(int i = -freq; i <= freq; i++)
					n = n + Math.abs(wLet5(i, freq, 1.));
				break;
			case  5:
				for(int i = -freq; i <= freq; i++)
					n = n + Math.abs(wLet6(i, freq, 1.));
				break;
			case  6:
				for(int i = -freq; i <= freq; i++)
					n = n + Math.abs(wLet7(i, freq, 1.));
				break;
			case  7:
				for(int i = -freq; i <= freq; i++)
					n = n + Math.abs(wLet8(i, freq, 1.));
				break;
			case  8:
				for(int i = -freq; i <= freq; i++)
					n = n + Math.abs(wLet9(i, freq, 1.));
				break;
			case  9:
				for(int i = -freq; i <= freq; i++)
					n = n + Math.abs(wLet10(i, freq, 1.));
				break;
			default:
				for(int i = -freq; i <= freq; i++)
					n = n + Math.abs(wLet1(i, freq, 1.));
				break;
		}
		n /= 2.;
		return n;
	}

	private static double getWLetNormMx(int freq, int waveletType)
	{
		double sum = 0;
		switch (waveletType)
		{
			case  0:
				for(int i = -freq; i <= freq; i++)
					sum = sum + i * wLet1(i, freq, 1.);
				break;
			case  1:
				for(int i = -freq; i <= freq; i++)
					sum = sum + i * wLet2(i, freq, 1.);
				break;
		}
		return sum;
	}

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
}

