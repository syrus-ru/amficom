package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.Client.Analysis.MathRef;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.*;
import java.util.*;

public class InitialAnalysis
{
	double[] data;
	double delta_x;
	double minimalThreshold;
	double minimalWeld;
	double minimalConnector;
	double minimalEndingSplash;
	double maximalNoise;
	int waveletType;
	double formFactor;

	int lastNonZeroPoint;

	double wn_c;
	double wn_w;
	int evSizeC;
	int evSizeW;
	double[] transC;
	double[] transW;
	double[] noise;
	double meanAttenuation = 0;

	public InitialAnalysis(
		double[] data,					  	//точки рефлектограммы
		double delta_x,				    	//расстояние между точками (м)
		double minimalThreshold,		//минимальный уровень события
		double minimalWeld,			  	//минимальный уровень неотражательного события
		double minimalConnector,		//минимальный уровень отражательного события
		double minimalEndingSplash,	//минимальный уровень последнего отражения
		double maximalNoise,				//максимальный уровень шума
		int waveletType,				  	//номер используемого вейвлета
		double formFactor,			  	//формфактор отражательного события
		int reflectiveSize,					//характерный размер отражательного события
		int nonReflectiveSize)			//характерный размер неотражательного события
	{
		this.data					       = data;
		this.delta_x				     = delta_x;
		this.minimalThreshold		 = minimalThreshold;
		this.minimalWeld			   = minimalWeld;
		this.minimalConnector		 = minimalConnector;
		this.minimalEndingSplash = minimalEndingSplash;
		this.maximalNoise			   = maximalNoise;
		this.waveletType			   = waveletType;
		this.formFactor			     = formFactor;
		this.evSizeC 						 = reflectiveSize;
		this.evSizeW 						 = nonReflectiveSize;
	}

	public ReflectogramEvent[] performAnalysis()
	{
		lastNonZeroPoint = getLastPoint();

		// характерный размер отражательного события (ширина вейвлета)
		//evSizeC = ReflectogramMath.getReflectiveEventSize(data, 0.5);
		// характерный размер неотражательного события
		//evSizeW = ReflectogramMath.getNonReflectiveEventSize(data, 1000, 1.467, 8);
		//evSizeW = 3 * evSizeC / 5;
		// ширина вспомогательного вейвлета для лучшей локализации отражательных событий

		wn_c = ReflectogramMath.getWLetNorma(evSizeC, waveletType);
		wn_w = ReflectogramMath.getWLetNorma(evSizeW, waveletType);

		// вычисляем уровень шума
		noise = getNoise(data, evSizeC);

		/*double[] denoised = new double[data.length];
		for (int i = 0; i < data.length; i++)
			denoised[i] = data[i] - noise[i];*/

		// выполняем вейвлет-преобразование
		transC = ReflectogramMath.waveletTransform(data, evSizeC, wn_c, waveletType, 0, lastNonZeroPoint);
		transW = ReflectogramMath.waveletTransform(data, evSizeW, wn_w, waveletType, 0, lastNonZeroPoint);

		// вычитаем из коэффициентов преобразования(КП) постоянную составляющую (среднее затухание)
		meanAttenuation = shiftToZeroAttenuation(transC);
		shiftToZeroAttenuation(transW);

		// устанавливаем в 0 КП, которые меньше уровня шума или минимального уровня события
		setNonZeroTransformation(transC, minimalThreshold, 0, transC.length);
		setNonZeroTransformation(transW, minimalThreshold, 0, transW.length);

		LinkedList events = new LinkedList();
		// определяем координаты и типы событий по КП
		findConnectors(
				events,
				transC,
				transW,
				0,
				lastNonZeroPoint - 1);//Math.min(lastNonZeroPoint + evSizeC, data.length - 10));

		// корректируем координаты отражательных событий
		//correctConnectorsCoords(events);

		double[] data_woc = excludeConnectors(events, data);
		transW = ReflectogramMath.waveletTransform(data_woc, evSizeW, wn_w, waveletType, 0, lastNonZeroPoint);
		shiftToZeroAttenuation(transW);
		setNonZeroTransformation(transW, minimalThreshold, 0, transW.length);
//		excludeMinimums(transW,
//										((ReflectogramEvent)events.get(0)).end,//7542,//
//										((ReflectogramEvent)events.get(events.size() - 1)).begin);//7550);//

		Pool.put("wavelet", "primarytrace", transC);
		Pool.put("doublearray", "noise", noise);
		Pool.put("wavelet", "deconnector", data_woc);
		Pool.put("wavelet", "primarytracenarrow", noise);

		findWelds(events, transW);
		siewLinearParts(events);
		correctWeldCoords(events);

//    // исключаем неидентифицированные события
//    excludeNonRecognizedEvents();

		// исключаем события с длиной, меньшей половины характерного размера
		excludeShortEvents(events, Math.max(evSizeW/2, 10), Math.max(evSizeW/2, 3), Math.max(evSizeC/2, 4));
		siewLinearParts(events);

		setEventParams(events, evSizeC);

		// корректируем конец волокна согласно минимального отражения
		correctEnd(events, evSizeC);
		return (ReflectogramEvent[])events.toArray(new ReflectogramEvent[events.size()]);
	}

