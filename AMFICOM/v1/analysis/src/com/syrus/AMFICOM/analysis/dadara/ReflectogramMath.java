package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.analysis.dadara.MathRef;

public class ReflectogramMath
{
	public static double[] getReflectogrammFromEvents(ReflectogramEvent[] re, int arrayLength)
	{
		correctEvents(re);

		if (arrayLength <= 0)
			arrayLength = re[re.length-1].getEnd();
		double[] y = new double[arrayLength];

		for(int i = 0; i < re.length; i++)
			for(int j = re[i].getBegin(); j < Math.min(re[i].getEnd() + 1, y.length); j++)
				y[j] = re[i].refAmplitude(j);

		for(int i = re[re.length-1].getEnd(); i < y.length; i++)
			y[i] = 0.;

		return y;
	}

	public static ReflectogramEvent[] getThreshold(ReflectogramEvent[] re, int threshold)
	{
		ReflectogramEvent[] thresholds = new ReflectogramEvent[re.length];
		for(int i=0; i < re.length; i++)
			thresholds[i] = re[i].getThresholdReflectogramEvent(threshold);

		correctEvents(thresholds);
		return thresholds;
	}

	public static double getMaximum(ReflectogramEvent[] re)
	{
		double ret = 0;
		for(int i = 0; i < re.length; i++)
			for(int j = re[i].getBegin(); j < re[i].getEnd(); j++)
				if(re[i].refAmplitude(j) > ret)
					ret = re[i].refAmplitude(j);
		return ret;
	}
	
	public static double[] getMaxDifPM(double[] y, ModelFunction mf, int begin, int end)
	{
	    double[] ret = new double[2];
	    ret[0] = 0;
	    ret[1] = 0;
	    for (int i = begin; i < end; i++)
	    {
	        double dif = y[i] - mf.fun(i);
	        if (dif < ret[0])
	            ret[0] = dif;
	        if (dif > ret[1])
	            ret[1] = dif;
	    }
	    return ret;
	}

  // ?: the same thing is made inside ReflectogramComparer.doIt()... //saa
	public static void align(ReflectogramEvent[] y, ReflectogramEvent[] etalon)
	{
		double maxData = 0;
		double maxEtalon = 0;

		for(int i = etalon[0].getBegin(); i < etalon[0].getEnd(); i++)
			if(etalon[0].refAmplitude(i) > maxEtalon)
				maxEtalon = etalon[0].refAmplitude(i);

		for(int i = y[0].getBegin(); i < y[0].getEnd(); i++)
			if(maxData < y[0].refAmplitude(i))
				maxData = y[0].refAmplitude(i);

		double dA = maxEtalon - maxData;
		if (dA != 0)
			for(int i = 0; i < y.length; i++)
			{
				y[i].shiftY(dA);
		/*a1_connector += dA;
				y[i].a2_connector += dA;
				y[i].a_linear += dA;
				y[i].a_weld += dA;*/
			}
	}

	// XXX: unused?
	public static void align(double[] y, ReflectogramEvent[] etalon)
	{
		double maxData = 0;
		double maxEtalon = 0;

		for(int i = etalon[0].getBegin(); i < etalon[0].getEnd(); i++)
			if(etalon[0].refAmplitude(i) > maxEtalon)
				maxEtalon = etalon[0].refAmplitude(i);

		for(int i = 0; i < y.length; i++)
			if(maxData < y[i])
				maxData = y[i];

		double dA = maxEtalon - maxData;
		if (dA != 0)
		{
			for(int i = 0; i < etalon.length; i++)
				etalon[i].shiftY(dA);
		}
	}

	public static ReflectogramEvent[] alignClone(ReflectogramEvent[] y, ReflectogramEvent[] etalon)
	{
		ReflectogramEvent[] ret = new ReflectogramEvent[y.length];
		for(int i = 0; i < y.length; i++)
			ret[i] = y[i].copy();

		align(ret, etalon);
		return ret;
	}

