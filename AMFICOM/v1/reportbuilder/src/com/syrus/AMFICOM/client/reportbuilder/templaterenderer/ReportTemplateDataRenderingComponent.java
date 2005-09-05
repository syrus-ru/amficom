/*
 * $Id: ReportTemplateDataRenderingComponent.java,v 1.3 2005/09/05 12:22:51 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.templaterenderer;

import java.awt.Color;
import java.awt.Dimension;
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
	
	public static final Dimension DEFAULT_SIZE = new Dimension(300,100);
	
	private	String reportName = null;
	private Point reportNameLocation = new Point();
	private	String modelName = null;	
	private Point modelNameLocation = new Point();
	
	private Dimension minimumSize = new Dimension();
	double modelNameWidth = 0;
	double reportNameWidth = 0;
	
	public ReportTemplateDataRenderingComponent (DataStorableElement dre) {
		super(dre);
		
		ReportModel reportModel = ReportModelPool.getModel(dre.getModelClassName());
		this.reportName = reportModel.getReportElementName(dre.getReportName());
		this.modelName = reportModel.getName();
	}
	
	public void paintComponent (Graphics g)	{
		g.setColor(Color.WHITE);
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		
		g.setColor(Color.black);		
		g.drawString(
			this.modelName,
			this.modelNameLocation.x,
			this.modelNameLocation.y);
			
		g.drawString(
			this.reportName,
			this.reportNameLocation.x,
			this.reportNameLocation.y);
		
		g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
	}
	
	/**
	 * Метод вызывается при изменении габаритов элемента отображения.
	 */
	private void refreshLabels() {
		this.modelNameLocation.y = (int) (this.getHeight() / 3.D);
		this.reportNameLocation.y = (int) (this.getHeight() * 2.D / 3.D);
		
		this.modelNameLocation.x = (int)((this.getWidth() - this.modelNameWidth) / 2.D);
		this.reportNameLocation.x = (int)((this.getWidth() - this.reportNameWidth) / 2.D);
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
	
	@Override
	public void setSize(Dimension newSize) {
		this.setSize(newSize.width,newSize.height);
	}
	
	@Override
	public void setSize(int width, int height) {
		super.setSize(
				width > this.minimumSize.width ? width : this.minimumSize.width,
				height > this.minimumSize.height ? height : this.minimumSize.height);
		refreshLabels();
	}
	
	/**
	 * Инициализируются минимальные значения габаритов объекта.
	 * Вызывается как только у окна появляется Graphics.
	 */
	public void initMinimumSizes() {
		Graphics g = this.getGraphics();
		FontMetrics fontMetrics = g.getFontMetrics();
		
		this.modelNameWidth =
			fontMetrics.getStringBounds(this.modelName,g).getWidth();
		this.reportNameWidth =
			fontMetrics.getStringBounds(this.reportName,g).getWidth();
		
		this.minimumSize.width = 
			(int)(this.modelNameWidth > this.reportNameWidth 
			? this.modelNameWidth : this.reportNameWidth) + 5;
		
		this.minimumSize.height = fontMetrics.getHeight() * 3 + 5;
	}
}
