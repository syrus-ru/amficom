
package com.syrus.AMFICOM.client_.general.ui_;

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

import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @version $Revision: 1.2 $, $Date: 2004/10/07 12:20:22 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class ObjPropertyTable extends JTable {

	private TableCellEditor		defaultTextFieldEditor;
	protected TableCellEditor[][]	cellEditors;
	private static final long	serialVersionUID	= -437251205606073016L;

	public ObjPropertyTable(ObjectResourceController controller, Object object) {
		this(new ObjPropertyTableModel(controller, object));
	}

	public ObjPropertyTable(ObjPropertyTableModel dm) {
		super(dm);
		initialization();
	}

	public void setDefaultTableCellRenderer() {
		ObjPropertyTableModel model = (ObjPropertyTableModel) getModel();

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
	 *                see {@link ObjectResourceController#getKeys()}
	 */
	public void setRenderer(TableCellRenderer renderer, String key) {
		ObjPropertyTableModel model = (ObjPropertyTableModel) getModel();
		for (int mRowIndex = 0; mRowIndex < model.getRowCount(); mRowIndex++) {
			if (model.controller.getKey(mRowIndex).equals(key)) {
				TableColumn col = this.getColumnModel().getColumn(mRowIndex);
				col.setCellRenderer(renderer);
			}
		}
	}

	private void updateModel() {
		ObjPropertyTableModel model = (ObjPropertyTableModel) getModel();
		for (int mRowIndex = 1; mRowIndex < model.getRowCount(); mRowIndex++) {
			Object obj = model.controller.getPropertyValue(model.controller.getKey(mRowIndex));
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
							List keys = new ArrayList(map.keySet());
							Collections.sort(keys);
							for (Iterator it = keys.iterator(); it.hasNext();) {
								cb.addItem(it.next());
							}
							keys.clear();
							keys = null;
						}

					}
				});
				this.cellEditors[mRowIndex][1] = new DefaultCellEditor(comboBox);
			} else {
				Class clazz = model.controller.getPropertyClass(model.controller.getKey(mRowIndex));
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

		//		this.getTableHeader().addMouseListener(new MouseAdapter() {
		//
		//			public void mouseClicked(MouseEvent evt) {
		//				JTableHeader header = (JTableHeader) evt.getSource();
		//				JTable table = header.getTable();
		//				TableColumnModel colModel = table.getColumnModel();
		//
		//				// The index of the column whose header was
		//				// clicked
		//				int columnIndex = colModel.getColumnIndexAtX(evt.getX());
		//				int mColIndex = table.convertColumnIndexToModel(columnIndex);
		//				ObjPropertyTableModel model = (ObjPropertyTableModel)
		// table.getModel();
		//				String s;
		//				if (model.getSortOrder(mColIndex))
		//					s = " v "; //$NON-NLS-1$
		//				else
		//					s = " ^ "; //$NON-NLS-1$
		//				String columnName = model.getColumnName(mColIndex);
		//				table.getColumnModel().getColumn(columnIndex)
		//						.setHeaderValue(s + (columnName == null ? "" : columnName) +
		// s);
		//
		//				for (int i = 0; i < model.getColumnCount(); i++) {
		//					if (i != mColIndex)
		//						table.getColumnModel().getColumn(table.convertColumnIndexToView(i))
		//								.setHeaderValue(model.getColumnName(i));
		//				}
		//
		//				// Force the header to resize and repaint itself
		//				header.resizeAndRepaint();
		//				model.sortRows(mColIndex);
		//
		//				// Return if not clicked on any column header
		//				if (columnIndex == -1) { return; }
		//
		//				// Determine if mouse was clicked between column
		//				// heads
		//				Rectangle headerRect =
		// table.getTableHeader().getHeaderRect(columnIndex);
		//				if (columnIndex == 0) {
		//					headerRect.width -= 3; // Hard-coded
		//					// constant
		//				} else {
		//					headerRect.grow(-3, 0); // Hard-coded
		//					// constant
		//				}
		//				if (!headerRect.contains(evt.getX(), evt.getY())) {
		//					// Mouse was clicked between column
		//					// heads
		//					// vColIndex is the column head closest
		//					// to the click
		//
		//					// vLeftColIndex is the column head to
		//					// the left of the
		//					// click
		//					int vLeftColIndex = columnIndex;
		//					if (evt.getX() < headerRect.x) {
		//						vLeftColIndex--;
		//					}
		//				}
		//			}
		//		});

	}

	public TableCellEditor getCellEditor(int row, int column) {
		TableCellEditor tableCellEditor = this.cellEditors[row][column];
		if (tableCellEditor == null) {
			if (column == 1) {
				ObjPropertyTableModel model = (ObjPropertyTableModel) getModel();
				int mRowIndex = row;
				Object obj = model.controller.getPropertyValue(model.controller.getKey(mRowIndex));
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
								List keys = new ArrayList(map.keySet());
								Collections.sort(keys);
								for (Iterator it = keys.iterator(); it.hasNext();) {
									cb.addItem(it.next());
								}
								keys.clear();
								keys = null;
							}

						}
					});
					this.cellEditors[mRowIndex][column] = new DefaultCellEditor(comboBox);
					tableCellEditor = this.cellEditors[mRowIndex][column];
				} else {
					Class clazz = model.controller.getPropertyClass(model.controller
							.getKey(mRowIndex));
					if (clazz.equals(Boolean.class)) {
						JCheckBox checkBox = new JCheckBox();
						this.cellEditors[mRowIndex][1] = new DefaultCellEditor(checkBox);
						tableCellEditor = this.cellEditors[mRowIndex][column];
					}
				}

			}
		}
		if (tableCellEditor == null) {
			if (this.defaultTextFieldEditor == null)
				this.defaultTextFieldEditor = new DefaultCellEditor(new JTextField());
			tableCellEditor = this.defaultTextFieldEditor;
		}
		return tableCellEditor;
	}

}