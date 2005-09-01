/*
 * $Id: ReportTemplateRenderer.java,v 1.1 2005/09/01 14:21:40 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import com.syrus.AMFICOM.client.report.AttachedTextComponent;
import com.syrus.AMFICOM.client.report.CreateReportException;
import com.syrus.AMFICOM.client.report.ImageRenderingComponent;
import com.syrus.AMFICOM.client.report.Renderer;
import com.syrus.AMFICOM.client.report.RenderingComponent;
import com.syrus.AMFICOM.client.report.RenderingComponentsContainer;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportEvent;
import com.syrus.AMFICOM.client.reportbuilder.templaterenderer.ReportTemplateDataRenderingComponent;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.ImageStorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;

public class ReportTemplateRenderer extends Renderer implements PropertyChangeListener{
	public enum MODE {CREATE_LABEL,CREATE_IMAGE,NO_SPECIAL};
	
	protected MODE commands_processing_mode;
	
	public ReportTemplateRenderer(){
		this.addPropertyChangeListener(ReportEvent.TYPE,this);
	}
	
	@Override
	public void buildFromTemplate(ReportTemplate template, Map<String, Object> data) throws CreateReportException {
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

		for (ImageStorableElement imageSE : template.getImageStorableElements())
		{
			RenderingComponent component = new ImageRenderingComponent(imageSE,imageSE.getImage());
			componentsContainer.addRenderingComponent(component);
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (!(evt instanceof ReportEvent))
			return;
		
		String eventType = ((ReportEvent)evt).getEventType();
		if (eventType.equals(ReportEvent.LABEL_CREATION_STARTED))
			ReportTemplateRenderer.this.commands_processing_mode = MODE.CREATE_LABEL;
		else if (eventType.equals(ReportEvent.IMAGE_CREATION_STARTED))
			ReportTemplateRenderer.this.commands_processing_mode = MODE.CREATE_IMAGE;
		else if (	eventType.equals(ReportEvent.LABEL_CREATION_CANCELED)
				||	eventType.equals(ReportEvent.IMAGE_CREATION_CANCELED))
			ReportTemplateRenderer.this.commands_processing_mode = MODE.NO_SPECIAL;
	}
}
