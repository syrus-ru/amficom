/**
 * $Id: MapViewController.java,v 1.2 2004/12/22 16:38:43 krupenn Exp $
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
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;

/**
 * ����� ������������ ��� �������� � ���������� �� ���������������
 * ��������� ������� � ��������� ����� � ������ �������������� ��������
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/12/22 16:38:43 $
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
		ctlMap.put(com.syrus.AMFICOM.map.TopologicalNode.class,
			TopologicalNodeController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.map.SiteNode.class,
			SiteNodeController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.map.NodeLink.class,
			NodeLinkController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.map.PhysicalLink.class,
			PhysicalLinkController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.map.Mark.class,
			MarkController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.map.Collector.class,
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
