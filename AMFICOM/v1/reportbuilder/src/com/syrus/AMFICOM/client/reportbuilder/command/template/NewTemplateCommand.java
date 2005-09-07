/*
 * $Id: NewTemplateCommand.java,v 1.1 2005/09/07 08:43:25 peskovsky Exp $
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
import com.syrus.AMFICOM.client.reportbuilder.ReportTemplateFactory;
import com.syrus.AMFICOM.client.reportbuilder.event.NewReportTemplateEvent;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.util.Log;

public class NewTemplateCommand extends AbstractCommand {
	ApplicationContext aContext;

	public NewTemplateCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		try {
			ReportTemplate reportTemplate = ReportTemplateFactory.createReportTemplate();
			
			ApplicationModel aModel = this.aContext.getApplicationModel(); 
			aModel.getCommand(ReportBuilderApplicationModel.MENU_WINDOW_TEMPLATE_SCHEME).execute();
			aModel.getCommand(ReportBuilderApplicationModel.MENU_WINDOW_TREE).execute();
			
			aModel.getCommand(ApplicationModel.MENU_VIEW_ARRANGE).execute();
			
			this.aContext.getDispatcher().firePropertyChange(new NewReportTemplateEvent(this,reportTemplate));
			this.result = RESULT_OK;
		} 
		catch (CreateObjectException e) {
			Log.errorException(e);
		}
	}
}
