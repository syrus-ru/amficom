/**
 * $Id: UnboundLinkController.java,v 1.7 2005/06/16 10:57:20 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.controllers;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.map.MapElement;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

/**
 * Контроллер элемента непривязанной линии (участка непривязанного кабеля). 
 * @author $Author: krupenn $
 * @version $Revision: 1.7 $, $Date: 2005/06/16 10:57:20 $
 * @module mapviewclient_v1
 */
public final class UnboundLinkController extends PhysicalLinkController {

	/**
	 * Instace.
	 */
//	private static UnboundLinkController instance = null;

	/**
	 * Private constructor.
	 */
	private UnboundLinkController(NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	/**
	 * Get instance.
	 * 
	 * @return instance
	 */
//	public static MapElementController getInstance() {
//		return instance;
//	}

	public static MapElementController createInstance(NetMapViewer netMapViewer) {
		return new UnboundLinkController(netMapViewer);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelectionVisible(MapElement me) {
		if(!(me instanceof UnboundLink))
			return false;

		UnboundLink link = (UnboundLink)me;
		
		CableController cc = (CableController)this.logicalNetLayer.getMapViewController().getController(link.getCablePath());

		return link.isSelected() || cc.isSelectionVisible(link.getCablePath());
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(
			MapElement me,
			Graphics g,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		if(! (me instanceof UnboundLink))
			return;

		UnboundLink link = (UnboundLink)me;
		
		if(!isElementVisible(link, visibleBounds))
			return;

		BasicStroke stroke = (BasicStroke )getStroke(link);
		Stroke str = new BasicStroke(
				MapPropertiesManager.getUnboundThickness(), 
				stroke.getEndCap(), 
				stroke.getLineJoin(), 
				stroke.getMiterLimit(), 
				stroke.getDashArray(), 
				stroke.getDashPhase());

		paint(link, g, visibleBounds, str, MapPropertiesManager.getUnboundLinkColor(), false);
	}
}
