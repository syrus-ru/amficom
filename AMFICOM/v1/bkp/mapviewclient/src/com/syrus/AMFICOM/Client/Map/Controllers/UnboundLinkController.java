/**
 * $Id: UnboundLinkController.java,v 1.4 2005/02/18 12:19:46 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.map.MapElement;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

/**
 * ���������� �������� ������������� ����� (������� �������������� ������). 
 * @author $Author: krupenn $
 * @version $Revision: 1.4 $, $Date: 2005/02/18 12:19:46 $
 * @module mapviewclient_v1
 */
public final class UnboundLinkController extends PhysicalLinkController
{
	private static final String PROPERTY_PANE_CLASS_NAME = 
			"";

	/**
	 * Instace.
	 */
	private static UnboundLinkController instance = null;
	
	/**
	 * Private constructor.
	 */
	private UnboundLinkController()
	{// empty
	}
	
	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new UnboundLinkController();
		return instance;
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
	public boolean isSelectionVisible(MapElement me)
	{
		if(! (me instanceof UnboundLink))
			return false;

		UnboundLink link = (UnboundLink)me;
		
		CableController cc = (CableController)getLogicalNetLayer().getMapViewController().getController(link.getCablePath());

		return link.isSelected() || cc.isSelectionVisible(link.getCablePath());
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
		throws MapConnectionException, MapDataException
	{
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
