/*
 * $Id: ReportBuilder.java,v 1.1 2005/08/12 10:23:10 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import com.syrus.AMFICOM.report.AttachedTextRenderingElement;
import com.syrus.AMFICOM.report.DataRenderingElement;
import com.syrus.AMFICOM.report.ImageRenderingElement;
import com.syrus.AMFICOM.report.ReportTemplate;

/**
 * Реализует отчёт по шаблону
 * @author $Author: peskovsky $
 * @version $Revision: 1.1 $, $Date: 2005/08/12 10:23:10 $
 * @module reportclient_v1
 */
public class ReportBuilder {
	public Report build (ReportTemplate template,boolean fromAnotherModule)
		throws CreateReportException
	{
		Report resultReport = new Report();
		
		for (DataRenderingElement drElement : template.getDataRenderers())
		{
			String modelName = drElement.getModelClassName();
			ReportModel model = ReportModelPool.getModel(modelName);
			if (model == null)
				throw new CreateReportException (
						LangModelReport.getString(drElement.getReportName()),
						CreateReportException.reportModelIsAbsent);

			ReportComponent component = model.createReport(
					drElement,
					fromAnotherModule);
			
			resultReport.addComponent(component);
		}

		for (AttachedTextRenderingElement label : template.getLabels())
		{
			ReportComponent component = new AttachedTextComponent(label);
			resultReport.addComponent(component);
		}

		for (ImageRenderingElement image : template.getImages())
		{
			ReportComponent component = new ImageRenderingComponent(image);
			resultReport.addComponent(component);
		}
		
		ReportLayout.dolayout(resultReport);
		
		return resultReport;
	}
}
