/*
 * $Id: ReportTemplateNewCommand.java,v 1.1 2005/09/01 14:21:39 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.command.template;

import java.beans.PropertyChangeEvent;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.reportbuilder.ReportTemplateFactory;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.util.Log;

public class ReportTemplateNewCommand extends AbstractCommand {
	ApplicationContext aContext;

	public ReportTemplateNewCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		try {
			ReportTemplate reportTemplate = ReportTemplateFactory.createReportTemplate();
			
			ApplicationModel aModel = this.aContext.getApplicationModel(); 
			aModel.getCommand("menuWindowTemplateScheme").execute();
			aModel.getCommand("menuWindowTree").execute();
			
			aModel.getCommand(ApplicationModel.MENU_VIEW_ARRANGE).execute();
			
			this.aContext.getDispatcher().firePropertyChange(new PropertyChangeEvent(this, "reportTemplateId", null, reportTemplate.getId()));
			this.result = RESULT_OK;
		} 
		catch (CreateObjectException e) {
			Log.errorException(e);
		}
	}
}
