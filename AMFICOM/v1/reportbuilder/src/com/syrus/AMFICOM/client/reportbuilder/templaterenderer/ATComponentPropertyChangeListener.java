/*
 * $Id: ATComponentPropertyChangeListener.java,v 1.1 2005/09/03 12:42:20 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.AttachedTextComponent;
import com.syrus.AMFICOM.client.report.RenderingComponent;
import com.syrus.AMFICOM.client.reportbuilder.event.ComponentSelectionChangeEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.DRComponentMovedEvent;
import com.syrus.AMFICOM.client.reportbuilder.event.ReportFlagEvent;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.StorableElement;

public class ATComponentPropertyChangeListener implements PropertyChangeListener{
	AttachedTextComponent textComponent = null;
	ApplicationContext applicationContext = null;
	
	public ATComponentPropertyChangeListener(
			AttachedTextComponent component,
			ApplicationContext aContext){
		this.textComponent = component;
		this.applicationContext = aContext;
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt instanceof DRComponentMovedEvent) {
			//���� ������������� ��������� ����������� ������, � ��������
			//�������� ������ ��������� ����������� ������ - ���������� ���������
			//�� �������������� ����������
			DRComponentMovedEvent event = (DRComponentMovedEvent)evt;
			
			StorableElement drElement = event.getDRComponentMoved().getElement();
			
			AttachedTextStorableElement textElement =
				(AttachedTextStorableElement) this.textComponent.getElement();
			if (	drElement.equals(textElement.getHorizontalAttacher())
				||	drElement.equals(textElement.getVerticalAttacher())) {
				textElement.suiteAttachingDistances();
				this.textComponent.setLocation(textElement.getX(),textElement.getY());
				//TODO ���� ������� � �������� �������� �������, � ��������
				//��������� ������� - �������� ���� ������� �� �����!
				this.applicationContext.getDispatcher().firePropertyChange(
						new ReportFlagEvent(this,ReportFlagEvent.REPAINT_RENDERER));			
			}
		}
		else if (evt instanceof ComponentSelectionChangeEvent) {
			//TODO ������-�� � ���� ������� ���� ������� ���������� ��������� �������
			//��� � ReportTemplateRenderer ������� ��������������� ����������� ������.
			RenderingComponent component = 
				((ComponentSelectionChangeEvent)evt).getRenderingComponent();
			if (!this.textComponent.equals(component)){
				Dimension textSize = this.textComponent.getTextSize();
				if (this.textComponent.getSize().equals(textSize))
					return;
				
				this.textComponent.setSize(textSize.width,textSize.height);
				//���������, ���� �� ��� �� ������ ����������
				this.textComponent.checkComponentWidth();
				//���������� ������ ��������� ��������
				StorableElement element = this.textComponent.getElement();
				element.setSize(
						this.textComponent.getWidth(),
						this.textComponent.getHeight());
				element.setModified(System.currentTimeMillis());
			}
		}
	}
}
