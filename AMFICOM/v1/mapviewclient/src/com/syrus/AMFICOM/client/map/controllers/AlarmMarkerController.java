/*-
 * $$Id: AlarmMarkerController.java,v 1.23 2006/02/15 11:12:33 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.Image;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.AlarmMarker;
import com.syrus.util.Log;

/**
 * Контроллер маркера сигнала тревоги.
 * 
 * @version $Revision: 1.23 $, $Date: 2006/02/15 11:12:33 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class AlarmMarkerController extends MarkerController {
	/**
	 * Имя первой пиктограммы. Пиктограммы маркера сигнала тревоги меняются с
	 * заданным периодом для мигания на карте.
	 */
	public static final String ALARM_IMAGE_NAME = "alarmmarker"; //$NON-NLS-1$
	/** Первая пиктограмма. */
	public static final String ALARM_IMAGE_PATH = "images/alarm_bell_green.gif"; //$NON-NLS-1$

	/**
	 * Имя второй пиктограммы. Пиктограммы маркера сигнала тревоги меняются с
	 * заданным периодом для мигания на карте.
	 */
	public static final String ALARM_IMAGE2_NAME = "alarmmarker2"; //$NON-NLS-1$
	/** Вторая пиктограмма. */
	public static final String ALARM_IMAGE2_PATH = "images/alarm_bell_red.gif"; //$NON-NLS-1$

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

	public static void init() throws ApplicationException {
		if (needInit) {
			alarm1ImageId = NodeTypeController.getImageId(
					AlarmMarkerController.ALARM_IMAGE_NAME,
					AlarmMarkerController.ALARM_IMAGE_PATH);
			alarm2ImageId = NodeTypeController.getImageId(
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
			final String s1 = I18N.getString(MapEditorResourceKeys.ENTITY_ALARM) + " " + marker.getName() //$NON-NLS-1$
					+ " (" + I18N.getString(MapEditorResourceKeys.PATH_LOWERCASE) + " " + marker.getMeasurementPath().getName() + ") " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ I18N.getString(MapEditorResourceKeys.DISTANCE_LOWERCASE) + " - " + distance; //$NON-NLS-1$ //$NON-NLS-2$

			return s1;
		} catch(Exception e) {
			Log.errorMessage(e);
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
