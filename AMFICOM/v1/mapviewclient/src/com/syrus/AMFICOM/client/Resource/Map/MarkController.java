/**
 * $Id: MarkController.java,v 1.2 2004/12/08 16:20:22 krupenn Exp $
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
import java.util.Iterator;
import java.util.List;
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
public final class MarkController extends AbstractNodeController
{
	public static final String IMAGE_NAME = "mark";
	public static final String IMAGE_PATH = "images/mark.gif";

	static
	{
		MapPropertiesManager.setOriginalImage(IMAGE_NAME, new ImageIcon(IMAGE_PATH).getImage());
	}

	private static MarkController instance = null;
	
	private MarkController()
	{
	}
	
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new MarkController();
		return instance;
	}

	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(!(me instanceof MapMarkElement))
			return;
		MapMarkElement mark = (MapMarkElement )me;

		if(!isElementVisible(mark, visibleBounds))
			return;
		
		super.paint(me, g, visibleBounds);

		MapCoordinatesConverter converter = getLogicalNetLayer();

		Point p = converter.convertMapToScreen( mark.getLocation());

		Graphics2D pg = (Graphics2D )g;
		pg.setStroke(new BasicStroke(MapPropertiesManager.getBorderThickness()));
		
		pg.setColor(MapPropertiesManager.getBorderColor());
		pg.setBackground(MapPropertiesManager.getTextBackground());
		pg.setFont(MapPropertiesManager.getFont());

		int width = getBounds(mark).width;
		int height = g.getFontMetrics().getHeight();

		String s1 = mark.getName();
		pg.drawRect(
				p.x + width, 
				p.y - height + 2,
				pg.getFontMetrics().stringWidth(s1), 
				height );

		pg.setColor(MapPropertiesManager.getTextBackground());
		pg.fillRect(
				p.x + width, 
				p.y - height + 2,
				pg.getFontMetrics().stringWidth(s1), 
				height );

		g.setColor(MapPropertiesManager.getTextColor());
		g.drawString(
				s1, 
				p.x + width, 
				p.y - 2 );
	}

	public void updateSizeInDoubleLt(MapMarkElement mark)
	{
		MapCoordinatesConverter converter = getLogicalNetLayer();
		
		mark.getLink().sortNodes();
		
		List nodes = mark.getLink().getSortedNodes();
		
		DoublePoint from;
		DoublePoint to = mark.getLocation();

		if(nodes.indexOf(mark.getNodeLink().getStartNode()) 
				< nodes.indexOf(mark.getNodeLink().getEndNode()))
			from = mark.getNodeLink().getStartNode().getLocation();
		else
			from = mark.getNodeLink().getEndNode().getLocation();

		mark.sizeInDoubleLt = converter.distance(from, to);
	}

	/**
	 * Передвинуть в точку на заданной расстоянии от начала
	 */
	public void moveToFromStartLt(MapMarkElement mark, double topologicalDistance)
	{
		MapCoordinatesConverter converter = getLogicalNetLayer();

		mark.getLink().sortNodeLinks();
		
		mark.setStartNode(mark.getLink().getStartNode());

		if ( topologicalDistance > mark.getLink().getLengthLt())
		{
			topologicalDistance = mark.getLink().getLengthLt();
		}

		mark.distance = topologicalDistance;

		double cumulativeDistance = 0;
		
		for(Iterator it = mark.getLink().getNodeLinks().iterator(); it.hasNext();)
		{
			mark.setNodeLink((MapNodeLinkElement )it.next());
			NodeLinkController nlc = (NodeLinkController )getLogicalNetLayer().getMapViewController().getController(mark.getNodeLink());
			nlc.updateLengthLt(mark.getNodeLink());
			if(cumulativeDistance + mark.getNodeLink().getLengthLt() > topologicalDistance)
			{
				double distanceFromStart = topologicalDistance - cumulativeDistance;
				DoublePoint newPoint = converter.pointAtDistance(
						mark.getStartNode().getLocation(), 
						mark.getNodeLink().getOtherNode(mark.getStartNode()).getLocation(),
						distanceFromStart);
				mark.setLocation(newPoint);
				break;
			}

			cumulativeDistance += mark.getNodeLink().getLengthLt();
			mark.setStartNode(mark.getNodeLink().getOtherNode(mark.getStartNode()));
		}
		
		updateSizeInDoubleLt(mark);
	}

	/**
	 * adjust marker position accurding to topological distance relative
	 * to current node link (which comprises startNode and endNode)
	 */
	public void adjustPosition(MapMarkElement mark, double screenDistance)
	{
		MapCoordinatesConverter converter = getLogicalNetLayer();

		Point sp = converter.convertMapToScreen(mark.getStartNode().getLocation());
	
		double startNodeX = sp.x;
		double startNodeY = sp.y;

		Point ep = converter.convertMapToScreen(mark.getNodeLink().getOtherNode(mark.getStartNode()).getLocation());

		double endNodeX = ep.x;
		double endNodeY = ep.y;

		double nodeLinkLength =  Math.sqrt( 
				(endNodeX - startNodeX) * (endNodeX - startNodeX) +
				(endNodeY - startNodeY) * (endNodeY - startNodeY) );

		double sinB = (endNodeY - startNodeY) / nodeLinkLength;

		double cosB = (endNodeX - startNodeX) / nodeLinkLength;

		mark.setLocation(converter.convertScreenToMap(new Point(
			(int )Math.round(startNodeX + cosB * screenDistance),
			(int )Math.round(startNodeY + sinB * screenDistance) ) ) );
	}
}
