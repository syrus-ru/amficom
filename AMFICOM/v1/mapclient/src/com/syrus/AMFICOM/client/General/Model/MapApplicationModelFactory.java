package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelFactory;

public class MapApplicationModelFactory
		implements ApplicationModelFactory
{
	public MapApplicationModelFactory()
	{
	}

	public ApplicationModel create()
	{
		ApplicationModel aModel = new MapApplicationModel();
		return aModel;
	}
}
