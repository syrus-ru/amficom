/*
 * $Id: NewTemplateCommand.java,v 1.7 2005/11/16 18:49:45 max Exp $
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
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderApplicationModel;
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderMainFrame;
import com.syrus.AMFICOM.client.reportbuilder.TemplateTypeChooser;
import com.syrus.AMFICOM.client.reportbuilder.event.UseTemplateEvent;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.report.DestinationModules;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.util.Log;

public class NewTemplateCommand extends AbstractCommand {
	public static final String NEW_TEMPLATE_NAME = "report.Command.NewTemplate.newTemplateName";
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
			if (currentTemplate != null) {
				if (currentTemplate.isChanged()) {
					int saveChanges = JOptionPane.showConfirmDialog(
							Environment.getActiveWindow(),
							I18N.getString("report.Command.SaveTemplate.saveConfirmText"),
							I18N.getString("report.File.confirm"),
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.WARNING_MESSAGE);
					if (saveChanges == JOptionPane.YES_OPTION)
						this.aContext.getApplicationModel().getCommand(
								ReportBuilderApplicationModel.MENU_SAVE_REPORT).execute();
					else if (saveChanges == JOptionPane.NO_OPTION) {
						if (currentTemplate.isNew()) {
							StorableObjectPool.delete(currentTemplate.getId());
							try {
								StorableObjectPool.flush(
										currentTemplate.getId(),
										LoginManager.getUserId(),
										true);
							} catch (ApplicationException e) {
								Log.errorException(e);
							}
						}					
					}
					else if (saveChanges == JOptionPane.CANCEL_OPTION) {
						this.result = RESULT_CANCEL;
						return;					
					}
				}
				else {
					if (currentTemplate.isNew()) {
						StorableObjectPool.delete(currentTemplate.getId());
						try {
							StorableObjectPool.flush(
									currentTemplate.getId(),
									LoginManager.getUserId(),
									true);
						} catch (ApplicationException e) {
							Log.errorException(e);
						}
					}					
				}
			}
			
			String reportModelName = TemplateTypeChooser.chooseModule();
			if(reportModelName == null) {
				return;
			}
			
			ReportTemplate reportTemplate = ReportTemplate.createInstance(
					LoginManager.getUserId(),
					I18N.getString(NEW_TEMPLATE_NAME),
					"",
					reportModelName);
			reportTemplate.setNew(true);
			
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
