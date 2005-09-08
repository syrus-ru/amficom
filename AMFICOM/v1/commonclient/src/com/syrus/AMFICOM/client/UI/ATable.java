
package com.syrus.AMFICOM.client.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIDefaults;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class ATable extends JTable {
	
	private static final long	serialVersionUID	= -7206079277893564010L;
	
	private int	height			= 4;
	private int	initialHeight	= -1;

	public ATable() {
		super();
		updateHeaderSize();
	}

	public ATable(Vector rowData, Vector columnNames) {
		super(rowData, columnNames);
		updateHeaderSize();
	}

	public ATable(Object[][] rowData, Object[] columnNames) {
		super(rowData, columnNames);
		updateHeaderSize();
	}

	public ATable(TableModel tm) {
		super(tm);
		updateHeaderSize();
	}

	public ATable(TableModel tm, TableColumnModel cm) {
		super(tm, cm);
		updateHeaderSize();
	}

	public ATable(TableModel tm, TableColumnModel cm, ListSelectionModel sm) {
		super(tm, cm, sm);
		updateHeaderSize();
	}

	public ATable(int numRows, int numColumns) {
		super(numRows, numColumns);
		this.updateHeaderSize();
	}

	@Override
	public void setModel(TableModel dataModel) {
		super.setModel(dataModel);
		this.updateHeaderSize();
	}

	@Override
	public void setColumnModel(TableColumnModel columnModel) {
		super.setColumnModel(columnModel);
		this.updateHeaderSize();
	}

	public void setHeaderHeight(int height) {
		this.height = height;
	}

	public int getHeaderHeight() {
		return this.height;
	}

	public void updateHeaderSize() {
		JTableHeader jTableHeader = getTableHeader();
		if (jTableHeader == null)
			return;

		if (this.initialHeight == -1) {
			this.initialHeight = jTableHeader.getPreferredSize().height;
		}

		boolean hasNonEmptyTitle = false;
		for (int i = 0; i < getColumnCount(); i++) {
			if (getColumnName(i) != null && getColumnName(i).length() != 0) {
				hasNonEmptyTitle = true;
				break;
			}
		}

		if (!hasNonEmptyTitle)
			jTableHeader.setPreferredSize(new Dimension(jTableHeader.getPreferredSize().width, this.height));
		else
			jTableHeader.setPreferredSize(new Dimension(jTableHeader.getPreferredSize().width, this.initialHeight));
	}

	/**
	 * Creates default cell renderers for objects, numbers, doubles, dates,
	 * booleans, and icons.
	 * 
	 * @see javax.swing.table.DefaultTableCellRenderer
	 * 
	 */
	@Override
	protected void createDefaultRenderers() {
		super.defaultRenderersByColumnClass = new UIDefaults();

		// Objects
		setLazyRenderer(Object.class, ADefaultTableCellRenderer.class.getName(), "getInstance");

		// Numbers
		setLazyRenderer(Number.class, ADefaultTableCellRenderer.class.getName(), "getInstance");

		// Doubles and Floats
		setLazyRenderer(Float.class, ADefaultTableCellRenderer.class.getName(), "getInstance");
		setLazyRenderer(Double.class, ADefaultTableCellRenderer.class.getName(), "getInstance");

		// Dates
		setLazyRenderer(Date.class, ADefaultTableCellRenderer.class.getName(), "getInstance");

		// Icons and ImageIcons
		setLazyRenderer(ImageIcon.class, ADefaultTableCellRenderer.class.getName(), "getInstance");
		setLazyRenderer(Icon.class, ADefaultTableCellRenderer.class.getName(), "getInstance");

		setLazyRenderer(Color.class, ADefaultTableCellRenderer.class.getName(), "getInstance");

		// Booleans
		setLazyRenderer(Boolean.class, ADefaultTableCellRenderer.class.getName(), "getInstance");

	}

	private void setLazyRenderer(	Class c,
									String s,
									String m) {
		setLazyValue(this.defaultRenderersByColumnClass, c, s, m);
	}

	@SuppressWarnings("unchecked")
	private void setLazyValue(	Hashtable h,
								Class c,
								String s,
								String m) {
		h.put(c, new UIDefaults.ProxyLazyValue(s, m));
	}
}
