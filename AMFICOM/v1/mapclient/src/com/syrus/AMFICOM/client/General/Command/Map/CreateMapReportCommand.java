package com.syrus.AMFICOM.Client.General.Command.Map;

import java.util.ArrayList;
import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Command.OpenTypedTemplateCommand;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.General.Report.AMTReport;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;

import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;

import com.syrus.AMFICOM.Client.Map.NetMapViewer;
import com.syrus.AMFICOM.Client.Map.MapMainFrame;
import com.syrus.AMFICOM.Client.Map.Report.MapReportModel;
import com.syrus.AMFICOM.Client.Map.Report.MapRenderPanel;

public class CreateMapReportCommand extends VoidCommand
{
	public static final String MAP = "map";

	ApplicationContext aContext;
/*	ArrayList tableFrames = new ArrayList();
	ArrayList panels = new ArrayList();*/
  MapMainFrame mmf = null;

	public CreateMapReportCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		CreateMapReportCommand rc = new CreateMapReportCommand(aContext);
/*      for (Iterator it = tableFrames.iterator(); it.hasNext();)
		{
			ATableFrame tf = (ATableFrame)it.next();
			rc.setParameter(TABLE, tf);
		}*/
/*		for (Iterator it = panels.iterator(); it.hasNext();)
		{
			rc.setParameter(PANEL, it.next());
		}*/
    
    rc.setParameter(MAP,this.mmf);
		return rc;
	}

	public void setParameter(String key, Object value)
	{
		if (key.equals(MAP))
		{
			this.mmf = (MapMainFrame) value;
		}
	}

	public void execute()
	{
		AMTReport report = new AMTReport();

		try
		{
			MapRenderPanel mrp = new MapRenderPanel(mmf);

			report.addReportPanel(MapReportModel.rep_topology, mrp);

			new OpenTypedTemplateCommand(
        aContext,
        ReportTemplate.rtt_Map,
        report).execute();
		}
		catch (CreateReportException exc)
		{

		}
	}
}
