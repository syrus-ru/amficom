package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelFactory;

public class MapSUApplicationModelFactory
		extends MapApplicationModelFactory 
{
	public MapSUApplicationModelFactory()
	{
	}

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
		aModel.setUsable("mapActionMarkShow", true);
		aModel.setUsable("mapActionMarkMove", true);
		aModel.setUsable("mapActionMarkCreate", true);
		aModel.setUsable("mapActionMarkDelete", true);
//		aModel.setUsable("mapActionIndication", true);
		aModel.setUsable("mapActionViewProperties", true);
		aModel.setUsable("mapActionEditProperties", true);

		aModel.setUsable("mapActionShowProto", true);
//		aModel.setUsable("mapActionReload", true);
		aModel.setUsable("mapActionReload", false);

		aModel.setUsable("mapModeNodeLink", true);
		aModel.setUsable("mapModeLink", true);
		aModel.setUsable("mapModePath", true);

		aModel.setUsable("menuEditUndo", true);
		aModel.setUsable("menuEditRedo", true);
		aModel.setUsable("menuEditCut", true);
		aModel.setUsable("menuEditCopy", true);
		aModel.setUsable("menuEditPaste", true);
		aModel.setUsable("menuEditSelect", true);

		aModel.setUsable("menuViewNavigator", true);
		aModel.setUsable("menuViewToolbar", true);
		aModel.setUsable("menuViewRefresh", true);
		aModel.setUsable("menuViewPanel", true);

		aModel.setUsable("menuNavigateZoomSelection", true);
		aModel.setUsable("menuNavigateZoomMap", true);
		aModel.setUsable("menuNavigateCenterMap", true);
		aModel.setUsable("menuNavigateCenterSelection", true);

		aModel.setUsable("menuElementGroup", true);
		aModel.setUsable("menuElementUngroup", true);
		aModel.setUsable("menuElementAlign", true);

		aModel.setUsable("menuHelpContents", true);
		aModel.setUsable("menuHelpFind", true);
		aModel.setUsable("menuelpTips", true);
		aModel.setUsable("menuHelpCourse", true);
		aModel.setUsable("menuHelpHelp", true);

		return aModel;
	}
}
