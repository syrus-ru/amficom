
package com.syrus.AMFICOM.client.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.9 $, $Date: 2005/09/07 02:37:31 $
 * @author $Author: arseniy $
 * @module commonclient
 */
public class WrapperedPropertyTable<T> extends ATable {

	private TableCellEditor		defaultTextFieldEditor;
	protected TableCellEditor[][]	cellEditors;
	private FocusListener	editingStopFocusListener;
	
	private static final long	serialVersionUID	= -437251205606073016L;

	public WrapperedPropertyTable(final Wrapper<T> controller, final T object, final String[] keys) {
		this(new WrapperedPropertyTableModel<T>(controller, object, keys));
	}

	public WrapperedPropertyTable(final WrapperedPropertyTableModel dm) {
		super(dm);
		this.initialization();
	}

	@Override
	public WrapperedPropertyTableModel<T> getModel() {
		return (WrapperedPropertyTableModel<T>) super.getModel();
	}

	public void setDefaultTableCellRenderer() {
		final WrapperedPropertyTableModel<T> model = this.getModel();

		TableColumn col = this.getColumnModel().getColumn(0);
		TableCellRenderer renderer = StubLabelCellRenderer.getInstance();
		col.setCellRenderer(renderer);

		for (int mColIndex = 1; mColIndex < model.getColumnCount(); mColIndex++) {
			renderer = StubLabelCellRenderer.getInstance();
			col = this.getColumnModel().getColumn(mColIndex);
			col.setCellRenderer(renderer);
		}
	}

	/**
	 * set custom renderer
	 * 
	 * @param renderer
	 * @param key
	 *                see {@link com.syrus.util.Wrapper#getKeys()}
	 */
	public void setRenderer(final TableCellRenderer renderer, final String key) {
		final WrapperedPropertyTableModel<T> model = this.getModel();
		for (int mRowIndex = 0; mRowIndex < model.getRowCount(); mRowIndex++) {
			if (model.keys[mRowIndex].equals(key)) {
				final TableColumn col = this.getColumnModel().getColumn(mRowIndex);
				col.setCellRenderer(renderer);
			}
		}
	}

	public void updateModel() {
		final WrapperedPropertyTableModel<T> model = this.getModel();
		for (int mRowIndex = 1; mRowIndex < model.getRowCount(); mRowIndex++) {
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
					JCheckBox checkBox = new JCheckBox();
					this.cellEditors[mRowIndex][1] = new DefaultCellEditor(checkBox);
				}
			}
		}
	}

	private void initialization() {
		this.cellEditors = new TableCellEditor[this.getModel().getRowCount()][this.getModel().getColumnCount()];
		this.updateModel();
		this.setColumnSelectionAllowed(false);
		this.setRowSelectionAllowed(true);		
	}

	private FocusListener getEditingStopFocusListener() {
		if (this.editingStopFocusListener == null) {
			this.editingStopFocusListener = new FocusAdapter() {
				@Override
				public void focusLost(final FocusEvent e) {
					WrapperedPropertyTable.this.editingStopped(null);
				}
			};
		}
		return this.editingStopFocusListener;
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

}
