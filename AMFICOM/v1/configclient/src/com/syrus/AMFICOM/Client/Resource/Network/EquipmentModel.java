package com.syrus.AMFICOM.Client.Resource.Network;

import java.util.Map;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

public class EquipmentModel extends CatalogElementModel
{
	private Equipment equipment;
	public EquipmentModel(Equipment equipment)
	{
		super(equipment);
		this.equipment = equipment;
	}

	public String getColumnValue(String col_id)
	{
		if(col_id.equals("id"))
			return equipment.id;
		if(col_id.equals("name"))
			return equipment.name;
		return "";
	}

	public Map getCharacteristics(ObjectResource obj)
	{
		return equipment.characteristics;
	}
}

