/*
 * $Id: ReportRenderer.java,v 1.18 2006/04/24 12:41:04 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.report.AbstractDataStorableElement;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.ImageStorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.AMFICOM.report.StorableElement;

/**
 * Реализует отчёт по шаблону
 * @author $Author: stas $
 * @version $Revision: 1.18 $, $Date: 2006/04/24 12:41:04 $
 * @module reportclient
 */
public class ReportRenderer extends JPanel {
	private static final long serialVersionUID = 6316228563298763509L;

	private ReportTemplate reportTemplate = null;
	/**
	 * Значение ординаты для нижнего края самого нижнего отображенного
	 * элемента.
	 */
	private int theLowestBorder = 0;
	
	private Dimension templateBounds = null;
	
	private ApplicationContext aContext = null;
	
	public ReportRenderer(ApplicationContext aContext) {
		this.aContext = aContext;
		jbInit();
	}
	
	private void jbInit(){
		this.setLayout(null);
		setBackground(Color.WHITE);
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
		
		Set<AbstractDataStorableElement> dataStorableElements = this.reportTemplate.getDataStorableElements();
		for (AbstractDataStorableElement dataElement : dataStorableElements) {
			String modelName = dataElement.getModelClassName();
			ReportModel model = ReportModelPool.getModel(modelName);

			//Для сиюминутных отчётов (например, "Характеристики схемного объекта")
			//достаём данные (Id объекта) из самого элемента шаблона. Если нет -
			//достаём их из таблицы.
			Object dsElementData = dataElement.getReportObjectId();
			if (dsElementData.equals(Identifier.VOID_IDENTIFIER)) {
				//Иначе - при создании отчёта из другого модуля, где не может
				//быть двух элементов с одинаковыми именами - достаём по имени
				//очёта.
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
			//Размер выставляется в createReport
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
		this.templateBounds = this.reportTemplate.getDimensions();
		this.templateBounds.height = this.theLowestBorder + ReportTemplate.VERTICAL_MARGIN_SIZE;

		this.setSize(this.templateBounds);
		this.setPreferredSize(this.templateBounds);
	}
	
	public List<RenderingComponent> getRenderingComponents() {
		List<RenderingComponent> components = new ArrayList<RenderingComponent>();
		for (int i = 0; i < this.getComponentCount(); i++)
			components.add((RenderingComponent)this.getComponent(i));
		return components;
	}
		
	/**
	 * Проверка на пересечения элементов шаблона
	 * @throws ElementsIntersectException 
	 * @throws ElementsIntersectException, если на схеме шаблона есть пересечения элементов.
	 */
	private void checkForIntersections() throws ApplicationException, ElementsIntersectException{
		Set<Identifiable> templateElements = new HashSet<Identifiable>();
		templateElements.addAll(this.reportTemplate.getAttachedTextStorableElements());
		templateElements.addAll(this.reportTemplate.getDataStorableElements());
		templateElements.addAll(this.reportTemplate.getImageStorableElements());

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
	 * Вызывается с параметром true, когда готовится HTML документ для печати.
	 * Иначе MSHTML, который в текущий момент используется для печати документов,
	 * складывает свои поля по умолчанию с нашими.
	 * Вызывается с параметром false после печати
 	 */
	public void setPrintable(boolean pritable) {
		this.templateBounds = this.reportTemplate.getMargins();
		this.templateBounds.height = this.theLowestBorder;
		
		setSize(this.templateBounds);
		setPreferredSize(this.templateBounds);
		
		int shift = ReportTemplate.STANDART_LEFT_MARGIN_SIZE;
		int shiftv = ReportTemplate.VERTICAL_MARGIN_SIZE;
		if (!pritable) {
			shift = -shift;
			shiftv = -shiftv;
		}
		for (int i = 0; i < this.getComponentCount(); i++) {
			Component component = this.getComponent(i);
			Point componentLocation = component.getLocation();
			component.setLocation(
				componentLocation.x - shift,
				componentLocation.y - shiftv);
		}
	}
}
