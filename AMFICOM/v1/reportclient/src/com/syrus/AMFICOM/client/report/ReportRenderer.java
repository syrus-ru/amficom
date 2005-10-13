/*
 * $Id: ReportRenderer.java,v 1.13 2005/10/13 08:50:48 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.ImageStorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.AMFICOM.report.StorableElement;
import com.syrus.AMFICOM.report.ReportTemplate.Orientation;
import com.syrus.AMFICOM.resource.IntDimension;

/**
 * ��������� ����� �� �������
 * @author $Author: peskovsky $
 * @version $Revision: 1.13 $, $Date: 2005/10/13 08:50:48 $
 * @module reportclient
 */
public class ReportRenderer extends JPanel {
	private static final long serialVersionUID = 6316228563298763509L;

	private ReportTemplate reportTemplate = null;
	/**
	 * �������� �������� ��� ������� ���� ������ ������� �������������
	 * ��������.
	 */
	private int theLowestBorder = 0;
	
	private IntDimension templateBounds = null;
	
	private ApplicationContext aContext = null;
	
	public ReportRenderer(ApplicationContext aContext) {
		this.aContext = aContext;
		jbInit();
	}
	
	private void jbInit(){
		this.setLayout(null);
	}
	
	public void setReportTemplate (ReportTemplate reportTemplate) {
		this.reportTemplate = reportTemplate;
		refreshTemplateBounds();
	}
	
	public void setData(Map<Object, Object> data)
		throws CreateReportException, CreateModelException, ApplicationException, IOException, ElementsIntersectException {
		if (this.reportTemplate == null)
			throw new AssertionError("Report template is not set!");
		
		this.checkForIntersections();
		
		Set<DataStorableElement> dataStorableElements = this.reportTemplate.getDataStorableElements();
		for (DataStorableElement dataElement : dataStorableElements) {
			String modelName = dataElement.getModelClassName();
			ReportModel model = ReportModelPool.getModel(modelName);

			//��� ����������� ������� (��������, "�������������� �������� �������")
			//������ ������ (Id �������) �� ������ �������� �������. ���� ��� -
			//������ �� �� �������.
			Object dsElementData = dataElement.getReportObjectId();
			if (dsElementData.equals(Identifier.VOID_IDENTIFIER)) {
				//����� - ��� �������� ������ �� ������� ������, ��� �� �����
				//���� ���� ��������� � ����������� ������� - ������ �� �����
				//�����.
				dsElementData = data.get(dataElement.getReportName());
				if (dsElementData == null)
					throw new CreateReportException(
							dataElement.getReportName(),
							modelName,
							CreateReportException.NO_DATA_TO_INSTALL);
			}
			
			RenderingComponent component = model.createReport(
					dataElement,
					dsElementData,
					this.aContext);
			
			((JComponent)component).setLocation(dataElement.getX(),dataElement.getY());
			//������ ������������ � createReport
			((JComponent)component).setPreferredSize(this.getSize());			
			
			this.add((JComponent)component);
		}

		for (AttachedTextStorableElement textElement : this.reportTemplate.getAttachedTextStorableElements()) {
			AttachedTextComponent component = new AttachedTextComponent(textElement);
			component.setText(textElement.getText());
			component.setLocation(textElement.getX(),textElement.getY());
			component.setSize(textElement.getWidth(),textElement.getHeight());
			component.setBorder(DataRenderingComponent.DEFAULT_BORDER);
			this.add(component);			
		}

		for (ImageStorableElement imageElement : this.reportTemplate.getImageStorableElements()) {
			ImageRenderingComponent component = new ImageRenderingComponent(
					imageElement,
					imageElement.getBufferedImage());
			component.setLocation(imageElement.getX(),imageElement.getY());
			component.setSize(imageElement.getWidth(),imageElement.getHeight());			
			this.add(component);
		}
		ReportLayout layout = new ReportLayout();
		layout.dolayout(this.getRenderingComponents(),this.reportTemplate);
		
		for (int i = 0; i < this.getComponentCount(); i++) {
			Component component = this.getComponent(i);
			int componentBottomBorder = component.getY() + component.getHeight();
			if (componentBottomBorder >	this.theLowestBorder)
				this.theLowestBorder = componentBottomBorder;
		}
		
		refreshTemplateBounds();
	}
	