	public double getMeanAttenuation()
	{
		return meanAttenuation;
	}

	int getLastPoint()
	{
		int lastPoint = data.length-1;
		for(int i = 300; i < data.length; i++)
			if(data[i] < 1.)
			{
				lastPoint = i;
				break;
			}
		if(lastPoint + 10 < data.length)
			lastPoint += 10;
		return lastPoint;
	}

	double[] getNoise(double[] data, int freq)
	{
		// First, we set that noise is euqal to the first derivative.
		double[] noise = new double[lastNonZeroPoint];

		for(int i = 0; i < lastNonZeroPoint - 1; i++)
		{
			noise[i] = Math.abs(data[i] - data[i+1]);
				if (noise[i] > maximalNoise)
					noise[i] = maximalNoise;
		}
		//noise[data.length - 1] = 0.;

		double EXP;
		// Cut of the prompt-peaks with exponent.
		for(int i = 0; i < lastNonZeroPoint; i++)
		{
			EXP = (Math.exp((double)i / lastNonZeroPoint) - 1.) * maximalNoise / (Math.exp(1.) - 1.);
				if (noise[i] > EXP)
					noise[i] = EXP;
		}

		convolutionOfNoise(noise, freq);
		return noise;
	}

	void convolutionOfNoise(double[] noise, int n_points)
	{
		for(int i = 0; i < lastNonZeroPoint; i++)
		{
			int n = 0;
			double meanValue = 0.;
			for (int j = i; j < Math.min(i + n_points, lastNonZeroPoint); j++)
			{
				meanValue += noise[j];
				n++;
			}
			if (n > 0)
				meanValue /= n;

			noise[i] = meanValue;
		}
	}

	double shiftToZeroAttenuation(double[] trans)
	{
		//возможное затухание находится в пределах [0; -0.5] дБ
		Histogramm histo = new Histogramm(-0.5, 0, 100);
		double[] y = histo.init(trans, 0, lastNonZeroPoint);
		double meanAtt = histo.getMaximumValue();

		for(int i=0; i < lastNonZeroPoint; i++)
			trans[i] -= meanAtt;

		return meanAtt;
	}

	void setNonZeroTransformation(double[] trans, double threshold, int start, int end)
	{
		for(int i = start; i < end; i++)
		{
			if(Math.abs(trans[i]) < Math.max(threshold, noise[i] / 2d))
				trans[i] = 0d;
		}

		//Excluding of the exidental zero points;
		for(int i = start; i < Math.min(end, trans.length - 2); i++)
		{
			if(Math.abs(trans[i]) < threshold)
			{
				if(Math.abs(trans[i-1]) > threshold && Math.abs(trans[i+1]) > threshold)
					trans[i] = (trans[i-1] + trans[i+1]) / 2.;
			}
		}


	}

	void excludeMinimums(double[] trans, int start, int end)
	{
		int[] minimums = MathRef.findLocalAbsMinimumPoints(trans, start, end);
		for (int i = 0; i < minimums.length; i++)
			trans[minimums[i]] = 0d;
	}

