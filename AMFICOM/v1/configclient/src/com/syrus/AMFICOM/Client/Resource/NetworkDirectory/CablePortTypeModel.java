package com.syrus.AMFICOM.Client.Resource.NetworkDirectory;

import java.util.Map;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Network.CatalogElementModel;

public class CablePortTypeModel extends CatalogElementModel
{
	private CablePortType portType;
	public CablePortTypeModel(CablePortType portType)
	{
		super(portType);
		this.portType = portType;
	}

	public String getColumnValue(String col_id)
	{
		if(col_id.equals("id"))
			return portType.id;
		if(col_id.equals("name"))
			return portType.name;
		return "";
	}

	public Map getCharacteristics(ObjectResource obj)
	{
		return portType.characteristics;
	}
}

