/*-
 * $Id: MapEvent.java,v 1.14 2005/09/29 11:32:36 krupenn Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.event;

import java.beans.PropertyChangeEvent;

/**
 * Событие карты
 * 
 * @version $Revision: 1.14 $, $Date: 2005/09/29 11:32:36 $
 * @module mapclient
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 */
public class MapEvent extends PropertyChangeEvent {
	private static final long serialVersionUID = -837303529240341674L;

	public static final String MAP_EVENT_TYPE = "mapevent"; //$NON-NLS-1$
	/** Открыто окно карты. */
	public static final String MAP_FRAME_SHOWN = "mapframeshownevent"; //$NON-NLS-1$

	/** Карта закрыта. */
	public static final String MAP_VIEW_CLOSED = "mapviewclosedevent"; //$NON-NLS-1$
	/** Окно с картой активировано. */
	public static final String MAP_VIEW_SELECTED = "mapviewselectedevent"; //$NON-NLS-1$
	/** Окно карты деактивировано. */
	public static final String MAP_VIEW_DESELECTED = "mapviewdeselectedevent"; //$NON-NLS-1$
	/** Содержимое вида изменилось. */
	public static final String MAP_VIEW_CHANGED = "mapviewchangedevent"; //$NON-NLS-1$
	/** Поменялись координаты курсора на карте. */
	public static final String MAP_VIEW_CENTER_CHANGED = "maplatlong"; //$NON-NLS-1$
	/** Поменялся масштаб на карте. */
	public static final String MAP_VIEW_SCALE_CHANGED = "mapscale"; //$NON-NLS-1$

	/** Окно с картой активировано. */
	public static final String MAP_SELECTED = "mapselectedevent"; //$NON-NLS-1$
	/** Окно карты деактивировано. */
	public static final String MAP_DESELECTED = "mapdeselectedevent"; //$NON-NLS-1$
	/** Содержимое карты изменилось. */
	public static final String MAP_CHANGED = "mapchangedevent"; //$NON-NLS-1$

	public static final String OTHER_SELECTED = "otherselectedevent"; //$NON-NLS-1$

	/** Поместить элемент на карту. */
	public static final String PLACE_ELEMENT = "placeelement"; //$NON-NLS-1$

	/** Изменен элемент карты. */
	public static final String MAP_ELEMENT_CHANGED = "mapelementchangedevent"; //$NON-NLS-1$

	/** Изменился список выбранных элементов на карте. */
	public static final String SELECTION_CHANGED = "mapselectionchangedevent"; //$NON-NLS-1$

	/** Изменился список выбранных элементов на карте. */
	public static final String UPDATE_SELECTION = "mapupdateselectionevent"; //$NON-NLS-1$

	/** Изменился список подключенных библиотек. */
	public static final String LIBRARY_SET_CHANGED = "maplibrarysetchangedevent"; //$NON-NLS-1$

	/** Необходимо перерисовать карту. */
	public static final String DO_SELECT = "doselect"; //$NON-NLS-1$

	/** Необходимо перерисовать карту. */
	public static final String NEED_REPAINT = "needrepaint"; //$NON-NLS-1$
	/** Необходимо перерисовать карту, включая картографические объекты. */
	public static final String NEED_FULL_REPAINT = "needfullrepaint"; //$NON-NLS-1$

	public static final String TOPOLOGY_CHANGED = "topologychanged"; //$NON-NLS-1$

	/** Необходимо перерисовать карту. */
	public static final String MAP_REPAINTED = "maprepainted"; //$NON-NLS-1$

	/** Копировать тип узла или линии. */
	public static final String COPY_TYPE = "copytype"; //$NON-NLS-1$

	private final String mapEventType;

	public MapEvent(Object source, String type) {
		super(source, MAP_EVENT_TYPE, null, null);
		this.mapEventType = type;
	}

	public MapEvent(Object source, String type, Object newValue) {	
		super(source, MAP_EVENT_TYPE, null, newValue);
		this.mapEventType = type;
	}

	public String getMapEventType() {
		return this.mapEventType;
	}
}
