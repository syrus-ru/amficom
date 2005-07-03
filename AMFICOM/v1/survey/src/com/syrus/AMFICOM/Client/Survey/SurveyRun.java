
// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.Survey;

import com.syrus.AMFICOM.Client.General.Model.DefaultSurveyApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Environment;

import javax.swing.UIManager;

public class SurveyRun
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
		new Survey(new DefaultSurveyApplicationModelFactory());
	}
}

