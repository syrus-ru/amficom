/*
* Основное окно конфигурирующего приложения.
*/
// Copyright (c) 2002 Syrus
package com.syrus.AMFICOM.Client.Configure.Map;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Map.*;

public class MapJMainFrame extends MapMainFrame
{
	public MapJMainFrame(Vector panelElements, ApplicationContext aContext)
	{
		super(panelElements, aContext);
	}

	public void init_module()
	{
		super.init_module();

		ApplicationModel aModel = aContext.getApplicationModel();
		
		aModel.setCommand("menuMapNew", new MapJNewCommand(this, aContext));
		aModel.setCommand("menuMapClose", new MapJCloseCommand(this));
		aModel.setCommand("menuMapOpen", new MapJOpenCommand((JDesktopPane )this.getParent(), this, aContext));
		aModel.setCommand("menuMapSave", new MapJSaveCommand((JDesktopPane )this.getParent(), this, aContext));
	}

	public void setMapContext( MapContext myMapContext)
	{
		super.setMapContext(myMapContext);
		if(myMapContext != null)
			if(myMapContext instanceof ISMMapContext)
			{
			}
	}
	
}