package com.syrus.AMFICOM.Client.General.Command.MapNav;

import java.awt.Cursor;

import com.syrus.AMFICOM.Client.Configure.Map.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class ZoomInCommand extends VoidCommand
{
	LogicalNetLayer logicalNetLayer;
	ApplicationModel aModel;
	
	public ZoomInCommand(LogicalNetLayer logicalNetLayer)
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
		return new ZoomInCommand(logicalNetLayer);
	}

	public void execute()
	{
	    logicalNetLayer.getMapContext().zoomIn();
	}
}