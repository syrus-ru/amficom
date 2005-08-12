/*
 * $Id: DRComponentMouseMotionListener.java,v 1.1 2005/08/12 10:23:10 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.report.ReportTemplate;

/**
 * MouseMotionListener for DataRenderingComponent
 * @author $Author: peskovsky $
 * @version $Revision: 1.1 $, $Date: 2005/08/12 10:23:10 $
 * @module reportclient_v1
 */
public class DRComponentMouseMotionListener implements MouseMotionListener{
	/**
	 * Величина поля шаблона - требуется, чтобы компоненты не вылезали
	 * на поля шаблона.
	 */
	private final Rectangle templateBounds;
	private final ApplicationContext aContext;
	
	/**
	 * Данный параметр указывает, будут ли данные о границах элемента отображения
	 * передаваться в его элемент (структуру сохраняемых данных). Равен true в случае,
	 * если редактируем схему шаблона, равен false, если просматриваем отчёт (в отчёте
	 * габариты элементов отображения данных отличаются от габаритов на схеме шаблона).
	 */
	private boolean synchronizeElements;

	public DRComponentMouseMotionListener(
			ReportTemplate template,
			ApplicationContext aContext,
			boolean synchronizeElements)
	{
		this.aContext = aContext;
		this.synchronizeElements = synchronizeElements;
		this.templateBounds = new Rectangle();
		int templateMarginSize = template.getMarginSize();
		this.templateBounds.setLocation(
				new Point(templateMarginSize,templateMarginSize));		
		this.templateBounds.setSize(template.getSize());
	}
	
	public void mouseMoved(MouseEvent e) {
		//В зависимости от положения мыши выставялется соответсвующий курсор
		final DataRenderingComponent component =
			(DataRenderingComponent) e.getSource();
		
		int x = e.getX();
		int y = e.getY();

		Rectangle componentBounds = component.getBounds();
		
		if (	(x < DataRenderingComponent.DIAGONAL_EDGE_SIZE)
			&&	(y < DataRenderingComponent.DIAGONAL_EDGE_SIZE))
		{
			component.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
		}
		else if (	(x > componentBounds.width - DataRenderingComponent.DIAGONAL_EDGE_SIZE)
				&&	(y > componentBounds.height - DataRenderingComponent.DIAGONAL_EDGE_SIZE))
		{
			component.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
		}
		else if (	(x > componentBounds.width - DataRenderingComponent.DIAGONAL_EDGE_SIZE)
				&&	(y < DataRenderingComponent.DIAGONAL_EDGE_SIZE))
		{
			component.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
		}
		else if (	(x < DataRenderingComponent.DIAGONAL_EDGE_SIZE)
				&&	(y > componentBounds.height - DataRenderingComponent.DIAGONAL_EDGE_SIZE))
		{
			component.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
		}
		else if (x < DataRenderingComponent.EDGE_SIZE)
		{
			component.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
		}
		else if (x > componentBounds.width - DataRenderingComponent.EDGE_SIZE)
		{
			component.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
		}
		else if (y < DataRenderingComponent.EDGE_SIZE)
		{
			component.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
		}
		else if (y > componentBounds.height - DataRenderingComponent.EDGE_SIZE)
		{
			component.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
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
		if (cursorType == Cursor.CROSSHAIR_CURSOR)
		{
			newLocation.x += dx;
			newLocation.y += dy;
		}
		else if (cursorType == Cursor.E_RESIZE_CURSOR)
		{
			newSize.width += dx;
		}
		else if (cursorType == Cursor.W_RESIZE_CURSOR)
		{
			newSize.width -= dx;
			newLocation.x += dx;
		}
		else if (cursorType == Cursor.S_RESIZE_CURSOR)
		{
			newSize.height += dy;
		}
		else if (cursorType == Cursor.N_RESIZE_CURSOR)
		{
			newSize.height -= dy;
			newLocation.y += dy;
		}
		else if (cursorType == Cursor.SE_RESIZE_CURSOR)
		{
			newSize.width += dx;
			newSize.height += dy;
		}
		else if (cursorType == Cursor.NE_RESIZE_CURSOR)
		{
			newSize.width += dx;

			newSize.height -= dy;
			newLocation.y += dy;
		}
		else if (cursorType == Cursor.NW_RESIZE_CURSOR)
		{
			newSize.width -= dx;
			newSize.height -= dy;
			
			newLocation.x += dx;
			newLocation.y += dy;
		}
		else if (cursorType == Cursor.SW_RESIZE_CURSOR)
		{
			newSize.width -= dx;
			newSize.height += dy;

			newLocation.x += dx;
		}

		if (	(this.templateBounds.x <= newLocation.x)
			&&	(newLocation.x + newSize.width <= this.templateBounds.x + this.templateBounds.width))
		{
			component.setLocation(newLocation.x,component.getY());
			component.setSize(newSize.width,component.getHeight());
		}

		if (	(this.templateBounds.y <= newLocation.y)
				&&	(newLocation.y + newSize.height <= this.templateBounds.y + this.templateBounds.height))
		{
			component.setLocation(component.getX(),newLocation.y);
			component.setSize(component.getWidth(),newSize.height);
		}
		
		if (this.synchronizeElements)
			component.getElement().setBounds(component.getBounds());
		
		//Отсылаем сообщение, чтобы те надписи, которые привязаны к данному
		//элементу отображения данных, передвинулись, сохраняя интервал между
		//ними.
		this.aContext.getDispatcher().firePropertyChange(
				new PropertyChangeEvent(
						component,
						DataRenderingComponent.BOUNDS_PROPERTY,
						prevBounds,
						component.getBounds()));
		

		component.getElement().setModified(System.currentTimeMillis());
		
		//TODO Здесь ещё с перерисовкой разобраться надо.
	}

	public void setSynchronizeElements(boolean synchronizeElements) {
		this.synchronizeElements = synchronizeElements;
	}

}
