package com.syrus.AMFICOM.Client.Resource.ISM;

import java.util.Map;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Network.CatalogElementModel;

public class KISModel extends CatalogElementModel
{
	private KIS kis;
	public KISModel(KIS kis)
	{
		super(kis);
		this.kis = kis;
	}

	public Map getCharacteristics(ObjectResource obj)
	{
		return kis.characteristics;
	}
}
