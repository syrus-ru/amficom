/**
 * $Id: MapViewController.java,v 1.1 2004/12/07 17:05:54 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import com.syrus.AMFICOM.Client.Resource.Map.*;

import java.util.HashMap;

/**
 * ����� ������������ ��� �������� � ���������� �� ���������������
 * ��������� ������� � ��������� ����� � ������ �������������� ��������
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/12/07 17:05:54 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapViewController
{
	private static java.util.Map ctlMap = new HashMap();
	
	private static MapViewController instance = null;
	
	private LogicalNetLayer lnl;
	
	private MapViewController(LogicalNetLayer lnl)
	{
		this.lnl = lnl;
	}
	
	public static MapViewController getInstance(LogicalNetLayer lnl)
	{
		if(instance == null)
			instance = new MapViewController(lnl);
		return instance;
	}

	static
	{
		ctlMap.put(MapPhysicalNodeElement.class,
			TopologicalNodeController.getInstance());
		ctlMap.put(MapSiteNodeElement.class,
			SiteNodeController.getInstance());
		ctlMap.put(MapNodeLinkElement.class,
			NodeLinkController.getInstance());
		ctlMap.put(MapPhysicalLinkElement.class,
			PhysicalLinkController.getInstance());
		ctlMap.put(MapMarkElement.class,
			MarkController.getInstance());
		ctlMap.put(MapPipePathElement.class,
			CollectorController.getInstance());

		ctlMap.put(MapCablePathElement.class,
			CableController.getInstance());
		ctlMap.put(MapMeasurementPathElement.class,
			MeasurementPathController.getInstance());
		ctlMap.put(MapUnboundNodeElement.class,
			UnboundNodeController.getInstance());
		ctlMap.put(MapUnboundLinkElement.class,
			UnboundLinkController.getInstance());
		ctlMap.put(MapMarker.class,
			MarkerController.getInstance());
	}

	public MapElementController getController(MapElement me)
	{
		MapElementController controller = (MapElementController )ctlMap.get(me.getClass());
		if(controller != null)
			controller.setLogicalNetLayer(lnl);
		return controller;
	}


	/**
	 * ��������� ��������
	 */
	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		getController(me).paint(me, g, visibleBounds);
	}

	/**
	 * ���������� ����, �����������, ��� ����� currentMousePoint ���������
	 * � ������������ �������� ��������. ��� ���� ������� ������������
	 * ��������� ������, ��� ����� ������-������������ �����. ������ ��������
	 * ����� mouseTolerancy
	 */
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
	{
		return getController(me).isMouseOnElement(me, currentMousePoint);
	}

	/**
	 * ����������, �������� �� ������� � ������� visibleBounds.
	 * ������������ ��� ��������� (������������ ������ ��������, ��������
	 * � ������� �������)
	 */
	public boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds)
	{
		return getController(me).isElementVisible(me, visibleBounds);
	}

	/**
	 * ����� ����������� ���������
	 */
	String getToolTipText(MapElement me)
	{
		return getController(me).getToolTipText(me);
	}

}
