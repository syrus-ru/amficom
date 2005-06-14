
package com.syrus.AMFICOM.Client.Analysis.Reflectometry;

import java.awt.Image;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ThresholdsMainFrame;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.util.Application;

public class Evaluation extends AbstractApplication {

	public Evaluation(AnalyseApplicationModelFactory factory) {
		if (!Environment.canRun(Environment.MODULE_EVALUATE))
			return;
		Application.init(Analyse.APPLICATION_NAME);
		super.aContext.setApplicationModel(factory.create());
		super.startMainFrame(new ThresholdsMainFrame(this.aContext), 
				(Image) UIManager.get(AnalysisResourceKeys.ICON_EVALUATE_MINI));
	}

	public static void main(String[] args) {
		new Evaluation(new ReflectometryAnalyseApplicationModelFactory());
	}
}
