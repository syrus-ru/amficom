/**
 * $Id: InsertSiteCommand.java,v 1.8 2004/10/18 15:33:00 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPipePathElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import java.awt.Point;

import java.util.Iterator;

/**
 * ���������� ������� ���� mpe �� �����. ������������ ��� �������� 
 * (drag/drop), � ����� point (� �������� �����������)
 * 
 * @version $Revision: 1.8 $, $Date: 2004/10/18 15:33:00 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class InsertSiteCommand extends MapActionCommandBundle
{
	/**
	 * ��������� �������� �����
	 */
	MapPhysicalNodeElement node;
	MapSiteNodeElement site;
	MapNodeProtoElement proto;
	MapPhysicalLinkElement link;

	MapNodeElement node1 = null;
	MapNodeElement node2 = null;

	MapPhysicalLinkElement link1 = null;
	MapPhysicalLinkElement link2 = null;

	
	Map map;
	
	/**
	 * �����, � ������� ��������� ����� �������������� ����
	 */
	Point point;

	public InsertSiteCommand(
			MapPhysicalNodeElement node,
			MapNodeProtoElement proto)
	{
		this.proto = proto;
		this.node = node;
	}

	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		if ( !getLogicalNetLayer().getContext().getApplicationModel()
				.isEnabled("mapActionCreateEquipment"))
			return;
		
		map = logicalNetLayer.getMapView().getMap();
		
		link = map.getPhysicalLink(node.getPhysicalLinkId());
		
		// ������� ����� ����
		site = super.createSite(
				node.getAnchor(),
				proto);
		site.setName(node.getName());
		site.setScaleCoefficient(logicalNetLayer.getDefaultScale() / logicalNetLayer.getCurrentScale());

		for(Iterator it = node.getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();

			if(node1 == null)
				node1 = mnle.getOtherNode(node);
			else
				node2 = mnle.getOtherNode(node);

			MapElementState nls = mnle.getState();

			if(mnle.getStartNode() == node)
				mnle.setStartNode(site);
			else
				mnle.setEndNode(site);

			registerStateChange(mnle, nls, mnle.getState());
		}

		super.removeNode(node);

		MapElementState pls = link.getState();

		if(link.getStartNode() == node)
			link.setStartNode(site);
		else
		if(link.getEndNode() == node)
			link.setEndNode(site);

		if(node.isActive())
		{
			if(link instanceof MapUnboundLinkElement)
				link1 = super.createUnboundLink(link.getStartNode(), site);
			else
				link1 = super.createPhysicalLink(link.getStartNode(), site);
			link1.setProto(link.getProto());
			MapPipePathElement collector = map.getCollector(link);
			if(collector != null)
				collector.addLink(link1);

			// �������� ��� ��������� ������ ���������� �����
			java.util.List nodelinks = link.getNodeLinks();
		
			// ���������� ��������� ���� � ��������� �������� ���������� �����
			MapNodeLinkElement startNodeLink = null;
			MapNodeElement startNode = link.getStartNode();
			for(Iterator it = nodelinks.iterator(); it.hasNext();)
			{
				startNodeLink = (MapNodeLinkElement )it.next();
				if(startNodeLink.getStartNode() == link.getStartNode()
					|| startNodeLink.getEndNode() == link.getStartNode())
				{
					break;
				}
			}
		
			// ������� ���� �� ���������� ����� - ������������ ��������� � ����� 
			// ���������� �����. �������� �� ���������� �� ������� ���� �� ���������
			// �� ��������, �������� � ���������
			for(;;)
			{
				// ���������� �������� � ����� �����
				link.removeNodeLink(startNodeLink);
				link1.addNodeLink(startNodeLink);
				
				MapElementState nls = startNodeLink.getState();
				
				startNodeLink.setPhysicalLinkId(link1.getId());

				registerStateChange(startNodeLink, nls, startNodeLink.getState());
				
				// ������� � ���������� ����
				startNode = startNodeLink.getOtherNode(startNode);

				// ���� ���������� �� ������ ����� �����, �� ��������
				// �������� ���� � ���������
				if(startNode == site)
				{
					link1.setEndNode(site);
					link.setStartNode(site);
					break;
				}
				
				// ������� � ���������� ���������
				for(Iterator it = startNode.getNodeLinks().iterator(); it.hasNext();)
				{
					MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
					if(startNodeLink != mnle)
					{
						startNodeLink = mnle;
						break;
					}
				}
			}//for(;;)

			MapView mapView = logicalNetLayer.getMapView();
	
			for(Iterator it = mapView.getCablePaths(link).iterator(); it.hasNext();)
			{
				MapCablePathElement cpath = (MapCablePathElement )it.next();
				cpath.addLink(link1);
				if(link1 instanceof MapUnboundLinkElement)
					((MapUnboundLinkElement )link1).setCablePath(cpath);
				else
					link1.getBinding().add(cpath);
			}
		}

		registerStateChange(link, pls, link.getState());
	
		// �������� ��������� - ���������� ����������
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
					site, 
					MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.setCurrentMapElement(site);
		logicalNetLayer.notifySchemeEvent(site);
		logicalNetLayer.notifyCatalogueEvent(site);

	}
}