	void findConnectors(LinkedList events, double[] trans, double[] correct, int start, int end)
	{
		int halfWidth = evSizeC / 2;
		if(halfWidth < 1)
			halfWidth = 1;

		int type;
		int x1; // начало события
		int x2; // конец события

		int k1; // середина первого участка
		int k2; // середина второго участка
		int k3; // середина третьего участка
		int c1; // точка максимума первого участка
		int c2; // точка максимума второго участка
		int c3; // точка максимума третьего участка
		int j; // конец первого участка
		int k; // конец второго участка
		int s; // конец третьего участка

		for(int i = start; i < end; i += (x2 - x1))
		{
			j = i + 1;
			c1 = j;
			while(j < end && (MathRef.sign(trans[i]) == MathRef.sign(trans[j])))
			{
				if(Math.abs(trans[c1]) < Math.abs(trans[j]))
					c1 = j;
				j++;
			}
			k = j + 1;
			c2 = k;
			while(k < end && (MathRef.sign(trans[j]) == MathRef.sign(trans[k])))
			{
				if(Math.abs(trans[c2]) < Math.abs(trans[k]))
					c2 = k;
				k++;
			}
			s = k + 1;
			c3 = s;
			while(s < end && (MathRef.sign(trans[k]) == MathRef.sign(trans[s])))
			{
				if(Math.abs(trans[c3]) < Math.abs(trans[s]))
					c3 = s;
				s++;
			}
			k1 = (j + i) / 2;
			k2 = (k + j) / 2;
			k3 = (s + k) / 2;

			x1 = i;
			if(Math.abs(trans[c1]) < minimalThreshold) // linear part
			{
				type = ReflectogramEvent.LINEAR;
				x2 = j;
			}
			else if(trans[c1] > minimalConnector &&
							trans[c2] < -minimalConnector)  // connector
			{
				type = ReflectogramEvent.CONNECTOR;
				x2 = k;
				for (int ii = c2; ii < s; ii++)
					if (correct != null && (Math.abs(correct[ii]) < minimalWeld))// || MathRef.sign(correct[ii]) != MathRef.sign(correct[ii-1])))
					{
						x2 = ii;
						break;
					}
				int infl = MathRef.findFirstAbsMinimumPoint(correct, c2, s);
				int constant = MathRef.findConstantPoint(correct, c2, s);
				x2 = Math.min(constant, Math.min(infl, x2));
			}
			else if(trans[c1] > minimalConnector &&
							Math.abs(trans[c2]) < minimalThreshold &&
							trans[c3] < -minimalConnector &&
							k - j < (int)(halfWidth * 1.5)) //connector
			{
				type = ReflectogramEvent.CONNECTOR;
				x2 = s;
				for (int ii = c3; ii < s; ii++)
					if (correct != null && (Math.abs(correct[ii]) < minimalWeld))// || MathRef.sign(correct[ii]) != MathRef.sign(correct[ii-1])))
					{
						x2 = ii;
						break;
					}
				int infl = MathRef.findFirstAbsMinimumPoint(correct, c3, s);
				int constant = MathRef.findConstantPoint(correct, c3, s);
				x2 = Math.min(constant, Math.min(infl, x2));
			}
			else if(trans[c1] > minimalWeld * .8 ||
							trans[c1] < -minimalWeld * .8) //weld
			{
				type = ReflectogramEvent.WELD;
				x2 = j;
			}
			else //linear
			{
				type = ReflectogramEvent.LINEAR;
				x2 = j;
			}

			if (x1 < lastNonZeroPoint && type == ReflectogramEvent.CONNECTOR)
			{
				ReflectogramEvent ep = new ReflectogramEvent();
				ep.setType(type);
				ep.begin = x1;
				if (i != 0)
				{
					ep.begin = c1 + (int)(0.6 * evSizeW);
					for (int ii = x1; ii < c1; ii++)
						if (correct != null && Math.abs(correct[ii]) > minimalConnector)
						{
							ep.begin = ii + (int)(0.6 * evSizeW);
							break;
						}
				}
				ep.end = x2;// - (int)(0.5 * evSizeW);
				events.add(ep);
			}
		}
	}

