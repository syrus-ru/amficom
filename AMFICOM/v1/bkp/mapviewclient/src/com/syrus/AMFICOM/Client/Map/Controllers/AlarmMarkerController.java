/**
 * $Id: AlarmMarkerController.java,v 1.6 2005/02/03 16:24:01 krupenn Exp $
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
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.AlarmMarker;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

/**
 * Контроллер маркера сигнала тревоги.
 * @author $Author: krupenn $
 * @version $Revision: 1.6 $, $Date: 2005/02/03 16:24:01 $
 * @module mapviewclient_v1
 */
public final class AlarmMarkerController extends MarkerController
{
	/**
	 * Имя первой пиктограммы. Пиктограммы маркера сигнала тревоги меняются с
	 * заданным периодом для мигания на карте.
	 */
	public static final String ALARM_IMAGE_NAME = "alarmmarker";
	/** Первая пиктограмма. */
	public static final String ALARM_IMAGE_PATH = "images/alarm_bell_green.gif";

	/**
	 * Имя второй пиктограммы. Пиктограммы маркера сигнала тревоги меняются с
	 * заданным периодом для мигания на карте.
	 */
	public static final String ALARM_IMAGE2_NAME = "alarmmarker2";
	/** Вторая пиктограмма. */
	public static final String ALARM_IMAGE2_PATH = "images/alarm_bell_red.gif";

	/**
	 * Флаг необходимости инициализировать изображения маркеров сигнала тревоги.
	 * Инициализация проводится один раз при первом обращении к отрисовке 
	 * маркера.
	 */
	private static boolean needInit = true;

	/**
	 * Instance.
	 */
	private static AlarmMarkerController instance = null;
	
	/**
	 * Private constructor.
	 */
	private AlarmMarkerController()
	{// empty
	}
	
	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new AlarmMarkerController();
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText(MapElement mapElement)
	{
		if(! (mapElement instanceof AlarmMarker))
			return null;
			
		AlarmMarker marker = (AlarmMarker)mapElement;
		
		String s1 = LangModelMap.getString("Alarm") 
				+ " " + marker.getName() 
				+ " (" + LangModelMap.getString("Path_lowercase") 
				+ " " + marker.getMeasurementPath().getName() 
				+ ") " + LangModelMap.getString("Distance_lowercase") 
				+ " - " + getFromStartLengthLf(marker);

		return s1;
	}

	/**
	 * {@inheritDoc}
	 */
	public Image getAlarmedImage(AbstractNode node)
	{
		Identifier creatorId = getLogicalNetLayer().getUserId();

		return MapPropertiesManager.getScaledImage(
				NodeTypeController.getImageId(
					creatorId, 
					AlarmMarkerController.ALARM_IMAGE2_NAME, 
					AlarmMarkerController.ALARM_IMAGE2_PATH));
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
					AlarmMarkerController.ALARM_IMAGE_NAME, 
					AlarmMarkerController.ALARM_IMAGE_PATH),
				new ImageIcon(AlarmMarkerController.ALARM_IMAGE_PATH).getImage());
			MapPropertiesManager.setOriginalImage(
				NodeTypeController.getImageId(
					creatorId, 
					AlarmMarkerController.ALARM_IMAGE2_NAME, 
					AlarmMarkerController.ALARM_IMAGE2_PATH),
				new ImageIcon(AlarmMarkerController.ALARM_IMAGE2_PATH).getImage());
		}
		super.paint(mapElement, g, visibleBounds);
	}
}
