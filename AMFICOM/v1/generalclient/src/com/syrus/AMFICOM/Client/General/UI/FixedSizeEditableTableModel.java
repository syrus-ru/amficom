package com.syrus.AMFICOM.Client.General.UI;

import javax.swing.table.*;
import java.util.*;

public class FixedSizeEditableTableModel extends AbstractTableModel
{
	String[] columnNames;
	Object[] p_defaultv;
	//Object[][] data;
	protected Vector rows = new Vector();
	int[] editable;

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

		// устанавливаем дефолтные данные
		//data = new Object[p_rows.length][p_columns.length];

		if (p_rows != null)
		for (int i = 0; i < p_rows.length; i++)
		{
			Vector row = new Vector(p_columns.length);
			row.add(p_rows[i]);  // устанавливаем заголовки строк
			for (int j = 0; j < p_defaultv.length; j++)
			{
				row.add(p_defaultv[j]); // устанавливаем дефолтные данные
				//data[i][j] = (p_defaultv[j]);
			}
			rows.add(row);
		}

		// устанавливаем заголовки строк
	/*	for (int i = 0; i < p_rows.length; i++)
		{
			data[i][0] = p_rows[i];
		}*/
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
		//data = new Object[][]{};
		rows = new Vector();
		super.fireTableDataChanged();
	}

	public int getColumnCount()
	{
		return columnNames.length;
	}

	public int getRowCount()
	{
		return rows.size();//data.length;
	}

	public String getColumnName(int col)
	{
		return columnNames[col];
	}

	public Object getValueAt(int row, int col)
	{
		//return data[row][col];
		Vector r = (Vector)rows.get(row);
		return r.get(col);
	}

	public Class getColumnClass(int c)
	{
		if (c == 0)
			return String.class;
		return p_defaultv[c-1].getClass();
		//return getValueAt(0, c).getClass();
	}

	public void setValueAt(Object value, int row, int col)
	{
		//data[row][col] = value;
		Vector r = (Vector)rows.get(row);
		r.set(col, value);
		super.fireTableCellUpdated(row, col);
	}

	public int addRow (String p_row, Object[] p_defaultv)
	{
		Vector r = new Vector(p_defaultv.length + 1);
		r.add(p_row);
		for (int i = 0; i < p_defaultv.length; i++)
			r.add(p_defaultv[i]);
		rows.add(r);
		super.fireTableRowsInserted(rows.size()-1, rows.size()-1);
		return rows.size()-1;
	}

	public int addRow (String p_row)
	{
		Vector r = new Vector(p_defaultv.length + 1);
		r.add(p_row);
		for (int i = 0; i < p_defaultv.length; i++)
			r.add(p_defaultv[i]);
		rows.add(r);
		super.fireTableRowsInserted(rows.size()-1, rows.size()-1);
		return rows.size()-1;
	}

	public void removeRow (int row)
	{
		rows.remove(row);
		super.fireTableRowsDeleted(row, row);
	}

	public void updateRow(Object[] updatedRow, int row)
	{
		Vector r = (Vector)rows.get(row);

		for (int i = 0; i < updatedRow.length; i++)
			//data[row][i] = updatedRow[i];
			r.setElementAt(updatedRow[i], i+1);
		super.fireTableDataChanged();
	}

	public void updateData(Object[][] updatedData)
	{
		/*for (int i = 0; i < data.length; i++)
			for (int j = 1; j < columnNames.length; j++)
			{
				data[i][j] = updatedData[i][j-1];
			}*/
		for (int i = 0; i < updatedData.length; i++)
		{
			Vector r = (Vector)rows.get(i);
			for (int j = 0; j < updatedData[i].length; j++)
				r.setElementAt(updatedData[i][j], j+1);
		}
		super.fireTableDataChanged();
	}

	public boolean isCellEditable(int p_row, int p_col)
	{
		for (int i = 0; i < editable.length; i++)
		{
			if (p_col == editable[i])
				return true;
		}
		return false;
	}

}