package com.syrus.AMFICOM.client_.general.ui_;

import com.syrus.AMFICOM.Client.General.UI.ATable;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * @version $Revision: 1.1 $, $Date: 2004/10/05 13:19:42 $
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
	}

	private void initialization() 
	{
		updateModel();
		this.setColumnSelectionAllowed(false);
		this.setRowSelectionAllowed(true);
	}

}