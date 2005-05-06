package com.syrus.AMFICOM.analysis.dadara;


public class ReflectogramMath
{
	private ReflectogramMath() { // static-only class
	}

	public static int getArrayMaxIndex(double[] yArr, int x0, int x1)
	{
		int ret = x0;
		for (int i = x0; i <= x1; i++)
			if (yArr[ret] < yArr[i])
				ret = i;
		return ret;
	}

	public static int getArrayMinIndex(double[] yArr, int x0, int x1)
	{
		int ret = x0;
		for (int i = x0; i <= x1; i++)
			if (yArr[ret] > yArr[i])
				ret = i;
		return ret;
	}

	public static double getArrayMax(double[] yArr)
	{
		return yArr[getArrayMaxIndex(yArr, 0, yArr.length - 1)];
	}

	public static double getArrayMin(double[] yArr)
	{
		return yArr[getArrayMinIndex(yArr, 0, yArr.length - 1)];
	}
	
	public static void updateMinArray(double[] acc, double[] arg)
	{
		int len = Math.min(acc.length, arg.length);
		for (int i = 0; i < len; i++)
		{
			if (acc[i] > arg[i])
				acc[i] = arg[i];
		}
	}
	public static void updateMaxArray(double[] acc, double[] arg)
	{
		int len = Math.min(acc.length, arg.length);
		for (int i = 0; i < len; i++)
		{
			if (acc[i] < arg[i])
				acc[i] = arg[i];
		}
	}
/*
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
*/
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
/*
// XXX: alignment: some old code
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
			}
	}

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
*/
	public static void alignArrayToEtalon(double[] y, final ModelTrace etalon)
	{
		int len = Math.min(y.length, etalon.getLength());
		if (len == 0)
			return;
		double[] et = etalon.getYArrayZeroPad(0, len);
		double maxDat = y[getArrayMaxIndex(y,  0, len - 1)];
		double maxEt = et[getArrayMaxIndex(et, 0, len - 1)];
		for (int i = 0; i < len; i++)
			y[i] += maxEt - maxDat;
	}

	public static ArrayModelTrace createAlignedArrayModelTrace(ModelTrace data, ModelTrace etalon)
	{
		double[] y = data.getYArray();
		alignArrayToEtalon(y, etalon);
		return new ArrayModelTrace(y);
	}
/*
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
			// refit is necessary?? // solution depends on IA
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
*/
	public static SimpleReflectogramEvent getEvent(int coord, SimpleReflectogramEvent[] re)
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
/*
	// takes amplitude with protection against for NullPointer //saa, 2004-10
	public static double getEventAmplitudeAt(int coord, ReflectogramEvent[] re)
	{
		ReflectogramEvent ev = getEvent(coord, re);
		if (ev == null)
			return 0;
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
*/
	public static int getEndOfTraceBegin(SimpleReflectogramEvent[] re)
	{
		// если есть событие "конец трассы", возвращаем его начало
		for(int i = re.length - 1; i >= 0; i--)
			if(re[i].getEventType() == SimpleReflectogramEvent.ENDOFTRACE)
				return re[i].getBegin();
		// если нет - считаем, что трассы совсем нет
		// XXX: м б тогда лучше начало м.з.?
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

	public static int getNonReflectiveEventSize(double[] data, double pulsewidth, double refraction, double resolution)
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
		double[] d = ReflectogramMath.linearize2point(data, firstPoint, secondPoint);
		double eventSize = - 3d / d[0] + 150d / refraction * pulsewidth / 1000d / resolution;
        System.out.println("eventSize = " + (- 3d / d[0]) + " + " + (150d / refraction * pulsewidth / 1000d / resolution));
		if(eventSize < 2)
			eventSize = 2;
		return (int)eventSize;
	}

	public static double[] getDerivative(double[] y, int freq, int wLet)
	{
		double norma = Wavelet.getNormMx(wLet, freq);
		return Wavelet.makeTransform(wLet, freq, y, 0, y.length - 1, norma);
	}

	public static double getMaximalDifference(double[] etalon, double[] data, int begin, int end)
	{
		double shift = 0.;

		for(int i = begin; i < Math.min(Math.min(etalon.length, data.length), end + 1); i++)
			if(Math.abs(shift) < Math.abs(data[i] - etalon[i]))
				shift = data[i] - etalon[i];

		return shift;
	}

	public static double[] linearize2point (double[] y, int begin, int end)
	{
		double res[] = new double[2];
		res[0] = (y[end] - y[begin]) / (end - begin);
		res[1] = y[begin] - res[0] * begin;
		return res;
	}

    public static double getPo(SimpleReflectogramEvent[] se,
            int i, ModelTrace mt) {
        if (se[i].getEventType() == SimpleReflectogramEvent.DEADZONE)
        {
            for (int j = i + 1; j < se.length; j++) {
                if (se[j].getEventType() == SimpleReflectogramEvent.LINEAR) {
                    int x1 = se[j].getBegin() + 1;
                    int x2 = se[j].getEnd() - 1;
                    if (x1 >= x2)
                    {
                        return mt.getY(x1);
                    }
                    else
                    {
                        double y1 = mt.getY(x1);
                        double y2 = mt.getY(x2);
                        return (x1 * y2 - x2 * y1) / (x1 - x2);
                    }
                }
            }
        }
        return mt.getY(se[i].getBegin());
    }
    public static int[] getEdzAdz(double po,
            SimpleReflectogramEvent sre, ModelTrace mt) {
        int adz = 0;
        int edz = 0;
        final int N = sre.getEnd() - sre.getBegin();
        double[] yarr = mt.getYArrayZeroPad(sre.getBegin(), N);
        // find max
        double vmax = po;
        for (int k = 0; k < N; k++) {
            if (vmax < yarr[k])
                vmax = yarr[k];
        }
        // find width
        for (int k = 0; k < N; k++) {
            if (yarr[k] > vmax - 1.5)
                edz++;
            if (yarr[k] > po + .5)
                adz++;
        }
        return new int[] { edz, adz };
    }
    public static double getMaxDev(double[] y,
            SimpleReflectogramEvent se, ModelTrace mt) {
        ModelTrace yMT = new ArrayModelTrace(y);
        return ReflectogramComparer.getMaxDeviation(mt, yMT, se);
    }
    public static double getRmsDev(double[] y,
            SimpleReflectogramEvent se, ModelTrace mt) {
        ModelTrace yMT = new ArrayModelTrace(y);
        return ReflectogramComparer.getRMSDeviation(mt, yMT, se);
    }
    public static double getYMin(SimpleReflectogramEvent ev, ModelTrace mt) {
        return getArrayMin(mt.getYRE(ev));
    }
    public static double getYMax(SimpleReflectogramEvent ev, ModelTrace mt) {
        return getArrayMax(mt.getYRE(ev));
    }
}
