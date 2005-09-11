package com.syrus.AMFICOM.client.model;

import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.SchematicsApplicationModel;

public class ObserverApplicationModelFactory {

	public ObserverApplicationModelFactory() {
		// empty
	}

	public ApplicationModel create() {
		new SchematicsApplicationModel();
		new AnalyseApplicationModel();
		ApplicationModel aModel = new ObserverApplicationModel();
		return aModel;
	}
}
