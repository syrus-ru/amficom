package com.syrus.AMFICOM.Client.Analysis.Reflectometry;

import java.awt.Image;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrame;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.ReflectometryAnalyseApplicationModelFactory;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.util.Application;

public class AnalyseExt
{
	private ApplicationContext aContext = new ApplicationContext();

	public AnalyseExt(AnalyseApplicationModelFactory factory)
	{
		if(!Environment.canRun(Environment.MODULE_SURVEY))
			return;

		Application.init(Analyse.APPLICATION_NAME);
		this.aContext.setApplicationModel(factory.create());
		this.aContext.setDispatcher(new Dispatcher());
		AnalyseMainFrame frame = new AnalyseMainFrame(this.aContext);
		frame.setIconImage((Image) UIManager.get(AnalysisResourceKeys.ICON_SURVEY_MINI));
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		new AnalyseExt(new ReflectometryAnalyseApplicationModelFactory());
	}
}
