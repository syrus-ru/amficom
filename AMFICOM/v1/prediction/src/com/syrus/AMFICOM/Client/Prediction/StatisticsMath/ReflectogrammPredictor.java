package com.syrus.AMFICOM.Client.Prediction.StatisticsMath;

import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;

public class ReflectogrammPredictor
{
	private ReflectoEventStatistics res;
	private double delta_x;
	private long date;
	private double []predictedReflectogramm;
	private ReflectogramEvent []prediction;
	private double []referenceArray;
	private ReflectoEventContainer reference;

	public ReflectoEventStatistics getStatistics()
	{
		return res;
	}


//--------------------------------------------------------------------
	public ReflectogrammPredictor(long date, ReflectoEventStatistics res)
	{
		this.date = date;
		this.res = res;
		this.delta_x = res.delta_x;
		this.reference = res.getReference();
		this.referenceArray = res.referenceArray;

		makePredictedReflectogramm();
	}

/*
	private void makePredictedReflectogramm()
	{
		double dT = (double)(this.date - reference.date);

		prediction = new ReflectogramEvent[reference.re.length];
		for(int i=0; i<prediction.length; i++)
		{
			prediction[i] = reference.re[i].copy();
		}

		ReflectogramEvent []etalon = reference.re;
		LinearCoeffs linCoeffs;
		double dA;
		for(int i=0; i<prediction.length; i++)
		{
			dA = 0.;
			if(etalon[i].connectorEvent == 1 && i == 0) //dead zone;
			{
				res.getSplashAmplitudeInformation(i);
				linCoeffs = (LinearCoeffs)Pool.get("linearCoeffs", "MyLinearCoeffs");
				dA = -dT*linCoeffs.k;
				prediction[0].a2_connector+=dA; //?
			}
			else if(etalon[i].connectorEvent == 1 && i!=0 && i<prediction.length-1)
			{
				res.getEnergyLossInformation(i);
				linCoeffs = (LinearCoeffs)Pool.get("linearCoeffs", "MyLinearCoeffs");
				dA = -dT*linCoeffs.k;
				prediction[i].a2_connector +=dA; //?
			}
			else if(etalon[i].linearEvent == 1)
			{
				res.getAmplitudeInformation(i);
				linCoeffs = (LinearCoeffs)Pool.get("linearCoeffs", "MyLinearCoeffs");
				dA = -dT*linCoeffs.k;
				prediction[i].a_linear+=dA;
			}
			else if(etalon[i].weldEvent == 1)
			{
				res.getEnergyLossInformation(i);
				linCoeffs = (LinearCoeffs)Pool.get("linearCoeffs", "MyLinearCoeffs");
				dA = -dT*linCoeffs.k;
				prediction[i].boost_weld+=dA;
			}
		}

		for(int i=1; i<prediction.length; i++) //siewing of the events;
		{
			double Ampl = prediction[i-1].refAmpl(prediction[i-1].end)[0];
			double ownAmpl = prediction[i].refAmpl(prediction[i].begin)[0];

			prediction[i].a_linear += (Ampl - ownAmpl);
			prediction[i].a1_connector += (Ampl - ownAmpl);
			prediction[i].a2_connector += (Ampl - ownAmpl);
			prediction[i].a_weld += (Ampl - ownAmpl);
		}

		double []tmp =  WorkWithReflectoEventsArray.getArrayFromReflectogramEvents(prediction);
		predictedReflectogramm = new double[referenceArray.length];

		for(int i=0; i<tmp.length; i++)
		{
			predictedReflectogramm[i] = tmp[i];
		}

//    double max = referenceArray[0];
//    double min = referenceArray[0];
//
//    for(int i=0; i<referenceArray.length; i++)
//    {
//      if(max<referenceArray[i])
//        max = referenceArray[i];
//      if(min>referenceArray[i])
//        min = referenceArray[i];
//    }
	}
*/


