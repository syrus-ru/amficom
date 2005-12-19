package com.syrus.AMFICOM.Client.Prediction.StatisticsMath;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;

@Deprecated
public class ReflectogrammPredictor
{
	private ReflectoEventStatistics res;
	private double delta_x;
	private long date;
	private double []predictedReflectogramm;
	private ReflectogramEvent []prediction;
	private double []referenceArray;
	private ReflectoEventContainer reference;

	// unused?
	private ReflectoEventStatistics getStatistics()
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
		double dT = (double)(this.date - reference.date);

		prediction = new ReflectogramEvent[reference.re.length];
		for(int i=0; i<prediction.length; i++)
		{
			prediction[i] = reference.re[i].copy();
		}

		/*
		 * Исходный код:
		 * Судя по тому, что результаты RES.getXxxInformation()
		 * кладутся в Pool многократно, затирая друг друга,
		 * будем предполагать, что полученные здесь объекты в Pool
		 * больше нигде не используются.
		 * В связи с этим считаем, что новый код, ничего не сохраняющий
		 * в Pool, также уложится в неписаный контракт этого метода.
		 * 
		 * Новый код:
		 * Получает нужные данные напрямую, минуя Pool.
		 */
		ReflectogramEvent []etalon = reference.re;
		LinearCoeffs linCoeffs;
		double dA;
		for(int i=0; i<prediction.length; i++)
		{
			dA = 0.;
			if(etalon[i].getType() == ReflectogramEvent.CONNECTOR && i == 0) //dead zone;
			{
				linCoeffs = res.trueGetSplashAmplitudeInformation(i).getLc();
				dA = -dT*linCoeffs.k;
				prediction[0].a2_connector+=dA; //?
				siewEvents(i, dA);
			}
			else if(etalon[i].getType() == ReflectogramEvent.CONNECTOR && i!=0 && i<prediction.length-1)
			{
				linCoeffs = res.trueGetEnergyLossInformation(i).getLc();
				dA = -dT*linCoeffs.k;
				prediction[i].a2_connector +=dA; //?
				siewEvents(i, dA);
			}
			else if(etalon[i].getType() == ReflectogramEvent.WELD)
			{
				linCoeffs = res.trueGetEnergyLossInformation(i).getLc();
				dA = -dT*linCoeffs.k;
				prediction[i].boost_weld+=dA;
				prediction[i].a_weld +=dA/2.;
				siewEvents(i, dA);
			}
			else if(etalon[i].getType() == ReflectogramEvent.CONNECTOR && i == etalon.length-1)
			{
				linCoeffs = res.trueGetSplashAmplitudeInformation(i).getLc();
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
