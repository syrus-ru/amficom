package com.syrus.AMFICOM.Client.General.Command.Map;

import java.awt.*;
import javax.swing.*;
import com.syrus.AMFICOM.CORBA.Map.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.Map.Editor.*;
import com.syrus.AMFICOM.Client.General.Command.*;

//A0A
public class MapMapSaveCommand extends VoidCommand
{
	MapMDIMain mapFrame;
	ApplicationContext aContext;
	JDesktopPane desktop;

	public MapMapSaveCommand()
	{
	}

	public MapMapSaveCommand(JDesktopPane desktop, MapMDIMain myMapFrame, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.mapFrame = myMapFrame;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new MapMapSaveCommand(desktop, mapFrame, aContext);
	}

	public void execute()
	{
		if(mapFrame.mapFrame == null)
		{
			System.out.println("map frame is null! Cannot create new map.");
			return;
		}
		new MapSaveCommand(desktop, mapFrame.mapFrame, aContext).execute();
	}

}