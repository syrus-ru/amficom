/**
 * $Id: MapEvent.java,v 1.1 2004/09/13 12:00:25 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.General.Event;

import com.syrus.AMFICOM.Client.General.Event.OperationEvent;

/**
 * Событие карты
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
	/** карта закрыта */
	public static final String MAP_CLOSED = "mapclosedevent";
	/** карта открыта */
	public static final String MAP_OPENED = "mapopenedevent";
	/** окно с картой активировано */
	public static final String MAP_SELECTED = "mapselectedevent";
	/** окно карты деактивировано */
	public static final String MAP_DESELECTED = "mapdeselectedevent";
	/** управление элементами на карте */
	public static final String MAP_NAVIGATE = "mapnavigateevent";
	/** содержимое карты изменилось */
	public static final String MAP_CHANGED = "mapchangedevent";
	/** содержимое вида изменилось */
	public static final String MAP_VIEW_CHANGED = "mapviewchangedevent";
	/** поместить элемент на карту */
	public static final String PLACE_ELEMENT = "placeelement";
	/** изменился список выбранных элементов на карте */
	public static final String SELECTION_CHANGED = "mapselectionchangedevent";
	/** поменялись координаты курсора на карте */
	public static final String MAP_CENTER_CHANGED = "maplatlong";
	/** открыто окно карты */
	public static final String MAP_FRAME_SHOWN = "mapframeshownevent";
	/** выделен элемент карты */
	public static final String MAP_ELEMENT_SELECTED = "mapelementselectedevent";
	/** снято выделение элемента карты */
	public static final String MAP_ELEMENT_DESELECTED = "mapelementdeselectedevent";
	
	public MapEvent(Object source, String type)
	{	
		super(source, 0, type);
	}
}
