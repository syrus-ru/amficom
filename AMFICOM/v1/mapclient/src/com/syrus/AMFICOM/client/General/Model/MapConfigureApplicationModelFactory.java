package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelFactory;

public class MapConfigureApplicationModelFactory
		extends MapApplicationModelFactory 
{
	public MapConfigureApplicationModelFactory()
	{
	}

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

		aModel.setUsable("mapActionShowProto", false);
		aModel.setUsable("mapActionReload", false);

		aModel.setUsable("mapModeNodeLink", true);
		aModel.setUsable("mapModeLink", true);
		aModel.setUsable("mapModePath", true);

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
