/**
 * $Id: EventMarkerController.java,v 1.10 2005/06/06 12:57:02 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.client.map.controllers;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.EventMarker;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

/**
 * ���������� ������� �������.
 * @author $Author: krupenn $
 * @version $Revision: 1.10 $, $Date: 2005/06/06 12:57:02 $
 * @module mapviewclient_v1
 */
public final class EventMarkerController extends MarkerController {
	/** ��� �����������. */
	public static final String EVENT_IMAGE_NAME = "eventmarker";
	/** �����������. */
	public static final String EVENT_IMAGE_PATH = "images/eventmarker.gif";

	/**
	 * ���� ������������� ���������������� ����������� �������� �������.
	 * ������������� ���������� ���� ��� ��� ������ ��������� � ��������� 
	 * �������.
	 */
	private static boolean needInit = true;

	/**
	 * Instace.
	 */
	private static EventMarkerController instance = null;
	
	/**
	 * Private constructor.
	 */
	private EventMarkerController() {
		// empty
	}

	/**
	 * Get instance.
	 * 
	 * @return instance
	 */
	public static MapElementController getInstance() {
		if(instance == null)
			instance = new EventMarkerController();
		return instance;
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
