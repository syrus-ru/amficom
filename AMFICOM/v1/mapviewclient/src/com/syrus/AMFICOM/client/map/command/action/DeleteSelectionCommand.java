/**
 * $Id: DeleteSelectionCommand.java,v 1.4 2004/10/09 13:33:40 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.VoidMapElement;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * ������� ��������� �������� �����. ������� �������� ������ ������ 
 * (CommandBundle), ��������� ��������� ��������.
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/10/09 13:33:40 $
 * @module
 * @author $Author: krupenn $
 * @see
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
		
		Map map = logicalNetLayer.getMapView().getMap();

		//������� ��� ��������� �������� ������������ �� ���������� �� �� ��������
		Iterator e;

		LinkedList nodesToDelete = new LinkedList();
		LinkedList nodeLinksToDelete = new LinkedList();
		LinkedList linksToDelete = new LinkedList();
		LinkedList cablePathsToDelete = new LinkedList();
//		LinkedList pathsToDelete = new LinkedList();

		for(e  = map.getNodes().iterator();e.hasNext();)
		{
			MapNodeElement node = (MapNodeElement )e.next();
			if (node.isSelected())
			{
				nodesToDelete.add(node);
			}
		}

		if ( logicalNetLayer.getContext().getApplicationModel().isEnabled("mapActionDeleteNode"))
		if(logicalNetLayer.getMapState().getShowMode() == MapState.SHOW_NODE_LINK)
		{
			for(e = map.getNodeLinks().iterator(); e.hasNext();)
			{
				MapNodeLinkElement nodeLink = (MapNodeLinkElement )e.next();
				if (nodeLink.isSelected())
				{
					MapLinkElement link = map.getPhysicalLink(nodeLink.getPhysicalLinkId());
					if(!(link instanceof MapUnboundLinkElement))
						nodeLinksToDelete.add(nodeLink);
				}
			}
		}

		// ��� �������� ���������� ����� ��������� ��� �� ��������� � 
		// �������������� ����
		if ( logicalNetLayer.getContext().getApplicationModel().isEnabled("mapActionDeleteNode"))
		if(logicalNetLayer.getMapState().getShowMode() == MapState.SHOW_PHYSICAL_LINK)
		{
			for(e = map.getPhysicalLinks().iterator(); e.hasNext();)
			{
				MapPhysicalLinkElement link = (MapPhysicalLinkElement )e.next();
				if (link.isSelected() 
					&& !(link instanceof MapUnboundLinkElement))
				{
//					for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();)
//						nodeLinksToDelete.add((MapNodeLinkElement )it.next());
					linksToDelete.add(link);
				}
			}
		}
/*
		if(logicalNetLayer.getMapState().getShowMode() == MapState.SHOW_CABLE_PATH)
		{
			for(e = logicalNetLayer.getMapView().getCablePaths().iterator(); e.hasNext();)
			{
				MapCablePathElement cp = (MapCablePathElement)e.next();
				if (cp.isSelected())
				{
					for(Iterator it = cp.getLinks().iterator(); it.hasNext();)
					{
						MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
						nodeLinksToDelete.addAll(link.getNodeLinks());
						linksToDelete.add(link);
					}
				}
				cablePathsToDelete.add(cp);
			}
		}
*/
/*
		if ( logicalNetLayer.getContext().getApplicationModel().isEnabled("mapActionDeletePath"))
		{
			System.out.println("mapActionDeletePath");
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
		if ( logicalNetLayer.getContext().getApplicationModel().isEnabled("mapActionMarkerDelete"))
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
					(MapPhysicalLinkElement )e.next());
			command.setLogicalNetLayer(logicalNetLayer);
			add(command);
		}

		// ������� ������ ������ �������� ����������
		e = nodeLinksToDelete.iterator();
		while (e.hasNext())
		{
			DeleteNodeLinkCommandBundle command = new DeleteNodeLinkCommandBundle(
					(MapNodeLinkElement )e.next());
			command.setLogicalNetLayer(logicalNetLayer);
			add(command);
		}

		// ������� ������ ������ �������� �����
		e = nodesToDelete.iterator();
		while (e.hasNext())
		{
			DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(
					(MapNodeElement )e.next());
			command.setLogicalNetLayer(logicalNetLayer);
			add(command);
		}

		// ������� ������ ������ �������� �����
		e = cablePathsToDelete.iterator();
		while (e.hasNext())
		{
//			removeCablePath((MapCablePathElement )e.next());
			UnPlaceSchemeCableLinkCommand command = new UnPlaceSchemeCableLinkCommand(
					(MapCablePathElement )e.next());
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
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "execute()");
		
		// ��������� ��� ������� � ������
		super.execute();
		
		MapElement mapElement = VoidMapElement.getInstance(logicalNetLayer.getMapView());

		logicalNetLayer.sendMapEvent(new MapEvent(mapElement, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(mapElement, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
	}


}
