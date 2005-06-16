
package com.syrus.AMFICOM.Client.Analysis.Reflectometry;

import java.awt.Image;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ThresholdsMainFrame;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.General.Model.ReflectometryAnalyseApplicationModelFactory;
import com.syrus.AMFICOM.client.model.AbstractApplication;

public class Evaluation extends AbstractApplication {

	public Evaluation() {
		super(Analyse.APPLICATION_NAME);
	}
	
	protected void init() {
		super.init();
		super.aContext.setApplicationModel(new ReflectometryAnalyseApplicationModelFactory().create());
		super.startMainFrame(new ThresholdsMainFrame(this.aContext), 
				(Image) UIManager.get(AnalysisResourceKeys.ICON_EVALUATE_MINI));
	}

	public static void main(String[] args) {
		new Evaluation();
	}
}
