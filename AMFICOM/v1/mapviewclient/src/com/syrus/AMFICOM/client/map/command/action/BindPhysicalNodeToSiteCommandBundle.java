/**
 * $Id: BindPhysicalNodeToSiteCommandBundle.java,v 1.2 2004/10/09 13:33:40 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPipePathElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import java.util.Iterator;

/**
 *  ������� �������� �������� ���������� ������ MapNodeElement. �������
 * ������� ��  ������������������ ��������� ��������
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/10/09 13:33:40 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class BindPhysicalNodeToSiteCommandBundle extends MapActionCommandBundle
{
	/**
	 * ��������� ����
	 */
	MapPhysicalNodeElement node;
	MapSiteNodeElement site;

	MapNodeElement node1 = null;
	MapNodeElement node2 = null;

	/**
	 * �����, �� ������� ������������ ��������
	 */
	MapView mapView;
	Map map;

	public BindPhysicalNodeToSiteCommandBundle(MapPhysicalNodeElement node, MapSiteNodeElement site)
	{
		this.node = node;
		this.site = site;
	}
	
	public void execute()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "execute()");

		mapView = logicalNetLayer.getMapView();
		map = mapView.getMap();

		MapPhysicalLinkElement link = map.getPhysicalLink(node.getPhysicalLinkId());

		for(Iterator it = node.getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();

			if(node1 == null)
				node1 = mnle.getOtherNode(node);
			else
				node2 = mnle.getOtherNode(node);

			if(mnle.getStartNode() == node)
				mnle.setStartNode(site);
			else
				mnle.setEndNode(site);
		}

		removeNode(node);

		if(link.getStartNode() == node)
			link.setStartNode(site);
		else
		if(link.getEndNode() == node)
			link.setEndNode(site);

		MapPhysicalLinkElement link1 = super.createUnboundLink(link.getStartNode(), site);
		link1.setProto(link.getProto());

		// single cpath, as long as link is UnboundLink
		MapCablePathElement cpath = (MapCablePathElement )(mapView.getCablePaths(link).getFirst());
		
		cpath.addLink(link1);

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
			
			startNodeLink.setPhysicalLinkId(link1.getId());

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

		logicalNetLayer.repaint();
	}
}

