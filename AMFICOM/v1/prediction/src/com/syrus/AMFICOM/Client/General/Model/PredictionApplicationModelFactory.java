package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;

public class PredictionApplicationModelFactory {
	
	public ApplicationModel create() {
		I18N.addResourceBundle("com.syrus.AMFICOM.resource.predictionMessages");
		new AnalyseApplicationModel();
		new SchematicsApplicationModel();
		
		ApplicationModel aModel = new PredictionApplicationModel();
		return aModel;
	}
}
