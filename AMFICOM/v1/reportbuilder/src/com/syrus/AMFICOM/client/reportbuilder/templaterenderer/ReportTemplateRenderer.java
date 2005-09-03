/*
 * $Id: ReportTemplateRenderer.java,v 1.2 2005/09/03 12:42:20 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.syrus.AMFICOM.client.model.ApplicationContext;
//import com.syrus.AMFICOM.client.report.RenderingComponentsContainer;
import com.syrus.AMFICOM.client.report.AttachedTextComponent;
import com.syrus.AMFICOM.client.report.DataRenderingComponent;
import com.syrus.AMFICOM.client.report.RenderingComponent;
import com.syrus.AMFICOM.client.reportbuilder.event.AttachLabelEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ComponentSelectionChangeEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.NewReportTemplateEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.client.reportbuilder.templaterenderer.RendererMode.MODE;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.AMFICOM.resource.IntDimension;

public class ReportTemplateRenderer extends JPanel implements PropertyChangeListener{
	private final static int BORDER_MARGIN_SIZE = 2;
	
	private ApplicationContext aContext; 
//	public RenderingComponentsContainer componentsContainer;
	private ReportTemplateRendererMouseListener mouseListener = null;
	private ReportTemplate template = null;
	private RenderingComponent selectedComponent = null;
	
	private AttachedTextComponent labelToBeAttached = null;
	private String labelAttachingType = null;
	
	public ReportTemplateRenderer(){
//		this.componentsContainer = new RenderingComponentsContainer(template);
		jbInit();
	}
	
	public void setContext(ApplicationContext aContext){
		if (this.aContext != null) {
			this.aContext.getDispatcher().removePropertyChangeListener(ReportEvent.TYPE, this);
			this.removeMouseListener(this.mouseListener);			
		}
		if (aContext != null) {
			this.aContext = aContext;
			this.aContext.getDispatcher().addPropertyChangeListener(ReportEvent.TYPE, this);
			this.mouseListener = new ReportTemplateRendererMouseListener(this,this.aContext);
			this.addMouseListener(this.mouseListener);			
		}
	}
	
	private void jbInit(){
		this.setLayout(null);
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt instanceof ReportFlagEvent){
			String eventType = ((ReportFlagEvent)evt).getEventType();
			if (eventType.equals(ReportFlagEvent.LABEL_CREATION_STARTED))
				RendererMode.setMode(MODE.CREATE_LABEL);
			else if (eventType.equals(ReportFlagEvent.IMAGE_CREATION_STARTED))
				RendererMode.setMode(MODE.CREATE_IMAGE);
			else if (	eventType.equals(ReportFlagEvent.LABEL_CREATION_CANCELED)
					||	eventType.equals(ReportFlagEvent.IMAGE_CREATION_CANCELED))
				RendererMode.setMode(MODE.NO_SPECIAL);
			else if (eventType.equals(ReportFlagEvent.DELETE_OBJECT)) {
				this.remove((JComponent)this.selectedComponent);
				this.selectedComponent = null;				
				ReportTemplateRenderer.this.repaint();
			}
			else if (eventType.equals(ReportFlagEvent.REPAINT_RENDERER))
				ReportTemplateRenderer.this.repaint();
		}
		else if (evt instanceof NewReportTemplateEvent){
			//TODO Запрос на сохранение.
			this.removeAll();
			
			this.setTemplate(((NewReportTemplateEvent)evt).getReportTemplate());
			DRIComponentMouseMotionListener.createInstance(this.template,this.aContext);
			DRIComponentMouseListener.createInstance(this.aContext);			
			ATComponentMouseMotionListener.createInstance(this.aContext);
			ATComponentMouseListener.createInstance(this.aContext);
			ATComponentKeyListener.createInstance(this.aContext);			
		}
		else if (evt instanceof ComponentSelectionChangeEvent){
			RenderingComponent eventComponent = ((ComponentSelectionChangeEvent)evt).getRenderingComponent();
			if (RendererMode.getMode().equals(MODE.ATTACH_LABEL)) {
				if (eventComponent instanceof DataRenderingComponent){
					AttachedTextStorableElement textElement =
						(AttachedTextStorableElement)this.labelToBeAttached.getElement();
					DataStorableElement dataElement =
						(DataStorableElement)eventComponent.getElement();
					textElement.setAttachment(
							dataElement,
							this.labelAttachingType);
					RendererMode.setMode(MODE.NO_SPECIAL);
				}
			}
			else if (RendererMode.getMode().equals(MODE.NO_SPECIAL))
				this.selectedComponent = eventComponent;
		}
		else if (evt instanceof AttachLabelEvent) {
			AttachLabelEvent alEvent = (AttachLabelEvent)evt;
			this.labelToBeAttached = alEvent.getTextComponentToAttach();
			this.labelAttachingType = alEvent.getTextAttachingType();
			RendererMode.setMode(MODE.ATTACH_LABEL);
		}
	}

	public ReportTemplate getTemplate() {
		return this.template;
	}

	public void setTemplate(ReportTemplate template) {
		this.template = template;
	}
	@Override
	public void paintComponent (Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		
		if (this.template != null){
			g.setColor(Color.BLACK);
			IntDimension templateSize = this.template.getSize();
			int marginSize = this.template.getMarginSize();
			g.drawRect(
					marginSize,
					marginSize,
					templateSize.getWidth() - 2 * marginSize,
					templateSize.getHeight() - 2 * marginSize);
			
			if (this.selectedComponent != null){
				g.setColor(Color.BLUE);
				g.drawRect(
						this.selectedComponent.getX() - BORDER_MARGIN_SIZE,
						this.selectedComponent.getY() - BORDER_MARGIN_SIZE,
						this.selectedComponent.getWidth() + 2 * BORDER_MARGIN_SIZE,
						this.selectedComponent.getHeight() + 2 * BORDER_MARGIN_SIZE);
			}
		}
		this.paintChildren(g);
	}
////		RenderingComponentsContainer componentsContainer =
////		new RenderingComponentsContainer(template);
//	
//	for (DataStorableElement drElement : template.getDataStorableElements())
//	{
//		//Для элементов отображения данных создаём панельки,
//		//на которых отображаются названия отчётов
//		ReportTemplateDataRenderingComponent textComponent = 
//			new ReportTemplateDataRenderingComponent(drElement);
//
//		this.add(textComponent);
////		componentsContainer.addRenderingComponent(textComponent);
//	}
//
//	for (AttachedTextStorableElement label : template.getTextStorableElements())
//	{
//		AttachedTextComponent textComponent = new AttachedTextComponent(label);
//		this.add(textComponent);			
////		componentsContainer.addRenderingComponent(textComponent);
//	}
//
//	for (ImageStorableElement imageSE : template.getImageStorableElements())
//	{
//		ImageRenderingComponent textComponent = new ImageRenderingComponent(imageSE,imageSE.getImage());
//		this.add(textComponent);			
////		componentsContainer.addRenderingComponent(textComponent);
//	}
//		
}
