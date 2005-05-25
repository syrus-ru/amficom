
package com.syrus.AMFICOM.Client.Analysis.Reflectometry;

import java.awt.Image;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ThresholdsMainFrame;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.ReflectometryAnalyseApplicationModelFactory;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.util.Application;

public class Evaluation {

	private ApplicationContext	aContext	= new ApplicationContext();

	public Evaluation(AnalyseApplicationModelFactory factory) {
		if (!Environment.canRun(Environment.MODULE_EVALUATE))
			return;

		this.aContext.setApplicationModel(factory.create());
		ThresholdsMainFrame frame = new ThresholdsMainFrame(this.aContext);
		frame.setIconImage((Image) UIManager.get(AnalysisResourceKeys.ICON_EVALUATE_MINI));
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		Application.init(Analyse.APPLICATION_NAME);
		new Evaluation(new ReflectometryAnalyseApplicationModelFactory());
	}
}
