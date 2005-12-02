/*
 * $Id: ATComponentMouseMotionListener.java,v 1.1 2005/12/02 11:37:17 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.AttachedTextComponent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.util.Log;

public class ATComponentMouseMotionListener implements MouseMotionListener{
	ApplicationContext applicationContext = null;
	
	private static ATComponentMouseMotionListener instance = null;
	
	/**
	 * Габариты рабочего поля шаблона - требуется, чтобы компоненты не вылезали
	 * на поля шаблона.
	 */
	private Rectangle templateBounds;
	
	public static void createInstance(
			ApplicationContext aContext,
			Rectangle templateBounds) {
		instance = new ATComponentMouseMotionListener(aContext,templateBounds);
	}
	
	public static ATComponentMouseMotionListener getInstance(){
		if (instance == null)
			throw new AssertionError("No instance for ATComponentMouseMotionListener. Use createInstance() first!");
		return instance;
	}
	
	private ATComponentMouseMotionListener (ApplicationContext aContext, Rectangle templateBounds) {
		this.applicationContext = aContext;
		this.templateBounds = templateBounds;
	}

//	public void setTemplateBounds(Rectangle templateBounds) {
//		this.templateBounds = templateBounds;
//	}
	
	public void mouseDragged(MouseEvent e) {
		AttachedTextComponent component = (AttachedTextComponent)e.getSource();
		int cursorType = component.getCursor().getType();
		if (cursorType != Cursor.HAND_CURSOR) {
			//TODO Надо завести карту курсоров
			component.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		
		AttachedTextStorableElement element =
			(AttachedTextStorableElement)component.getElement();
		
		Point mousePressedLocation = component.getMousePressedLocation();
		int newX = component.getX() + e.getX() - mousePressedLocation.x;
		int newY = component.getY() + e.getY() - mousePressedLocation.y;
		
		if (newX < this.templateBounds.x)
			newX = this.templateBounds.x;
		else if (newX + component.getWidth() > this.templateBounds.x + this.templateBounds.width)
			newX = this.templateBounds.x + this.templateBounds.width - component.getWidth();

		if (newY < this.templateBounds.y)
			newY = this.templateBounds.y;
		else if (newY + component.getHeight() > this.templateBounds.y + this.templateBounds.height)
			newY = this.templateBounds.y + this.templateBounds.height - component.getHeight();
		
		component.setLocation(newX,newY);
		element.setLocation(newX,newY);
		
		try {
			element.refreshAttachingDistances();
		} catch (ApplicationException e1) {
			Log.errorMessage("ReportTemplateRenderer.propertyChange | " + e1.getMessage());
			Log.errorException(e1);			
		}
		
		this.applicationContext.getDispatcher().firePropertyChange(new ReportFlagEvent(this,ReportFlagEvent.REPAINT_RENDERER));
	}
	public void mouseMoved(MouseEvent e) {
		// empty
	}
}
