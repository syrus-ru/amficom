package com.syrus.AMFICOM.Client.Configure.Map.Strategy;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import com.ofx.geometry.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.General.Model.*;

import com.syrus.AMFICOM.Client.Configure.Map.*;

public class MoveFixedDistanceStrategy implements MapStrategy {

  LogicalNetLayer logicalNetLayer;
  Point thePoint;
  ApplicationContext aContext;

  public MoveFixedDistanceStrategy(ApplicationContext aContext, MouseEvent me, LogicalNetLayer lnl)
  {
	this.aContext = aContext;
    logicalNetLayer = lnl;
    thePoint = me.getPoint();
  }

  public void doContextChanges()
  {
		//Перемещаем выбранные элементы взависимости от разрешения на перемещение
    MapElement theVictim = logicalNetLayer.getMapContext().getCurrentMapElement();
    if (theVictim instanceof MapNodeElement)
    {
      MapNodeElement myNode = (MapNodeElement)theVictim;
      SxDoublePoint startSxPoint = myNode.getAnchor();
      SxDoublePoint endSxPoint = logicalNetLayer.convertScreenToMap(thePoint);
      double deltaX = endSxPoint.getX() - startSxPoint.getX();
      double deltaY = endSxPoint.getY() - startSxPoint.getY();
      Enumeration e = logicalNetLayer.getMapContext().getNodes().elements();

      while (e.hasMoreElements() )
      {
        MapNodeElement theNode = (MapNodeElement) e.nextElement();
        if (theNode.isSelected() && theNode.isMovable())
        {
          theNode.move(deltaX, deltaY);
        }
		if(theNode instanceof MapMarkElement)
		{
			MapMarkElement mme = (MapMarkElement )theNode;
			mme.moveToFromStart(mme.distance);
		}
      }
    }
  }
}
