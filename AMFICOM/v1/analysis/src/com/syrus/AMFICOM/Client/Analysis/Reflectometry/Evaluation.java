package com.syrus.AMFICOM.Client.Analysis.Reflectometry;

import java.awt.Toolkit;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ThresholdsMainFrame;
import com.syrus.AMFICOM.Client.General.Model.*;

public class Evaluation
{
	ApplicationContext aContext = new ApplicationContext();

	public Evaluation(AnalyseApplicationModelFactory factory)
	{
		if(!Environment.canRun(Environment.MODULE_EVALUATE))
			return;

		aContext.setApplicationModel(factory.create());
		ThresholdsMainFrame frame = new ThresholdsMainFrame(aContext);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("images/main/evaluate_mini.gif"));
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

		new Evaluation(new ReflectometryAnalyseApplicationModelFactory());
	}
}