	public static void correctEvents(ReflectogramEvent[] re)
	{
		for(int i = 0; i < re.length; i++)
			if(re[i].getEventType() == ReflectogramEvent.CONNECTOR)
			{
				if(re[i].getBegin() < 0)
					re[i].setBegin(0);

				if(i > 0)
					re[i-1].setEnd(re[i].getBegin());
				if(i < re.length-1)
					re[i+1].setBegin(re[i].getEnd());
			}
			// XXX: refit is necessary?? // solution depends on IA
	}

	public static ReflectogramEvent getEvent(int coord, ReflectogramEvent[] re)
	{
		if(re == null)
			return null;

		// ищем справа налево, чтобы отдать предпочтение [i].begin перед [i-1].end
		for(int i = re.length - 1; i >= 0; i--)
		{
			if(re[i].getBegin() <= coord && re[i].getEnd() >= coord)
				return re[i];
		}
		return null;
	}
	
	// takes amplitude with protection against for NullPointer //saa, 2004-10
	public static double getEventAmplitudeAt(int coord, ReflectogramEvent[] re)
	{
		ReflectogramEvent ev = getEvent(coord, re);
		if (ev == null)
			return 0; // XXX
		else
			return ev.refAmplitude(coord);
	}

	public static int getEventType(int coord, ReflectogramEvent[] re)
	{
		for(int i = 0; i < re.length; i++)
		{
			if(re[i].getBegin() <= coord && re[i].getEnd() >= coord)
				return re[i].getEventType();
		}
		return -1;
	}

	public static int getEventNumber(int coord, ReflectogramEvent[] re)
	{
		for(int i = 0; i < re.length; i++)
		{
			if(re[i].getBegin() <= coord && re[i].getEnd() >= coord)
				return i;
		}
		return -1;
	}

	public static int getLastSplash(ReflectogramEvent[] re)
	{
		for(int i = re.length - 1; i >= 0; i--)
			if(re[i].getEventType() == ReflectogramEvent.CONNECTOR)
				return re[i].getBegin();
		return 0;
	}

	public static int getReflectiveEventSize(double[] data, double level)
	{
		int eventSize = 0;
		int maxIndex = 4;

		for (int i = 0; i < Math.min(300, data.length); i++)
			if(data[i] > data[maxIndex])
				maxIndex = i;

		eventSize = maxIndex;
		for(int i = maxIndex; i < data.length; i++)
			if(data[i] < data[maxIndex] - level)
			{
				eventSize = i;
				break;
			}

		eventSize *= 0.6;
		if(eventSize < 4)
			eventSize = 4;

		return eventSize;
	}

	public static int getNonReflectiveEventSize(double[] data, int pulsewidth, double refraction, double resolution)
	{
		double firstLevel = 0.25;
		double secondLevel = 1.75;
		int firstPoint = 0;
		int secondPoint = 1;
		int maxIndex = 4;

		for (int i = 0; i < Math.min(300, data.length); i++)
			if(data[i] > data[maxIndex])
				maxIndex = i;

		for(int i = maxIndex; i < data.length; i++)
			if(data[i] < data[maxIndex] - firstLevel)
			{
				firstPoint = i;
				break;
			}

		for(int i = firstPoint + 1; i < data.length; i++)
			if(data[i] < data[maxIndex] - secondLevel)
			{
				secondPoint = i;
				break;
			}
		double[] d = MathRef.linearize2point(data, firstPoint, secondPoint);
		double eventSize = - 3d / d[0] + 150d / refraction * (double)pulsewidth / 1000d / resolution;
		if(eventSize < 2)
			eventSize = 2;
		return (int)eventSize;
	}

	/*
	public static double[] waveletTransform(double[] y, int freq, double[] norma, int wLet)
	{
		double[] trans = new double[y.length];
		for(int i = 0; i < y.length; i++)
		{
			double tmp = 0.;
			for(int j = Math.max(i - freq, 0); j < Math.min(i + freq + 1, y.length); j++)
				tmp = tmp + y[j] * wLet(j - i, freq, norma, wLet);
			trans[i] = tmp;
		}
		return trans;
	}
	*/
	public static double[] waveletTransform(double[] y, int freq, double norma, int wLet, int start, int end)
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

	public static double[] getDerivative(double[] y, int freq, int wLet)
	{
		double norma = getWLetNorma(freq, wLet);
		return waveletTransform(y, freq, norma, wLet, 0, y.length);
	}

