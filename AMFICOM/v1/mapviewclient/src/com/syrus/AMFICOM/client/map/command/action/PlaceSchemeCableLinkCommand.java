/**
 * $Id: PlaceSchemeCableLinkCommand.java,v 1.9 2004/12/24 15:42:11 krupenn Exp $
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
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundLink;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.scheme.corba.CableChannelingItem;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;

import java.util.Iterator;
import java.util.List;
import com.syrus.AMFICOM.map.PhysicalLinkBinding;

/**
 * ���������� ������ �� �����.
 * 
 * @version $Revision: 1.9 $, $Date: 2004/12/24 15:42:11 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
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

		mapView = logicalNetLayer.getMapView();
		map = mapView.getMap();
		
		SiteNode[] mne = mapView.getSideNodes(scl);
		
		startNode = mne[0];
		endNode = mne[1];
		
		cablePath = mapView.findCablePath(scl);
		// ���� ��������� ���� ��� ���� - ������ �� ������
		if(cablePath != null)
		{
//			super.checkCablePathLinks(cablePath);
			return;
		}

		cablePath = super.createCablePath(scl, startNode, endNode);

//		List ccis = (List )scl.channelingItems;

		// ���� �� ���� ����� ���������� ���� �� ����������
		SiteNode bufferStartSite = startNode;

		// ���� �� ��������� �������� ������.
		for(int i = 0; i < scl.cableChannelingItems().length; i++)
		{
			CableChannelingItem cci = (CableChannelingItem )scl.cableChannelingItems()[i];
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
				cablePath.addLink(unbound);
				unbound.setCablePath(cablePath);

				bufferStartSite = emsne;
			}
			// � ��������� ������ ��������� ������ � ������������ �����
			{
				
				PhysicalLink link = cci.physicalLinkImpl();
				
				// ���� ����� �� ����������, �������� ������ ������� ��������
				if(link == null)
				{
					UnboundLink unbound = super.createUnboundLinkWithNodeLink(smsne, emsne);
					cablePath.addLink(unbound);
					unbound.setCablePath(cablePath);
				}
				else
				{
					link.getBinding().add(cablePath);
					if(cci.rowX() != -1
						&& cci.placeY() != -1)
						link.getBinding().bind(cablePath, cci.rowX(), cci.placeY());
		
					cablePath.addLink(link);
				}
			}
		}

		// ���� �������� �������� �� ������� �� �����, ������� �������������
		// ����� �� �������� �� ��������� ����
		if(endNode != bufferStartSite)
		{
			UnboundLink unbound = super.createUnboundLinkWithNodeLink(bufferStartSite, endNode);
			cablePath.addLink(unbound);
			unbound.setCablePath(cablePath);
		}

		// �������� ��������� - ���������� ����������
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
				cablePath, 
				MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.setCurrentMapElement(cablePath);
		logicalNetLayer.notifySchemeEvent(cablePath);
	}

}
