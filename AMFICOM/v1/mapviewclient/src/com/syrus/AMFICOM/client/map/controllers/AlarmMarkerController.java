/**
 * $Id: AlarmMarkerController.java,v 1.15 2005/08/11 17:08:10 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
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
 * ���������� ������� ������� �������.
 * @author $Author: arseniy $
 * @version $Revision: 1.15 $, $Date: 2005/08/11 17:08:10 $
 * @module mapviewclient
 */
public final class AlarmMarkerController extends MarkerController {
	/**
	 * ��� ������ �����������. ����������� ������� ������� ������� �������� �
	 * �������� �������� ��� ������� �� �����.
	 */
	public static final String ALARM_IMAGE_NAME = "alarmmarker";
	/** ������ �����������. */
	public static final String ALARM_IMAGE_PATH = "images/alarm_bell_green.gif";

	/**
	 * ��� ������ �����������. ����������� ������� ������� ������� �������� �
	 * �������� �������� ��� ������� �� �����.
	 */
	public static final String ALARM_IMAGE2_NAME = "alarmmarker2";
	/** ������ �����������. */
	public static final String ALARM_IMAGE2_PATH = "images/alarm_bell_red.gif";

	/**
	 * ���� ������������� ���������������� ����������� �������� ������� �������.
	 * ������������� ���������� ���� ��� ��� ������ ��������� � ��������� �������.
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

		final String s1 = LangModelMap.getString("Alarm") + " " + marker.getName()
				+ " (" + LangModelMap.getString("Path_lowercase") + " " + marker.getMeasurementPath().getName() + ") "
				+ LangModelMap.getString("Distance_lowercase") + " - " + super.getFromStartLengthLf(marker);

		return s1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getAlarmedImage(final AbstractNode node) {
		return MapPropertiesManager.getScaledImage(alarm2ImageId);
	}

}
