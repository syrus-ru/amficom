/*
 * $Id: SaveTemplateCommand.java,v 1.8 2005/11/16 18:50:58 max Exp $
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
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderMainFrame;
import com.syrus.AMFICOM.client.reportbuilder.TemplateOpenSaveDialog;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.report.AbstractDataStorableElement;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.ImageStorableElement;
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
		if (isChanged()) {
			String defaultLangName = I18N.getString(NewTemplateCommand.NEW_TEMPLATE_NAME);
			if (currentTemplate.getName().equals(defaultLangName)) {
				TemplateOpenSaveDialog.saveTemplate(currentTemplate);
			} else {
				try {
					StorableObjectPool.flush(currentTemplate,LoginManager.getUserId(),true);
					StorableObjectPool.flush(
							currentTemplate.getReverseDependencies(false),
							LoginManager.getUserId(),
							true);
					currentTemplate.setNew(false);
				} catch (ApplicationException e1) {
					Log.errorMessage("SaveTemplateCommand.execute | " + e1.getMessage());
					Log.errorException(e1);			
					JOptionPane.showMessageDialog(
							Environment.getActiveWindow(),
							I18N.getString("report.Exception.saveTemplateError")
								+ " ("
								+ e1.getMessage()
								+ ").",
							I18N.getString("report.Exception.error"),
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
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
