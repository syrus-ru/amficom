/**
 * $Id: GenerateCablePathCablingCommandBundle.java,v 1.19 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;

/**
 *  ������� ��������� �������� � ������������ � ������������ ������.
 *  �� ������������� ����� ������������ ������� � ������ ������������� � ���.
 *  ��� ������������ �������� �����������. �� ������������� ��������� 
 *  ������������ ������� ���� � ������� �������� ������������� � ���.
 * @author $Author: krupenn $
 * @version $Revision: 1.19 $, $Date: 2005/05/27 15:14:55 $
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
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

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
					super.removePhysicalLink(un);

					link = super.createPhysicalLink(startsite, endsite);
					// ��������� ����������� � ����� ��������������� �������
					for(Iterator it2 = un.getNodeLinks().iterator(); it2.hasNext();)
					{
						NodeLink mnle = (NodeLink)it2.next();
						mnle.setPhysicalLink(link);
						link.addNodeLink(mnle);
					}
					this.path.addLink(link, CableController.generateCCI(this.path, link, this.logicalNetLayer.getUserId()));
					link.getBinding().add(this.path);
				}

				startsite = endsite;
			}
			this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
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
			command2.setLogicalNetLayer(this.logicalNetLayer);
			command2.execute();
			super.add(command2);

		}
		return site2;
	}

}

