/*
 * ObjectResourceTable.java
 * Created on 03.08.2004 9:25:28
 * 
 */

package com.syrus.AMFICOM.Client.General.UI;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * @author Vladimir Dolzhenko
 */
public class ObjectResourceTable extends JTable {

	int	lastSortedIndex	= -1;

	public ObjectResourceTable() {
		customInitialization();
	}

	public ObjectResourceTable(TableModel dm) {
		super(dm);
		customInitialization();
	}

	public ObjectResourceTable(TableModel dm, TableColumnModel cm) {
		super(dm, cm);
		customInitialization();
	}

	public ObjectResourceTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
		super(dm, cm, sm);
		customInitialization();
	}

	public ObjectResourceTable(int numRows, int numColumns) {
		super(numRows, numColumns);
		customInitialization();
	}

	public ObjectResourceTable(Vector rowData, Vector columnNames) {
		super(rowData, columnNames);
		customInitialization();
	}

	public ObjectResourceTable(final Object[][] rowData, final Object[] columnNames) {
		super(rowData, columnNames);
		customInitialization();
	}

	private void customInitialization() {
		this.setColumnSelectionAllowed(false);
		this.setRowSelectionAllowed(true);

		this.getTableHeader().addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent evt) {
				JTableHeader header = (JTableHeader) evt.getSource();
				JTable table = header.getTable();
				TableColumnModel colModel = table.getColumnModel();

				// The index of the column whose header was
				// clicked
				int columnIndex = colModel.getColumnIndexAtX(evt.getX());
				int mColIndex = table.convertColumnIndexToModel(columnIndex);
				ObjResTableModel model = (ObjResTableModel) table.getModel();
				String s;
				if (model.getSortOrder(mColIndex))
					s = " v "; //$NON-NLS-1$
				else
					s = " ^ "; //$NON-NLS-1$
				table.getColumnModel().getColumn(columnIndex)
						.setHeaderValue(s + model.getColumnName(mColIndex) + s);

				for (int i = 0; i < model.getColumnCount(); i++) {
					if (i != mColIndex)
						table.getColumnModel().getColumn(table.convertColumnIndexToView(i))
								.setHeaderValue(model.getColumnName(i));
				}

				// Force the header to resize and repaint itself
				header.resizeAndRepaint();
				lastSortedIndex = mColIndex;
				model.sortRows(mColIndex);

				// Return if not clicked on any column header
				if (columnIndex == -1) { return; }

				// Determine if mouse was clicked between column
				// heads
				Rectangle headerRect = table.getTableHeader().getHeaderRect(columnIndex);
				if (columnIndex == 0) {
					headerRect.width -= 3; // Hard-coded
							       // constant
				} else {
					headerRect.grow(-3, 0); // Hard-coded
								// constant
				}
				if (!headerRect.contains(evt.getX(), evt.getY())) {
					// Mouse was clicked between column
					// heads
					// vColIndex is the column head closest
					// to the click

					// vLeftColIndex is the column head to
					// the left of the
					// click
					int vLeftColIndex = columnIndex;
					if (evt.getX() < headerRect.x) {
						vLeftColIndex--;
					}
				}
			}
		});

	}

	public void resort() {
		if (this.lastSortedIndex >= 0) {			
			ObjResTableModel model = (ObjResTableModel) this.getModel();
			if (model!=null)
				model.sortRows(this.lastSortedIndex);
		}		
	}

}