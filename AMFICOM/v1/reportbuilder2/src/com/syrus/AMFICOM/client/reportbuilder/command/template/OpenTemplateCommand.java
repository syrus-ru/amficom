/*
 * $Id: OpenTemplateCommand.java,v 1.2 2006/05/23 15:41:59 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.command.template;

import java.util.Set;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderApplicationModel;
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderMainFrame;
import com.syrus.AMFICOM.client.reportbuilder.TemplateOpenSaveDialog;
import com.syrus.AMFICOM.client.reportbuilder.TemplateTypeChooser;
import com.syrus.AMFICOM.client.reportbuilder.event.UseTemplateEvent;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.report.AbstractDataStorableElement;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.ImageStorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.util.Log;

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
		if (isChanged()) {
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
						Log.errorMessage(e);
					}
				} else {
					StorableObjectPool.clean();
				}
			}
			else if (saveChanges == JOptionPane.CANCEL_OPTION)
				return;
		}
		
		ReportTemplate templateToOpen = TemplateOpenSaveDialog.openTemplate();
		if (templateToOpen == null) {
			this.result = RESULT_NO;			
			return;
		}
		
		TemplateTypeChooser.setType(templateToOpen.getDestinationModule());

		ApplicationModel aModel = this.aContext.getApplicationModel(); 
		aModel.getCommand(ReportBuilderApplicationModel.MENU_WINDOW_TEMPLATE_SCHEME).execute();
		aModel.getCommand(ReportBuilderApplicationModel.MENU_WINDOW_TREE).execute();
		
		aModel.getCommand(ApplicationModel.MENU_VIEW_ARRANGE).execute();
		
		this.aContext.getDispatcher().firePropertyChange(new UseTemplateEvent(this,templateToOpen));
		this.result = RESULT_OK;
	}
	
	private boolean isChanged() {
		ReportTemplate template = this.mainFrame.getTemplateRenderer().getTemplate();
		if (template == null) {
			return false;
		}
		if (template.isChanged()) {
			return true;
		}
		try {
			Set<AttachedTextStorableElement> attTexts = template.getAttachedTextStorableElements(false);
			Set<AbstractDataStorableElement> dataElements = template.getDataStorableElements(false);
			Set<ImageStorableElement> images = template.getImageStorableElements(false);
			
			for (AttachedTextStorableElement element : attTexts) {
				if (element.isChanged()) {
					return true;
				}
			}
			for (AbstractDataStorableElement element : dataElements) {
				if (element.isChanged()) {
					return true;
				}
			}
			for (ImageStorableElement element : images) {
				if (element.isChanged()) {
					return true;
				}
			}
		} catch (ApplicationException e) {
			//Never can happen
			Log.errorMessage(e);
		}
		return false;
	}
}
