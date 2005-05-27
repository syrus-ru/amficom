/**
 * $Id: SiteNodeController.java,v 1.11 2005/05/27 15:14:56 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;

import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;

/**
 * Контроллер сетевого узла.
 * @author $Author: krupenn $
 * @version $Revision: 1.11 $, $Date: 2005/05/27 15:14:56 $
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
	private static SiteNodeController instance = null;

	/**
	 * Private constructor.
	 */
	protected SiteNodeController() {
		// empty
	}

	/**
	 * Get instance.
	 * 
	 * @return instance
	 */
	public static MapElementController getInstance() {
		if(instance == null)
			instance = new SiteNodeController();
		return instance;
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
		if (getLogicalNetLayer().getMapView().getMap().getExternalNodes().contains(site)) {
			MapCoordinatesConverter converter = getLogicalNetLayer();
			
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
			MapCoordinatesConverter converter = getLogicalNetLayer();
			
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
