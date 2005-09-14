/*
 * $Id: ReportBuilderApplicationModelFactory.java,v 1.4 2005/09/14 14:37:29 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

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
		return aModel;
	}
}
