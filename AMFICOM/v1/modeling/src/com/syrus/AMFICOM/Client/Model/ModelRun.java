package com.syrus.AMFICOM.Client.Model;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Lang.LangModelModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.DefaultModelApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Environment;

public class ModelRun
{
	public static void main(String[] args)
	{
		Environment.initialize();
		LangModelModel.initialize();
		LangModelAnalyse.initialize();
		LangModelSchematics.initialize();
		LangModelConfig.initialize();
		com.syrus.AMFICOM.Client.General.Lang.LangModelReport.initialize();

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