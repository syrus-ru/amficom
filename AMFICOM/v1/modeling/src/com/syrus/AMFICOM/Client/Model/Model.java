package com.syrus.AMFICOM.Client.Model;

import java.awt.Image;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Model.DefaultModelApplicationModelFactory;
import com.syrus.AMFICOM.client.model.AbstractApplication;
import com.syrus.AMFICOM.resource.ModelResourceKeys;

public class Model extends AbstractApplication {

	public static final String APPLICATION_NAME = "modeling";
	
	public Model() {
		super(APPLICATION_NAME);
	}		
	
	@Override
	protected void init() {
		super.aContext.setApplicationModel(new DefaultModelApplicationModelFactory().create());
		super.startMainFrame(new ModelMDIMain(super.aContext), (Image) UIManager.get(ModelResourceKeys.ICON_MODEL_MAIN));
	}
	
	public static void main(String[] args) {
		new Model();
	}
}

