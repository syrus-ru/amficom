/*
 * $Id: ReportTemplateRendererMouseListener.java,v 1.5 2005/09/18 13:13:19 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.reportbuilder.event.ComponentSelectionChangeEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.client.reportbuilder.templaterenderer.RendererMode.RENDERER_MODE;

public class ReportTemplateRendererMouseListener implements MouseListener {

	private final ReportTemplateRenderer renderer;
	private final ApplicationContext applicationContext;	

	public ReportTemplateRendererMouseListener(
		ReportTemplateRenderer renderer,
		ApplicationContext aContext){
		this.renderer = renderer;
		this.applicationContext = aContext;
	}
		
	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		this.applicationContext.getDispatcher().firePropertyChange(
				new ComponentSelectionChangeEvent(
					this,
					null));
		this.applicationContext.getDispatcher().firePropertyChange(
				new ReportFlagEvent(this,ReportFlagEvent.REPAINT_RENDERER));		
	}

	public void mouseReleased(MouseEvent e) {
		if (RendererMode.getMode().equals(RENDERER_MODE.CREATE_IMAGE)){
			this.renderer.createImageRenderingComponent(e.getPoint());
		}
		else if (RendererMode.getMode().equals(RENDERER_MODE.CREATE_LABEL)){
			this.renderer.createTextRenderingComponent(e.getPoint());
		}
		this.applicationContext.getDispatcher().firePropertyChange(
				new ReportFlagEvent(this,ReportFlagEvent.SPECIAL_MODE_CANCELED));
		this.applicationContext.getDispatcher().firePropertyChange(
				new ReportFlagEvent(this,ReportFlagEvent.REPAINT_RENDERER));
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}
}
