package com.syrus.AMFICOM.Client.Prediction;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Model.*;

public class PredictionRun
{
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