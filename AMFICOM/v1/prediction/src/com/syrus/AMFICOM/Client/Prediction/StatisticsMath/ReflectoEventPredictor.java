package com.syrus.AMFICOM.Client.Prediction.StatisticsMath;

import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.io.BellcoreStructure;

public class ReflectoEventPredictor
{
	private ReflectogramEvent []reference;
	private ReflectogramEvent []prediction;
	private ReflectoEventStatistics reflectoEventStatistics;
	private long date;
	private double delta_x;

	public ReflectoEventPredictor(long date, ReflectoEventStatistics res)
	{
		this.reflectoEventStatistics = res;
		this.date = date;
		this.getPrediction();
	}

	private void getPrediction()
	{
		this.reference = reflectoEventStatistics.getReference().re;
		if(reference == null) return;

		long referenceDate = reflectoEventStatistics.getReference().date;
		long timeShift = date - referenceDate;

		delta_x = reflectoEventStatistics.delta_x;
		prediction = new ReflectogramEvent[reference.length];
		prediction[0] = reference[0].copy();

		LinearCoeffs linearCoeffs;
		for(int i=1; i<reference.length; i++)
		{
			prediction[i] = reference[i].copy();
			{ // information about how the amplitude will change
				reflectoEventStatistics.getAmplitudeInformation(i);
				if(Pool.get("linearCoeffs", "MyLinearCoeffs") == null)
					continue;
				linearCoeffs = (LinearCoeffs)Pool.get("linearCoeffs", "MyLinearCoeffs");

				if(this.reference[i].getType() == ReflectogramEvent.CONNECTOR)
				{
					prediction[i].a1_connector += (linearCoeffs.k*timeShift);
					prediction[i].aLet_connector += (linearCoeffs.k*timeShift);
					prediction[i].a2_connector += (linearCoeffs.k*timeShift);
				}
				else if(this.reference[i].getType() == ReflectogramEvent.LINEAR)
					prediction[i].a_linear += (linearCoeffs.k*timeShift);
				else
					prediction[i].a_weld += (linearCoeffs.k*timeShift);
			}
		}
	}

	public ReflectogramEvent[] getPredictedEvents()
	{
		return prediction;
	}

	public double[] getPredictedReflectogramm()
	{
		if(prediction == null)
			return null;
		BellcoreStructure bs = reflectoEventStatistics.getReference().bs;
		int length;
		if(bs == null)
			length = prediction[prediction.length-1].end;
		else
			length = bs.dataPts.TNDP;

		double[] y = new double[length];

		for(int i = 0; i < prediction.length; i++)
			for(int j = prediction[i].begin; j <= prediction[i].end && j < y.length; j++)
				y[j] = prediction[i].refAmpl(j)[0];

		return y;
	}
}