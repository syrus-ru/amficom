package com.syrus.AMFICOM.Client.Resource.Network;

import java.util.Map;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

public class CableLinkModel extends CatalogElementModel
{
	private CableLink link;
	public CableLinkModel(CableLink link)
	{
		super(link);
		this.link = link;
	}

	public String getColumnValue(String col_id)
	{
		if(col_id.equals("id"))
			return link.id;
		if(col_id.equals("name"))
			return link.name;
		return "";
	}

	public Map getCharacteristics(ObjectResource obj)
	{
		return link.characteristics;
	}
}

