/**
 * $Id: GenerateCablePathCablingCommandBundle.java,v 1.9 2004/12/07 17:05:54 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *  ������� ��������� �������� � ������������ � ������������ ������.
 *  �� ������������� ����� ������������ ������� � ������ ������������� � ���.
 *  ��� ������������ �������� �����������. �� ������������� ��������� 
 *  ������������ ������� ���� � ������� �������� ������������� � ���.
 * 
 * 
 * 
 * @version $Revision: 1.9 $, $Date: 2004/12/07 17:05:54 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class GenerateCablePathCablingCommandBundle extends MapActionCommandBundle
{
	/**
	 * ��������� ����
	 */
	MapCablePathElement path;
	
	/**
	 * ��� ����� ��� ��������� ������ ������������� ���������
	 */
	MapNodeProtoElement proto;

	/**
	 * �����, �� ������� ������������ ��������
	 */
	MapView mapView;

	Map map;

	public GenerateCablePathCablingCommandBundle(
			MapCablePathElement path, 
			MapNodeProtoElement proto)
	{
		this.path = path;
		this.proto = proto;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		mapView = logicalNetLayer.getMapView();
		map = mapView.getMap();
		
		// ��� ������������ ����� ���������� ������������������
		// ����� �� ���������� � ���������
		MapSiteNodeElement startsite = (MapSiteNodeElement )path.getStartNode();
		MapSiteNodeElement endsite = null;
		
		// ���������, ��� ���� �������� ������� ����� (���� ��� �������������
		// �������, ������������� �� ��� ����� ������� ����)
		startsite = this.checkSite(startsite);

		// ��������� ������, ��������� ������������ ��������
		List list  = new LinkedList();
		list.addAll(path.getLinks());

		// ���� �� ���� ������, ����������� � ��������� ����
		// �� ������������� ������ ������������ �������
		for(Iterator it = list.iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();

			// ������� � ���������� ����
			if(startsite == link.getEndNode())
				endsite = (MapSiteNodeElement )link.getStartNode();
			else
				endsite = (MapSiteNodeElement )link.getEndNode();

			// ���������, ��� ���� �������� ������� ����� (���� ��� �������������
			// �������, ������������� �� ��� ����� ������� ����)
			endsite = this.checkSite(endsite);

			// ���� ������������� �����, ������������ �������
			if(link instanceof MapUnboundLinkElement)
			{
				path.removeLink(link);
				MapUnboundLinkElement un = (MapUnboundLinkElement )link;
				super.removePhysicalLink(un);

				link = super.createPhysicalLink(startsite, endsite);
				// ��������� ����������� � ����� ��������������� �������
				for(Iterator it2 = un.getNodeLinks().iterator(); it2.hasNext();)
				{
					MapNodeLinkElement mnle = (MapNodeLinkElement )it2.next();
					mnle.setPhysicalLinkId(link.getId());
					link.addNodeLink(mnle);
				}
				path.addLink(link);
				link.getBinding().add(path);
			}

			startsite = endsite;
		}

		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}

	/**
	 * ���������, ��� ���� �������� ������� �����.
	 * ���� �� �������� ������������� ���������, ������������� �� ��� �����
	 * ������� ����
	 */
	private MapSiteNodeElement checkSite(MapSiteNodeElement site)
	{
		MapSiteNodeElement site2 = site;
		if(site instanceof MapUnboundNodeElement)
		{
			CreateSiteCommandAtomic command = 
					new CreateSiteCommandAtomic(
						proto, 
						site.getLocation());
			command.setLogicalNetLayer(logicalNetLayer);
			command.execute();
			super.add(command);
			
			site2 = command.getSite();
	
			BindUnboundNodeToSiteCommandBundle command2 = 
					new BindUnboundNodeToSiteCommandBundle(
						(MapUnboundNodeElement )site, 
						site2);
			command2.setLogicalNetLayer(logicalNetLayer);
			command2.execute();
			super.add(command2);

		}
		return site2;
	}

}

