package com.syrus.AMFICOM.Client.General.Command.Map;

import java.awt.*;
import javax.swing.*;
import com.syrus.AMFICOM.CORBA.Map.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.Configure.Map.Editor.*;
import com.syrus.AMFICOM.Client.General.Command.*;

//A0A
public class MapMapSaveAsCommand extends VoidCommand
{
	MapMDIMain mapFrame;
	ApplicationContext aContext;
	JDesktopPane desktop;

	public MapMapSaveAsCommand()
	{
	}

	public MapMapSaveAsCommand(JDesktopPane desktop, MapMDIMain myMapFrame, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.mapFrame = myMapFrame;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new MapMapSaveAsCommand(desktop, mapFrame, aContext);
	}

	public void execute()
	{
		if(mapFrame.mapFrame == null)
		{
			System.out.println("map frame is null! Cannot create new map.");
			return;
		}
		new MapSaveAsCommand(desktop, mapFrame.mapFrame, aContext).execute();
	}

}