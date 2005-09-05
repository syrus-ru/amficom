/*
 * $Id: DRIComponentMouseMotionListener.java,v 1.2 2005/09/05 12:22:51 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.DataRenderingComponent;
import com.syrus.AMFICOM.client.report.RenderingComponent;
import com.syrus.AMFICOM.client.reportbuilder.event.DRComponentMovedEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.report.StorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.AMFICOM.resource.IntDimension;

/**
 * MouseMotionListener for DataRenderingComponent и ImageRenderingComponent
 * @author $Author: peskovsky $
 * @version $Revision: 1.2 $, $Date: 2005/09/05 12:22:51 $
 * @module reportclient_v1
 */
public class DRIComponentMouseMotionListener implements MouseMotionListener{
	/**
	 * √абариты рабочегопол€ шаблона - требуетс€, чтобы компоненты не вылезали
	 * на пол€ шаблона.
	 */
	private final Rectangle templateBounds;
	private final ApplicationContext applicationContext;
	
	private static DRIComponentMouseMotionListener instance = null;
	
	public static void createInstance(
			ReportTemplate template,
			ApplicationContext aContext){
		instance = new DRIComponentMouseMotionListener(template,aContext);
	}

	public static DRIComponentMouseMotionListener getInstance(){
		if (instance == null)
			throw new AssertionError("No instance for DRIComponentMouseMotionListener. Use createInstance() first!");
		return instance;
	}
	
	private DRIComponentMouseMotionListener(
			ReportTemplate template,
			ApplicationContext aContext){
		this.applicationContext = aContext;
		
		this.templateBounds = new Rectangle();
		int templateMarginSize = template.getMarginSize();
		this.templateBounds.setLocation(
				new Point(templateMarginSize,templateMarginSize));
		IntDimension size = template.getSize();
		this.templateBounds.setSize(
				size.getWidth() - 2 * templateMarginSize,
				size.getHeight() - 2 * templateMarginSize);
	}
	
	public void mouseMoved(MouseEvent e) {
		//¬ зависимости от положени€ мыши выстав€летс€ соответсвующий курсор
		final DataRenderingComponent component =
			(DataRenderingComponent) e.getSource();
		
		int x = e.getX();
		int y = e.getY();

		Rectangle componentBounds = component.getBounds();
		//TODO Ќадо завести карту курсоров		
		if (	(x < RenderingComponent.DIAGONAL_EDGE_SIZE)
			&&	(y < RenderingComponent.DIAGONAL_EDGE_SIZE)) {
			component.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
		}
		else if (	(x > componentBounds.width - RenderingComponent.DIAGONAL_EDGE_SIZE)
				&&	(y > componentBounds.height - RenderingComponent.DIAGONAL_EDGE_SIZE)) {
			component.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
		}
		else if (	(x > componentBounds.width - RenderingComponent.DIAGONAL_EDGE_SIZE)
				&&	(y < RenderingComponent.DIAGONAL_EDGE_SIZE)) {
			component.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
		}
		else if (	(x < RenderingComponent.DIAGONAL_EDGE_SIZE)
				&&	(y > componentBounds.height - RenderingComponent.DIAGONAL_EDGE_SIZE)) {
			component.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
		}
		else if (x < RenderingComponent.EDGE_SIZE) {
			component.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
		}
		else if (x > componentBounds.width - RenderingComponent.EDGE_SIZE) {
			component.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
		}
		else if (y < RenderingComponent.EDGE_SIZE) {
			component.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
		}
		else if (y > componentBounds.height - RenderingComponent.EDGE_SIZE)	{
			component.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
		}
		else {
			component.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
	}

	public void mouseDragged(MouseEvent e) {
		final DataRenderingComponent component =
			(DataRenderingComponent) e.getSource();
		
		Point mousePressedLocation = component.getMousePressedLocation();
		
		int dx = e.getX() - mousePressedLocation.x;
		int dy = e.getY() - mousePressedLocation.y;

		Rectangle prevBounds = component.getBounds();
		
		Dimension newSize = prevBounds.getSize();
		Point newLocation = prevBounds.getLocation();		

		int cursorType = component.getCursor().getType();
		if (cursorType == Cursor.HAND_CURSOR) {
			newLocation.x += dx;
			newLocation.y += dy;
		}
		else if (cursorType == Cursor.E_RESIZE_CURSOR) {
			newSize.width += dx;
			mousePressedLocation.x = e.getX();
		}
		else if (cursorType == Cursor.W_RESIZE_CURSOR) {
			newSize.width -= dx;
			newLocation.x += dx;
		}
		else if (cursorType == Cursor.S_RESIZE_CURSOR) {
			newSize.height += dy;
			mousePressedLocation.y = e.getY();			
		}
		else if (cursorType == Cursor.N_RESIZE_CURSOR) {
			newSize.height -= dy;
			newLocation.y += dy;
		}
		else if (cursorType == Cursor.SE_RESIZE_CURSOR)	{
			newSize.width += dx;
			newSize.height += dy;
			mousePressedLocation.x = e.getX();
			mousePressedLocation.y = e.getY();			
		}
		else if (cursorType == Cursor.NE_RESIZE_CURSOR)	{
			newSize.width += dx;

			newSize.height -= dy;
			newLocation.y += dy;
			
			mousePressedLocation.x = e.getX();			
		}
		else if (cursorType == Cursor.NW_RESIZE_CURSOR)	{
			newSize.width -= dx;
			newSize.height -= dy;
			
			newLocation.x += dx;
			newLocation.y += dy;
		}
		else if (cursorType == Cursor.SW_RESIZE_CURSOR)	{
			newSize.width -= dx;
			newSize.height += dy;

			newLocation.x += dx;

			mousePressedLocation.y = e.getY();			
		}
		
		component.setMousePressedLocation(mousePressedLocation);

		if (newLocation.x < this.templateBounds.x)
			newLocation.x = this.templateBounds.x;
		else if (newLocation.x + newSize.width > this.templateBounds.x + this.templateBounds.width)
			newLocation.x = this.templateBounds.x + this.templateBounds.width - newSize.width;

		if (newLocation.y < this.templateBounds.y)
			newLocation.y = this.templateBounds.y;
		else if (newLocation.y + newSize.height > this.templateBounds.y + this.templateBounds.height)
			newLocation.y = this.templateBounds.y + this.templateBounds.height - newSize.height;

		component.setLocation(newLocation);
		component.setSize(newSize);
		
		StorableElement element = component.getElement();
		element.setLocation(component.getX(),component.getY());
		element.setSize(component.getWidth(),component.getHeight());			

		component.getElement().setModified(System.currentTimeMillis());
		
		//ќтсылаем сообщение, чтобы те надписи, которые прив€заны к данному
		//элементу отображени€ данных, передвинулись, сохран€€ интервал между
		//ними.
		this.applicationContext.getDispatcher().firePropertyChange(new DRComponentMovedEvent(this,component));		
		this.applicationContext.getDispatcher().firePropertyChange(new ReportFlagEvent(this,ReportFlagEvent.REPAINT_RENDERER));
	}
}
