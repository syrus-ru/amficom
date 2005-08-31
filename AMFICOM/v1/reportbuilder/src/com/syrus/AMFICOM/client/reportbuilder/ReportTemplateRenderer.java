/*
 * $Id: ReportTemplateRenderer.java,v 1.3 2005/08/31 10:29:03 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

import com.syrus.AMFICOM.client.report.AttachedTextComponent;
import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.ImageRenderingComponent;
import com.syrus.AMFICOM.client.report.Renderer;
import com.syrus.AMFICOM.client.report.RenderingComponent;
import com.syrus.AMFICOM.client.report.RenderingComponentsContainer;
import com.syrus.AMFICOM.client.reportbuilder.ReportTemplateDataRenderingComponent;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.ImageStorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;

public class ReportTemplateRenderer extends Renderer {

	@Override
	public void buildFromTemplate(ReportTemplate template, boolean fromAnotherModule) throws CreateReportException {
		RenderingComponentsContainer componentsContainer =
			new RenderingComponentsContainer(template);
		
		for (DataStorableElement drElement : template.getDataStorableElements())
		{
			//Для элементов отображения данных создаём панельки,
			//на которых отображаются названия отчётов
			ReportTemplateDataRenderingComponent component = 
				new ReportTemplateDataRenderingComponent(drElement);
	
			componentsContainer.addRenderingComponent(component);
		}

		for (AttachedTextStorableElement label : template.getTextStorableElements())
		{
			RenderingComponent component = new AttachedTextComponent(label);
			componentsContainer.addRenderingComponent(component);
		}

		for (ImageStorableElement image : template.getImageStorableElements())
		{
			RenderingComponent component = new ImageRenderingComponent(image);
			componentsContainer.addRenderingComponent(component);
		}
	}
}
