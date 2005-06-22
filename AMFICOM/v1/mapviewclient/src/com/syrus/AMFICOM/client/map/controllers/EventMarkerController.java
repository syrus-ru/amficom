/**
 * $Id: EventMarkerController.java,v 1.12 2005/06/22 08:43:48 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.EventMarker;

/**
 * Контроллер маркера события.
 * @author $Author: krupenn $
 * @version $Revision: 1.12 $, $Date: 2005/06/22 08:43:48 $
 * @module mapviewclient_v1
 */
public final class EventMarkerController extends MarkerController {
	/** Имя пиктограммы. */
	public static final String EVENT_IMAGE_NAME = "eventmarker";
	/** Пиктограмма. */
	public static final String EVENT_IMAGE_PATH = "images/eventmarker.gif";

	/**
	 * Флаг необходимости инициализировать изображения маркеров событий.
	 * Инициализация проводится один раз при первом обращении к отрисовке 
	 * маркера.
	 */
	private static boolean needInit = true;

	/**
	 * Instace.
	 */
//	private static EventMarkerController instance = null;
	
	/**
	 * Private constructor.
	 */
	private EventMarkerController(NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	/**
	 * Get instance.
	 * 
	 * @return instance
	 */
//	public static MapElementController getInstance() {
//		return instance;
//	}

	public static MapElementController createInstance(NetMapViewer netMapViewer) {
		return new EventMarkerController(netMapViewer);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText(MapElement mapElement) {
		if(!(mapElement instanceof EventMarker))
			return null;

		EventMarker marker = (EventMarker )mapElement;
		
		String s1 = LangModelMap.getString("Event") 
			+ " " + marker.getName() 
			+ " (" + LangModelMap.getString("Path_lowercase")
			+ " " + marker.getMeasurementPath().getName() + ")";

		return s1;
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(
			MapElement mapElement,
			Graphics g,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		if(needInit) {
			Identifier creatorId = LoginManager.getUserId();

			MapPropertiesManager.setOriginalImage(
				NodeTypeController.getImageId(
					creatorId, 
					EventMarkerController.EVENT_IMAGE_NAME, 
					EventMarkerController.EVENT_IMAGE_PATH),
				new ImageIcon(EVENT_IMAGE_PATH).getImage());
		}
		super.paint(mapElement, g, visibleBounds);
	}


}
