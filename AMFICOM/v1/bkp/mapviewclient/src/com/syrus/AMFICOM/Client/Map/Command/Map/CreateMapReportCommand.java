package com.syrus.AMFICOM.Client.Map.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.OpenTypedTemplateCommand;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Report.AMTReport;
import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.Map.Report.MapReportModel;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPipePathElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMarker;

public class CreateMapReportCommand extends VoidCommand
{
	ApplicationContext aContext;
	AMTReport aReport = new AMTReport();
	MapReportModel mrm = new MapReportModel();

	public CreateMapReportCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		CreateMapReportCommand rc = new CreateMapReportCommand(aContext);
		rc.aReport = this.aReport;
		rc.mrm = this.mrm;
   
		return rc;
	}

	public void execute()
	{
		aReport.addRecord(
			  mrm.getLangForField(MapReportModel.topology),
			  MapFrame.getMapMainFrame().getMapViewer().getMapShot());

		MapElement element = MapFrame.getMapMainFrame().getMapViewer().getLogicalNetLayer().getCurrentMapElement();

		if(element instanceof MapCablePathElement)
		{
			aReport.addRecord(
			  mrm.getLangForField(MapReportModel.cableLayout),
			  element);
		}
		else
		if(element instanceof MapPhysicalLinkElement)
		{
			aReport.addRecord(
			  mrm.getLangForField(MapReportModel.tunnelCableList),
			  element);
		}
		else
		if(element instanceof MapMarker)
		{
			aReport.addRecord(
			  mrm.getLangForField(MapReportModel.markerInfo),
			  element);
		}
		else
		if(element instanceof MapSiteNodeElement)
		{
			aReport.addRecord(
			  mrm.getLangForField(MapReportModel.shaftInfo),
			  element);
		}
		else
		if(element instanceof MapPipePathElement)
		{
			aReport.addRecord(
			  mrm.getLangForField(MapReportModel.collectorInfo),
			  element);
		}
		
		OpenTypedTemplateCommand ottc = new OpenTypedTemplateCommand(
				aContext, 
				ReportTemplate.rtt_Map,
				aReport);
		ottc.execute();
	}
}



