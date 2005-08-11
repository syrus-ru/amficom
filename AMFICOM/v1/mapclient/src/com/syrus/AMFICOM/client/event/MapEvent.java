/**
 * $Id: MapEvent.java,v 1.6 2005/08/11 09:07:42 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.event;

import java.beans.PropertyChangeEvent;

/**
 * Событие карты
 * 
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2005/08/11 09:07:42 $
 * @module mapclient_v2
 * @author $Author: arseniy $
 */
public class MapEvent extends PropertyChangeEvent {
	private static final long serialVersionUID = -837303529240341674L;

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

	public static final String OTHER_SELECTED = "otherselectedevent";

	/** Управление элементами на карте. */
	public static final String MAP_NAVIGATE = "mapnavigateevent";
	/** Поместить элемент на карту. */
	public static final String PLACE_ELEMENT = "placeelement";

	/** Изменен элемент карты. */
	public static final String MAP_ELEMENT_CHANGED = "mapelementchangedevent";

	/** Изменился список выбранных элементов на карте. */
	public static final String SELECTION_CHANGED = "mapselectionchangedevent";

	/** Изменился список подключенных библиотек. */
	public static final String LIBRARY_SET_CHANGED = "maplibrarysetchangedevent";

	/** Необходимо перерисовать карту. */
	public static final String NEED_REPAINT = "needrepaint";
	/** Необходимо перерисовать карту, включая картографические объекты. */
	public static final String NEED_FULL_REPAINT = "needfullrepaint";

	/** Снять выделение со всех объектов. */
	public static final String DESELECT_ALL = "deselectall";
	
	/** Необходимо выделить объекты. */
	public static final String NEED_SELECT = "needselect";

	/** Необходимо снять выделение с объектов. */
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
