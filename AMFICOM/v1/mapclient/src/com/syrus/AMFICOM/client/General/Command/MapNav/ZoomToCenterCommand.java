package com.syrus.AMFICOM.Client.General.Command.MapNav;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import java.awt.Cursor;

public class ZoomToCenterCommand extends VoidCommand
{
	LogicalNetLayer logicalNetLayer;
	private ApplicationModel aModel;
	
	public ZoomToCenterCommand(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("logicalNetLayer"))
			logicalNetLayer = (LogicalNetLayer )value;
	}

	public Object clone()
	{
		return new ZoomToCenterCommand(logicalNetLayer);
	}

	public void execute()
	{
		if(aModel.isSelected("mapActionZoomToCenter"))
		{
//			logicalNetLayer.mapToolBarState = 0;
			logicalNetLayer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

			aModel.setSelected("mapActionZoomToPoint", false);
			aModel.fireModelChanged("");
		}
		else
		if(!aModel.isSelected("mapActionZoomToPoint"))
		{
			aModel.setSelected("mapActionZoomToPoint", true);

			aModel.setSelected("mapActionZoomBox", false);
			aModel.setSelected("mapActionHandPan", false);
			aModel.fireModelChanged("");

//			logicalNetLayer.mapToolBarState = logicalNetLayer.zoomToCenter;
			logicalNetLayer.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		}
	}
}
