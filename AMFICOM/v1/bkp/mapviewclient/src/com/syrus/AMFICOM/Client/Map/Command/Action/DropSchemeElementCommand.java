/**
 * $Id: DropSchemeElementCommand.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * ���������� ������� ���� mpe �� �����. ������������ ��� �������� 
 * (drag/drop), � ����� point (� �������� �����������)
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class DropSchemeElementCommand extends MapActionCommand
{
	/**
	 * ��������� �������� �����
	 */
	MapSiteNodeElement site = null;
	MapUnboundNodeElement unbound = null;
	SchemeElement se = null;
	String prevSiteId;
	
	Map map;
	
	/**
	 * �����, � ������� ��������� ����� �������������� ����
	 */
	Point point;

	public DropSchemeElementCommand(
			SchemeElement se,
			Point point)
	{
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.se = se;
		this.point = point;
	}

	public void execute()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "execute()");

		if ( !getLogicalNetLayer().getContext().getApplicationModel()
				.isEnabled("mapActionCreateEquipment"))
			return;
		
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
	
		Point2D.Double coordinatePoint = logicalNetLayer.convertScreenToMap(point);
		
		map = logicalNetLayer.getMapView().getMap();
		
		MapElement me = logicalNetLayer.getMapElementAtPoint(point);
		
		if(me instanceof MapSiteNodeElement
			&& !(me instanceof MapUnboundNodeElement))
		{
			site = (MapSiteNodeElement )me;
			se.site_id = site.getId();
		}
		else
		{
			unbound = new MapUnboundNodeElement(
				se,
				dataSource.GetUId(MapSiteNodeElement.typ),
				coordinatePoint,
				map,
				logicalNetLayer.getDefaultScale() / logicalNetLayer.getScale(),
				logicalNetLayer.getUnboundProto());
		
			Pool.put(MapSiteNodeElement.typ, unbound.getId(), unbound);
			map.addNode(unbound);
			
			site = unbound;
		}

		// �������� ��������� - ���������� ����������
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
				site, 
				MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.setCurrentMapElement(site);
		logicalNetLayer.notifySchemeEvent(site);
		logicalNetLayer.notifyCatalogueEvent(site);

	}
	
	public void undo()
	{
		if(unbound == null)
		{
			se.site_id = prevSiteId;
		}
		else
		{
			map.removeNode(unbound);
		}
	}
	
	public void redo()
	{
		if(unbound == null)
		{
			se.site_id = site.getId();
		}
		else
		{
			map.addNode(unbound);
		}
	}
}
