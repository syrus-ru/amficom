package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.Client.Analysis.MathRef;
import com.syrus.AMFICOM.Client.Resource.Pool;


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
		double max_y = 0;
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
		int last_point = re[re.length-1].begin;
		double Po = 0;

		events = new TraceEvent[re.length];

		for (int i = 0; i < re.length; i++)
		{
			if (i == 0)
				type = TraceEvent.INITIATE;
			else if (i == re.length - 1)
				type = TraceEvent.TERMINATE;
			else if (re[i].getType() == ReflectogramEvent.LINEAR)
				type = TraceEvent.LINEAR;
			else if (re[i].getType() == ReflectogramEvent.CONNECTOR)
				type = TraceEvent.CONNECTOR;
			else if (re[i].getType() == ReflectogramEvent.WELD)
				type = TraceEvent.WELD;
			else
				type = TraceEvent.NON_IDENTIFIED;

			events[i] = new TraceEvent(type, re[i].begin, re[i].end);

			if (type == TraceEvent.LINEAR)
			{
				events[i].data = new double[5];
				events[i].data[0] = top - re[i].refAmpl(re[i].begin)[0];
				events[i].data[1] = top - re[i].refAmpl(re[i].end)[0];
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
				events[i].data[0] = top - re[i].refAmpl(re[i].begin)[0];
				events[i].data[1] = top - re[i].refAmpl(re[i].end)[0];
				events[i].data[2] = events[i].data[1] - events[i].data[0];
			}
			else if (type == TraceEvent.TERMINATE)
			{
				events[i].data = new double[3];
				events[i].data[0] = top - re[i].a1_connector;
				events[i].data[1] = top - re[i].a1_connector - re[i].aLet_connector;
				events[i].data[2] = re[i].k_connector;
			}
			else if (type == TraceEvent.INITIATE)
			{
				for (int j = 1; j < re.length; j++)
					if (re[j].getType() == ReflectogramEvent.LINEAR)
					{
						Po = re[j].refAmpl(0)[0];
						break;
					}

				int adz=0;
				int edz=0;
				for(int k = re[i].begin; k < re[i].end; k++)
				{
					if(re[i].refAmpl(k)[0] > re[i].aLet_connector + re[i].a1_connector - 1.5)
						edz++;
					if(re[i].refAmpl(k)[0] > Po + .5)
						adz++;
				}

				events[i].data = new double[4];
				events[i].data[0] = top - (Po + re[i].a2_connector) / 2; //amplitude
				events[i].data[1] = top - Po; // Po
				events[i].data[2] = edz;
				events[i].data[3] = adz;
			}
			else if (type == TraceEvent.NON_IDENTIFIED)
			{
				events[i].data = new double[3];
				events[i].data[0] = top - re[i].refAmpl(re[i].begin)[0];
				events[i].data[1] = top - re[i].refAmpl(re[i].end)[0];
				events[i].data[2] = events[i].data[1] - events[i].data[0]; //eventMaxDeviation

			}
		}

		double max_noise = 0.;
		boolean b = false;
		for(int i = re[re.length - 1].end; i < y.length; i++)
		{
			if (y[i] == min_y)
				b = true;
			if(b && max_noise < y[i])
				max_noise = y[i];
		}
		overallStats = new TraceEvent(TraceEvent.OVERALL_STATS, 0, re[re.length-1].begin);
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

		for(int i = 0; i < re.length; i++)
		{
			for(int j = re[i].begin; j < y.length; j++)
			{
				if(j < last_point)
				{
					filtered[j] = Math.max(0, re[i].refAmpl(j)[0]);
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
	}
}