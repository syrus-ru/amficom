package com.syrus.AMFICOM.Client.Resource.NetworkDirectory;

import java.util.Map;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Network.CatalogElementModel;

public class EquipmentTypeModel extends CatalogElementModel
{
	private EquipmentType equipmentType;
	public EquipmentTypeModel(EquipmentType eqType)
	{
		super(eqType);
		this.equipmentType = eqType;
	}

	public String getColumnValue(String col_id)
	{
		if(col_id.equals("id"))
			return equipmentType.id;
		if(col_id.equals("name"))
			return equipmentType.name;
		if(col_id.equals("eq_class"))
			return equipmentType.eqClass;

		return "";
	}

	public Map getCharacteristics(ObjectResource obj)
	{
		return equipmentType.characteristics;
	}
}

