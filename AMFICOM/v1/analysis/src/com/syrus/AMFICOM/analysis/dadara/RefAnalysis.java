package com.syrus.AMFICOM.analysis.dadara;

//import com.syrus.AMFICOM.Client.Analysis.MathRef;
//import com.syrus.AMFICOM.Client.Resource.Pool;

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

	public void decode (double[] y, ReflectogramEvent[] re)
	{
		events = new TraceEvent[re.length];

		if(re.length == 0)
			return;

		double max_y = 0; // XXX: saa: is 0 good for min/max? shall we use y[0] instead?
		double min_y = 0;
		for (int i = 0; i < y.length; i++)
		{
			if (max_y < y[i])
				max_y = y[i];
			if (min_y > y[i])
				min_y = y[i];
		}
		double top = max_y - min_y;

		int type;
		int last_point = re[re.length-1].getBegin(); // getBegin: changed acc. to Stas-2004-10 (was: getEnd)
		double Po = 0;

		for (int i = 0; i < re.length; i++)
		{
			if (i == 0)
				type = TraceEvent.INITIATE;
			else if (i == re.length - 1)
				type = TraceEvent.TERMINATE;
			else if (re[i].getEventType() == ReflectogramEvent.LINEAR)
				type = TraceEvent.LINEAR;
			else if (re[i].getEventType() == ReflectogramEvent.CONNECTOR)
				type = TraceEvent.CONNECTOR;
			else if (re[i].getEventType() == ReflectogramEvent.WELD)
				type = TraceEvent.WELD;
			else
				type = TraceEvent.NON_IDENTIFIED;

			events[i] = new TraceEvent(type, re[i].getBegin(), re[i].getEnd());

			/* -- removed --
    if (type == TraceEvent.LINEAR)
			{
				events[i].data = new double[5];
				events[i].data[0] = top - re[i].refAmplitude(re[i].getBegin());
				events[i].data[1] = top - re[i].refAmplitude(re[i].getEnd());
				events[i].data[2] = -re[i].b_linear;
				events[i].data[3] = re[i].a_linearError; // ср кв отклонение
				events[i].data[4] = re[i].a_linearError * (re[i].chi2Linear + 3.); // макс откл
			}
			else if (type == TraceEvent.CONNECTOR)
			{
				events[i].data = new double[4];
				events[i].data[0] = top - re[i].a1_connector;
				events[i].data[1] = top - re[i].a2_connector;
				events[i].data[2] = top - re[i].a1_connector - re[i].aLet_connector;
				events[i].data[3] = re[i].k_connector;
			}
			else if (type == TraceEvent.WELD)
			{
				events[i].data = new double[3];
				events[i].data[0] = top - re[i].refAmplitude(re[i].getBegin());
				events[i].data[1] = top - re[i].refAmplitude(re[i].getEnd());
				events[i].data[2] = events[i].data[1] - events[i].data[0];
			}
			else if (type == TraceEvent.TERMINATE)
			{
				events[i].data = new double[3];
				events[i].data[0] = top - re[i].a1_connector;
				events[i].data[1] = top - re[i].a1_connector - re[i].aLet_connector;
				events[i].data[2] = re[i].k_connector;
			}
			*/
			
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
				events[i].data[3] = re[i].getRMSDeviation(y);
				events[i].data[4] = re[i].getMaxDeviation(y);
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
					if (re[j].getEventType() == ReflectogramEvent.LINEAR) {
						Po = re[j].refAmplitude(0);
						break;
					}

				int adz = 0;
				int edz = 0;
				// find max
				double vmax = Po;
				for (int k = re[i].getBegin(); k < re[i].getEnd(); k++) {
					if (re[i].refAmplitude(k) > vmax)
						vmax = re[i].refAmplitude(k);
				}
				// find width // NOTE: changed a little by saa, 2004-07
				for (int k = re[i].getBegin(); k < re[i].getEnd(); k++) {
					//if(re[i].refAmplitude(k) > re[i].aLet_connector +
					// re[i].a1_connector - 1.5) -- OLD
					if (re[i].refAmplitude(k) > vmax - 1.5)
						edz++;
					if (re[i].refAmplitude(k) > Po + .5)
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

		double max_noise = 0.;
		boolean b = false;
		for(int i = re[re.length - 1].getEnd(); i < y.length; i++)
		{
			if (y[i] == min_y)
				b = true;
			if(b && max_noise < y[i])
				max_noise = y[i];
		}
		overallStats = new TraceEvent(TraceEvent.OVERALL_STATS, 0, re[re.length-1].getBegin());
		overallStats.data = new double[5];
		overallStats.data[0] = max_y - Po;
		overallStats.data[1] = max_y - y[last_point];
		overallStats.data[2] = (max_y - max_noise * 0.98);
		overallStats.data[3] = (max_y - Po);
		overallStats.data[4] = re.length;

		filtered = new double[y.length];
		noise = new double[y.length];
		max_noise = 0;


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
    int pos_from = re[i].getBegin();
    int pos_to = re[i].getEnd();
    for(int j = pos_from; j < pos_to; j++)
			{
				if(j < last_point) // XXX: saa: I think there should be '<='
				{
					filtered[j] = Math.max(0, re[i].refAmplitude(j));
					noise[j] = Math.abs(y[j] - filtered[j]);
					if (noise[j] > max_noise)
						max_noise = noise[j];
				}
				else
				{
					filtered[j] = y[j];
					noise[j] = max_noise;
				}
			}
		}
  //System.out.println("decode: after-processing (for/for) dt/ms " + (System.currentTimeMillis()-t0));
	}
}