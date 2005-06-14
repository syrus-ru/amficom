package com.syrus.AMFICOM.Client.Analysis.Reflectometry;

import java.awt.Image;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrame;
import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.General.Model.ReflectometryAnalyseApplicationModelFactory;
import com.syrus.AMFICOM.client.model.AbstractApplication;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.util.Application;

public class AnalyseExt extends AbstractApplication
{
	public AnalyseExt(AnalyseApplicationModelFactory factory)
	{
		if(!Environment.canRun(Environment.MODULE_SURVEY))
			return;
		Application.init(Analyse.APPLICATION_NAME);
		super.aContext.setApplicationModel(factory.create());
		super.startMainFrame(new AnalyseMainFrame(this.aContext), 
				(Image) UIManager.get(AnalysisResourceKeys.ICON_SURVEY_MINI));
	}

	public static void main(String[] args)
	{
		new AnalyseExt(new ReflectometryAnalyseApplicationModelFactory());
	}
}
