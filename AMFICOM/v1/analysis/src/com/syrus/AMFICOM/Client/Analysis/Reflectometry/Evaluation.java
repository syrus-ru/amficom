
package com.syrus.AMFICOM.Client.Analysis.Reflectometry;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ThresholdsMainFrame;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.General.Model.ReflectometryAnalyseApplicationModelFactory;
import com.syrus.AMFICOM.client.model.AbstractApplication;

public class Evaluation extends AbstractApplication {
	public static final String APPLICATION_NAME = "evaluation";
	
	public Evaluation() {
		super(Evaluation.APPLICATION_NAME);
	}

	@Override
	protected void init() {
		super.aContext.setApplicationModel(new ReflectometryAnalyseApplicationModelFactory().create());
		final ImageIcon imageIcon = (ImageIcon) UIManager.getIcon(AnalysisResourceKeys.ICON_EVALUATE_MINI);
		super.startMainFrame(new ThresholdsMainFrame(this.aContext), imageIcon.getImage());
	}

	public static void main(String[] args) {
		new Evaluation();
	}
}
