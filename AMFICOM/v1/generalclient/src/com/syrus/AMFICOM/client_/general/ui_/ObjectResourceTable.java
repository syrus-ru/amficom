package com.syrus.AMFICOM.client_.general.ui_;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @version $Revision: 1.4 $, $Date: 2004/11/03 07:23:50 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class ObjectResourceTable extends JTable {

	private static final long	serialVersionUID	= -437251205606073016L;

	public ObjectResourceTable(ObjectResourceController controller, List objectResourceList) {
		this(new ObjectResourceTableModel(controller, objectResourceList));
	}
	
	public ObjectResourceTable(ObjectResourceController controller) {
		this(new ObjectResourceTableModel(controller, new LinkedList()));
	}

	public ObjectResourceTable(ObjectResourceTableModel dm) {
		super(dm);
		initialization();
	}

	public void setDefaultTableCellRenderer() {
		ObjectResourceTableModel model = (ObjectResourceTableModel) getModel();

		for (int mColIndex = 0; mColIndex < model.getColumnCount(); mColIndex++) {
			TableCellRenderer renderer = StubLabelCellRenderer.getInstance();
			TableColumn col = this.getColumnModel().getColumn(mColIndex);
			Class clazz = model.controller.getPropertyClass(model.controller.getKey(mColIndex));
			if (clazz.equals(Boolean.class))
				renderer = null;
			else if (clazz.equals(Color.class)) {
				renderer = ColorCellRenderer.getInstance();
			}

			col.setCellRenderer(renderer);
		}
	}
	
	public void setEditor(TableCellEditor editor, String key) {
		ObjectResourceTableModel model = (ObjectResourceTableModel) getModel();
		for (int mColIndex = 0; mColIndex < model.getColumnCount(); mColIndex++) {
			if (model.controller.getKey(mColIndex).equals(key)) {
				TableColumn col = this.getColumnModel().getColumn(mColIndex);
				col.setCellEditor(editor);
			}
		}
	}

	/**
	 * set custom renderer
	 * @param renderer
	 * @param key see {@link ObjectResourceController#getKeys()}
	 */
	public void setRenderer(TableCellRenderer renderer, String key) {
		ObjectResourceTableModel model = (ObjectResourceTableModel) getModel();
		for (int mColIndex = 0; mColIndex < model.getColumnCount(); mColIndex++) {
			if (model.controller.getKey(mColIndex).equals(key)) {
				TableColumn col = this.getColumnModel().getColumn(mColIndex);
				col.setCellRenderer(renderer);
			}
		}
	}

	private void updateModel() {
		ObjectResourceTableModel model = (ObjectResourceTableModel) getModel();
		for (int mColIndex = 0; mColIndex < model.getColumnCount(); mColIndex++) {
			Object obj = model.controller.getPropertyValue(model.controller.getKey(mColIndex));
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
				TableColumn sportColumn = getColumnModel().getColumn(mColIndex);
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

			}
		}
	}

	private void initialization() {
		updateModel();
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
				ObjectResourceTableModel model = (ObjectResourceTableModel) table.getModel();
				String s;
				if (model.getSortOrder(mColIndex))
					s = " v "; //$NON-NLS-1$
				else
					s = " ^ "; //$NON-NLS-1$
				String columnName = model.getColumnName(mColIndex);
				table.getColumnModel().getColumn(columnIndex)
						.setHeaderValue(s + (columnName == null ? "" : columnName) + s);

				for (int i = 0; i < model.getColumnCount(); i++) {
					if (i != mColIndex)
						table.getColumnModel().getColumn(table.convertColumnIndexToView(i))
								.setHeaderValue(model.getColumnName(i));
				}

				// Force the header to resize and repaint itself
				header.resizeAndRepaint();
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

}