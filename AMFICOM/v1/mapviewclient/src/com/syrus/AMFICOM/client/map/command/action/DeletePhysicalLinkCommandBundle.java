/**
 * $Id: DeletePhysicalLinkCommandBundle.java,v 1.21 2005/07/24 12:41:05 krupenn Exp $
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
import java.util.List;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.util.Log;

/**
 * � ������ ������ ����������� �������� �������� �����. � �����������
 * �� ����, ����� �������� ����� �� ������ ���������� �������� �������� 
 * ���������� �����, �����, �����  (� �����). �������
 * ������� �� ������������������ ��������� ��������
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.21 $, $Date: 2005/07/24 12:41:05 $
 * @module mapviewclient_v1
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
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);
		
		// ����� ����� ���� ������� � ���������� ��������� ������� � �������
		// ������ ������� ��������, � ���� ������ � �� ����� ���������
		// ���� isRemoved
		if(this.link.isRemoved())
			return;

		setResult(Command.RESULT_OK);

		try
		{
			MapView mapView = this.logicalNetLayer.getMapView();
			this.map = mapView.getMap();
			List cablePathsToScan = mapView.getCablePaths(this.link);
			this.link.sortNodes();
			/// ��������� ��� �������������� ���� �����
			for(Iterator it = this.link.getSortedNodes().iterator(); it.hasNext();)
			{
				AbstractNode ne = (AbstractNode)it.next();
				if(ne instanceof TopologicalNode)
				{
					TopologicalNode node = (TopologicalNode)ne;
					super.removeNode(node);
				}
			}
			// ��������� ��� ��������� �����
			for(Iterator it = this.link.getNodeLinks().iterator(); it.hasNext();)
			{
				NodeLink nodeLink = (NodeLink)it.next();
				super.removeNodeLink(nodeLink);
			}
			// ��������� ���� �����
			super.removePhysicalLink(this.link);
			// ����������� ��� ��������� ����, ������� ��������� �� ��������� �����,
			// � ����������� �� ��� ���������� ������������� ������
			for(Iterator it = cablePathsToScan.iterator(); it.hasNext();)
			{
				CablePath cablePath = (CablePath)it.next();
				
				UnboundLink unbound = super.createUnboundLinkWithNodeLink(
						this.link.getStartNode(),
						this.link.getEndNode());
				unbound.setCablePath(cablePath);

				CableChannelingItem cableChannelingItem = cablePath.getFirstCCI(this.link);
				CableChannelingItem newCableChannelingItem = CableController.generateCCI(cablePath, unbound);
				newCableChannelingItem.insertSelfBefore(cableChannelingItem);
				cableChannelingItem.setParentPathOwner(null, false);
				cablePath.removeLink(cableChannelingItem);
				cablePath.addLink(unbound, newCableChannelingItem);
			}
			this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
		}
		catch(Throwable e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
}
