package com.syrus.AMFICOM.Client.Configure.Map.Strategy;

import java.awt.*;
import com.ofx.geometry.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;

public class AddPhysicalNodeElementStrategy implements  MapStrategy{

  LogicalNetLayer logicalNetLayer;
  MapNodeLinkElement curLink;
  Point currentMousePoint;
  ApplicationContext aContext;

  public AddPhysicalNodeElementStrategy(
		ApplicationContext aContext,
		LogicalNetLayer lnl,
        MapNodeLinkElement myNodeLink,
        Point myPoint)
  {
	this.aContext = aContext;
    logicalNetLayer = lnl;
    curLink = myNodeLink;
    currentMousePoint = myPoint;
  }

  public void doContextChanges()
  {
		//В данном классе реализуется алгоритм добавления myPhysicalNodeElement на карту
		//Удаляется curLink, добавляется myPhysicalNodeElement в точку myPoint
		//Создаются два новых NodeLink и добавляются в myPhysicalLinkElement
    DataSourceInterface dataSource = aContext.getDataSourceInterface();

    SxDoublePoint coordinatePoint = logicalNetLayer.convertScreenToMap(currentMousePoint);

    MapNodeElement myPhysicalNodeElement = new MapPhysicalNodeElement( 
		dataSource.GetUId( MapPhysicalNodeElement.typ),
        curLink.PhysicalLinkID,
        coordinatePoint,
        logicalNetLayer.getMapContext(),
        MapNodeElement.defaultBounds);
	Pool.put(MapPhysicalNodeElement.typ, myPhysicalNodeElement.getId(), myPhysicalNodeElement);
	myPhysicalNodeElement.setScaleCoefficient(logicalNetLayer.getMapContext().defaultScale / logicalNetLayer.viewer.getScale());

    logicalNetLayer.getMapContext().getNodes().addElement(myPhysicalNodeElement);
    MapNodeElement myStartNode = curLink.startNode;
    MapNodeElement myEndNode = curLink.endNode;

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
				disp.notify(new MapNavigateEvent(myPhysicalNodeElement, MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
				logicalNetLayer.getMapContext().setCurrent(myPhysicalNodeElement);
				logicalNetLayer.perform_processing = true;
				logicalNetLayer.getMapContext().notifySchemeEvent(myPhysicalNodeElement);
				logicalNetLayer.getMapContext().notifyCatalogueEvent(myPhysicalNodeElement);
			}


    logicalNetLayer.getMapContext().removeNodeLink(curLink);

    MapNodeLinkElement link1 = new MapNodeLinkElement( dataSource.GetUId( MapNodeLinkElement.typ), myStartNode , myPhysicalNodeElement, logicalNetLayer.getMapContext());
	Pool.put(MapNodeLinkElement.typ, link1.getId(), link1);
    MapNodeLinkElement link2 = new MapNodeLinkElement( dataSource.GetUId( MapNodeLinkElement.typ), myPhysicalNodeElement, myEndNode, logicalNetLayer.getMapContext());
	Pool.put(MapNodeLinkElement.typ, link2.getId(), link2);

    logicalNetLayer.getMapContext().getNodeLinks().add(link1);
    logicalNetLayer.getMapContext().getNodeLinks().add(link2);

    MapPhysicalLinkElement myPhysicalLinkElement = logicalNetLayer.getMapContext().getPhysicalLinkbyNodeLink( curLink.getId());
    myPhysicalLinkElement.nodeLink_ids.removeElement( curLink.getId());
    myPhysicalLinkElement.nodeLink_ids.add( link1.getId());
    myPhysicalLinkElement.nodeLink_ids.add( link2.getId());


  }
}