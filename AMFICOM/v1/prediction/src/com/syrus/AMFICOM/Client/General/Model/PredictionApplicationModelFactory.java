package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.client.model.ApplicationModel;

public class PredictionApplicationModelFactory {
	
	public ApplicationModel create() {
		new AnalyseApplicationModel();
		new SchematicsApplicationModel();
		
		ApplicationModel aModel = new PredictionApplicationModel();
		return aModel;
	}
}
