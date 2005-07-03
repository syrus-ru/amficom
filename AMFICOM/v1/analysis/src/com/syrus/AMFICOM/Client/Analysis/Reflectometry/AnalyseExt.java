package com.syrus.AMFICOM.Client.Analysis.Reflectometry;

import java.awt.Image;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainFrame;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.General.Model.ReflectometryAnalyseApplicationModelFactory;
import com.syrus.AMFICOM.client.model.AbstractApplication;

public class AnalyseExt extends AbstractApplication
{
	public AnalyseExt()
	{
		super(Analyse.APPLICATION_NAME);
	}
	
	protected void init() {
		super.init();
		
		super.aContext.setApplicationModel(new ReflectometryAnalyseApplicationModelFactory().create());
		super.startMainFrame(new AnalyseMainFrame(this.aContext), 
				(Image) UIManager.get(AnalysisResourceKeys.ICON_SURVEY_MINI));
	}

	public static void main(String[] args) {
		new AnalyseExt();
	}
}
