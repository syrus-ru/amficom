/*
 * $Id: TemplateParametersCommand.java,v 1.1 2005/09/07 08:43:25 peskovsky Exp $
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
import com.syrus.AMFICOM.client.reportbuilder.ReportTemplateFactory;
import com.syrus.AMFICOM.client.reportbuilder.TemplateParametersDialog;
import com.syrus.AMFICOM.client.reportbuilder.event.NewReportTemplateEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.util.Log;

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
