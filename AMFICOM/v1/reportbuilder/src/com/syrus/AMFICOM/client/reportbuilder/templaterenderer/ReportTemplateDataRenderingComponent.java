/*
 * $Id: ReportTemplateDataRenderingComponent.java,v 1.1 2005/09/01 14:21:40 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;

import com.syrus.AMFICOM.client.report.DataRenderingComponent;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.client.report.ReportModelPool;
import com.syrus.AMFICOM.report.DataStorableElement;

public class ReportTemplateDataRenderingComponent extends
		DataRenderingComponent {

	private static final long serialVersionUID = -3594197341032602733L;
	private	String reportName = null;
	private Point reportNameLocation = new Point();
	private	String modelName = null;	
	private Point modelNameLocation = new Point();
	
	public ReportTemplateDataRenderingComponent (DataStorableElement dre)
	{
		super(dre);
		
		ReportModel reportModel = ReportModelPool.getModel(dre.getModelClassName());
		this.reportName = reportModel.getReportElementName(dre);
		this.modelName = reportModel.getName();		
	}
	
	public void paintComponent (Graphics g)
	{
		super.paint(g);
		
		g.drawString(
			this.modelName,
			this.modelNameLocation.x,
			this.modelNameLocation.y);
			
		g.drawString(
			this.reportName,
			this.reportNameLocation.x,
			this.reportNameLocation.y);
	}
	
	/**
	 * Метод вызывается при изменении габаритов элемента отображения.
	 */
	public void refreshLabels()
	{
		this.modelNameLocation.y = (int) (this.getHeight() / 3.D);
		this.reportNameLocation.y = (int) (this.getHeight() * 2.D / 3.D);
		
		Graphics g = this.getGraphics();
		FontMetrics fontMetrics = g.getFontMetrics();
		
		double modelNameWidth = fontMetrics.getStringBounds(this.modelName,g).getWidth();
		this.modelNameLocation.x = (int)((this.getWidth() - modelNameWidth) / 2.D);
		
		double reportNameWidth = fontMetrics.getStringBounds(this.reportName,g).getWidth();
		this.reportNameLocation.x = (int)((this.getWidth() - reportNameWidth) / 2.D);		
	}
	
	public void setX(int x) {
		this.setLocation(x,this.getY());
	}

	public void setY(int y) {
		this.setLocation(this.getX(),y);
	}

	public void setWidth(int width) {
		this.setSize(width,this.getHeight());
	}

	public void setHeight(int height) {
		this.setSize(this.getWidth(),height);
	}
}
