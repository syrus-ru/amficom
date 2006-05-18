package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.client.model.ApplicationModel;

public abstract class ModelApplicationModelFactory {
	
	public ApplicationModel create() {
		ModelApplicationModel aModel = new ModelApplicationModel();
		return aModel;
	}
}