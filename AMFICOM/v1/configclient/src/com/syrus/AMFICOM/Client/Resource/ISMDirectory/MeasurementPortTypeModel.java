package com.syrus.AMFICOM.Client.Resource.ISMDirectory;

import java.util.Map;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Network.CatalogElementModel;

public class MeasurementPortTypeModel extends CatalogElementModel
{
	private MeasurementPortType portType;
	public MeasurementPortTypeModel(MeasurementPortType portType)
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
		if(col_id.equals("description"))
			return portType.description;
		return "";
	}

	public Map getCharacteristics(ObjectResource obj)
	{
		return portType.characteristics;
	}
}

