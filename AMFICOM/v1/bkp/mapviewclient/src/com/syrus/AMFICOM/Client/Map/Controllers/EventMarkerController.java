/**
 * $Id: EventMarkerController.java,v 1.6 2005/02/03 16:24:01 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.EventMarker;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

/**
 * Контроллер маркера события.
 * @author $Author: krupenn $
 * @version $Revision: 1.6 $, $Date: 2005/02/03 16:24:01 $
 * @module mapviewclient_v1
 */
public final class EventMarkerController extends MarkerController
{
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
	private static EventMarkerController instance = null;
	
	/**
	 * Private constructor.
	 */
	private EventMarkerController()
	{// empty
	}
	
	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new EventMarkerController();
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText(MapElement mapElement)
	{
		if(! (mapElement instanceof EventMarker))
			return null;
			
		EventMarker marker = (EventMarker)mapElement;
		
		String s1 = LangModelMap.getString("Event") 
			+ " " + marker.getName() 
			+ " (" + LangModelMap.getString("Path_lowercase")
			+ " " + marker.getMeasurementPath().getName() + ")";

		return s1;
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(MapElement mapElement, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(needInit)
		{
			Identifier creatorId = getLogicalNetLayer().getUserId();

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
