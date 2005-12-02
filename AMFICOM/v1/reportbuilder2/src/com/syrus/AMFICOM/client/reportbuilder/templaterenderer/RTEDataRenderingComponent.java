/*
 * $Id: RTEDataRenderingComponent.java,v 1.1 2005/12/02 11:37:17 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.dnd.DropTarget;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.CreateModelException;
import com.syrus.AMFICOM.client.report.DataRenderingComponent;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.client.report.ReportModelPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.report.AbstractDataStorableElement;

public class RTEDataRenderingComponent extends
		DataRenderingComponent {
	private static final long serialVersionUID = -3594197341032602733L;
	
	public static final Dimension DEFAULT_SIZE = new Dimension(300,100);
	
	private DropTarget dropTarget = null;
	private RTEComponentDragDropListener dropTargetListener = null;	
	
	private	String reportName = null;
	private Point reportNameLocation = new Point();
	private	String modelName = null;	
	private Point modelNameLocation = new Point();
	private	String objectName = null;	
	private Point objectNameLocation = new Point();
	
	private Dimension minimumSize = new Dimension();
	private double modelNameWidth = 0;
	private double reportNameWidth = 0;
	private double objectNameWidth = 0;

	private ApplicationContext applicationContext = null;	
	
	public RTEDataRenderingComponent (AbstractDataStorableElement dre){
		super(dre);
	}
	
	public void setContext(ApplicationContext aContext){
		if (this.applicationContext != null) {
			this.dropTarget.removeDropTargetListener(this.dropTargetListener);
		}
		if (aContext != null) {
			this.applicationContext = aContext;
			this.dropTargetListener = new RTEComponentDragDropListener(
					this,
					this.applicationContext);
			this.dropTarget = new DropTarget(this, this.dropTargetListener);
			this.dropTarget.setActive(true);
		}
	}
	
	public void paintComponent (Graphics g)	{
		g.setColor(Color.WHITE);
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		
		g.setColor(Color.black);		
		g.drawString(
			this.modelName,
			this.modelNameLocation.x,
			this.modelNameLocation.y);
			
		g.drawString(
			this.reportName,
			this.reportNameLocation.x,
			this.reportNameLocation.y);

		AbstractDataStorableElement<?> dsElement = (AbstractDataStorableElement)this.getElement();
		if (dsElement.getReportObjectId() != null)	
			g.drawString(
					this.objectName,
					this.objectNameLocation.x,
					this.objectNameLocation.y);
		
		g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
	}
	
	/**
	 * Метод вызывается при изменении габаритов элемента отображения.
	 */
	private void refreshPositions() {
		AbstractDataStorableElement<?> dsElement = (AbstractDataStorableElement)this.getElement();

		if (dsElement.getReportObjectId() == null) {		
			this.modelNameLocation.y = (int) (this.getHeight() / 3.D);
			this.reportNameLocation.y = (int) (this.getHeight() * 2.D / 3.D);
		}
		else {
			this.modelNameLocation.y = (int) (this.getHeight() / 4.D);
			this.reportNameLocation.y = (int) (this.getHeight() * 2.D / 4.D);
			this.objectNameLocation.y = (int) (this.getHeight() * 3.D / 4.D);		
		}
			
		this.modelNameLocation.x = (int)((this.getWidth() - this.modelNameWidth) / 2.D);
		this.reportNameLocation.x = (int)((this.getWidth() - this.reportNameWidth) / 2.D);
		this.objectNameLocation.x = (int)((this.getWidth() - this.objectNameWidth) / 2.D);		
	}
	
	public void setX(int x) {
		this.setLocation(x,this.getY());
	}

	public void setY(int y) {
		this.setLocation(this.getX(),y);
	}

	public void setWidth(int width) {
		this.setSize(width,this.getHeight());
	}

	public void setHeight(int height) {
		this.setSize(this.getWidth(),height);
	}
	
	@Override
	public void setSize(Dimension newSize) {
		this.setSize(newSize.width,newSize.height);
	}
	
	@Override
	public void setSize(int width, int height) {
		super.setSize(
				width > this.minimumSize.width ? width : this.minimumSize.width,
				height > this.minimumSize.height ? height : this.minimumSize.height);
		refreshPositions();
	}
	
	/**
	 * Инициализируются минимальные значения габаритов объекта.
	 * Вызывается как только у окна появляется Graphics.
	 * @throws ApplicationException 
	 */
	public void refreshLabels()  throws CreateModelException, ApplicationException{
		AbstractDataStorableElement<?> dsElement = (AbstractDataStorableElement)this.getElement();
		ReportModel reportModel = ReportModelPool.getModel(
				dsElement.getModelClassName());
	
		this.reportName = reportModel.getReportElementName(
				dsElement.getReportName());
		this.modelName = reportModel.getLocalizedShortName();

		Identifier reportObjectId = dsElement.getReportObjectId();
		if (reportObjectId != null) {
			StorableObject storableObject =
				StorableObjectPool.getStorableObject(reportObjectId,true);
			//TODO Здесь костыль. Нужно, чтобы все были Namable
			if (storableObject == null)
				this.objectName = "";
			else if (storableObject instanceof Namable)
				this.objectName = ((Namable)storableObject).getName();
			else
				this.objectName = storableObject.getClass().getName();
		}
		else 
			this.objectName = "";
		
		Graphics g = this.getGraphics();
		FontMetrics fontMetrics = g.getFontMetrics();
		
		this.modelNameWidth =
			fontMetrics.getStringBounds(this.modelName,g).getWidth();
		this.reportNameWidth =
			fontMetrics.getStringBounds(this.reportName,g).getWidth();
		this.objectNameWidth =
			fontMetrics.getStringBounds(this.objectName,g).getWidth();
		
		this.minimumSize.width = (int)Math.max(
				Math.max(this.modelNameWidth,this.reportNameWidth),
				this.objectNameWidth) + 5;
		
		this.minimumSize.height = fontMetrics.getHeight() * 3 + 5;
		
		this.refreshPositions();
	}
	
	/**
	 * Возвращает полное локализованное имя элемента шаблона. Используется
	 * из ReportTemplateRendererDragDropListener, для создания надписи-
	 * заголовка элемента. Такой метод уже реализован в ReportModel, но
	 * здесь удобнее написать свой, поскольку имеются готовые локализванные
	 * названия модели и элемента шаблона
	 */
	public String getReportFullName() {
		return this.modelName + ReportModel.REPORT_NAME_DIVIDER + this.reportName;
	}
	
	public void removeDropTargetListener() {
		if (	this.dropTarget != null
			&&	this.dropTargetListener != null)
			this.dropTarget.removeDropTargetListener(this.dropTargetListener);
	}
}
