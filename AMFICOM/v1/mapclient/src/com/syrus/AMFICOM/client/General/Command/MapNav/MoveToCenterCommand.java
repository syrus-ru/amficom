package com.syrus.AMFICOM.Client.General.Command.MapNav;

import java.awt.Cursor;

import com.syrus.AMFICOM.Client.Configure.Map.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class MoveToCenterCommand extends VoidCommand
{
	LogicalNetLayer logicalNetLayer;
	ApplicationModel aModel;
	
	public MoveToCenterCommand(LogicalNetLayer logicalNetLayer)
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
		return new MoveToCenterCommand(logicalNetLayer);
	}

	public void execute()
	{
		if(aModel.isSelected("mapActionMoveToCenter"))
		{
			logicalNetLayer.setMapState(LogicalNetLayer.NO_ACTION);
			logicalNetLayer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

			aModel.setSelected("mapActionMoveToCenter", false);
			aModel.fireModelChanged("");
		}
		else
		if(!aModel.isSelected("mapActionMoveToCenter"))
		{
			aModel.setSelected("mapActionMoveToCenter", true);

			aModel.setSelected("mapActionZoomToPoint", false);
			aModel.setSelected("mapActionZoomBox", false);
			aModel.setSelected("mapActionHandPan", false);
			aModel.fireModelChanged("");

			logicalNetLayer.setMapState(LogicalNetLayer.MOVE_TO_CENTER);
			logicalNetLayer.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		}
	}
}