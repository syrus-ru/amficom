package com.syrus.AMFICOM.Client.Map.Strategy;

import java.awt.*;
import java.util.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;

import com.syrus.AMFICOM.Client.Map.*;

public class DeleteSelectionStrategy implements MapStrategy
{
	LogicalNetLayer logicalNetLayer;
	ApplicationContext aContext;

	public DeleteSelectionStrategy(ApplicationContext aContext, LogicalNetLayer lnl)
	{
		this.aContext = aContext;
		logicalNetLayer = lnl;
	}

	public void doContextChanges()
	{
		//Удаляем все выбранные элементы взависимости от разрешения на их удаление
		Iterator e = logicalNetLayer.getMapContext().getNodes().iterator();

		LinkedList nodesToDelete = new LinkedList();
		LinkedList nodeLinksToDelete = new LinkedList();
		LinkedList linksToDelete = new LinkedList();
		LinkedList pathsToDelete = new LinkedList();

		while (e.hasNext())
		{
			MapNodeElement myNode = (MapNodeElement )e.next();
			if (myNode.isSelected())
			{
				nodesToDelete.add(myNode);
			}
		}

		if ( logicalNetLayer.mapMainFrame
				.aContext.getApplicationModel().isEnabled("mapActionDeleteNode"))
		{
			System.out.println("mapActionDeleteNode");
			e = logicalNetLayer.getMapContext().getNodeLinks().iterator();
			while (e.hasNext())
			{
				MapNodeLinkElement myNodeLink = (MapNodeLinkElement )e.next();
				if (myNodeLink.isSelected())
				{
					nodeLinksToDelete.add(myNodeLink);
				}
			}
		}

		if ( logicalNetLayer.mapMainFrame
				.aContext.getApplicationModel().isEnabled("mapActionDeleteNode"))
		{
			System.out.println("mapActionDeleteNode");
			e = logicalNetLayer.getMapContext().getPhysicalLinks().iterator();
			while (e.hasNext())
			{
				MapPhysicalLinkElement link = (MapPhysicalLinkElement )e.next();
				if (link.isSelected())
				{
					for(Iterator it = logicalNetLayer.getMapContext().getNodeLinksInPhysicalLink(link.getId()).iterator(); it.hasNext();)
						nodeLinksToDelete.add((MapNodeLinkElement )it.next());
					linksToDelete.add(link);
				}
			}
		}

		if ( logicalNetLayer.mapMainFrame
				.aContext.getApplicationModel().isEnabled("mapActionDeletePath"))
		{
			System.out.println("mapActionDeletePath");
			e = logicalNetLayer.getMapContext().getTransmissionPath().iterator();
			while (e.hasNext())
			{
				MapTransmissionPathElement path = (MapTransmissionPathElement )e.next();
				if (path.isSelected())
				{
					pathsToDelete.add(path);
				}
			}
		}

		if ( logicalNetLayer.mapMainFrame
				.aContext.getApplicationModel().isEnabled("mapActionMarkerDelete"))
		{
			e = logicalNetLayer.getMapContext().markers.iterator();
			while (e.hasNext())
			{
				MapMarker myMarker = (MapMarker )e.next();
				if (myMarker.isSelected())
				{
					myMarker.sendMessage_Marker_Deleted();
					logicalNetLayer.getMapContext().markers.remove(myMarker);

					if(myMarker instanceof MapAlarmMarker)
					{
						MapAlarmMarker marker = (MapAlarmMarker )myMarker;
						MapPhysicalLinkElement link = logicalNetLayer.mapMainFrame.findMapLinkByCableLink(marker.link_id);
						if(link != null)
						{
							link.setAlarmState(false);
						}
						else
						{
							MapEquipmentNodeElement node = logicalNetLayer.mapMainFrame.findMapElementByCableLink(marker.link_id);
							if(node != null)
							{
								node.setAlarmState(false);
							}
						}
					}

					//Если существует ещё маркер то выбыраем его
					if (logicalNetLayer.getMapContext().markers.size() > 0)
					{
						((MapMarker)(logicalNetLayer.getMapContext().markers.get(0))).select();
					}

					logicalNetLayer.mapMainFrame.aContext.getDispatcher().notify( 
						new MapNavigateEvent(
							this,
							MapNavigateEvent.MAP_MARKER_DELETED_EVENT,
							 myMarker.getId(),
							 0.0D,
							 myMarker.transmissionPath.getId(),
							 "") );
				}
			}
		}

		//deleting
		e = nodeLinksToDelete.iterator();
		while (e.hasNext())
		{
			MapStrategy myStrategy = new DeleteNodeLinkStrategy(
					aContext, 
					logicalNetLayer, 
					(MapNodeLinkElement )e.next());
			myStrategy.doContextChanges();
		}

		e = nodesToDelete.iterator();
		while (e.hasNext())
		{
			MapStrategy myStrategy = new DeleteNodeStrategy(
					aContext, 
					logicalNetLayer, 
					(MapNodeElement )e.next());
			myStrategy.doContextChanges();
		}

		e = pathsToDelete.iterator();
		while (e.hasNext())
		{
			logicalNetLayer.getMapContext().removeTransmissionPath(
				(MapTransmissionPathElement )e.next());
		}

		MapElement myMapElement = new VoidMapElement( logicalNetLayer);
			Dispatcher disp = aContext.getDispatcher();
			if(disp != null)
			{
				logicalNetLayer.perform_processing = false;
				disp.notify(new OperationEvent(myMapElement, 0, "mapchangeevent"));
				disp.notify(new MapNavigateEvent(myMapElement, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
				logicalNetLayer.perform_processing = true;
			}
	}
}
