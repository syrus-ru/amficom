/**
 * $Id: TopologicalNodeController.java,v 1.4 2005/01/21 16:19:57 krupenn Exp $
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
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.TopologicalNode;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;
import com.syrus.AMFICOM.Client.Map.Controllers.MapElementController;
import com.syrus.AMFICOM.Client.Map.Controllers.AbstractNodeController;

/**
 * элемент карты - узел 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2005/01/21 16:19:57 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class TopologicalNodeController extends AbstractNodeController
{
	public static final Rectangle DEFAULT_BOUNDS = new Rectangle(10, 10);
	public static final Rectangle MIN_BOUNDS = new Rectangle(2, 2);
	public static final Rectangle MAX_BOUNDS = new Rectangle(15, 15);

	public static final String CLOSED_NODE = "node";
	public static final String OPEN_NODE = "void";

	public static final String CLOSED_NODE_IMAGE = "images/node.gif";
	public static final String OPEN_NODE_IMAGE = "images/void.gif";

	private static final String PROPERTY_PANE_CLASS_NAME = "";

	private static boolean needInit = true;

	private static TopologicalNodeController instance = null;

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
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

	public void setActive(TopologicalNode node, boolean active)
	{
		Identifier creatorId = new Identifier(
			getLogicalNetLayer().getContext().getSessionInterface().getAccessIdentifier().user_id);

		node.setActive(active);
		if(active)
			node.setImageId(NodeTypeController.getImageId(creatorId, CLOSED_NODE, CLOSED_NODE_IMAGE));
		else
			node.setImageId(NodeTypeController.getImageId(creatorId, OPEN_NODE, OPEN_NODE_IMAGE));
	}

	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(needInit)
		{
			Identifier creatorId = new Identifier(
				getLogicalNetLayer().getContext().getSessionInterface().getAccessIdentifier().user_id);

			MapPropertiesManager.setOriginalImage(
				NodeTypeController.getImageId(creatorId, OPEN_NODE, OPEN_NODE_IMAGE),
				new ImageIcon(OPEN_NODE_IMAGE).getImage());
			MapPropertiesManager.setOriginalImage(
				NodeTypeController.getImageId(creatorId, CLOSED_NODE, CLOSED_NODE_IMAGE), 
				new ImageIcon(CLOSED_NODE_IMAGE).getImage());
		}

		if(!(me instanceof TopologicalNode))
			return;
		TopologicalNode node = (TopologicalNode)me;

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