	private void makePredictedReflectogramm()
	{
		double dT = (double)(this.date - reference.date);

		prediction = new ReflectogramEvent[reference.re.length];
		for(int i=0; i<prediction.length; i++)
		{
			prediction[i] = reference.re[i].copy();
		}

		ReflectogramEvent []etalon = reference.re;
		LinearCoeffs linCoeffs;
		double dA;
		for(int i=0; i<prediction.length; i++)
		{
			dA = 0.;
			if(etalon[i].getType() == ReflectogramEvent.CONNECTOR && i == 0) //dead zone;
			{
				res.getSplashAmplitudeInformation(i);
				linCoeffs = (LinearCoeffs)Pool.get("linearCoeffs", "MyLinearCoeffs");
				dA = -dT*linCoeffs.k;
				prediction[0].a2_connector+=dA; //?
				siewEvents(i, dA);
			}
			else if(etalon[i].getType() == ReflectogramEvent.CONNECTOR && i!=0 && i<prediction.length-1)
			{
				res.getEnergyLossInformation(i);
				linCoeffs = (LinearCoeffs)Pool.get("linearCoeffs", "MyLinearCoeffs");
				dA = -dT*linCoeffs.k;
				prediction[i].a2_connector +=dA; //?
				siewEvents(i, dA);
			}
			else if(etalon[i].getType() == ReflectogramEvent.WELD)
			{
				res.getEnergyLossInformation(i);
				linCoeffs = (LinearCoeffs)Pool.get("linearCoeffs", "MyLinearCoeffs");
				dA = -dT*linCoeffs.k;
				prediction[i].boost_weld+=dA;
				prediction[i].a_weld +=dA/2.;
				siewEvents(i, dA);
			}
			else if(etalon[i].getType() == ReflectogramEvent.CONNECTOR && i == etalon.length-1)
			{
				res.getSplashAmplitudeInformation(i);
				linCoeffs = (LinearCoeffs)Pool.get("linearCoeffs", "MyLinearCoeffs");
				dA = dT*linCoeffs.k;
				prediction[i].aLet_connector += dA;
			}
		}


		double []tmp =  ReflectogramMath.getReflectogrammFromEvents(prediction, prediction[prediction.length-1].end);

		predictedReflectogramm = new double[referenceArray.length];
		for(int i=0; i<tmp.length; i++)
		{
			predictedReflectogramm[i] = tmp[i];
		}

//    double max = referenceArray[0];
//    double min = referenceArray[0];
//
//    for(int i=0; i<referenceArray.length; i++)
//    {
//      if(max<referenceArray[i])
//        max = referenceArray[i];
//      if(min>referenceArray[i])
//        min = referenceArray[i];
//    }
	}

	private void siewEvents(int n, double dA)
	{
		for(int i=n+1; i<prediction.length; i++)
		{
			prediction[i].a_linear +=dA;
			prediction[i].a_weld +=dA;
			prediction[i].a1_connector +=dA;
			prediction[i].a2_connector +=dA;
		}

	}

//--------------------------------------------------------------------
	public double []getPredictedReflectogramm()
	{
		 return predictedReflectogramm;
	}
}





/*
public class ReflectogrammPredictor
{
	private ReflectoEventStatistics res;
	private double delta_x;
	private long date;
	private double []predictedReflectogramm;
	private double []referenceArray;
	private double []changesArray;
	private ReflectoEventContainer reference;

	public ReflectoEventStatistics getStatistics()
	{
		return res;
	}


//--------------------------------------------------------------------
	public ReflectogrammPredictor(long date, ReflectoEventStatistics res)
	{
		this.date = date;
		this.res = res;
		this.delta_x = res.delta_x;
		this.reference = res.getReference();
		this.referenceArray = res.referenceArray;

		makePredictedReflectogramm();
	}


	private void makePredictedReflectogramm()
	{
		changesArray = new double[referenceArray.length];
		LinearCoeffs linCoeff;
		double dT = (double)(this.date - reference.date);
		for(int i=0; i<reference.re.length; i++)
		{

			if(reference.re[i].linearEvent == 1)
			{
				res.getAmplitudeInformation(i);
				linCoeff = (LinearCoeffs)Pool.get("linearCoeffs", "MyLinearCoeffs");
				double A = dT*linCoeff.k;
				for(int j=reference.re[i].begin; j<=reference.re[i].end; j++)
				{
					changesArray[j] = A;
				}
			}
		}


		WorkWithReflectoEventsArray res;

		com.syrus.AMFICOM.Client.Analysis.HistoAnalysis.LinearCoeffs lc = WorkWithReflectoArray.
				performLinearFitting(changesArray, 0, reference.re[reference.re.length-1].begin);


		for(int i=0; i<changesArray.length; i++)
		{
			changesArray[i] = lc.f((double)i);
		}

		predictedReflectogramm = new double[referenceArray.length];
		for(int i=0; i<predictedReflectogramm.length && i<reference.re[reference.re.length-1].end; i++)
		{
			predictedReflectogramm[i] = referenceArray[i]-changesArray[i];
		}

		for(int i=reference.re[reference.re.length-1].end; i<predictedReflectogramm.length; i++)
		{
			predictedReflectogramm[i] = referenceArray[i];
		}

	}



//--------------------------------------------------------------------
	public double []getPredictedReflectogramm()
	{
		 return predictedReflectogramm;
	}
}

*/