	private void refreshTemplateBounds() {
		this.templateBounds = new IntDimension(this.reportTemplate.getSize());
		if (this.reportTemplate.getOrientation().equals(Orientation.LANDSCAPE)) {
			int tempWidth = this.templateBounds.getWidth();
			this.templateBounds.setWidth(this.templateBounds.getHeight());
			this.templateBounds.setHeight(tempWidth);
		}

		int marginSize = this.reportTemplate.getMarginSize();
		if (this.templateBounds.getHeight()	< this.theLowestBorder + marginSize)
			this.templateBounds.setHeight(this.theLowestBorder + marginSize);

		this.setSize(this.templateBounds.getWidth(),this.templateBounds.getHeight());
		this.setPreferredSize(this.getSize());
	}
	
	public List<RenderingComponent> getRenderingComponents() {
		List<RenderingComponent> components = new ArrayList<RenderingComponent>();
		for (int i = 0; i < this.getComponentCount(); i++)
			components.add((RenderingComponent)this.getComponent(i));
		return components;
	}
	
	@Override
	public void paintComponent (Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		
		if (this.reportTemplate != null){
			//������ ���� �������
			g.setColor(Color.BLACK);
			g.drawRect(
					2,
					2,
					this.templateBounds.getWidth() - 3,
					this.templateBounds.getHeight() - 3);
		}
		
		this.paintChildren(g);
	}
	
	/**
	 * �������� �� ����������� ��������� �������
	 * @throws ElementsIntersectException 
	 * @throws ElementsIntersectException, ���� �� ����� ������� ���� ����������� ���������.
	 */
	private void checkForIntersections() throws ApplicationException, ElementsIntersectException{
		Set<Identifiable> templateElements = null;
		templateElements = this.reportTemplate.getReverseDependencies(true);

		for (Identifiable element1 : templateElements){
			for (Identifiable element2 : templateElements){
				StorableElement se1 = (StorableElement)element1;
				StorableElement se2 = (StorableElement)element2;
				if (	(se1 != se2)
					&&	this.elementsIntersect(se1,se2))
					throw new ElementsIntersectException(se1,se2);
			}
		}
	}
	
	private boolean elementsIntersect(
			StorableElement element1,
			StorableElement element2) {
		int e1w = element1.getWidth();
		int e1h = element1.getHeight();
		int e2w = element2.getWidth();
		int e2h = element2.getHeight();
		if (e2w <= 0 || e2h <= 0 || e1w <= 0 || e1h <= 0) {
		    return false;
		}
		int e1x = element1.getX();
		int e1y = element1.getY();
		int e2x = element2.getX();
		int e2y = element2.getY();
		e2w += e2x;
		e2h += e2y;
		e1w += e1x;
		e1h += e1y;
		//      overflow || intersect
		return ((e2w < e2x || e2w > e1x) &&
			(e2h < e2y || e2h > e1y) &&
			(e1w < e1x || e1w > e2x) &&
			(e1h < e1y || e1h > e2y));
	}
	
	/**
	 * ���������� � ���������� true, ����� ��������� HTML �������� ��� ������.
	 * ����� MSHTML, ������� � ������� ������ ������������ ��� ������ ����������,
	 * ���������� ���� ���� �� ��������� � ������.
	 * ���������� � ���������� false ����� ������
 	 */
	public void setPrintable(boolean pritable) {
		int shift = this.reportTemplate.getMarginSize();
		if (!pritable)
			shift = -shift;
		for (int i = 0; i < this.getComponentCount(); i++) {
			Component component = this.getComponent(i);
			Point componentLocation = component.getLocation();
			component.setLocation(
				componentLocation.x - shift,
				componentLocation.y - shift);
		}
	}
}
