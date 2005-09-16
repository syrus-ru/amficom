/**
 * $Id: CollectorController.java,v 1.13 2005/09/16 08:19:17 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLink;

/**
 * Контроллер коллектора.
 * @author $Author: krupenn $
 * @version $Revision: 1.13 $, $Date: 2005/09/16 08:19:17 $
 * @module mapviewclient
 */
public final class CollectorController extends AbstractLinkController {
	
	/**
	 * Private constructor.
	 */
	private CollectorController(final NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	public static MapElementController createInstance(final NetMapViewer netMapViewer) {
		return new CollectorController(netMapViewer);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText(final MapElement mapElement) {
		if (!(mapElement instanceof Collector))
			return null;

		final Collector collector = (Collector) mapElement;

		return collector.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSelectionVisible(final MapElement mapElement) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isElementVisible(final MapElement mapElement, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		if (!(mapElement instanceof Collector))
			return false;

		final Collector collector = (Collector) mapElement;

		boolean vis = false;
		for (final PhysicalLink link : collector.getPhysicalLinks()) {
			final PhysicalLinkController plc = (PhysicalLinkController) this.logicalNetLayer.getMapViewController().getController(link);
			if (plc.isElementVisible(link, visibleBounds)) {
				vis = true;
				break;
			}
		}
		return vis;
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(final MapElement mapElement, final Graphics g, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		if (!(mapElement instanceof Collector)) {
			return;
		}

		final Collector collector = (Collector) mapElement;

		if (!this.isElementVisible(collector, visibleBounds)) {
			return;
		}

		final BasicStroke stroke = (BasicStroke) getStroke(collector);
		final Stroke str = new BasicStroke(getLineSize(collector),
				stroke.getEndCap(),
				stroke.getLineJoin(),
				stroke.getMiterLimit(),
				stroke.getDashArray(),
				stroke.getDashPhase());
		final Color color = getColor(collector);

		for (final PhysicalLink link : collector.getPhysicalLinks()) {
			final PhysicalLinkController plc = (PhysicalLinkController) this.logicalNetLayer.getMapViewController().getController(link);
			plc.paint(link, g, visibleBounds, str, color, false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isMouseOnElement(final MapElement mapElement, final Point currentMousePoint)
			throws MapConnectionException,
				MapDataException {
		// if(! (mapElement instanceof Collector))
		// return false;
		//
		// final Collector collector = (Collector )mapElement;

		return false;
	}

	public Rectangle2D getBoundingRectangle(MapElement mapElement) throws MapConnectionException, MapDataException {
		final Collector collector = (Collector) mapElement;
		Rectangle2D rectangle = new Rectangle();
		for(PhysicalLink physicalLink : collector.getPhysicalLinks()) {
			final PhysicalLinkController controller = (PhysicalLinkController) this.logicalNetLayer.getMapViewController().getController(physicalLink);
			rectangle = rectangle.createUnion(controller.getBoundingRectangle(physicalLink));
		}
		return rectangle;
	}
}