	void findWelds(LinkedList events, double[] trans)
	{
		int halfWidth = evSizeW / 2;
		if(halfWidth < 1)
			halfWidth = 1;

		int type;
		int x1; // начало события
		int x2; // конец события

		int k1; // середина первого участка
		int k2; // середина второго участка
		int k3; // середина третьего участка
		int c1; // точка максимума первого участка
		int c2; // точка максимума второго участка
		int c3; // точка максимума третьего участка

		int j;
		int k;
		int s;

		LinkedList clone = null;
		synchronized(this)
		{
			clone = (LinkedList)events.clone();
		}

		int counter = 0;
		for (int r = 0; r < clone.size() - 1; r++)
		{
			ReflectogramEvent connector = (ReflectogramEvent)clone.get(r);
			ReflectogramEvent connector_next = (ReflectogramEvent)clone.get(r+1);
			int start = connector.end;
			int end = connector_next.begin;

			double meanNoise = 0;
			for (int i = start; i < end; i++)
				meanNoise += Math.abs(noise[i]);
			meanNoise /= (double)(end - start);
			setNonZeroTransformation(trans, meanNoise * 0.8, start, end);

			for(int i = start; i < end; i += (x2 - x1))
			{
				j = i + 1;
				c1 = j;
				while(MathRef.sign(trans[i]) == MathRef.sign(trans[j])
							&& j < end)
				{
					if(Math.abs(trans[c1]) < Math.abs(trans[j]))
						c1 = j;
					j++;
				}
				k = j + 1;
				c2 = k;
				while(MathRef.sign(trans[j]) == MathRef.sign(trans[k])
							&& k < end)
				{
					if(Math.abs(trans[c2]) < Math.abs(trans[k]))
						c2 = k;
					k++;
				}
				s = k + 1;
				c3 = s;
				while(MathRef.sign(trans[k]) == MathRef.sign(trans[s])
							 && s < end)
				{
					if(Math.abs(trans[c3]) < Math.abs(trans[s]))
						c3 = s;
					s++;
				}
				k1 = (j + i) / 2;
				k2 = (k + j) / 2;
				k3 = (s + k) / 2;

				x1 = i;
				if(Math.abs(trans[c1]) < minimalThreshold) // linear part
				{
					type = ReflectogramEvent.LINEAR;
					x2 = j;
				}
				else if(trans[c1] > minimalWeld * .8 ||
								 trans[c1] < -minimalWeld * .8)
				 //&&	 j - i < 3 * evSizeC)//weld
				{
					type = ReflectogramEvent.WELD;
					x2 = j;
				}
				else if(trans[c1] > noise[k1] * 3 &&
								trans[c3] < -noise[k3] * 3 &&
								trans[c1] > minimalConnector &&
								trans[c3] < -minimalConnector &&
								k - j < (int)(halfWidth * 1.5) &&
								Math.abs(trans[c2]) < .00001)//reflection (connector, anyway)
				{
					type = ReflectogramEvent.SINGULARITY;
					x2 = s;
				}
				else if(trans[c1] > minimalConnector &&
								trans[c2] < minimalConnector &&
								Math.abs((trans[c1] + trans[c2]) / (trans[c1] - trans[c2])) < .5) //reflection
				{
					type = ReflectogramEvent.SINGULARITY;
					x2=k;
				}
				else if(trans[c1] > minimalConnector &&
								trans[c3] < minimalConnector &&
								Math.abs((trans[c1] + trans[c3]) / (trans[c1] - trans[c3])) < .5 &&
								k - j < (int)(halfWidth * 1.5))  //reflection
				{
					type = ReflectogramEvent.SINGULARITY;
					x2 = s;
				}
				else //linear
				{
					type = ReflectogramEvent.LINEAR;
					x2 = j;
				}

				if (x1 < lastNonZeroPoint)
				{
					counter++;
					ReflectogramEvent ep = new ReflectogramEvent();
					ep.setType(type);
					ep.begin = x1;
					ep.end = x2;
					events.add(counter + r, ep);
				}
			}
		}
		ReflectogramEvent[] ev = (ReflectogramEvent[])events.toArray(new ReflectogramEvent[events.size()]);
		int i = ev.length;
	}

