/**
 * $Id: PlaceSchemeCableLinkCommand.java,v 1.22 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import java.util.Iterator;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.CableChannelingItem;

/**
 * ���������� ������ �� �����.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.22 $, $Date: 2005/05/27 15:14:55 $
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
	SchemeCableLink schemeCableLink = null;
	
	Map map;

	MapView mapView;
	
	public PlaceSchemeCableLinkCommand(SchemeCableLink schemeCableLink)
	{
		super();
		this.schemeCableLink = schemeCableLink;
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
			this.startNode = this.mapView.getStartNode(this.schemeCableLink);
			this.endNode = this.mapView.getEndNode(this.schemeCableLink);
			this.cablePath = this.mapView.findCablePath(this.schemeCableLink);
			// ���� ��������� ���� ��� ���� - ������ �� ������
			if(this.cablePath != null)
			{
//			super.checkCablePathLinks(cablePath);
				return;
			}
			this.cablePath = super.createCablePath(this.schemeCableLink, this.startNode, this.endNode);
			// ���� �� ���� ����� ���������� ���� �� ����������
			SiteNode bufferStartSite = this.startNode;
			// ���� �� ��������� �������� ������.
			Identifier userId = this.logicalNetLayer.getUserId();
			for(Iterator iter = this.schemeCableLink.getCableChannelingItems().iterator(); iter.hasNext();) {
				CableChannelingItem cci = (CableChannelingItem )iter.next();
				SiteNode smsne = cci.getStartSiteNode();
				SiteNode emsne = cci.getEndSiteNode();

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
					this.cablePath.addLink(unbound, CableController.generateCCI(this.cablePath, unbound, userId));
					unbound.setCablePath(this.cablePath);

					bufferStartSite = emsne;
				}
				// � ��������� ������ ��������� ������ � ������������ �����
				{
					
					PhysicalLink link = cci.getPhysicalLink();
					
					// ���� ����� �� ����������, �������� ������ ������� ��������
					if(link == null)
					{
						UnboundLink unbound = super.createUnboundLinkWithNodeLink(smsne, emsne);
						this.cablePath.addLink(unbound, CableController.generateCCI(this.cablePath, unbound, userId));
						unbound.setCablePath(this.cablePath);
					}
					else
					{
						link.getBinding().add(this.cablePath);
						if(cci.getRowX() != -1
							&& cci.getPlaceY() != -1)
							link.getBinding().bind(this.cablePath, cci.getRowX(), cci.getPlaceY());
			
						this.cablePath.addLink(link, CableController.generateCCI(this.cablePath, link, userId));
					}
				}
			}
			// ���� �������� �������� �� ������� �� �����, ������� �������������
			// ����� �� �������� �� ��������� ����
			if(this.endNode != bufferStartSite)
			{
				UnboundLink unbound = super.createUnboundLinkWithNodeLink(bufferStartSite, this.endNode);
				this.cablePath.addLink(unbound, CableController.generateCCI(this.cablePath, unbound, userId));
				unbound.setCablePath(this.cablePath);
			}
			// �������� ��������� - ���������� ����������
			this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
			this.logicalNetLayer.sendMapEvent(new MapNavigateEvent(
					this.cablePath, 
					MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
			this.logicalNetLayer.setCurrentMapElement(this.cablePath);
			this.logicalNetLayer.notifySchemeEvent(this.cablePath);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}

}