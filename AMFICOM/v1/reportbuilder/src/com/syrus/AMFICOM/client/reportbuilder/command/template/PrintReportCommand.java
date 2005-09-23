/*
 * $Id: PrintReportCommand.java,v 1.6 2005/09/23 12:10:59 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.command.template;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.report.ReportPrinter;
import com.syrus.AMFICOM.client.report.ReportRenderer;
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderMainFrame;
import com.syrus.AMFICOM.report.ReportTemplate;

public class PrintReportCommand extends AbstractCommand {
	ReportBuilderMainFrame mainFrame = null;

	public PrintReportCommand(
			ReportBuilderMainFrame mainFrame) {
		//���������� ���������� MainFrame - � ������ ������������� �������
		//� ���� ��� ��� �������������� ���������.
		this.mainFrame = mainFrame;
	}

	@Override
	public void execute() {
		ReportRenderer reportRenderer = this.mainFrame.getReportRenderer();
		if (reportRenderer == null)
			return;
		
		ReportTemplate reportTemplate = this.mainFrame.getTemplateRenderer().getTemplate();
		ReportPrinter.printReport(
				reportRenderer,
				reportTemplate);
	}
}
