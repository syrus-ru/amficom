package com.syrus.AMFICOM.Client.General.Command.MapNav;

import java.util.*;
import java.awt.Cursor;


import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;

import com.ofx.geometry.SxDoublePoint;

public class CenterSelectionCommand extends VoidCommand
{
	LogicalNetLayer logicalNetLayer;
	ApplicationModel aModel;
	
	public CenterSelectionCommand(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("logicalNetLayer"))
			logicalNetLayer = (LogicalNetLayer )value;
		if(field.equals("applicationModel"))
			aModel = (ApplicationModel )value;
	}

	public Object clone()
	{
		return new CenterSelectionCommand(logicalNetLayer);
	}

	public void execute()
	{
		if(logicalNetLayer == null)
			return;

		MapElement me;
/*
		me = logicalNetLayer.getMapContext().getCurrentMapElement();
		if(me == null)
			return;
*/		
		Vector vec = new Vector();
		SxDoublePoint pts[];

		for(Enumeration enum = logicalNetLayer.getMapContext().getNodes().elements();
			enum.hasMoreElements();)
		{
			me = (MapElement )enum.nextElement();
			if(me.isSelected())
				vec.add(me.getAnchor());
		}

		for(Enumeration enum = logicalNetLayer.getMapContext().markers.elements();
			enum.hasMoreElements();)
		{
			me = (MapElement )enum.nextElement();
			if(me.isSelected())
				vec.add(me.getAnchor());
		}

		for(Enumeration enum = logicalNetLayer.getMapContext().getNodeLinks().elements();
			enum.hasMoreElements();)
		{
			me = (MapElement )enum.nextElement();
			if(me.isSelected())
				vec.add(me.getAnchor());
		}

		for(Enumeration enum = logicalNetLayer.getMapContext().getPhysicalLinks().elements();
			enum.hasMoreElements();)
		{
			me = (MapElement )enum.nextElement();
			if(me.isSelected())
				vec.add(me.getAnchor());
		}

		for(Enumeration enum = logicalNetLayer.getMapContext().getTransmissionPath().elements();
			enum.hasMoreElements();)
		{
			me = (MapElement )enum.nextElement();
			if(me.isSelected())
				vec.add(me.getAnchor());
		}

		pts = new SxDoublePoint[vec.size()];
		vec.copyInto(pts);
		SxDoublePoint point = new SxDoublePoint(0.0, 0.0);
		for(int i = 0; i < pts.length; i++)
		{
			point.x += pts[i].x;
			point.y += pts[i].y;
		}
		point.x /= pts.length;
		point.y /= pts.length;
		
      logicalNetLayer.getMapViewer().setCenter(point.x, point.y);

      logicalNetLayer.parent.myMapScrollPane.upDateScroll();
      logicalNetLayer.postDirtyEvent();
      logicalNetLayer.postPaintEvent();
      return;

	}
}