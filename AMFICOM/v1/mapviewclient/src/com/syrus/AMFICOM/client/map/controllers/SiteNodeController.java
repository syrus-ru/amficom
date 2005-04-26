/**
 * $Id: SiteNodeController.java,v 1.8 2005/04/26 16:14:18 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import java.awt.Color;
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
import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;

/**
 * Контроллер сетевого узла.
 * @author $Author: krupenn $
 * @version $Revision: 1.8 $, $Date: 2005/04/26 16:14:18 $
 * @module mapviewclient_v1
 */
public class SiteNodeController extends AbstractNodeController
		implements VisualManager
{
	static final int IMG_SIZE = 16;

	public static Image externalNodeImage = Toolkit.getDefaultToolkit()
			.getImage("images/extlink.gif").getScaledInstance(
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
	protected SiteNodeController()
	{// empty
	}
	
	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new SiteNodeController();
		return instance;
	}

	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		// TODO Auto-generated method stub
		return null;
	}
	public ObjectResourceController getController() {
//		return PhysicalLinkWrapper.getInstance();
		return null;
	}
	
	public StorableObjectEditor getGeneralPropertiesPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint (MapElement mapElement, Graphics g, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException
	{
		if(!(mapElement instanceof SiteNode))
			return;
		SiteNode site = (SiteNode)mapElement;

		if(!isElementVisible(site, visibleBounds))
			return;
		
		super.paint(site, g, visibleBounds);
		
		//Если внешний узел то рисовать рамку
		if (getLogicalNetLayer().getMapView().getMap().getExternalNodes().contains(site))
		{
			MapCoordinatesConverter converter = getLogicalNetLayer();
			
			Point p = converter.convertMapToScreen(site.getLocation());
	
			int width = getBounds(site).width;
			int height = getBounds(site).height;

			Graphics2D pg = (Graphics2D )g; 
			
			pg.drawImage(
					SiteNodeController.externalNodeImage,
	                p.x + width / 2 - IMG_SIZE / 2,
	                p.y - height / 2 - IMG_SIZE / 2,
	                null);

			g.setColor(Color.BLUE);
			g.drawRect( 
					p.x - width / 2,
					p.y - height / 2,
					width,
					height);
		}

		if(MapPropertiesManager.isShowNodesNames())
		{
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
