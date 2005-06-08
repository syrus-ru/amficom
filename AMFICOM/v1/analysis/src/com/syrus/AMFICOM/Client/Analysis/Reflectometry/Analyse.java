package com.syrus.AMFICOM.Client.Analysis.Reflectometry;

import java.awt.Image;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrameSimplified;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.ReflectometryAnalyseApplicationModelFactory;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.util.Application;

public class Analyse
{
	private ApplicationContext aContext = new ApplicationContext();
	
	public static final String APPLICATION_NAME = "analysis";

	public Analyse(AnalyseApplicationModelFactory factory)
	{
		if(!Environment.canRun(Environment.MODULE_ANALYSE))
			return;
		Application.init(Analyse.APPLICATION_NAME);
		this.aContext.setApplicationModel(factory.create());
		this.aContext.setDispatcher(new Dispatcher());
		AnalyseMainFrameSimplified frame = new AnalyseMainFrameSimplified(this.aContext);

		frame.setIconImage((Image) UIManager.get(AnalysisResourceKeys.ICON_ANALYSIS_MINI));
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		new Analyse(new ReflectometryAnalyseApplicationModelFactory());
	}
}
