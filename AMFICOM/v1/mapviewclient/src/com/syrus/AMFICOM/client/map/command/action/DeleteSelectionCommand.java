/**
 * $Id: DeleteSelectionCommand.java,v 1.27 2005/08/24 08:19:58 krupenn Exp $
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
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.util.Log;

/**
 * ������� ��������� �������� �����. ������� �������� ������ ������ 
 * (CommandBundle), ��������� ��������� ��������.
 * @author $Author: krupenn $
 * @version $Revision: 1.27 $, $Date: 2005/08/24 08:19:58 $
 * @module mapviewclient
 */
public class DeleteSelectionCommand extends MapActionCommandBundle {
	/**
	 * ��� ��������� ����������� ���� ���� ��������� ������� �� �������� ���������
	 * ��������. ���������� �������� �������������� ������ ��� ������ execute()
	 */
	@Override
	public void setNetMapViewer(final NetMapViewer netMapViewer) {
		super.setNetMapViewer(netMapViewer);

		//������� ��� ��������� �������� ������������ �� ���������� �� �� ��������
		Iterator e;

		final LinkedList<AbstractNode> nodesToDelete = new LinkedList<AbstractNode>();
		final LinkedList<NodeLink> nodeLinksToDelete = new LinkedList<NodeLink>();
		final LinkedList<PhysicalLink> linksToDelete = new LinkedList<PhysicalLink>();
		final LinkedList cablePathsToDelete = new LinkedList();
//		LinkedList pathsToDelete = new LinkedList();

		final int showMode = this.logicalNetLayer.getMapState().getShowMode();

		for(Iterator it = this.logicalNetLayer.getSelectedElements().iterator(); it.hasNext();)
		{
			MapElement me = (MapElement)it.next();
			if(me instanceof AbstractNode)
			{
				nodesToDelete.add((AbstractNode) me);
			}
			else
			if(me instanceof NodeLink
				&& showMode == MapState.SHOW_NODE_LINK)
			{
				NodeLink nodeLink = (NodeLink)me;
				PhysicalLink link = nodeLink.getPhysicalLink();
				if(!(link instanceof UnboundLink)) {
					nodeLinksToDelete.add(nodeLink);
				}
			}
			else
			if(me instanceof PhysicalLink
				&& showMode == MapState.SHOW_PHYSICAL_LINK)
			{
				if(! (me instanceof UnboundLink)) {
					linksToDelete.add((PhysicalLink) me);
				}
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
			command.setNetMapViewer(this.netMapViewer);
			add(command);
		}

		// ������� ������ ������ �������� ����������
		e = nodeLinksToDelete.iterator();
		while (e.hasNext())
		{
			DeleteNodeLinkCommandBundle command = new DeleteNodeLinkCommandBundle(
					(NodeLink)e.next());
			command.setNetMapViewer(this.netMapViewer);
			add(command);
		}

		// ������� ������ ������ �������� �����
		e = nodesToDelete.iterator();
		while (e.hasNext())
		{
			DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(
					(AbstractNode)e.next());
			command.setNetMapViewer(this.netMapViewer);
			add(command);
		}

		// ������� ������ ������ �������� �����
		e = cablePathsToDelete.iterator();
		while (e.hasNext())
		{
//			removeCablePath((MapCablePathElement )e.next());
			UnPlaceSchemeCableLinkCommand command = new UnPlaceSchemeCableLinkCommand(
					(CablePath)e.next());
			command.setNetMapViewer(this.netMapViewer);
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
	@Override
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);
		
		// ��������� ��� ������� � ������
		super.execute();

		MapElement mapElement = com.syrus.AMFICOM.mapview.VoidElement.getInstance(this.logicalNetLayer.getMapView());

		this.logicalNetLayer.setCurrentMapElement(mapElement);
	}


}
