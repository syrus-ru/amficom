package com.syrus.AMFICOM.Client.Resource.ISM;

import java.util.Map;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Network.CatalogElementModel;

public class MeasurementPortModel extends CatalogElementModel
{
	private MeasurementPort port;
	public MeasurementPortModel(MeasurementPort port)
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
		return "";
	}

	public Map getCharacteristics(ObjectResource obj)
	{
		return port.characteristics;
	}
}

