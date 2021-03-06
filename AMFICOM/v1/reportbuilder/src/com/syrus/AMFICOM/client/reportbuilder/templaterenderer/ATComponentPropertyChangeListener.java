/*
 * $Id: ATComponentPropertyChangeListener.java,v 1.5 2005/10/05 09:39:37 peskovsky Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.AttachedTextComponent;
import com.syrus.AMFICOM.client.report.DataRenderingComponent;
import com.syrus.AMFICOM.client.report.RenderingComponent;
import com.syrus.AMFICOM.client.reportbuilder.event.ComponentSelectionChangeEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.DRComponentMovedEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.StorableElement;
import com.syrus.util.Log;

public class ATComponentPropertyChangeListener implements PropertyChangeListener{
	AttachedTextComponent textComponent = null;
	ApplicationContext applicationContext = null;
	Rectangle templateBounds = null;
	
	public ATComponentPropertyChangeListener(
			AttachedTextComponent component,
			ApplicationContext aContext,
			Rectangle templateBounds){
		this.textComponent = component;
		this.applicationContext = aContext;
		this.templateBounds = templateBounds;
	}
	
//	public void setTemplateBounds(Rectangle templateBounds) {
//		this.templateBounds = templateBounds;
//	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt instanceof DRComponentMovedEvent) {
			//???? ????????????? ????????? ??????????? ??????, ? ????????
			//???????? ?????? ????????? ??????????? ?????? - ?????????? ?????????
			//?? ?????????????? ??????????
			DRComponentMovedEvent event = (DRComponentMovedEvent)evt;
			
			DataRenderingComponent drComponent = event.getDRComponentMoved();
			StorableElement drElement = drComponent.getElement();
			
			AttachedTextStorableElement textElement =
				(AttachedTextStorableElement) this.textComponent.getElement();
			try {
				if (	drElement.equals(textElement.getHorizontalAttacher())
					||	drElement.equals(textElement.getVerticalAttacher())) {
					textElement.suiteAttachingDistances(this.templateBounds);
					this.textComponent.setLocation(textElement.getX(),textElement.getY());
//				drComponent.setLocation(drElement.getX(),drElement.getY());				
					//TODO ???? ??????? ? ???????? ???????? ???????, ? ????????
					//????????? ??????? - ???????? ???? ??????? ?? ?????!
					this.applicationContext.getDispatcher().firePropertyChange(
							new ReportFlagEvent(this,ReportFlagEvent.REPAINT_RENDERER));			
				}
			} catch (ApplicationException e) {
				Log.errorMessage("ReportTemplateRenderer.propertyChange | " + e.getMessage());
				Log.errorException(e);			
			}
		}
		else if (evt instanceof ComponentSelectionChangeEvent) {
			//TODO ??????-?? ? ???? ??????? ???? ??????? ?????????? ????????? ???????
			//??? ? ReportTemplateRenderer ??????? ??????????????? ??????????? ??????.
			//? ?? ???????? ???? ???????? ?? ??????? ?????????.
			RenderingComponent component = 
				((ComponentSelectionChangeEvent)evt).getRenderingComponent();
			if (!this.textComponent.equals(component)){
				Dimension textSize = this.textComponent.getTextSize();
				if (this.textComponent.getSize().equals(textSize))
					return;
				
				this.textComponent.setSize(textSize.width,textSize.height);
				//?????????, ???? ?? ??? ?? ?????? ??????????
				this.textComponent.checkComponentWidth();
				
				//??????? ??????? ???????
				this.textComponent.getCaret().setVisible(false);
				this.textComponent.setEditable(false);
				
				//?????????? ?????? ????????? ????????
				StorableElement element = this.textComponent.getElement();
				element.setSize(textSize.width,textSize.height);
			}
		}
	}
}
