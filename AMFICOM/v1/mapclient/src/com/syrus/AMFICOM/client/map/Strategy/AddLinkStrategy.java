package com.syrus.AMFICOM.Client.Map.Strategy;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import com.ofx.geometry.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;

/**
 * Класс $RCSfile: AddLinkStrategy.java,v $ используется для 
 * здесь обрабатывается создание элемента nodeLink
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/06/28 11:47:51 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class AddLinkStrategy implements MapStrategy 
{
	LogicalNetLayer logicalNetLayer;
	MapNodeElement endNode;
	Point point;

	ApplicationContext aContext;

	public AddLinkStrategy (ApplicationContext aContext, LogicalNetLayer lnl, MapNodeElement myEndNode , Point myPoint )
	{
		this.aContext = aContext;
		logicalNetLayer = lnl;
		endNode = myEndNode;
		point = myPoint;
	}

	public void doContextChanges()
	{
		DataSourceInterface dataSource = logicalNetLayer.mapMainFrame.getContext().getDataSourceInterface();
	
		MapElement curObject = logicalNetLayer.getMapContext().getCurrentMapElement();
		MapNodeElement startNode = (MapNodeElement)curObject;

		//Если endNode == null то создаем новый MapPhysicalNodeElement
		//и к нему Link
		if ( endNode == null)
		{
			SxDoublePoint sp = new SxDoublePoint (
				logicalNetLayer.convertScreenToMap(point).x,
				logicalNetLayer.convertScreenToMap(point).y);

			MapPhysicalNodeElement myPhysicalNodeElement = new MapPhysicalNodeElement( 
				dataSource.GetUId( MapPhysicalNodeElement.typ),
				null,
				sp ,
				logicalNetLayer.getMapContext(),
				MapNodeElement.defaultBounds );

			Pool.put(MapPhysicalNodeElement.typ, myPhysicalNodeElement.getId(), myPhysicalNodeElement);

			myPhysicalNodeElement.setNonactive();
			logicalNetLayer.getMapContext().getNodes().add(myPhysicalNodeElement);
			endNode = (MapNodeElement)myPhysicalNodeElement;
		}//if ( endNode == null)
		else
		{
			if ( endNode instanceof MapPhysicalNodeElement)
			{
				MapPhysicalNodeElement physicalNodeElement1 = (MapPhysicalNodeElement)endNode ;
				physicalNodeElement1.setActive();
			}
		}

		boolean exists = false;//Флаг существования NodeLink

		Iterator e = logicalNetLayer.getMapContext().getNodeLinks().iterator();
		MapNodeLinkElement curLink;

		//Пробегаем по циклу и смотрим существование NodeLink
		while (e.hasNext())
		{
			curLink = (MapNodeLinkElement)e.next();
			if (((curLink.endNode.equals(endNode))&&(curLink.startNode.equals(startNode)))
				|| ((curLink.endNode.equals(startNode))&&(curLink.startNode.equals(endNode))) )
			{
				exists = true;
				break;
			}
		}

		//Если NOdeLink существует то ничего не делать
		if(exists)
		{
			return;
		}
		//NOdeLink Не существует - создать его
		MapNodeLinkElement myNodeLink = new MapNodeLinkElement( 
			dataSource.GetUId( MapNodeLinkElement.typ),
			startNode, 
			endNode, 
			logicalNetLayer.getMapContext());
		Pool.put(MapNodeLinkElement.typ, myNodeLink.getId(), myNodeLink);

		logicalNetLayer.getMapContext().getNodeLinks().add(myNodeLink);

		//Далее в зависимости от того какая комбинация startNode и endNode
		// т.е. смотрим какие у нас startNode и endNode
		if ( !( startNode instanceof MapPhysicalNodeElement ) && !( endNode instanceof MapPhysicalNodeElement ) )
		{
			MapPhysicalLinkElement myPhysicalLinkElement = new MapPhysicalLinkElement( 
				dataSource.GetUId( MapPhysicalLinkElement.typ ),
				startNode, 
				endNode, 
				logicalNetLayer.getMapContext() );
			Pool.put(MapPhysicalLinkElement.typ, myPhysicalLinkElement.getId(), myPhysicalLinkElement);

			Dispatcher disp = aContext.getDispatcher();
			if(disp != null)
			{
				logicalNetLayer.perform_processing = false;
				disp.notify(new OperationEvent(this, 0, "mapchangeevent"));
				logicalNetLayer.perform_processing = true;
			}
			if(disp != null)
			{
				logicalNetLayer.perform_processing = false;
				disp.notify(new MapNavigateEvent(myPhysicalLinkElement, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
				logicalNetLayer.perform_processing = true;
				logicalNetLayer.getMapContext().notifySchemeEvent(myPhysicalLinkElement);
				logicalNetLayer.getMapContext().notifyCatalogueEvent(myPhysicalLinkElement);
			}

			myPhysicalLinkElement.nodeLink_ids.add( myNodeLink.getId());
			logicalNetLayer.getMapContext().getPhysicalLinks().add(myPhysicalLinkElement);
			return;
		}//if ( !( startNode instanceof MapPhysicalNodeElement ) && !( endNode instanceof MapPhysicalNodeElement ) )

		//Если startNode KIS или Equip а endNode = MapPhysicalNodeElement
		if ( !( startNode instanceof MapPhysicalNodeElement ) 
			&& ( endNode instanceof MapPhysicalNodeElement ) )
		{
			if ( logicalNetLayer.getMapContext().getNodeLinksContainingNode( endNode).size() == 2 )
			{
				Iterator e1 = logicalNetLayer.getMapContext().getNodeLinksContainingNode( endNode).iterator();

				//Пробегаем и получаем nodeLink отличный от myNodeLink (а их только два) и добавляем его в myPhysicalLinkElement
				while ( e1.hasNext())
				{
					MapNodeLinkElement nodeLink = (MapNodeLinkElement) e1.next();

					if ( nodeLink != myNodeLink )
					{
						MapPhysicalLinkElement myPhysicalLinkElement = logicalNetLayer.getMapContext().getPhysicalLinkbyNodeLink(  nodeLink.getId() );
						myPhysicalLinkElement.addMapNodeLink( myNodeLink);
					}
				}
			}
			else
			{
				//Иначе т.к. к MapPhysicalNodeElement может быть подсоединено мах 2 мин 1 NodeLink
				//в данном случае 1 создаём myPhysicalLinkElement и добавляем в него myNodeLink.getID()
				MapPhysicalLinkElement myPhysicalLinkElement = new MapPhysicalLinkElement( 
					dataSource.GetUId( MapPhysicalLinkElement.typ ),
					startNode, 
					endNode, 
					logicalNetLayer.getMapContext() );
				Pool.put(MapPhysicalLinkElement.typ, myPhysicalLinkElement.getId(), myPhysicalLinkElement);

				Dispatcher disp = aContext.getDispatcher();
				if(disp != null)
				{
					logicalNetLayer.perform_processing = false;
					disp.notify(new OperationEvent(this, 0, "mapchangeevent"));
					logicalNetLayer.perform_processing = true;
				}
				if(disp != null)
				{
					logicalNetLayer.perform_processing = false;
					disp.notify(new MapNavigateEvent(myPhysicalLinkElement, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
					logicalNetLayer.perform_processing = true;
					logicalNetLayer.getMapContext().notifySchemeEvent(myPhysicalLinkElement);
					logicalNetLayer.getMapContext().notifyCatalogueEvent(myPhysicalLinkElement);
				}
				myPhysicalLinkElement.nodeLink_ids.add( myNodeLink.getId());
				logicalNetLayer.getMapContext().getPhysicalLinks().add(myPhysicalLinkElement);
			}
			return;
		}

		//В этом случае в myPhysicalLinkElement добавляем myNodeLink
		if ( ( startNode instanceof MapPhysicalNodeElement ) && 
			!( endNode instanceof MapPhysicalNodeElement ) )
		{
			MapPhysicalLinkElement myPhysicalLinkElement = null;
			Iterator e2 = logicalNetLayer.getMapContext().getNodeLinksContainingNode( startNode).iterator();

			while(e2.hasNext())
			{
				MapNodeLinkElement nodeLink = (MapNodeLinkElement )e2.next();

				if(nodeLink != myNodeLink)
				{
					myPhysicalLinkElement = logicalNetLayer.getMapContext().getPhysicalLinkbyNodeLink(  nodeLink.getId() );
					myPhysicalLinkElement.addMapNodeLink( myNodeLink);
				}
			}
			return;
		}

		//В этом случае в один из physicalLink добавляются его NodeLinks другой, а другой уничтожается
		if ( ( startNode instanceof MapPhysicalNodeElement ) 
			&& ( endNode instanceof MapPhysicalNodeElement ) )
		{
			MapPhysicalLinkElement myPhysicalLinkElement = null;
			Iterator e2 = logicalNetLayer.getMapContext().getNodeLinksContainingNode(startNode).iterator();
	
			//В myPhysicalLinkElement добавляем myNodeLink
			while(e2.hasNext())
			{
				MapNodeLinkElement nodeLink = (MapNodeLinkElement )e2.next();

				if(nodeLink != myNodeLink)
				{
					myPhysicalLinkElement = logicalNetLayer.getMapContext().getPhysicalLinkbyNodeLink(nodeLink.getId());
					myPhysicalLinkElement.addMapNodeLink( myNodeLink);
				}
			}

			Iterator e1 = logicalNetLayer.getMapContext().getNodeLinksContainingNode( endNode).iterator();

			while ( e1.hasNext())
			{
				MapNodeLinkElement nodeLink = (MapNodeLinkElement) e1.next();

				if ( nodeLink != myNodeLink )
				{
					//получаем другой physicalLink
					MapPhysicalLinkElement newPhysicalLinkElement = logicalNetLayer.getMapContext().getPhysicalLinkbyNodeLink(  nodeLink.getId() );

					//добавляем в myPhysicalLinkElement NodeLink newPhysicalLinkElement
					for (int i = 0; i < newPhysicalLinkElement.nodeLink_ids.size(); i++ )
					{
						myPhysicalLinkElement.nodeLink_ids.add( logicalNetLayer.getMapContext().getNodeLink( (String) newPhysicalLinkElement.nodeLink_ids.get(i)).getId() );
					}
					//удаляем newPhysicalLinkElement
					logicalNetLayer.getMapContext().removePhysicalLink( newPhysicalLinkElement);
		
					for (int i = 0; i < logicalNetLayer.getMapContext().getTransmissionPathByPhysicalLink(newPhysicalLinkElement.getId()).size();i++)
					{
						//Удаляем из вектора MapTransmissionPathElement в которых содержится newPhysicalLinkElement
						//и добавляем myPhysicalLinkElement
						((MapTransmissionPathElement) (logicalNetLayer.getMapContext().
							getTransmissionPathByPhysicalLink( newPhysicalLinkElement.getId() ).get(i))
							).physicalLink_ids.removeElement(newPhysicalLinkElement.getId());

						((MapTransmissionPathElement) (logicalNetLayer.getMapContext().
							getTransmissionPathByPhysicalLink( newPhysicalLinkElement.getId() ).get(i))
							).physicalLink_ids.add(myPhysicalLinkElement.getId());
					}

					//Улаживаем концы
					if ( myPhysicalLinkElement.endNode == newPhysicalLinkElement.endNode )
					{
						myPhysicalLinkElement.endNode = newPhysicalLinkElement.startNode;
						return;
					}

					if ( myPhysicalLinkElement.endNode == newPhysicalLinkElement.startNode )
					{
						myPhysicalLinkElement.endNode = newPhysicalLinkElement.endNode;
						return;
					}
		
					if ( myPhysicalLinkElement.startNode == newPhysicalLinkElement.endNode )
					{
						myPhysicalLinkElement.startNode = newPhysicalLinkElement.startNode;
						return;
					}
		
					if ( myPhysicalLinkElement.startNode == newPhysicalLinkElement.startNode )
					{
						myPhysicalLinkElement.startNode = newPhysicalLinkElement.endNode;
						return;
					}

				}
			}
			return;
		}

	}
}