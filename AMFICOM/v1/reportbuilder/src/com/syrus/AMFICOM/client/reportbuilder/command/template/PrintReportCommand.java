/*
 * $Id: PrintReportCommand.java,v 1.2 2005/09/13 12:23:11 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.command.template;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.report.ReportPrinter;
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderMainFrame;
import com.syrus.AMFICOM.report.ReportTemplate;

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
		Map<Object,Object> reportData = this.mainFrame.getTemplateRenderer().getDataForReport();
		ApplicationContext aContext = this.mainFrame.getContext();
		try {
			ReportPrinter.printReport(reportTemplate,reportData,aContext);
		} catch (CreateReportException e) {
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					e.getMessage(),
					LangModelReport.getString("report.Exception.error"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
}