	double[] excludeConnectors(LinkedList events, double[] data)
	{
		double[] data_woc = new double[data.length];
		for (int i = 0; i < data.length; i++)
			data_woc[i] = data[i];

		double delta = 0;
		double[] _d = null;
		for (int i = 1; i < events.size(); i++)
		{
			ReflectogramEvent ep = (ReflectogramEvent)events.get(i);
			ReflectogramEvent ep_last = (ReflectogramEvent)events.get(i-1);
			double d[] = MathRef.linearize2point(data, ep_last.end, ep.begin);

			if (_d != null)
			{
				delta += ((data[ep_last.begin] + _d[0] * (ep_last.end - ep_last.begin)) - data[ep_last.end]);
				for (int j = ep_last.end; j < ep.begin; j++)
					data_woc[j] += delta;
			}
			else
			{
				for (int j = ep_last.begin; j < ep_last.end; j++)
					data_woc[j] = data[ep_last.end] + d[0] * (j - ep_last.end);
			}
			_d = d;

			for (int j = ep.begin; j < ep.end; j++)
				data_woc[j] = data[ep.begin] + d[0] * (j - ep.begin) + delta;
		}
		return data_woc;
	}

	void correctConnectorsCoords(LinkedList events)
	{
		for (int i = 1; i < events.size(); i++)
		{
			ReflectogramEvent ep = (ReflectogramEvent)events.get(i);
			ReflectogramEvent ep_last = (ReflectogramEvent)events.get(i-1);
			if (ep.begin - ep_last.end < 1.5 * evSizeW)
			{
				ep.begin -= Math.min(0.5 * evSizeW, 2 * (ep.begin - ep_last.end) / 5);
				ep_last.end = ep.begin;
			}
		}
	}

	void correctWeldCoords(LinkedList events)
	{
		for (int i = 0; i < events.size(); i++)
		{
			ReflectogramEvent ep = (ReflectogramEvent)events.get(i);
			if (ep.getType() == ReflectogramEvent.WELD)
			{
				if (i > 0)
				{
					// передний фронт
					ReflectogramEvent ep_last = (ReflectogramEvent)events.get(i-1);
					if (ep_last.getType() == ReflectogramEvent.LINEAR &&
							ep_last.end - ep_last.begin > 3)
					{
						double[] d = MathRef.linearize2point(data, ep_last.begin, ep_last.end);
						for(int j = ep.begin; j < (ep.begin + ep.end) / 2; j++)
						{
							if(Math.abs(data[j] - (d[1] + d[0] * j)) > //Math.max(minimalWeld, Math.abs(3 * noise[j])))
								 Math.abs(2 * noise[j]))
							{
								ep.begin = j;
								if (ep.end - ep.begin < evSizeW)
									ep.begin = ep.end - evSizeW;
								ep_last.end = ep.begin;
								if (ep_last.end <= ep_last.begin)
									ep_last.begin = ep_last.end - 1;
								break;
							}
						}
					}
				}
				if (i < events.size() - 1)
				{
					ReflectogramEvent ep_next = (ReflectogramEvent)events.get(i+1);
					if (ep_next.getType() == ReflectogramEvent.LINEAR &&
							ep_next.end - ep_next.begin > 3)
					{
						double[] d = MathRef.linearize2point(data, ep_next.begin, ep_next.end);
						for(int j = ep.end; j > ep.begin; j--)
						{
							if (Math.abs(data[j] - (d[1] + d[0] * j)) > //Math.max(minimalWeld, Math.abs(3 * noise[j])))
									Math.abs(2 * noise[j]))
							{
								ep.end = j;
								if (ep.end - ep.begin < evSizeW)
									ep.end = ep.begin + evSizeW;
								ep_next.begin = ep.end;
								if (ep_next.end <= ep_next.begin)
									ep_next.begin = ep_next.end - 1;
								break;
							}
						}
					}
				}
			}
		}
	}

