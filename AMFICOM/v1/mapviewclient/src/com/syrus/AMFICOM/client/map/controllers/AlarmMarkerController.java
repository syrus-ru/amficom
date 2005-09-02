/**
 * $Id: AlarmMarkerController.java,v 1.16 2005/09/02 09:33:50 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.Image;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.client.map.MapException;
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
 * @version $Revision: 1.16 $, $Date: 2005/09/02 09:33:50 $
 * @module mapviewclient
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
	 * Инициализация проводится один раз при первом обращении к отрисовке маркера.
	 */
	private static boolean needInit = true;
	private static Identifier alarm1ImageId;
	private static Identifier alarm2ImageId;

	/**
	 * Private constructor.
	 */
	private AlarmMarkerController(final NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	public static MapElementController createInstance(final NetMapViewer netMapViewer) {
		return new AlarmMarkerController(netMapViewer);
	}

	public static void init(final Identifier creatorId) throws ApplicationException {
		if (needInit) {
			alarm1ImageId = NodeTypeController.getImageId(creatorId,
					AlarmMarkerController.ALARM_IMAGE_NAME,
					AlarmMarkerController.ALARM_IMAGE_PATH);
			alarm2ImageId = NodeTypeController.getImageId(creatorId,
					AlarmMarkerController.ALARM_IMAGE2_NAME,
					AlarmMarkerController.ALARM_IMAGE2_PATH);

			MapPropertiesManager.setOriginalImage(alarm1ImageId, new ImageIcon(AlarmMarkerController.ALARM_IMAGE_PATH).getImage());
			MapPropertiesManager.setOriginalImage(alarm2ImageId, new ImageIcon(AlarmMarkerController.ALARM_IMAGE2_PATH).getImage());

			needInit = false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getToolTipText(final MapElement mapElement) {
		if (!(mapElement instanceof AlarmMarker)) {
			return null;
		}

		final AlarmMarker marker = (AlarmMarker) mapElement;

		try {
			double distance = super.getFromStartLengthLf(marker);
			final String s1 = LangModelMap.getString("Alarm") + " " + marker.getName()
					+ " (" + LangModelMap.getString("Path_lowercase") + " " + marker.getMeasurementPath().getName() + ") "
					+ LangModelMap.getString("Distance_lowercase") + " - " + distance;

			return s1;
		} catch(MapException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getAlarmedImage(final AbstractNode node) {
		return MapPropertiesManager.getScaledImage(alarm2ImageId);
	}

}
