package com.syrus.AMFICOM.Client.Analysis.Report;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ScaledGraphPanel;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ReportPanel
{
	String title = "";
	ScaledGraphPanel panel;

	ReportPanel(String title, ScaledGraphPanel panel)
	{
		this.title = title;
		this.panel = panel;
	}
}
