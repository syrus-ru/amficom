package com.syrus.AMFICOM.Client.General.Model;

public class MapApplicationModelFactory
		implements ApplicationModelFactory
{
	public ApplicationModel create()
	{
		ApplicationModel aModel = new MapApplicationModel();
		return aModel;
	}
}
