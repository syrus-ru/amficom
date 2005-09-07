package com.syrus.AMFICOM.client.UI;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.10 $, $Date: 2005/09/07 02:37:31 $
 * @author $Author: arseniy $
 * @module commonclient
 */
public class WrapperedTable<T> extends ATable {

	private static final long	serialVersionUID	= -437251205606073016L;
	boolean allowSorting = true;

	public WrapperedTable(final Wrapper<T> controller, final List<T> objectResourceList, final String[] keys) {
		this(new WrapperedTableModel<T>(controller, objectResourceList, keys));
	}
	
	public WrapperedTable(final Wrapper<T> controller, final String[] keys) {
		this(new WrapperedTableModel<T>(controller, keys));
	}

	public WrapperedTable(final WrapperedTableModel<T> dm) {
		super(dm);
		this.initialization();
	}

	@Override
	public WrapperedTableModel<T> getModel() {
		return (WrapperedTableModel<T>) super.getModel();
	}

	public void setDefaultTableCellRenderer() {
		final WrapperedTableModel<T> model = this.getModel();
		for (int mColIndex = 0; mColIndex < model.getColumnCount(); mColIndex++) {
			TableCellRenderer renderer = StubLabelCellRenderer.getInstance();
			final TableColumn col = this.getColumnModel().getColumn(mColIndex);
			final Class clazz = model.wrapper.getPropertyClass(model.keys[mColIndex]);
			if (clazz.equals(Boolean.class)) {
				renderer = null;
			}
			else if (clazz.equals(Color.class)) {
				renderer = ColorCellRenderer.getInstance();
			}

			col.setCellRenderer(renderer);
		}
	}
	
	public void setEditor(final TableCellEditor editor, final String key) {
		final WrapperedTableModel<T> model = this.getModel();
		for (int mColIndex = 0; mColIndex < model.getColumnCount(); mColIndex++) {
			if (model.keys[mColIndex].equals(key)) {
				final TableColumn col = this.getColumnModel().getColumn(mColIndex);
				col.setCellEditor(editor);
			}
		}
	}

	/**
	 * set custom renderer
	 * @param renderer
	 * @param key see {@link Wrapper#getKeys()}
	 */
	public void setRenderer(final TableCellRenderer renderer, final String key) {
		final WrapperedTableModel<T> model = this.getModel();
		for (int mColIndex = 0; mColIndex < model.getColumnCount(); mColIndex++) {
			if (model.keys[mColIndex].equals(key)) {
				final TableColumn col = this.getColumnModel().getColumn(mColIndex);
				col.setCellRenderer(renderer);
			}
		}
	}
	
	public void setAllowSorting(final boolean allowSorting) {
		this.allowSorting = allowSorting;
	}
	
	public boolean isAllowSorting() {
		return this.allowSorting;
	}

	private void updateModel() {
		final WrapperedTableModel<T> model = this.getModel();
		for (int mColIndex = 0; mColIndex < model.getColumnCount(); mColIndex++) {
			final Object obj = model.wrapper.getPropertyValue(model.keys[mColIndex]);
			if (obj instanceof Map) {
				final Map<?, ?> map = (Map) obj;
				final AComboBox comboBox = new AComboBox();
				List keys = new ArrayList(map.keySet());
				Collections.sort(keys);
				comboBox.setRenderer(LabelCheckBoxRenderer.getInstance());
				for (Iterator it = keys.iterator(); it.hasNext();) {
					comboBox.addItem(it.next());
				}
				keys.clear();
				keys = null;
				final TableColumn sportColumn = getColumnModel().getColumn(mColIndex);
				sportColumn.setCellEditor(new DefaultCellEditor(comboBox));
				comboBox.addActionListener(new ActionListener() {

					public void actionPerformed(final ActionEvent e) {
						final AComboBox cb = (AComboBox) e.getSource();
						if (cb.getItemCount() != map.keySet().size()) {
							cb.removeAllItems();
							List keys1 = new ArrayList(map.keySet());
							Collections.sort(keys1);
							for (Iterator it = keys1.iterator(); it.hasNext();) {
								cb.addItem(it.next());
							}
							keys1.clear();
							keys1 = null;
						}

					}
				});

			}
		}
	}
	
	public void sortColumn(int mColIndex) {
		WrapperedTableModel<T> model = this.getModel();
		String s;
		if (model.getSortOrder(mColIndex)) {
			s = " ^ "; //$NON-NLS-1$
		} else {
			s = " v "; //$NON-NLS-1$
		}
		final String columnName = model.getColumnName(mColIndex);
		this.getColumnModel().getColumn(this.convertColumnIndexToView(mColIndex)).setHeaderValue(s
				+ (columnName == null ? "" : columnName)
				+ s);

		for (int i = 0; i < model.getColumnCount(); i++) {
			if (i != mColIndex) {
				this.getColumnModel().getColumn(this.convertColumnIndexToView(i)).setHeaderValue(model.getColumnName(i));
			}
		}

		// Force the header to resize and repaint itself
		this.getTableHeader().resizeAndRepaint();
		model.sortRows(mColIndex);
	}
	
	private void initialization() {
		this.updateModel();
		this.setColumnSelectionAllowed(false);
		this.setRowSelectionAllowed(true);

		this.getTableHeader().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent evt) {
				if (!WrapperedTable.this.allowSorting) {
					return;
				}
				
				final JTableHeader header = (JTableHeader) evt.getSource();
				final JTable table = header.getTable();
				final TableColumnModel colModel = table.getColumnModel();

				// The index of the column whose header was
				// clicked
				final int columnIndex = colModel.getColumnIndexAtX(evt.getX());
				final int mColIndex = table.convertColumnIndexToModel(columnIndex);

				WrapperedTable.this.sortColumn(mColIndex);

				// Return if not clicked on any column header
				if (columnIndex == -1) {
					return;
				}

				// Determine if mouse was clicked between column
				// heads
				final Rectangle headerRect = table.getTableHeader().getHeaderRect(columnIndex);
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

}
