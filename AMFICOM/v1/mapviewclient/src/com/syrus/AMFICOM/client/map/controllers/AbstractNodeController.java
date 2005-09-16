/**
 * $Id: AbstractNodeController.java,v 1.18 2005/09/16 08:19:17 krupenn Exp $
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
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;

/**
 * ���������� ����.
 * @author $Author: krupenn $
 * @version $Revision: 1.18 $, $Date: 2005/09/16 08:19:17 $
 * @module mapviewclient
 */
public abstract class AbstractNodeController extends AbstractMapElementController {
	/** ������ ����������� �����������. */
	public static final Rectangle DEFAULT_BOUNDS = new Rectangle(14, 14);
	/** ����������� ������ ��������. */
	public static final Rectangle MIN_BOUNDS = new Rectangle(6, 6);
	/** ������������ ������ ��������. */
	public static final Rectangle MAX_BOUNDS = new Rectangle(40, 40);

	/** ������ ����������� � �������� �����������. */
	protected static java.util.Map<AbstractNode, Rectangle> boundsContainer = new HashMap<AbstractNode, Rectangle>();

	/**
	 * ��������� ������ �������� ������ �������. ������������ ��� �����������
	 * ��������� ������ �������� � �������.
	 */
	protected static Point2D.Double anchorContainer = new Point2D.Double(0.0D, 0.0D);

	/**
	 * ��������� ������ ������� ������. ������������ ��� �����������
	 * ��������� ������ �������� � ������� �������.
	 */
	protected static Rectangle searchBounds = new Rectangle();

