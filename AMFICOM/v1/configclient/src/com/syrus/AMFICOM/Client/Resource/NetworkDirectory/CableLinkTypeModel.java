package com.syrus.AMFICOM.Client.Resource.NetworkDirectory;

import java.util.Map;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Network.CatalogElementModel;

public class CableLinkTypeModel extends CatalogElementModel
{
	private CableLinkType linkType;
	public CableLinkTypeModel(CableLinkType linkType)
	{
		super(linkType);
		this.linkType = linkType;
	}

	public String getColumnValue(String col_id)
	{
		if(col_id.equals("id"))
			return linkType.id;
		if(col_id.equals("name"))
			return linkType.name;
		return "";
	}

	public Map getCharacteristics(ObjectResource obj)
	{
		return linkType.characteristics;
	}
}

