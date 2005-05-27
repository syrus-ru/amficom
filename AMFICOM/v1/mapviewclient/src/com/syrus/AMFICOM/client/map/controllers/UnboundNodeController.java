/**
 * $Id: UnboundNodeController.java,v 1.5 2005/05/27 15:14:56 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.UnboundNode;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 * ���������� �������������� ����� (������� �����).
 * @author $Author: krupenn $
 * @version $Revision: 1.5 $, $Date: 2005/05/27 15:14:56 $
 * @module mapviewclient_v1
 */
public class UnboundNodeController extends SiteNodeController
{
	/**
	 * Instace.
	 */
	private static UnboundNodeController instance = null;
	
	/**
	 * Private constructor.
	 */
	private UnboundNodeController() {
		// empty
	}
	
	/**
	 * Get instance.
	 * 
	 * @return instance
	 */
	public static MapElementController getInstance() {
		if(instance == null)
			instance = new UnboundNodeController();
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
		if(!(mapElement instanceof UnboundNode))
			return;
		UnboundNode unbound = (UnboundNode)mapElement;

		if(!isElementVisible(unbound, visibleBounds))
			return;
		
		super.paint(unbound, g, visibleBounds);
		
		MapCoordinatesConverter converter = getLogicalNetLayer();
		
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
