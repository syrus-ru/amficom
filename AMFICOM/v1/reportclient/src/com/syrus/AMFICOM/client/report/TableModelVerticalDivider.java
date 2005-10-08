/*
 * $Id: TableModelVerticalDivider.java,v 1.2 2005/10/08 13:30:14 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.report.TableDataStorableElement;
/**
 * Класс для реализации вртикального разбиения исходной табличной
 * модели.
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/10/08 13:30:14 $
 * @author Peskovsky Peter
 * @module reportclient
 */
public class TableModelVerticalDivider {
	private static final int DEFAULT_COLUMN_WIDTH = 100;
	public static TableDataRenderingComponent createReport(
			AbstractTableModel tableModel,
			TableDataStorableElement tableStorableElement) {
		int vertDivisionsCount =
			tableStorableElement.getVerticalDivisionsCount();
		
		TableDataRenderingComponent renderingComponent = null;
		renderingComponent = new TableDataRenderingComponent(
			tableStorableElement,
			new TestReportTableModel(tableModel,vertDivisionsCount),
			getTableColumnWidths(tableModel,vertDivisionsCount));
		
		return renderingComponent;
	}
	
	private static List<Integer> getTableColumnWidths(
			AbstractTableModel tableModel,
			int vertDivisionsCount) {
		List<Integer> tableColumnWidths = new ArrayList<Integer>();
		for (
				int j = 0;
				j < tableModel.getColumnCount() * vertDivisionsCount;
				j++)
			tableColumnWidths.add(new Integer(DEFAULT_COLUMN_WIDTH));

		return tableColumnWidths;
	}
}

class TestReportTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -1354361144242928271L;

	private AbstractTableModel tableModel = null;
	
	private static final String EMPTY_STRING = "";
	
	private int vertDivisionsCount = 1;
	private int originalRowCount = 0;
	private int originalColumnCount = 0;	
	private int rowCount = 1;
	private int columnCount = 1;	
	
	protected TestReportTableModel (
			AbstractTableModel tableModel,
			int vertDivisionsCount) {
		this.vertDivisionsCount = vertDivisionsCount;
		this.tableModel = tableModel;
		
		this.originalRowCount = this.tableModel.getRowCount();
		this.originalColumnCount = this.tableModel.getColumnCount();
		
		//Вычисляем число строк и столбцов для таблицы
		this.rowCount = this.originalRowCount / this.vertDivisionsCount;
		if (this.originalRowCount % this.vertDivisionsCount > 0)		
			this.rowCount++;
		
		this.columnCount = this.originalColumnCount * this.vertDivisionsCount;
	}
	
	public int getRowCount() {
		return this.rowCount;
	}

	public int getColumnCount() {
		return this.columnCount;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return this.tableModel.getColumnName(
				columnIndex % this.originalColumnCount);
    }
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		int absoluteRowIndex = this.getRowCount()
			* (columnIndex / this.originalColumnCount) + rowIndex;
		if (rowIndex >= this.originalRowCount)
			return EMPTY_STRING;
		
		return this.tableModel.getValueAt(
				absoluteRowIndex,
				columnIndex % this.originalColumnCount);
	}
}

