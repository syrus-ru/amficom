/**
 * $Id: MapEvent.java,v 1.10 2005/02/07 17:00:54 krupenn Exp $
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
 * @version $Revision: 1.10 $, $Date: 2005/02/07 17:00:54 $
 * @module mapclient_v2
 * @author $Author: krupenn $
 */
public class MapEvent extends OperationEvent 
{
	/** Открыто окно карты. */
	public static final String MAP_FRAME_SHOWN = "mapframeshownevent";

	/** Карта закрыта. */
	public static final String MAP_VIEW_CLOSED = "mapviewclosedevent";
	/** Окно с картой активировано. */
	public static final String MAP_VIEW_SELECTED = "mapviewselectedevent";
	/** Окно карты деактивировано. */
	public static final String MAP_VIEW_DESELECTED = "mapviewdeselectedevent";
	/** Содержимое вида изменилось. */
	public static final String MAP_VIEW_CHANGED = "mapviewchangedevent";
	/** Поменялись координаты курсора на карте. */
	public static final String MAP_VIEW_CENTER_CHANGED = "maplatlong";
	/** Поменялся масштаб на карте. */
	public static final String MAP_VIEW_SCALE_CHANGED = "mapscale";

	/** Окно с картой активировано. */
	public static final String MAP_SELECTED = "mapselectedevent";
	/** Окно карты деактивировано. */
	public static final String MAP_DESELECTED = "mapdeselectedevent";
	/** Содержимое карты изменилось. */
	public static final String MAP_CHANGED = "mapchangedevent";

	/** Управление элементами на карте. */
	public static final String MAP_NAVIGATE = "mapnavigateevent";
	/** Поместить элемент на карту. */
	public static final String PLACE_ELEMENT = "placeelement";

	/** Выделен элемент карты. */
	public static final String MAP_ELEMENT_SELECTED = "mapelementselectedevent";
	/** Снято выделение элемента карты. */
	public static final String MAP_ELEMENT_DESELECTED = "mapelementdeselectedevent";
	/** Изменен элемент карты. */
	public static final String MAP_ELEMENT_CHANGED = "mapelementchangedevent";

	/** Изменился список выбранных элементов на карте. */
	public static final String SELECTION_CHANGED = "mapselectionchangedevent";

	/** Необходимо перерисовать карту. */
	public static final String NEED_REPAINT = "needrepaint";
	/** Необходимо перерисовать карту, включая картографические объекты. */
	public static final String NEED_FULL_REPAINT = "needfullrepaint";

	/** Снять выделение со всех объектов. */
	public static final String DESELECT_ALL = "deselectall";
	
	public MapEvent(Object source, String type)
	{	
		super(source, 0, type);
	}
}
