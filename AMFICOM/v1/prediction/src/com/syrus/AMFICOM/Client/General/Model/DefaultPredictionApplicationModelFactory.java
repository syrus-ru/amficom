package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.client.model.ApplicationModel;

public class DefaultPredictionApplicationModelFactory extends PredictionApplicationModelFactory {

	@Override
	public ApplicationModel create() {
		ApplicationModel aModel = super.create();
		
		aModel.setUsable("menuSessionSave", false);
		aModel.setUsable("menuSessionUndo", false);
		aModel.setVisible("menuSessionOptions", false);
		
		return aModel;
	}
}