/**
 * $Id: MapStrategyManager.java,v 1.14 2005/08/11 12:43:32 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.strategy;

import java.util.HashMap;

import com.syrus.AMFICOM.map.MapElement;

/**
 * ��������� ��������� ��� ��������� �����.
 * @author $Author: arseniy $
 * @version $Revision: 1.14 $, $Date: 2005/08/11 12:43:32 $
 * @module mapviewclient
 */
public final class MapStrategyManager 
{
	/**
	 * ����������� ������, ��� ��� � ������ ������������ ������ �����������
	 * ������.
	 */
	private MapStrategyManager()
	{//empty
	}

	/**
	 * ������� ���������.
	 */
	private static java.util.Map strategyMap = new HashMap();

	static
	{
		strategyMap.put(com.syrus.AMFICOM.mapview.AlarmMarker.class,
			MapAlarmMarkerStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.mapview.CablePath.class,
			MapCablePathElementStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.mapview.EventMarker.class,
			MapEventMarkerStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.map.Mark.class,
			MapMarkElementStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.map.NodeLink.class,
			MapNodeLinkElementStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.mapview.Marker.class,
			MapMarkerStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.mapview.MeasurementPath.class,
			MapPathElementStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.map.PhysicalLink.class,
			MapPhysicalLinkElementStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.map.TopologicalNode.class,
			MapPhysicalNodeElementStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.map.SiteNode.class,
			MapSiteNodeElementStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.mapview.UnboundNode.class,
			MapUnboundNodeElementStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.mapview.VoidElement.class,
			MapVoidElementStrategy.getInstance());
		strategyMap.put(com.syrus.AMFICOM.mapview.Selection.class,
			MapSelectionElementStrategy.getInstance());
	}
	
	/**
	 * �������� ��������� �������� � ��������� �����.
	 * @param mapElement ������� �����
	 * @return ���������
	 */
	public static MapStrategy getStrategy(MapElement mapElement)
	{
		MapStrategy strategy = (MapStrategy )strategyMap.get(mapElement.getClass());
		if(strategy != null)
		{
			strategy.setMapElement(mapElement);
		}
		return strategy;
	}
}
