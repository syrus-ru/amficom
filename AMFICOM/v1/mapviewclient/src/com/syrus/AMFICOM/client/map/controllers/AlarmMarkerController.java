/**
 * $Id: AlarmMarkerController.java,v 1.7 2005/02/18 12:19:45 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
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
 * ���������� ������� ������� �������.
 * @author $Author: krupenn $
 * @version $Revision: 1.7 $, $Date: 2005/02/18 12:19:45 $
 * @module mapviewclient_v1
 */
public final class AlarmMarkerController extends MarkerController
{
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
	 * ������������� ���������� ���� ��� ��� ������ ��������� � ��������� 
	 * �������.
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
		throws MapConnectionException, MapDataException
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
