/**
 * $Id: BindPhysicalNodeToSiteCommandBundle.java,v 1.30 2005/08/12 10:33:30 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.Iterator;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.util.Log;

/**
 *  ������� ������������ ��������������� ����, ��������������
 *  �������������� ������, � �������� ����. ��� ���� �����, ������� 
 *  ����������� ������ ����, ������� �� 2 �����
 * @author $Author: krupenn $
 * @version $Revision: 1.30 $, $Date: 2005/08/12 10:33:30 $
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
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);

		try
		{
			this.mapView = this.logicalNetLayer.getMapView();
			this.map = this.mapView.getMap();
			UnboundLink link = (UnboundLink)this.node.getPhysicalLink();
			// ������� "�����" � "������" ����, ������������ ���������
			// �������� ���� ����������
			for(Iterator it = this.map.getNodeLinks(this.node).iterator(); it.hasNext();)
			{
				NodeLink nodeLink = (NodeLink)it.next();

				if(nodeLink.getStartNode().equals(this.node))
					nodeLink.setStartNode(this.site);
				else
					nodeLink.setEndNode(this.site);
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
			CablePath cablePath = link.getCablePath();

//			CableChannelingItem cableChannelingItem = cablePath.getFirstCCI(link);
			for(CableChannelingItem cableChannelingItem : cablePath.getSchemeCableLink().getPathMembers()) {
				if(cablePath.getBinding().get(cableChannelingItem) == link) {
					CableChannelingItem newCableChannelingItem = 
						CableController.generateCCI(
								cablePath, 
								newLink,
								cableChannelingItem.getStartSiteNode(),
								this.site);
					newCableChannelingItem.insertSelfBefore(cableChannelingItem);
					// ����� ����� ����������� � ��������� ����
					cablePath.addLink(newLink, newCableChannelingItem);
					cableChannelingItem.setStartSiteNode(this.site);
				}
			}

			newLink.setCablePath(cablePath);
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
		}
		catch(Throwable e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
}

