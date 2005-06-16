/**
 * $Id: DeleteSelectionCommand.java,v 1.18 2005/06/16 10:57:19 krupenn Exp $
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

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;

/**
 * ������� ��������� �������� �����. ������� �������� ������ ������ 
 * (CommandBundle), ��������� ��������� ��������.
 * @author $Author: krupenn $
 * @version $Revision: 1.18 $, $Date: 2005/06/16 10:57:19 $
 * @module mapviewclient_v1
 */
public class DeleteSelectionCommand extends MapActionCommandBundle
{
	/**
	 * ��� ��������� ����������� ���� ���� ��������� ������� �� ��������
	 * ��������� ��������. ���������� �������� �������������� ������
	 * ��� ������ execute()
	 */
	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		super.setLogicalNetLayer(logicalNetLayer);
		
		//������� ��� ��������� �������� ������������ �� ���������� �� �� ��������
		Iterator e;

		LinkedList nodesToDelete = new LinkedList();
		LinkedList nodeLinksToDelete = new LinkedList();
		LinkedList linksToDelete = new LinkedList();
		LinkedList cablePathsToDelete = new LinkedList();
//		LinkedList pathsToDelete = new LinkedList();

		int showMode = logicalNetLayer.getMapState().getShowMode();

		for(Iterator it = logicalNetLayer.getSelectedElements().iterator(); it.hasNext();)
		{
			MapElement me = (MapElement)it.next();
			if(me instanceof AbstractNode)
			{
				nodesToDelete.add(me);
			}
			else
			if(me instanceof NodeLink
				&& showMode == MapState.SHOW_NODE_LINK)
			{
				NodeLink nodeLink = (NodeLink)me;
				PhysicalLink link = nodeLink.getPhysicalLink();
				if(!(link instanceof UnboundLink))
					nodeLinksToDelete.add(nodeLink);
			}
			else
			if(me instanceof PhysicalLink
				&& showMode == MapState.SHOW_PHYSICAL_LINK)
			{
				if(! (me instanceof UnboundLink))
					linksToDelete.add(me);
			}
		}

/*
		if(logicalNetLayer.getMapState().getShowMode() == MapState.SHOW_CABLE_PATH)
		{
			for(e = logicalNetLayer.getMapView().getCablePaths().iterator(); e.hasNext();)
			{
				MapCablePathElement cablePath = (MapCablePathElement)e.next();
				if (cablePath.isSelected())
				{
					for(Iterator it = cablePath.getLinks().iterator(); it.hasNext();)
					{
						MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
						nodeLinksToDelete.addAll(link.getNodeLinks());
						linksToDelete.add(link);
					}
				}
				cablePathsToDelete.add(cablePath);
			}
		}
*/
/*
		{
			e = map.getTransmissionPath().iterator();
			while (e.hasNext())
			{
				MapTransmissionPathElement path = (MapTransmissionPathElement )e.next();
				if (path.isSelected())
				{
					pathsToDelete.add(path);
				}
			}
		}
		{
			e = map.getMarkers().iterator();
			while (e.hasNext())
			{
				MapMarker marker = (MapMarker )e.next();
				if (marker.isSelected())
				{
					//marker.sendMessage_Marker_Deleted();
					map.getMarkers().remove(marker);

					if(marker instanceof MapAlarmMarker)
					{
						MapAlarmMarker amarker = (MapAlarmMarker )marker;
						MapPhysicalLinkElement link = logicalNetLayer.findMapLinkByCableLink(amarker.link_id);
						if(link != null)
						{
							link.setAlarmState(false);
						}
						else
						{
							MapSiteNodeElement enode = logicalNetLayer.findMapElementByCableLink(amarker.link_id);
							if(enode != null)
							{
								enode.setAlarmState(false);
							}
						}
					}

					//���� ���������� ��� ������ �� �������� ���
					if (logicalNetLayer.getMapView().getMap().getMarkers().size() > 0)
					{
						((MapMarker)(map.getMarkers().get(0))).setSelected(true);
					}

					logicalNetLayer.getContext().getDispatcher().notify( 
						new MapNavigateEvent(
							this,
							MapNavigateEvent.MAP_MARKER_DELETED_EVENT,
							marker.getId(),
							0.0D,
							marker.getTransmissionPath().getId(),
							"") );
				}
			}
		}
*/

		// ������� ������ ������ �������� ����������
		e = linksToDelete.iterator();
		while (e.hasNext())
		{
			DeletePhysicalLinkCommandBundle command = new DeletePhysicalLinkCommandBundle(
					(PhysicalLink)e.next());
			command.setLogicalNetLayer(logicalNetLayer);
			add(command);
		}

		// ������� ������ ������ �������� ����������
		e = nodeLinksToDelete.iterator();
		while (e.hasNext())
		{
			DeleteNodeLinkCommandBundle command = new DeleteNodeLinkCommandBundle(
					(NodeLink)e.next());
			command.setLogicalNetLayer(logicalNetLayer);
			add(command);
		}

		// ������� ������ ������ �������� �����
		e = nodesToDelete.iterator();
		while (e.hasNext())
		{
			DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(
					(AbstractNode)e.next());
			command.setLogicalNetLayer(logicalNetLayer);
			add(command);
		}

		// ������� ������ ������ �������� �����
		e = cablePathsToDelete.iterator();
		while (e.hasNext())
		{
//			removeCablePath((MapCablePathElement )e.next());
			UnPlaceSchemeCableLinkCommand command = new UnPlaceSchemeCableLinkCommand(
					(CablePath)e.next());
			command.setLogicalNetLayer(logicalNetLayer);
			command.execute();
		}
		
/*
		e = pathsToDelete.iterator();
		while (e.hasNext())
		{
			logicalNetLayer.getMapView().removePath(
				(MapPathElement )e.next());
		}
*/
	}
	
	/**
	 * ����� �������� �������� ������� ������� ����� � ����������
	 * ���������� �� ���������� �����
	 */
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");
		
		// ��������� ��� ������� � ������
		super.execute();

		MapElement mapElement = com.syrus.AMFICOM.mapview.VoidElement.getInstance(this.logicalNetLayer.getMapView());

		this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
		this.logicalNetLayer.setCurrentMapElement(mapElement);
	}


}
