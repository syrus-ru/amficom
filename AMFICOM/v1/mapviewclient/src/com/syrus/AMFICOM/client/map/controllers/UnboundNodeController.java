/**
 * $Id: UnboundNodeController.java,v 1.3 2005/02/03 16:24:01 krupenn Exp $
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
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.UnboundNode;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 * Контроллер непривязанного узела (элемент схемы).
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/02/03 16:24:01 $
 * @module mapviewclient_v1
 */
public class UnboundNodeController extends SiteNodeController
{
	private static final String PROPERTY_PANE_CLASS_NAME = 
			"";

	/**
	 * Instace.
	 */
	private static UnboundNodeController instance = null;
	
	/**
	 * Private constructor.
	 */
	private UnboundNodeController()
	{// empty
	}
	
	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new UnboundNodeController();
		return instance;
	}

	/**
	 * Получить имя класса панели, описывающей свойства кабельного пути.
	 * @return имя класса
	 */
	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint (MapElement mapElement, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(!(mapElement instanceof UnboundNode))
			return;
		UnboundNode unbound = (UnboundNode)mapElement;

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
