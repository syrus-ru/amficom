/*
 * $Id: DRComponentMouseMotionListener.java,v 1.1 2005/08/11 11:17:34 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.general.report;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ListIterator;

import com.syrus.AMFICOM.report.ReportTemplate;

/**
 * MouseMotionListener for DataRenderingComponent
 * @author $Author: peskovsky $
 * @version $Revision: 1.1 $, $Date: 2005/08/11 11:17:34 $
 * @module reportclient_v1
 */
public class DRComponentMouseMotionListener implements MouseMotionListener{
	/**
	 * ¬еличина пол€ шаблона - требуетс€, чтобы компоненты не вылезали
	 * на пол€ шаблона.
	 */
	private final Rectangle templateBounds;

	public DRComponentMouseMotionListener(
			ReportTemplate template)
	{
		this.templateBounds = new Rectangle();
		int templateMarginSize = template.getMarginSize();
		this.templateBounds.setLocation(
				new Point(templateMarginSize,templateMarginSize));		
		this.templateBounds.setSize(template.getSize());
	}
	
	public void mouseMoved(MouseEvent e) {
		//¬ зависимости от положени€ мыши выстав€летс€ соответсвующий курсор
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

		boolean smthChanged = false;

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
		
		
		if ((imagableRect.y <= newY)
			&& (newY + newHeight <= imagableRect.y + imagableRect.height))
		{
			if (selectedRenderingObject != null)
			{
				selectedRenderingObject.y = newY;
				selectedRenderingObject.height = newHeight;
				smthChanged = true;
			}
			else
			{
				ip.setLocation(ip.getX(),newY);
				ip.setSize(ip.getWidth(),newHeight);
				ip.setPreferredSize(ip.getSize());
			}
		}

//		if (selectedRenderingObject == null)
//		{
//			this.remove(ip);
//			this.add(ip,new XYConstraints(ip.getX(),ip.getY(),-1,-1));
//			this.repaint();
//			prevBounds = ip.getBounds();
//		}
//
//		if (!smthChanged)
//			return;
//
//    for (ListIterator lIt = reportTemplate.labels.listIterator(); lIt.hasNext();)
//		{
//			FirmedTextPane tp = (FirmedTextPane)lIt.next();
//			if ((tp.vertFirmer != null) &&
//				tp.vertFirmer.equals(selectedRenderingObject) ||
//				(tp.horizFirmer != null) &&
//				tp.horizFirmer.equals(selectedRenderingObject))
//			{
//				tp.refreshCoords(this.imagableRect);
//				this.remove(tp);
//				this.add(tp, new XYConstraints(tp.getX(), tp.getY(), -1, -1));
//			}
//		}
//
//		reportTemplate.curModified = System.currentTimeMillis();
//
//		this.repaint();
	}

}
