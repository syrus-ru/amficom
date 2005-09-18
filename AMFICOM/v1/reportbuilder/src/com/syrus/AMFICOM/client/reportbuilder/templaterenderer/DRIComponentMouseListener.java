/*
 * $Id: DRIComponentMouseListener.java,v 1.4 2005/09/18 13:13:19 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.DataRenderingComponent;
import com.syrus.AMFICOM.client.report.ImageRenderingComponent;
import com.syrus.AMFICOM.client.reportbuilder.event.ComponentSelectionChangeEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.client.reportbuilder.templaterenderer.RendererMode.RENDERER_MODE;

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
		//Empty
	}

	public void mouseEntered(MouseEvent e) {
		//Empty
	}

	public void mouseExited(MouseEvent e) {
		//Empty
	}
}
