/**
 * $Id: BindPhysicalNodeToSiteCommandBundle.java,v 1.12 2005/01/31 12:19:18 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.MapView;

import java.util.Iterator;

/**
 *  ������� ������������ ��������������� ����, ��������������
 *  �������������� ������, � �������� ����. ��� ���� �����, ������� 
 *  ����������� ������ ����, ������� �� 2 �����
 * 
 * 
 * @version $Revision: 1.12 $, $Date: 2005/01/31 12:19:18 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class BindPhysicalNodeToSiteCommandBundle extends MapActionCommandBundle
{
	/**
	 * ��������������� ����
	 */
	TopologicalNode node;
	
	/** ����, � �������� ������������� �������������� ���� */
	SiteNode site;

	/** 
	 * ����, ����������� "�����" �� ���������������� ���� �� ��� �� �����,
	 * ��� � ��������������� ����
	 */
	AbstractNode node1 = null;

	/** 
	 * ����, ����������� "������" �� ���������������� ���� �� ��� �� �����,
	 * ��� � ��������������� ����
	 */
	AbstractNode node2 = null;

	/**
	 * ���, �� ������� ������������ ��������
	 */
	MapView mapView;

	/**
	 * �����, �� ������� ������������ ��������
	 */
	Map map;

	public BindPhysicalNodeToSiteCommandBundle(
			TopologicalNode node, 
			SiteNode site)
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

		PhysicalLink link = node.getPhysicalLink();

		// ������� "�����" � "������" ����, ������������ ���������
		// �������� ���� ����������
		for(Iterator it = node.getNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink mnle = (NodeLink)it.next();

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
		UnboundLink newLink = super.createUnboundLink(link.getStartNode(), site);
		newLink.setType(link.getType());

		// single cpath, as long as link is UnboundLink
		CablePath cpath = (CablePath)(getLogicalNetLayer().getMapViewController().getCablePaths(link).get(0));
		CableController cableController = (CableController )
			getLogicalNetLayer().getMapViewController().getController(cpath);
		
		// ����� ����� ����������� � ��������� ����
		cpath.addLink(newLink, cableController.generateCCI(newLink));
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

		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
}

