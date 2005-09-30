/*-
 * $$Id: NodeLinkController.java,v 1.27 2005/09/30 16:08:39 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
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
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.resource.DoublePoint;

/**
 * ���������� ��������� �����.
 * 
 * @version $Revision: 1.27 $, $Date: 2005/09/30 16:08:39 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class NodeLinkController extends AbstractLinkController {
	/** ������� �������, ������������� ����� ���������. */
	protected java.util.Map<Identifier, Rectangle> labelBoxContainer = new HashMap<Identifier, Rectangle>();

	/**
	 * ������ ��� ������-����������� ��������� ��� �������� ��������� ����� �
	 * ������-������������.
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
	private NodeLinkController(final NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	public static MapElementController createInstance(final NetMapViewer netMapViewer) {
		return new NodeLinkController(netMapViewer);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText(final MapElement mapElement) {
		if (!(mapElement instanceof NodeLink)) {
			return null;
		}

		final NodeLink link = (NodeLink) mapElement;

		final String s1 = link.getName();
		String s2 = ""; //$NON-NLS-1$
		String s3 = ""; //$NON-NLS-1$
		try {
			final AbstractNode smne = link.getStartNode();
			s2 = ":\n" //$NON-NLS-1$
					+ "   " //$NON-NLS-1$
					+ LangModelMap.getString(MapEditorResourceKeys.FROM_LOWERCASE)
					+ " " //$NON-NLS-1$
					+ smne.getName()
					+ " [" //$NON-NLS-1$
					+ MapViewController.getMapElementReadableType(smne)
					+ "]"; //$NON-NLS-1$
			final AbstractNode emne = link.getEndNode();
			s3 = "\n" //$NON-NLS-1$
					+ "   " //$NON-NLS-1$
					+ LangModelMap.getString(MapEditorResourceKeys.TO_LOWERCASE)
					+ " " //$NON-NLS-1$
					+ emne.getName()
					+ " [" //$NON-NLS-1$
					+ MapViewController.getMapElementReadableType(emne)
					+ "]"; //$NON-NLS-1$
		} catch (Exception e) {
			Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "getToolTipText()", e); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return s1 + s2 + s3;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSelectionVisible(final MapElement mapElement) {
		if (!(mapElement instanceof NodeLink)) {
			return false;
		}
		final NodeLink nodeLink = (NodeLink) mapElement;
		return nodeLink.isSelected();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isElementVisible(final MapElement mapElement, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		if (!(mapElement instanceof NodeLink)) {
			return false;
		}

		final NodeLink nodeLink = (NodeLink) mapElement;

		return visibleBounds.intersectsLine(nodeLink.getStartNode().getLocation().getX(),
				nodeLink.getStartNode().getLocation().getY(),
				nodeLink.getEndNode().getLocation().getX(),
				nodeLink.getEndNode().getLocation().getY());
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(final MapElement mapElement, final Graphics g, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		if (!(mapElement instanceof NodeLink)) {
			return;
		}

		final NodeLink nodeLink = (NodeLink) mapElement;

		long f;
		long d;
		f = System.currentTimeMillis();
		if (!isElementVisible(nodeLink, visibleBounds)) {
			return;
		}
		d = System.currentTimeMillis();
		MapViewController.addTime1(d - f);

		f = System.currentTimeMillis();
		final Stroke stroke = getStroke(nodeLink);
		d = System.currentTimeMillis();
		MapViewController.addTime2(d - f);

		f = System.currentTimeMillis();
		final Color color = getColor(nodeLink);
		d = System.currentTimeMillis();
		MapViewController.addTime3(d - f);

		f = System.currentTimeMillis();
		this.paint(nodeLink, g, visibleBounds, stroke, color, isSelectionVisible(nodeLink));
		d = System.currentTimeMillis();
		MapViewController.addTime4(d - f);

		f = System.currentTimeMillis();

		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();
		final Point from = converter.convertMapToScreen(nodeLink.getStartNode().getLocation());
		final Point to = converter.convertMapToScreen(nodeLink.getEndNode().getLocation());

		// �������� �������� � ������� NodeLink
		if (MapPropertiesManager.isShowLength()) {
			final int fontHeight = g.getFontMetrics().getHeight();
			final String text = MapPropertiesManager.getDistanceFormat().format(nodeLink.getLengthLt())
					+ " " //$NON-NLS-1$
					+ MapPropertiesManager.getMetric();
			final int textWidth = g.getFontMetrics().stringWidth(text);
			final int centerX = (from.x + to.x) / 2;
			final int centerY = (from.y + to.y) / 2;

			g.setColor(MapPropertiesManager.getBorderColor());
			g.setFont(MapPropertiesManager.getFont());

			final Rectangle labelBox = getLabelBox(nodeLink);

			labelBox.setBounds(centerX, centerY - fontHeight + 2, textWidth, fontHeight);

			g.drawRect(centerX, centerY - fontHeight + 2, textWidth, fontHeight);

			g.setColor(MapPropertiesManager.getTextBackground());
			g.fillRect(centerX, centerY - fontHeight + 2, textWidth, fontHeight);

			g.setColor(MapPropertiesManager.getTextColor());
			g.drawString(text, centerX, centerY);
		}
		d = System.currentTimeMillis();
		MapViewController.addTime5(d - f);
	}

	public Color getAlarmedColor(final NodeLink nodeLink) {
		final PhysicalLinkController plc = (PhysicalLinkController) this.logicalNetLayer.getMapViewController().getController(nodeLink.getPhysicalLink());
		return plc.getAlarmedColor(nodeLink.getPhysicalLink());
	}

	public Color getColor(final NodeLink nodeLink) {
		final PhysicalLinkController plc = (PhysicalLinkController) this.logicalNetLayer.getMapViewController().getController(nodeLink.getPhysicalLink());
		return plc.getColor(nodeLink.getPhysicalLink());
	}

	public Stroke getStroke(final NodeLink nodeLink) {
		final PhysicalLinkController plc = (PhysicalLinkController) this.logicalNetLayer.getMapViewController().getController(nodeLink.getPhysicalLink());
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
	public void paint(final NodeLink nodeLink,
			final Graphics g,
			final Rectangle2D.Double visibleBounds,
			final Stroke stroke,
			final Color color,
			final boolean selectionVisible) throws MapConnectionException, MapDataException {
		if (!isElementVisible(nodeLink, visibleBounds)) {
			return;
		}

		this.updateLengthLt(nodeLink);

		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

		final Point from = converter.convertMapToScreen(nodeLink.getStartNode().getLocation());
		final Point to = converter.convertMapToScreen(nodeLink.getEndNode().getLocation());

		final Graphics2D p = (Graphics2D) g;

		p.setStroke(stroke);

		// ���� alarm ���� �� ����������� thread ����� ������ showAlarmState �
		// NodeLink ����� ������
		if ((nodeLink.getAlarmState()) && MapPropertiesManager.isDrawAlarmed()) {
			p.setColor(getAlarmedColor(nodeLink));
		} else {
			p.setColor(color);
		}

		if (selectionVisible) {
			p.setColor(MapPropertiesManager.getSelectionColor());
			p.setStroke(new BasicStroke(MapPropertiesManager.getSelectionThickness()));
		}

		p.drawLine(from.x, from.y, to.x, to.y);

		if (isSelectionVisible(nodeLink)) {
			p.setStroke(MapPropertiesManager.getSelectionStroke());

			final double dx = (to.x - from.x);
			final double dy = (to.y - from.y);

			final double length = Math.sqrt(dx * dx + dy * dy);

			// ������ �� ����� ���������, ������� ���� ����������� ���������
			// � �������� 4 � 6 ����� � ������ �������
			final double l = 4;
			final double l1 = 6;

			// a - ���� ������� nodelink
			final double sinA = dy / length;

			final double cosA = dx / length;

			// �������� �� x � �� y ��� ����� ���������
			final int lxshift = (int) (l * sinA);
			final int lyshift = (int) (l * cosA);

			final int l1xshift = (int) (l1 * sinA);
			final int l1yshift = (int) (l1 * cosA);

			p.setColor(MapPropertiesManager.getFirstSelectionColor());
			p.drawLine(from.x + lxshift, from.y - lyshift, to.x + lxshift, to.y - lyshift);
			p.drawLine(from.x - lxshift, from.y + lyshift, to.x - lxshift, to.y + lyshift);

			p.setColor(MapPropertiesManager.getSecondSelectionColor());
			p.drawLine(from.x + l1xshift, from.y - l1yshift, to.x + l1xshift, to.y - l1yshift);
			p.drawLine(from.x - l1xshift, from.y + l1yshift, to.x - l1xshift, to.y + l1yshift);
		}

	}

	/**
	 * {@inheritDoc} <br>
	 * ����� ��������� �� ���������, ���� ��� ��������� � ������ ����� ���������.
	 */
	public boolean isMouseOnElement(final MapElement mapElement, final Point currentMousePoint)
			throws MapConnectionException,
				MapDataException {
		if (!(mapElement instanceof NodeLink)) {
			return false;
		}

		final NodeLink nodeLink = (NodeLink) mapElement;

		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

		final int[] xx = searchPolygon.xpoints;
		final int[] yy = searchPolygon.ypoints;

		final Point from = converter.convertMapToScreen(nodeLink.getStartNode().getLocation());
		final Point to = converter.convertMapToScreen(nodeLink.getEndNode().getLocation());

		final int minX = (int) from.getX();
		final int maxX = (int) to.getX();

		final int minY = (int) from.getY();
		final int maxY = (int) to.getY();

		final int mouseTolerancy = MapPropertiesManager.getMouseTolerancy();

		if (Math.abs(maxX - minX) < Math.abs(maxY - minY)) {
			xx[0] = minX - mouseTolerancy;
			yy[0] = minY;
			xx[1] = maxX - mouseTolerancy;
			yy[1] = maxY;
			xx[2] = maxX;
			yy[2] = maxY + mouseTolerancy;
			xx[3] = maxX + mouseTolerancy;
			yy[3] = maxY;
			xx[4] = minX + mouseTolerancy;
			yy[4] = minY;
			xx[5] = minX;
			yy[5] = minY - mouseTolerancy;
		} else {
			xx[0] = minX;
			yy[0] = minY + mouseTolerancy;
			xx[1] = maxX;
			yy[1] = maxY + mouseTolerancy;
			xx[2] = maxX + mouseTolerancy;
			yy[2] = maxY;
			xx[3] = maxX;
			yy[3] = maxY - mouseTolerancy;
			xx[4] = minX;
			yy[4] = minY - mouseTolerancy;
			xx[5] = minX - mouseTolerancy;
			yy[5] = minY;
		}

		searchPolygon.invalidate();
		if (searchPolygon.contains(currentMousePoint)) {
			return true;
		}
		return false;
	}

	/**
	 * �������� ������� ��������, ������������� ����� ���������.
	 * @param nodeLink �������� �����
	 * @return �������
	 */
	public Rectangle getLabelBox(final NodeLink nodeLink) {
		Rectangle rect = this.labelBoxContainer.get(nodeLink.getId());
		if (rect == null) {
			rect = new Rectangle();
			this.labelBoxContainer.put(nodeLink.getId(), rect);
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
	public boolean isMouseOnThisObjectsLabel(final NodeLink nodeLink, final Point currentMousePoint) {
		return getLabelBox(nodeLink).contains(currentMousePoint);
	}

	/**
	 * �������� �������������� ����� ����� �� ����������� �������� �����.
	 * 
	 * @param nodeLink
	 *        �������� �����
	 */
	public void updateLengthLt(final NodeLink nodeLink) throws MapConnectionException, MapDataException {
		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

		if (converter != null) {
			nodeLink.setLengthLt(converter.distance(nodeLink.getStartNode().getLocation(), nodeLink.getEndNode().getLocation()));
		}
	}

	/**
	 * ���������� ����� ��������� ����� �� ��������� ���� <code>node</code>.
	 * ���������� ���������������� ���� �������������� � ������������ � �����
	 * ������, ���������� ���� <code>node</code> �� ��������.
	 * 
	 * @param nodeLink
	 *        �������� �����
	 * @param node
	 *        ����
	 * @param dist
	 *        �������������� ����������
	 */
	public void setSizeFrom(final NodeLink nodeLink, final AbstractNode node, final double dist)
			throws MapConnectionException,
				MapDataException {
		final AbstractNode oppositeNode = (nodeLink.getStartNode().equals(node)) ? nodeLink.getEndNode() : nodeLink.getStartNode();

		final double prevDist = nodeLink.getLengthLt();
		
		if(prevDist == 0) {
			return;
		}

		final double coef = dist / prevDist;

		final double absc = coef * (node.getLocation().getX() - oppositeNode.getLocation().getX()) + oppositeNode.getLocation().getX();
		final double ordi = coef * (node.getLocation().getY() - oppositeNode.getLocation().getY()) + oppositeNode.getLocation().getY();

		node.setLocation(new DoublePoint(absc, ordi));

		this.updateLengthLt(nodeLink);
	}

	/**
	 * �������� ����� ��������� ����� � �������� �����������.
	 * 
	 * @param nodeLink
	 *        �������� �����
	 * @return �����
	 */
	public double getScreenLength(final NodeLink nodeLink) throws MapConnectionException, MapDataException {
		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

		final Point start = converter.convertMapToScreen(nodeLink.getStartNode().getLocation());
		final Point end = converter.convertMapToScreen(nodeLink.getEndNode().getLocation());

		return Math.sqrt((end.x - start.x) * (end.x - start.x) + (end.y - start.y) * (end.y - start.y));
	}

	/**
	 * �������� ����� ({@link #slope}<code>[0]</code>) � ������� ({@link #slope}<code>[1]</code>)
	 * ���� ������� ��������� ����� � �������� �����������.
	 * 
	 * @param nodeLink
	 *        �������� �����
	 * @return ������ �� 2 ��������� ({@link #slope})
	 */
	public double[] calcScreenSlope(final NodeLink nodeLink) throws MapConnectionException, MapDataException {
		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

		final Point start = converter.convertMapToScreen(nodeLink.getStartNode().getLocation());
		final Point end = converter.convertMapToScreen(nodeLink.getEndNode().getLocation());

		final double nodeLinkLength = Math.sqrt((end.x - start.x) * (end.x - start.x) + (end.y - start.y) * (end.y - start.y));

		final double sinB = (end.y - start.y) / nodeLinkLength;

		final double cosB = (end.x - start.x) / nodeLinkLength;

		this.slope[0] = sinB;
		this.slope[1] = cosB;

		return this.slope;
	}

	public Rectangle getBoundingRectangle(MapElement mapElement) throws MapConnectionException, MapDataException {
		NodeLink nodeLink = (NodeLink) mapElement;

		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();
		final Point from = converter.convertMapToScreen(nodeLink.getStartNode().getLocation());
		final Point to = converter.convertMapToScreen(nodeLink.getEndNode().getLocation());

		return new Rectangle(from.x, from.y, to.x - from.x, to.y - from.y);
	}
	
}
