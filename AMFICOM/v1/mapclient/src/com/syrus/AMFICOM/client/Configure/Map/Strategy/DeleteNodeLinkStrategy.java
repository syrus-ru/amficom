package com.syrus.AMFICOM.Client.Configure.Map.Strategy;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.General.Event.*;

public class DeleteNodeLinkStrategy implements MapStrategy {
  MapNodeLinkElement myNodeLink;
  LogicalNetLayer logicalNetLayer;
  ApplicationContext aContext;

  public DeleteNodeLinkStrategy(ApplicationContext aContext, LogicalNetLayer lnl, MapNodeLinkElement curNodeLink)
  {
	this.aContext = aContext;
    logicalNetLayer = lnl;
    myNodeLink = curNodeLink;
  }

  public void doContextChanges()
  {
//В данном классе реализуется алгоритм удаления myNodeLink. Взависимости от того
//какие конечные точки на концах происходит операция удаления nodeLink,
//physicalLink и tansmissionpath
	DataSourceInterface dataSource = logicalNetLayer.mapMainFrame.getContext().getDataSourceInterface();
    if ( logicalNetLayer.getMapContext().
              getTransmissionPathByNodeLink( myNodeLink.getId()).size() > 0 )
    {
      JOptionPane.showMessageDialog(
			logicalNetLayer.mainFrame,
			"Нельзя удалить фрагмент линии связи!\n" +
				"Сначала удалите все пути тестирования данного фрагмента",
			"", 
			JOptionPane.ERROR_MESSAGE);
      return;
    }


    int state = 0;

      if (
           (
             !( myNodeLink.startNode instanceof MapPhysicalNodeElement) &&
              ( myNodeLink.endNode instanceof MapPhysicalNodeElement)
           )
            ||
           (
              ( myNodeLink.startNode instanceof MapPhysicalNodeElement) &&
             !( myNodeLink.endNode instanceof MapPhysicalNodeElement)
           )
         )
       {
        if (  myNodeLink.startNode instanceof MapPhysicalNodeElement)
          {
           MapPhysicalNodeElement physicalNodeElement = (MapPhysicalNodeElement)myNodeLink.startNode;
            if (physicalNodeElement.isActive())
             {
                state = 1;
             }
            else
             {
                state = 2;
             }
          }

        if (  myNodeLink.endNode instanceof MapPhysicalNodeElement)
          {
            MapPhysicalNodeElement physicalNodeElement = (MapPhysicalNodeElement)myNodeLink.endNode;
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
///////////////////////////////////////////////////////////////////////////////
        if (
              ( myNodeLink.startNode instanceof MapPhysicalNodeElement) &&
              ( myNodeLink.endNode instanceof MapPhysicalNodeElement)
            )
          {

      MapPhysicalNodeElement physicalNodeElement1 = (MapPhysicalNodeElement)myNodeLink.startNode;
      MapPhysicalNodeElement physicalNodeElement2 = (MapPhysicalNodeElement)myNodeLink.endNode;

           if ( !physicalNodeElement1.isActive() && physicalNodeElement2.isActive() )
              {
                state = 5;
              }

           if ( physicalNodeElement1.isActive() && !physicalNodeElement2.isActive() )
              {
                state = 6;
              }

           if ( physicalNodeElement1.isActive() && physicalNodeElement2.isActive() )
              {
                state = 7;
              }

           if ( !physicalNodeElement1.isActive() && !physicalNodeElement2.isActive() )
              {
                state = 8;
              }

          }

      if (
             !( myNodeLink.startNode instanceof MapPhysicalNodeElement) &&
             !( myNodeLink.endNode instanceof MapPhysicalNodeElement)
         )
         {
           state = 9;
         }

//    JOptionPane.showMessageDialog( logicalNetLayer.mainFrame, " state " + state);
{
      MapPhysicalLinkElement mapPhysicalLinkElement = logicalNetLayer.getMapContext().getPhysicalLinkbyNodeLink( myNodeLink.getId());
      logicalNetLayer.getMapContext().deleteTranmissionPath( mapPhysicalLinkElement.getId() );

		Dispatcher disp = aContext.getDispatcher();
		if(disp != null)
		{
			logicalNetLayer.perform_processing = false;
			disp.notify(new OperationEvent(this, 0, "mapchangeevent"));
			logicalNetLayer.perform_processing = true;
		}
//      logicalNetLayer.parent.myMapElementsPanel.updateTable();
}

switch (state)
{
 case 1: {
//Получаем physicalNodeElement и делаем его не активным
          MapPhysicalNodeElement physicalNodeElement = (MapPhysicalNodeElement)myNodeLink.startNode;
          physicalNodeElement.setNonactive();
          logicalNetLayer.getMapContext().removeNodeLink(myNodeLink);

//Удаляем myNodeLink из mapPhysicalLinkElement
          MapPhysicalLinkElement mapPhysicalLinkElement = logicalNetLayer.getMapContext().getPhysicalLinkbyNodeLink( myNodeLink.getId());
          mapPhysicalLinkElement.nodeLink_ids.removeElement( myNodeLink.getId());

           if (mapPhysicalLinkElement.startNode == myNodeLink.endNode)
           {
             mapPhysicalLinkElement.startNode = myNodeLink.startNode;
           }
           else
           {
             mapPhysicalLinkElement.endNode = myNodeLink.startNode;
           }

          }
          break;

 case 2:  {
//Получаем physicalNodeElement и удаляем его с myNodeLink
          MapPhysicalNodeElement physicalNodeElement = (MapPhysicalNodeElement)myNodeLink.startNode;
          logicalNetLayer.getMapContext().removeNode(physicalNodeElement);
          logicalNetLayer.getMapContext().removeNodeLink(myNodeLink);

          MapPhysicalLinkElement mapPhysicalLinkElement = logicalNetLayer.getMapContext().getPhysicalLinkbyNodeLink( myNodeLink.getId());
          logicalNetLayer.getMapContext().removePhysicalLink( mapPhysicalLinkElement);

			Dispatcher disp = aContext.getDispatcher();
			if(disp != null)
			{
				logicalNetLayer.perform_processing = false;
				disp.notify(new OperationEvent(this, 0, "mapchangeevent"));
				logicalNetLayer.perform_processing = true;
			}
//          logicalNetLayer.parent.myMapElementsPanel.updateTable();
          }
          break;
 case 3:  {
//Получаем physicalNodeElement и делаем его не активным
          MapPhysicalNodeElement physicalNodeElement = (MapPhysicalNodeElement)myNodeLink.endNode;
          physicalNodeElement.setNonactive();
          logicalNetLayer.getMapContext().removeNodeLink(myNodeLink);

          MapPhysicalLinkElement mapPhysicalLinkElement = logicalNetLayer.getMapContext().getPhysicalLinkbyNodeLink( myNodeLink.getId());
          mapPhysicalLinkElement.nodeLink_ids.removeElement( myNodeLink.getId());

           if ( mapPhysicalLinkElement.startNode == myNodeLink.startNode )
           {
             mapPhysicalLinkElement.startNode = myNodeLink.endNode;
           }
           else
           {
             mapPhysicalLinkElement.endNode = myNodeLink.endNode;
           }

           if ( mapPhysicalLinkElement.nodeLink_ids.size() == 0)
           {
            logicalNetLayer.getMapContext().removePhysicalLink( mapPhysicalLinkElement);

			Dispatcher disp = aContext.getDispatcher();
			if(disp != null)
			{
				logicalNetLayer.perform_processing = false;
				disp.notify(new OperationEvent(this, 0, "mapchangeevent"));
				logicalNetLayer.perform_processing = true;
			}
//            logicalNetLayer.parent.myMapElementsPanel.updateTable();
           }
          }
          break;
 case 4:  {
//Получаем physicalNodeElement и удаляем его с myNodeLink
          MapPhysicalNodeElement physicalNodeElement = (MapPhysicalNodeElement)myNodeLink.endNode;
          logicalNetLayer.getMapContext().removeNode(physicalNodeElement);
          logicalNetLayer.getMapContext().removeNodeLink(myNodeLink);

          MapPhysicalLinkElement mapPhysicalLinkElement = logicalNetLayer.getMapContext().getPhysicalLinkbyNodeLink( myNodeLink.getId());
          logicalNetLayer.getMapContext().removePhysicalLink( mapPhysicalLinkElement);

			Dispatcher disp = aContext.getDispatcher();
			if(disp != null)
			{
				logicalNetLayer.perform_processing = false;
				disp.notify(new OperationEvent(this, 0, "mapchangeevent"));
				logicalNetLayer.perform_processing = true;
			}
//          logicalNetLayer.parent.myMapElementsPanel.updateTable();
          }
          break;
 case 5:  {
//Получаем physicalNodeElement1 и удаляем его с myNodeLink
// и делаем	 physicalNodeElement2 не активным
      MapPhysicalNodeElement physicalNodeElement1 = (MapPhysicalNodeElement)myNodeLink.startNode;
      MapPhysicalNodeElement physicalNodeElement2 = (MapPhysicalNodeElement)myNodeLink.endNode;
                 logicalNetLayer.getMapContext().removeNodeLink(myNodeLink);
                 logicalNetLayer.getMapContext().removeNode(physicalNodeElement1);
                 physicalNodeElement2.setNonactive();

          MapPhysicalLinkElement mapPhysicalLinkElement = logicalNetLayer.getMapContext().getPhysicalLinkbyNodeLink( myNodeLink.getId());

           if ( mapPhysicalLinkElement.startNode == myNodeLink.startNode )
           {
             mapPhysicalLinkElement.startNode = myNodeLink.endNode ;
           }
           else
           {
             mapPhysicalLinkElement.endNode = myNodeLink.endNode ;
           }

           mapPhysicalLinkElement.nodeLink_ids.removeElement( myNodeLink.getId());

           if ( mapPhysicalLinkElement.nodeLink_ids.size() == 0)
           {
            logicalNetLayer.getMapContext().removePhysicalLink( mapPhysicalLinkElement);

			Dispatcher disp = aContext.getDispatcher();
			if(disp != null)
			{
				logicalNetLayer.perform_processing = false;
				disp.notify(new OperationEvent(this, 0, "mapchangeevent"));
				logicalNetLayer.perform_processing = true;
			}
//            logicalNetLayer.parent.myMapElementsPanel.updateTable();
           }

          }
         break;
 case 6:  {
//Получаем physicalNodeElement2 и удаляем его с myNodeLink
// и делаем	 physicalNodeElement1 не активным
      MapPhysicalNodeElement physicalNodeElement1 = (MapPhysicalNodeElement)myNodeLink.startNode;
      MapPhysicalNodeElement physicalNodeElement2 = (MapPhysicalNodeElement)myNodeLink.endNode;
                 logicalNetLayer.getMapContext().removeNodeLink(myNodeLink);
                 logicalNetLayer.getMapContext().removeNode(physicalNodeElement2);
                 physicalNodeElement1.setNonactive();

          MapPhysicalLinkElement mapPhysicalLinkElement = logicalNetLayer.getMapContext().getPhysicalLinkbyNodeLink( myNodeLink.getId());

           if ( mapPhysicalLinkElement.startNode == myNodeLink.endNode )
           {
             mapPhysicalLinkElement.startNode = myNodeLink.startNode ;
           }
           else
           {
             mapPhysicalLinkElement.endNode = myNodeLink.startNode ;
           }
           mapPhysicalLinkElement.nodeLink_ids.removeElement( myNodeLink.getId());

           if ( mapPhysicalLinkElement.nodeLink_ids.size() == 0)
           {
            logicalNetLayer.getMapContext().removePhysicalLink( mapPhysicalLinkElement);

			Dispatcher disp = aContext.getDispatcher();
			if(disp != null)
			{
				logicalNetLayer.perform_processing = false;
				disp.notify(new OperationEvent(this, 0, "mapchangeevent"));
				logicalNetLayer.perform_processing = true;
			}
//            logicalNetLayer.parent.myMapElementsPanel.updateTable();
           }

          }
         break;
case 7:   {
//Получаем physicalNodeElement2 и physicalNodeElement1
// и делаем	их не активным	и удаляем myNodeLink
      MapPhysicalNodeElement physicalNodeElement1 = (MapPhysicalNodeElement)myNodeLink.startNode;
      MapPhysicalNodeElement physicalNodeElement2 = (MapPhysicalNodeElement)myNodeLink.endNode;
                   physicalNodeElement1.setNonactive();
                   physicalNodeElement2.setNonactive();
                   logicalNetLayer.getMapContext().removeNodeLink(myNodeLink);


           MapPhysicalLinkElement mapPhysicalLinkElement = logicalNetLayer.getMapContext().getPhysicalLinkbyNodeLink( myNodeLink.getId());
           mapPhysicalLinkElement.nodeLink_ids.removeElement( myNodeLink.getId());

           Enumeration e = logicalNetLayer.getMapContext().getNodeLinksContainingNode( mapPhysicalLinkElement.startNode).elements();
//Создаём ещё один physicaloLink
           while ( e.hasMoreElements())
           {
            MapNodeLinkElement nodeLink = (MapNodeLinkElement) e.nextElement();

            if ( mapPhysicalLinkElement.nodeLink_ids.contains( nodeLink.getId() ) )
             {
               MapNodeElement nodeElement = logicalNetLayer.getMapContext().getOtherNodeOfNodeLink( nodeLink, mapPhysicalLinkElement.startNode);

               MapPhysicalLinkElement myPhysicalLinkElement = 
					new MapPhysicalLinkElement(  
						dataSource.GetUId( MapPhysicalLinkElement.typ ),
                        nodeLink.startNode, 
						nodeLink.endNode, 
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
//               logicalNetLayer.parent.myMapElementsPanel.updateTable();

               logicalNetLayer.getMapContext().getPhysicalLinks().addElement(myPhysicalLinkElement);
               myPhysicalLinkElement.nodeLink_ids.add( nodeLink.getId());
               mapPhysicalLinkElement.nodeLink_ids.removeElement( nodeLink.getId());

              while ( (nodeElement != myNodeLink.startNode) && (nodeElement != myNodeLink.endNode ) )
               {
                Enumeration ee = logicalNetLayer.getMapContext().getNodeLinksContainingNode( nodeElement).elements();

                while ( ee.hasMoreElements())
                {
                   MapNodeLinkElement myNodeLink = (MapNodeLinkElement) ee.nextElement();

                   if ( myNodeLink != nodeLink )
                   {
                       nodeLink = myNodeLink;

                       myPhysicalLinkElement.addMapNodeLink( myNodeLink);
                       mapPhysicalLinkElement.nodeLink_ids.removeElement( myNodeLink.getId());
                       nodeElement = logicalNetLayer.getMapContext().getOtherNodeOfNodeLink( myNodeLink, nodeElement);

                   }

                }
               }

               mapPhysicalLinkElement.startNode = myNodeLink.endNode;
             }
           }
//////////////////

          }
         break;
case 8:   {
//Удаляем physicalNodeElement1, physicalNodeElement2 и myNodeLink
      MapPhysicalNodeElement physicalNodeElement1 = (MapPhysicalNodeElement)myNodeLink.startNode;
      MapPhysicalNodeElement physicalNodeElement2 = (MapPhysicalNodeElement)myNodeLink.endNode;
                 logicalNetLayer.getMapContext().removeNode(physicalNodeElement1);
                 logicalNetLayer.getMapContext().removeNode(physicalNodeElement2);
                   logicalNetLayer.getMapContext().removeNodeLink(myNodeLink);

           MapPhysicalLinkElement mapPhysicalLinkElement = logicalNetLayer.getMapContext().getPhysicalLinkbyNodeLink( myNodeLink.getId());
           logicalNetLayer.getMapContext().removePhysicalLink( mapPhysicalLinkElement);

			Dispatcher disp = aContext.getDispatcher();
			if(disp != null)
			{
				logicalNetLayer.perform_processing = false;
				disp.notify(new OperationEvent(this, 0, "mapchangeevent"));
				logicalNetLayer.perform_processing = true;
			}
//           logicalNetLayer.parent.myMapElementsPanel.updateTable();

          }
        break;
case 9:   {
//Просто удаляем myNodeLink
           logicalNetLayer.getMapContext().removeNodeLink(myNodeLink);

           MapPhysicalLinkElement mapPhysicalLinkElement = logicalNetLayer.getMapContext().getPhysicalLinkbyNodeLink( myNodeLink.getId());
           logicalNetLayer.getMapContext().removePhysicalLink( mapPhysicalLinkElement);

			Dispatcher disp = aContext.getDispatcher();
			if(disp != null)
			{
				logicalNetLayer.perform_processing = false;
				disp.notify(new OperationEvent(this, 0, "mapchangeevent"));
				logicalNetLayer.perform_processing = true;
			}
//           logicalNetLayer.parent.myMapElementsPanel.updateTable();
          }
        break;
      }

     /* MapElement myMapElement = new VoidMapElement( logicalNetLayer);

      logicalNetLayer.parent.myTable.setModel(myMapElement);
      myMapElement.initColumnSizes( logicalNetLayer.parent.myTable);
*/
  }
}