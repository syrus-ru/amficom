package com.syrus.AMFICOM.analysis.dadara;


public class RefAnalysis
{
	public TraceEvent[] events;
	public TraceEvent[] concavities;
	public double[] noise;
	public double[] filtered;
	public TraceEvent overallStats;

	public RefAnalysis()
	{
	}

	public void decode (double[] y, ModelTraceManager mtm)
	{
		ComplexReflectogramEvent[] re = mtm.getComplexEvents();
		events = new TraceEvent[re.length];

		if(re.length == 0)
			return;

		double maxY = 0; // XXX: saa: is 0 good for min/max? shall we use y[0] instead?
		double minY = 0;
		for (int i = 0; i < y.length; i++)
		{
			if (maxY < y[i])
				maxY = y[i];
			if (minY > y[i])
				minY = y[i];
		}
		double top = maxY - minY;

		int type;
		int lastPoint = re.length > 0
			? re[re.length - 1].getBegin() // getBegin: changed acc. to Stas-2004-10 (was: getEnd)
			: 0;

		double Po = 0;

		for (int i = 0; i < re.length; i++)
		{
			if (i == 0)
				type = TraceEvent.INITIATE;
			else if (i == re.length - 1)
				type = TraceEvent.TERMINATE;
			else switch (re[i].getEventType())
			{
			case SimpleReflectogramEvent.LINEAR:
				type = TraceEvent.LINEAR;
				break;
			case SimpleReflectogramEvent.CONNECTOR:
				type = TraceEvent.CONNECTOR;
				break;
			case SimpleReflectogramEvent.SPLICE:
				type = TraceEvent.WELD;
				break;
			default:
				type = TraceEvent.NON_IDENTIFIED;
			}

			events[i] = new TraceEvent(type, re[i].getBegin(), re[i].getEnd());

			// определяем "асимптотические" значения слева и справа

			double asympB = re[i].getAsympY0();
			double asympE = re[i].getAsympY1();

			if (type == TraceEvent.LINEAR) {
			    // TODO: использовать mloss вместо asympB,asympE,
			    // т.к. (нынешние) asympB,asympE зависят от смежных участков.
				events[i].data = new double[5];
				events[i].data[0] = top - asympB;
				events[i].data[1] = top - asympE;
				events[i].data[2] = -(asympE - asympB)
						/ (re[i].getEnd() - re[i].getBegin());
				ModelTrace yMT = new ArrayModelTrace(y); 
				events[i].data[3] = ReflectogramComparer.getRMSDeviation(mtm.getModelTrace(), yMT, re[i]);
				events[i].data[4] = ReflectogramComparer.getMaxDeviation(mtm.getModelTrace(), yMT, re[i]);
			} else if (type == TraceEvent.CONNECTOR) {
				events[i].data = new double[4];
				events[i].data[0] = top - asympB;
				events[i].data[1] = top - asympE;
				events[i].data[2] = events[i].data[0]
						- re[i].getALet(); // XXX
				events[i].data[3] = 0; // FIXIT: больше не используется, убрать
			} else if (type == TraceEvent.WELD) {
				events[i].data = new double[3];
				events[i].data[0] = top - asympB;
				events[i].data[1] = top - asympE;
				events[i].data[2] = events[i].data[1] - events[i].data[0];
			} else if (type == TraceEvent.TERMINATE) {
				events[i].data = new double[3];
				events[i].data[0] = top - asympB;
				events[i].data[1] = events[i].data[0]
						- re[i].getALet(); // XXX
				events[i].data[2] = 0; // FIXIT: больше не используется, убрать
			} else if (type == TraceEvent.INITIATE) {
				// extrapolate first linear event to x = 0
				for (int j = 1; j < re.length; j++)
					if (re[j].getEventType() == SimpleReflectogramEvent.LINEAR) {
						int x1 = re[j].getBegin() + 1;
						int x2 = re[j].getEnd() - 1;
						if (x1 >= x2)
						{
							Po = mtm.getY(x1);
						}
						else
						{
							double y1 = mtm.getY(x1);
							double y2 = mtm.getY(x2);
							Po = (x1 * y2 - x2 * y1) / (x1 - x2);
						}
						break;
					}

				int adz = 0;
				int edz = 0;
				// find max
				double vmax = Po;
				for (int k = re[i].getBegin(); k < re[i].getEnd(); k++) {
					if (vmax < mtm.getY(k))
						vmax = mtm.getY(k);
				}
				// find width // NOTE: changed a little by saa, 2004-07
				for (int k = re[i].getBegin(); k < re[i].getEnd(); k++) {
					//if(re[i].refAmplitude(k) > re[i].aLet_connector +
					// re[i].a1_connector - 1.5) -- OLD
					if (mtm.getY(k) > vmax - 1.5)
						edz++;
					if (mtm.getY(k) > Po + .5)
						adz++;
				}

				events[i].data = new double[4];
				//events[i].data[0] = top - (Po + re[i].a2_connector) / 2;
				// //amplitude --- ??? -- XXX
				events[i].data[0] = top - vmax; // changed by saa
				events[i].data[1] = top - Po; // Po
				events[i].data[2] = edz;
				events[i].data[3] = adz;
			} else if (type == TraceEvent.NON_IDENTIFIED) {
				events[i].data = new double[3];
				events[i].data[0] = top - asympB;
				events[i].data[1] = top - asympE;
				events[i].data[2] = events[i].data[1] - events[i].data[0]; //eventMaxDeviation
			}
		}

		double maxNoise = 0.;
		boolean b = false;
		for(int i = re[re.length - 1].getEnd(); i < y.length; i++)
		{
			if (y[i] == minY)
				b = true;
			if(b && maxNoise < y[i])
				maxNoise = y[i];
		}
		overallStats = new TraceEvent(TraceEvent.OVERALL_STATS, 0, re[re.length-1].getBegin());
		overallStats.data = new double[5];
		overallStats.data[0] = maxY - Po;
		overallStats.data[1] = maxY - y[lastPoint];
		overallStats.data[2] = (maxY - maxNoise * 0.98);
		overallStats.data[3] = (maxY - Po);
		overallStats.data[4] = re.length;

		filtered = new double[y.length];
		noise = new double[y.length];
		maxNoise = 0;


//		noise = (double[])Pool.get("doublearray", "noise");
//		//noise = ReflectogramMath.getDerivative(noise, 3, 0);
//				//MathRef.getDerivative(y);
//		for (int i = 0; i < noise.length; i++)
//			noise[i] = Math.abs(noise[i]);
//
//		for (int i = 1; i < noise.length - 1; i++)
//			filtered[i] = y[i] - noise[i - 1];
//

		//long t0 = System.currentTimeMillis();
  for(int i = 0; i < re.length; i++)
		{
			// for(int j = re[i].getBegin(); j < y.length; j++) -- changed just because was too slow -- saa
	int posFrom = re[i].getBegin();
	int posTo = re[i].getEnd();
	for(int j = posFrom; j < posTo; j++)
			{
				if(j < lastPoint) // XXX: saa: I think there should be '<='
				{
					filtered[j] = Math.max(0, mtm.getY(j));
					noise[j] = Math.abs(y[j] - filtered[j]);
					if (noise[j] > maxNoise)
						maxNoise = noise[j];
				}
				else
				{
					filtered[j] = y[j];
					noise[j] = maxNoise;
				}
			}
		}
  //System.out.println("decode: after-processing (for/for) dt/ms " + (System.currentTimeMillis()-t0));
	}
}
