/*
 * $Id: FixedSizeEditableTableModel.java,v 1.5 2004/09/27 13:52:39 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.UI;

import java.util.*;
import javax.swing.table.AbstractTableModel;

/**
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2004/09/27 13:52:39 $
 * @module generalclient_v1
 */
public class FixedSizeEditableTableModel extends AbstractTableModel
{
	protected String[] columnNames;
	protected Object[] defaultValues;
	protected List rows = new ArrayList();
	protected int[] editable;

	public FixedSizeEditableTableModel (String[] columnNames, // заголовки столбцов
			Object[] defaultValues,// дефолтные значения
			String[] rowTitles,    // заголовки (0й столбец) строк
			int[] editable)     // номера редактируемых столбцов
	{
		// Устанавливаем заголовки столбцов
		this.columnNames = columnNames;
		this.defaultValues = defaultValues;

		// устанавливаем заголовки строк
		if (rowTitles != null)
			for (int i = 0; i < rowTitles.length; i++)
			{
				List row = new ArrayList(columnNames.length);
				row.add(rowTitles[i]);  // устанавливаем заголовки строк
				for (int j = 0; j < defaultValues.length; j++)
					row.add(defaultValues[j]); // устанавливаем дефолтные данные
				rows.add(row);
			}

		if (editable == null)
			this.editable = new int[0];
		else
			this.editable = editable;
	}

	public void setEditableColumns (int[] columns)
	{
		editable = columns;
	}

	public void clearTable()
	{
		rows = new ArrayList();
		super.fireTableDataChanged();
	}

	public int getColumnCount()
	{
		return columnNames.length;
	}

	public int getRowCount()
	{
		return rows.size();
	}

	public String getColumnName(int col)
	{
		return columnNames[col];
	}

	public Object getValueAt(int row, int col)
	{
		List r = (List)rows.get(row);
		return r.get(col);
	}

	public Class getColumnClass(int c)
	{
		if (c == 0)
			return String.class;
		return defaultValues[c-1].getClass();
	}

	public void setValueAt(Object value, int row, int col)
	{
		List r = (List)rows.get(row);
		r.set(col, value);
		super.fireTableCellUpdated(row, col);
	}

	public int addRow(String rowTitle, Object[] values)
	{
		List r = new ArrayList(values.length + 1);
		r.add(rowTitle);
		for (int i = 0; i < values.length; i++)
			r.add(values[i]);
		rows.add(r);
		super.fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
		return rows.size() - 1;
	}

	public int addRow(String rowTitle)
	{
		List r = new ArrayList(defaultValues.length + 1);
		r.add(rowTitle);
		for (int i = 0; i < defaultValues.length; i++)
			r.add(defaultValues[i]);
		rows.add(r);
		super.fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
		return rows.size() - 1;
	}

	public void removeRow(int row)
	{
		rows.remove(row);
		super.fireTableRowsDeleted(row, row);
	}

	public void updateRow(Object value, int row)
	{
		List r = (List)rows.get(row);
		r.set(1, value);
		super.fireTableDataChanged();
	}

	public void updateRow(Object[] values, int row)
	{
		List r = (List)rows.get(row);

		for (int i = 0; i < values.length; i++)
			r.set(i + 1, values[i]);
		super.fireTableDataChanged();
	}

	public void updateRow(String rowTitle, Object value, int row)
	{
		List r = (List) rows.get(row);
		r.set(0, rowTitle);
		r.set(1, value);
		super.fireTableDataChanged();
	}

	public void updateRow(String rowTitle, Object[] values, int row)
	{
		List r = (List) rows.get(row);
		r.set(0, rowTitle);
		for (int i = 0; i < values.length; i++)
			r.set(i + 1, values[i]);
		super.fireTableDataChanged();
	}

	public void updateColumn(Object[] updatedData, int col)
	{
		for (int i = 0; i < updatedData.length; i++)
			((List) (rows.get(i))).set(col, updatedData[i]);
		super.fireTableDataChanged();
	}

	public void updateData(Object[][] updatedData)
	{
		for (int i = 0; i < updatedData.length; i++)
		{
			List r = (List)rows.get(i);
			for (int j = 0; j < updatedData[i].length; j++)
				r.set(j + 1, updatedData[i][j]);
		}
		super.fireTableDataChanged();
	}

	public boolean isCellEditable(int row, int col)
	{
		for (int i = 0; i < editable.length; i++)
			if (col == editable[i])
				return true;
		return false;
	}
}
