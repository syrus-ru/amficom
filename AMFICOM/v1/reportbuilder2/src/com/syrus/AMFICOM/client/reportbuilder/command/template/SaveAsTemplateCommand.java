/*
 * $Id: SaveAsTemplateCommand.java,v 1.1 2005/12/02 11:37:17 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.command.template;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderApplicationModel;
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderMainFrame;
import com.syrus.AMFICOM.client.reportbuilder.TemplateOpenSaveDialog;
import com.syrus.AMFICOM.client.reportbuilder.TemplateTypeChooser;
import com.syrus.AMFICOM.client.reportbuilder.event.UseTemplateEvent;
import com.syrus.AMFICOM.client.reportbuilder.templaterenderer.ReportTemplateRenderer;
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
		ReportTemplateRenderer renderer = this.mainFrame.getTemplateRenderer();
		ReportTemplate currentTemplate = renderer.getTemplate();		
		TemplateOpenSaveDialog.saveTemplate(currentTemplate);
		ReportTemplate newTemplate = TemplateOpenSaveDialog.getReportTemplate();
		TemplateTypeChooser.setType(newTemplate.getDestinationModule());

		ApplicationModel aModel = this.aContext.getApplicationModel(); 
		aModel.getCommand(ReportBuilderApplicationModel.MENU_WINDOW_TEMPLATE_SCHEME).execute();
		aModel.getCommand(ReportBuilderApplicationModel.MENU_WINDOW_TREE).execute();
		
		aModel.getCommand(ApplicationModel.MENU_VIEW_ARRANGE).execute();
		
		this.aContext.getDispatcher().firePropertyChange(new UseTemplateEvent(this,newTemplate));
		this.result = RESULT_OK;
	}
}