	void siewLinearParts(LinkedList events)
	{
		int counter = 1;
		while (counter != 0)
		{
			counter = 0;
			for (int i = 1; i < events.size() - 1; i++)
			{
				ReflectogramEvent ep = (ReflectogramEvent)events.get(i);
				ReflectogramEvent ep_next = (ReflectogramEvent)events.get(i + 1);
				if (ep.getType() == ReflectogramEvent.LINEAR &&
						ep_next.getType() == ReflectogramEvent.LINEAR)
				{
					ep.end = ep_next.end;
					events.remove(ep_next);
					counter++;
				}
			}
		}
	}

	void excludeShortEvents(LinkedList events, int linearLength, int weldLength, int connectorLength)
	{
		boolean key = false;
		for (int i = 1; i < events.size() - 1; i++)
		{
			ReflectogramEvent ep = (ReflectogramEvent)events.get(i);
			if (ep.getType() == ReflectogramEvent.LINEAR &&
					ep.end - ep.begin <= linearLength)
				key = true;
			if (ep.getType() == ReflectogramEvent.CONNECTOR &&
					ep.end - ep.begin <= connectorLength)
				key = true;
			if (ep.getType() == ReflectogramEvent.WELD &&
					ep.end - ep.begin <= weldLength)
				key = true;

			if (key)
			{
				ReflectogramEvent ep_next = (ReflectogramEvent)events.get(i + 1);
				ReflectogramEvent ep_last = (ReflectogramEvent)events.get(i - 1);
				if (ep_next.getType() == ReflectogramEvent.LINEAR)
					ep_next.begin = ep_last.end;
				else if (ep_last.getType() == ReflectogramEvent.LINEAR)
					ep_last.end = ep_next.begin;
				else if(ep_next.getType() == ReflectogramEvent.WELD)
					ep_next.begin = ep_last.end;
				else
					ep_last.end = ep_next.begin;
				key = false;
				events.remove(ep);
			}
		}
	}

	void correctEnd(LinkedList events, int eventSise)
	{
		for (int i = events.size() - 1; i > 0; i--)
		{
			ReflectogramEvent ep = (ReflectogramEvent)events.get(i);
			if (ep.getType() == ReflectogramEvent.CONNECTOR
					&& ep.aLet_connector > minimalEndingSplash
					&& ep.a1_connector >= 0
					&& ep.a2_connector >= 0
					&& ep.end <= lastNonZeroPoint
					&& ep.end - ep.begin > eventSise)
			{
				int l = Math.min (lastNonZeroPoint - 1, Math.min(ep.end, ep.begin + eventSise * 2));
				if (l >= lastNonZeroPoint)
					l = lastNonZeroPoint-1;
				//ep.end = l;
				break;
			}
			else
				events.remove(ep);
		}
	}

