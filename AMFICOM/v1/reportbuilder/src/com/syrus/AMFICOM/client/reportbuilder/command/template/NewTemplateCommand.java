/*
 * $Id: NewTemplateCommand.java,v 1.2 2005/09/13 12:23:11 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.command.template;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderApplicationModel;
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderMainFrame;
import com.syrus.AMFICOM.client.reportbuilder.ReportTemplateFactory;
import com.syrus.AMFICOM.client.reportbuilder.event.UseTemplateEvent;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.util.Log;

public class NewTemplateCommand extends AbstractCommand {
	ApplicationContext aContext;
	ReportBuilderMainFrame mainFrame;
	
	public NewTemplateCommand(
			ApplicationContext aContext,
			ReportBuilderMainFrame mainFrame) {
		this.aContext = aContext;
		this.mainFrame = mainFrame;
	}

	@Override
	public void execute() {
		try {
			ReportTemplate currentTemplate = this.mainFrame.getTemplateRenderer().getTemplate();
			if (	currentTemplate != null
				&&	currentTemplate.isModified()) {
				int saveChanges = JOptionPane.showConfirmDialog(
						Environment.getActiveWindow(),
						LangModelReport.getString("report.Command.SaveTemplate.saveConfirmText"),
						LangModelReport.getString("report.File.confirm"),
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE);
				if (saveChanges == JOptionPane.OK_OPTION)
					this.aContext.getApplicationModel().getCommand(
							ReportBuilderApplicationModel.MENU_SAVE_REPORT).execute();
				else if (saveChanges == JOptionPane.CANCEL_OPTION) {
					this.result = RESULT_CANCEL;
					return;					
				}
			}
			
			ReportTemplate reportTemplate = ReportTemplateFactory.createReportTemplate();
			
			ApplicationModel aModel = this.aContext.getApplicationModel(); 
			aModel.getCommand(ReportBuilderApplicationModel.MENU_WINDOW_TEMPLATE_SCHEME).execute();
			aModel.getCommand(ReportBuilderApplicationModel.MENU_WINDOW_TREE).execute();
			
			aModel.getCommand(ApplicationModel.MENU_VIEW_ARRANGE).execute();
			
			this.aContext.getDispatcher().firePropertyChange(new UseTemplateEvent(this,reportTemplate));
			this.result = RESULT_OK;
		} 
		catch (CreateObjectException e) {
			Log.errorException(e);
		}
	}
}
