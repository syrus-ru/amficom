package com.syrus.AMFICOM.Client.General.Command.Analysis;

import java.util.ArrayList;
import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Command.OpenTypedTemplateCommand;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.General.Report.AMTReport;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ATableFrame;
import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.SimpleResizableFrame;

public class CreateAnalysisReportCommand extends VoidCommand
{
	public static final String TABLE = "table";
	public static final String PANEL = "panel";
	public static final String TYPE = "type";

	private ApplicationContext aContext;
	private ArrayList tableFrames = new ArrayList();
	private ArrayList panels = new ArrayList();
  private String type = "";

	public CreateAnalysisReportCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		CreateAnalysisReportCommand rc = new CreateAnalysisReportCommand(aContext);
		for (Iterator it = tableFrames.iterator(); it.hasNext();)
		{
			ATableFrame tf = (ATableFrame)it.next();
			rc.setParameter(TABLE, tf);
		}
		for (Iterator it = panels.iterator(); it.hasNext();)
		{
			SimpleResizableFrame rf = (SimpleResizableFrame)it.next();
			rc.setParameter(PANEL, rf);
		}

		return rc;
	}

	public void setParameter(String key, Object value)
	{
		if (key.equals(TABLE) && value instanceof ATableFrame)
		{
			tableFrames.add(value);
		}
		else if (key.equals(PANEL) && value instanceof SimpleResizableFrame)
		{
			panels.add(value);
		}
		else if (key.equals(TYPE))
		{
			type = (String)value;
		}
	}

	public void execute()
	{
		AMTReport report = new AMTReport();
		for (Iterator it = tableFrames.iterator(); it.hasNext();)
		{
			ATableFrame tf = (ATableFrame)it.next();
			report.addRecord(tf.getReportTitle(),	tf.getTableModel());
		}

		for (Iterator it = panels.iterator(); it.hasNext();)
		{
			SimpleResizableFrame rf = (SimpleResizableFrame)it.next();
			report.addRecord(rf.getReportTitle(), rf.getTopGraphPanel());
		}

		new OpenTypedTemplateCommand(aContext, type,
																 report).execute();
	}
}



