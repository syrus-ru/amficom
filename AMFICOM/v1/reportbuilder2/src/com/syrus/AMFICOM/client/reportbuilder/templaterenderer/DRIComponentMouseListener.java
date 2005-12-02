/*
 * $Id: DRIComponentMouseListener.java,v 1.1.1.1 2005/12/02 11:37:17 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.DataRenderingComponent;
import com.syrus.AMFICOM.client.report.ImageRenderingComponent;
import com.syrus.AMFICOM.client.reportbuilder.event.ComponentSelectionChangeEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.client.reportbuilder.templaterenderer.RendererMode.RENDERER_MODE;
import com.syrus.AMFICOM.report.StorableElement;
import com.syrus.AMFICOM.report.TableDataStorableElement;

public class DRIComponentMouseListener implements MouseListener{
	ApplicationContext applicationContext;
	private static DRIComponentMouseListener instance = null;
	
	public static void createInstance(
			ApplicationContext aContext){
		instance = new DRIComponentMouseListener(aContext);
	}

	public static DRIComponentMouseListener getInstance(){
		if (instance == null)
			throw new AssertionError("No instance for DRIComponentMouseListener. Use createInstance() first!");
		return instance;
	}

	private DRIComponentMouseListener(
			ApplicationContext aContext){
		this.applicationContext = aContext;
	}
	
	public void mouseClicked(MouseEvent e) {
		//Empty
	}

	public void mousePressed(MouseEvent e) {
		final DataRenderingComponent component =
			(DataRenderingComponent) e.getSource();
		component.setMousePressedLocation(e.getPoint());
		
		if (!	(	RendererMode.getMode().equals(RENDERER_MODE.NO_SPECIAL)
				||	(	RendererMode.getMode().equals(RENDERER_MODE.ATTACH_LABEL)
					&&	!(component instanceof ImageRenderingComponent))))
			return;

		//В процессе привязки надписи финальный этап (описывается в 
		//ReportTemplateRenderer) инициируется по ComponentSelectionChangeEvent.
		this.applicationContext.getDispatcher().firePropertyChange(
				new ComponentSelectionChangeEvent(
					this,
					component));
		this.applicationContext.getDispatcher().firePropertyChange(
				new ReportFlagEvent(this,ReportFlagEvent.REPAINT_RENDERER));		
	}

	public void mouseReleased(MouseEvent e) {
		if (!SwingUtilities.isRightMouseButton(e))
			return;

		final DataRenderingComponent component =
			(DataRenderingComponent) e.getSource();
		final StorableElement storableElement = component.getElement();
		
		if (	!RendererMode.getMode().equals(RENDERER_MODE.NO_SPECIAL)
			||	!(storableElement instanceof TableDataStorableElement))
			return;

		TableDataComponentMenu menu = new TableDataComponentMenu(
				(TableDataStorableElement)storableElement,
				this.applicationContext);
		Point screenLocation = component.getLocationOnScreen();
		menu.setLocation(
			screenLocation.x + component.getWidth() / 2,
			screenLocation.y + component.getHeight() / 2);
		menu.setVisible(true);
	}

	public void mouseEntered(MouseEvent e) {
		//Empty
	}

	public void mouseExited(MouseEvent e) {
		//Empty
	}
}
