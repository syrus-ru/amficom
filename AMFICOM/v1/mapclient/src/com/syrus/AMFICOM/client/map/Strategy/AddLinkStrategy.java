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
 * ����� $RCSfile: AddLinkStrategy.java,v $ ������������ ��� 
 * ����� �������������� �������� �������� nodeLink
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

		//���� endNode == null �� ������� ����� MapPhysicalNodeElement
		//� � ���� Link
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

		boolean exists = false;//���� ������������� NodeLink

		Iterator e = logicalNetLayer.getMapContext().getNodeLinks().iterator();
		MapNodeLinkElement curLink;

		//��������� �� ����� � ������� ������������� NodeLink
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

		//���� NOdeLink ���������� �� ������ �� ������
		if(exists)
		{
			return;
		}
		//NOdeLink �� ���������� - ������� ���
		MapNodeLinkElement myNodeLink = new MapNodeLinkElement( 
			dataSource.GetUId( MapNodeLinkElement.typ),
			startNode, 
			endNode, 
			logicalNetLayer.getMapContext());
		Pool.put(MapNodeLinkElement.typ, myNodeLink.getId(), myNodeLink);

		logicalNetLayer.getMapContext().getNodeLinks().add(myNodeLink);

		//����� � ����������� �� ���� ����� ���������� startNode � endNode
		// �.�. ������� ����� � ��� startNode � endNode
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

		//���� startNode KIS ��� Equip � endNode = MapPhysicalNodeElement
		if ( !( startNode instanceof MapPhysicalNodeElement ) 
			&& ( endNode instanceof MapPhysicalNodeElement ) )
		{
			if ( logicalNetLayer.getMapContext().getNodeLinksContainingNode( endNode).size() == 2 )
			{
				Iterator e1 = logicalNetLayer.getMapContext().getNodeLinksContainingNode( endNode).iterator();

				//��������� � �������� nodeLink �������� �� myNodeLink (� �� ������ ���) � ��������� ��� � myPhysicalLinkElement
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
				//����� �.�. � MapPhysicalNodeElement ����� ���� ������������ ��� 2 ��� 1 NodeLink
				//� ������ ������ 1 ������ myPhysicalLinkElement � ��������� � ���� myNodeLink.getID()
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

		//� ���� ������ � myPhysicalLinkElement ��������� myNodeLink
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

		//� ���� ������ � ���� �� physicalLink ����������� ��� NodeLinks ������, � ������ ������������
		if ( ( startNode instanceof MapPhysicalNodeElement ) 
			&& ( endNode instanceof MapPhysicalNodeElement ) )
		{
			MapPhysicalLinkElement myPhysicalLinkElement = null;
			Iterator e2 = logicalNetLayer.getMapContext().getNodeLinksContainingNode(startNode).iterator();
	
			//� myPhysicalLinkElement ��������� myNodeLink
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
					//�������� ������ physicalLink
					MapPhysicalLinkElement newPhysicalLinkElement = logicalNetLayer.getMapContext().getPhysicalLinkbyNodeLink(  nodeLink.getId() );

					//��������� � myPhysicalLinkElement NodeLink newPhysicalLinkElement
					for (int i = 0; i < newPhysicalLinkElement.nodeLink_ids.size(); i++ )
					{
						myPhysicalLinkElement.nodeLink_ids.add( logicalNetLayer.getMapContext().getNodeLink( (String) newPhysicalLinkElement.nodeLink_ids.get(i)).getId() );
					}
					//������� newPhysicalLinkElement
					logicalNetLayer.getMapContext().removePhysicalLink( newPhysicalLinkElement);
		
					for (int i = 0; i < logicalNetLayer.getMapContext().getTransmissionPathByPhysicalLink(newPhysicalLinkElement.getId()).size();i++)
					{
						//������� �� ������� MapTransmissionPathElement � ������� ���������� newPhysicalLinkElement
						//� ��������� myPhysicalLinkElement
						((MapTransmissionPathElement) (logicalNetLayer.getMapContext().
							getTransmissionPathByPhysicalLink( newPhysicalLinkElement.getId() ).get(i))
							).physicalLink_ids.removeElement(newPhysicalLinkElement.getId());

						((MapTransmissionPathElement) (logicalNetLayer.getMapContext().
							getTransmissionPathByPhysicalLink( newPhysicalLinkElement.getId() ).get(i))
							).physicalLink_ids.add(myPhysicalLinkElement.getId());
					}

					//��������� �����
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