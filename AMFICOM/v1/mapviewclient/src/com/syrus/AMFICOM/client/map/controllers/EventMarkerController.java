/**
 * $Id: EventMarkerController.java,v 1.3 2005/01/31 12:19:18 krupenn Exp $
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
import com.syrus.AMFICOM.map.MapElement;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.EventMarker;

/**
 * элемент карты - узел 
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2005/01/31 12:19:18 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class EventMarkerController extends MarkerController
{
	public static final String IMAGE_NAME = "eventmarker";
	public static final String IMAGE_PATH = "images/eventmarker.gif";

	private static boolean needInit = true;

	private static EventMarkerController instance = null;
	
	private EventMarkerController()
	{
	}
	
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new EventMarkerController();
		return instance;
	}

	public String getToolTipText(MapElement me)
	{
		if(! (me instanceof EventMarker))
			return null;
			
		EventMarker marker = (EventMarker)me;
		
		String s1 = LangModelMap.getString("Event") 
			+ " " + marker.getName() 
			+ " (" + LangModelMap.getString("Path_lowercase")
			+ " " + marker.getMeasurementPath().getName() + ")";

		return s1;
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
		}
		super.paint(me, g, visibleBounds);
	}


}
