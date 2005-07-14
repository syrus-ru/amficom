/**
 * $Id: NodeLinkController.java,v 1.15 2005/07/14 15:37:04 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.client.map.controllers;

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

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;

/**
 * ���������� ��������� �����.
 * @author $Author: krupenn $
 * @version $Revision: 1.15 $, $Date: 2005/07/14 15:37:04 $
 * @module mapviewclient_v1
 */
public final class NodeLinkController extends AbstractLinkController {
	/** ������� �������, ������������� ����� ���������. */
	protected java.util.Map labelBoxContainer = new HashMap();

	/** 
	 * ������ ��� ������-����������� ��������� ��� �������� ���������
	 * ����� � ������-������������.
	 */
	private static Polygon searchPolygon = new Polygon(new int[6], new int[6], 6);

	/**
	 * ����� (<code>slope[0]</code>) � ������� (<code>slope[1]</code>) 
	 * ���� ������� ��������� ����� � �������� �����������.
	 */
	protected double[] slope = new double[2];

	/**
	 * Private constructor.
	 */
	private NodeLinkController(NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	public static MapElementController createInstance(NetMapViewer netMapViewer) {
		return new NodeLinkController(netMapViewer);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText(MapElement mapElement) {
		if(!(mapElement instanceof NodeLink))
			return null;

		NodeLink link = (NodeLink )mapElement;

		String s1 = link.getName();
		String s2 = "";
		String s3 = "";
		try {
			AbstractNode smne = link.getStartNode();
			s2 =  ":\n" 
				+ "   " 
				+ LangModelMap.getString("From") 
				+ " " 
				+ smne.getName() 
				+ " [" 
				+ MapViewController.getMapElementReadableType(smne)
				+ "]";
			AbstractNode emne = link.getEndNode();
			s3 = "\n" 
				+ "   " 
				+ LangModelMap.getString("To") 
				+ " " 
				+ emne.getName() 
				+ " [" 
				+ MapViewController.getMapElementReadableType(emne)
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

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelectionVisible(MapElement mapElement) {
		if(!(mapElement instanceof NodeLink))
			return false;

		NodeLink nodeLink = (NodeLink )mapElement;

		PhysicalLinkController plc = (PhysicalLinkController)this.logicalNetLayer.getMapViewController().getController(nodeLink.getPhysicalLink());

		return nodeLink.isSelected() 
			|| plc.isSelectionVisible(nodeLink.getPhysicalLink());
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isElementVisible(
			MapElement mapElement,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		if(!(mapElement instanceof NodeLink))
			return false;

		NodeLink nodeLink = (NodeLink )mapElement;
		
		return visibleBounds.intersectsLine(
			nodeLink.getStartNode().getLocation().getX(),
			nodeLink.getStartNode().getLocation().getY(),
			nodeLink.getEndNode().getLocation().getX(),
			nodeLink.getEndNode().getLocation().getY());
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(
			MapElement mapElement,
			Graphics g,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		if(!(mapElement instanceof NodeLink))
			return;

		NodeLink nodeLink = (NodeLink )mapElement;
		
		long f;
		long d;
		f = System.currentTimeMillis();
		if(!isElementVisible(nodeLink, visibleBounds))
			return;
		d = System.currentTimeMillis();
		MapViewController.addTime1(d - f);
		
		f = System.currentTimeMillis();		
		Stroke stroke = getStroke(nodeLink);
		d = System.currentTimeMillis();
		MapViewController.addTime2(d - f);
		
		f = System.currentTimeMillis();		
		Color color = getColor(nodeLink);
		d = System.currentTimeMillis();
		MapViewController.addTime3(d - f);
	

		f = System.currentTimeMillis();
		paint(nodeLink, g, visibleBounds, stroke, color);
		d = System.currentTimeMillis();
		MapViewController.addTime4(d - f);
		
		f = System.currentTimeMillis();

		MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();
		Point from = converter.convertMapToScreen(nodeLink.getStartNode().getLocation());
		Point to = converter.convertMapToScreen(nodeLink.getEndNode().getLocation());

		// �������� �������� � ������� NodeLink
		if(MapPropertiesManager.isShowLength()) {
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
		d = System.currentTimeMillis();
		MapViewController.addTime5(d - f);
	}

	public Color getColor(MapElement mapElement) {
		if(!(mapElement instanceof NodeLink))
			return MapPropertiesManager.getColor();
		NodeLink nodeLink = (NodeLink )mapElement;
		PhysicalLinkController plc = (PhysicalLinkController)this.logicalNetLayer.getMapViewController().getController(nodeLink.getPhysicalLink());
		return plc.getColor(nodeLink.getPhysicalLink());
	}

	public Stroke getStroke(MapElement mapElement) {
		if(!(mapElement instanceof NodeLink))
			return MapPropertiesManager.getStroke();
		NodeLink nodeLink = (NodeLink )mapElement;
		PhysicalLinkController plc = (PhysicalLinkController)this.logicalNetLayer.getMapViewController().getController(nodeLink.getPhysicalLink());
		return plc.getStroke(nodeLink.getPhysicalLink());
	}

	/**
	 * ���������� �������� ����� � �������� ������ � ������.
	 * � ������ �����
	 * @param nodeLink �������� �����
	 * @param g ����������� ��������
	 * @param visibleBounds ������� �������
	 * @param stroke ����� �����
	 * @param color ���� �����
	 */
	public void paint(
			NodeLink nodeLink,
			Graphics g, 
			Rectangle2D.Double visibleBounds, 
			Stroke stroke, 
			Color color)
			throws MapConnectionException, MapDataException {
		if(!isElementVisible(nodeLink, visibleBounds))
			return;

		updateLengthLt(nodeLink);

		MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

		Point from = converter.convertMapToScreen(nodeLink.getStartNode().getLocation());
		Point to = converter.convertMapToScreen(nodeLink.getEndNode().getLocation());

		Graphics2D p = (Graphics2D )g;

		p.setStroke( stroke);

		//���� alarm ���� �� ����������� thread ����� ������ showAlarmState �
		// NodeLink ����� ������
		if((nodeLink.getAlarmState()) && MapPropertiesManager.isShowAlarmState()) {
			p.setColor(getAlarmedColor(nodeLink));
		}
		else {
			p.setColor(color);
		}

		if(isSelectionVisible(nodeLink)) {
			p.setColor(MapPropertiesManager.getSelectionColor());
			p.setStroke(new BasicStroke(MapPropertiesManager.getSelectionThickness()));
		}

		p.drawLine(from.x, from.y, to.x, to.y);

		if (isSelectionVisible(nodeLink)) {
			p.setStroke(MapPropertiesManager.getSelectionStroke());
			
			double dx = (to.x - from.x);
			double dy = (to.y - from.y);

			double length = Math.sqrt( dx * dx + dy * dy );

			// ������ �� ����� ���������, ������� ���� ����������� ���������
			// � �������� 4 � 6 ����� � ������ �������
			double l = 4;
			double l1 = 6;
			
			// a - ���� ������� nodelink
			double sinA = dy / length;

			double cosA = dx / length;

			// �������� �� x � �� y ��� ����� ���������
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
	 * {@inheritDoc}
	 * <br>����� ��������� �� ���������, ���� ��� ��������� � ������ ����� 
	 * ���������.
	 */
	public boolean isMouseOnElement(
			MapElement mapElement,
			Point currentMousePoint)
			throws MapConnectionException, MapDataException {
		if(!(mapElement instanceof NodeLink))
			return false;

		NodeLink nodeLink = (NodeLink )mapElement;
		
		MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

		int[] xx = searchPolygon.xpoints;
		int[] yy = searchPolygon.ypoints;

		Point from = converter.convertMapToScreen(nodeLink.getStartNode().getLocation());
		Point to = converter.convertMapToScreen(nodeLink.getEndNode().getLocation());

		int minX = (int )from.getX();
		int maxX = (int )to.getX();

		int minY = (int )from.getY();
		int maxY = (int )to.getY();
		
		int mouseTolerancy = MapPropertiesManager.getMouseTolerancy();

		if (Math.abs(maxX - minX) < Math.abs(maxY - minY)) {
			xx[0] = minX - mouseTolerancy; yy[0] = minY;
			xx[1] = maxX - mouseTolerancy; yy[1] = maxY;
			xx[2] = maxX; yy[2] = maxY + mouseTolerancy;
			xx[3] = maxX + mouseTolerancy; yy[3] = maxY;
			xx[4] = minX + mouseTolerancy; yy[4] = minY;
			xx[5] = minX; yy[5] = minY - mouseTolerancy;
		}
		else {
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

	/**
	 * �������� ������� ��������, ������������� ����� ���������.
	 * @param nodeLink �������� �����
	 * @return �������
	 */
	public Rectangle getLabelBox(NodeLink nodeLink) {
		Rectangle rect = (Rectangle )this.labelBoxContainer.get(nodeLink);
		if(rect == null) {
			rect = new Rectangle();
			this.labelBoxContainer.put(nodeLink, rect);
		}
		return rect;
	}

	/**
	 * ���������� ����, �����������, ��� ����� currentMousePoint ��������� 
	 * � �������� ��������, ������������� ����� ���������.
	 * @param nodeLink �������� �����
	 * @param currentMousePoint �������� �����
	 * @return �������� �����
	 */
	public boolean isMouseOnThisObjectsLabel(
			NodeLink nodeLink,
			Point currentMousePoint) {
		return getLabelBox(nodeLink).contains(currentMousePoint);
	}
	
	/**
	 * �������� �������������� ����� ����� �� ����������� �������� �����.
	 * @param nodeLink �������� �����
	 */	
	public void updateLengthLt(NodeLink nodeLink)
			throws MapConnectionException, MapDataException {
		MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

		if(converter != null)
			nodeLink.setLengthLt(converter.distance(
					nodeLink.getStartNode().getLocation(), 
					nodeLink.getEndNode().getLocation()));
	}

	/**
	 * ���������� ����� ��������� ����� �� ��������� ���� <code>node</code>. 
	 * ���������� ���������������� ���� �������������� � ������������ � ����� 
	 * ������, ���������� ���� <code>node</code> �� ��������.
	 * @param nodeLink �������� �����
	 * @param node ����
	 * @param dist �������������� ����������
	 */
	public void setSizeFrom(NodeLink nodeLink, AbstractNode node, double dist)
			throws MapConnectionException, MapDataException {
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

	/**
	 * �������� ����� ��������� ����� � �������� �����������.
	 * @param nodeLink �������� �����
	 * @return �����
	 */
	public double getScreenLength(NodeLink nodeLink)
			throws MapConnectionException, MapDataException {
		MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();
		
		Point start = converter.convertMapToScreen(nodeLink.getStartNode().getLocation());
		Point end = converter.convertMapToScreen(nodeLink.getEndNode().getLocation());

		return Math.sqrt( 
				(end.x - start.x) * (end.x - start.x) +
				(end.y - start.y) * (end.y - start.y) );
	}

	/**
	 * �������� ����� ({@link #slope}<code>[0]</code>) � ������� 
	 * ({@link #slope}<code>[1]</code>) 
	 * ���� ������� ��������� ����� � �������� �����������.
	 * @param nodeLink �������� �����
	 * @return ������ �� 2 ��������� ({@link #slope})
	 */
	public double[] calcScreenSlope(NodeLink nodeLink)
			throws MapConnectionException, MapDataException {
		MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();
		
		Point start = converter.convertMapToScreen(nodeLink.getStartNode().getLocation());
		Point end = converter.convertMapToScreen(nodeLink.getEndNode().getLocation());

		double nodeLinkLength =  Math.sqrt( 
				(end.x - start.x) * (end.x - start.x) +
				(end.y - start.y) * (end.y - start.y) );

		double sinB = (end.y - start.y) / nodeLinkLength;

		double cosB = (end.x - start.x) / nodeLinkLength;
		
		this.slope[0] = sinB;
		this.slope[1] = cosB;
		
		return this.slope;
	}
	
}
