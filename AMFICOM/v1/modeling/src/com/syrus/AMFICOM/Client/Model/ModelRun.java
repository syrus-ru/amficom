package com.syrus.AMFICOM.Client.Model;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Model.DefaultModelApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Environment;

public class ModelRun
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

		new Model(new DefaultModelApplicationModelFactory());
	}
}