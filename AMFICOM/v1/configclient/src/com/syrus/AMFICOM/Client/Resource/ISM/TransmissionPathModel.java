package com.syrus.AMFICOM.Client.Resource.ISM;

import java.util.Map;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Network.CatalogElementModel;

public class TransmissionPathModel extends CatalogElementModel
{
	private TransmissionPath path;
	public TransmissionPathModel(TransmissionPath path)
	{
		super(path);
		this.path = path;
	}

	public String getColumnValue(String col_id)
	{
		if(col_id.equals("id"))
			return path.id;
		if(col_id.equals("name"))
			return path.name;
		return "";
	}

	public Map getCharacteristics(ObjectResource obj)
	{
		return path.characteristics;
	}
}

