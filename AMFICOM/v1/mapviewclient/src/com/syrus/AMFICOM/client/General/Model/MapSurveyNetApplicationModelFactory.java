package com.syrus.AMFICOM.Client.General.Model;

public class MapSurveyNetApplicationModelFactory 
		extends MapApplicationModelFactory 
{
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setUsable(MapApplicationModel.ACTION_EDIT_MAP, false);
		aModel.setUsable(MapApplicationModel.ACTION_EDIT_PROPERTIES, false);
		aModel.setUsable(MapApplicationModel.ACTION_SAVE_MAP, false);

		aModel.setUsable(MapApplicationModel.MODE_NODE_LINK, false);

		return aModel;
	}
}