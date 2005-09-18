/*
 * $Id: PrintReportCommand.java,v 1.5 2005/09/18 13:13:19 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.command.template;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.report.ReportException;
import com.syrus.AMFICOM.client.report.ReportPrinter;
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderMainFrame;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.util.Log;

public class PrintReportCommand extends AbstractCommand {
	ReportBuilderMainFrame mainFrame = null;

	public PrintReportCommand(
			ReportBuilderMainFrame mainFrame) {
		//Приходится передавать MainFrame - в момент инициализации команды
		//у меня ещё нет реализованного рендерера.
		this.mainFrame = mainFrame;
	}

	@Override
	public void execute() {
		ReportTemplate reportTemplate = this.mainFrame.getTemplateRenderer().getTemplate();
		ApplicationContext aContext = this.mainFrame.getContext();
		try {
			ReportPrinter.printReport(
					reportTemplate,
					ReportBuilderMainFrame.EMPTY_REPORT_DATA,
					aContext);
		} catch (ReportException e) {
			Log.errorMessage("PrintReportCommand.execute | " + e.getMessage());
			Log.errorException(e);			
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					e.getMessage(),
					LangModelReport.getString("report.Exception.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
}
