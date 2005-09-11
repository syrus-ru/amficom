package com.syrus.AMFICOM.client.observer.command;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.observer.ObserverMainFrame;

public class CreateObserverReportCommand extends AbstractCommand {
	ApplicationContext aContext;

	private final ObserverMainFrame mainFrame;

	public CreateObserverReportCommand(ApplicationContext aContext, ObserverMainFrame mainFrame) {
		this.aContext = aContext;
		this.mainFrame = mainFrame;
	}

	public void execute() {
/*
		AMTReport report = new AMTReport();
		if(mainFrame == null)
			return;

		ObserveReportModel orModel = new ObserveReportModel();

		// Для отчёта по списку сигналов тревоги передаём
		// TableModel таблицы AlarmFrame
		// if (mainFrame.alarmsFrame != null)
		// report.addRecord(
		// orModel.getLangForField(ObserveReportModel.alarms_list),
		// mainFrame.alarmsFrame.getTableModel());

		// Для отчёта по схеме передаём id схемы
		// if (mainFrame.schemeViewerFrame != null)
		// report.addRecord(
		// orModel.getLangForField(ObserveReportModel.alarm_scheme),
		// mainFrame.schemeViewerFrame.panel.scheme.getId());

		if(mainFrame.alarmPopupFrame != null)
			report.addRecord(
					orModel.getLangForField(ObserveReportModel.alarm_info),
					mainFrame.alarmPopupFrame.getDescriptionPane());

		if(mainFrame.resultFrame != null)
			report
					.addRecord(
							orModel
									.getLangForField(ObserveReportModel.alarm_reflectogramm),
							mainFrame.resultFrame.getReflectPicture());

		new OpenTypedTemplateCommand(
				aContext,
				ReportTemplate.rtt_Observe,
				report).execute();
*/
	}
}
