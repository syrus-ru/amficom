/**
 * $Id: DeletePhysicalLinkCommandBundle.java,v 1.5 2004/12/22 16:38:40 krupenn Exp $
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
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import java.util.Iterator;
import java.util.List;

/**
 * � ������ ������ ����������� �������� �������� �����. � �����������
 * �� ����, ����� �������� ����� �� ������ ���������� �������� �������� 
 * ���������� �����, �����, �����  (� �����). �������
 * ������� �� ������������������ ��������� ��������
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/12/22 16:38:40 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class DeletePhysicalLinkCommandBundle extends MapActionCommandBundle
{
	/**
	 * ��������� ��������
	 */
	PhysicalLink link;
	
	/**
	 * �����
	 */
	Map map;

	public DeletePhysicalLinkCommandBundle(PhysicalLink link)
	{
		super();
		this.link = link;
	}


	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");
		
		// ����� ����� ���� ������� � ���������� ��������� ������� � �������
		// ������ ������� ��������, � ���� ������ � �� ����� ���������
		// ���� isRemoved
		if(link.isRemoved())
			return;

		MapView mapView = logicalNetLayer.getMapView();
		map = mapView.getMap();
		
		List cablePathsToScan = mapView.getCablePaths(link);

		link.sortNodes();
		
		/// ��������� ��� �������������� ���� �����
		for(Iterator it = link.getSortedNodes().iterator(); it.hasNext();)
		{
			AbstractNode ne = (AbstractNode)it.next();
			if(ne instanceof TopologicalNode)
			{
				TopologicalNode node = (TopologicalNode)ne;
				super.removeNode(node);
			}
		}
		
		// ��������� ��� ��������� �����
		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink nodeLink = (NodeLink)it.next();
			super.removeNodeLink(nodeLink);
		}
		
		// ��������� ���� �����
		super.removePhysicalLink(link);
		
		// ����������� ��� ��������� ����, ������� ��������� �� ��������� �����,
		// � ����������� �� ��� ���������� ������������� ������
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
