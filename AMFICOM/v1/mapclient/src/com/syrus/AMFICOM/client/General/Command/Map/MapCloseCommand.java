package com.syrus.AMFICOM.Client.General.Command.Map;

import java.awt.*;
import javax.swing.*;
import java.util.Vector;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Configure.*;
import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.General.Command.*;


//A0A
public class MapCloseCommand extends VoidCommand
{
	Object parameter;
	MapMainFrame mapFrame;

	public MapCloseCommand()
	{
	}

	public MapCloseCommand(MapMainFrame myMapFrame)
	{
		this.mapFrame = myMapFrame;
		this.parameter = parameter;
	}

	public Object clone()
	{
		return new MapCloseCommand(mapFrame);
	}

	public void execute()
	{
        System.out.println("Closing new map context");
        mapFrame.setMapContext( null);

        MapMainFrame.iniFile.setValue( "last_long", String.valueOf( mapFrame.myMapViewer.getCenter()[0])  );
        MapMainFrame.iniFile.setValue( "last_lat", String.valueOf( mapFrame.myMapViewer.getCenter()[1]) );

        if ( MapMainFrame.iniFile.saveKeys() )
        {
			System.out.println("Params saved");
        }
        mapFrame.setTitle( LangModelMap.String("AppTitle"));

         // mapFrame.setTitle( mapFrame.getTitle() + " - " + mapFrame.mapPanel.myMapViewer.lnlgetMapContext().name);
	}

}