package com.syrus.AMFICOM.Client.General.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.Map.MapMainFrame;
import com.syrus.AMFICOM.Client.Map.MapOptionsDialog;

import java.util.Vector;


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