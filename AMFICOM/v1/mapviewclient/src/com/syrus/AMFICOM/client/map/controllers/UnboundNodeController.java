/**
 * $Id: UnboundNodeController.java,v 1.11 2005/08/11 12:43:30 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
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
 * ���������� �������������� ����� (������� �����).
 * @author $Author: arseniy $
 * @version $Revision: 1.11 $, $Date: 2005/08/11 12:43:30 $
 * @module mapviewclient
 */
public class UnboundNodeController extends SiteNodeController
{
	/**
	 * Private constructor.
	 */
	private UnboundNodeController(NetMapViewer netMapViewer) {
		super(netMapViewer);
	}
	
	public static MapElementController createInstance(NetMapViewer netMapViewer) {
		return new UnboundNodeController(netMapViewer);
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(
			MapElement mapElement,
			Graphics g,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		if(!(mapElement instanceof UnboundNode))
			return;
		UnboundNode unbound = (UnboundNode)mapElement;

		if(!isElementVisible(unbound, visibleBounds))
			return;
		
		super.paint(unbound, g, visibleBounds);
		
		MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();
		
		Point p = converter.convertMapToScreen(unbound.getLocation());

		Graphics2D pg = (Graphics2D )g;
		
		int width = getBounds(unbound).width + 20;
		int height = getBounds(unbound).height + 20;
		
		pg.setStroke(new BasicStroke(MapPropertiesManager.getUnboundThickness()));
		if(unbound.getCanBind()) {
			pg.setColor(MapPropertiesManager.getCanBindColor());
		}
		else {
			pg.setColor(MapPropertiesManager.getUnboundElementColor());
		}
		pg.drawRect( 
				p.x - width / 2,
				p.y - height / 2,
				width,
				height);
	}
}
