package com.syrus.AMFICOM.Client.Map.Strategy;

import java.awt.*;
import java.awt.event.*;

import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class ChangeEndPointStrategy implements  MapStrategy {

  LogicalNetLayer logicalNetLayer;
  Point myPoint;
  ApplicationContext aContext;

  public ChangeEndPointStrategy(
		ApplicationContext aContext, 
		MouseEvent me, 
		LogicalNetLayer lnl)
  {
	this.aContext = aContext;
    logicalNetLayer = lnl;
    myPoint = me.getPoint();
  }

  public void doContextChanges()
  {
//������ ����� ���������� ����� ���� �������� EndPoint � logicalNetLayer.
    logicalNetLayer.setEndPoint(myPoint);
  }
}

