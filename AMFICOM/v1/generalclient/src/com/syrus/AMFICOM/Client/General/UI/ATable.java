package com.syrus.AMFICOM.Client.General.UI;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class ATable extends JTable
{
	private int height = 4;
	private int initialHeight = -1;

	public ATable()
	{
		super();
		updateHeaderSize();
	}

	public ATable(Vector rowData, Vector columnNames)
	{
		super(rowData, columnNames);
		updateHeaderSize();
	}

	public ATable(Object[][] rowData, Object[] columnNames)
	{
		super(rowData, columnNames);
		updateHeaderSize();
	}

	public ATable(TableModel tm)
	{
		super(tm);
		updateHeaderSize();
	}

	public ATable(TableModel tm, TableColumnModel cm)
	{
		super(tm, cm);
		updateHeaderSize();
	}

	public ATable(TableModel tm, TableColumnModel cm, ListSelectionModel sm)
	{
		super(tm, cm, sm);
		updateHeaderSize();
	}

	public ATable(int numRows, int numColumns)
	{
		super(numRows, numColumns);
		updateHeaderSize();
	}

	public void setModel(TableModel dataModel)
	{
		super.setModel(dataModel);
		updateHeaderSize();
	}

	public void setColumnModel(TableColumnModel columnModel)
	{
		super.setColumnModel(columnModel);
		updateHeaderSize();
	}

	public void setHeaderHeight(int height)
	{
		this.height = height;
	}

	public int getHeaderHeight()
	{
		return height;
	}

	public void updateHeaderSize()
	{
		JTableHeader jTableHeader = getTableHeader();
		if (jTableHeader == null)
			return;

		if (initialHeight == -1)
			initialHeight = jTableHeader.getPreferredSize().height;

		boolean hasNonEmptyTitle = false;
		for (int i = 0; i < getColumnCount(); i++)
		{
			if (getColumnName(i) != null && getColumnName(i).length() != 0)
			{
				hasNonEmptyTitle = true;
				break;
			}
		}

		if (!hasNonEmptyTitle)
			jTableHeader.setPreferredSize(new Dimension(jTableHeader.getPreferredSize().width, height));
		else
			jTableHeader.setPreferredSize(new Dimension(jTableHeader.getPreferredSize().width, initialHeight));
	}
}
