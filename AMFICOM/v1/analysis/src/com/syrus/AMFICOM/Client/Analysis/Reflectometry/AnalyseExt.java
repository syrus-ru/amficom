package com.syrus.AMFICOM.Client.Analysis.Reflectometry;

import java.awt.Toolkit;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.ReflectometryAnalyseApplicationModelFactory;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrame;

public class AnalyseExt
{
	ApplicationContext aContext = new ApplicationContext();

	public AnalyseExt(AnalyseApplicationModelFactory factory)
	{
		if(!Environment.canRun(Environment.MODULE_SURVEY))
			return;

		aContext.setApplicationModel(factory.create());
		AnalyseMainFrame frame = new AnalyseMainFrame(aContext);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("images/main/survey_mini.gif"));
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		try {
			UIManager.setLookAndFeel(Environment.getLookAndFeel());
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		new AnalyseExt(new ReflectometryAnalyseApplicationModelFactory());
	}
}