package com.syrus.AMFICOM.Client.Prediction.StatisticsMath;

public class Fitting
{
//-----------------------------------------------------------
	public static  LinearCoeffs performLinearFitting(long []time, double []value)
	{
			double alfa=0., beta=0., gamma=0., dzeta=0., n=0.;
			for(int i=0; i<time.length; i++)
			{
				 beta = beta - value[i]*((double)time[i]);
				 alfa = alfa + ((double)time[i])*((double)time[i]);
				 gamma = gamma + ((double)time[i]);
				 dzeta = dzeta - value[i];
				 n = n + 1.;
			}

			double a_ = (n*beta/gamma - dzeta)/(gamma - n*alfa/gamma);
			double b_ = -(alfa*a_ + beta)/gamma;

			double disp = 0;
			double absDisp = 0.;
			double chi2 = 0.;
			for(int i=0; i<time.length; i++)
			{
				disp = disp + (value[i] - (a_*time[i]+b_))*(value[i] - (a_*time[i]+b_));
				absDisp = absDisp + Math.abs(value[i] - (a_*time[i]+b_));
				chi2 = chi2 + (value[i] - (a_*time[i]+b_))*(value[i] - (a_*time[i]+b_))/
							 ((value[i] + (a_*time[i]+b_))*(value[i] + (a_*time[i]+b_)));
			}
			disp = disp/time.length;
			absDisp = absDisp/time.length;
			disp = Math.sqrt(disp);
			chi2 = chi2/time.length;

			return new LinearCoeffs(b_, a_, disp, absDisp, chi2);
	}

//-----------------------------------------------------------
	public static  LinearCoeffs performLinearFitting(TimeDependenceData[] tdd)
	{
			double alfa=0., beta=0., gamma=0., dzeta=0., n=0.;
			for(int i=0; i<tdd.length; i++)
			{
				 beta = beta - tdd[i].value*((double)tdd[i].date);
				 alfa = alfa + ((double)tdd[i].date)*((double)tdd[i].date);
				 gamma = gamma + ((double)tdd[i].date);
				 dzeta = dzeta - tdd[i].value;
				 n = n + 1.;
			}

			double a_ = (n*beta/gamma - dzeta)/(gamma - n*alfa/gamma);
			double b_ = -(alfa*a_ + beta)/gamma;

			double disp = 0.;
			double absDisp = 0.;
			double chi2 = 0.;
			for(int i=0; i<tdd.length; i++)
			{
				disp = disp + (tdd[i].value - (a_*tdd[i].date+b_))*(tdd[i].value - (a_*tdd[i].date+b_));
				absDisp = absDisp + Math.abs(tdd[i].value - (a_*tdd[i].date+b_));
				chi2 = chi2 + (tdd[i].value - (a_*tdd[i].date+b_))*(tdd[i].value - (a_*tdd[i].date+b_))/
							 ((tdd[i].value + (a_*tdd[i].date+b_))*(tdd[i].value + (a_*tdd[i].date+b_)));
			}
			disp = disp/tdd.length;
			absDisp = absDisp/tdd.length;
			disp = Math.sqrt(disp);
			chi2 = chi2/tdd.length;

			return new LinearCoeffs(b_, a_, disp, absDisp, chi2);
	}

}


