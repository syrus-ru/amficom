/**
 * $Id: MapApplicationModel.java,v 1.9 2005/09/16 14:53:38 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.model;

/**
 * Модель приложения определяет функциональные элементы, досступные при работе
 * с картой.
 *  
 * 
 * @author Andrei Kroupennikov
 * @version $Revision: 1.9 $, $Date: 2005/09/16 14:53:38 $
 * @module mapviewclient
 */
public class MapApplicationModel extends ApplicationModel
{
	public static final String ACTION_SAVE_MAP = "savemap"; //$NON-NLS-1$
	public static final String ACTION_SAVE_MAP_VIEW = "savemapview"; //$NON-NLS-1$
	public static final String ACTION_EDIT_MAP = "editmap"; //$NON-NLS-1$
	public static final String ACTION_EDIT_MAP_VIEW = "editmapview"; //$NON-NLS-1$
	public static final String ACTION_EDIT_BINDING = "editbinding"; //$NON-NLS-1$
	public static final String ACTION_USE_MARKER = "usemarker"; //$NON-NLS-1$
	public static final String ACTION_INDICATION = "indication"; //$NON-NLS-1$
	public static final String ACTION_EDIT_PROPERTIES = "editproperties"; //$NON-NLS-1$

	public static final String MODE_NODE_LINK = "mapModeNodeLink"; //$NON-NLS-1$
	public static final String MODE_LINK = "mapModeLink"; //$NON-NLS-1$
	public static final String MODE_CABLE_PATH = "mapModeCablePath"; //$NON-NLS-1$
	public static final String MODE_PATH = "mapModePath"; //$NON-NLS-1$

	public static final String MODE_NODES = "mapModeViewNodes"; //$NON-NLS-1$
	public static final String MODE_INDICATION = "mapModeViewIndication"; //$NON-NLS-1$

	public static final String OPERATION_ZOOM_IN = "mapActionZoomIn"; //$NON-NLS-1$
	public static final String OPERATION_ZOOM_OUT = "mapActionZoomOut"; //$NON-NLS-1$
	public static final String OPERATION_ZOOM_TO_POINT = "mapActionZoomToPoint"; //$NON-NLS-1$
	public static final String OPERATION_ZOOM_BOX = "mapActionZoomBox"; //$NON-NLS-1$
	public static final String OPERATION_CENTER_SELECTION = "mapActionCenterSelection"; //$NON-NLS-1$
	public static final String OPERATION_MOVE_TO_CENTER = "mapActionMoveToCenter"; //$NON-NLS-1$
	public static final String OPERATION_HAND_PAN = "mapActionHandPan"; //$NON-NLS-1$
	public static final String OPERATION_MEASURE_DISTANCE = "mapActionMeasureDistance"; //$NON-NLS-1$
	public static final String OPERATION_MOVE_FIXED = "mapActionMoveFixed"; //$NON-NLS-1$

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

	@Override
	public String toString()
	{
		return getClass().getName();
	}
}
