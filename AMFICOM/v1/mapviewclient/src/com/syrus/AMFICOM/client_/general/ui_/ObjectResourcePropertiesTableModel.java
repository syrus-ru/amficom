package com.syrus.AMFICOM.client_.general.ui_;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.util.Iterator;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class ObjectResourcePropertiesTableModel extends AbstractTableModel
{
	public static final String KEY_PROPERTY = "Property";
	public static final String KEY_VALUE = "Value";

	protected ObjectResourcePropertiesController controller;	
	
	private ObjectResource objectResource;

	public ObjectResourcePropertiesTableModel(
			ObjectResourcePropertiesController controller) 
	{
		this(controller, null);
	}

	public ObjectResourcePropertiesTableModel(
			ObjectResourcePropertiesController controller, 
			ObjectResource objectResource) 
	{
		this.controller = controller;
		setObjectResource(objectResource);
	}

	public void clear() 
	{
		setObjectResource(null);
		super.fireTableDataChanged();
	}

	public Class getColumnClass(int columnIndex) 
	{
		Class clazz = Object.class;
		if(columnIndex == 0)
			clazz = String.class;
		else
			clazz = Object.class;
		return clazz;
	}

	public int getColumnCount() 
	{
		return 2;
	}

	public String getColumnName(int columnIndex) 
	{
		String name = "";

		if(columnIndex == 0)
			name = LangModel.getString(KEY_PROPERTY);
		else
		if(columnIndex == 0)
			name = LangModel.getString(KEY_VALUE);

		return name;
	}

	public ObjectResource getObjectResource() 
	{
		return objectResource;
	}

	public void setObjectResource(ObjectResource or) 
	{
		this.objectResource = or;
		super.fireTableDataChanged();
	}

	public int getRowCount() 
	{
		if(this.controller == null)
			return 0;
		return this.controller.getKeys().size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) 
	{
		if(this.controller == null)
			return "";
		if(objectResource == null)
			return "";
		Object obj;
		String property = this.controller.getKey(rowIndex);
		if(columnIndex == 0)
			obj = this.controller.getName(property);
		else
		{
			obj = this.controller.getValue(objectResource, property);

			if (this.controller.getPropertyValue(property) instanceof Map) 
			{
				Map map = (Map) this.controller.getPropertyValue(property);
				Object keyObject = null;
				for(Iterator it = map.keySet().iterator();it.hasNext();)
				{
					Object keyObj = it.next();
					if (map.get(keyObj).equals(obj))
					{
						keyObject = keyObj;
						break;
					}
				}
				obj = keyObject;
				
			}
		}		
		return obj;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) 
	{
		boolean editable;
		if(this.controller == null)
			editable = false;
		if(columnIndex == 0)
			editable = false;
		else
		{
			String property = this.controller.getKey(rowIndex);
			editable = this.controller.isEditable(property);
		}
		return editable;
	}

	public void setValueAt(Object obj, int rowIndex, int columnIndex) 
	{
		if(this.controller == null)
			return;
		if(columnIndex == 0)
			return;
		if(this.objectResource == null)
			return;

		String property = this.controller.getKey(rowIndex);
		if (this.controller.getPropertyValue(property) instanceof Map) 
		{
			Map map = (Map) this.controller.getPropertyValue(property);
			this.controller.setValue(objectResource, property, map.get(obj));
		} else
			this.controller.setValue(objectResource, property, obj);
		this.fireTableDataChanged();
	}

	public void setController(ObjectResourcePropertiesController controller)
	{
		this.controller = controller;
		this.fireTableDataChanged();
	}

	public ObjectResourcePropertiesController getController()
	{
		return controller;
	}
}