package com.syrus.AMFICOM.Client.Prediction;

import java.awt.Frame;
import java.awt.Toolkit;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.PredictionApplicationModelFactory;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Lang.LangModelPrediction;
import com.syrus.AMFICOM.Client.General.Model.DefaultPredictionApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Environment;
public class Prediction
{
	ApplicationContext aContext = new ApplicationContext();

	public Prediction(PredictionApplicationModelFactory factory)
	{
		if(!Environment.canRun(Environment.MODULE_PROGNOSIS))
			return;

		aContext.setApplicationModel(factory.create());

		Frame frame = new PredictionMDIMain(aContext);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("images/main/prognosis_mini.gif"));
		frame.setVisible(true);
	}

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
//		com.syrus.AMFICOM.Client.General.Lang.LangModelReport.initialize();
//		LangModelPrediction.initialize();
//		LangModelAnalyse.initialize();

		new Prediction(new DefaultPredictionApplicationModelFactory());
	}
}
