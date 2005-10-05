/*
 * $Id: OpenTemplateCommand.java,v 1.4 2005/10/05 09:39:37 peskovsky Exp $
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
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderApplicationModel;
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderMainFrame;
import com.syrus.AMFICOM.client.reportbuilder.TemplateOpenSaveDialog;
import com.syrus.AMFICOM.client.reportbuilder.event.UseTemplateEvent;
import com.syrus.AMFICOM.report.ReportTemplate;

public class OpenTemplateCommand extends AbstractCommand {
	ApplicationContext aContext;
	ReportBuilderMainFrame mainFrame;

	public OpenTemplateCommand(
			ApplicationContext aContext,
			ReportBuilderMainFrame mainFrame) {
		this.aContext = aContext;
		this.mainFrame = mainFrame;
	}

	@Override
	public void execute() {
		ReportTemplate currentTemplate = this.mainFrame.getTemplateRenderer().getTemplate();
		if (	currentTemplate != null
			&&	currentTemplate.isChanged()) {
			int saveChanges = JOptionPane.showConfirmDialog(
					Environment.getActiveWindow(),
					LangModelReport.getString("report.Command.SaveTemplate.saveConfirmText"),
					LangModelReport.getString("report.File.confirm"),
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (saveChanges == JOptionPane.YES_OPTION)
				this.aContext.getApplicationModel().getCommand(
						ReportBuilderApplicationModel.MENU_SAVE_REPORT).execute();
			else if (saveChanges == JOptionPane.CANCEL_OPTION)
				return;
		}
		
		ReportTemplate templateToOpen = TemplateOpenSaveDialog.openTemplate();
		if (templateToOpen == null) {
			this.result = RESULT_NO;			
			return;
		}
		this.aContext.getDispatcher().firePropertyChange(new UseTemplateEvent(this,templateToOpen));
		this.result = RESULT_OK;
	}
}
