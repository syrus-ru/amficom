package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;

import java.awt.Color;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class StubPropertyDisplayModel implements ObjectResourceDisplayModel
{
	ObjectResource or;
	
	public StubPropertyDisplayModel()
	{
	}
	
	public StubPropertyDisplayModel(ObjectResource or)
	{
		this.or = or;
	}
	
	static LinkedList empty = new LinkedList();
	
	public List getColumns()
	{
		if(or == null)
			return empty;
		ObjectResourceModel mod = or.getModel();
		if(mod == null)
			return empty;
		return mod.getPropertyColumns();
	}
	
	public String getColumnName(String col_id)
	{
		if(or == null)
			return "";
		ObjectResourceModel mod = or.getModel();
		if(mod == null)
			return "";
		return mod.getPropertyName(col_id);
	}
	
	public int getColumnSize(String col_id)
	{
		return 100;
	}
	
	public boolean isColumnEditable(String col_id)
	{
		if(or == null)
			return false;
		ObjectResourceModel mod = or.getModel();
		if(mod == null)
			return false;
		return mod.isPropertyEditable(col_id);
	}

	public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
    {
		if(this.or == null)
			return null;
		ObjectResourceModel mod = this.or.getModel();
		if(mod == null)
			return null;
		return (PropertyRenderer )mod.getPropertyRenderer(col_id);
	}

	public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
    {
		if(this.or == null)
			return null;
		ObjectResourceModel mod = this.or.getModel();
		if(mod == null)
			return null;
		return (PropertyEditor )mod.getPropertyEditor(col_id);
	}

	public boolean isColumnColored(String col_id)
	{
		return false;
	}
	
	public Color getColumnColor(ObjectResource or, String col_id)
	{
		return Color.white;
	}
}
