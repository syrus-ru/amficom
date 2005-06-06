package com.syrus.AMFICOM.client.map.command.map;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;

public class CreateMapReportCommand extends AbstractCommand
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



