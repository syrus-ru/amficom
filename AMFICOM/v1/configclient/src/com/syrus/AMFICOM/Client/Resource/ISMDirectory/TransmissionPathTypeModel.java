package com.syrus.AMFICOM.Client.Resource.ISMDirectory;

import java.util.Map;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Network.CatalogElementModel;

public class TransmissionPathTypeModel extends CatalogElementModel
{
	private TransmissionPathType pathType;
	public TransmissionPathTypeModel(TransmissionPathType pathType)
	{
		super(pathType);
		this.pathType = pathType;
	}

	public String getColumnValue(String col_id)
	{
		if(col_id.equals("id"))
			return pathType.id;
		if(col_id.equals("name"))
			return pathType.name;
		return "";
	}

	public Map getCharacteristics(ObjectResource obj)
	{
		return pathType.characteristics;
	}
}

