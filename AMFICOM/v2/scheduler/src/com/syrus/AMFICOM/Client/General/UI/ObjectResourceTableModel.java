/*
 * ObjectResourceTableModel.java
 * Created on 02.08.2004 9:50:37
 * 
 */
package com.syrus.AMFICOM.Client.General.UI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

/**
 * @author Vladimir Dolzhenko
 */
public abstract class ObjectResourceTableModel extends AbstractTableModel{
	
	public class ColumnSorter implements Comparator {

		boolean	ascending;

		int		colIndex;

		ColumnSorter(int colIndex, boolean ascending) {
			this.colIndex = colIndex;
			this.ascending = ascending;
		}

		public int compare(Object a, Object b) {
			int result = 0;
			ObjectResourceTableRow v1 = (ObjectResourceTableRow) a;
			ObjectResourceTableRow v2 = (ObjectResourceTableRow) b;
			Object o1 = v1.get(this.colIndex);
			Object o2 = v2.get(this.colIndex);

			// Treat empty strains like nulls
			if (o1 instanceof String && ((String) o1).length() == 0) {
				o1 = null;
			}
			if (o2 instanceof String && ((String) o2).length() == 0) {
				o2 = null;
			}

			// Sort nulls so they appear last, regardless
			// of sort order
			if (o1 == null && o2 == null) {
				result = 0;
			} else if (o1 == null) {
				result = 1;
			} else if (o2 == null) {
				result = -1;
			} else if (o1 instanceof Comparable) {
				if (this.ascending) {
					result = ((Comparable) o1).compareTo(o2);
				} else
					result = ((Comparable) o2).compareTo(o1);

			} else {
				if (this.ascending) {
					result = o1.toString().compareTo(o2.toString());
				} else
					result = o2.toString().compareTo(o1.toString());

			}
			return result;
		}
	}


		private int				columnCount;

		private boolean[]		sortOrders;

		private List	testLines;
		
		public ObjectResourceTableModel(int columnCount){
			this.columnCount = columnCount;
			this.sortOrders = new boolean[this.columnCount];
		}

		public void addRow(ObjectResourceTableRow value) {
			if (this.testLines == null)
				this.testLines = new ArrayList();
			this.testLines.add(value);
		}

		public abstract Class getColumnClass(int columnIndex);

		public int getColumnCount() {
			return this.columnCount;
		}

		public abstract String getColumnName(int columnIndex);

		public Object getRow(int rowIndex) {
			Object result = null;
			if (this.testLines == null)
				result = null;
			else if (!this.testLines.isEmpty())
				result = this.testLines.get(rowIndex);
			return result;
		}

		public int getRowCount() {
			int count = 0;
			if (this.testLines != null)
				count = this.testLines.size();
			return count;
		}

		public java.util.List getRowData(int rowIndex) {
			ObjectResourceTableRow line = (ObjectResourceTableRow) getRow(rowIndex);
			return (line == null) ? null : line.getData();
		}

		public boolean getSortOrder(int columnIndex) {
			return this.sortOrders[columnIndex];
		}

		public int getObjectResourceIndex(ObjectResource objectResource) {
			int rowIndex = -1;
			if (this.testLines != null) {
				for (int i = 0; i < this.testLines.size(); i++) {
					ObjectResourceTableRow row = (ObjectResourceTableRow) this.testLines.get(i);
					if (row.getObjectResource().equals(objectResource)) {
						rowIndex = i;
						break;
					}
				}
			}
			return rowIndex;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			Object obj = null;
			if (this.testLines != null) {
				ObjectResourceTableRow line = (ObjectResourceTableRow) this.testLines.get(rowIndex);
				java.util.List data = line.getData();
				if (columnIndex < data.size())
					obj = data.get(columnIndex);
			}
			return obj;
		}

		public void remove(int rowIndex) {
			this.testLines.remove(rowIndex);
		}

		public void removeAll() {
			if (this.testLines != null)
				this.testLines.clear();

		}

		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			if (this.testLines == null)
				this.testLines = new ArrayList();
			ObjectResourceTableRow line;
			if (this.testLines.size() >= rowIndex) {
				line = (ObjectResourceTableRow) this.testLines.get(rowIndex);
				line.setValue(value, columnIndex);
			}
		}

		public void sortRows(int columnIndex) {
			sortRows(columnIndex, this.sortOrders[columnIndex]);
			this.sortOrders[columnIndex] = !this.sortOrders[columnIndex];
		}

		public void sortRows(int columnIndex, boolean ascending) {
			if (this.testLines != null)
				Collections.sort(this.testLines, new ColumnSorter(columnIndex, ascending));
		}

	
}
