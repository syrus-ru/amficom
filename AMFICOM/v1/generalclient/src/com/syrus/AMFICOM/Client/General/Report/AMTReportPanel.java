package com.syrus.AMFICOM.Client.General.Report;

import javax.swing.JPanel;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class AMTReportPanel
{
	public String title = "";
	public JPanel panel;

	AMTReportPanel(String title, JPanel panel)
	{
		this.title = title;
		this.panel = panel;
	}
}
