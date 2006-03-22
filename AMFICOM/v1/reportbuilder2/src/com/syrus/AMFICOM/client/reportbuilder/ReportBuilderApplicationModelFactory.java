/*
 * $Id: ReportBuilderApplicationModelFactory.java,v 1.1.1.1 2005/12/02 11:37:17 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.SchematicsApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModel;

public class ReportBuilderApplicationModelFactory
{
	public ReportBuilderApplicationModelFactory() {
		//Maybe nothing
	}

	public ApplicationModel create() {
		ApplicationModel aModel = new ReportBuilderApplicationModel();
		new SchematicsApplicationModel();
		new AnalyseApplicationModel();
		return aModel;
	}
}
