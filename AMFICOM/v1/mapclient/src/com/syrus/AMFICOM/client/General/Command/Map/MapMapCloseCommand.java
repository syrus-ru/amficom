package com.syrus.AMFICOM.Client.General.Command.Map;

import java.awt.*;
import javax.swing.*;
import java.util.Vector;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Configure.*;
import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.Map.Editor.*;
import com.syrus.AMFICOM.Client.General.Command.*;


//A0A
public class MapMapCloseCommand extends VoidCommand
{
	Object parameter;
	MapMDIMain mapFrame;
	Dispatcher dispatcher;

	public MapMapCloseCommand(MapMDIMain myMapFrame, Dispatcher dispatcher)
	{
		this.mapFrame = myMapFrame;
		this.parameter = parameter;
		this.dispatcher = dispatcher;
	}

	public Object clone()
	{
		return new MapMapCloseCommand(mapFrame, dispatcher);
	}

	public void execute()
	{
		if(mapFrame.mapFrame == null)
			return;

		new MapCloseCommand(mapFrame.mapFrame).execute();
		dispatcher.notify(new OperationEvent(this, 0, "mapcloseevent"));
	}

}