/*
 * $Id: ReportTemplateRendererMouseListener.java,v 1.1.1.1 2005/12/02 11:37:17 bass Exp $
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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.util.Log;

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
			try {
				this.renderer.createImageRenderingComponent(e.getPoint());
			} catch (Exception e1) {
				Log.errorMessage("ReportTemplateRenderer.propertyChange | " + e1.getMessage());
				Log.errorException(e1);			
			}
		}
		else if (RendererMode.getMode().equals(RENDERER_MODE.CREATE_LABEL)){
			try {
				this.renderer.createTextRenderingComponent(e.getPoint());
			} catch (CreateObjectException e1) {
				Log.errorMessage("ReportTemplateRenderer.propertyChange | " + e1.getMessage());
				Log.errorException(e1);			
			}
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
