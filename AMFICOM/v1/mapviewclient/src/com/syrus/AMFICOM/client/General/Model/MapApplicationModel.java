package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.EmptyMapDataSource;
import com.syrus.AMFICOM.Client.Resource.EmptyMapViewDataSource;
import com.syrus.AMFICOM.Client.Resource.RISDMapDataSource;
import com.syrus.AMFICOM.Client.Resource.RISDMapViewDataSource;

public class MapApplicationModel extends ApplicationModel
{
	public static final String ACTION_SAVE_MAP = "savemap";
	public static final String ACTION_SAVE_MAP_VIEW = "savemapview";
	public static final String ACTION_EDIT_MAP = "editmap";
	public static final String ACTION_EDIT_MAP_VIEW = "editmapview";
	public static final String ACTION_EDIT_BINDING = "editbinding";
	public static final String ACTION_USE_MARKER = "usemarker";
	public static final String ACTION_INDICATION = "indication";
	public static final String ACTION_EDIT_PROPERTIES = "editproperties";

	public MapApplicationModel()
	{
		super.add(ACTION_SAVE_MAP);
		super.add(ACTION_SAVE_MAP_VIEW);
		super.add(ACTION_EDIT_MAP);
		super.add(ACTION_EDIT_MAP_VIEW);
		super.add(ACTION_EDIT_BINDING);
		super.add(ACTION_USE_MARKER);
		super.add(ACTION_INDICATION);
		super.add(ACTION_EDIT_PROPERTIES);
		
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
		super.add("mapActionHandPan");

		super.add("mapModeViewNodes");
		
		super.add("mapActionMeasureDistance");

//		super.add("mapActionMoveNode");
//		super.add("mapActionMoveEquipment");
//		super.add("mapActionMoveKIS");

//		super.add("mapActionShowLink");
//		super.add("mapActionShowEquipment");
//		super.add("mapActionShowKIS");
//		super.add("mapActionShowPath");

//		super.add("mapActionCreateLink");
//		super.add("mapActionCreateEquipment");
//		super.add("mapActionCreateKIS");
//		super.add("mapActionCreatePath");

//		super.add("mapActionDeleteNode");
//		super.add("mapActionDeleteEquipment");
//		super.add("mapActionDeleteKIS");
//		super.add("mapActionDeletePath");

//		super.add("mapActionMarkerMove");
//		super.add("mapActionMarkerCreate");
//		super.add("mapActionMarkerDelete");

//		super.add("mapActionMarkShow");
//		super.add("mapActionMarkMove");
//		super.add("mapActionMarkCreate");
//		super.add("mapActionMarkDelete");

//		super.add("mapActionIndication");

//		super.add("mapActionViewProperties");
//		super.add("mapActionEditProperties");

//		super.add("mapActionShowProto");
//		super.add("mapActionReload");

	}

	private static DataSourceInterface dataSource = null;
	
	public DataSourceInterface getDataSource(final SessionInterface session) 
	{
		String connection = Environment.getConnectionType();
        if ((this.session == null) || (!this.session.equals(session)))
			synchronized (this) 
			{
					if ((this.session == null) || (!this.session.equals(session))) 
					{
						this.session = session;
						if(connection.equalsIgnoreCase(Environment.CONNECTION_RISD))
							this.dataSource = new RISDMapViewDataSource(this.session);
						else
						if(connection.equalsIgnoreCase(Environment.CONNECTION_EMPTY))
							this.dataSource = new EmptyMapViewDataSource(this.session);
					}
			}
        return this.dataSource;
	}
}
