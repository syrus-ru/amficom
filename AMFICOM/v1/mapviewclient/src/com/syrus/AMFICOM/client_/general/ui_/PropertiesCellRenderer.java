package com.syrus.AMFICOM.client_.general.ui_;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class PropertiesCellRenderer extends DefaultTableCellRenderer 
{

	private static PropertiesCellRenderer instance;

	private PropertiesCellRenderer() 
	{
		// empty
	}

	public static PropertiesCellRenderer getInstance() 
	{
		if (instance == null)
			instance = new PropertiesCellRenderer();
		return instance;
	}
	
	protected void customRendering(	JTable table,
					ObjectResource objectResource,
					ObjectResourcePropertiesController objectResourceController,
					String key) 
	{
		
	}

	public Component getTableCellRendererComponent(
			JTable table,
            Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column)
	{
		Component renderer;

		column = table.convertColumnIndexToModel(column);
		ObjectResourcePropertiesTableModel model = (ObjectResourcePropertiesTableModel )table.getModel();
		if(column == 0)
		{
			String text = model.getController().getName((String )value);
			renderer = super.getTableCellRendererComponent(
				table, text, isSelected, hasFocus, row, column);
		}
		else
		{
			Object propVal = model.getController().getPropertyValue(model.getController().getKey(row));
			
			renderer = super.getTableCellRendererComponent(
				table, value, isSelected, hasFocus, row, column);
		}
		return renderer;
	}
}