package com.syrus.AMFICOM.Client.General.Model;

public class MapSurveyNetApplicationModelFactory 
		extends MapApplicationModelFactory 
{
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setUsable("mapActionMoveNode", false);
		aModel.setUsable("mapActionMoveEquipment", false);
		aModel.setUsable("mapActionMoveKIS", false);
		aModel.setUsable("mapActionShowLink", true);
		aModel.setUsable("mapActionShowEquipment", true);
		aModel.setUsable("mapActionShowKIS", true);
		aModel.setUsable("mapActionShowPath", true);
		aModel.setUsable("mapActionCreateLink", false);
		aModel.setUsable("mapActionCreateEquipment", false);
		aModel.setUsable("mapActionCreateKIS", false);
		aModel.setUsable("mapActionCreatePath", false);
		aModel.setUsable("mapActionDeleteNode", false);
		aModel.setUsable("mapActionDeleteEquipment", false);
		aModel.setUsable("mapActionDeleteKIS", false);
		aModel.setUsable("mapActionDeletePath", false);
		aModel.setUsable("mapActionMarkerMove", true);
		aModel.setUsable("mapActionMarkerCreate", true);
		aModel.setUsable("mapActionMarkerDelete", true);
		aModel.setUsable("mapActionMarkShow", true);
		aModel.setUsable("mapActionMarkMove", false);
		aModel.setUsable("mapActionMarkCreate", false);
		aModel.setUsable("mapActionMarkDelete", false);
		aModel.setUsable("mapActionIndication", true);
		aModel.setUsable("mapActionViewProperties", true);
		aModel.setUsable("mapActionEditProperties", false);

		aModel.setUsable("mapActionShowProto", false);
		aModel.setUsable("mapActionReload", true);

		aModel.setUsable("mapModeNodeLink", false);
		aModel.setUsable("mapModeLink", true);
		aModel.setUsable("mapModePath", true);

		return aModel;
	}
}