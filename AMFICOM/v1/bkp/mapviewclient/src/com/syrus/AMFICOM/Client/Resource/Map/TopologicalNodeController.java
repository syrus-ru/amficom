/**
 * $Id: TopologicalNodeController.java,v 1.4 2004/12/23 16:57:59 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.TopologicalNode;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

/**
 * элемент карты - узел 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/23 16:57:59 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class TopologicalNodeController extends AbstractNodeController
{
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_PHYSICAL_LINK_ID = "physical_link_id";
	public static final String COLUMN_X = "x";
	public static final String COLUMN_Y = "y";
	public static final String COLUMN_ACTIVE = "active";

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
/*
	protected static String[][] exportColumns = null;

	public String[][] getExportColumns()
	{
		if(exportColumns == null)
		{
			exportColumns = new String[7][2];
			exportColumns[0][0] = COLUMN_ID;
			exportColumns[1][0] = COLUMN_NAME;
			exportColumns[2][0] = COLUMN_DESCRIPTION;
			exportColumns[3][0] = COLUMN_PHYSICAL_LINK_ID;
			exportColumns[4][0] = COLUMN_X;
			exportColumns[5][0] = COLUMN_Y;
			exportColumns[6][0] = COLUMN_ACTIVE;
		}
		exportColumns[0][1] = getId().toString();
		exportColumns[1][1] = getName();
		exportColumns[2][1] = getDescription();
		exportColumns[3][1] = physicalLinkId;
		exportColumns[4][1] = String.valueOf(getLocation().x);
		exportColumns[5][1] = String.valueOf(getLocation().y);
		exportColumns[6][1] = String.valueOf(isActive());
		
		return exportColumns;
	}
	
	public void setColumn(String field, String value)
	{
		if(field.equals(COLUMN_ID))
			setId(value);
		else
		if(field.equals(COLUMN_NAME))
			setName(value);
		else
		if(field.equals(COLUMN_DESCRIPTION))
			setDescription(value);
		else
		if(field.equals(COLUMN_PHYSICAL_LINK_ID))
			physicalLinkId = value;
		else
		if(field.equals(COLUMN_X))
			location.x = Double.parseDouble(value);
		else
		if(field.equals(COLUMN_Y))
			location.y = Double.parseDouble(value);
		else
		if(field.equals(COLUMN_ACTIVE))
			setActive(Boolean.valueOf(value).booleanValue());
	}

	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
*/	
}
