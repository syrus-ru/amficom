/**
 * $Id: MarkController.java,v 1.14 2005/06/23 08:27:18 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;

/**
 * Контроллер метки.
 * @author $Author: krupenn $
 * @version $Revision: 1.14 $, $Date: 2005/06/23 08:27:18 $
 * @module mapviewclient_v1
 */
public final class MarkController extends AbstractNodeController {
	/** Имя пиктограммы. */
	public static final String IMAGE_NAME = "mark";
	/** Пиктограмма. */
	public static final String IMAGE_PATH = "images/mark.gif";

	private static boolean needInit = true;
	private static Identifier imageId;

	/**
	 * Private constructor.
	 */
	private MarkController(NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	public static MapElementController createInstance(NetMapViewer netMapViewer) {
		return new MarkController(netMapViewer);
	}

	public static void init(Identifier creatorId) throws ApplicationException {
		if(needInit ) {
			imageId = NodeTypeController.getImageId(
					creatorId, 
					MarkController.IMAGE_NAME, 
					MarkController.IMAGE_PATH);

			MapPropertiesManager.setOriginalImage(
				imageId,
				new ImageIcon(MarkController.IMAGE_PATH).getImage());
				
			needInit = false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Image getImage(AbstractNode node) {
		node.setImageId(imageId);
		return super.getImage(node);
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(
			MapElement mapElement,
			Graphics g,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		if(!(mapElement instanceof Mark))
			return;
		Mark mark = (Mark )mapElement;

		if(!isElementVisible(mark, visibleBounds))
			return;
		
		super.paint(mapElement, g, visibleBounds);

		MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

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

	/**
	 * Пересчитать расстояние от начального узла фрагмента, на котором
	 * находится метка, до метки.
	 * @param mark метка
	 */
	public void updateSizeInDoubleLt(Mark mark)
			throws MapConnectionException, MapDataException {
		MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();
		
		mark.getPhysicalLink().sortNodes();
		
		List nodes = mark.getPhysicalLink().getSortedNodes();
		
		DoublePoint from;
		DoublePoint to = mark.getLocation();

		if(nodes.indexOf(mark.getNodeLink().getStartNode()) 
				< nodes.indexOf(mark.getNodeLink().getEndNode()))
			from = mark.getNodeLink().getStartNode().getLocation();
		else
			from = mark.getNodeLink().getEndNode().getLocation();

		mark.setSizeInDoubleLt(converter.distance(from, to));
	}

	/**
	 * Передвинуть в точку на заданном расстоянии от начала линии.
	 * @param mark метка
	 * @param topologicalDistance топологическая дистанция
	 */
	public void moveToFromStartLt(Mark mark, double topologicalDistance)
			throws MapConnectionException, MapDataException {
		MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

		mark.getPhysicalLink().sortNodeLinks();
		
		mark.setStartNode(mark.getPhysicalLink().getStartNode());

		if(topologicalDistance > mark.getPhysicalLink().getLengthLt()) {
			topologicalDistance = mark.getPhysicalLink().getLengthLt();
		}

		mark.setDistance(topologicalDistance);

		double cumulativeDistance = 0;
		
		for(Iterator it = mark.getPhysicalLink().getNodeLinks().iterator(); it.hasNext();) {
			mark.setNodeLink((NodeLink )it.next());
			NodeLinkController nlc = (NodeLinkController)this.logicalNetLayer.getMapViewController().getController(mark.getNodeLink());
			nlc.updateLengthLt(mark.getNodeLink());
			if(cumulativeDistance + mark.getNodeLink().getLengthLt() > topologicalDistance) {
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
	 * Adjust marker position accurding to topological distance relative
	 * to current node link (which comprises startNode and endNode).
	 * @param mark метка
	 * @param screenDistance экранное расстояние
	 */
	public void adjustPosition(Mark mark, double screenDistance)
			throws MapConnectionException, MapDataException {
		MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

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
