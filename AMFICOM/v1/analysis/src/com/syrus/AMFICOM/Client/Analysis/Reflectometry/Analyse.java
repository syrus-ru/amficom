package com.syrus.AMFICOM.Client.Analysis.Reflectometry;

import java.awt.Image;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrameSimplified;
import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.General.Model.ReflectometryAnalyseApplicationModelFactory;
import com.syrus.AMFICOM.client.model.AbstractApplication;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.util.Application;

public class Analyse extends AbstractApplication
{
	public static final String APPLICATION_NAME = "analysis";

	public Analyse(AnalyseApplicationModelFactory factory)
	{
		if(!Environment.canRun(Environment.MODULE_ANALYSE))
			return;
		Application.init(Analyse.APPLICATION_NAME);
		super.aContext.setApplicationModel(factory.create());
		super.startMainFrame(new AnalyseMainFrameSimplified(this.aContext), 
				(Image) UIManager.get(AnalysisResourceKeys.ICON_ANALYSIS_MINI));
	}

	public static void main(String[] args)
	{
		new Analyse(new ReflectometryAnalyseApplicationModelFactory());
	}
}
