/**
 * $Id: MapEvent.java,v 1.3 2004/10/04 15:58:19 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.General.Event;

/**
 * Событие карты
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/04 15:58:19 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapEvent extends OperationEvent 
{
	/** открыто окно карты */
	public static final String MAP_FRAME_SHOWN = "mapframeshownevent";

	/** карта открыта */
//	public static final String MAP_VIEW_OPENED = "mapviewopenedevent";
	/** карта закрыта */
	public static final String MAP_VIEW_CLOSED = "mapviewclosedevent";
	/** окно с картой активировано */
	public static final String MAP_VIEW_SELECTED = "mapviewselectedevent";
	/** окно карты деактивировано */
	public static final String MAP_VIEW_DESELECTED = "mapviewdeselectedevent";
	/** содержимое вида изменилось */
	public static final String MAP_VIEW_CHANGED = "mapviewchangedevent";
	/** поменялись координаты курсора на карте */
	public static final String MAP_VIEW_CENTER_CHANGED = "maplatlong";

	/** окно с картой активировано */
	public static final String MAP_SELECTED = "mapselectedevent";
	/** окно карты деактивировано */
	public static final String MAP_DESELECTED = "mapdeselectedevent";
	/** содержимое карты изменилось */
	public static final String MAP_CHANGED = "mapchangedevent";

	/** управление элементами на карте */
	public static final String MAP_NAVIGATE = "mapnavigateevent";
	/** поместить элемент на карту */
	public static final String PLACE_ELEMENT = "placeelement";

	/** выделен элемент карты */
	public static final String MAP_ELEMENT_SELECTED = "mapelementselectedevent";
	/** снято выделение элемента карты */
	public static final String MAP_ELEMENT_DESELECTED = "mapelementdeselectedevent";
	/** изменился список выбранных элементов на карте */
	public static final String SELECTION_CHANGED = "mapselectionchangedevent";
	
	public MapEvent(Object source, String type)
	{	
		super(source, 0, type);
	}
}
