package com.syrus.AMFICOM.Client.General.Command.Optimize;

import com.syrus.AMFICOM.Client.Map.Report.MapReportModel;
import com.syrus.AMFICOM.Client.Optimize.Report.OptimizationReportModel;
import com.syrus.AMFICOM.Client.Schematics.Report.SchemeReportModel;
import java.util.ArrayList;
import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Command.OpenTypedTemplateCommand;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.General.Report.AMTReport;

import com.syrus.AMFICOM.Client.Optimize.OptimizeMDIMain;

public class CreateOptimizeReportCommand extends VoidCommand
{
	ApplicationContext aContext;

  private OptimizeMDIMain mainFrame = null;

	public CreateOptimizeReportCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		CreateOptimizeReportCommand rc = new CreateOptimizeReportCommand(aContext);
    rc.mainFrame = this.mainFrame;
    
		return rc;
	}

	public void setMainWindow(OptimizeMDIMain value)
	{
		if (value != null)
		{
			mainFrame = value;
		}
	}

	public void execute()
	{
    if (mainFrame == null)
      return;

    OptimizationReportModel orm = new OptimizationReportModel();
    AMTReport aReport = new AMTReport();
    
//		aReport.addRecord(OptimizationReportModel.sourceData,null);
		aReport.addRecord(
      orm.getLangForField(SchemeReportModel.scheme),
      mainFrame.schemeFrame.schemePanel.scheme.getId());
      
//		aReport.addRecord(MapReportModel.rep_topology,null);
//		aReport.addRecord(OptimizationReportModel.equipFeatures,null);
//		aReport.addRecord(OptimizationReportModel.optimizeResults,null);
//		aReport.addRecord(OptimizationReportModel.cost,null);

    if (mainFrame.iterHistFrame != null)
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
        mainFrame.nodesModeFrame.getTableForReport());
      
		new OpenTypedTemplateCommand(aContext, ReportTemplate.rtt_Optimization,
																 aReport).execute();
	}
}



