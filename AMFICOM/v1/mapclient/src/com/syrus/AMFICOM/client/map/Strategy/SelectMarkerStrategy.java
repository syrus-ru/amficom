package com.syrus.AMFICOM.Client.Map.Strategy;

import java.awt.*;
import java.util.*;

import com.ofx.geometry.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.General.Model.*;

import com.syrus.AMFICOM.Client.Map.*;

//��������� ����������� ������������� �������

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
		//����� ������ ����������� ��� ������� ���������� � ������������� �������
		Iterator e = logicalNetLayer.getMapContext().getNodes().iterator();
	
		LinkedList nodesToSelect = new LinkedList();
		LinkedList nodeLinkToSelect = new LinkedList();

		//��������� � ������� ������ �� � ������� MapNodeElement
		while (e.hasNext())
		{
			MapNodeElement myNode = (MapNodeElement )e.next();
			SxDoublePoint mySXPoint = myNode.getAnchor();
			Point p = logicalNetLayer.convertMapToScreen(mySXPoint);

			if (selectionRect.contains(p))
			{
				myNode.select();
				nodesToSelect.add(myNode.getId());
			}
		}

		e = logicalNetLayer.getMapContext().getNodeLinks().iterator();

		//��������� � ������� ������ �� � ������� nodeLink
		while (e.hasNext() )
		{
			MapNodeLinkElement myNodeLink = (MapNodeLinkElement )e.next();
			Point p;
			if (
				selectionRect.contains(logicalNetLayer.convertMapToScreen(myNodeLink.startNode.getAnchor())) 
				&& selectionRect.contains(logicalNetLayer.convertMapToScreen(myNodeLink.endNode.getAnchor())))
	       {
				myNodeLink.select();
				nodeLinkToSelect.add(myNodeLink.getId());
			}
		}

		logicalNetLayer.setMode(LogicalNetLayer.NULL_MODE);
	}
}

