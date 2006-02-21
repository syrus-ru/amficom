package com.syrus.AMFICOM.client.model;

import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.SchematicsApplicationModel;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;

public class ObserverApplicationModelFactory {

	public ObserverApplicationModelFactory() {
		// empty
	}

	public ApplicationModel create(ApplicationContext aContext) {
		new SchematicsApplicationModel();
		new SchedulerModel(aContext);
		new AnalyseApplicationModel();
		ApplicationModel aModel = new ObserverApplicationModel();
		return aModel;
	}
}
