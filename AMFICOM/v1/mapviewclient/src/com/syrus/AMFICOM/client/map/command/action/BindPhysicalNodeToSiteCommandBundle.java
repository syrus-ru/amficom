/**
 * $Id: BindPhysicalNodeToSiteCommandBundle.java,v 1.22 2005/06/06 12:57:01 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
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
 * @author $Author: krupenn $
 * @version $Revision: 1.22 $, $Date: 2005/06/06 12:57:01 $
 * @module mapclient_v1
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

		try
		{
			this.mapView = this.logicalNetLayer.getMapView();
			this.map = this.mapView.getMap();
			PhysicalLink link = this.node.getPhysicalLink();
			// ������� "�����" � "������" ����, ������������ ���������
			// �������� ���� ����������
			for(Iterator it = this.map.getNodeLinks(this.node).iterator(); it.hasNext();)
			{
				NodeLink mnle = (NodeLink)it.next();

				if(this.node1 == null)
					this.node1 = mnle.getOtherNode(this.node);
				else
					this.node2 = mnle.getOtherNode(this.node);

				if(mnle.getStartNode().equals(this.node))
					mnle.setStartNode(this.site);
				else
					mnle.setEndNode(this.site);
			}
			// �������������� ���� ���������
			super.removeNode(this.node);
			// ����������� �������� ���� �����
			if(link.getStartNode().equals(this.node))
				link.setStartNode(this.site);
			else
			if(link.getEndNode().equals(this.node))
				link.setEndNode(this.site);
			// ��������� ������ �����
			UnboundLink newLink = super.createUnboundLink(link.getStartNode(), this.site);
			newLink.setType(link.getType());
			// single cpath, as long as link is UnboundLink
			CablePath cpath = (CablePath)(this.mapView.getCablePaths(link).get(0));
			// ����� ����� ����������� � ��������� ����
			cpath.addLink(newLink, CableController.generateCCI(cpath, newLink, this.logicalNetLayer.getUserId()));
			newLink.setCablePath(cpath);
			// ��������� ��������� � ����� ����� ���� �� ��������� ��
			// ��������� ����
			super.moveNodeLinks(
					this.map,
					link,
					newLink,
					false,
					this.site,
					null);
			link.setStartNode(this.site);
			cpath.sortLinks();
			this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		}
		catch(Throwable e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
}

