/*-
 * $$Id: UnboundLinkController.java,v 1.12 2005/09/30 16:08:39 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.controllers;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.UnboundLink;

/**
 * ���������� �������� ������������� ����� (������� �������������� ������).
 *  
 * @version $Revision: 1.12 $, $Date: 2005/09/30 16:08:39 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class UnboundLinkController extends PhysicalLinkController {

	/**
	 * Private constructor.
	 */
	private UnboundLinkController(final NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	public static MapElementController createInstance(final NetMapViewer netMapViewer) {
		return new UnboundLinkController(netMapViewer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSelectionVisible(final MapElement me) {
		if (!(me instanceof UnboundLink)) {
			return false;
		}

		final UnboundLink link = (UnboundLink) me;

		final CableController cc = (CableController) this.logicalNetLayer.getMapViewController().getController(link.getCablePath());

		return link.isSelected() || cc.isSelectionVisible(link.getCablePath());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paint(final MapElement me, final Graphics g, final Rectangle2D.Double visibleBounds)
			throws MapConnectionException,
				MapDataException {
		if (!(me instanceof UnboundLink)) {
			return;
		}

		final UnboundLink link = (UnboundLink) me;

		if (!isElementVisible(link, visibleBounds)) {
			return;
		}

		final BasicStroke stroke = (BasicStroke) getStroke(link);
		final Stroke str = new BasicStroke(MapPropertiesManager.getUnboundThickness(),
				stroke.getEndCap(),
				stroke.getLineJoin(),
				stroke.getMiterLimit(),
				stroke.getDashArray(),
				stroke.getDashPhase());

		this.paint(link, g, visibleBounds, str, MapPropertiesManager.getUnboundLinkColor(), false);
	}
}
