/**
 * $Id: UnboundNodeController.java,v 1.12 2005/08/11 17:08:10 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.UnboundNode;

/**
 * Контроллер непривязанного узела (элемент схемы).
 * @author $Author: arseniy $
 * @version $Revision: 1.12 $, $Date: 2005/08/11 17:08:10 $
 * @module mapviewclient
 */
public class UnboundNodeController extends SiteNodeController {
	/**
	 * Private constructor.
	 */
	private UnboundNodeController(final NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	public static MapElementController createInstance(final NetMapViewer netMapViewer) {
		return new UnboundNodeController(netMapViewer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paint(final MapElement mapElement, final Graphics g, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		if (!(mapElement instanceof UnboundNode)) {
			return;
		}
		final UnboundNode unbound = (UnboundNode) mapElement;

		if (!super.isElementVisible(unbound, visibleBounds)) {
			return;
		}

		super.paint(unbound, g, visibleBounds);

		final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

		final Point p = converter.convertMapToScreen(unbound.getLocation());

		final Graphics2D pg = (Graphics2D) g;

		final int width = super.getBounds(unbound).width + 20;
		int height = super.getBounds(unbound).height + 20;

		pg.setStroke(new BasicStroke(MapPropertiesManager.getUnboundThickness()));
		if (unbound.getCanBind()) {
			pg.setColor(MapPropertiesManager.getCanBindColor());
		} else {
			pg.setColor(MapPropertiesManager.getUnboundElementColor());
		}
		pg.drawRect(p.x - width / 2, p.y - height / 2, width, height);
	}
}
