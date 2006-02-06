/*-
* $Id: WrapperedPropertyTable.java,v 1.18 2005/12/16 10:57:38 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/
package com.syrus.AMFICOM.client.UI;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.18 $, $Date: 2005/12/16 10:57:38 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class WrapperedPropertyTable<T> extends ATable {

	private TableCellEditor		defaultTextFieldEditor;
	protected TableCellRenderer[][]	cellRenderers;
	protected TableCellEditor[][]	cellEditors;
	private FocusListener	editingStopFocusListener;
	
	private static final long	serialVersionUID	= -437251205606073016L;

	public WrapperedPropertyTable(final Wrapper<T> controller, 
	                              final T object, 
	                              final String[] keys) {
		this(new WrapperedPropertyTableModel<T>(controller, object, keys));
	}

	public WrapperedPropertyTable(final WrapperedPropertyTableModel<T> dm) {
		super(dm);
		this.initialization();
	}

	@Override
	public WrapperedPropertyTableModel<T> getModel() {
		return (WrapperedPropertyTableModel<T>) super.getModel();
	}

	public void setDefaultTableCellRenderer() {
		this.setDefaultTableCellRenderer(StubLabelCellRenderer.getInstance());
	}
	
	public void setDefaultTableCellRenderer(final TableCellRenderer renderer) {
		final WrapperedPropertyTableModel<T> model = this.getModel();

		TableColumn col = this.getColumnModel().getColumn(0);
		col.setCellRenderer(renderer);
		for (int mColIndex = 1; mColIndex < model.getColumnCount(); mColIndex++) {
			col = this.getColumnModel().getColumn(mColIndex);
			col.setCellRenderer(renderer);
		}
	}
	
	@Override
	public String getToolTipText(final MouseEvent e) {
    	final Point p = e.getPoint();
    	final int rowIndex = this.rowAtPoint(p);
        
        return this.getModel().getValueAt(rowIndex, 0).toString();
    }

	/**
	 * set custom renderer
	 * 
	 * @param renderer
	 * @param key
	 *                see {@link com.syrus.util.Wrapper#getKeys()}
	 */
	public void setRenderer(final TableCellRenderer renderer, final String key) {
		this.cellRenderers[this.getModel().getRowIndex(key)][1] = renderer;
	}

	public void updateModel() {
		final WrapperedPropertyTableModel<T> model = this.getModel();
		for (int mRowIndex = 0; mRowIndex < model.getRowCount(); mRowIndex++) {
			final Object obj = model.wrapper.getPropertyValue(model.keys[mRowIndex]);
			if (obj instanceof Map) {
				final Map map = (Map) obj;
				final AComboBox comboBox = new AComboBox();
				List keys = new ArrayList(map.keySet());
				Collections.sort(keys);
				comboBox.setRenderer(LabelCheckBoxRenderer.getInstance());
				for (final Iterator it = keys.iterator(); it.hasNext();) {
					comboBox.addItem(it.next());
				}
				keys.clear();
				keys = null;
				final TableColumn sportColumn = getColumnModel().getColumn(1);
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
				
				comboBox.addFocusListener(this.getEditingStopFocusListener());
				
				this.cellEditors[mRowIndex][1] = new DefaultCellEditor(comboBox);
			} else {
				final Class clazz = model.wrapper.getPropertyClass(model.keys[mRowIndex]);
				if (clazz.equals(Boolean.class)) {
					final JCheckBox checkBox = new JCheckBox();
					checkBox.setHorizontalAlignment(SwingConstants.CENTER);
					this.cellEditors[mRowIndex][1] = new DefaultCellEditor(checkBox);
				}
			}
		}
	}

	private void initialization() {
		this.cellEditors = 
			new TableCellEditor[this.getModel().getRowCount()][this.getModel().getColumnCount()];
		this.cellRenderers = 
			new TableCellRenderer[this.getModel().getRowCount()][this.getModel().getColumnCount()];
		this.updateModel();
		this.setColumnSelectionAllowed(false);
		this.setRowSelectionAllowed(true);		
	}

	private FocusListener getEditingStopFocusListener() {
		if (this.editingStopFocusListener == null) {
			this.editingStopFocusListener = new FocusAdapter() {
				@Override
				public void focusLost(final FocusEvent e) {
					WrapperedPropertyTable.this.editingCanceled(null);
				}
			};
		}
		return this.editingStopFocusListener;
	}
	
	@Override
	public void tableChanged(TableModelEvent e) {		
		super.tableChanged(e);
		if (this.cellRenderers != null && 
				(this.getModel().getRowCount() > this.cellRenderers.length
					|| (this.cellRenderers.length > 0 && this.getModel().getColumnCount() > this.cellRenderers[0].length))) {
			this.cellEditors = 
				new TableCellEditor[this.getModel().getRowCount()][this.getModel().getColumnCount()];
			this.cellRenderers = 
				new TableCellRenderer[this.getModel().getRowCount()][this.getModel().getColumnCount()];
			this.updateModel();
		}
	}
	
	@Override
	public TableCellRenderer getCellRenderer(	int row,
												int column) {		
		TableCellRenderer tableCellRenderer = this.cellRenderers[row][column];
		if (tableCellRenderer == null) {
			return super.getCellRenderer(row, column);
		}
		return tableCellRenderer;
	}
	
	@Override
	public TableCellEditor getCellEditor(final int row, final int column) {
		TableCellEditor tableCellEditor = this.cellEditors[row][column];
		if (tableCellEditor == null) {
			if (this.defaultTextFieldEditor == null) {
				final JTextField textField = new JTextField();
				textField.addFocusListener(this.getEditingStopFocusListener());
				this.defaultTextFieldEditor = new DefaultCellEditor(textField);
			}
			tableCellEditor = this.defaultTextFieldEditor;
		}
		return tableCellEditor;
	}
	
	@SuppressWarnings("unused")
	private void setCellEditor(final TableCellEditor tableCellEditor,
	                          final int row, 
	                          final int column) {
		this.cellEditors[row][column] = tableCellEditor;
	}
}
