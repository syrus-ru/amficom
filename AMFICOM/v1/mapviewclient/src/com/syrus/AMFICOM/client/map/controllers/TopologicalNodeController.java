/*-
 * $$Id: TopologicalNodeController.java,v 1.24 2005/09/30 16:08:39 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

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
import com.syrus.AMFICOM.map.TopologicalNode;

/**
 * ���������� ��������������� ����.
 * 
 * @version $Revision: 1.24 $, $Date: 2005/09/30 16:08:39 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class TopologicalNodeController extends AbstractNodeController {
	/** ������ ����������� �����������. */
	public static final Rectangle NODE_BOUNDS = new Rectangle(10, 10);
	/** ����������� ������ ��������. */
	public static final Rectangle MIN_NODE_BOUNDS = new Rectangle(2, 2);
	/** ������������ ������ ��������. */
	public static final Rectangle MAX_NODE_BOUNDS = new Rectangle(15, 15);

	/** ��� ����������� ��������� ��������������� ����. */
	public static final String CLOSED_NODE = "node"; //$NON-NLS-1$
	/** ��� ����������� ������������������������ ����. */
	public static final String OPEN_NODE = "void"; //$NON-NLS-1$

	/** ����������� ��������� ��������������� ����. */
	public static final String CLOSED_NODE_IMAGE = "images/node.gif"; //$NON-NLS-1$
	/** ����������� ��������� ��������������� ����. */
	public static final String OPEN_NODE_IMAGE = "images/void.gif"; //$NON-NLS-1$

	/**
	 * ���� ������������� ���������������� ����������� �������� �������.
	 * ������������� ���������� ���� ��� ��� ������ ��������� � ��������� �������.
	 */
	private static boolean needInit = true;

	/** ������������� ����������� ��������� ��������������� ����. */
	private static Identifier openImageId;
	/** ������������� ����������� ������������������������ ����. */
	private static Identifier closedImageId;

	/**
	 * Private constructor.
	 */
	private TopologicalNodeController(final NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	public static MapElementController createInstance(final NetMapViewer netMapViewer) {
		return new TopologicalNodeController(netMapViewer);
	}

	public static void init() throws ApplicationException {
		if (needInit) {
			openImageId = NodeTypeController.getImageId(
					TopologicalNodeController.OPEN_NODE,
					TopologicalNodeController.OPEN_NODE_IMAGE);
			closedImageId = NodeTypeController.getImageId(
					TopologicalNodeController.CLOSED_NODE,
					TopologicalNodeController.CLOSED_NODE_IMAGE);

			MapPropertiesManager.setOriginalImage(openImageId, new ImageIcon(TopologicalNodeController.OPEN_NODE_IMAGE).getImage());
			MapPropertiesManager.setOriginalImage(closedImageId, new ImageIcon(TopologicalNodeController.CLOSED_NODE_IMAGE).getImage());

			needInit = false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Rectangle getDefaultBounds() {
		return TopologicalNodeController.NODE_BOUNDS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Rectangle getMinBounds() {
		return TopologicalNodeController.MIN_NODE_BOUNDS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Rectangle getMaxBounds() {
		return TopologicalNodeController.MAX_NODE_BOUNDS;
	}

	/**
	 * E��������� ���������� ��������������� ����. ������ ��������� ����� ������
	 * ����������� � ������������ � ����������� ����.
	 * 
	 * @param node ��������������� ����
	 * @param active ���� ����������
	 */
	public void setActive(final TopologicalNode node, final boolean active) {
		node.setActive(active);

		if (active) {
			node.setImageId(openImageId);
		}
		else {
			node.setImageId(closedImageId);
		}
	}

	@Override
	public Identifier getImageId(final AbstractNode node) {
		if (node.getImageId() == null) {
			final TopologicalNode topologicalNode = (TopologicalNode) node;

			if (topologicalNode.isActive()) {
				topologicalNode.setImageId(openImageId);
			}
			else {
				topologicalNode.setImageId(closedImageId);
			}
		}
		return node.getImageId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getImage(final AbstractNode node) {
		final TopologicalNode topologicalNode = (TopologicalNode) node;

		if (topologicalNode.isActive()) {
			node.setImageId(openImageId);
		}
		else {
			node.setImageId(closedImageId);
		}

		if (topologicalNode.isActive()) {
			return MapPropertiesManager.getScaledImage(closedImageId);
		}

		return MapPropertiesManager.getScaledImage(openImageId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paint(final MapElement mapElement, final Graphics g, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		if (!(mapElement instanceof TopologicalNode)) {
			return;
		}
		final TopologicalNode node = (TopologicalNode) mapElement;

		if (!super.isElementVisible(node, visibleBounds)) {
			return;
		}

		super.paint(node, g, visibleBounds);

		if (node.isCanBind()) {
			final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

			final Point p = converter.convertMapToScreen(node.getLocation());

			final Graphics2D pg = (Graphics2D) g;

			final int width = getBounds(node).width + 20;
			final int height = getBounds(node).height + 20;

			pg.setStroke(new BasicStroke(MapPropertiesManager.getUnboundThickness()));
			pg.setColor(MapPropertiesManager.getCanBindColor());
			pg.drawRect(p.x - width / 2, p.y - height / 2, width, height);
		}
	}
}
