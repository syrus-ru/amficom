package com.syrus.AMFICOM.Client.General.Report;

import javax.swing.table.TableModel;

public class AMTReportTable
{
	public String title = "";
	public TableModel model;

	AMTReportTable(String title, TableModel model)
	{
		this.title = title;
		this.model = model;
	}
}
