/**
 * $Id: UnboundNodeController.java,v 1.2 2005/01/31 12:19:18 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.map.MapElement;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 * Элемент карты - непривязанный узел (элемент схемы).
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2005/01/31 12:19:18 $
 * @module mapviewclient_v1
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

	private static final String PROPERTY_PANE_CLASS_NAME = 
			"";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}


	/**
	 * {@inheritDoc}
	 */
	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(!(me instanceof UnboundNode))
			return;
		UnboundNode unbound = (UnboundNode)me;

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
