package com.syrus.AMFICOM.Client.Prediction;

import java.awt.Toolkit;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.DefaultPredictionApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.PredictionApplicationModelFactory;

public class Prediction
{
	ApplicationContext aContext = new ApplicationContext();

	public Prediction(PredictionApplicationModelFactory factory)
	{
		if(!Environment.canRun(Environment.MODULE_PROGNOSIS))
			return;

		aContext.setApplicationModel(factory.create());

		PredictionMDIMain frame = new PredictionMDIMain(aContext);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("images/main/prognosis_mini.gif"));
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		new Prediction(new DefaultPredictionApplicationModelFactory());
	}
}
