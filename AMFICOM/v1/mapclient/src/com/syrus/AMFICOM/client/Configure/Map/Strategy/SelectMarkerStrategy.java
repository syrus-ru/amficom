package com.syrus.AMFICOM.Client.Configure.Map.Strategy;

import java.awt.*;
import java.util.*;

import com.ofx.geometry.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.General.Model.*;

import com.syrus.AMFICOM.Client.Configure.Map.*;

//Выделение содержимого прямоугольной области

public class SelectMarkerStrategy implements MapStrategy{

  LogicalNetLayer logicalNetLayer;
  Rectangle selectionRect;
  ApplicationContext aContext;


  public SelectMarkerStrategy(ApplicationContext aContext, LogicalNetLayer lnl, Rectangle rect)
  {
	this.aContext = aContext;
    logicalNetLayer = lnl;
    selectionRect = rect;
  }

  public void doContextChanges()
  {
//Здесь просто проверяется что элемент содержится в прямоугольной области
    Enumeration e = logicalNetLayer.getMapContext().getNodes().elements();

    Vector nodesToSelect = new Vector();
    Vector NodeLinkToSelect = new Vector();

//Пробегаем и смотрим вхотит ли в область MapNodeElement
    while (e.hasMoreElements())
    {
      MapNodeElement myNode = (MapNodeElement) e.nextElement();
      SxDoublePoint mySXPoint = myNode.getAnchor();
      Point p = logicalNetLayer.convertMapToScreen(mySXPoint);

     if (selectionRect.contains(p))
      {
        myNode.select();
        nodesToSelect.addElement(myNode.getId());
      }
    }

    e = logicalNetLayer.getMapContext().getNodeLinks().elements();

//Пробегаем и смотрим вхотит ли в область nodeLink
    while (e.hasMoreElements() )
    {
      MapNodeLinkElement myNodeLink = (MapNodeLinkElement) e.nextElement();
      Point p ;
      if (
           selectionRect.contains( logicalNetLayer.convertMapToScreen ( myNodeLink.startNode.getAnchor()  ) ) &&
           selectionRect.contains( logicalNetLayer.convertMapToScreen ( myNodeLink.endNode.getAnchor()  ) )
         )
       {
        myNodeLink.select();
        NodeLinkToSelect.addElement(myNodeLink.getId());
       }
    }

    logicalNetLayer.setMode(LogicalNetLayer.NULL_MODE);
  }
}

