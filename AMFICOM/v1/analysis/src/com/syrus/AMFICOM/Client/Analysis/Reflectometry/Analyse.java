package com.syrus.AMFICOM.Client.Analysis.Reflectometry;

import java.awt.Image;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrameSimplified;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.ReflectometryAnalyseApplicationModelFactory;

public class Analyse
{
	private ApplicationContext aContext = new ApplicationContext();

	public Analyse(AnalyseApplicationModelFactory factory)
	{
		if(!Environment.canRun(Environment.MODULE_ANALYSE))
			return;

		this.aContext.setApplicationModel(factory.create());
		AnalyseMainFrameSimplified frame = new AnalyseMainFrameSimplified(this.aContext);

		frame.setIconImage((Image) UIManager.get(AnalysisResourceKeys.ICON_ANALYSIS_MINI));
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