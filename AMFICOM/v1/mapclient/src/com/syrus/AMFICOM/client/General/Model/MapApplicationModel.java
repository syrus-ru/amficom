package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.EmptyMapDataSource;
import com.syrus.AMFICOM.Client.Resource.RISDMapDataSource;

public class MapApplicationModel extends ApplicationModel
{
	public MapApplicationModel()
	{
		add("menuMap");
		add("menuMapNew");
		add("menuMapOpen");
		add("menuMapClose");
		add("menuMapSave");
		add("menuMapOptions");
		add("menuExit");

		add("menuEdit");
		add("menuEditUndo");
		add("menuEditRedo");
		add("menuEditCut");
		add("menuEditCopy");
		add("menuEditPaste");
		add("menuEditDelete");
		add("menuEditSelectAll");
		add("menuEditSelect");

		add("menuView");
		add("menuViewNavigator");
		add("menuViewToolbar");
		add("menuViewRefresh");
		add("menuViewPoints");
		add("menuViewMetrics");
		add("menuViewPanel");

		add("menuNavigate");
		add("menuNavigateLeft");
		add("menuNavigateRight");
		add("menuNavigateUp");
		add("menuNavigateDown");
		add("menuNavigateZoomIn");
		add("menuNavigateZoomOut");
		add("menuNavigateZoomBox");
		add("menuNavigateZoomSelection");
		add("menuNavigateZoomMap");
		add("menuNavigateCenterPoint");
		add("menuNavigateCenterSelection");
		add("menuNavigateCenterMap");

		add("menuElement");
		add("menuElementCatalogue");
		add("menuElementGroup");
		add("menuElementUngroup");
		add("menuElementAlign");
		add("menuElementProperties");

		add("menuMarker");
		add("menuMarkerCreate");
		add("menuMarkerDelete");
		add("menuMarkerDeleteAll");

		add("menuHelp");
		add("menuHelpContents");
		add("menuHelpFind");
		add("menuHelpTips");
		add("menuHelpCourse");
		add("menuHelpHelp");
		add("menuHelpAbout");

		add("mapActionMoveNode");
		add("mapActionMoveEquipment");
		add("mapActionMoveKIS");

		add("mapActionShowLink");
		add("mapActionShowEquipment");
		add("mapActionShowKIS");
		add("mapActionShowPath");

		add("mapActionCreateLink");
		add("mapActionCreateEquipment");
		add("mapActionCreateKIS");
		add("mapActionCreatePath");

		add("mapActionDeleteNode");
		add("mapActionDeleteEquipment");
		add("mapActionDeleteKIS");
		add("mapActionDeletePath");

		add("mapActionMarkerMove");
		add("mapActionMarkerCreate");
		add("mapActionMarkerDelete");

		add("mapActionMarkShow");
		add("mapActionMarkMove");
		add("mapActionMarkCreate");
		add("mapActionMarkDelete");

		add("mapActionIndication");

		add("mapActionViewProperties");
		add("mapActionEditProperties");

		add("mapActionShowProto");
		add("mapActionReload");

		add("mapModeNodeLink");
		add("mapModeLink");
		add("mapModeCablePath");
		add("mapModePath");

		add("mapActionZoomIn");
		add("mapActionZoomOut");
		add("mapActionZoomToPoint");
		add("mapActionZoomBox");
		add("mapActionCenterSelection");
		add("mapActionMoveToCenter");

		add("mapModeViewNodes");

		add("mapActionHandPan");
		
		add("mapActionMeasureDistance");

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
