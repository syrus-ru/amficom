package com.syrus.AMFICOM.Client.Resource.Network;

import com.syrus.AMFICOM.Client.Resource.General.Characteristic;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;

import java.awt.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class CatalogElementModel extends ObjectResourceModel
{
	protected ObjectResource obj;

	public CatalogElementModel(ObjectResource obj)
	{
		this.obj = obj;
	}

	public abstract Map getCharacteristics(ObjectResource obj);

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

	public List getPropertyColumns()
	{
		List cols = new ArrayList();
		for(Iterator it = getCharacteristics(obj).values().iterator(); it.hasNext();)
		{
			Characteristic ch = (Characteristic )it.next();
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
