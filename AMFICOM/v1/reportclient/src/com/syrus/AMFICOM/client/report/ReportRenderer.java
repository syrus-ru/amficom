/*
 * $Id: ReportRenderer.java,v 1.2 2005/08/31 10:32:54 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.ImageStorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;

/**
 * Реализует отчёт по шаблону
 * @author $Author: peskovsky $
 * @version $Revision: 1.2 $, $Date: 2005/08/31 10:32:54 $
 * @module reportclient_v1
 */
public class ReportRenderer extends Renderer{
	private static final long serialVersionUID = 6316228563298763509L;

	public void buildFromTemplate (ReportTemplate template,boolean fromAnotherModule)
		throws CreateReportException
	{
		RenderingComponentsContainer componentsContainer =
			new RenderingComponentsContainer(template);
		
		for (DataStorableElement drElement : template.getDataStorableElements()){
			String modelName = drElement.getModelClassName();
			ReportModel model = ReportModelPool.getModel(modelName);
			if (model == null)
				throw new CreateReportException (
						LangModelReport.getString(drElement.getReportName()),
						CreateReportException.reportModelIsAbsent);

			RenderingComponent component = model.createReport(
					drElement,
					fromAnotherModule);
			
			componentsContainer.addRenderingComponent(component);
		}

		for (AttachedTextStorableElement label : template.getTextStorableElements()){
			RenderingComponent component = new AttachedTextComponent(label);
			componentsContainer.addRenderingComponent(component);
		}

		for (ImageStorableElement image : template.getImageStorableElements()){
			RenderingComponent component = new ImageRenderingComponent(image);
			componentsContainer.addRenderingComponent(component);
		}
		
		ReportLayout layout = new ReportLayout();
		layout.dolayout(componentsContainer);
	}
}
