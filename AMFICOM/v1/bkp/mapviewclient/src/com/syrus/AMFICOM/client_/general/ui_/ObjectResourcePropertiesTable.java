package com.syrus.AMFICOM.client_.general.ui_;

import com.syrus.AMFICOM.Client.General.UI.ATable;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
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

import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @version $Revision: 1.2 $, $Date: 2004/09/29 16:13:46 $
 * @author $Author: krupenn $
 * @module generalclient_v1
 */
public class ObjectResourcePropertiesTable extends ATable 
{

	public ObjectResourcePropertiesTable(ObjectResourcePropertiesController controller, ObjectResource objectResource) 
	{
		this(new ObjectResourcePropertiesTableModel(controller, objectResource));
	}

	public ObjectResourcePropertiesTable(ObjectResourcePropertiesTableModel dm) 
	{
		super(dm);
		initialization();
	}

	private void updateModel() 
	{
		ObjectResourcePropertiesTableModel model = (ObjectResourcePropertiesTableModel) getModel();

		TableColumn column = getColumnModel().getColumn(1);
		TableCellRenderer renderer = PropertiesCellRenderer.getInstance();
		column.setCellRenderer(renderer);
//		TableCellEditor editor = PropertiesCellEditor.getInstance();
//		column.setCellEditor(editor);
/*
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
*/
	}

	private void initialization() 
	{
		updateModel();
		this.setColumnSelectionAllowed(false);
		this.setRowSelectionAllowed(true);
	}

}