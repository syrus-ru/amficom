/**
 * $Id: SiteNodeController.java,v 1.5 2005/04/06 17:41:12 krupenn Exp $
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
import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 * ���������� �������� ����.
 * @author $Author: krupenn $
 * @version $Revision: 1.5 $, $Date: 2005/04/06 17:41:12 $
 * @module mapviewclient_v1
 */
public class SiteNodeController extends AbstractNodeController
		implements VisualManager
{
	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapSitePane";

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
	 * �������� ��� ������ ������, ����������� �������� ���������� ����.
	 * @return ��� ������
	 */
	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
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
