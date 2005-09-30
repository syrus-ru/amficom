/*-
 * $$Id: SiteNodeController.java,v 1.19 2005/09/30 16:08:39 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;

/**
 * Контроллер сетевого узла.
 * 
 * @version $Revision: 1.19 $, $Date: 2005/09/30 16:08:39 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class SiteNodeController extends AbstractNodeController {
	static final int IMG_SIZE = 16;

	public static Image externalNodeImage = Toolkit.getDefaultToolkit().getImage("images/extlink2.gif").getScaledInstance(IMG_SIZE, //$NON-NLS-1$
			IMG_SIZE,
			Image.SCALE_SMOOTH);

	/**
	 * Private constructor.
	 */
	protected SiteNodeController(final NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	public static MapElementController createInstance(final NetMapViewer netMapViewer) {
		return new SiteNodeController(netMapViewer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paint(final MapElement mapElement, final Graphics g, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		if (!(mapElement instanceof SiteNode)) {
			return;
		}
		final SiteNode site = (SiteNode) mapElement;

		if (!MapPropertiesManager.isLayerVisible(site.getType())) {
			return;
		}

		if (!this.isElementVisible(site, visibleBounds)) {
			return;
		}

		super.paint(site, g, visibleBounds);

		//Если внешний узел то рисовать рамку
		if (this.logicalNetLayer.getMapView().getMap().getExternalNodes().contains(site)) {
			final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

			final Point p = converter.convertMapToScreen(site.getLocation());

			final int height = getBounds(site).height;

			final Graphics2D pg = (Graphics2D) g;

			pg.drawImage(SiteNodeController.externalNodeImage, p.x - IMG_SIZE / 2, p.y - height / 2 - IMG_SIZE, null);
		}

		if (MapPropertiesManager.isLayerLabelVisible(site.getType())) {
			final MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();

			final Point p = converter.convertMapToScreen(site.getLocation());

			final int width = getBounds(site).width;
			final int height = getBounds(site).height;

			g.setColor(MapPropertiesManager.getBorderColor());
			g.setFont(MapPropertiesManager.getFont());

			final int fontHeight = g.getFontMetrics().getHeight();
			final String text = site.getName();
			final int textWidth = g.getFontMetrics().stringWidth(text);
			final int centerX = p.x + width / 2;
			final int centerY = p.y + height;

			g.drawRect(centerX, centerY - fontHeight + 2, textWidth, fontHeight);

			g.setColor(MapPropertiesManager.getTextBackground());
			g.fillRect(centerX, centerY - fontHeight + 2, textWidth, fontHeight);

			g.setColor(MapPropertiesManager.getTextColor());
			g.drawString(text, centerX, centerY);
		}
	}
}
