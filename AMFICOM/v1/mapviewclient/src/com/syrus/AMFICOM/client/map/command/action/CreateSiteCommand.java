/**
 * $Id: CreateSiteCommand.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.ResourceUtil;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;

import java.awt.Point;
import java.awt.geom.Point2D;

import java.util.HashMap;

/**
 * ���������� ������� ���� mpe �� �����. ������������ ��� �������� 
 * (drag/drop), � ����� point (� �������� �����������)
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class CreateSiteCommand extends MapActionCommand
{
	/**
	 * ��������� �������� �����
	 */
	MapSiteNodeElement site;
	MapNodeProtoElement proto;
	
	Map map;
	
	/**
	 * �����, � ������� ��������� ����� �������������� ����
	 */
	Point point;

	public CreateSiteCommand(
			MapNodeProtoElement proto,
			Point point)
	{
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.proto = proto;
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

		// ������� ����� ����
		site = new MapSiteNodeElement(
				dataSource.GetUId( MapSiteNodeElement.typ),
				coordinatePoint,
				map,
				logicalNetLayer.getDefaultScale() / logicalNetLayer.getScale(),
				proto);

		// ���������� �������� ����������� �� �������������
		site.attributes = (HashMap )ResourceUtil.copyAttributes(dataSource, proto.attributes);

		Pool.put(MapSiteNodeElement.typ, site.getId(), site);
		map.addNode(site);
		
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
		map.removeNode(site);
	}
	
	public void redo()
	{
		map.addNode(site);
	}
}
