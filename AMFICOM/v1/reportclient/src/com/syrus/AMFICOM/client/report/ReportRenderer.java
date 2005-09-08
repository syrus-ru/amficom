/*
 * $Id: ReportRenderer.java,v 1.5 2005/09/08 13:59:10 peskovsky Exp $
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.ImageStorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.AMFICOM.resource.IntDimension;

/**
 * Реализует отчёт по шаблону
 * @author $Author: peskovsky $
 * @version $Revision: 1.5 $, $Date: 2005/09/08 13:59:10 $
 * @module reportclient_v1
 */
public class ReportRenderer extends JPanel {
	private static final long serialVersionUID = 6316228563298763509L;

	private ReportTemplate reportTemplate = null;
	
	public ReportRenderer() {
		jbInit();
	}
	
	private void jbInit(){
		this.setLayout(null);
	}
	
	public void setReportTemplate (ReportTemplate reportTemplate) {
		this.reportTemplate = reportTemplate;
	}
	
	public void setData(Map<String, Object> data) throws CreateReportException {
		if (this.reportTemplate == null)
			throw new AssertionError("Report template is not set!");
		
		for (DataStorableElement dataElement : this.reportTemplate.getDataStorableElements()) {
			String dsElementLangName = LangModelReport.getString(dataElement.getReportName());
			String modelName = dataElement.getModelClassName();
			
			ReportModel model = ReportModelPool.getModel(modelName);
			if (model == null)
				throw new CreateReportException (
						dsElementLangName,
						CreateReportException.reportModelIsAbsent);
			
			Object dsElementData = data.get(dsElementLangName);
			if (dsElementData == null)
				throw new CreateReportException (
						dsElementLangName,
						CreateReportException.noDataToInstall);
			
			RenderingComponent component = model.createReport(
					dataElement,
					dsElementData);
			
			((JComponent)component).setLocation(dataElement.getX(),dataElement.getY());
			//Размеры выставляются в createReport
			
			this.add((JComponent)component);
		}

		for (AttachedTextStorableElement textElement : this.reportTemplate.getTextStorableElements()) {
			AttachedTextComponent component = new AttachedTextComponent(textElement);
			component.setText(textElement.getText());
			component.setLocation(textElement.getX(),textElement.getY());
			component.setSize(textElement.getWidth(),textElement.getHeight());
			component.setBorder(AttachedTextComponent.DEFAULT_BORDER);
			this.add(component);			
		}

		for (ImageStorableElement imageElement : this.reportTemplate.getImageStorableElements()) {
			ImageRenderingComponent component = new ImageRenderingComponent(
					imageElement,
					imageElement.getImage());
			component.setLocation(imageElement.getX(),imageElement.getY());
			component.setSize(imageElement.getWidth(),imageElement.getHeight());			
			this.add(component);
		}
		ReportLayout layout = new ReportLayout();
		layout.dolayout(this.getRenderingComponents(),this.reportTemplate);
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
			IntDimension templateSize = this.reportTemplate.getSize();
			g.setColor(Color.BLACK);
			g.drawRect(
					2,
					2,
					templateSize.getWidth() - 3,
					templateSize.getHeight() - 3);
			
			//Рисуем поля шаблона
			int marginSize = this.reportTemplate.getMarginSize();
			g.setColor(Color.BLACK);
			g.drawRect(
					marginSize,
					marginSize,
					templateSize.getWidth() - 2 * marginSize,
					templateSize.getHeight() - 2 * marginSize);
		}
		
		this.paintChildren(g);
	}
	
	/**
	 * Вызвается когда готовится HTML документ для печати. Иначе MSHTML,
	 * который в текущий момент используется для печати документов, складывает
	 * свои поля по умолчанию с нашими.
 	 */
	public void setPrintable() {
		int marginSize = this.reportTemplate.getMarginSize();
		for (int i = 0; i < this.getComponentCount(); i++) {
			Component component = this.getComponent(i);
			Point componentLocation = component.getLocation();
			component.setLocation(
				componentLocation.x - marginSize,
				componentLocation.y - marginSize);
		}
	}
}
