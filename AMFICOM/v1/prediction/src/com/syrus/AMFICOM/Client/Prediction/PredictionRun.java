package com.syrus.AMFICOM.Client.Prediction;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Lang.LangModelPrediction;
import com.syrus.AMFICOM.Client.General.Model.DefaultPredictionApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Environment;

public class PredictionRun
{
	public static void main(String[] args)
	{
		Environment.initialize();

		try
		{
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
//		LangModelPrediction.initialize();
//		LangModelAnalyse.initialize();
//		com.syrus.AMFICOM.Client.General.Lang.LangModelReport.initialize();

		new Prediction(new DefaultPredictionApplicationModelFactory());
	}
}