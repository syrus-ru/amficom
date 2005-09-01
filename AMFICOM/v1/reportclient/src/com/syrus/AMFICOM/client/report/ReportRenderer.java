/*
 * $Id: ReportRenderer.java,v 1.3 2005/09/01 14:21:22 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.util.Map;

import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.ImageStorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.util.Log;

/**
 * Реализует отчёт по шаблону
 * @author $Author: peskovsky $
 * @version $Revision: 1.3 $, $Date: 2005/09/01 14:21:22 $
 * @module reportclient_v1
 */
public class ReportRenderer extends Renderer{
	private static final long serialVersionUID = 6316228563298763509L;

	public void buildFromTemplate (
			ReportTemplate template,
			Map<String, Object> data)
		throws CreateReportException
	{
		RenderingComponentsContainer componentsContainer =
			new RenderingComponentsContainer(template);
		
		for (DataStorableElement dsElement : template.getDataStorableElements()){
			String dsElementLangName = LangModelReport.getString(dsElement.getReportName());
			String modelName = dsElement.getModelClassName();
			
			ReportModel model = ReportModelPool.getModel(modelName);
			if (model == null)
				throw new CreateReportException (
						dsElementLangName,
						CreateReportException.reportModelIsAbsent);
			
			Object dsElementData = data.get(dsElementLangName);
			if (dsElementData == null)
				throw new CreateReportException (
						dsElementLangName,
						CreateReportException.noDataToInstall);
			
			RenderingComponent component = model.createReport(
					dsElement,
					dsElementData);
			
			componentsContainer.addRenderingComponent(component);
		}

		for (AttachedTextStorableElement label : template.getTextStorableElements()){
			RenderingComponent component = new AttachedTextComponent(label);
			componentsContainer.addRenderingComponent(component);
		}

		for (ImageStorableElement imageElement : template.getImageStorableElements()){
			RenderingComponent component = new ImageRenderingComponent(imageElement,imageElement.getImage());
			componentsContainer.addRenderingComponent(component);
		}
		
		ReportLayout layout = new ReportLayout();
		layout.dolayout(componentsContainer);
	}
}
