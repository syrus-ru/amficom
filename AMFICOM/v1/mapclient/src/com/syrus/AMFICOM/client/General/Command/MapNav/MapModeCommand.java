package com.syrus.AMFICOM.Client.General.Command.MapNav;

import java.awt.Cursor;

import com.syrus.AMFICOM.Client.Map.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class MapModeCommand extends VoidCommand
{
	LogicalNetLayer logicalNetLayer;
	ApplicationModel aModel;

	String modeString;
	int mode;
	
	public MapModeCommand(LogicalNetLayer logicalNetLayer, String modeString, int mode)
	{
		this.logicalNetLayer = logicalNetLayer;
		this.modeString = modeString;
		this.mode = mode;
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
		return new MapModeCommand(logicalNetLayer, modeString, mode);
	}

	public void execute()
	{
		if ( aModel.isEnabled(modeString))
			if(!aModel.isSelected(modeString))
		{
			aModel.setSelected("mapModeNodeLink", false);
			aModel.setSelected("mapModeLink", false);
			aModel.setSelected("mapModePath", false);

			aModel.setSelected(modeString, true);

			aModel.fireModelChanged("");

	        logicalNetLayer.getMapContext().linkState = mode;
		    logicalNetLayer.postDirtyEvent();
			logicalNetLayer.postPaintEvent();
		}
	}
}