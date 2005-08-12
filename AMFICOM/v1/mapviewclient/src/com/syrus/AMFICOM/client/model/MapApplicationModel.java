/**
 * $Id: MapApplicationModel.java,v 1.6 2005/08/12 14:52:33 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.model;

import static com.syrus.AMFICOM.client.model.MapApplicationModel.ACTION_EDIT_BINDING;
import static com.syrus.AMFICOM.client.model.MapApplicationModel.ACTION_EDIT_MAP;
import static com.syrus.AMFICOM.client.model.MapApplicationModel.ACTION_EDIT_MAP_VIEW;
import static com.syrus.AMFICOM.client.model.MapApplicationModel.ACTION_EDIT_PROPERTIES;
import static com.syrus.AMFICOM.client.model.MapApplicationModel.ACTION_INDICATION;
import static com.syrus.AMFICOM.client.model.MapApplicationModel.ACTION_SAVE_MAP;
import static com.syrus.AMFICOM.client.model.MapApplicationModel.ACTION_SAVE_MAP_VIEW;
import static com.syrus.AMFICOM.client.model.MapApplicationModel.ACTION_USE_MARKER;
import static com.syrus.AMFICOM.client.model.MapApplicationModel.MODE_CABLE_PATH;
import static com.syrus.AMFICOM.client.model.MapApplicationModel.MODE_INDICATION;
import static com.syrus.AMFICOM.client.model.MapApplicationModel.MODE_LINK;
import static com.syrus.AMFICOM.client.model.MapApplicationModel.MODE_NODES;
import static com.syrus.AMFICOM.client.model.MapApplicationModel.MODE_NODE_LINK;
import static com.syrus.AMFICOM.client.model.MapApplicationModel.MODE_PATH;
import static com.syrus.AMFICOM.client.model.MapApplicationModel.OPERATION_CENTER_SELECTION;
import static com.syrus.AMFICOM.client.model.MapApplicationModel.OPERATION_HAND_PAN;
import static com.syrus.AMFICOM.client.model.MapApplicationModel.OPERATION_MEASURE_DISTANCE;
import static com.syrus.AMFICOM.client.model.MapApplicationModel.OPERATION_MOVE_FIXED;
import static com.syrus.AMFICOM.client.model.MapApplicationModel.OPERATION_MOVE_TO_CENTER;
import static com.syrus.AMFICOM.client.model.MapApplicationModel.OPERATION_ZOOM_BOX;
import static com.syrus.AMFICOM.client.model.MapApplicationModel.OPERATION_ZOOM_IN;
import static com.syrus.AMFICOM.client.model.MapApplicationModel.OPERATION_ZOOM_OUT;
import static com.syrus.AMFICOM.client.model.MapApplicationModel.OPERATION_ZOOM_TO_POINT;

/**
 * ������ ���������� ���������� �������������� ��������, ���������� ��� ������
 * � ������.
 *  
 * 
 * @author Andrei Kroupennikov
 * @version $Revision: 1.6 $, $Date: 2005/08/12 14:52:33 $
 * @module mapviewclient
 */
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
	public static final String MODE_INDICATION = "mapModeViewIndication";

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
		super.add(MODE_INDICATION);

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

	public String toString()
	{
		return getClass().getName();
	}
}
