package com.syrus.AMFICOM.Client.Map.Strategy;

import com.ofx.geometry.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.General.Event.*;

public class TransmissionPathStrategy implements MapStrategy {

  LogicalNetLayer logicalNetLayer;
  ApplicationContext aContext;

  public TransmissionPathStrategy (ApplicationContext aContext, LogicalNetLayer lnl )
  {
	this.aContext = aContext;
    logicalNetLayer = lnl;
  }

//A0A
  public void doContextChanges()
  {
    DataSourceInterface dataSource = logicalNetLayer.mapMainFrame.getContext().getDataSourceInterface();

   MapNodeElement startNode;
   MapNodeElement endNode;
   Vector physicalLinkIDs = new Vector();
   MapPhysicalLinkElement myMapPhysicalLinkElement;

    Enumeration e = logicalNetLayer.getMapContext().getTransmissionPath().elements();
    while ( e.hasMoreElements())
    {
      MapTransmissionPathElement transmissionPath = ( MapTransmissionPathElement)e.nextElement();

      //удаляем пустые ID-шники
      for (int i = 0; i < transmissionPath.physicalLink_ids.size(); i++)
       {
        if ( !logicalNetLayer.getMapContext().getPhysicalLinks().contains( transmissionPath.physicalLink_ids.get(i) ) )
        {
          transmissionPath.physicalLink_ids.removeElement( transmissionPath.physicalLink_ids.get(i) );
        }
       }

      myMapPhysicalLinkElement = logicalNetLayer.getMapContext().getPhysicalLink( (String)transmissionPath.physicalLink_ids.get(0) );
      startNode = myMapPhysicalLinkElement.startNode;
      endNode = myMapPhysicalLinkElement.endNode;
      physicalLinkIDs.add( myMapPhysicalLinkElement.getId());

//A0A
      for (int i = 1; i < transmissionPath.physicalLink_ids.size(); i++)
       {
         MapPhysicalLinkElement tempPhysicalLinkElement = logicalNetLayer.getMapContext().getPhysicalLink( (String)transmissionPath.physicalLink_ids.get(i) );

         if (
             ( startNode == tempPhysicalLinkElement.startNode )
             ||
             ( startNode == tempPhysicalLinkElement.endNode )
             ||
             ( endNode == tempPhysicalLinkElement.startNode )
             ||
             ( endNode == tempPhysicalLinkElement.endNode )
            )
           {

             if ( startNode == tempPhysicalLinkElement.startNode )
              {
                startNode = tempPhysicalLinkElement.endNode;
              }

             if ( startNode == tempPhysicalLinkElement.endNode )
              {
                startNode = tempPhysicalLinkElement.startNode;
              }

             if ( endNode == tempPhysicalLinkElement.startNode )
              {
                endNode = tempPhysicalLinkElement.endNode;
              }

             if ( endNode == tempPhysicalLinkElement.endNode )
              {
                endNode = tempPhysicalLinkElement.startNode;
              }
             physicalLinkIDs.add( tempPhysicalLinkElement.getId());
           }
//A0A
          else
          {
            if ( logicalNetLayer.mapMainFrame.aContext.getApplicationModel()
                         .isEnabled("mapActionCreatePath"))
             {
              MapTransmissionPathElement myMapTransmissionPathElement = 
					new MapTransmissionPathElement( 
						dataSource.GetUId( MapTransmissionPathElement.typ),
                        startNode, 
						endNode, 
						logicalNetLayer.getMapContext());
			  Pool.put(MapTransmissionPathElement.typ, myMapTransmissionPathElement.getId(), myMapTransmissionPathElement);

			Dispatcher disp = aContext.getDispatcher();
			if(disp != null)
			{
				logicalNetLayer.perform_processing = false;
				disp.notify(new OperationEvent(myMapTransmissionPathElement, 0, "mapchangeevent"));
				logicalNetLayer.perform_processing = true;
			}
			if(disp != null)
			{
				logicalNetLayer.perform_processing = false;
				disp.notify(new MapNavigateEvent(myMapTransmissionPathElement, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
				logicalNetLayer.perform_processing = true;
				logicalNetLayer.getMapContext().notifySchemeEvent(myMapTransmissionPathElement);
				logicalNetLayer.getMapContext().notifyCatalogueEvent(myMapTransmissionPathElement);
			}
//              logicalNetLayer.parent.myMapElementsPanel.updateTable();
              myMapTransmissionPathElement.physicalLink_ids = physicalLinkIDs;
              logicalNetLayer.getMapContext().getTransmissionPath().add( myMapTransmissionPathElement);
             }
          }
       }
    }//while ( e.hasMoreElements())

  }

}
