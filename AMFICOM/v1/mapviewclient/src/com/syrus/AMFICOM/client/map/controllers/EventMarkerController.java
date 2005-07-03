/**
 * $Id: EventMarkerController.java,v 1.13 2005/06/23 08:27:18 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.controllers;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.EventMarker;

/**
 * Контроллер маркера события.
 * @author $Author: krupenn $
 * @version $Revision: 1.13 $, $Date: 2005/06/23 08:27:18 $
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

	private static Identifier eventImageId;

	/**
	 * Private constructor.
	 */
	private EventMarkerController(NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	public static MapElementController createInstance(NetMapViewer netMapViewer) {
		return new EventMarkerController(netMapViewer);
	}

	public static void init(Identifier creatorId) throws ApplicationException {
		if(needInit) {
			eventImageId = NodeTypeController.getImageId(
					creatorId, 
					EventMarkerController.EVENT_IMAGE_NAME, 
					EventMarkerController.EVENT_IMAGE_PATH);

			MapPropertiesManager.setOriginalImage(
				eventImageId,
				new ImageIcon(EventMarkerController.EVENT_IMAGE_PATH).getImage());
				
			needInit = false;
		}
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

}
