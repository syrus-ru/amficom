/**
 * $Id: UnboundNodeController.java,v 1.3 2004/12/23 16:58:00 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementController;
import com.syrus.AMFICOM.Client.Resource.Map.SiteNodeController;
import com.syrus.AMFICOM.map.MapElement;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 * элемент карты - узел 
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/12/23 16:58:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class UnboundNodeController extends SiteNodeController
{
	private static UnboundNodeController instance = null;
	
	private UnboundNodeController()
	{
	}
	
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new UnboundNodeController();
		return instance;
	}

	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(!(me instanceof MapUnboundNodeElement))
			return;
		MapUnboundNodeElement unbound = (MapUnboundNodeElement )me;

		if(!isElementVisible(unbound, visibleBounds))
			return;
		
		super.paint(unbound, g, visibleBounds);
		
		MapCoordinatesConverter converter = getLogicalNetLayer();
		
		Point p = converter.convertMapToScreen(unbound.getLocation());

		Graphics2D pg = (Graphics2D )g;
		
		int width = getBounds(unbound).width + 20;
		int height = getBounds(unbound).height + 20;
		
		pg.setStroke(new BasicStroke(MapPropertiesManager.getUnboundThickness()));
		if (unbound.getCanBind())
		{
			pg.setColor(MapPropertiesManager.getCanBindColor());
		}
		else
		{
			pg.setColor(MapPropertiesManager.getUnboundElementColor());
		}
		pg.drawRect( 
				p.x - width / 2,
				p.y - height / 2,
				width,
				height);
	}
}
