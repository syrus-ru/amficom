/**
 * $Id: NodeLinkController.java,v 1.4 2004/12/23 16:57:59 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import java.util.HashMap;

/**
 * линейный элемента карты 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/23 16:57:59 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class NodeLinkController extends AbstractLinkController
{
	/** границы объекта, отображающего длину фрагмента */
	protected java.util.Map labelBoxContainer = new HashMap();

	private static Polygon searchPolygon = new Polygon(new int[6], new int[6], 6);

	private static NodeLinkController instance = null;
	
	private NodeLinkController()
	{
	}
	
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new NodeLinkController();
		return instance;
	}

	public String getToolTipText(MapElement me)
	{
		if(! (me instanceof NodeLink))
			return null;

		NodeLink link = (NodeLink )me;
		
		String s1 = link.getName();
		String s2 = "";
		String s3 = "";
		try
		{
			AbstractNode smne = link.getStartNode();
			s2 =  ":\n" 
				+ "   " 
				+ LangModelMap.getString("From") 
				+ " " 
				+ smne.getName() 
				+ " [" 
				+ LangModel.getString("node" + smne.getClass().getName()) 
				+ "]";
			AbstractNode emne = link.getEndNode();
			s3 = "\n" 
				+ "   " 
				+ LangModelMap.getString("To") 
				+ " " 
				+ emne.getName() 
				+ " [" 
				+ LangModel.getString("node" + emne.getClass().getName()) 
				+ "]";
		}
		catch(Exception e)
		{
			Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getToolTipText()", 
				e);
		}
		return s1 + s2 + s3;
	}

	public boolean isSelectionVisible(MapElement me)
	{
		if(! (me instanceof NodeLink))
			return false;

		NodeLink nodeLink = (NodeLink )me;

		PhysicalLinkController plc = (PhysicalLinkController )getLogicalNetLayer().getMapViewController().getController(nodeLink.getPhysicalLink());

		return nodeLink.isSelected() 
			|| plc.isSelectionVisible(nodeLink.getPhysicalLink());
	}

	public boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds)
	{
		if(! (me instanceof NodeLink))
			return false;

		NodeLink nodeLink = (NodeLink )me;
		
		return visibleBounds.intersectsLine(
			nodeLink.getStartNode().getLocation().getX(),
			nodeLink.getStartNode().getLocation().getY(),
			nodeLink.getEndNode().getLocation().getX(),
			nodeLink.getEndNode().getLocation().getY());
	}

	/**
	 * получить границы элемента
	 */
	public Rectangle getLabelBox(NodeLink nodeLink)
	{
		Rectangle rect = (Rectangle )labelBoxContainer.get(nodeLink);
		if(rect == null)
		{
			rect = new Rectangle();
			labelBoxContainer.put(nodeLink, rect);
		}
		return rect;
	}

	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(! (me instanceof NodeLink))
			return;

		NodeLink nodeLink = (NodeLink )me;
		
		if(!isElementVisible(nodeLink, visibleBounds))
			return;

		BasicStroke basistroke;
		Color color;
		int lineSize;
		
		PhysicalLinkController plc = (PhysicalLinkController )getLogicalNetLayer().getMapViewController().getController(nodeLink.getPhysicalLink());

		lineSize = plc.getLineSize(nodeLink.getPhysicalLink());
		basistroke = (BasicStroke )plc.getStroke(nodeLink.getPhysicalLink());
		color = plc.getColor(nodeLink.getPhysicalLink());
	
		Stroke str = new BasicStroke(
				lineSize, 
				basistroke.getEndCap(), 
				basistroke.getLineJoin(), 
				basistroke.getMiterLimit(), 
				basistroke.getDashArray(), 
				basistroke.getDashPhase());

		paint(nodeLink, g, visibleBounds, str, color);

		MapCoordinatesConverter converter = getLogicalNetLayer();
		Point from = converter.convertMapToScreen(nodeLink.getStartNode().getLocation());
		Point to = converter.convertMapToScreen(nodeLink.getEndNode().getLocation());

		//Рисовать табличку с длинной NodeLink
		if ( MapPropertiesManager.isShowLength() )
		{
			int fontHeight = g.getFontMetrics().getHeight();
			String text = 
					MapPropertiesManager.getDistanceFormat().format(nodeLink.getLengthLt()) 
					+ " " 
					+ MapPropertiesManager.getMetric();
			int textWidth = g.getFontMetrics().stringWidth(text);
			int centerX = (from.x + to.x) / 2;
			int centerY = (from.y + to.y) / 2;

			g.setColor(MapPropertiesManager.getBorderColor());
			g.setFont(MapPropertiesManager.getFont());

			Rectangle labelBox = getLabelBox(nodeLink);

			labelBox.setBounds(
					centerX,
					centerY - fontHeight + 2,
					textWidth,
					fontHeight);

			g.drawRect(
					centerX,
					centerY - fontHeight + 2,
					textWidth,
					fontHeight);

			g.setColor(MapPropertiesManager.getTextBackground());
			g.fillRect(
					centerX,
					centerY - fontHeight + 2,
					textWidth,
					fontHeight);

			g.setColor(MapPropertiesManager.getTextColor());
			g.drawString(
					text,
					centerX,
					centerY);
		}
	}

	public void paint(
			NodeLink nodeLink,
			Graphics g, 
			Rectangle2D.Double visibleBounds, 
			Stroke stroke, 
			Color color)
	{
		if(!isElementVisible(nodeLink, visibleBounds))
			return;

		updateLengthLt(nodeLink);

		MapCoordinatesConverter converter = getLogicalNetLayer();

		Point from = converter.convertMapToScreen(nodeLink.getStartNode().getLocation());
		Point to = converter.convertMapToScreen(nodeLink.getEndNode().getLocation());

		Graphics2D p = (Graphics2D )g;

		p.setStroke( stroke);

		//Если alarm есть то специальный thread будет менять showAlarmState и
		// NodeLink будет мигать
		if ( (nodeLink.getAlarmState()) && MapPropertiesManager.isShowAlarmState() )
		{
			p.setColor(getAlarmedColor(nodeLink));
		}
		else
		{
			p.setColor(color);
		}

		if (isSelectionVisible(nodeLink))
		{
			p.setColor(MapPropertiesManager.getSelectionColor());
			p.setStroke(new BasicStroke(MapPropertiesManager.getSelectionThickness()));
		}

		p.drawLine(from.x, from.y, to.x, to.y);

		if (isSelectionVisible(nodeLink))
		{
			p.setStroke(MapPropertiesManager.getSelectionStroke());
			
			double dx = (to.x - from.x);
			double dy = (to.y - from.y);

			double length = Math.sqrt( dx * dx + dy * dy );

			// рисуем по линии выделения, которые идут параллельно фрагменту
			// с отступом 4 и 6 точек с каждой стороны
			double l = 4;
			double l1 = 6;
			
			// a - угол наклона nodelink
			double sinA = dy / length;

			double cosA = dx / length;

			// смещение по x и по y для линии выделения
			int lxshift = (int )(l * sinA);
			int lyshift = (int )(l * cosA);

			int l1xshift = (int )(l1 * sinA);
			int l1yshift = (int )(l1 * cosA);

			p.setColor(MapPropertiesManager.getFirstSelectionColor());
			p.drawLine(
					from.x + lxshift, 
					from.y - lyshift, 
					to.x + lxshift, 
					to.y - lyshift);
			p.drawLine(
					from.x - lxshift, 
					from.y + lyshift, 
					to.x - lxshift, 
					to.y + lyshift);

			p.setColor(MapPropertiesManager.getSecondSelectionColor());
			p.drawLine(
					from.x + l1xshift, 
					from.y - l1yshift, 
					to.x + l1xshift, 
					to.y - l1yshift);
			p.drawLine(
					from.x - l1xshift, 
					from.y + l1yshift, 
					to.x - l1xshift, 
					to.y + l1yshift);
		}

	}

	/**
	 * точка находится на фрагменте, если она находится в рамках линий выделения
	 */
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
	{
		if(! (me instanceof NodeLink))
			return false;

		NodeLink nodeLink = (NodeLink )me;
		
		MapCoordinatesConverter converter = getLogicalNetLayer();

		int[] xx = searchPolygon.xpoints;
		int[] yy = searchPolygon.ypoints;

		Point from = converter.convertMapToScreen(nodeLink.getStartNode().getLocation());
		Point to = converter.convertMapToScreen(nodeLink.getEndNode().getLocation());

		int minX = (int )from.getX();
		int maxX = (int )to.getX();

		int minY = (int )from.getY();
		int maxY = (int )to.getY();
		
		int mouseTolerancy = MapPropertiesManager.getMouseTolerancy();

		if (Math.abs(maxX - minX) < Math.abs(maxY - minY))
		{
			xx[0] = minX - mouseTolerancy; yy[0] = minY;
			xx[1] = maxX - mouseTolerancy; yy[1] = maxY;
			xx[2] = maxX; yy[2] = maxY + mouseTolerancy;
			xx[3] = maxX + mouseTolerancy; yy[3] = maxY;
			xx[4] = minX + mouseTolerancy; yy[4] = minY;
			xx[5] = minX; yy[5] = minY - mouseTolerancy;
		}
		else
		{
			xx[0] = minX; yy[0] = minY + mouseTolerancy;
			xx[1] = maxX; yy[1] = maxY + mouseTolerancy;
			xx[2] = maxX + mouseTolerancy; yy[2] = maxY;
			xx[3] = maxX; yy[3] = maxY - mouseTolerancy;
			xx[4] = minX; yy[4] = minY - mouseTolerancy;
			xx[5] = minX - mouseTolerancy; yy[5] = minY;
		}

		searchPolygon.invalidate();
		if(searchPolygon.contains(currentMousePoint))
		{
			return true;
		}
		return false;
	}

	public boolean isMouseOnThisObjectsLabel(
			NodeLink nodeLink, 
			Point currentMousePoint)
	{
		return getLabelBox(nodeLink).contains(currentMousePoint);
	}
	
	/**
	 * обновить топологическую длину линии по координатам концевых узлов
	 */	
	public void updateLengthLt(NodeLink nodeLink)
	{
		MapCoordinatesConverter converter = getLogicalNetLayer();

		if(converter != null)
			nodeLink.setLength(converter.distance(
					nodeLink.getStartNode().getLocation(), 
					nodeLink.getEndNode().getLocation()));
	}

	/**
	 * установить дистанцию от противоположного узла
	 */
	public void setSizeFrom(NodeLink nodeLink, AbstractNode node, double dist)
	{
		AbstractNode oppositeNode = 
			(nodeLink.getStartNode().equals(node)) 
					? nodeLink.getEndNode() 
					: nodeLink.getStartNode();

		double prevDist = nodeLink.getLengthLt();
		
		double coef = dist / prevDist;

		double absc = coef * (node.getLocation().getX() - oppositeNode.getLocation().getX()) 
				+ oppositeNode.getLocation().getX();
		double ordi = coef * (node.getLocation().getY() - oppositeNode.getLocation().getY()) 
				+ oppositeNode.getLocation().getY();

		node.setLocation(new DoublePoint(absc, ordi));
		
		updateLengthLt(nodeLink);
	}

	public double getScreenLength(NodeLink nodeLink)
	{
		MapCoordinatesConverter converter = getLogicalNetLayer();
		
		Point start = converter.convertMapToScreen(nodeLink.getStartNode().getLocation());
		Point end = converter.convertMapToScreen(nodeLink.getEndNode().getLocation());

		return Math.sqrt( 
				(end.x - start.x) * (end.x - start.x) +
				(end.y - start.y) * (end.y - start.y) );
	}

	protected double[] slope = new double[2];
	
	public double[] calcScreenSlope(NodeLink nodeLink)
	{
		MapCoordinatesConverter converter = getLogicalNetLayer();
		
		Point start = converter.convertMapToScreen(nodeLink.getStartNode().getLocation());
		Point end = converter.convertMapToScreen(nodeLink.getEndNode().getLocation());

		double nodeLinkLength =  Math.sqrt( 
				(end.x - start.x) * (end.x - start.x) +
				(end.y - start.y) * (end.y - start.y) );

		double sinB = (end.y - start.y) / nodeLinkLength;

		double cosB = (end.x - start.x) / nodeLinkLength;
		
		slope[0] = sinB;
		slope[1] = cosB;
		
		return slope;
	}
	
}
