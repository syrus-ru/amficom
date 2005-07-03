/**
 * $Id: GenerateCablePathCablingCommandBundle.java,v 1.25 2005/06/29 15:51:17 krupenn Exp $
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
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.util.Log;

/**
 *  ������� ��������� �������� � ������������ � ������������ ������.
 *  �� ������������� ����� ������������ ������� � ������ ������������� � ���.
 *  ��� ������������ �������� �����������. �� ������������� ��������� 
 *  ������������ ������� ���� � ������� �������� ������������� � ���.
 * @author $Author: krupenn $
 * @version $Revision: 1.25 $, $Date: 2005/06/29 15:51:17 $
 * @module mapviewclient_v1
 */
public class GenerateCablePathCablingCommandBundle extends MapActionCommandBundle
{
	/**
	 * ��������� ����
	 */
	CablePath path;
	
	/**
	 * ��� ����� ��� ��������� ������ ������������� ���������
	 */
	SiteNodeType proto;

	/**
	 * �����, �� ������� ������������ ��������
	 */
	MapView mapView;

	Map map;

	public GenerateCablePathCablingCommandBundle(
			CablePath path, 
			SiteNodeType proto)
	{
		this.path = path;
		this.proto = proto;
	}
	
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Log.FINER);

		this.mapView = this.logicalNetLayer.getMapView();
		this.map = this.mapView.getMap();
		
		try {
			// ��� ������������ ����� ���������� ������������������
			// ����� �� ���������� � ���������
			SiteNode startsite = (SiteNode)this.path.getStartNode();
			SiteNode endsite = null;
			// ���������, ��� ���� �������� ������� ����� (���� ��� �������������
			// �������, ������������� �� ��� ����� ������� ����)
			startsite = this.checkSite(startsite);
			// ��������� ������, ��������� ������������ ��������
			List list  = new LinkedList();
			list.addAll(this.path.getLinks());
			// ���� �� ���� ������, ����������� � ��������� ����
			// �� ������������� ������ ������������ �������
			for(Iterator it = list.iterator(); it.hasNext();)
			{
				PhysicalLink link = (PhysicalLink)it.next();

				// ������� � ���������� ����
				if(startsite == link.getEndNode())
					endsite = (SiteNode)link.getStartNode();
				else
					endsite = (SiteNode)link.getEndNode();

				// ���������, ��� ���� �������� ������� ����� (���� ��� �������������
				// �������, ������������� �� ��� ����� ������� ����)
				endsite = this.checkSite(endsite);

				// ���� ������������� �����, ������������ �������
				if(link instanceof UnboundLink)
				{
					this.path.removeLink(link);
					UnboundLink un = (UnboundLink)link;

					link = super.createPhysicalLink(startsite, endsite);
					// ��������� ����������� � ����� ��������������� �������
					for(Iterator it2 = un.getNodeLinks().iterator(); it2.hasNext();)
					{
						NodeLink mnle = (NodeLink)it2.next();
						mnle.setPhysicalLink(link);
					}
					super.removePhysicalLink(un);
					this.path.addLink(link, CableController.generateCCI(this.path, link, LoginManager.getUserId()));
					link.getBinding().add(this.path);
				}

				startsite = endsite;
			}
			this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}

	/**
	 * ���������, ��� ���� �������� ������� �����.
	 * ���� �� �������� ������������� ���������, ������������� �� ��� �����
	 * ������� ����
	 */
	private SiteNode checkSite(SiteNode site)
	{
		SiteNode site2 = site;
		if(site instanceof UnboundNode)
		{
			CreateSiteCommandAtomic command = 
					new CreateSiteCommandAtomic(
						this.proto, 
						site.getLocation());
			command.setLogicalNetLayer(this.logicalNetLayer);
			command.execute();
			super.add(command);
			
			site2 = command.getSite();
	
			BindUnboundNodeToSiteCommandBundle command2 = 
					new BindUnboundNodeToSiteCommandBundle(
						(UnboundNode)site, 
						site2);
			command2.setNetMapViewer(this.netMapViewer);
			command2.execute();
			super.add(command2);

		}
		return site2;
	}

}

