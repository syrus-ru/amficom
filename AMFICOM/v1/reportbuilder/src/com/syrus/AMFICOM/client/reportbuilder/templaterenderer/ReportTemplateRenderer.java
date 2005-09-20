/*
 * $Id: ReportTemplateRenderer.java,v 1.11 2005/09/20 09:25:54 peskovsky Exp $
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
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.report.AttachedTextComponent;
import com.syrus.AMFICOM.client.report.CreateModelException;
import com.syrus.AMFICOM.client.report.DataRenderingComponent;
import com.syrus.AMFICOM.client.report.ImageRenderingComponent;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.report.RenderingComponent;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.client.report.ReportModelPool;
import com.syrus.AMFICOM.client.report.ReportModel.ReportType;
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderApplicationModel;
import com.syrus.AMFICOM.client.reportbuilder.event.AttachLabelEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ComponentSelectionChangeEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.UseTemplateEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportQuickViewEvent;
import com.syrus.AMFICOM.client.reportbuilder.templaterenderer.RendererMode.RENDERER_MODE;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.ImageStorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.AMFICOM.report.StorableElement;
import com.syrus.AMFICOM.report.TableDataStorableElement;
import com.syrus.AMFICOM.report.TextAttachingType;
import com.syrus.AMFICOM.report.ReportTemplate.ORIENTATION;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.util.Log;

public class ReportTemplateRenderer extends JPanel implements PropertyChangeListener{
	private final static int BORDER_MARGIN_SIZE = 2;
	private static final int HEADER_TOCOMPONENT_DISTANCE = 10;	
	
	private ApplicationContext applicationContext; 
	private ReportTemplateRendererMouseListener mouseListener = null;
	
	private DropTarget dropTarget = null;	
	private ReportTemplateRendererDropTargetListener dropTargetListener = null;	
	
	private ReportTemplate template = null;
	
	private RenderingComponent selectedComponent = null;
	
	private AttachedTextComponent labelToBeAttached = null;
	private String labelAttachingType = null;
	
	private Rectangle marginBounds = new Rectangle();
	private Rectangle templateBounds = new Rectangle();	
	
	public ReportTemplateRenderer(){
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
			this.applicationContext.getDispatcher().addPropertyChangeListener(
					ReportEvent.TYPE,
					this);
			this.mouseListener = new ReportTemplateRendererMouseListener(
					this,
					this.applicationContext);
			this.addMouseListener(this.mouseListener);
			
			this.dropTargetListener = new ReportTemplateRendererDropTargetListener(
					this,
					this.applicationContext);
			this.dropTarget = new DropTarget(this, this.dropTargetListener);
			this.dropTarget.setActive(true);
		}

		for (Component object : this.getComponents())
			if (object instanceof RTEDataRenderingComponent){
				((RTEDataRenderingComponent)object).setContext(
						this.applicationContext);
			}
	}
	
	private void jbInit(){
		this.setLayout(null);
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt instanceof ReportFlagEvent){
			String eventType = ((ReportFlagEvent)evt).getEventType();
			if (eventType.equals(ReportFlagEvent.LABEL_CREATION_STARTED))
				RendererMode.setMode(RENDERER_MODE.CREATE_LABEL);
			else if (eventType.equals(ReportFlagEvent.IMAGE_CREATION_STARTED))
				RendererMode.setMode(RENDERER_MODE.CREATE_IMAGE);
			else if (eventType.equals(ReportFlagEvent.SPECIAL_MODE_CANCELED))
				RendererMode.setMode(RENDERER_MODE.NO_SPECIAL);
			else if (eventType.equals(ReportFlagEvent.DELETE_OBJECT)) {
				ReportTemplateRenderer.this.removeRenderingComponent(
						this.selectedComponent);
				this.selectedComponent = null;				
				ReportTemplateRenderer.this.repaint();
			}
			else if (eventType.equals(ReportFlagEvent.TEMPLATE_PARAMETERS_CHANGED)) {
				this.refreshTemplateBounds();
				ReportTemplateRenderer.this.repaint();				
			}			
			else if (eventType.equals(ReportFlagEvent.REPAINT_RENDERER))
				ReportTemplateRenderer.this.repaint();
		}
		else if (evt instanceof UseTemplateEvent){
			this.removeAllComponents();
			
			try {
				this.setTemplate(((UseTemplateEvent)evt).getReportTemplate());
			} catch (Exception e) {
				Log.errorMessage("ReportTemplateRenderer.propertyChange | " + e.getMessage());
				Log.errorException(e);			
				JOptionPane.showMessageDialog(
						Environment.getActiveWindow(),
						e.getMessage(),
						LangModelReport.getString("report.Exception.error"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			DRIComponentMouseMotionListener.createInstance(this.applicationContext,this.marginBounds);
			DRIComponentMouseListener.createInstance(this.applicationContext);			
			ATComponentMouseMotionListener.createInstance(this.applicationContext,this.marginBounds);
			ATComponentMouseListener.createInstance(this.applicationContext);
			ATComponentKeyListener.createInstance(this.applicationContext);			
		}
		else if (evt instanceof ReportQuickViewEvent){
			Object reportObject = ((ReportQuickViewEvent)evt).getReportObject();
			Map<String,String> reportObjectAttributes = 
				ReportDataChecker.getObjectReportAttributes(reportObject);
			if (reportObjectAttributes == null)
				return;
			
			Identifier reportObjectId = ((StorableObject)reportObject).getId();			
			//Для быстрого просмотра содержимое рабочего поля стирается
			//и вместо него лепится один элемент.
			Command command =
				this.applicationContext.getApplicationModel().getCommand(
					ReportBuilderApplicationModel.MENU_REPORT_TEMPLATE_SAVE);
			command.execute();
			if (command.getResult() == Command.RESULT_CANCEL)
				return;
			
			this.removeAllComponents();
			
			try {
				this.createDataComponentWithText(
						reportObjectAttributes.get(ReportDataChecker.REPORT_NAME),
						reportObjectAttributes.get(ReportDataChecker.MODEL_CLASS_NAME),
						reportObjectId,
						new Point(this.marginBounds.x + 5,this.marginBounds.y + 50),
						new Dimension(this.marginBounds.width - 10,300));
			} catch (Exception e) {
				Log.errorMessage("ReportTemplateRenderer.propertyChange | " + e.getMessage());
				Log.errorException(e);			
				JOptionPane.showMessageDialog(
						Environment.getActiveWindow(),
						e.getMessage(),
						LangModelReport.getString("report.Exception.error"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			this.applicationContext.getDispatcher().firePropertyChange(
					new ReportFlagEvent(this,ReportFlagEvent.CHANGE_VIEW));
		}
		else if (evt instanceof ComponentSelectionChangeEvent){
			RenderingComponent eventComponent = ((ComponentSelectionChangeEvent)evt).getRenderingComponent();
			if (RendererMode.getMode().equals(RENDERER_MODE.ATTACH_LABEL)) {
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
			else if (RendererMode.getMode().equals(RENDERER_MODE.NO_SPECIAL))
				this.selectedComponent = eventComponent;
		}
		else if (evt instanceof AttachLabelEvent) {
			AttachLabelEvent alEvent = (AttachLabelEvent)evt;
			this.labelToBeAttached = alEvent.getTextComponentToAttach();
			this.labelAttachingType = alEvent.getTextAttachingType();
			RendererMode.setMode(RENDERER_MODE.ATTACH_LABEL);
		}
	}

	private void removeAllComponents() {
		for (int i = 0; i < this.getComponentCount(); i++) {
			this.removeRenderingComponent(
					(RenderingComponent)this.getComponent(i));
		}
	}
	
	private void removeRenderingComponent(RenderingComponent component) {
		if (component instanceof DataRenderingComponent) {
			DataRenderingComponent drComponent =
				(DataRenderingComponent) component;
			StorableElement drElement = drComponent.getElement();
			
			drComponent.removeMouseListener(DRIComponentMouseListener.getInstance());
			drComponent.removeMouseMotionListener(DRIComponentMouseMotionListener.getInstance());
			if (drComponent instanceof RTEDataRenderingComponent)
				((RTEDataRenderingComponent)drComponent).removeDropTargetListener();
			
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
		this.template.removeElement(component.getElement());
	}
	
	public ReportTemplate getTemplate() {
		return this.template;
	}

	public void setTemplate(ReportTemplate template) throws CreateModelException, ApplicationException {
		this.template = template;
		this.refreshTemplateBounds();
		
		for (DataStorableElement dataElement : this.template.getDataStorableElements()) {
			this.createReportTemplateDataRenderingComponent(
					dataElement.getReportName(),
					dataElement.getModelClassName(),
					dataElement.getReportObjectId(),
					new Point(dataElement.getX(),dataElement.getY()),
					new Dimension(dataElement.getWidth(),dataElement.getHeight()));
		}

		for (AttachedTextStorableElement textElement : this.template.getTextStorableElements()) {
			AttachedTextComponent component = this.createTextRenderingComponent(
					new Point(textElement.getX(),textElement.getY()));
			component.setText(textElement.getText());
			component.setLocation(textElement.getX(),textElement.getY());
			component.setSize(textElement.getWidth(),textElement.getHeight());
			component.setBorder(DataRenderingComponent.DEFAULT_BORDER);
		}

		for (ImageStorableElement imageElement : this.template.getImageStorableElements()) {
			ImageRenderingComponent component = this.createImageRenderingComponent(
					new Point(imageElement.getX(),imageElement.getY()));
			component.setLocation(imageElement.getX(),imageElement.getY());
			component.setSize(imageElement.getWidth(),imageElement.getHeight());			
			this.add(component);
		}
	}
	
	private void refreshTemplateBounds() {
		IntDimension size = this.template.getSize();
		
		if (this.template.getOrientation().equals(ORIENTATION.PORTRAIT))
			this.setSize(size.getWidth(),size.getHeight() * 2);
		else
			this.setSize(2 * size.getHeight(),size.getWidth());
		this.setPreferredSize(this.getSize());				
		
		int templateMarginSize = this.template.getMarginSize();
		this.marginBounds.setLocation(
				new Point(templateMarginSize,templateMarginSize));
		this.templateBounds.setLocation(
				new Point(2,2));

		if (this.template.getOrientation().equals(ORIENTATION.PORTRAIT)){
			this.marginBounds.setSize(
					size.getWidth() - 2 * templateMarginSize,
					2 * size.getHeight() - 2 * templateMarginSize);
			this.templateBounds.setSize(
					size.getWidth() - 4,
					2 * size.getHeight() - 4);
		}
		else {
			this.marginBounds.setSize(
					2 * size.getHeight() - 2 * templateMarginSize,
					size.getWidth() - 2 * templateMarginSize);
			this.templateBounds.setSize(
					2 * size.getHeight() - 4,
					size.getWidth() - 4);
		}
	}
	
	@Override
	public void paintComponent (Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		
		if (this.template != null){
			//Рисуем края шаблона			
			g.setColor(Color.BLACK);
			g.drawRect(
					this.templateBounds.x,
					this.templateBounds.y,
					this.templateBounds.width,
					this.templateBounds.height);
			
			//Рисуем поля шаблона
			g.setColor(Color.BLACK);
			g.drawRect(
					this.marginBounds.x,
					this.marginBounds.y,
					this.marginBounds.width,
					this.marginBounds.height);
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
			JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				LangModelReport.getString("report.Exception.errorReadingImage"),
				LangModelReport.getString("report.Exception.error"),
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
		this.template.addElement(element);
		 
		ImageRenderingComponent component = new ImageRenderingComponent(element,element.getImage());
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
		element.setFont(AttachedTextComponent.DEFAULT_FONT);
		AttachedTextComponent component = new AttachedTextComponent(element);
		this.add(component);
		component.setLocation(point.x,point.y);
		component.setSize(new Dimension(AttachedTextComponent.MINIMUM_COMPONENT_SIZE));
		component.setBorder(DataRenderingComponent.DEFAULT_BORDER);
		
		component.addMouseListener(ATComponentMouseListener.getInstance());
		component.addMouseMotionListener(ATComponentMouseMotionListener.getInstance());
		component.addKeyListener(ATComponentKeyListener.getInstance());
		component.setATPropertyChangeListener(
				new ATComponentPropertyChangeListener(
						component,
						this.applicationContext,
						this.marginBounds));
		
		element.setLocation(point.x,point.y);
		element.setSize(
			AttachedTextComponent.MINIMUM_COMPONENT_SIZE.width,
			AttachedTextComponent.MINIMUM_COMPONENT_SIZE.height);
		this.template.addElement(element);		
		
		this.applicationContext.getDispatcher().addPropertyChangeListener(
				ReportEvent.TYPE,
				component.getATPropertyChangeListener());

		return component;
	}
	
	public RTEDataRenderingComponent createReportTemplateDataRenderingComponent(
			String reportName,
			String reportModelName,
			Identifier reportObjectId,
			Point location,
			Dimension size) throws CreateModelException, ApplicationException {
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
		storableElement.setReportObjectId(reportObjectId);		

		RTEDataRenderingComponent component =
			new RTEDataRenderingComponent(storableElement);
		
		component.addMouseListener(DRIComponentMouseListener.getInstance());
		component.addMouseMotionListener(DRIComponentMouseMotionListener.getInstance());
		
		this.add(component);
		component.refreshLabels();
		
		component.setLocation(location.x,location.y);
		if (size == null)
			component.setSize(new Dimension(
					RTEDataRenderingComponent.DEFAULT_SIZE));
		else
			component.setSize(size);
		
		component.setContext(this.applicationContext);
		
		storableElement.setLocation(location.x,location.y);
		storableElement.setSize(component.getWidth(),component.getHeight());
		this.template.addElement(storableElement);

		return component;
	}
	/**
	 * Создаёт элемент отображения данных с заголовком, привязанным сверху,
	 * по центру.
	 * @param reportName Имя элемента шаблона
	 * @param reportModelName Имя класса модели отчётов для элемента
	 * @param reportObjectId Дополнительные данные
	 * @param location Расположение
	 * @param size Размер или null для размера по умолчанию
	 * @throws CreateModelException 
	 * @throws ApplicationException 
	 */
	public void createDataComponentWithText(
			String reportName,
			String reportModelName,
			Identifier reportObjectId,
			Point location,
			Dimension size) throws CreateModelException, ApplicationException {
		RTEDataRenderingComponent dataComponent = 
			this.createReportTemplateDataRenderingComponent(
				reportName,
				reportModelName,
				reportObjectId,
				location,
				size);
		DataStorableElement dataElement =
			(DataStorableElement)dataComponent.getElement();
	
		//Заголовок для элемента шаблона
		AttachedTextComponent headerTextComponent =
			this.createTextRenderingComponent(location);
		AttachedTextStorableElement headerTextElement =
			(AttachedTextStorableElement)headerTextComponent.getElement();
		
		headerTextComponent.setText(dataComponent.getReportFullName());
		headerTextElement.setText(headerTextComponent.getText());
		
		Dimension textSize = headerTextComponent.getTextSize();
		headerTextComponent.setSize(textSize);
		headerTextElement.setSize(textSize.width,textSize.height);
		
		Point textLocation = new Point(
				dataComponent.getX() + dataComponent.getWidth() / 2
					- headerTextComponent.getWidth() / 2,
				dataComponent.getY() - headerTextComponent.getHeight()
					- HEADER_TOCOMPONENT_DISTANCE);
		headerTextComponent.setLocation(textLocation);
		headerTextElement.setLocation(textLocation.x,textLocation.y);

		headerTextElement.setAttachment(dataElement,TextAttachingType.TO_TOP);
		headerTextElement.setAttachment(dataElement,TextAttachingType.TO_WIDTH_CENTER);			
	}
	
	private String getImageFileName() {
		String fileName = null;
		JFileChooser fileChooser = new JFileChooser();

		String[] filters = {"bmp","gif","jpg","png"};
		ChoosableFileFilter bmpFilter = new ChoosableFileFilter(
				filters,
				"Image file formats");
		fileChooser.addChoosableFileFilter(bmpFilter);

		fileChooser.setDialogTitle(LangModelReport.getString("report.File.selectFileToRead"));
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
}
