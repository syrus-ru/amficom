package com.syrus.AMFICOM.Client.General.Command.MapNav;

import java.awt.Cursor;

import com.syrus.AMFICOM.Client.Configure.Map.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class ZoomBoxCommand extends VoidCommand
{
	LogicalNetLayer logicalNetLayer;
	ApplicationModel aModel;
	
	public ZoomBoxCommand(LogicalNetLayer logicalNetLayer)
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
		return new ZoomBoxCommand(logicalNetLayer);
	}

	public void execute()
	{
		if(aModel.isSelected("mapActionZoomBox"))
		{
			logicalNetLayer.setMapState(LogicalNetLayer.NO_ACTION);
			logicalNetLayer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

			aModel.setSelected("mapActionZoomBox", false);
			aModel.fireModelChanged("");
		}
		else
		if(!aModel.isSelected("mapActionZoomBox"))
		{
			aModel.setSelected("mapActionZoomBox", true);

			aModel.setSelected("mapActionMoveToCenter", false);
			aModel.setSelected("mapActionZoomToPoint", false);
			aModel.setSelected("mapActionHandPan", false);
			aModel.fireModelChanged("");

		   logicalNetLayer.setMapState(LogicalNetLayer.ZOOM_TO_RECT);
		   logicalNetLayer.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		}
	}
}