package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.util.ArrayList;
import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Command.OpenTypedTemplateCommand;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.General.Report.AMTReport;

public class CreateSchemeReportCommand extends VoidCommand
{
	public static final String TABLE = "table";
	public static final String PANEL = "panel";
	public static final String TYPE = "type";

	ApplicationContext aContext;
	ArrayList tableFrames = new ArrayList();
	ArrayList panels = new ArrayList();
	String type = "";

	public CreateSchemeReportCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		CreateSchemeReportCommand rc = new CreateSchemeReportCommand(aContext);
		for (Iterator it = panels.iterator(); it.hasNext();)
		{
			UgoPanel rf = (UgoPanel)it.next();
			rc.setParameter(PANEL, rf);
		}
		rc.type = type;
		return rc;
	}

	public void setParameter(String key, Object value)
	{
		if (key.equals(PANEL) && value instanceof UgoPanel)
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
		for (Iterator it = panels.iterator(); it.hasNext();)
		{
			UgoPanel rf = (UgoPanel)it.next();
			report.addReportPanel(rf.getReportTitle(), rf);
		}

		new OpenTypedTemplateCommand(aContext, type,
																 report).execute();
	}
}




