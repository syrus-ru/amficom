/**
 * $Id: MapEvent.java,v 1.8 2004/12/22 16:09:48 krupenn Exp $
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
 * @version $Revision: 1.8 $, $Date: 2004/12/22 16:09:48 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapEvent extends OperationEvent 
{
	/** открыто окно карты */
	public static final String MAP_FRAME_SHOWN = "mapframeshownevent";

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
	/** поменялся масштаб на карте */
	public static final String MAP_VIEW_SCALE_CHANGED = "mapscale";

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
	/** изменен элемент карты */
	public static final String MAP_ELEMENT_CHANGED = "mapelementchangedevent";

	/** изменился список выбранных элементов на карте */
	public static final String SELECTION_CHANGED = "mapselectionchangedevent";
	
	public MapEvent(Object source, String type)
	{	
		super(source, 0, type);
	}
}
