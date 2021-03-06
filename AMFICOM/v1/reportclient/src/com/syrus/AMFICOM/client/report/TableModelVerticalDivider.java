/*
 * $Id: TableModelVerticalDivider.java,v 1.4 2006/04/25 10:59:34 stas Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.report.TableDataStorableElement;
/**
 * ????? ??? ?????????? ???????????? ????????? ???????? ?????????
 * ??????.
 * @author $Author: stas $
 * @version $Revision: 1.4 $, $Date: 2006/04/25 10:59:34 $
 * @author Peskovsky Peter
 * @module reportclient
 */
public class TableModelVerticalDivider {
	
	public static TableDataRenderingComponent createReport(
			AbstractTableModel tableModel,
			TableDataStorableElement tableStorableElement) {
		int vertDivisionsCount =
			tableStorableElement.getVerticalDivisionsCount();
		
		TableDataRenderingComponent renderingComponent = null;
		renderingComponent = new TableDataRenderingComponent(
			tableStorableElement,
			new TestReportTableModel(tableModel,vertDivisionsCount));
		
		return renderingComponent;
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
		
		//????????? ????? ????? ? ???????? ??? ???????
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

