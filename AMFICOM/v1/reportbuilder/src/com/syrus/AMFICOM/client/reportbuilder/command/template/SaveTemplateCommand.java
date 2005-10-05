/*
 * $Id: SaveTemplateCommand.java,v 1.4 2005/10/05 09:39:37 peskovsky Exp $
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
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderMainFrame;
import com.syrus.AMFICOM.client.reportbuilder.TemplateOpenSaveDialog;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.util.Log;

public class SaveTemplateCommand extends AbstractCommand {
	ApplicationContext aContext;
	ReportBuilderMainFrame mainFrame;

	public SaveTemplateCommand(
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
			String defaultLangName = LangModelReport.getString(NewTemplateCommand.NEW_TEMPLATE_NAME);
			if (currentTemplate.getName().equals(defaultLangName)) {
				TemplateOpenSaveDialog.saveTemplate(currentTemplate);
			}
			else {
				try {
					StorableObjectPool.putStorableObject(currentTemplate);
					StorableObjectPool.flush(currentTemplate,LoginManager.getUserId(),true);					
				} catch (ApplicationException e1) {
					Log.errorMessage("SaveTemplateCommand.execute | " + e1.getMessage());
					Log.errorException(e1);			
					JOptionPane.showMessageDialog(
							Environment.getActiveWindow(),
							LangModelReport.getString("report.Exception.saveTemplateError")
								+ " ("
								+ e1.getMessage()
								+ ").",
							LangModelReport.getString("report.Exception.error"),
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		this.result = RESULT_OK;
	}
}
