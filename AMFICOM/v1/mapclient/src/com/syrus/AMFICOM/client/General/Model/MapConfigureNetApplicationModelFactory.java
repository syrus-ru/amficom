package com.syrus.AMFICOM.Client.General.Model;

public class MapConfigureNetApplicationModelFactory
		extends MapApplicationModelFactory 
{
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setUsable("mapActionMoveNode", true);
		aModel.setUsable("mapActionMoveEquipment", true);
		aModel.setUsable("mapActionMoveKIS", false);
		aModel.setUsable("mapActionShowLink", true);
		aModel.setUsable("mapActionShowEquipment", true);
		aModel.setUsable("mapActionShowKIS", false);
		aModel.setUsable("mapActionShowPath", false);
		aModel.setUsable("mapActionCreateLink", true);
		aModel.setUsable("mapActionCreateEquipment", true);
		aModel.setUsable("mapActionCreateKIS", false);
		aModel.setUsable("mapActionCreatePath", false);
		aModel.setUsable("mapActionDeleteNode", true);
		aModel.setUsable("mapActionDeleteEquipment", true);
		aModel.setUsable("mapActionDeleteKIS", false);
		aModel.setUsable("mapActionDeletePath", false);
		aModel.setUsable("mapActionMarkerMove", false);
		aModel.setUsable("mapActionMarkerCreate", false);
		aModel.setUsable("mapActionMarkerDelete", false);
		aModel.setUsable("mapActionMarkShow", true);
		aModel.setUsable("mapActionMarkMove", true);
		aModel.setUsable("mapActionMarkCreate", true);
		aModel.setUsable("mapActionMarkDelete", true);
		aModel.setUsable("mapActionIndication", false);
		aModel.setUsable("mapActionViewProperties", true);
		aModel.setUsable("mapActionEditProperties", true);

		aModel.setUsable("mapActionShowProto", true);
		aModel.setUsable("mapActionReload", false);

		aModel.setUsable("mapModeNodeLink", true);
		aModel.setUsable("mapModeLink", true);
		aModel.setUsable("mapModePath", false);

		aModel.setUsable("menuEditUndo", false);
		aModel.setUsable("menuEditRedo", false);
		aModel.setUsable("menuEditCut", false);
		aModel.setUsable("menuEditCopy", false);
		aModel.setUsable("menuEditPaste", false);
		aModel.setUsable("menuEditSelect", false);

		aModel.setUsable("menuViewNavigator", false);
		aModel.setUsable("menuViewToolbar", false);
		aModel.setUsable("menuViewRefresh", false);
		aModel.setUsable("menuViewPanel", false);

		aModel.setUsable("menuNavigateZoomSelection", false);
		aModel.setUsable("menuNavigateZoomMap", false);
		aModel.setUsable("menuNavigateCenterMap", false);
		aModel.setUsable("menuNavigateCenterSelection", false);

		aModel.setUsable("menuElementGroup", false);
		aModel.setUsable("menuElementUngroup", false);
		aModel.setUsable("menuElementAlign", false);

		aModel.setUsable("menuHelpContents", false);
		aModel.setUsable("menuHelpFind", false);
		aModel.setUsable("menuelpTips", false);
		aModel.setUsable("menuHelpCourse", false);
		aModel.setUsable("menuHelpHelp", false);

		return aModel;
	}
}
