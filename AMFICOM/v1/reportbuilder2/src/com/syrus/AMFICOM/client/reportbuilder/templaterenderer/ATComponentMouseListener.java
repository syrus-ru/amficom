/*
 * $Id: ATComponentMouseListener.java,v 1.1.1.1 2005/12/02 11:37:17 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.AttachedTextComponent;
import com.syrus.AMFICOM.client.reportbuilder.event.ComponentSelectionChangeEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.client.reportbuilder.templaterenderer.RendererMode.RENDERER_MODE;

public class ATComponentMouseListener implements MouseListener{
	ApplicationContext applicationContext = null;
	
	public static ATComponentMouseListener instance = null;
	
	public static void createInstance(ApplicationContext aContext){
		instance = new ATComponentMouseListener(aContext);
	}
	
	public static ATComponentMouseListener getInstance(){
		if (instance == null)
			throw new AssertionError("No instance for ATComponentMouseMotionListener. Use createInstance() first!");
		return instance;
	}

	private ATComponentMouseListener (ApplicationContext aContext){
		this.applicationContext = aContext;
	}
	
	public void mouseClicked(MouseEvent e) {
		//Empty
	}

	public void mousePressed(MouseEvent e) {
		if (!RendererMode.getMode().equals(RENDERER_MODE.NO_SPECIAL))
			return;
		
		AttachedTextComponent component = (AttachedTextComponent)e.getSource();		
		component.setMousePressedLocation(e.getPoint());

		this.applicationContext.getDispatcher().firePropertyChange(
			new ComponentSelectionChangeEvent(
				this,
				component));
		this.applicationContext.getDispatcher().firePropertyChange(new ReportFlagEvent(this,ReportFlagEvent.REPAINT_RENDERER));
	}

	public void mouseReleased(MouseEvent e) {
		AttachedTextComponent component = (AttachedTextComponent)e.getSource();
		component.setCursor(Cursor.getDefaultCursor());
		component.getCaret().setVisible(true);
		component.setEditable(true);

		if (SwingUtilities.isRightMouseButton(e)){
			//Вызов контекстного меню
			TextComponentMenu menu = new TextComponentMenu(
					component,
					this.applicationContext);
			Point screenLocation = component.getLocationOnScreen();
			menu.setLocation(
				screenLocation.x + component.getWidth() / 2,
				screenLocation.y + component.getHeight() / 2);
			menu.setVisible(true);
		}
	}

	public void mouseEntered(MouseEvent e) {
		//Empty
	}

	public void mouseExited(MouseEvent e) {
		AttachedTextComponent component = (AttachedTextComponent)e.getSource();
		component.setCursor(Cursor.getDefaultCursor());
	}
}
