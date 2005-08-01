package com.syrus.AMFICOM.Client.Survey;

import java.awt.Image;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Model.DefaultSurveyApplicationModelFactory;
import com.syrus.AMFICOM.client.model.AbstractApplication;
import com.syrus.AMFICOM.resource.SurveyResourceKeys;

public class Survey extends AbstractApplication {
	
	public static final String APPLICATION_NAME = "observer";
	
	public Survey() {
		super(APPLICATION_NAME);
	}		

	protected void init() {
		super.init();		
		super.aContext.setApplicationModel(new DefaultSurveyApplicationModelFactory().create());
		super.startMainFrame(new SurveyMainFrame(this.aContext), (Image) UIManager.get(SurveyResourceKeys.ICON_OBSERVE));
	}
	
	public static void main(String[] args) {
		new Survey();
	}
}
