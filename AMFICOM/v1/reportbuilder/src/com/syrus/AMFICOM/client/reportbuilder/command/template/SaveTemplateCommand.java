/*
 * $Id: SaveTemplateCommand.java,v 1.2 2005/09/13 12:23:11 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.command.template;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderMainFrame;
import com.syrus.AMFICOM.client.reportbuilder.ReportTemplateFactory;
import com.syrus.AMFICOM.client.reportbuilder.TemplateOpenSaveDialog;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.report.ReportTemplate;

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
		String defaultLangName = LangModelReport.getString(ReportTemplateFactory.NEW_REPORT_NAME);
		if (currentTemplate.getName().equals(defaultLangName)) {
			TemplateOpenSaveDialog.saveTemplate(currentTemplate);
		}
		else {
			//TODO Здесь должно быть сохранение шаблона с указанным именем
			try {
				StorableObjectPool.putStorableObject(currentTemplate);
			} catch (IllegalObjectEntityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		currentTemplate.refreshModified();		
		this.result = RESULT_OK;
	}
}
