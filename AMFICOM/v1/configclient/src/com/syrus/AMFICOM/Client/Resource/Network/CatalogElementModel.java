package com.syrus.AMFICOM.Client.Resource.Network;

import java.awt.Component;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;

import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

public abstract class CatalogElementModel extends ObjectResourceModel
{
	protected ObjectResource obj;

	public CatalogElementModel(ObjectResource obj)
	{
		this.obj = obj;
	}

	public abstract Hashtable getCharacteristics(ObjectResource obj);

	public Component getPropertyRenderer(String col_id)
	{
		Characteristic ch = (Characteristic )getCharacteristics(obj).get(col_id);
		if(ch == null)
			return null;
		return ch.getRenderer();
	}

	public Component getPropertyEditor(String col_id)
	{
		Characteristic ch = (Characteristic )getCharacteristics(obj).get(col_id);
		if(ch == null)
			return null;
		return ch.getEditor();
	}

	public Vector getPropertyColumns()
	{
		Vector cols = new Vector();
		for(Enumeration enum = getCharacteristics(obj).elements(); enum.hasMoreElements();)
		{
			Characteristic ch = (Characteristic )enum.nextElement();
			cols.add(ch.getId());
		}
		return cols;
	}

	public String getPropertyName(String col_id)
	{
		Characteristic ch = (Characteristic )getCharacteristics(obj).get(col_id);
		if(ch == null)
			return "";
		return ch.getName();
	}

	public String getPropertyValue(String col_id)
	{
		Characteristic ch = (Characteristic )getCharacteristics(obj).get(col_id);
		if(ch == null)
			return "";
		return ch.value;
	}

	public void setPropertyValue(String col_id, Object val)
	{
		Characteristic ch = (Characteristic )getCharacteristics(obj).get(col_id);
		if(ch == null)
			;
		else
			ch.setValue(val);
	}

	public boolean isPropertyEditable(String col_id)
	{
		Characteristic ch = (Characteristic )getCharacteristics(obj).get(col_id);
		if(ch == null)
			return false;
		return ch.isEditable();
	}
}
