package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.EmptyMapDataSource;
import com.syrus.AMFICOM.Client.Resource.RISDMapDataSource;

public class MapApplicationModel extends ApplicationModel
{
	public MapApplicationModel()
	{
		super.add("menuMap");
		super.add("menuMapNew");
		super.add("menuMapOpen");
		super.add("menuMapClose");
		super.add("menuMapSave");
		super.add("menuMapOptions");
		super.add("menuExit");

		super.add("menuEdit");
		super.add("menuEditUndo");
		super.add("menuEditRedo");
		super.add("menuEditCut");
		super.add("menuEditCopy");
		super.add("menuEditPaste");
		super.add("menuEditDelete");
		super.add("menuEditSelectAll");
		super.add("menuEditSelect");

		super.add("menuView");
		super.add("menuViewNavigator");
		super.add("menuViewToolbar");
		super.add("menuViewRefresh");
		super.add("menuViewPoints");
		super.add("menuViewMetrics");
		super.add("menuViewPanel");

		super.add("menuNavigate");
		super.add("menuNavigateLeft");
		super.add("menuNavigateRight");
		super.add("menuNavigateUp");
		super.add("menuNavigateDown");
		super.add("menuNavigateZoomIn");
		super.add("menuNavigateZoomOut");
		super.add("menuNavigateZoomBox");
		super.add("menuNavigateZoomSelection");
		super.add("menuNavigateZoomMap");
		super.add("menuNavigateCenterPoint");
		super.add("menuNavigateCenterSelection");
		super.add("menuNavigateCenterMap");

		super.add("menuElement");
		super.add("menuElementCatalogue");
		super.add("menuElementGroup");
		super.add("menuElementUngroup");
		super.add("menuElementAlign");
		super.add("menuElementProperties");

		super.add("menuMarker");
		super.add("menuMarkerCreate");
		super.add("menuMarkerDelete");
		super.add("menuMarkerDeleteAll");

		super.add("menuHelp");
		super.add("menuHelpContents");
		super.add("menuHelpFind");
		super.add("menuHelpTips");
		super.add("menuHelpCourse");
		super.add("menuHelpHelp");
		super.add("menuHelpAbout");

		super.add("mapActionMoveNode");
		super.add("mapActionMoveEquipment");
		super.add("mapActionMoveKIS");

		super.add("mapActionShowLink");
		super.add("mapActionShowEquipment");
		super.add("mapActionShowKIS");
		super.add("mapActionShowPath");

		super.add("mapActionCreateLink");
		super.add("mapActionCreateEquipment");
		super.add("mapActionCreateKIS");
		super.add("mapActionCreatePath");

		super.add("mapActionDeleteNode");
		super.add("mapActionDeleteEquipment");
		super.add("mapActionDeleteKIS");
		super.add("mapActionDeletePath");

		super.add("mapActionMarkerMove");
		super.add("mapActionMarkerCreate");
		super.add("mapActionMarkerDelete");

		super.add("mapActionMarkShow");
		super.add("mapActionMarkMove");
		super.add("mapActionMarkCreate");
		super.add("mapActionMarkDelete");

		super.add("mapActionIndication");

		super.add("mapActionViewProperties");
		super.add("mapActionEditProperties");

		super.add("mapActionShowProto");
		super.add("mapActionReload");

		super.add("mapModeNodeLink");
		super.add("mapModeLink");
		super.add("mapModeCablePath");
		super.add("mapModePath");

		super.add("mapActionZoomIn");
		super.add("mapActionZoomOut");
		super.add("mapActionZoomToPoint");
		super.add("mapActionZoomBox");
		super.add("mapActionCenterSelection");
		super.add("mapActionMoveToCenter");

		super.add("mapModeViewNodes");

		super.add("mapActionHandPan");
		
		super.add("mapActionMeasureDistance");

	}

	public DataSourceInterface getDataSource(SessionInterface si)
	{
		String connection = Environment.getConnectionType();
		if(connection.equals("RISD"))
			return new RISDMapDataSource(si);
		else
		if(connection.equals("Empty"))
			return new EmptyMapDataSource(si);
		return null;
	}
}
