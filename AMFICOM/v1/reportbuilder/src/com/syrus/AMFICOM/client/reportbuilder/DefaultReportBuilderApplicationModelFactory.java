/*
 * $Id: DefaultReportBuilderApplicationModelFactory.java,v 1.2 2005/08/31 10:07:26 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

import com.syrus.AMFICOM.client.model.ApplicationModel;

public class DefaultReportBuilderApplicationModelFactory
		extends ReportBuilderApplicationModelFactory
{
	public DefaultReportBuilderApplicationModelFactory()
	{
		//Maybe nothing
	}

	@Override
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setVisible("menuSessionSave", false);
		aModel.setVisible("menuSessionUndo", false);
		aModel.setVisible("menuSessionOptions", false);
//    aModel.setUsable("menuRead", true);

		return aModel;
	}
}
