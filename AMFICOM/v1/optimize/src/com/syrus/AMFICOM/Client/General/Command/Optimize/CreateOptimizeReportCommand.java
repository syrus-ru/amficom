package com.syrus.AMFICOM.Client.General.Command.Optimize;

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

    mainFrame.scheme;
    mainFrame.optimizerContext;
    mainFrame.kisSelectFrame;           // объект (окно), содержащй информацию о характеристиках и ценах оборудования
    mainFrame.iterHistFrame;       // окно графика хода оптимизации
    mainFrame.paramsFrame; // окно задания параметров оптимизации
    mainFrame.solutionFrame;            // окно подробной нитки маршрута одного из решений
    mainFrame.nodesModeFrame;// окно задания режимов узлов ( fixed , active )
    mainFrame.ribsModeFrame;  // окно задания режимов рёбер ( active )
    mainFrame.schemeFrame;                // окно отображения схемы
    mainFrame.mapFrame;                      // окно отображения схемы
      
		AMTReport report = new AMTReport();
    
//    report.addReportTable(OptimizationReportModel.alarms_list,alarmFrame.getTableModel());    

		new OpenTypedTemplateCommand(aContext, ReportTemplate.rtt_Optimization,
																 report).execute();
	}
}



