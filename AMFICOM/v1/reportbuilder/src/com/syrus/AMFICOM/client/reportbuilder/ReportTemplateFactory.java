package com.syrus.AMFICOM.client.reportbuilder;

import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.report.DestinationModules;
import com.syrus.AMFICOM.report.ReportTemplate;

/*
 * $Id: ReportTemplateFactory.java,v 1.5 2005/09/13 12:23:11 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

public class ReportTemplateFactory {
	public static final String NEW_REPORT_NAME = "report.Command.NewTemplate.newTemplateName";
	
	public static final ReportTemplate createReportTemplate() throws CreateObjectException{
		ReportTemplate reportTemplate = new ReportTemplate();
		
		reportTemplate.setName(LangModelReport.getString(NEW_REPORT_NAME));
		reportTemplate.setSize(ReportTemplate.A4);
		reportTemplate.setMarginSize(ReportTemplate.STANDART_MARGIN_SIZE);		
		reportTemplate.setDestinationModule(DestinationModules.UNKNOWN_MODULE);
//		try {
//			//TODO Правильный код сюда нужен!
//			reportTemplate.setId(IdentifierPool.getGeneratedIdentifier(REPORT_TEMPLATE_CODE));
//		} catch (IdentifierGenerationException e) {
//			throw new CreateObjectException ("ReportTemplateFactory.createReportTemplate() | cannot generate identifier ", e);
//		}
		
		return reportTemplate;
	}
}
