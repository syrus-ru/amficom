/**
 * $Id: AlarmMarkerController.java,v 1.5 2005/02/02 09:05:10 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Controllers.MapElementController;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;
import com.syrus.AMFICOM.mapview.AlarmMarker;
import com.syrus.AMFICOM.mapview.MeasurementPath;

/**
 * элемент карты - узел 
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2005/02/02 09:05:10 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class AlarmMarkerController extends MarkerController
{
	public static final String ALARM_IMAGE_NAME = "alarmmarker";
	public static final String ALARM_IMAGE_PATH = "images/alarm_bell_green.gif";

	public static final String ALARM_IMAGE2_NAME = "alarmmarker2";
	public static final String ALARM_IMAGE2_PATH = "images/alarm_bell_red.gif";

	private static boolean needInit = true;

	private static AlarmMarkerController instance = null;
	
	private AlarmMarkerController()
	{
	}
	
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new AlarmMarkerController();
		return instance;
	}

	public String getToolTipText(MapElement me)
	{
		if(! (me instanceof AlarmMarker))
			return null;
			
		AlarmMarker marker = (AlarmMarker)me;
		
		String s1 = LangModelMap.getString("Alarm") 
				+ " " + marker.getName() 
				+ " (" + LangModelMap.getString("Path_lowercase") 
				+ " " + marker.getMeasurementPath().getName() 
				+ ") " + LangModelMap.getString("Distance_lowercase") 
				+ " - " + getFromStartLengthLf(marker);

		return s1;
	}

	public Image getAlarmedImage(AbstractNode node)
	{
		Identifier creatorId = getLogicalNetLayer().getUserId();

		return MapPropertiesManager.getScaledImage(
				NodeTypeController.getImageId(creatorId, ALARM_IMAGE2_NAME, ALARM_IMAGE2_PATH));
	}

	public void paint(MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(needInit)
		{
			Identifier creatorId = getLogicalNetLayer().getUserId();

			MapPropertiesManager.setOriginalImage(
				NodeTypeController.getImageId(creatorId, ALARM_IMAGE_NAME, ALARM_IMAGE_PATH),
				new ImageIcon(ALARM_IMAGE_PATH).getImage());
			MapPropertiesManager.setOriginalImage(
				NodeTypeController.getImageId(creatorId, ALARM_IMAGE2_NAME, ALARM_IMAGE2_PATH),
				new ImageIcon(ALARM_IMAGE2_PATH).getImage());
		}
		super.paint(me, g, visibleBounds);
	}
}