	void setEventParams(LinkedList events, int evSize)
	{
		int counter = 0;

		for (Iterator it = events.iterator(); it.hasNext();)
		{
			ReflectogramEvent re = (ReflectogramEvent)it.next();
			double[] d = MathRef.LSA(data, re.begin, re.end, re.begin);
			re.a_linear = d[1];
			re.b_linear = d[0];
		}

		//Setting of the weld params;
		for (int i = 0; i < events.size(); i++)
		{
			ReflectogramEvent ep = (ReflectogramEvent)events.get(i);
			ep.center_weld = (double)(ep.begin + ep.end) / 2.;
			ep.width_weld = evSize * .9;

			double A1;
			double A2;
			double A3;
			double k;

			if(i > 0 && i < events.size() - 1
				 && ((ReflectogramEvent)events.get(i-1)).getType() == ReflectogramEvent.LINEAR
				 && ((ReflectogramEvent)events.get(i+1)).getType() == ReflectogramEvent.LINEAR)
			{
				A1 = ((ReflectogramEvent)events.get(i-1)).linearF(ep.begin);
				A2 = ((ReflectogramEvent)events.get(i+1)).linearF(ep.begin);
				A3 = ((ReflectogramEvent)events.get(i+1)).linearF(ep.end);
				k = (((ReflectogramEvent)events.get(i-1)).b_linear + ((ReflectogramEvent)events.get(i+1)).b_linear) / 2.;
			}
			else if(i > 0 && i < events.size() - 1
							&& ((ReflectogramEvent)events.get(i-1)).getType() == ReflectogramEvent.LINEAR
							&& ((ReflectogramEvent)events.get(i+1)).getType() != ReflectogramEvent.LINEAR)
			{
				A1 = ((ReflectogramEvent)events.get(i-1)).linearF(ep.begin);
				A2 = data[ep.end];
				A3 = data[ep.end];
				k = ((ReflectogramEvent)events.get(i-1)).b_linear;
			}
			else if(i > 0 && i < events.size() - 1
							&& ((ReflectogramEvent)events.get(i-1)).getType() != ReflectogramEvent.LINEAR
							&& ((ReflectogramEvent)events.get(i+1)).getType() == ReflectogramEvent.LINEAR)
			{
				A1 = data[ep.begin];
				A2 = ((ReflectogramEvent)events.get(i+1)).linearF(ep.begin);
				A3 = ((ReflectogramEvent)events.get(i+1)).linearF(ep.end);
				k = ((ReflectogramEvent)events.get(i+1)).b_linear;
			}
			else
			{
				A1 = data[ep.begin];
				A2 = data[ep.end];
				A3 = data[ep.end];
				k = 0.;
			}
			ep.a_weld = (A1 + A3) / 2.;
			ep.boost_weld = A2 - A1;
			ep.b_weld = k;
		}

		// Setting of the connector params;
		for (int i = 0; i < events.size(); i++)
		{
			ReflectogramEvent ep = (ReflectogramEvent)events.get(i);
			double A1=0.;
			double A2=0.;
			double ALet=0.;
			double width=0.;
			double width_40=0.;
			double width_70=0.;
			double width_90=0.;
			int st = 0;
			double centre=0.;
			double sigma1=0.;
			double sigma2=0.;
			double sigmaFit = 0.;

			if(i > 0 && ((ReflectogramEvent)events.get(i-1)).getType() == ReflectogramEvent.LINEAR)
				A1 = ((ReflectogramEvent)events.get(i-1)).linearF(ep.begin);
			else
				A1 = data[ep.begin];

			if(i < events.size() - 1 && ((ReflectogramEvent)events.get(i+1)).getType() == ReflectogramEvent.LINEAR)
				A2 = ((ReflectogramEvent)events.get(i+1)).linearF(ep.end);
			else
				A2 = data[ep.end];

			ALet = A1;
			for(int j = ep.begin; j <= ep.end; j++)
			{
				if (ALet < data[j])
					ALet = data[j];
			}
			ALet = ALet - A1;

			st = 0;
			if(i > 0) // not deadzone
			{
				for(int j = ep.begin; j <= ep.end; j++)
				{
					if(data[j] > A1 + ALet *.9)
						width_90++;
					if(data[j] > A1 + ALet *.7)
						width_70++;
					if(data[j] > A1 + ALet *.4)
					{
						width_40++;
						if (st == 0)
							st = j - 1;
					}
				}
				if (width_40 - width_70 < 7)
					width = width_70;
				else
					width = width_90;
				centre = st + width / 2;
			}
			else	// deadzone
			{
				for(int j = ep.begin; j < ep.end; j++)
					if(data[j] > A1 + ALet - 0.5)
					{
						width++;
						centre = centre + j;
					}
				centre = centre / width - 1;
			}


	/*	if(width>0.1)
		{
			centre = centre/width;
		}
		else
//		if (width < 0.1)
		{
			centre = (ep[i]->begin+ep[i]->end)/2.;
		}*/

			sigma1 = (centre - ep.begin) / 20.;
		sigma2 = (ep.end - centre) * (1. - formFactor);
		sigmaFit = (ep.end - centre) * formFactor;

		ep.a1_connector = A1;
		ep.a2_connector = A2;

		ep.aLet_connector = ALet;
		ep.width_connector = width;
		ep.center_connector = centre;
		ep.sigma1_connector = sigma1;
		ep.sigma2_connector = sigma2;
		ep.sigmaFit_connector = sigmaFit;
		ep.k_connector = formFactor;
		}
	}
}
