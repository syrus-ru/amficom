
package com.syrus.AMFICOM.client.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/06 14:52:47 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class WrapperedPropertyTable extends ATable {

	private TableCellEditor		defaultTextFieldEditor;
	protected TableCellEditor[][]	cellEditors;
	
	private static final long	serialVersionUID	= -437251205606073016L;

	public WrapperedPropertyTable(final Wrapper controller, final Object object, final String[] keys) {
		this(new WrapperedPropertyTableModel(controller, object, keys));
	}

	public WrapperedPropertyTable(final WrapperedPropertyTableModel dm) {
		super(dm);
		initialization();
	}

	public void setDefaultTableCellRenderer() {
		WrapperedPropertyTableModel model = (WrapperedPropertyTableModel) getModel();

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
	public void setRenderer(TableCellRenderer renderer, String key) {
		WrapperedPropertyTableModel model = (WrapperedPropertyTableModel) getModel();
		for (int mRowIndex = 0; mRowIndex < model.getRowCount(); mRowIndex++) {
			if (model.keys[mRowIndex].equals(key)) {
				TableColumn col = this.getColumnModel().getColumn(mRowIndex);
				col.setCellRenderer(renderer);
			}
		}
	}

	private void updateModel() {
		WrapperedPropertyTableModel model = (WrapperedPropertyTableModel) getModel();
		for (int mRowIndex = 1; mRowIndex < model.getRowCount(); mRowIndex++) {
			Object obj = model.wrapper.getPropertyValue(model.keys[mRowIndex]);
			if (obj instanceof Map) {
				final Map map = (Map) obj;
				AComboBox comboBox = new AComboBox();
				List keys = new ArrayList(map.keySet());
				Collections.sort(keys);
				comboBox.setRenderer(LabelCheckBoxRenderer.getInstance());
				for (Iterator it = keys.iterator(); it.hasNext();) {
					comboBox.addItem(it.next());
				}
				keys.clear();
				keys = null;
				TableColumn sportColumn = getColumnModel().getColumn(1);
				sportColumn.setCellEditor(new DefaultCellEditor(comboBox));

				comboBox.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						AComboBox cb = (AComboBox) e.getSource();
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
				this.cellEditors[mRowIndex][1] = new DefaultCellEditor(comboBox);
			} else {
				Class clazz = model.wrapper.getPropertyClass(model.keys[mRowIndex]);
				if (clazz.equals(Boolean.class)) {
					JCheckBox checkBox = new JCheckBox();
					this.cellEditors[mRowIndex][1] = new DefaultCellEditor(checkBox);
				}
			}
		}
	}

	private void initialization() {
		this.cellEditors = new TableCellEditor[this.getModel().getRowCount()][this.getModel().getColumnCount()];
		updateModel();
		this.setColumnSelectionAllowed(false);
		this.setRowSelectionAllowed(true);
	}

	public TableCellEditor getCellEditor(int row, int column) {
		TableCellEditor tableCellEditor = this.cellEditors[row][column];
		if (tableCellEditor == null) {
			if (column == 1) {
				WrapperedPropertyTableModel model = (WrapperedPropertyTableModel) getModel();
				int mRowIndex = row;
				Object obj = model.wrapper.getPropertyValue(model.keys[mRowIndex]);
				if (obj instanceof Map) {
					final Map map = (Map) obj;
					AComboBox comboBox = new AComboBox();
					List keys = new ArrayList(map.keySet());
					Collections.sort(keys);
					comboBox.setRenderer(LabelCheckBoxRenderer.getInstance());
					for (Iterator it = keys.iterator(); it.hasNext();) {
						comboBox.addItem(it.next());
					}
					keys.clear();
					keys = null;
					TableColumn sportColumn = getColumnModel().getColumn(1);
					sportColumn.setCellEditor(new DefaultCellEditor(comboBox));

					comboBox.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							AComboBox cb = (AComboBox) e.getSource();
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
					this.cellEditors[mRowIndex][column] = new DefaultCellEditor(comboBox);
					tableCellEditor = this.cellEditors[mRowIndex][column];
				} else {
					Class clazz = model.wrapper.getPropertyClass(model.keys[mRowIndex]);
					if (clazz.equals(Boolean.class)) {
						JCheckBox checkBox = new JCheckBox();
						this.cellEditors[mRowIndex][1] = new DefaultCellEditor(checkBox);
						tableCellEditor = this.cellEditors[mRowIndex][column];
					}
				}

			}
		}
		if (tableCellEditor == null) {
			if (this.defaultTextFieldEditor == null) {
				this.defaultTextFieldEditor = new DefaultCellEditor(new JTextField());
			}
			tableCellEditor = this.defaultTextFieldEditor;
		}
		return tableCellEditor;
	}

}
