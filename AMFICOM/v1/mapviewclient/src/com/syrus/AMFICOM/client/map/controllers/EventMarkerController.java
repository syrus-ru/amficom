/**
 * $Id: EventMarkerController.java,v 1.16 2005/09/16 14:53:34 krupenn Exp $
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
 * @version $Revision: 1.16 $, $Date: 2005/09/16 14:53:34 $
 * @module mapviewclient
 */
public final class EventMarkerController extends MarkerController {
	/** Имя пиктограммы. */
	public static final String EVENT_IMAGE_NAME = "eventmarker"; //$NON-NLS-1$
	/** Пиктограмма. */
	public static final String EVENT_IMAGE_PATH = "images/eventmarker.gif"; //$NON-NLS-1$

	/**
	 * Флаг необходимости инициализировать изображения маркеров событий.
	 * Инициализация проводится один раз при первом обращении к отрисовке маркера.
	 */
	private static boolean needInit = true;

	private static Identifier eventImageId;

	/**
	 * Private constructor.
	 */
	private EventMarkerController(final NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	public static MapElementController createInstance(final NetMapViewer netMapViewer) {
		return new EventMarkerController(netMapViewer);
	}

	public static void init(final Identifier creatorId) throws ApplicationException {
		if (needInit) {
			eventImageId = NodeTypeController.getImageId(creatorId,
					EventMarkerController.EVENT_IMAGE_NAME,
					EventMarkerController.EVENT_IMAGE_PATH);

			MapPropertiesManager.setOriginalImage(eventImageId, new ImageIcon(EventMarkerController.EVENT_IMAGE_PATH).getImage());

			needInit = false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getToolTipText(final MapElement mapElement) {
		if(!(mapElement instanceof EventMarker)) {
			return null;
		}

		final EventMarker marker = (EventMarker )mapElement;
		
		final String s1 = LangModelMap.getString("Event") + " " + marker.getName()  //$NON-NLS-1$ //$NON-NLS-2$
			+ " (" + LangModelMap.getString("Path_lowercase") + " " + marker.getMeasurementPath().getName() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		return s1;
	}

}
