package com.syrus.AMFICOM.Client.General.Command.Map;

import java.awt.*;
import javax.swing.*;
import java.util.Vector;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Configure.*;
import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.General.Command.*;


//A0A
public class MapSelectMapViewerCommand extends VoidCommand
{
	Object parameter;
	MapMainFrame mapFrame;

	public MapSelectMapViewerCommand()
	{
	}

	public MapSelectMapViewerCommand(MapMainFrame myMapFrame)
	{
		this.mapFrame = myMapFrame;
		this.parameter = parameter;
	}

	public Object clone()
	{
		return new MapSelectMapViewerCommand(mapFrame);
	}

	public void execute()
	{
		MapOptionsDialog mod = new MapOptionsDialog();
		mod.setModal(true);
		mod.show();
		if(mod.ret_code == MapOptionsDialog.OK_CODE)
		{
			System.out.println("select map " + mod.path + " / " + mod.map);
		}
	}

}