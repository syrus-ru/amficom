/**
 * $Id: TopologicalNodeController.java,v 1.3 2004/12/22 16:38:42 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
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
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.MapElement;

/**
 * ������� ����� - ���� 
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/12/22 16:38:42 $
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

	private static boolean needInit = true;

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

	public void setActive(TopologicalNode node, boolean active)
	{
		node.setActive(active);
		if(active)
			node.setImageId(getLogicalNetLayer().getImageId(CLOSED_NODE, CLOSED_NODE_IMAGE));
		else
			node.setImageId(getLogicalNetLayer().getImageId(OPEN_NODE, OPEN_NODE_IMAGE));
	}

	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(needInit)
		{
			MapPropertiesManager.setOriginalImage(
				getLogicalNetLayer().getImageId(OPEN_NODE, OPEN_NODE_IMAGE),
				new ImageIcon(OPEN_NODE_IMAGE).getImage());
			MapPropertiesManager.setOriginalImage(
				getLogicalNetLayer().getImageId(CLOSED_NODE, CLOSED_NODE_IMAGE), 
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
