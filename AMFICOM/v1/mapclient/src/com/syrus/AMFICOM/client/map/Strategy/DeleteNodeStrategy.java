package com.syrus.AMFICOM.Client.Map.Strategy;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;

//����� ��������� ��������� �������� �������� ���������� ������ Node

public class DeleteNodeStrategy implements MapStrategy{

  LogicalNetLayer logicalNetLayer;
  MapNodeElement myNode;
  ApplicationContext aContext;

  public DeleteNodeStrategy(ApplicationContext aContext, LogicalNetLayer lnl, MapNodeElement node)
  {
	this.aContext = aContext;
    logicalNetLayer = lnl;
    myNode = node;
  }

  public void doContextChanges()
  {
	DataSourceInterface dataSource = logicalNetLayer.mapMainFrame.getContext().getDataSourceInterface();

//������������ �� ���� ������ ���� myNode � �� ������ ���������� �������
   if ( myNode instanceof MapEquipmentNodeElement)
   {
    if ( !logicalNetLayer.mapMainFrame.aContext.getApplicationModel().isEnabled("mapActionDeleteEquipment"))
     {
         return;
     }
   }

   if ( myNode instanceof MapKISNodeElement)
   {
    if ( !logicalNetLayer.mapMainFrame.aContext.getApplicationModel().isEnabled("mapActionDeleteKIS"))
     {
         return;
     }
   }

   if ( myNode instanceof MapPhysicalNodeElement)
   {
    if ( !logicalNetLayer.mapMainFrame.aContext.getApplicationModel().isEnabled("mapActionDeleteNode"))
     {
         return;
     }
   }

   if ( myNode instanceof MapMarkElement)
   {
    if ( !logicalNetLayer.mapMainFrame.aContext.getApplicationModel().isEnabled("mapActionMarkDelete"))
     {
         return;
     }
   }


//�������� ����, ��� myNode �������� PhysicalNodeElement
    if (myNode instanceof MapPhysicalNodeElement)
    {
     MapPhysicalNodeElement myPhysicalNodeElement = (MapPhysicalNodeElement)myNode;

//�������� ����, ��� PhysicalNodeElement �������� Active �.�. PhysicalNodeElement ��������
     if ( myPhysicalNodeElement.isActive() )
      {

       MapNodeElement node1 =(MapNodeElement) logicalNetLayer.getMapContext().getOtherNodeOfNodeLinksContainingNode(myNode).get(0);
       MapNodeElement node2 =(MapNodeElement) logicalNetLayer.getMapContext().getOtherNodeOfNodeLinksContainingNode(myNode).get(1);

       MapPhysicalLinkElement mapPhysicalLinkElement = logicalNetLayer.getMapContext().getPhysicalLinkbyNodeLink( ((MapNodeLinkElement)(logicalNetLayer.getMapContext().getNodeLinksContainingNode(myNode).get(0))).getId() );

//������� myPhysicalNodeElement � ��� NodeLin, ������� ������������ � ����
       mapPhysicalLinkElement.nodeLink_ids.removeElement( ((MapNodeLinkElement)(logicalNetLayer.getMapContext().getNodeLinksContainingNode(myNode).get(0))).getId() );
       mapPhysicalLinkElement.nodeLink_ids.removeElement( ((MapNodeLinkElement)(logicalNetLayer.getMapContext().getNodeLinksContainingNode(myNode).get(1))).getId() );

       Vector deleteNodeLink = logicalNetLayer.getMapContext().getNodeLinksContainingNode((MapNodeElement)myPhysicalNodeElement);
       Enumeration e = deleteNodeLink.elements();

       while(e.hasMoreElements())
        {
         MapNodeLinkElement curNodeLink = (MapNodeLinkElement)e.nextElement();
         logicalNetLayer.getMapContext().removeNodeLink(curNodeLink);
        }

//       MapNodeLinkElement newNodeLink = new MapNodeLinkElement( logicalNetLayer.mapMainFrame.dataSource.GetUId( MapNodeLinkElement.typ),
       MapNodeLinkElement newNodeLink = new MapNodeLinkElement( dataSource.GetUId( MapNodeLinkElement.typ),
                                             node1,
                                             node2,
                                             logicalNetLayer.getMapContext());
	   Pool.put(MapNodeLinkElement.typ, newNodeLink.getId(), newNodeLink);

       logicalNetLayer.getMapContext().removeNode(myNode);
       logicalNetLayer.getMapContext().addNodeLink(newNodeLink);
/////
       if ( mapPhysicalLinkElement.startNode == mapPhysicalLinkElement.endNode )
       {
         MapNodeElement myNode = mapPhysicalLinkElement.startNode;
         mapPhysicalLinkElement.addMapNodeLink( newNodeLink );
         mapPhysicalLinkElement.startNode = myNode;
         mapPhysicalLinkElement.endNode = myNode;
       }
       else
       {
         mapPhysicalLinkElement.addMapNodeLink( newNodeLink );
       }

//////
     }
     else
     {
    //������� PhysicalNodeElement � ��������� � ��� NodeLink
      Vector deleteNodeLink = logicalNetLayer.getMapContext().getNodeLinksContainingNode((MapNodeElement)myPhysicalNodeElement);
      Enumeration e = deleteNodeLink.elements();

      while(e.hasMoreElements())
      {
           MapNodeLinkElement curNodeLink = (MapNodeLinkElement)e.nextElement();
           MapStrategy myStrategy = new DeleteNodeLinkStrategy(aContext, logicalNetLayer, curNodeLink);
           myStrategy.doContextChanges();
      }

     }

    }
    else
    {
 //������� Node � ��������� � ��� NodeLink
      Vector deleteNodeLink = logicalNetLayer.getMapContext().getNodeLinksContainingNode(myNode);
      Enumeration e = deleteNodeLink.elements();

      if ( deleteNodeLink.isEmpty() )
         {
          logicalNetLayer.getMapContext().removeNode(myNode);
         }

      while(e.hasMoreElements())
      {
           MapNodeLinkElement curNodeLink = (MapNodeLinkElement)e.nextElement();
           int state = 0;
//�������� ������������ ������������ NodeLink � ���������
           if (
                 (curNodeLink.startNode instanceof MapPhysicalNodeElement) &&
                !(curNodeLink.endNode instanceof MapPhysicalNodeElement)
                 ||
                 (curNodeLink.endNode instanceof MapPhysicalNodeElement) &&
                !(curNodeLink.startNode instanceof MapPhysicalNodeElement)
              )
            {
              if (curNodeLink.startNode instanceof MapPhysicalNodeElement)
                {
                   MapPhysicalNodeElement physicalNodeElement =
                                    (MapPhysicalNodeElement)curNodeLink.startNode;

                 if (physicalNodeElement.isActive())
                     {
                       state = 1;
                     }
                 else
                     {
                       state = 2;
                     }
                }

              if (  curNodeLink.endNode instanceof MapPhysicalNodeElement)
                {
                    MapPhysicalNodeElement physicalNodeElement =
                                        (MapPhysicalNodeElement)curNodeLink.endNode;

                 if (physicalNodeElement.isActive())
                    {
                      state = 3;
                    }
                else
                    {
                      state = 4;
                    }

                }

            }


           if (
                 !(curNodeLink.startNode instanceof MapPhysicalNodeElement) &&
                !(curNodeLink.endNode instanceof MapPhysicalNodeElement)

              )
            {
              state = 5;

            }

  switch (state)
  {
    case 1:
          {
          MapPhysicalNodeElement physicalNodeElement = (MapPhysicalNodeElement)curNodeLink.startNode;
          physicalNodeElement.setNonactive();
          logicalNetLayer.getMapContext().removeNodeLink(curNodeLink);
          logicalNetLayer.getMapContext().removeNode(myNode);
          }
          break;
    case 2:
          {
          MapPhysicalNodeElement physicalNodeElement = (MapPhysicalNodeElement)curNodeLink.startNode;
          logicalNetLayer.getMapContext().removeNode(physicalNodeElement);
          logicalNetLayer.getMapContext().removeNodeLink(curNodeLink);
          logicalNetLayer.getMapContext().removeNode(myNode);
          }
          break;

    case 3:
          {
          MapPhysicalNodeElement physicalNodeElement = (MapPhysicalNodeElement)curNodeLink.endNode;
          physicalNodeElement.setNonactive();
          logicalNetLayer.getMapContext().removeNodeLink(curNodeLink);
          logicalNetLayer.getMapContext().removeNode(myNode);
          }
          break;
    case 4:
          {
          MapPhysicalNodeElement physicalNodeElement = (MapPhysicalNodeElement)curNodeLink.endNode;
          logicalNetLayer.getMapContext().removeNode(physicalNodeElement);
          logicalNetLayer.getMapContext().removeNodeLink(curNodeLink);
          logicalNetLayer.getMapContext().removeNode(myNode);
          }
          break;
    case 5:
          {
          logicalNetLayer.getMapContext().removeNodeLink(curNodeLink);
          logicalNetLayer.getMapContext().removeNode(myNode);
          }
          break;

  }

       }
      MapElement myMapElement = new VoidMapElement( logicalNetLayer);

		Dispatcher disp = aContext.getDispatcher();
		if(disp != null)
		{
			logicalNetLayer.perform_processing = false;
			disp.notify(new MapNavigateEvent(myMapElement, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
			logicalNetLayer.perform_processing = true;
		}
//      logicalNetLayer.parent.setContents((ObjectResource )myMapElement);//��������� ������� �������
    }
  }

}

