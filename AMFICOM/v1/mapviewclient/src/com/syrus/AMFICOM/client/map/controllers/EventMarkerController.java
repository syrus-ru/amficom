/*-
 * $$Id: EventMarkerController.java,v 1.20 2005/10/11 08:56:11 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.controllers;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.EventMarker;

/**
 * Контроллер маркера события.
 * 
 * @version $Revision: 1.20 $, $Date: 2005/10/11 08:56:11 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
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

	public static void init() throws ApplicationException {
		if (needInit) {
			eventImageId = NodeTypeController.getImageId(
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
		
		final String s1 = I18N.getString(MapEditorResourceKeys.ENTITY_EVENT) + " " + marker.getName()  //$NON-NLS-1$
			+ " (" + I18N.getString(MapEditorResourceKeys.PATH_LOWERCASE) + " " + marker.getMeasurementPath().getName() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		return s1;
	}

}
