package com.syrus.AMFICOM.Client.Analysis.Reflectometry;

import java.awt.Toolkit;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrameSimplified;
import com.syrus.AMFICOM.Client.General.Model.*;

public class Analyse
{
	ApplicationContext aContext = new ApplicationContext();

	public Analyse(AnalyseApplicationModelFactory factory)
	{
		if(!Environment.canRun(Environment.MODULE_ANALYSE))
			return;

		aContext.setApplicationModel(factory.create());
		AnalyseMainFrameSimplified frame = new AnalyseMainFrameSimplified(aContext);

		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("images/main/analyse_mini.gif"));
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		new Analyse(new ReflectometryAnalyseApplicationModelFactory());
	}
}