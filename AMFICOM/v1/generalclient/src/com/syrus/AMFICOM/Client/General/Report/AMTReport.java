package com.syrus.AMFICOM.Client.General.Report;

import java.util.ArrayList;
import javax.swing.table.TableModel;
import javax.swing.JPanel;

public class AMTReport
{
	public ArrayList tables = new ArrayList();

	public ArrayList panels = new ArrayList();

	public AMTReport()
	{
	}

	public void addReportTable(String title, TableModel model)
	{
		tables.add(new AMTReportTable(title, model));
	}

	public void addReportPanel(String title, JPanel panel)
	{
		panels.add(new AMTReportPanel(title, panel));
	}
}