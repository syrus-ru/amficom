/*
 * $Id: SaveAsTemplateCommand.java,v 1.2 2005/09/13 12:23:11 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.command.template;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderMainFrame;
import com.syrus.AMFICOM.client.reportbuilder.TemplateOpenSaveDialog;
import com.syrus.AMFICOM.report.ReportTemplate;

public class SaveAsTemplateCommand extends AbstractCommand {
	ApplicationContext aContext;
	ReportBuilderMainFrame mainFrame;

	public SaveAsTemplateCommand(
			ApplicationContext aContext,
			ReportBuilderMainFrame mainFrame) {
		this.aContext = aContext;
		this.mainFrame = mainFrame;
	}

	@Override
	public void execute() {
		ReportTemplate currentTemplate = this.mainFrame.getTemplateRenderer().getTemplate();		
		TemplateOpenSaveDialog.saveTemplate(currentTemplate);
		currentTemplate.refreshModified();
		this.result = RESULT_OK;
	}
}
