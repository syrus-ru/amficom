package com.syrus.AMFICOM.Client.Map.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.OpenTypedTemplateCommand;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Report.AMTReport;
import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
//import com.syrus.AMFICOM.Client.Map.Report.MapReportModel;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMarker;

public class CreateMapReportCommand extends VoidCommand
{
	ApplicationContext aContext;
//	AMTReport aReport = new AMTReport();
//	MapReportModel mrm = new MapReportModel();

	public CreateMapReportCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public void execute()
	{
/*
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
		if(element instanceof PhysicalLink)
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
		if(element instanceof SiteNode)
		{
			aReport.addRecord(
			  mrm.getLangForField(MapReportModel.shaftInfo),
			  element);
		}
		else
		if(element instanceof Collector)
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
*/
	}
}



