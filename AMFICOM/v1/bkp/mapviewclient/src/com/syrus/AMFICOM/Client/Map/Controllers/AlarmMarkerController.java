/**
 * $Id: AlarmMarkerController.java,v 1.2 2004/12/30 16:17:48 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.mapview.AlarmMarker;
import com.syrus.AMFICOM.Client.Map.mapview.MeasurementPath;

/**
 * элемент карты - узел 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/12/30 16:17:48 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class AlarmMarkerController extends MarkerController
{
	public static final String IMAGE_NAME = "alarmmarker";
	public static final String IMAGE_PATH = "images/alarm_bell_green.gif";

	public static final String IMAGE2_NAME = "alarmmarker2";
	public static final String IMAGE2_PATH = "images/alarm_bell_red.gif";

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
		Identifier creatorId = new Identifier(
			getLogicalNetLayer().getContext().getSessionInterface().getAccessIdentifier().user_id);

		return MapPropertiesManager.getScaledImage(
				NodeTypeController.getImageId(creatorId, IMAGE2_NAME, IMAGE2_PATH));
	}

	public void paint(MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(needInit)
		{
			Identifier creatorId = new Identifier(
				getLogicalNetLayer().getContext().getSessionInterface().getAccessIdentifier().user_id);

			MapPropertiesManager.setOriginalImage(
				NodeTypeController.getImageId(creatorId, IMAGE_NAME, IMAGE_PATH),
				new ImageIcon(IMAGE_PATH).getImage());
			MapPropertiesManager.setOriginalImage(
				NodeTypeController.getImageId(creatorId, IMAGE2_NAME, IMAGE2_PATH),
				new ImageIcon(IMAGE2_PATH).getImage());
		}
		super.paint(me, g, visibleBounds);
	}
}
