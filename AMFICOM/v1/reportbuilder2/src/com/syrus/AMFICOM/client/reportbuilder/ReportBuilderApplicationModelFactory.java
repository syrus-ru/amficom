/*
 * $Id: ReportBuilderApplicationModelFactory.java,v 1.2 2006/04/11 05:58:32 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ModelApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.PredictionApplicationModel;
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
		new ModelApplicationModel();
		new PredictionApplicationModel();
		return aModel;
	}
}
