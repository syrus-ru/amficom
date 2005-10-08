/*
 * $Id: ReportRenderer.java,v 1.11 2005/10/08 13:30:14 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
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

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.ImageStorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.AMFICOM.report.ReportTemplate.Orientation;
import com.syrus.AMFICOM.resource.IntDimension;

/**
 * Реализует отчёт по шаблону
 * @author $Author: arseniy $
 * @version $Revision: 1.11 $, $Date: 2005/10/08 13:30:14 $
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
		throws CreateReportException, CreateModelException, ApplicationException, IOException {
		if (this.reportTemplate == null)
			throw new AssertionError("Report template is not set!");
		
		for (DataStorableElement dataElement : this.reportTemplate.getDataStorableElements()) {
			String modelName = dataElement.getModelClassName();
			ReportModel model = ReportModelPool.getModel(modelName);

			//Для сиюминутных отчётов (например, "Характеристики схемного объекта")
			//достаём данные (Id объекта) из самого элемента шаблона. Если нет -
			//достаём их из таблицы.
			Object dsElementData = dataElement.getReportObjectId();
			if (dsElementData == null) {
				//Иначе - при создании отчёта из другого модуля, где не может
				//быть двух элементов с одинаковыми именами - достаём по имени
				//очёта.
				dsElementData = data.get(dataElement.getReportName());
			}
			
			if (dsElementData == null)
				throw new CreateReportException(
						dataElement.getReportName(),
						modelName,
						CreateReportException.NO_DATA_TO_INSTALL);
			
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
			//Рисуем края шаблона
			g.setColor(Color.BLACK);
			g.drawRect(
					2,
					2,
					this.templateBounds.getWidth() - 3,
					this.templateBounds.getHeight() - 3);
			
//			//Рисуем поля шаблона
//			g.setColor(Color.BLACK);
//			g.drawRect(
//					marginSize,
//					marginSize,
//					templateSize.getWidth() - 2 * marginSize,
//					this.theLowestBorder - marginSize + 3);
		}
		
		this.paintChildren(g);
	}
	
	/**
	 * Вызывается с параметром true, когда готовится HTML документ для печати.
	 * Иначе MSHTML, который в текущий момент используется для печати документов,
	 * складывает свои поля по умолчанию с нашими.
	 * Вызывается с параметром false после печати
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
