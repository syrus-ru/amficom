/**
 * $Id: SiteNodeController.java,v 1.14 2005/06/16 10:57:20 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
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
 * @author $Author: krupenn $
 * @version $Revision: 1.14 $, $Date: 2005/06/16 10:57:20 $
 * @module mapviewclient_v1
 */
public class SiteNodeController extends AbstractNodeController {
	static final int IMG_SIZE = 16;

	public static Image externalNodeImage = Toolkit.getDefaultToolkit()
			.getImage("images/extlink2.gif").getScaledInstance(
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH);

	/**
	 * Instance
	 */
//	private static SiteNodeController instance = null;

	/**
	 * Private constructor.
	 */
	protected SiteNodeController(NetMapViewer netMapViewer) {
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
		
		//Если внешний узел то рисовать рамку
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
