/**
 * $Id: EventMarkerController.java,v 1.1 2004/12/22 16:38:42 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;

import com.syrus.AMFICOM.Client.Resource.Map.*;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;
import com.syrus.AMFICOM.map.MapElement;

/**
 * элемент карты - узел 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/12/22 16:38:42 $
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
		if(! (me instanceof MapEventMarker))
			return null;
			
		MapEventMarker marker = (MapEventMarker )me;
		
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
			MapPropertiesManager.setOriginalImage(
				getLogicalNetLayer().getImageId(IMAGE_NAME, IMAGE_PATH),
				new ImageIcon(IMAGE_PATH).getImage());
		}
		super.paint(me, g, visibleBounds);
	}


}
