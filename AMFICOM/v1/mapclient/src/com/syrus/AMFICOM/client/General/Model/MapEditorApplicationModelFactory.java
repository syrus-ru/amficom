package com.syrus.AMFICOM.Client.General.Model;

public class MapEditorApplicationModelFactory
		implements ApplicationModelFactory
{
	public MapEditorApplicationModelFactory()
	{
	}

	public ApplicationModel create()
	{
		ApplicationModel aModel = new MapEditorApplicationModel();
		return aModel;
	}
}