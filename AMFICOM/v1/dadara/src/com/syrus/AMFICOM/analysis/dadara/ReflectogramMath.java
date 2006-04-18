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
	public static SimpleReflectogramEvent getEvent(int coord, SimpleReflectogramEvent[] re)
	{
		if(re == null)
			return null;

		// ���� ������ ������, ����� ������ ������������ [i].begin ����� [i-1].end
		for(int i = re.length - 1; i >= 0; i--)
		{
			if(re[i].getBegin() <= coord && re[i].getEnd() >= coord)
				return re[i];
		}
		return null;
	}
	/**
	 * ���������� ��������� ������ ������� "����� �������"
	 *   ���� 0, ���� ������ ������� ���.
	 * @param re ������ �������
	 * @return ��������� ������ ������� "����� �������"
	 *   ���� 0, ���� ������ ������� ���.
	 */
	public static int getEndOfTraceBegin(SimpleReflectogramEvent[] re)
	{
		// ���� ���� ������� "����� ������", ���������� ��� ������
		for(int i = re.length - 1; i >= 0; i--)
			if(re[i].getEventType() == SimpleReflectogramEvent.ENDOFTRACE)
				return re[i].getBegin();
		// ���� ��� - �������, ��� ������ ������ ���
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

	public static int getNonReflectiveEventSize(
//			double[] data,
			double pulsewidth,
			double refraction,
			double resolution)
	{
		if (pulsewidth <= 0)
			return 50; // XXX: default wavelet width when pulsewidth is unknown
		// XXX: �������, ��� ������ ����������� ����� ������������� ������������� ���������� ��������� ��. 400 ��
		// �������� ������ ������, ���������� �� ������ �� testDB
		final double minEffectivePulseWidth = 400;
		if (pulsewidth < minEffectivePulseWidth)
			pulsewidth = minEffectivePulseWidth;

		// @todo: ����� ������� � ������ PARS_REFACT (2006-04-xx), ������������
		// ReflectometryUtil.pulseWidthQPNanosecondsToEventLengthMeters()
		double eventSize = 150d / refraction * pulsewidth / 1000d / resolution;
		// ������ �������� ������ 2 �� ��������� ���� � ��������
		// �������� ������ 5 �� ������������� ��� ���������� � ������������� �������
		// 15 (5..20) - �� ����������� ������ ������ � ������������
		if(eventSize < 15)
			eventSize = 15;
		return (int)eventSize;
//		double firstLevel = 0.25;
//		double secondLevel = 1.75;
//		int firstPoint = 0;
//		int secondPoint = 1;
//		int maxIndex = 4;
//
//		for (int i = 0; i < Math.min(300, data.length); i++)
//			if(data[i] > data[maxIndex])
//				maxIndex = i;
//
//		for(int i = maxIndex + 1; i < data.length; i++)
//			if(data[i] < data[maxIndex] - firstLevel)
//			{
//				firstPoint = i - 1;
//				break;
//			}
//
//		for(int i = firstPoint + 1; i < data.length; i++)
//			if(data[i] < data[maxIndex] - secondLevel)
//			{
//				secondPoint = i;
//				break;
//			}
//		double[] d = ReflectogramMath.linearize2point(data, firstPoint, secondPoint);
//		double eventSize = - 3d / d[0] + 150d / refraction * pulsewidth / 1000d / resolution;
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
				// XXX: ���� �����, ���� (i + 1)-� ������� - �� ���. �������
				// �� � �������� Po ���� ������ ����� y1 DZ.
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
			// XXX: �������, ���� ���. ������� ���, ���� ������� y1, � �� y0.
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
	/**
	 * used to find RMS Value of noise
	 */
	public static double getRMSValue(double[]y, int iFrom, int iToEx, double y0) {
		double acc = 0;
		for (int i = iFrom; i < iToEx; i++) {
			acc += (y[i] - y0) * (y[i] - y0);
		}
		return y0 + Math.sqrt(acc / (iToEx - iFrom));
	}

	/**
	 * ���������� ������ ��� ��������� ������� �� RMS.
	 * ������ ����� ����� ������� �������� �� ������ RMS ������.
	 * @param ev �������, �� �������� �������� ���. �������
	 * @param mt ���������� ������
	 * @return ������ ��� ��������� ������� �� RMS
	 */
	public static double getRmsLoss(SimpleReflectogramEvent ev,
			ModelTrace mt) {
		int xBeg = ev.getBegin();
		int xEnd = ev.getEnd();
		double x0 = (xBeg + xEnd) / 2.0; // ��� ������ ��������
		double mxy = 0.0;
		double mxx = 0.0;
		double yA[] = mt.getYArray(xBeg, xEnd - xBeg + 1);
		for (int i = xBeg; i <= xEnd; i++) {
			final double vx = i - x0;
			final double vy = yA[i - xBeg]; //vy = mt.getY(i);
			mxx += vx * vx;
			mxy += vy * vx;
		}
		return -mxy / mxx * (xEnd - xBeg);
	}

	/**
	 * ����������, �������� �� �������� �� ������� �������.
	 * @param se �������
	 * @return true, ���� �� ������� ������� �������� ��������
	 */
	public static boolean isEventAnchorable(SimpleReflectogramEvent se) {
		switch (se.getEventType()) {
		case SimpleReflectogramEvent.DEADZONE:   // fall through
		case SimpleReflectogramEvent.ENDOFTRACE: // fall through
		case SimpleReflectogramEvent.CONNECTOR:  // fall through
		case SimpleReflectogramEvent.GAIN: // fall through
		case SimpleReflectogramEvent.LOSS:
			return true;
		default:
			return false;
		}
	}
}
