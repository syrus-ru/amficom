/**
 * $Id: MapEvent.java,v 1.1 2004/09/13 12:00:25 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.General.Event;

import com.syrus.AMFICOM.Client.General.Event.OperationEvent;

/**
 * ������� �����
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:00:25 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapEvent extends OperationEvent 
{
	/** ����� ������� */
	public static final String MAP_CLOSED = "mapclosedevent";
	/** ����� ������� */
	public static final String MAP_OPENED = "mapopenedevent";
	/** ���� � ������ ������������ */
	public static final String MAP_SELECTED = "mapselectedevent";
	/** ���� ����� �������������� */
	public static final String MAP_DESELECTED = "mapdeselectedevent";
	/** ���������� ���������� �� ����� */
	public static final String MAP_NAVIGATE = "mapnavigateevent";
	/** ���������� ����� ���������� */
	public static final String MAP_CHANGED = "mapchangedevent";
	/** ���������� ���� ���������� */
	public static final String MAP_VIEW_CHANGED = "mapviewchangedevent";
	/** ��������� ������� �� ����� */
	public static final String PLACE_ELEMENT = "placeelement";
	/** ��������� ������ ��������� ��������� �� ����� */
	public static final String SELECTION_CHANGED = "mapselectionchangedevent";
	/** ���������� ���������� ������� �� ����� */
	public static final String MAP_CENTER_CHANGED = "maplatlong";
	/** ������� ���� ����� */
	public static final String MAP_FRAME_SHOWN = "mapframeshownevent";
	/** ������� ������� ����� */
	public static final String MAP_ELEMENT_SELECTED = "mapelementselectedevent";
	/** ����� ��������� �������� ����� */
	public static final String MAP_ELEMENT_DESELECTED = "mapelementdeselectedevent";
	
	public MapEvent(Object source, String type)
	{	
		super(source, 0, type);
	}
}
