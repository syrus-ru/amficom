/*
 * $Id: ReportTemplateRenderer.java,v 1.4 2005/09/07 08:43:25 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DropTarget;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
//import com.syrus.AMFICOM.client.report.RenderingComponentsContainer;
import com.syrus.AMFICOM.client.report.AttachedTextComponent;
import com.syrus.AMFICOM.client.report.DataRenderingComponent;
import com.syrus.AMFICOM.client.report.ImageRenderingComponent;
import com.syrus.AMFICOM.client.report.RenderingComponent;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.client.report.ReportModelPool;
import com.syrus.AMFICOM.client.report.ReportModel.ReportType;
import com.syrus.AMFICOM.client.reportbuilder.event.AttachLabelEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ComponentSelectionChangeEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.NewReportTemplateEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.client.reportbuilder.templaterenderer.RendererMode.MODE;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.ImageStorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.AMFICOM.report.StorableElement;
import com.syrus.AMFICOM.report.TableDataStorableElement;
import com.syrus.AMFICOM.report.ReportTemplate.ORIENTATION;
import com.syrus.AMFICOM.resource.IntDimension;

public class ReportTemplateRenderer extends JPanel implements PropertyChangeListener{
	private final static int BORDER_MARGIN_SIZE = 2;
	
	private ApplicationContext applicationContext; 
//	public RenderingComponentsContainer componentsContainer;
	private ReportTemplateRendererMouseListener mouseListener = null;
	
	private DropTarget dropTarget = null;	
	private ReportTemplateRendererDropTargetListener dropTargetListener = null;	
	
	private ReportTemplate template = null;
	private RenderingComponent selectedComponent = null;
	
	private AttachedTextComponent labelToBeAttached = null;
	private String labelAttachingType = null;
	
	private Rectangle templateBounds = new Rectangle();
	
	public ReportTemplateRenderer(){
//		this.componentsContainer = new RenderingComponentsContainer(template);
		jbInit();
	}
	
	public void setContext(ApplicationContext aContext){
		if (this.applicationContext != null) {
			this.applicationContext.getDispatcher().removePropertyChangeListener(ReportEvent.TYPE, this);
			this.removeMouseListener(this.mouseListener);
			this.dropTarget.removeDropTargetListener(this.dropTargetListener);
		}
		if (aContext != null) {
			this.applicationContext = aContext;
			this.applicationContext.getDispatcher().addPropertyChangeListener(ReportEvent.TYPE, this);
			this.mouseListener = new ReportTemplateRendererMouseListener(this,this.applicationContext);
			this.addMouseListener(this.mouseListener);
			
			this.dropTargetListener = new ReportTemplateRendererDropTargetListener(this,this.applicationContext);
			this.dropTarget = new DropTarget(this, this.dropTargetListener);
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
			else if (eventType.equals(ReportFlagEvent.SPECIAL_MODE_CANCELED))
				RendererMode.setMode(MODE.NO_SPECIAL);
			else if (eventType.equals(ReportFlagEvent.DELETE_OBJECT)) {
				ReportTemplateRenderer.this.removeRenderingComponent(
						this.selectedComponent);
				this.selectedComponent = null;				
				ReportTemplateRenderer.this.repaint();
			}
			else if (eventType.equals(ReportFlagEvent.TEMPLATE_PARAMETERS_CHANGED)) {
				IntDimension size = this.template.getSize();
				this.setSize(size.getWidth(),size.getHeight());
				this.setPreferredSize(this.getSize());				
				this.refreshTemplateBounds();
				ReportTemplateRenderer.this.repaint();				
			}			
			else if (eventType.equals(ReportFlagEvent.REPAINT_RENDERER))
				ReportTemplateRenderer.this.repaint();
		}
		else if (evt instanceof NewReportTemplateEvent){
			//TODO Запрос на сохранение.
			this.removeAll();
			
			this.setTemplate(((NewReportTemplateEvent)evt).getReportTemplate());
			DRIComponentMouseMotionListener.createInstance(this.applicationContext,this.templateBounds);
			DRIComponentMouseListener.createInstance(this.applicationContext);			
			ATComponentMouseMotionListener.createInstance(this.applicationContext,this.templateBounds);
			ATComponentMouseListener.createInstance(this.applicationContext);
			ATComponentKeyListener.createInstance(this.applicationContext);			
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
					
					textElement.setModified(System.currentTimeMillis());
					
					this.applicationContext.getDispatcher().firePropertyChange(
							new ReportFlagEvent(this,ReportFlagEvent.SPECIAL_MODE_CANCELED),true);			
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

	private void removeRenderingComponent(RenderingComponent component) {
		if (component instanceof DataRenderingComponent) {
			DataRenderingComponent drComponent =
				(DataRenderingComponent) component;
			StorableElement drElement = drComponent.getElement();
			
			drComponent.removeMouseListener(DRIComponentMouseListener.getInstance());
			drComponent.removeMouseMotionListener(DRIComponentMouseMotionListener.getInstance());
			
			for (Component object : this.getComponents()){
				//Удаляем привязанные надписи
				if (object instanceof AttachedTextComponent){
					AttachedTextComponent textComponent =
						(AttachedTextComponent)object;
					AttachedTextStorableElement storableElement =
						(AttachedTextStorableElement)textComponent.getElement();
					
					if (	drElement.equals(storableElement.getHorizontalAttacher())
						||	drElement.equals(storableElement.getVerticalAttacher()))
						this.removeRenderingComponent(textComponent);
				}
			}
		}
		else if (component instanceof AttachedTextComponent) {
			AttachedTextComponent atComponent =
				(AttachedTextComponent) component;
			
			atComponent.removeMouseListener(ATComponentMouseListener.getInstance());
			atComponent.removeMouseMotionListener(ATComponentMouseMotionListener.getInstance());
			atComponent.removeKeyListener(ATComponentKeyListener.getInstance());
			this.applicationContext.getDispatcher().removePropertyChangeListener(
					ReportEvent.TYPE,
					atComponent.getATPropertyChangeListener());
		}
		this.remove((JComponent)component);
	}
	
	public ReportTemplate getTemplate() {
		return this.template;
	}

	public void setTemplate(ReportTemplate template) {
		this.template = template;
		IntDimension size = this.template.getSize();
		this.setSize(size.getWidth(),size.getHeight());
		this.setPreferredSize(this.getSize());		
		
		this.refreshTemplateBounds();		
	}
	
	private void refreshTemplateBounds() {
		int templateMarginSize = this.template.getMarginSize();
		this.templateBounds.setLocation(
				new Point(templateMarginSize,templateMarginSize));
		IntDimension size = this.template.getSize();
		if (this.template.getOrientation().equals(ORIENTATION.PORTRAIT))
			this.templateBounds.setSize(
					size.getWidth() - 2 * templateMarginSize,
					size.getHeight() - 2 * templateMarginSize);
		else
			this.templateBounds.setSize(
					size.getHeight() - 2 * templateMarginSize,
					size.getWidth() - 2 * templateMarginSize);
	}
	
	@Override
	public void paintComponent (Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		
		if (this.template != null){
			//Рисуем поля шаблона
			g.setColor(Color.BLACK);
			g.drawRect(
					this.templateBounds.x,
					this.templateBounds.y,
					this.templateBounds.width,
					this.templateBounds.height);
			//Рисуем рамку выделенного элемента
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
	
	public ImageRenderingComponent createImageRenderingComponent(Point point) {
		String imageFileName = this.getImageFileName();
		if (imageFileName == null)
			return null;

		BufferedImage elementImage = null;
		boolean imageReadCorrectly = false;
		try {
			elementImage = ImageIO.read(new File(imageFileName));
			if (elementImage != null)
				imageReadCorrectly = true;
		} catch (IOException e1) {
		}
		
		if (!imageReadCorrectly) {
			//TODO Лэнги!!!
			JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				"Ошибка при чтении файла изображения.",
				"Ошибка",
				JOptionPane.ERROR_MESSAGE);
				
			this.applicationContext.getDispatcher().firePropertyChange(
				new ReportFlagEvent(this,ReportFlagEvent.SPECIAL_MODE_CANCELED));
				
			return null;
		}
					
		ImageStorableElement element = new ImageStorableElement();
		element.setLocation(point.x,point.y);
		Dimension defaultSize = ImageRenderingComponent.DEFAULT_IMAGE_SIZE;
		element.setSize(new IntDimension(defaultSize.width,defaultSize.height));
		element.setImage(elementImage);
		 
		//TODO Где создавать компонент?
		ImageRenderingComponent component = new ImageRenderingComponent(element,element.getImage());
//			this.renderer.componentsContainer.addRenderingComponent(textComponent);
		this.add(component);
		component.setLocation(element.getX(),element.getY());
		component.setSize(element.getWidth(),element.getHeight());
		component.addMouseListener(DRIComponentMouseListener.getInstance());
		component.addMouseMotionListener(DRIComponentMouseMotionListener.getInstance());
		
		this.applicationContext.getDispatcher().firePropertyChange(
			new ReportFlagEvent(this,ReportFlagEvent.SPECIAL_MODE_CANCELED));
		
		return component;
	}

	public AttachedTextComponent createTextRenderingComponent(Point point){
		AttachedTextStorableElement element = new AttachedTextStorableElement();
		//TODO Где создавать компонент?
		AttachedTextComponent component = new AttachedTextComponent(element);
//			this.renderer.componentsContainer.addRenderingComponent(textComponent);
		this.add(component);
		component.setLocation(point.x,point.y);
		component.setSize(new Dimension(AttachedTextComponent.MINIMUM_COMPONENT_SIZE));
		component.setBorder(AttachedTextComponent.DEFAULT_BORDER);
		
		component.addMouseListener(ATComponentMouseListener.getInstance());
		component.addMouseMotionListener(ATComponentMouseMotionListener.getInstance());
		component.addKeyListener(ATComponentKeyListener.getInstance());
		component.setATPropertyChangeListener(
				new ATComponentPropertyChangeListener(
						component,
						this.applicationContext,
						this.templateBounds));
		
		element.setLocation(point.x,point.y);
		element.setSize(
			AttachedTextComponent.MINIMUM_COMPONENT_SIZE.width,
			AttachedTextComponent.MINIMUM_COMPONENT_SIZE.height);
		
		this.applicationContext.getDispatcher().addPropertyChangeListener(
				ReportEvent.TYPE,
				component.getATPropertyChangeListener());

		return component;
	}
	
	public ReportTemplateDataRenderingComponent createReportTemplateDataRenderingComponent(
			String reportName,
			String reportModelName,			
			Point point){
		ReportModel reportModel = ReportModelPool.getModel(reportModelName);
		ReportType reportType = reportModel.getReportKind(reportName);
		
		DataStorableElement storableElement = null;
		if (reportType.equals(ReportType.TABLE))
			storableElement = new TableDataStorableElement(
					reportName,
					reportModelName,
					1);
		else
			storableElement = new DataStorableElement(
					reportName,
					reportModelName);

		ReportTemplateDataRenderingComponent component =
			new ReportTemplateDataRenderingComponent(storableElement);
		
		component.addMouseListener(DRIComponentMouseListener.getInstance());
		component.addMouseMotionListener(DRIComponentMouseMotionListener.getInstance());
			
		this.add(component);
		component.initMinimumSizes();
		
		Dimension defaultSize = ReportTemplateDataRenderingComponent.DEFAULT_SIZE;
		
		component.setLocation(point.x,point.y);
		component.setSize(new Dimension(defaultSize));
		
		storableElement.setLocation(point.x,point.y);
		storableElement.setSize(defaultSize.width,defaultSize.height);

		return component;
	}
	
	private String getImageFileName() {
		String fileName = null;
		JFileChooser fileChooser = new JFileChooser();

		String[] filters = {"bmp","gif","jpg","png"};
		ChoosableFileFilter bmpFilter = new ChoosableFileFilter(
				filters,
				"Image file formats");
		fileChooser.addChoosableFileFilter(bmpFilter);

		fileChooser.setDialogTitle("Выберите файл для чтения");
		fileChooser.setMultiSelectionEnabled(false);

		int option = fileChooser.showOpenDialog(Environment.getActiveWindow());
		if(option == JFileChooser.APPROVE_OPTION) {
			fileName = fileChooser.getSelectedFile().getPath();
			if(!	(	fileName.endsWith(".bmp")
					 || fileName.endsWith(".gif")
					 || fileName.endsWith(".jpg")
					 || fileName.endsWith(".png")))
				return null;
		}

		if(fileName == null)
			return null;

		if(!(new File(fileName)).exists())
			return null;

		return fileName;
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
