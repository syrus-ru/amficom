package com.syrus.AMFICOM.Client.Prediction;

import java.awt.Image;
import java.awt.Toolkit;

import com.syrus.AMFICOM.Client.General.Model.DefaultPredictionApplicationModelFactory;
import com.syrus.AMFICOM.client.model.AbstractApplication;

public class Prediction extends AbstractApplication {

	public static final String APPLICATION_NAME = "prediction";
	
	public Prediction() {
		super(APPLICATION_NAME);
	}		
	
	@Override
	protected void init() {
		super.aContext.setApplicationModel(new DefaultPredictionApplicationModelFactory().create());
		Image image = Toolkit.getDefaultToolkit().getImage("images/main/prognosis_mini.gif");
		// (Image) UIManager.get(ModelResourceKeys.ICON_MODEL_MAIN)
		super.startMainFrame(new PredictionMDIMain(super.aContext), image);
	}
	
	public static void main(String[] args) {
		new Prediction();
	}
}
