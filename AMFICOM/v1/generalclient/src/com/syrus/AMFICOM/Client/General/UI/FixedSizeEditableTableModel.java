package com.syrus.AMFICOM.Client.General.UI;

import java.util.*;

import javax.swing.table.*;

public class FixedSizeEditableTableModel extends AbstractTableModel
{
	protected String[] columnNames;
	protected Object[] p_defaultv;
	protected List rows = new ArrayList();
	protected int[] editable;

	public FixedSizeEditableTableModel (String[] p_columns, // заголовки столбцов
																			Object[] p_defaultv,// дефолтные значения
																			String[] p_rows,    // заголовки (0й столбец) строк
																			int[] editable)     // номера редактируемых столбцов
	{
		// Устанавливаем заголовки столбцов
		columnNames = new String[p_columns.length];
		this.p_defaultv = p_defaultv;
		for (int i = 0; i < p_columns.length; i++)
			columnNames[i] = new String(p_columns[i]);

		// устанавливаем заголовки строк
		if (p_rows != null)
			for (int i = 0; i < p_rows.length; i++)
			{
				List row = new ArrayList(p_columns.length);
				row.add(p_rows[i]);  // устанавливаем заголовки строк
				for (int j = 0; j < p_defaultv.length; j++)
					row.add(p_defaultv[j]); // устанавливаем дефолтные данные
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
		return p_defaultv[c-1].getClass();
	}

	public void setValueAt(Object value, int row, int col)
	{
		List r = (List)rows.get(row);
		r.set(col, value);
		super.fireTableCellUpdated(row, col);
	}

	public int addRow (String p_row, Object[] values)
	{
		List r = new ArrayList(values.length + 1);
		r.add(p_row);
		for (int i = 0; i < values.length; i++)
			r.add(values[i]);
		rows.add(r);
		super.fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
		return rows.size() - 1;
	}

	public int addRow (String p_row)
	{
		List r = new ArrayList(p_defaultv.length + 1);
		r.add(p_row);
		for (int i = 0; i < p_defaultv.length; i++)
			r.add(p_defaultv[i]);
		rows.add(r);
		super.fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
		return rows.size() - 1;
	}

	public void removeRow (int row)
	{
		rows.remove(row);
		super.fireTableRowsDeleted(row, row);
	}

	public void updateRow(Object[] updatedRow, int row)
	{
		List r = (List)rows.get(row);

		for (int i = 0; i < updatedRow.length; i++)
			r.set(i + 1, updatedRow[i]);
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
		{
			if (col == editable[i])
				return true;
		}
		return false;
	}
}
