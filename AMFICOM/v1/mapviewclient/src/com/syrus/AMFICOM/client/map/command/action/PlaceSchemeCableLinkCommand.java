/**
 * $Id: PlaceSchemeCableLinkCommand.java,v 1.7 2004/11/25 13:00:49 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Scheme.CableChannelingItem;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;

import java.util.Iterator;
import java.util.List;

/**
 * ���������� ������ �� �����.
 * 
 * @version $Revision: 1.7 $, $Date: 2004/11/25 13:00:49 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class PlaceSchemeCableLinkCommand extends MapActionCommandBundle
{
	/**
	 * ��������� ���� ���������� ����
	 */
	MapSiteNodeElement startNode = null;
	
	/**
	 * �������� ���� ���������� ����
	 */
	MapSiteNodeElement endNode = null;

	/**
	 * ����������� ��������� ����
	 */
	MapCablePathElement cablePath = null;
	
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
		
		MapSiteNodeElement[] mne = mapView.getSideNodes(scl);
		
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

		List ccis = (List )scl.channelingItems;

		// ���� �� ���� ����� ���������� ���� �� ����������
		MapSiteNodeElement bufferStartSite = startNode;

		// ���� �� ��������� �������� ������.
		for(Iterator it = ccis.iterator(); it.hasNext();)
		{
			CableChannelingItem cci = (CableChannelingItem )it.next();
			MapSiteNodeElement smsne = map.getMapSiteNodeElement(cci.startSiteId);
			MapSiteNodeElement emsne = map.getMapSiteNodeElement(cci.endSiteId);

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
				MapUnboundLinkElement unbound = super.createUnboundLinkWithNodeLink(bufferStartSite, smsne);
				cablePath.addLink(unbound);
				unbound.setCablePath(cablePath);

				bufferStartSite = emsne;
			}
			// � ��������� ������ ��������� ������ � ������������ �����
			{
				
				MapPhysicalLinkElement link = map.getPhysicalLink(cci.physicalLinkId);
				
				// ���� ����� �� ����������, �������� ������ ������� ��������
				if(link == null)
				{
					MapUnboundLinkElement unbound = super.createUnboundLinkWithNodeLink(smsne, emsne);
					cablePath.addLink(unbound);
					unbound.setCablePath(cablePath);
				}
				else
				{
					link.getBinding().add(cablePath);
					if(cci.row_x != -1
						&& cci.place_y != -1)
						link.getBinding().bind(cablePath, cci.row_x, cci.place_y);
		
					cablePath.addLink(link);
				}
			}
		}

		// ���� �������� �������� �� ������� �� �����, ������� �������������
		// ����� �� �������� �� ��������� ����
		if(endNode != bufferStartSite)
		{
			MapUnboundLinkElement unbound = super.createUnboundLinkWithNodeLink(bufferStartSite, endNode);
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
