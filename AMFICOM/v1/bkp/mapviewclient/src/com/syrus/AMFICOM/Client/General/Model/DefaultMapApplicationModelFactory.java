package com.syrus.AMFICOM.Client.General.Model;

public class DefaultMapApplicationModelFactory
		extends MapApplicationModelFactory 
{
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setUsable("mapActionMoveNode", true);
		aModel.setUsable("mapActionMoveEquipment", true);
		aModel.setUsable("mapActionMoveKIS", true);
		aModel.setUsable("mapActionShowLink", true);
		aModel.setUsable("mapActionShowEquipment", true);
		aModel.setUsable("mapActionShowKIS", true);
		aModel.setUsable("mapActionShowPath", true);
		aModel.setUsable("mapActionCreateLink", true);
		aModel.setUsable("mapActionCreateEquipment", true);
		aModel.setUsable("mapActionCreateKIS", true);
		aModel.setUsable("mapActionCreatePath", true);
		aModel.setUsable("mapActionDeleteNode", true);
		aModel.setUsable("mapActionDeleteEquipment", true);
		aModel.setUsable("mapActionDeleteKIS", true);
		aModel.setUsable("mapActionDeletePath", true);
		aModel.setUsable("mapActionMarkerMove", true);
		aModel.setUsable("mapActionMarkerCreate", true);
		aModel.setUsable("mapActionMarkerDelete", true);
		aModel.setUsable("mapActionIndication", true);
		aModel.setUsable("mapActionViewProperties", true);
		aModel.setUsable("mapActionEditProperties", true);

		aModel.setUsable("mapActionMarkShow", true);
		aModel.setUsable("mapActionMarkMove", true);
		aModel.setUsable("mapActionMarkCreate", true);
		aModel.setUsable("mapActionMarkDelete", true);

		aModel.setInstalled("mapActionShowProto", false);
		aModel.setUsable("mapActionReload", true);

		aModel.setUsable("mapModeNodeLink", true);
		aModel.setUsable("mapModeLink", true);
		aModel.setUsable("mapModePath", true);

		return aModel;
	}
}
