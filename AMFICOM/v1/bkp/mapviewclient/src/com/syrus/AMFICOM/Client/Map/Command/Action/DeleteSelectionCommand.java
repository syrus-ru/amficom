/**
 * $Id: DeleteSelectionCommand.java,v 1.12 2005/01/30 15:38:17 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.Client.Map.mapview.VoidElement;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Удалить выбранные элементы карты. Команда является пучком команд 
 * (CommandBundle), удаляющих отдельные элементы.
 * 
 * 
 * 
 * @version $Revision: 1.12 $, $Date: 2005/01/30 15:38:17 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class DeleteSelectionCommand extends MapActionCommandBundle
{
	/**
	 * при установке логического слоя сети создаются команды на удаление
	 * выбранных объектов. выполнение удаления осуществляется только
	 * при вызове execute()
	 */
	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		super.setLogicalNetLayer(logicalNetLayer);
		
		MapView mapView = logicalNetLayer.getMapView();
		Map map = mapView.getMap();

		//Удаляем все выбранные элементы взависимости от разрешения на их удаление
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

					//Если существует ещё маркер то выбыраем его
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

		// создать список команд удаления фрагментов
		e = linksToDelete.iterator();
		while (e.hasNext())
		{
			DeletePhysicalLinkCommandBundle command = new DeletePhysicalLinkCommandBundle(
					(PhysicalLink)e.next());
			command.setLogicalNetLayer(logicalNetLayer);
			add(command);
		}

		// создать список команд удаления фрагментов
		e = nodeLinksToDelete.iterator();
		while (e.hasNext())
		{
			DeleteNodeLinkCommandBundle command = new DeleteNodeLinkCommandBundle(
					(NodeLink)e.next());
			command.setLogicalNetLayer(logicalNetLayer);
			add(command);
		}

		// создать список команд удаления узлов
		e = nodesToDelete.iterator();
		while (e.hasNext())
		{
			DeleteNodeCommandBundle command = new DeleteNodeCommandBundle(
					(AbstractNode)e.next());
			command.setLogicalNetLayer(logicalNetLayer);
			add(command);
		}

		// создать список команд удаления узлов
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
	 * после удаления обновить текущий элемент карты и оповестить
	 * слушателей об изменениях карты
	 */
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");
		
		// выполнить все команды в списке
		super.execute();

		MapElement mapElement = com.syrus.AMFICOM.Client.Map.mapview.VoidElement.getInstance(logicalNetLayer.getMapView());

		logicalNetLayer.setCurrentMapElement(mapElement);
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(mapElement, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.sendMapEvent(new MapEvent(mapElement, MapEvent.MAP_CHANGED));
	}


}
