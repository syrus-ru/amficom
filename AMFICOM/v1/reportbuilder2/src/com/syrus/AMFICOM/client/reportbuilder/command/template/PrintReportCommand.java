/*
 * $Id: PrintReportCommand.java,v 1.2 2006/04/24 07:27:30 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.command.template;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.report.ReportRenderer;
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderMainFrame;
import com.syrus.util.PrintUtilities;

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
		ReportRenderer reportRenderer = this.mainFrame.getReportRenderer();
		if (reportRenderer == null)
			return;
		
		this.mainFrame.getTemplateRenderer().getTemplate();
		PrintUtilities.printComponent(reportRenderer);
	}
}
