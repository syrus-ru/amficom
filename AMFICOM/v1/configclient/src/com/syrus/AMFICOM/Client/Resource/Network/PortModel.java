package com.syrus.AMFICOM.Client.Resource.Network;

import java.util.Map;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

public class PortModel extends CatalogElementModel
{
	private Port port;
	public PortModel(Port port)
	{
		super(port);
		this.port = port;
	}

	public String getColumnValue(String col_id)
	{
		if(col_id.equals("id"))
			return port.id;
		if(col_id.equals("name"))
			return port.name;
		if(col_id.equals("description"))
			return port.description;
		if(col_id.equals("type_id"))
			return port.typeId;
		if(col_id.equals("equipment_id"))
			return port.equipmentId;
		if(col_id.equals("interface_id"))
			return port.interfaceId;
		if(col_id.equals("address_id"))
			return port.addressId;
		if(col_id.equals("domain_id"))
			return port.domainId;

		return "";
	}

	public Map getCharacteristics(ObjectResource obj)
	{
		return port.characteristics;
	}
}

