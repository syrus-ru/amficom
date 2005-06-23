/**
 * $Id: AlarmMarkerController.java,v 1.13 2005/06/23 08:27:18 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.Image;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.AlarmMarker;

/**
 * Контроллер маркера сигнала тревоги.
 * @author $Author: krupenn $
 * @version $Revision: 1.13 $, $Date: 2005/06/23 08:27:18 $
 * @module mapviewclient_v1
 */
public final class AlarmMarkerController extends MarkerController {
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
	private static Identifier alarm1ImageId;
	private static Identifier alarm2ImageId;

	/**
	 * Private constructor.
	 */
	private AlarmMarkerController(NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	public static MapElementController createInstance(NetMapViewer netMapViewer) {
		return new AlarmMarkerController(netMapViewer);
	}

	public static void init(Identifier creatorId) throws ApplicationException {
		if(needInit) {
			alarm1ImageId = NodeTypeController.getImageId(
					creatorId, 
					AlarmMarkerController.ALARM_IMAGE_NAME, 
					AlarmMarkerController.ALARM_IMAGE_PATH);
			alarm2ImageId = NodeTypeController.getImageId(
					creatorId, 
					AlarmMarkerController.ALARM_IMAGE2_NAME, 
					AlarmMarkerController.ALARM_IMAGE2_PATH);

			MapPropertiesManager.setOriginalImage(
				alarm1ImageId,
				new ImageIcon(AlarmMarkerController.ALARM_IMAGE_PATH).getImage());
			MapPropertiesManager.setOriginalImage(
				alarm2ImageId,
				new ImageIcon(AlarmMarkerController.ALARM_IMAGE2_PATH).getImage());
				
			needInit = false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText(MapElement mapElement) {
		if(!(mapElement instanceof AlarmMarker))
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
	public Image getAlarmedImage(AbstractNode node) {
		return MapPropertiesManager.getScaledImage(alarm2ImageId);
	}

}
