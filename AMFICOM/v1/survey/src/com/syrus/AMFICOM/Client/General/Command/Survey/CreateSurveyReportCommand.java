package com.syrus.AMFICOM.Client.General.Command.Survey;

import com.syrus.AMFICOM.Client.Survey.Report.ObserveReportModel;
import com.syrus.AMFICOM.Client.Survey.SurveyMDIMain;

import com.syrus.AMFICOM.Client.General.Command.OpenTypedTemplateCommand;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.General.Report.AMTReport;

public class CreateSurveyReportCommand extends VoidCommand
{
	ApplicationContext aContext;

  private SurveyMDIMain mainFrame = null;

	public CreateSurveyReportCommand(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		CreateSurveyReportCommand rc = new CreateSurveyReportCommand(aContext);
    rc.mainFrame = this.mainFrame;
    
		return rc;
	}

	public void setParameter(Object value)
	{
		if (value instanceof SurveyMDIMain)
		{
			mainFrame = (SurveyMDIMain)value;
		}
	}

	public void execute()
	{
		AMTReport report = new AMTReport();
    if (mainFrame == null)
      return;
      
    ObserveReportModel orModel = new ObserveReportModel();

//    Для отчёта по списку сигналов тревоги передаём
//    TableModel таблицы AlarmFrame        
    if (mainFrame.alarmsFrame != null)
      report.addRecord(
        orModel.getLangForField(ObserveReportModel.alarms_list),
        mainFrame.alarmsFrame.getTableModel());

    //Для отчёта по схеме передаём id схемы
    if (mainFrame.schemeViewerFrame != null)
        report.addRecord(
          orModel.getLangForField(ObserveReportModel.alarm_scheme),
          mainFrame.schemeViewerFrame.panel.scheme.getId());

    if (mainFrame.alarmPopupFrame != null)
        report.addRecord(
          orModel.getLangForField(ObserveReportModel.alarm_info),
          mainFrame.alarmPopupFrame.getDescriptionPane());
      
    if (mainFrame.resultFrame != null)
        report.addRecord(
          orModel.getLangForField(ObserveReportModel.alarm_reflectogramm),
          mainFrame.resultFrame.getReflectPicture());

		new OpenTypedTemplateCommand(aContext, ReportTemplate.rtt_Observe,
																 report).execute();
	}
}



