package com.syrus.AMFICOM.Client.Model;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class ModelRun
{
	public static void main(String[] args)
	{
		Environment.initialize();

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		new Model(new DefaultModelApplicationModelFactory());
	}
}