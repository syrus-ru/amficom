/*
 * $Id: SaveReportCommand.java,v 1.1.1.1 2005/12/02 11:37:17 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.command.template;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.report.HTMLReportEncoder;
import com.syrus.AMFICOM.client.report.ReportRenderer;
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderMainFrame;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.util.Log;

public class SaveReportCommand extends AbstractCommand {
	ReportBuilderMainFrame mainFrame = null;

	public SaveReportCommand(
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

		ReportTemplate reportTemplate = this.mainFrame.getTemplateRenderer().getTemplate();		
		HTMLReportEncoder encoder = new HTMLReportEncoder(
			reportRenderer.getRenderingComponents(),
			reportTemplate);
				
		try {
			encoder.encodeToHTML();	
			this.result = RESULT_OK;
		} catch (IOException e) {
			Log.errorMessage("SaveReportCommand.execute | " + e.getMessage());
			Log.errorException(e);			
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					I18N.getString("report.Exception.errorSavingHTML")
						+ " ("
						+ e.getMessage()
						+ ").",
					I18N.getString("report.Exception.error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
