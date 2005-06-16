/**
 * $Id: CollectorController.java,v 1.9 2005/06/16 10:57:20 krupenn Exp $
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
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLink;

/**
 * Контроллер коллектора.
 * @author $Author: krupenn $
 * @version $Revision: 1.9 $, $Date: 2005/06/16 10:57:20 $
 * @module mapviewclient_v1
 */
public final class CollectorController extends AbstractLinkController {
	/**
	 * Instance.
	 */
//	private static CollectorController instance = null;
	
	/**
	 * Private constructor.
	 */
	private CollectorController(NetMapViewer netMapViewer) {
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
		return new CollectorController(netMapViewer);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getToolTipText(MapElement mapElement) {
		if(!(mapElement instanceof Collector))
			return null;

		Collector collector = (Collector )mapElement;

		return collector.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelectionVisible(MapElement mapElement) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isElementVisible(
			MapElement mapElement,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		if(!(mapElement instanceof Collector))
			return false;

		Collector collector = (Collector )mapElement;

		boolean vis = false;
		for(Iterator it = collector.getPhysicalLinks().iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink )it.next();
			PhysicalLinkController plc = (PhysicalLinkController)this.logicalNetLayer.getMapViewController().getController(link);
			if(plc.isElementVisible(link, visibleBounds)) {
				vis = true;
				break;
			}
		}
		return vis;
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(
			MapElement mapElement,
			Graphics g,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		if(!(mapElement instanceof Collector))
			return;

		Collector collector = (Collector )mapElement;
		
		if(!isElementVisible(collector, visibleBounds))
			return;

		BasicStroke stroke = (BasicStroke )getStroke(collector);
		Stroke str = new BasicStroke(
				getLineSize(collector), 
				stroke.getEndCap(), 
				stroke.getLineJoin(), 
				stroke.getMiterLimit(), 
				stroke.getDashArray(), 
				stroke.getDashPhase());
		Color color = getColor(collector);

		for(Iterator it = collector.getPhysicalLinks().iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink )it.next();
			PhysicalLinkController plc = (PhysicalLinkController)this.logicalNetLayer.getMapViewController().getController(link);
			plc.paint(link, g, visibleBounds, str, color, false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isMouseOnElement(
			MapElement mapElement,
			Point currentMousePoint)
			throws MapConnectionException, MapDataException {
//		if(! (mapElement instanceof Collector))
//			return false;
//
//		Collector collector = (Collector )mapElement;
		
		return false;
	}

}