	public static double getMaximalDifference(double[] etalon, double[] data, int begin, int end)
	{
		double shift = 0.;

		for(int i = begin; i < Math.min(Math.min(etalon.length, data.length), end + 1); i++)
			if(Math.abs(shift) < Math.abs(data[i] - etalon[i]))
				shift = data[i] - etalon[i];

		return shift;
	}
	/*
	public static double[] getWLetNorma(int freq)
	{
		double[] n = new double[10];
		for (int i = 0; i < 10; i++)
			n[i] = 0;

		for(int i = -freq; i <= freq; i++)
		{
			n[0] = n[0] + Math.abs(wLet1(i, freq, 1.));
			n[1] = n[1] + Math.abs(wLet2(i, freq, 1.));
			n[2] = n[2] + Math.abs(wLet3(i, freq, 1.));
			n[3] = n[3] + Math.abs(wLet4(i, freq, 1.));
			n[4] = n[4] + Math.abs(wLet5(i, freq, 1.));

			n[5] = n[5] + Math.abs(wLet6(i, freq, 1.));
			n[6] = n[6] + Math.abs(wLet7(i, freq, 1.));
			n[7] = n[7] + Math.abs(wLet8(i, freq, 1.));
			n[8] = n[8] + Math.abs(wLet9(i, freq, 1.));
			n[9] = n[9] + Math.abs(wLet10(i, freq, 1.));
		}
		for (int i = 0; i < 10; i++)
			n[i] /= 2.;

		return n;
	}
	*/

	static double getWLetNorma(int freq, int waveletType)
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

	/*
	public static double wLet(int arg, int freq, double[] norma, int waveletType)
	{
		switch (waveletType)
		{
			case  0: return  wLet1(arg, freq, norma[0]);
			case  1: return  wLet2(arg, freq, norma[1]);
			case  2: return  wLet3(arg, freq, norma[2]);
			case  3: return  wLet4(arg, freq, norma[3]);
			case  4: return  wLet5(arg, freq, norma[4]);
			case  5: return  wLet6(arg, freq, norma[5]);
			case  6: return  wLet7(arg, freq, norma[6]);
			case  7: return  wLet8(arg, freq, norma[7]);
			case  8: return  wLet9(arg, freq, norma[8]);
			case  9: return wLet10(arg, freq, norma[9]);
			default: return  wLet1(arg, freq, norma[0]);
		}
	}
	*/

	public static double wLet(int arg, int freq, double norma, int waveletType)
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

	static double wLet1(int arg, int freq, double norma)
	{
		return (Math.sin(arg * Math.PI / freq)) / norma;
	}

	static double wLet2(int arg, int freq, double norma)
	{
		return (Math.sin(arg * Math.PI / freq) * Math.abs((double)arg)) / norma;
	}

	static double wLet3(int arg, int freq, double norma)
	{
		return (Math.sin(arg * Math.PI / freq) / (1. + Math.abs((double)arg))) / norma;
	}

	static double wLet4(int arg, int freq, double norma)
	{
		return (Math.sin(arg * Math.PI / freq) / (1. + Math.sqrt(Math.abs((double)arg)))) / norma;
	}

	static double wLet5(int arg, int freq, double norma)
	{
		if(arg < 0)
			return -1. / norma;
		if(arg > 0)
			return 1. / norma;
		return 0.;
	}

	static double wLet6(int arg, int freq, double norma)
	{
		return (((double)arg) / freq) / norma;
	}

	static double wLet7(int arg, int freq, double norma)
	{
		if(arg > 0)
			return ((double)(freq - arg)) / norma;
		if(arg < 0)
			return -((double)(freq + arg)) / norma;
		return 0.;
	}

	static double wLet8(int arg, int freq, double norma)
	{
		return (Math.sin(arg * Math.PI / freq) + ((double)arg) / freq / 2.) / norma;
	}

	static double wLet9(int arg, int freq, double norma)
	{
		return (Math.sin(arg * Math.PI / freq / 2.))/norma;
	}

	static double wLet10(int arg, int freq, double norma)
	{
		return (Math.sin(arg * Math.PI / freq / 2.) - ((double)arg) / freq / 2.) / norma;
	}
}
