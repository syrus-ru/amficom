/**
 * $Id: BindPhysicalNodeToSiteCommandBundle.java,v 1.6 2004/10/19 14:10:03 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import java.util.Iterator;

/**
 *  ������� ������������ ��������������� ����, ��������������
 *  �������������� ������, � �������� ����. ��� ���� �����, ������� 
 *  ����������� ������ ����, ������� �� 2 �����
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2004/10/19 14:10:03 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class BindPhysicalNodeToSiteCommandBundle extends MapActionCommandBundle
{
	/**
	 * ��������������� ����
	 */
	MapPhysicalNodeElement node;
	
	/** ����, � �������� ������������� �������������� ���� */
	MapSiteNodeElement site;

	/** 
	 * ����, ����������� "�����" �� ���������������� ���� �� ��� �� �����,
	 * ��� � ��������������� ����
	 */
	MapNodeElement node1 = null;

	/** 
	 * ����, ����������� "������" �� ���������������� ���� �� ��� �� �����,
	 * ��� � ��������������� ����
	 */
	MapNodeElement node2 = null;

	/**
	 * ���, �� ������� ������������ ��������
	 */
	MapView mapView;

	/**
	 * �����, �� ������� ������������ ��������
	 */
	Map map;

	public BindPhysicalNodeToSiteCommandBundle(
			MapPhysicalNodeElement node, 
			MapSiteNodeElement site)
	{
		this.node = node;
		this.site = site;
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

		MapPhysicalLinkElement link = map.getPhysicalLink(node.getPhysicalLinkId());

		// ������� "�����" � "������" ����, ������������ ���������
		// �������� ���� ����������
		for(Iterator it = node.getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();

			if(node1 == null)
				node1 = mnle.getOtherNode(node);
			else
				node2 = mnle.getOtherNode(node);

			if(mnle.getStartNode().equals(node))
				mnle.setStartNode(site);
			else
				mnle.setEndNode(site);
		}

		// �������������� ���� ���������
		super.removeNode(node);

		// ����������� �������� ���� �����
		if(link.getStartNode().equals(node))
			link.setStartNode(site);
		else
		if(link.getEndNode().equals(node))
			link.setEndNode(site);

		// ��������� ������ �����
		MapUnboundLinkElement newLink = super.createUnboundLink(link.getStartNode(), site);
		newLink.setProto(link.getProto());

		// single cpath, as long as link is UnboundLink
		MapCablePathElement cpath = (MapCablePathElement )(mapView.getCablePaths(link).get(0));
		
		// ����� ����� ����������� � ��������� ����
		cpath.addLink(newLink);
		newLink.setCablePath(cpath);

		// ��������� ��������� � ����� ����� ���� �� ��������� ��
		// ��������� ����
		super.moveNodeLinks(
				link,
				newLink,
				false,
				site,
				null);
		link.setStartNode(site);
		cpath.sortLinks();
/* MapActionBundleCommand

		// ���������� ��������� ���� � ��������� �������� ���������� �����
		MapNodeLinkElement startNodeLink = link.getStartNodeLink();
		MapNodeElement startNode = link.getStartNode();
	
		// ������� ���� �� ���������� ����� - ������������ ��������� � ����� 
		// ���������� �����. �������� �� ���������� �� ������� ���� �� ���������
		// �� ��������, �������� � ���������
		for(;;)
		{
			// ���������� �������� � ����� �����
			link.removeNodeLink(startNodeLink);
			newLink.addNodeLink(startNodeLink);
			startNodeLink.setPhysicalLinkId(newLink.getId());

			// ������� � ���������� ����
			startNode = startNodeLink.getOtherNode(startNode);

			// ���� ���������� �� ������ ����� �����, �� ��������
			// �������� ���� � ���������
			if(startNode == site)
			{
				newLink.setEndNode(site);
				link.setStartNode(site);
				cpath.sortLinks();
				break;
			}
			
			// ������� � ���������� ���������
			startNodeLink = startNode.getOtherNodeLink(startNodeLink);
		}//for(;;)
*/
		logicalNetLayer.repaint();
	}
}

