package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.EmptyMapDataSource;
import com.syrus.AMFICOM.Client.Resource.RISDMapDataSource;

public class MapApplicationModel extends ApplicationModel
{
	public MapApplicationModel()
	{
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
}
