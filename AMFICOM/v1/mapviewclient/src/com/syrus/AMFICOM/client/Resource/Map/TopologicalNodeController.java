/**
 * $Id: TopologicalNodeController.java,v 1.2 2004/12/08 16:20:22 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import javax.swing.ImageIcon;

/**
 * элемент карты - узел 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/12/08 16:20:22 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class TopologicalNodeController extends AbstractNodeController
{
	public final static Rectangle DEFAULT_BOUNDS = new Rectangle(10, 10);
	public final static Rectangle MIN_BOUNDS = new Rectangle(2, 2);
	public final static Rectangle MAX_BOUNDS = new Rectangle(15, 15);

	public static final String CLOSED_NODE = "node";
	public static final String OPEN_NODE = "void";

	public static final String CLOSED_NODE_IMAGE = "images/node.gif";
	public static final String OPEN_NODE_IMAGE = "images/void.gif";

	static
	{
		MapPropertiesManager.setOriginalImage(OPEN_NODE, new ImageIcon(OPEN_NODE_IMAGE).getImage());
		MapPropertiesManager.setOriginalImage(CLOSED_NODE, new ImageIcon(CLOSED_NODE_IMAGE).getImage());
	}

	private static TopologicalNodeController instance = null;
	
	private TopologicalNodeController()
	{
	}
	
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new TopologicalNodeController();
		return instance;
	}

	public Rectangle getDefaultBounds()
	{
		return DEFAULT_BOUNDS;
	}
	
	public Rectangle getMinBounds()
	{
		return MIN_BOUNDS;
	}
	
	public Rectangle getMaxBounds()
	{
		return MAX_BOUNDS;
	}

	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(!(me instanceof MapPhysicalNodeElement))
			return;
		MapPhysicalNodeElement node = (MapPhysicalNodeElement )me;

		if(!isElementVisible(node, visibleBounds))
			return;
		
		super.paint(node, g, visibleBounds);

		if (node.isCanBind())
		{
			MapCoordinatesConverter converter = getLogicalNetLayer();
			
			Point p = converter.convertMapToScreen(node.getLocation());
	
			Graphics2D pg = (Graphics2D )g;
			
			int width = getBounds(node).width + 20;
			int height = getBounds(node).height + 20;
			
			pg.setStroke(new BasicStroke(MapPropertiesManager.getUnboundThickness()));
			pg.setColor(MapPropertiesManager.getCanBindColor());
			pg.drawRect( 
					p.x - width / 2,
					p.y - height / 2,
					width,
					height);
		}
	}
}
