/*
 * $Id: ATComponentMouseMotionListener.java,v 1.1 2005/09/03 12:42:20 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.AttachedTextComponent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;

public class ATComponentMouseMotionListener implements MouseMotionListener{
	ApplicationContext applicationContext = null;
	
	public static ATComponentMouseMotionListener instance = null;
	
	public static void createInstance(ApplicationContext aContext){
		instance = new ATComponentMouseMotionListener(aContext);
	}
	
	public static ATComponentMouseMotionListener getInstance(){
		if (instance == null)
			throw new AssertionError("No instance for ATComponentMouseMotionListener. Use createInstance() first!");
		return instance;
	}
	
	private ATComponentMouseMotionListener (ApplicationContext aContext){
		this.applicationContext = aContext;
	}

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
		
		component.setLocation(newX,newY);
		element.setLocation(newX,newY);

		element.setModified(System.currentTimeMillis());
		
		this.applicationContext.getDispatcher().firePropertyChange(new ReportFlagEvent(this,ReportFlagEvent.REPAINT_RENDERER));
	}
	public void mouseMoved(MouseEvent e) {
		// empty
	}
}
