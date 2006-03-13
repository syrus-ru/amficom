/*
 * $Id: ReportTemplateRenderer.java,v 1.2 2006/03/13 13:53:57 bass Exp $
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
import java.util.Set;

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
import com.syrus.AMFICOM.client.report.RenderingComponent;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.client.report.ReportModelPool;
import com.syrus.AMFICOM.client.report.ReportModel.ReportType;
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderApplicationModel;
import com.syrus.AMFICOM.client.reportbuilder.event.AttachLabelEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ComponentSelectionChangeEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportQuickViewEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.UseTemplateEvent;
import com.syrus.AMFICOM.client.reportbuilder.templaterenderer.RendererMode.RENDERER_MODE;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.report.AbstractDataStorableElement;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.ImageStorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.AMFICOM.report.StorableElement;
import com.syrus.AMFICOM.report.TableDataStorableElement;
import com.syrus.AMFICOM.report.TextAttachingType;
import com.syrus.AMFICOM.report.ReportTemplate.Orientation;
import com.syrus.AMFICOM.resource.IntDimension;
import com.syrus.AMFICOM.resource.IntPoint;
import com.syrus.util.Log;

public class ReportTemplateRenderer extends JPanel implements PropertyChangeListener{
	private static final long serialVersionUID = 1947965880055353754L;
	private final static int BORDER_MARGIN_SIZE = 2;
	private static final int HEADER_TOCOMPONENT_DISTANCE = 10;	
	
	private ApplicationContext applicationContext; 
	private ReportTemplateRendererMouseListener mouseListener = null;
	
	private DropTarget dropTarget = null;	
	private ReportTemplateRendererDropTargetListener dropTargetListener = null;	
	
	private ReportTemplate template = null;
	
	private RenderingComponent selectedComponent = null;
	private Set<StorableElement> intersectingElements = null;
	
	private AttachedTextComponent labelToBeAttached = null;
	private TextAttachingType labelAttachingType = null;
	
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
				try {
					ReportTemplateRenderer.this.removeRenderingComponent(
							this.selectedComponent,
							true);
					this.selectedComponent = null;				
					ReportTemplateRenderer.this.repaint();
				} catch (ApplicationException e) {
					Log.errorMessage("ReportTemplateRenderer.propertyChange | " + e.getMessage());
					Log.errorException(e);			
					JOptionPane.showMessageDialog(
							Environment.getActiveWindow(),
							I18N.getString("report.Exception.deleteObjectError"),
							I18N.getString("report.Exception.error"),
							JOptionPane.ERROR_MESSAGE);
				}
			}
			else if (eventType.equals(ReportFlagEvent.TEMPLATE_PARAMETERS_CHANGED)) {
				this.refreshTemplateBounds();
				ReportTemplateRenderer.this.repaint();				
			}			
			else if (eventType.equals(ReportFlagEvent.REPAINT_RENDERER))
				ReportTemplateRenderer.this.repaint();
			else
				//Не обнуляем список взаимопересечённых элементов
				return;
		}
		else if (evt instanceof UseTemplateEvent){
			this.removeAllComponents();
			
			DRIComponentMouseMotionListener.createInstance(this.applicationContext,this.marginBounds);
			DRIComponentMouseListener.createInstance(this.applicationContext);			
			ATComponentMouseMotionListener.createInstance(this.applicationContext,this.marginBounds);
			ATComponentMouseListener.createInstance(this.applicationContext);
			ATComponentKeyListener.createInstance(this.applicationContext);			

			try {
				this.setTemplate(((UseTemplateEvent)evt).getReportTemplate());
			} catch (Exception e) {
				Log.errorMessage("ReportTemplateRenderer.propertyChange | " + e.getMessage());
				Log.errorException(e);			
				JOptionPane.showMessageDialog(
						Environment.getActiveWindow(),
						e.getMessage(),
						I18N.getString("report.Exception.error"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}
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
						I18N.getString("report.Exception.error"),
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
					AbstractDataStorableElement dataElement =
						(AbstractDataStorableElement)eventComponent.getElement();
					textElement.setAttachment(
							dataElement,
							this.labelAttachingType);
					
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
		else
			//Не обнуляем список
			return;
		//Обнуляем список пересекающихся объектов.
		this.intersectingElements = null;
	}

	private void removeAllComponents() {
		while (this.getComponentCount() > 0) {
			try {
				this.removeRenderingComponent(
						(RenderingComponent)this.getComponent(0),false);
			} catch (ApplicationException e) {
				Log.errorException(e);
			}
		}
	}
	
	private void removeRenderingComponent(RenderingComponent component,boolean deleteElement) throws ApplicationException {
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
						this.removeRenderingComponent(textComponent,deleteElement);
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
		
		if (deleteElement) {
			StorableElement element = component.getElement();
			this.template.removeElement(element);
		}
	}
	
	public ReportTemplate getTemplate() {
		return this.template;
	}

	public void setTemplate(ReportTemplate template) throws CreateModelException, ApplicationException, IOException {
		this.template = template;
		this.refreshTemplateBounds();
		
		for (AbstractDataStorableElement dataElement : this.template.getDataStorableElements())
			this.createReportTemplateDataRenderingComponent(dataElement);

		for (AttachedTextStorableElement textElement : this.template.getAttachedTextStorableElements())
			this.createTextRenderingComponent(textElement);

		for (ImageStorableElement imageElement : this.template.getImageStorableElements())
			this.createImageRenderingComponent(imageElement);
	}
	
	private void refreshTemplateBounds() {
		IntDimension size = this.template.getSize();
		
		if (this.template.getOrientation().equals(Orientation.PORTRAIT))
			this.setSize(size.getWidth(),size.getHeight() * 2);
		else
			this.setSize(2 * size.getHeight(),size.getWidth());
		this.setPreferredSize(this.getSize());				
		
		int templateMarginSize = this.template.getMarginSize();
		this.marginBounds.setLocation(
				new Point(templateMarginSize,templateMarginSize));
		this.templateBounds.setLocation(
				new Point(2,2));

		if (this.template.getOrientation().equals(Orientation.PORTRAIT)){
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
			//Рисуем рамки вокруг пересекающихся элементов
			if (this.intersectingElements != null) {
				g.setColor(Color.RED);
				for (StorableElement element : this.intersectingElements) {
					Component elementsComponent = null;
					for (Component component : this.getComponents()) {
						if (((RenderingComponent)component).getElement().equals(element)) {
							elementsComponent = component;
							break;
						}
					}
					if (elementsComponent != null)
						g.drawRect(
								elementsComponent.getX() - BORDER_MARGIN_SIZE,
								elementsComponent.getY() - BORDER_MARGIN_SIZE,
								elementsComponent.getWidth() + 2 * BORDER_MARGIN_SIZE,
								elementsComponent.getHeight() + 2 * BORDER_MARGIN_SIZE);
				}
			}
		}
		
		this.paintChildren(g);
	}
	
	public ImageRenderingComponent createImageRenderingComponent(Point point) throws ApplicationException, IOException {
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
				I18N.getString("report.Exception.errorReadingImage"),
				I18N.getString("report.Exception.error"),
				JOptionPane.ERROR_MESSAGE);
				
			this.applicationContext.getDispatcher().firePropertyChange(
				new ReportFlagEvent(this,ReportFlagEvent.SPECIAL_MODE_CANCELED));
				
			return null;
		}

		Dimension defaultSize = ImageRenderingComponent.DEFAULT_IMAGE_SIZE;

		ImageStorableElement element = ImageStorableElement.createInstance(
				LoginManager.getUserId(),
				elementImage,
				new IntDimension(defaultSize.width,defaultSize.height),
				new IntPoint(point.x,point.y));
		this.template.addElement(element);
		 
		ImageRenderingComponent component = new ImageRenderingComponent(element,element.getBufferedImage());
		this.add(component);
		component.setLocation(element.getX(),element.getY());
		component.setSize(element.getWidth(),element.getHeight());
		component.addMouseListener(DRIComponentMouseListener.getInstance());
		component.addMouseMotionListener(DRIComponentMouseMotionListener.getInstance());
		
		this.applicationContext.getDispatcher().firePropertyChange(
			new ReportFlagEvent(this,ReportFlagEvent.SPECIAL_MODE_CANCELED));
		
		return component;
	}

	public ImageRenderingComponent createImageRenderingComponent(ImageStorableElement imageElement) throws ApplicationException, IOException {
		ImageRenderingComponent component = new ImageRenderingComponent(
				imageElement,
				imageElement.getBufferedImage());
		this.add(component);
		component.setLocation(imageElement.getX(),imageElement.getY());
		component.setSize(imageElement.getWidth(),imageElement.getHeight());
		component.addMouseListener(DRIComponentMouseListener.getInstance());
		component.addMouseMotionListener(DRIComponentMouseMotionListener.getInstance());
		
		this.applicationContext.getDispatcher().firePropertyChange(
			new ReportFlagEvent(this,ReportFlagEvent.SPECIAL_MODE_CANCELED));
		
		return component;
	}
	
	public AttachedTextComponent createTextRenderingComponent(Point point) throws CreateObjectException{
		AttachedTextStorableElement element = AttachedTextStorableElement.createInstance(
				LoginManager.getUserId(),
				AttachedTextComponent.DEFAULT_FONT,
				new IntPoint(point.x,point.y),
				new IntDimension(
						AttachedTextComponent.MINIMUM_COMPONENT_SIZE.width,
						AttachedTextComponent.MINIMUM_COMPONENT_SIZE.height));

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
		
		this.template.addElement(element);		
		
		this.applicationContext.getDispatcher().addPropertyChangeListener(
				ReportEvent.TYPE,
				component.getATPropertyChangeListener());

		return component;
	}

	public AttachedTextComponent createTextRenderingComponent(AttachedTextStorableElement textElement){
		AttachedTextComponent component = new AttachedTextComponent(textElement);
		this.add(component);
		
		IntPoint location = textElement.getLocation();
		component.setLocation(location.x,location.y);
		component.setSize(textElement.getWidth(),textElement.getHeight());
		component.setText(textElement.getText());
		component.setBorder(DataRenderingComponent.DEFAULT_BORDER);
		
		component.addMouseListener(ATComponentMouseListener.getInstance());
		component.addMouseMotionListener(ATComponentMouseMotionListener.getInstance());
		component.addKeyListener(ATComponentKeyListener.getInstance());
		component.setATPropertyChangeListener(
				new ATComponentPropertyChangeListener(
						component,
						this.applicationContext,
						this.marginBounds));
		
		this.applicationContext.getDispatcher().addPropertyChangeListener(
				ReportEvent.TYPE,
				component.getATPropertyChangeListener());

		return component;
	}
	
	
	public RTEDataRenderingComponent createReportTemplateDataRenderingComponent(
			AbstractDataStorableElement storableElement) throws CreateModelException, ApplicationException {
		RTEDataRenderingComponent component =
			new RTEDataRenderingComponent(storableElement);
		
		component.addMouseListener(DRIComponentMouseListener.getInstance());
		component.addMouseMotionListener(DRIComponentMouseMotionListener.getInstance());
		
		this.add(component);
		component.refreshLabels();
		
		IntPoint location = storableElement.getLocation();
		component.setLocation(location.x,location.y);
		component.setSize(storableElement.getWidth(),storableElement.getHeight());
		
		component.setContext(this.applicationContext);

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
		
		IntPoint intLocation = new IntPoint(location.x,location.y);		
		AbstractDataStorableElement storableElement = null;
		if (reportType.equals(ReportType.TABLE))
			storableElement = TableDataStorableElement.createInstance(
					LoginManager.getUserId(),
					reportName,
					reportModelName,
					1,
					intLocation);
		else
			storableElement = DataStorableElement.createInstance(
					LoginManager.getUserId(),
					reportName,
					reportModelName,
					intLocation);
			
//			storableElement = new DataStorableElement(
//					reportName,
//					reportModelName);
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
		AbstractDataStorableElement dataElement =
			(AbstractDataStorableElement)dataComponent.getElement();
	
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

		fileChooser.setDialogTitle(I18N.getString("report.File.selectFileToRead"));
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

	public void setIntersectingElements(Set<StorableElement> intersectingElements) {
		this.intersectingElements = intersectingElements;
	}
}
