/**
 * $Id: DeletePhysicalLinkCommandBundle.java,v 1.3 2004/10/14 15:39:05 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import java.util.Iterator;
import java.util.List;

/**
 * � ������ ������ ����������� �������� �������� NodeLink. � �����������
 * �� ����, ����� �������� ����� �� ������ ���������� �������� �������� 
 * ���������� �����, �����, �����  (� �����). �������
 * ������� �� ������������������ ��������� ��������
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/14 15:39:05 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class DeletePhysicalLinkCommandBundle extends MapActionCommandBundle
{
	/**
	 * ��������� ��������
	 */
	MapPhysicalLinkElement link;
	
	/**
	 * �����
	 */
	Map map;

	public DeletePhysicalLinkCommandBundle(MapPhysicalLinkElement link)
	{
		super();
		this.link = link;
	}


	public void execute()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "execute()");
		
		// �������� ����� ���� ������ � ���������� ��������� ������� � �������
		// ������ ������� ��������, � ���� ������ � ���� ����� ���������
		// ���� isRemoved
		if(link.isRemoved())
			return;

		MapView mapView = logicalNetLayer.getMapView();
		map = mapView.getMap();
		
		List cablePathsToScan = mapView.getCablePaths(link);

		link.sortNodes();
		
		for(Iterator it = link.getSortedNodes().iterator(); it.hasNext();)
		{
			MapNodeElement ne = (MapNodeElement )it.next();
			if(ne instanceof MapPhysicalNodeElement)
			{
				MapPhysicalNodeElement node = (MapPhysicalNodeElement )ne;
				super.removeNode(node);
			}
		}
		
		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement nodeLink = (MapNodeLinkElement )it.next();
			super.removeNodeLink(nodeLink);
		}
		
		super.removePhysicalLink(link);
		
		for(Iterator it = cablePathsToScan.iterator(); it.hasNext();)
		{
			MapCablePathElement cpath = (MapCablePathElement )it.next();
			
			cpath.removeLink(link);
			MapUnboundLinkElement unbound = super.createUnboundLinkWithNodeLink(
					link.getStartNode(),
					link.getEndNode());
			unbound.setCablePath(cpath);
			cpath.addLink(unbound);
		}

		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
}