	public AbstractNodeController(final NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	/**
	 * �������� ������ ����������� �����������.
	 * @return ������ ����������� �����������
	 */
	public Rectangle getDefaultBounds() {
		return AbstractNodeController.DEFAULT_BOUNDS;
	}

	/**
	 * �������� ����������� ������ ��������.
	 * 
	 * @return ����������� ������ ��������
	 */
	public Rectangle getMinBounds() {
		return AbstractNodeController.MIN_BOUNDS;
	}

	/**
	 * �������� ������������ ������ ��������.
	 * 
	 * @return ������������ ������ ��������
	 */
	public Rectangle getMaxBounds() {
		return AbstractNodeController.MAX_BOUNDS;
	}

	/**
	 * ���������� ����������� ��������������� �����������. ��� ���� �����������
	 * ������� ��������.
	 * 
	 * @param mapElement ������� �����
	 */
	public void updateScaleCoefficient(final MapElement mapElement) {
		if (!(mapElement instanceof AbstractNode)) {
			return;
		}
		final AbstractNode node = (AbstractNode) mapElement;

		final double scaleCoefficient = this.logicalNetLayer.getDefaultScale() / this.logicalNetLayer.getCurrentScale();

		int w = (int) (getDefaultBounds().getWidth() * scaleCoefficient);
		int h = (int) (getDefaultBounds().getHeight() * scaleCoefficient);

		if (w >= this.getMaxBounds().width || h >= this.getMaxBounds().height) {
			w = this.getMaxBounds().width;
			h = this.getMaxBounds().height;
		} else if (w <= this.getMinBounds().width || h <= this.getMinBounds().height) {
			w = this.getMinBounds().width;
			h = this.getMinBounds().height;
		}
		this.setBounds(node, new Rectangle(w, h));

		MapPropertiesManager.setScaledImageSize(this.getImageId(node), w, h);
	}

	public Identifier getImageId(final AbstractNode node) {
		return node.getImageId();
	}

	/**
	 * ��������� ������� ��������. ��������� ����� ������ ��������������
	 * �������� ����� ��� ����������� �����, ������� �������� �
	 * ���-�������.
	 * @param node ����
	 * @return ������� �����������
	 */
	public Rectangle getBounds(final AbstractNode node) {
		Rectangle rect = boundsContainer.get(node);
		if (rect == null) {
			rect = new Rectangle(DEFAULT_BOUNDS);
			boundsContainer.put(node, rect);
		}
		return rect;
	}

	/**
	 * ���������� ������� ��������.
	 * 
	 * @param node ����
	 * @param rect ������� �����������
	 */
	public void setBounds(final AbstractNode node, final Rectangle rect) {
		boundsContainer.put(node, rect);
	}

	public Rectangle2D getBoundingRectangle(final MapElement mapElement) throws MapConnectionException, MapDataException {
		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();
		final Point p = converter.convertMapToScreen(mapElement.getLocation());
		Rectangle bounds = this.getBounds((AbstractNode) mapElement);
		final int width = bounds.width;
		final int height = bounds.height;
		return new Rectangle(
				p.x - width / 2, 
				p.y - height / 2,
				width,
				height);
	}

	/**
	 * �������� ����������� ��������.
	 * 
	 * @param node ����
	 * @return �����������
	 */
	public Image getImage(final AbstractNode node) {
		return MapPropertiesManager.getScaledImage(node.getImageId());
	}

	/**
	 * �������� ����������� �������� ��� ������� ������� �������.
	 * 
	 * @param node ����
	 * @return �����������
	 */
	public Image getAlarmedImage(final AbstractNode node) {
		return this.getImage(node);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isElementVisible(final MapElement mapElement, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		if (!(mapElement instanceof AbstractNode)) {
			return false;
		}
		final AbstractNode node = (AbstractNode) mapElement;
		anchorContainer.setLocation(node.getLocation().getX(), node.getLocation().getY());
		return visibleBounds.contains(anchorContainer);
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(final MapElement mapElement, final Graphics g, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		if (!(mapElement instanceof AbstractNode)) {
			return;
		}
		final AbstractNode node = (AbstractNode) mapElement;

		if (!isElementVisible(node, visibleBounds)) {
			return;
		}

		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

		final Point p = converter.convertMapToScreen(node.getLocation());

		final Graphics2D pg = (Graphics2D) g;

		final int width = getBounds(node).width;
		final int height = getBounds(node).height;

		pg.drawImage(this.getImage(node), p.x - width / 2, p.y - height / 2, null);

		// ���� �� �������� ���� ������ �������, �� ��������
		// � ����������� �� ����� getShowAlarmed()
		if (node.getAlarmState()) {
			if (MapPropertiesManager.isDrawAlarmed()) {
				pg.drawImage(getAlarmedImage(node), p.x - width / 2, p.y - height / 2, null);

				pg.setStroke(MapPropertiesManager.getAlarmedStroke());
				pg.setColor(MapPropertiesManager.getAlarmedColor());
				pg.drawRect(p.x - width / 2, p.y - height / 2, width, height);
			}
		}

		// ���� ������� �� �������� �����
		if (node.isSelected()) {
			pg.setStroke(new BasicStroke(3));
			pg.setColor(MapPropertiesManager.getSelectionColor());
			pg.drawRect(p.x - width / 2, p.y - height / 2, width, height);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isMouseOnElement(final MapElement mapElement, final Point currentMousePoint)
			throws MapConnectionException,
				MapDataException {
		if (!(mapElement instanceof AbstractNode)) {
			return false;
		}

		final AbstractNode node = (AbstractNode) mapElement;

		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

		// �������� ���� ��� ������ ���������� � ��������������
		final int width = (int) getBounds(node).getWidth();
		final int height = (int) getBounds(node).getHeight();
		final Point p = converter.convertMapToScreen(node.getLocation());
		searchBounds.setBounds(p.x - width / 2, p.y - height / 2, width, height);
		if (searchBounds.contains(currentMousePoint)) {
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText(final MapElement mapElement) {
		if (!(mapElement instanceof AbstractNode)) {
			return null;
		}

		final AbstractNode node = (AbstractNode) mapElement;

		final String s1 = node.getName();

		return s1 + " [" + MapViewController.getMapElementReadableType(node) + "]";
	}

}
