package com.syrus.AMFICOM.Client.Analysis.Report;

import java.util.ArrayList;
import javax.swing.table.TableModel;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ScaledGraphPanel;

public class AnalysisReport
{
	ArrayList tables = new ArrayList();

	ArrayList panels = new ArrayList();

	public AnalysisReport()
	{
	}

	public void addReportTable(String title, TableModel model)
	{
		tables.add(new ReportTable(title, model));
	}

	public void addReportPanel(String title, ScaledGraphPanel panel)
	{
		panels.add(new ReportPanel(title, panel));
	}
}