package com.syrus.AMFICOM.Client.General.Command.MapNav;

import java.awt.Cursor;

import com.syrus.AMFICOM.Client.Configure.Map.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class HandPanCommand extends VoidCommand
{
	LogicalNetLayer logicalNetLayer;
	ApplicationModel aModel;
	
	public HandPanCommand(LogicalNetLayer logicalNetLayer)
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
		return new HandPanCommand(logicalNetLayer);
	}

	public void execute()
	{
		if(aModel.isSelected("mapActionHandPan"))
		{
			logicalNetLayer.setMapState(LogicalNetLayer.NO_ACTION);
			logicalNetLayer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

			aModel.setSelected("mapActionHandPan", false);
			aModel.fireModelChanged("");
		}
		else
		if(!aModel.isSelected("mapActionHandPan"))
		{
			aModel.setSelected("mapActionHandPan", true);

			aModel.setSelected("mapActionMoveToCenter", false);
			aModel.setSelected("mapActionZoomToPoint", false);
			aModel.setSelected("mapActionZoomBox", false);
			aModel.fireModelChanged("");

			logicalNetLayer.setMapState(LogicalNetLayer.MOVE_HAND_BUTTON);
			logicalNetLayer.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
	}
}