package com.syrus.AMFICOM.Client.ReportBuilder;

import com.syrus.AMFICOM.Client.General.Model.ApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class ReportDefaultApplicationModelFactory
		extends ReportApplicationModelFactory
{

	public ReportDefaultApplicationModelFactory()
	{
	}

	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setEnabled("menuAlarmAlert", false);
		aModel.setEnabled("menuReportBuilder", false);

		return aModel;
	}
}