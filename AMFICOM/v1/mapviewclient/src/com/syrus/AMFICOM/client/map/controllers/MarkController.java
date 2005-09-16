/**
 * $Id: MarkController.java,v 1.21 2005/09/16 14:53:34 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
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
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.resource.DoublePoint;

/**
 * ���������� �����.
 * @author $Author: krupenn $
 * @version $Revision: 1.21 $, $Date: 2005/09/16 14:53:34 $
 * @module mapviewclient
 */
public final class MarkController extends AbstractNodeController {
	/** ��� �����������. */
	public static final String IMAGE_NAME = "mark"; //$NON-NLS-1$
	/** �����������. */
	public static final String IMAGE_PATH = "images/mark.gif"; //$NON-NLS-1$

	private static boolean needInit = true;
	private static Identifier imageId;

	/**
	 * Private constructor.
	 */
	private MarkController(final NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	public static MapElementController createInstance(final NetMapViewer netMapViewer) {
		return new MarkController(netMapViewer);
	}

	public static void init(final Identifier creatorId) throws ApplicationException {
		if (needInit) {
			imageId = NodeTypeController.getImageId(creatorId, MarkController.IMAGE_NAME, MarkController.IMAGE_PATH);
			MapPropertiesManager.setOriginalImage(imageId, new ImageIcon(MarkController.IMAGE_PATH).getImage());
			needInit = false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getImage(final AbstractNode node) {
		node.setImageId(imageId);
		return super.getImage(node);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paint(final MapElement mapElement, final Graphics g, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		if (!(mapElement instanceof Mark)) {
			return;
		}
		final Mark mark = (Mark) mapElement;

		if (!this.isElementVisible(mark, visibleBounds)) {
			return;
		}

		super.paint(mapElement, g, visibleBounds);

		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

		final Point p = converter.convertMapToScreen(mark.getLocation());

		final Graphics2D pg = (Graphics2D) g;
		pg.setStroke(new BasicStroke(MapPropertiesManager.getBorderThickness()));

		pg.setColor(MapPropertiesManager.getBorderColor());
		pg.setBackground(MapPropertiesManager.getTextBackground());
		pg.setFont(MapPropertiesManager.getFont());

		final int width = getBounds(mark).width;
		final int height = g.getFontMetrics().getHeight();

		final String s1 = mark.getName();
		pg.drawRect(p.x + width, p.y - height + 2, pg.getFontMetrics().stringWidth(s1), height);

		pg.setColor(MapPropertiesManager.getTextBackground());
		pg.fillRect(p.x + width, p.y - height + 2, pg.getFontMetrics().stringWidth(s1), height);

		g.setColor(MapPropertiesManager.getTextColor());
		g.drawString(s1, p.x + width, p.y - 2);
	}

	/**
	 * ����������� ���������� �� ���������� ���� ���������, �� ������� ���������
	 * �����, �� �����.
	 * 
	 * @param mark
	 *        �����
	 */
	public void updateSizeInDoubleLt(final Mark mark) throws MapConnectionException, MapDataException {
		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

		mark.getPhysicalLink().sortNodes();

		final List<AbstractNode> nodes = mark.getPhysicalLink().getSortedNodes();

		DoublePoint from;
		final DoublePoint to = mark.getLocation();

		if (nodes.indexOf(mark.getNodeLink().getStartNode()) < nodes.indexOf(mark.getNodeLink().getEndNode())) {
			from = mark.getNodeLink().getStartNode().getLocation();
		}
		else {
			from = mark.getNodeLink().getEndNode().getLocation();
		}

		mark.setSizeInDoubleLt(converter.distance(from, to));
	}

	/**
	 * ����������� � ����� �� �������� ���������� �� ������ �����.
	 * 
	 * @param mark
	 *        �����
	 * @param topologicalDistance
	 *        �������������� ���������
	 */
	public void moveToFromStartLt(final Mark mark, double topologicalDistance) throws MapConnectionException, MapDataException {
		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

		mark.getPhysicalLink().sortNodeLinks();

		mark.setStartNode(mark.getPhysicalLink().getStartNode());

		if (topologicalDistance > mark.getPhysicalLink().getLengthLt()) {
			topologicalDistance = mark.getPhysicalLink().getLengthLt();
		}

		mark.setDistance(topologicalDistance);

		double cumulativeDistance = 0;

		for (final NodeLink nodeLink : mark.getPhysicalLink().getNodeLinks()) {
			mark.setNodeLink(nodeLink);
			final NodeLinkController nlc = (NodeLinkController) this.logicalNetLayer.getMapViewController().getController(mark.getNodeLink());
			nlc.updateLengthLt(mark.getNodeLink());
			if (cumulativeDistance + mark.getNodeLink().getLengthLt() > topologicalDistance) {
				final double distanceFromStart = topologicalDistance - cumulativeDistance;
				final DoublePoint newPoint = converter.pointAtDistance(mark.getStartNode().getLocation(),
						mark.getNodeLink().getOtherNode(mark.getStartNode()).getLocation(),
						distanceFromStart);
				mark.setLocation(newPoint);
				break;
			}

			cumulativeDistance += mark.getNodeLink().getLengthLt();
			mark.setStartNode(mark.getNodeLink().getOtherNode(mark.getStartNode()));
		}

		this.updateSizeInDoubleLt(mark);
	}

	/**
	 * Adjust marker position accurding to topological distance relative to
	 * current node link (which comprises startNode and endNode).
	 * 
	 * @param mark
	 *        �����
	 * @param screenDistance
	 *        �������� ����������
	 */
	public void adjustPosition(final Mark mark, final double screenDistance) throws MapConnectionException, MapDataException {
		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

		final Point sp = converter.convertMapToScreen(mark.getStartNode().getLocation());

		final double startNodeX = sp.x;
		final double startNodeY = sp.y;

		final Point ep = converter.convertMapToScreen(mark.getNodeLink().getOtherNode(mark.getStartNode()).getLocation());

		final double endNodeX = ep.x;
		final double endNodeY = ep.y;

		final double nodeLinkLength = Math.sqrt((endNodeX - startNodeX)
				* (endNodeX - startNodeX)
				+ (endNodeY - startNodeY)
				* (endNodeY - startNodeY));

		final double sinB = (endNodeY - startNodeY) / nodeLinkLength;

		final double cosB = (endNodeX - startNodeX) / nodeLinkLength;

		mark.setLocation(converter.convertScreenToMap(new Point((int) Math.round(startNodeX + cosB * screenDistance),
				(int) Math.round(startNodeY + sinB * screenDistance))));
	}

	@Override
	public Identifier getImageId(final AbstractNode node) {
		if (node.getImageId() == null) {
			final Mark mark = (Mark) node;
			mark.setImageId(imageId);
		}
		return node.getImageId();
	}
}
