/**
 * $Id: SiteNodeController.java,v 1.16 2005/08/11 12:43:30 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
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
 * ���������� �������� ����.
 * @author $Author: arseniy $
 * @version $Revision: 1.16 $, $Date: 2005/08/11 12:43:30 $
 * @module mapviewclient
 */
public class SiteNodeController extends AbstractNodeController {
	static final int IMG_SIZE = 16;

	public static Image externalNodeImage = Toolkit.getDefaultToolkit()
			.getImage("images/extlink2.gif").getScaledInstance(
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH);

	/**
	 * Private constructor.
	 */
	protected SiteNodeController(NetMapViewer netMapViewer) {
		super(netMapViewer);
	}

	public static MapElementController createInstance(NetMapViewer netMapViewer) {
		return new SiteNodeController(netMapViewer);
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint(
			MapElement mapElement,
			Graphics g,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		if(!(mapElement instanceof SiteNode))
			return;
		SiteNode site = (SiteNode)mapElement;

		if(!MapPropertiesManager.isLayerVisible(site.getType()))
			return;
		
		if(!isElementVisible(site, visibleBounds))
			return;
		
		super.paint(site, g, visibleBounds);
		
		//���� ������� ���� �� �������� �����
		if (this.logicalNetLayer.getMapView().getMap().getExternalNodes().contains(site)) {
			MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();
			
			Point p = converter.convertMapToScreen(site.getLocation());
	
			int height = getBounds(site).height;

			Graphics2D pg = (Graphics2D )g; 
			
			pg.drawImage(
					SiteNodeController.externalNodeImage,
	                p.x - IMG_SIZE / 2,
	                p.y - height / 2 - IMG_SIZE,
	                null);
		}

		if(MapPropertiesManager.isLayerLabelVisible(site.getType())) {
			MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();
			
			Point p = converter.convertMapToScreen(site.getLocation());
	
			int width = getBounds(site).width;
			int height = getBounds(site).height;

			g.setColor(MapPropertiesManager.getBorderColor());
			g.setFont(MapPropertiesManager.getFont());

			int fontHeight = g.getFontMetrics().getHeight();
			String text = site.getName();
			int textWidth = g.getFontMetrics().stringWidth(text);
			int centerX = p.x + width / 2;
			int centerY = p.y + height;

			g.drawRect(
					centerX,
					centerY - fontHeight + 2,
					textWidth,
					fontHeight);

			g.setColor(MapPropertiesManager.getTextBackground());
			g.fillRect(
					centerX,
					centerY - fontHeight + 2,
					textWidth,
					fontHeight);

			g.setColor(MapPropertiesManager.getTextColor());
			g.drawString(
					text,
					centerX,
					centerY);
		}
	}
}
