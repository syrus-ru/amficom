package com.syrus.AMFICOM.Client.Model;

import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Lang.LangModelModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.DefaultModelApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.ModelApplicationModelFactory;

public class Model
{
	ApplicationContext aContext = new ApplicationContext();

	public Model(ModelApplicationModelFactory factory)
	{
		if(!Environment.canRun(Environment.MODULE_MODEL))
			return;

		aContext.setApplicationModel(factory.create());

		Frame frame = new ModelMDIMain(aContext);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("images/main/model_mini.gif"));
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		Environment.initialize();
//		LangModelModel.initialize();
//		LangModelAnalyse.initialize();
//		LangModelSchematics.initialize();
//		LangModelConfig.initialize();
//		com.syrus.AMFICOM.Client.General.Lang.LangModelReport.initialize();

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

