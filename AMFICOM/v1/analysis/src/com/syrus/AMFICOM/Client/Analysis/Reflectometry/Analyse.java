package com.syrus.AMFICOM.Client.Analysis.Reflectometry;

import java.awt.Image;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrameSimplified;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.General.Model.ReflectometryAnalyseApplicationModelFactory;
import com.syrus.AMFICOM.client.model.AbstractApplication;

public class Analyse extends AbstractApplication
{
	public static final String APPLICATION_NAME = "analysis";

	public Analyse() {
		super(Analyse.APPLICATION_NAME);
	}
	
	protected void init() {
		super.init();		
		super.aContext.setApplicationModel(new ReflectometryAnalyseApplicationModelFactory().create());
		super.startMainFrame(new AnalyseMainFrameSimplified(this.aContext), (Image) UIManager.get(AnalysisResourceKeys.ICON_ANALYSIS_MINI));
	}

	public static void main(String[] args) {
		new Analyse();
	}
}
