/*
 * $Id: TemplateParametersCommand.java,v 1.1.1.1 2005/12/02 11:37:17 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.command.template;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderMainFrame;
import com.syrus.AMFICOM.client.reportbuilder.TemplateParametersDialog;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.report.ReportTemplate;

public class TemplateParametersCommand extends AbstractCommand {
	ApplicationContext aContext;
	ReportBuilderMainFrame mainFrame = null;

	public TemplateParametersCommand(
			ApplicationContext aContext,
			ReportBuilderMainFrame mainFrame) {
		this.aContext = aContext;
		//Приходится передавать MainFrame - в момент инициализации команды
		//у меня ещё нет реализованного рендерера.
		this.mainFrame = mainFrame;
	}

	@Override
	public void execute() {
		ReportTemplate reportTemplate = this.mainFrame.getTemplateRenderer().getTemplate();
		if (reportTemplate == null)
			return;

		//TODO Где-нить нужно предупреждение, что изменение параметров
		//приведёт к искажению данных.
		TemplateParametersDialog dialog = TemplateParametersDialog.getInstance(reportTemplate);
		dialog.setVisible(true);
		
		this.aContext.getDispatcher().firePropertyChange(
				new ReportFlagEvent(this,ReportFlagEvent.TEMPLATE_PARAMETERS_CHANGED));
		this.result = RESULT_OK;
	}
}
