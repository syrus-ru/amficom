package com.syrus.AMFICOM.Client.Configure.Map.Strategy;

import java.awt.*;
import java.util.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;

import com.syrus.AMFICOM.Client.Configure.Map.*;

public class DeleteSelectionStrategy implements MapStrategy{

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
		Enumeration e = logicalNetLayer.getMapContext().getNodes().elements();

		Vector nodesToDelete = new Vector();
		Vector nodeLinksToDelete = new Vector();
		Vector linksToDelete = new Vector();
		Vector pathsToDelete = new Vector();

		while (e.hasMoreElements())
		{
			MapNodeElement myNode = (MapNodeElement) e.nextElement();
			if (myNode.isSelected())
			{
				nodesToDelete.addElement(myNode);
			}
		}

		if ( logicalNetLayer.mapMainFrame
				.aContext.getApplicationModel().isEnabled("mapActionDeleteNode"))
		{
			System.out.println("mapActionDeleteNode");
			e = logicalNetLayer.getMapContext().getNodeLinks().elements();
			while (e.hasMoreElements())
			{
				MapNodeLinkElement myNodeLink = (MapNodeLinkElement) e.nextElement();
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
			e = logicalNetLayer.getMapContext().getPhysicalLinks().elements();
			while (e.hasMoreElements())
			{
				MapPhysicalLinkElement link = (MapPhysicalLinkElement )e.nextElement();
				if (link.isSelected())
				{
					for(
							Enumeration en = 
								logicalNetLayer.getMapContext().getNodeLinksInPhysicalLink(link.getId()).elements();
							en.hasMoreElements();)
						nodeLinksToDelete.add((MapNodeLinkElement )en.nextElement());
					linksToDelete.add(link);
				}
			}
		}

		if ( logicalNetLayer.mapMainFrame
				.aContext.getApplicationModel().isEnabled("mapActionDeletePath"))
		{
			System.out.println("mapActionDeletePath");
			e = logicalNetLayer.getMapContext().getTransmissionPath().elements();
			while (e.hasMoreElements())
			{
				MapTransmissionPathElement path = (MapTransmissionPathElement )e.nextElement();
				if (path.isSelected())
				{
					pathsToDelete.add(path);
				}
			}
		}

		if ( logicalNetLayer.mapMainFrame
				.aContext.getApplicationModel().isEnabled("mapActionMarkerDelete"))
		{
			e = logicalNetLayer.getMapContext().markers.elements();
			while (e.hasMoreElements())
			{
				MapMarker myMarker = (MapMarker) e.nextElement();
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
		e = nodeLinksToDelete.elements();
		while (e.hasMoreElements())
		{
			MapStrategy myStrategy = new DeleteNodeLinkStrategy(aContext, logicalNetLayer, (MapNodeLinkElement)e.nextElement());
			myStrategy.doContextChanges();
		}

		e = nodesToDelete.elements();
		while (e.hasMoreElements())
		{
			MapStrategy myStrategy = new DeleteNodeStrategy(aContext, logicalNetLayer, (MapNodeElement)e.nextElement());
			myStrategy.doContextChanges();
		}

		e = pathsToDelete.elements();
		while (e.hasMoreElements())
		{
			logicalNetLayer.getMapContext().removeTransmissionPath(
				(MapTransmissionPathElement )e.nextElement());
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
//		logicalNetLayer.parent.setContents((ObjectResource )myMapElement);//Заполняем таблицу свойств

	}
}
