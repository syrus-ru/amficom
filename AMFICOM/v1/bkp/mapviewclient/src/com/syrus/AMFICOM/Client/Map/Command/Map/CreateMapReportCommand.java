package com.syrus.AMFICOM.Client.Map.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.OpenTypedTemplateCommand;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Report.AMTReport;
import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.Map.Report.MapReportModel;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;

public class CreateMapReportCommand extends VoidCommand
{
	ApplicationContext aContext;

//  private MapMDIMain mainFrame = null;

	public CreateMapReportCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		CreateMapReportCommand rc = new CreateMapReportCommand(aContext);
//    rc.mainFrame = this.mainFrame;
    
		return rc;
	}

/*	public void setMainWindow(MapMDIMain value)
	{
		if (value != null)
		{
			mainFrame = value;
		}
	}*/

	public void execute()
	{
/*    if (mainFrame == null)
      return;*/

    MapReportModel mrm = new MapReportModel();
    AMTReport aReport = new AMTReport();
    
//		aReport.addRecord(OptimizationReportModel.sourceData,null);
		aReport.addRecord(
      mrm.getLangForField(MapReportModel.topology),
      MapFrame.getMapMainFrame().getMapViewer().getMapShot());
      
//		aReport.addRecord(MapReportModel.rep_topology,null);
//		aReport.addRecord(OptimizationReportModel.equipFeatures,null);
//		aReport.addRecord(OptimizationReportModel.optimizeResults,null);
//		aReport.addRecord(OptimizationReportModel.cost,null);

/*    if (mainFrame.iterHistFrame != null)
    	aReport.addRecord(
        orm.getLangForField(OptimizationReportModel.iterationsHistory),
        mainFrame.iterHistFrame.getItHistPanel());

    if (mainFrame.paramsFrame != null)        
      aReport.addRecord(
        orm.getLangForField(OptimizationReportModel.optimizationParams),
        mainFrame.paramsFrame.getTableModelForReport());

    if (mainFrame.solutionFrame != null)                
      aReport.addRecord(
        orm.getLangForField(OptimizationReportModel.solution),
        mainFrame.solutionFrame.getTableModelForReport());
    
    if (mainFrame.nodesModeFrame != null)                
      aReport.addRecord(
        orm.getLangForField(OptimizationReportModel.nodesOptimizeProperties),
        mainFrame.nodesModeFrame.getTableForReport());*/
      
		new OpenTypedTemplateCommand(aContext, ReportTemplate.rtt_Optimization,
																 aReport).execute();
	}
}



