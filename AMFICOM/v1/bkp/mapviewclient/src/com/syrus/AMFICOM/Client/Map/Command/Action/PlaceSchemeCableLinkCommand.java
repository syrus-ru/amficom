/**
 * $Id: PlaceSchemeCableLinkCommand.java,v 1.14 2005/02/08 15:11:09 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.corba.CableChannelingItem;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;

/**
 * ���������� ������ �� �����.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.14 $, $Date: 2005/02/08 15:11:09 $
 * @module mapviewclient_v1
 */
public class PlaceSchemeCableLinkCommand extends MapActionCommandBundle
{
	/**
	 * ��������� ���� ���������� ����
	 */
	SiteNode startNode = null;
	
	/**
	 * �������� ���� ���������� ����
	 */
	SiteNode endNode = null;

	/**
	 * ����������� ��������� ����
	 */
	CablePath cablePath = null;
	
	/**
	 * ����������� ������
	 */
	SchemeCableLink scl = null;
	
	Map map;

	MapView mapView;
	
	public PlaceSchemeCableLinkCommand(SchemeCableLink scl)
	{
		super();
		this.scl = scl;
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
		
		this.startNode = this.mapView.getStartNode(this.scl);
		this.endNode = this.mapView.getEndNode(this.scl);
		
		this.cablePath = this.mapView.findCablePath(this.scl);
		// ���� ��������� ���� ��� ���� - ������ �� ������
		if(this.cablePath != null)
		{
//			super.checkCablePathLinks(cablePath);
			return;
		}

		this.cablePath = super.createCablePath(this.scl, this.startNode, this.endNode);

//		List ccis = (List )scl.channelingItems;

		// ���� �� ���� ����� ���������� ���� �� ����������
		SiteNode bufferStartSite = this.startNode;

		// ���� �� ��������� �������� ������.
		for(int i = 0; i < this.scl.cableChannelingItems().length; i++)
		{
			CableChannelingItem cci = this.scl.cableChannelingItems()[i];
			SiteNode smsne = cci.startSiteNodeImpl();
			SiteNode emsne = cci.endSiteNodeImpl();

			// ���� ������� �������� �� ������������� �������������� �����
			// (���� �� ����� �������� �� ������� �� �����) �� �������
			// �������� ����������
			if(smsne == null
				|| emsne == null)
			{
				continue;
			}

			// a link between bufferStartSite and current cci exists
			boolean exists = false;
			
			// ��������� � ���������� ���� ���������� ����
			if(bufferStartSite.equals(smsne))
			{
				bufferStartSite = emsne;
				exists = true;
			}
			else
			if(bufferStartSite.equals(emsne))
			{
				bufferStartSite = smsne;
				exists = true;
			}
			
			// ���� �� ���� �� ���� ���������� ������� �� ���������, �� ����
			// ���������� ������ ������������������ �������� (����� 
			// bufferStartSite - cci.startSiteId �� ����������), ��
			// ������� �� ����� ������� ������������� ����� �� ������ ���������
			if(!exists)
			{
				UnboundLink unbound = super.createUnboundLinkWithNodeLink(bufferStartSite, smsne);
				this.cablePath.addLink(unbound, CableController.generateCCI(unbound));
				unbound.setCablePath(this.cablePath);

				bufferStartSite = emsne;
			}
			// � ��������� ������ ��������� ������ � ������������ �����
			{
				
				PhysicalLink link = cci.physicalLinkImpl();
				
				// ���� ����� �� ����������, �������� ������ ������� ��������
				if(link == null)
				{
					UnboundLink unbound = super.createUnboundLinkWithNodeLink(smsne, emsne);
					this.cablePath.addLink(unbound, CableController.generateCCI(unbound));
					unbound.setCablePath(this.cablePath);
				}
				else
				{
					link.getBinding().add(this.cablePath);
					if(cci.rowX() != -1
						&& cci.placeY() != -1)
						link.getBinding().bind(this.cablePath, cci.rowX(), cci.placeY());
		
					this.cablePath.addLink(link, CableController.generateCCI(link));
				}
			}
		}

		// ���� �������� �������� �� ������� �� �����, ������� �������������
		// ����� �� �������� �� ��������� ����
		if(this.endNode != bufferStartSite)
		{
			UnboundLink unbound = super.createUnboundLinkWithNodeLink(bufferStartSite, this.endNode);
			this.cablePath.addLink(unbound, CableController.generateCCI(unbound));
			unbound.setCablePath(this.cablePath);
		}

		// �������� ��������� - ���������� ����������
		this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		this.logicalNetLayer.sendMapEvent(new MapNavigateEvent(
				this.cablePath, 
				MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		this.logicalNetLayer.setCurrentMapElement(this.cablePath);
		this.logicalNetLayer.notifySchemeEvent(this.cablePath);
	}

}
