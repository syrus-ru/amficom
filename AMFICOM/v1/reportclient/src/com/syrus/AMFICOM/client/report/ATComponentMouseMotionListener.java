/*
 * $Id: ATComponentMouseMotionListener.java,v 1.1 2005/08/31 10:32:54 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.TextAttachingType;

public class ATComponentMouseMotionListener implements MouseMotionListener{
	private boolean allowMovingAttachedLabels;

	protected ATComponentMouseMotionListener (
			boolean allowMoving)
	{
		this.allowMovingAttachedLabels = allowMoving;
	}

	public void mouseDragged(MouseEvent e) {
		AttachedTextComponent component = (AttachedTextComponent)e.getSource();
		AttachedTextStorableElement element =
			(AttachedTextStorableElement)component.getElement();
		
		// В режиме просмотра нельзя двигать привязанные надписи
		if (	!this.allowMovingAttachedLabels
			&&	(	!element.getHorizontalAttachType().equals(TextAttachingType.TO_FIELDS_LEFT)
				||	!element.getVerticalAttachType().equals(TextAttachingType.TO_FIELDS_TOP)))
			return;

		Point mousePressedLocation = component.getMousePressedLocation();
		int newX = component.getX() + e.getX() - mousePressedLocation.x;
		int newY = component.getY() + e.getY() - mousePressedLocation.y;
		
		component.setLocation(newX,newY);
		element.setLocation(newX,newY);

		element.setModified(System.currentTimeMillis());
		//TODO Как-то сделать перерисовку
	}
	
	public void onMouseDragged(MouseEvent e) {
		AttachedTextComponent component = (AttachedTextComponent)e.getSource();
		AttachedTextStorableElement element =
			(AttachedTextStorableElement)component.getElement();
		
		// В режиме просмотра нельзя двигать привязанные надписи
		if (	!this.allowMovingAttachedLabels
			&&	(	!element.getHorizontalAttachType().equals(TextAttachingType.TO_FIELDS_LEFT)
				||	!element.getVerticalAttachType().equals(TextAttachingType.TO_FIELDS_TOP)))
			return;

		Point mousePressedLocation = component.getMousePressedLocation();
		int newX = component.getX() + e.getX() - mousePressedLocation.x;
		int newY = component.getY() + e.getY() - mousePressedLocation.y;
		
		component.setLocation(newX,newY);
		element.setLocation(newX,newY);

		element.setModified(System.currentTimeMillis());
		//TODO Как-то сделать перерисовку
	}

	public void mouseMoved(MouseEvent e) {
		// empty
	}

	public void setAllowMovingAttachedLabels(boolean allowMoving) {
		this.allowMovingAttachedLabels = allowMoving;
	}

}
