package com.syrus.AMFICOM.Client.General.Model;

public class MapReportApplicationModelFactory 
		extends MapApplicationModelFactory 
{
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setUsable(MapApplicationModel.ACTION_INDICATION, false);
		aModel.setUsable(MapApplicationModel.ACTION_EDIT_MAP, false);
		aModel.setUsable(MapApplicationModel.ACTION_EDIT_MAP_VIEW, false);
		aModel.setUsable(MapApplicationModel.ACTION_EDIT_BINDING, false);
		aModel.setUsable(MapApplicationModel.ACTION_EDIT_PROPERTIES, false);
		aModel.setUsable(MapApplicationModel.ACTION_SAVE_MAP, false);
		aModel.setUsable(MapApplicationModel.ACTION_SAVE_MAP_VIEW, false);
		aModel.setUsable(MapApplicationModel.ACTION_USE_MARKER, false);

		aModel.setUsable("mapModeNodeLink", false);

		return aModel;
	}
}
