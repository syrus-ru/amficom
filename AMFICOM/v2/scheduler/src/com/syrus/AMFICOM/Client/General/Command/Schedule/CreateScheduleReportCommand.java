
package com.syrus.AMFICOM.Client.General.Command.Schedule;

//import com.syrus.AMFICOM.Client.Survey.Report.SheduleReportModel;
import com.syrus.AMFICOM.Client.Schedule.Report.ScheduleReportModel;
import com.syrus.AMFICOM.Client.Schedule.ScheduleMainFrame;

import com.syrus.AMFICOM.Client.General.Command.OpenTypedTemplateCommand;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Report.ReportTemplate;
import com.syrus.AMFICOM.Client.General.Report.AMTReport;

public class CreateScheduleReportCommand extends VoidCommand {

	ApplicationContext		aContext;

	private ScheduleMainFrame	mainFrame	= null;

	public CreateScheduleReportCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public Object clone() {
		CreateScheduleReportCommand rc = new CreateScheduleReportCommand(aContext);
		rc.mainFrame = this.mainFrame;

		return rc;
	}

	public void setParameter(Object value) {
		if (value instanceof ScheduleMainFrame) {
			this.mainFrame = (ScheduleMainFrame) value;
		}
	}

	public void execute() {
		AMTReport report = new AMTReport();
		if (this.mainFrame == null)
			return;

		ScheduleReportModel srModel = new ScheduleReportModel();

		//    Для отчёта по списку сигналов тревоги передаём
		//    TableModel таблицы AlarmFrame
		if (this.mainFrame.getPlanFrame() != null)
			report.addRecord(srModel.getLangForField(ScheduleReportModel.plan_panel), this.mainFrame
					.getPlanFrame().getPlanPanel());

		//    //Для отчёта по схеме передаём id схемы
		//    if (mainFrame.schemeViewerFrame != null)
		//        report.addRecord(
		//          orModel.getLangForField(ObserveReportModel.alarm_scheme),
		//          mainFrame.schemeViewerFrame.panel.scheme.getId());
		//
		//    if (mainFrame.alarmPopupFrame != null)
		//        report.addRecord(
		//          orModel.getLangForField(ObserveReportModel.alarm_info),
		//          mainFrame.alarmPopupFrame.getDescriptionPane());
		//      
		//    if (mainFrame.resultFrame != null)
		//        report.addRecord(
		//          orModel.getLangForField(ObserveReportModel.alarm_reflectogramm),
		//          mainFrame.resultFrame.getReflectPicture());

		new OpenTypedTemplateCommand(this.aContext, ReportTemplate.rtt_Scheduler, report).execute();
	}
}

