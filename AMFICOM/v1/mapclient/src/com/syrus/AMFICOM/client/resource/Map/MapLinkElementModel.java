package com.syrus.AMFICOM.Client.Resource.Map;

import com.ofx.geometry.SxDoublePoint;

import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Scheme.ElementAttribute;

import java.awt.Component;

import java.util.Enumeration;
import java.util.Vector;

//A0A
public class MapLinkElementModel extends ObjectResourceModel
{
	protected MapLinkElement obj;

	MapLinkElementModel(MapLinkElement obj)
	{
		this.obj = obj;
	}

	public Component getPropertyRenderer(String col_id)
	{
		ElementAttribute ea = (ElementAttribute )obj.attributes.get(col_id);
		if(ea == null)
//			return getColumnRenderer(col_id);
			return null;
		return ea.getRenderer();
	}

	public Component getPropertyEditor(String col_id)
	{
		ElementAttribute ea = (ElementAttribute )obj.attributes.get(col_id);
		if(ea == null)
//			return getColumnEditor(col_id);
			return null;
		return ea.getEditor();
	}

	public Vector getPropertyColumns()
	{
		Vector cols = new Vector();//getColumns();
		for(Enumeration enum = obj.attributes.elements(); enum.hasMoreElements();)
		{
			ElementAttribute ea = (ElementAttribute )enum.nextElement();
			if(ea.visible)
				cols.add(ea.getId());
		}
		return cols;
	}

	public String getPropertyName(String col_id)
	{
		ElementAttribute ea = (ElementAttribute )obj.attributes.get(col_id);
		if(ea == null)
//			return getColumnName(col_id);
			return "";
		return ea.getName();
	}

	public String getPropertyValue(String col_id)
	{
		ElementAttribute  ea = (ElementAttribute )obj.attributes.get(col_id);
		if(ea == null)
//			return getColumnValue(col_id);
			return "";
		return ea.value;
	}

	public void setPropertyValue(String col_id, Object val)
	{
		ElementAttribute ea = (ElementAttribute )obj.attributes.get(col_id);
		if(ea == null)
//			setColumnValue(col_id, val);
			;
		else
			ea.setValue(val);
	}

	public boolean isPropertyEditable(String col_id)
	{
		ElementAttribute ea = (ElementAttribute )obj.attributes.get(col_id);
		if(ea == null)
//			return isColumnEditable(col_id);
			return false;
		return ea.isEditable();
	}
}
