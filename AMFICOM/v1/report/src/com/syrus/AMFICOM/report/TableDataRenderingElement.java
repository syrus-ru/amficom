/*
 * $Id: TableDataRenderingElement.java,v 1.1 2005/08/11 11:15:30 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

import com.syrus.AMFICOM.general.Identifier;

/**
 * Класс для отображения данных в табличном виде
 * @author $Author: peskovsky $
 * @version $Revision: 1.1 $, $Date: 2005/08/11 11:15:30 $
 * @module report_v1
 */
public final class TableDataRenderingElement extends DataRenderingElement implements Serializable {
	private static final long serialVersionUID = -2699698026579054587L;

	private static int DEFAULT_COLUMN_WIDTH = 100;
	
	private int[] columnWidths = null;
	
	public TableDataRenderingElement (
			String reportName,
			String modelClassName,
			Point location,
			int[] columnWidths)
	{
		super(reportName,modelClassName);
		this.columnWidths = columnWidths;
		
		this.setLocation(location);
		this.setSize(this.getTotalColumnWidth(),-1);
	}

	public TableDataRenderingElement (
			String reportName,
			String modelClassName,
			Point location,
			int columnCount)
	{
		super(reportName,modelClassName);		
		this.columnWidths = new int[columnCount];
		Arrays.fill(this.columnWidths,TableDataRenderingElement.DEFAULT_COLUMN_WIDTH);
		
		this.setLocation(location);
		this.setSize(this.getTotalColumnWidth(),-1);
	}
	
	public int getTotalColumnWidth()
	{
		int totalColumnWidth = 0;
		for (int i = 0; i < this.columnWidths.length; i++)
			totalColumnWidth += this.columnWidths[i];
		
		return totalColumnWidth;
	}
	
	public void writeObject(java.io.ObjectOutputStream out) throws IOException {
		//При сериализации дополнительные данные не сериализуются.
		//Мы сохраняем ШАБЛОНЫ, а не отчёты.
		out.writeObject(this.getBounds());
		out.writeObject(this.getId());		
		out.writeObject(this.getReportName());		
		out.writeObject(this.getModelClassName());

		//TODO Проверить правильно ли будет сериализоваться
		//массив интов.
		out.writeObject(this.columnWidths);
	}

	public void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.setBounds((Rectangle)in.readObject());
		this.id = (Identifier)in.readObject();
		this.reportName = (String)in.readObject();
		this.modelClassName = (String)in.readObject();

		//TODO Проверить правильно ли будет сериализоваться
		//массив интов.
		
		this.columnWidths = (int[])in.readObject();		
	}

	public int[] getColumnWidths() {
		return this.columnWidths;
	}

	public void setColumnWidth(int columnIndex, int columnWidth) {
		this.columnWidths[columnIndex] = columnWidth;
	}
}
