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

	public static final String MODE_NODE_LINK = "mapModeNodeLink";
	public static final String MODE_LINK = "mapModeLink";
	public static final String MODE_CABLE_PATH = "mapModeCablePath";
	public static final String MODE_PATH = "mapModePath";

	public static final String MODE_NODES = "mapModeViewNodes";

	public static final String OPERATION_ZOOM_IN = "mapActionZoomIn";
	public static final String OPERATION_ZOOM_OUT = "mapActionZoomOut";
	public static final String OPERATION_ZOOM_TO_POINT = "mapActionZoomToPoint";
	public static final String OPERATION_ZOOM_BOX = "mapActionZoomBox";
	public static final String OPERATION_CENTER_SELECTION = "mapActionCenterSelection";
	public static final String OPERATION_MOVE_TO_CENTER = "mapActionMoveToCenter";
	public static final String OPERATION_HAND_PAN = "mapActionHandPan";
	public static final String OPERATION_MEASURE_DISTANCE = "mapActionMeasureDistance";
	public static final String OPERATION_MOVE_FIXED = "mapActionMoveFixed";

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
		
		super.add(MODE_NODE_LINK);
		super.add(MODE_LINK);
		super.add(MODE_CABLE_PATH);
		super.add(MODE_PATH);

		super.add(MODE_NODES);

		super.add(OPERATION_ZOOM_IN);
		super.add(OPERATION_ZOOM_OUT);
		super.add(OPERATION_ZOOM_TO_POINT);
		super.add(OPERATION_ZOOM_BOX);
		super.add(OPERATION_CENTER_SELECTION);
		super.add(OPERATION_MOVE_TO_CENTER);
		super.add(OPERATION_HAND_PAN);
		super.add(OPERATION_MEASURE_DISTANCE);
		super.add(OPERATION_MOVE_FIXED);

	}
/*
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
*/
}
