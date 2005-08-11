/**
 * $Id: MapEvent.java,v 1.6 2005/08/11 09:07:42 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.client.event;

import java.beans.PropertyChangeEvent;

/**
 * ������� �����
 * 
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2005/08/11 09:07:42 $
 * @module mapclient_v2
 * @author $Author: arseniy $
 */
public class MapEvent extends PropertyChangeEvent {
	private static final long serialVersionUID = -837303529240341674L;

	/** ������� ���� �����. */
	public static final String MAP_FRAME_SHOWN = "mapframeshownevent";

	/** ����� �������. */
	public static final String MAP_VIEW_CLOSED = "mapviewclosedevent";
	/** ���� � ������ ������������. */
	public static final String MAP_VIEW_SELECTED = "mapviewselectedevent";
	/** ���� ����� ��������������. */
	public static final String MAP_VIEW_DESELECTED = "mapviewdeselectedevent";
	/** ���������� ���� ����������. */
	public static final String MAP_VIEW_CHANGED = "mapviewchangedevent";
	/** ���������� ���������� ������� �� �����. */
	public static final String MAP_VIEW_CENTER_CHANGED = "maplatlong";
	/** ��������� ������� �� �����. */
	public static final String MAP_VIEW_SCALE_CHANGED = "mapscale";

	/** ���� � ������ ������������. */
	public static final String MAP_SELECTED = "mapselectedevent";
	/** ���� ����� ��������������. */
	public static final String MAP_DESELECTED = "mapdeselectedevent";
	/** ���������� ����� ����������. */
	public static final String MAP_CHANGED = "mapchangedevent";

	public static final String OTHER_SELECTED = "otherselectedevent";

	/** ���������� ���������� �� �����. */
	public static final String MAP_NAVIGATE = "mapnavigateevent";
	/** ��������� ������� �� �����. */
	public static final String PLACE_ELEMENT = "placeelement";

	/** ������� ������� �����. */
	public static final String MAP_ELEMENT_CHANGED = "mapelementchangedevent";

	/** ��������� ������ ��������� ��������� �� �����. */
	public static final String SELECTION_CHANGED = "mapselectionchangedevent";

	/** ��������� ������ ������������ ���������. */
	public static final String LIBRARY_SET_CHANGED = "maplibrarysetchangedevent";

	/** ���������� ������������ �����. */
	public static final String NEED_REPAINT = "needrepaint";
	/** ���������� ������������ �����, ������� ���������������� �������. */
	public static final String NEED_FULL_REPAINT = "needfullrepaint";

	/** ����� ��������� �� ���� ��������. */
	public static final String DESELECT_ALL = "deselectall";
	
	/** ���������� �������� �������. */
	public static final String NEED_SELECT = "needselect";

	/** ���������� ����� ��������� � ��������. */
	public static final String NEED_DESELECT = "needdeselect";

	public MapEvent(Object source, String type)
	{	
		super(source, type, null, null);
	}

	public MapEvent(Object source, String type, Object newValue)
	{	
		super(source, type, null, newValue);
	}
}
