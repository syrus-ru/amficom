package com.syrus.AMFICOM.Client.General.Command.Map;

import com.syrus.AMFICOM.Client.Configure.Map.Editor.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class MapMapNewCommand extends VoidCommand
{
	ApplicationContext aContext;
	MapMDIMain mapFrame;

	public MapMapNewCommand()
	{
	}

	public MapMapNewCommand(MapMDIMain myMapFrame, ApplicationContext aContext)
	{
		this.mapFrame = myMapFrame;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new MapMapNewCommand(mapFrame, aContext);
	}

	public void execute()
	{
		if(mapFrame.mapFrame == null)
			new ViewMapAllCommand(mapFrame.desktopPane, aContext, new MapConfigureApplicationModelFactory()).execute();
		new MapNewCommand(mapFrame.mapFrame, aContext).execute();
	}

}