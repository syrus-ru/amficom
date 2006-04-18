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

		// ищем справа налево, чтобы отдать предпочтение [i].begin перед [i-1].end
		for(int i = re.length - 1; i >= 0; i--)
		{
			if(re[i].getBegin() <= coord && re[i].getEnd() >= coord)
				return re[i];
		}
		return null;
	}
	/**
	 * Возвращает дистанцию начала события "конец волокна"
	 *   либо 0, если такого события нет.
	 * @param re список событий
	 * @return дистанция начала события "конец волокна"
	 *   либо 0, если такого события нет.
	 */
	public static int getEndOfTraceBegin(SimpleReflectogramEvent[] re)
	{
		// если есть событие "конец трассы", возвращаем его начало
		for(int i = re.length - 1; i >= 0; i--)
			if(re[i].getEventType() == SimpleReflectogramEvent.ENDOFTRACE)
				return re[i].getBegin();
		// если нет - считаем, что трассы совсем нет
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
		// XXX: считаем, что полоса пропускания цепей рефлектометра соответствует разрешению импульсом ок. 400 нс
		// довольно грубая оценка, основанная на тестах по testDB
		final double minEffectivePulseWidth = 400;
		if (pulsewidth < minEffectivePulseWidth)
			pulsewidth = minEffectivePulseWidth;

		// @todo: после слияния с веткой PARS_REFACT (2006-04-xx), использовать
		// ReflectometryUtil.pulseWidthQPNanosecondsToEventLengthMeters()
		double eventSize = 150d / refraction * pulsewidth / 1000d / resolution;
		// ширина вейвлета меньше 2 не допустимо быть в принципе
		// значения меньше 5 не рекомендуются как приводящие к неустойчивому анализу
		// 15 (5..20) - на проведенных тестах близко к оптимальному
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
				// XXX: быть может, если (i + 1)-е событие - не лин. участок
				// то в качестве Po надо просто брать y1 DZ.
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
			// XXX: пожалуй, если лин. участка нет, надо вернуть y1, а не y0.
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
	 * Определяет потери для линейного события по RMS.
	 * Потери равны длине события умножить на наклон RMS прямой.
	 * @param ev событие, по которому проводим лин. участок
	 * @param mt фитируемая кривая
	 * @return потери для линейного события по RMS
	 */
	public static double getRmsLoss(SimpleReflectogramEvent ev,
			ModelTrace mt) {
		int xBeg = ev.getBegin();
		int xEnd = ev.getEnd();
		double x0 = (xBeg + xEnd) / 2.0; // это строго середина
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
	 * Определяет, возможна ли привязка по данному событию.
	 * @param se событие
	 * @return true, если по данному событию привязка возможна
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
