package com.syrus.AMFICOM.Client.General.Command.Config;

import java.awt.*;
import javax.swing.*;
import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
//import com.syrus.AMFICOM.Client.Test.*;
import com.syrus.AMFICOM.Client.Configure.*;
import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Map.*;

public class MapConfigViewOpenCommand extends MapViewOpenCommand
{
	public MapConfigViewOpenCommand()
	{
	}

	public MapConfigViewOpenCommand(Dispatcher dispatcher, JDesktopPane desktop, ApplicationContext aContext, ApplicationModelFactory factory)
	{
		super(dispatcher, desktop, aContext, factory);
	}
	public Object clone()
	{
		return new MapConfigViewOpenCommand(dispatcher, desktop, aContext, factory);
	}

	public void execute()
	{
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.topologyViewing))
		{
			return;
		}

		ApplicationContext aC = new ApplicationContext();
		aC.setApplicationModel(factory.create());
		aC.setConnectionInterface(aContext.getConnectionInterface());
		aC.setSessionInterface(aContext.getSessionInterface());
		aC.setDataSourceInterface(aC.getApplicationModel().getDataSource(aContext.getSessionInterface()));
		aC.setDispatcher(dispatcher);

//		MapOpenCommand com = new MapOpenCommand(desktop, null, aContext);
		MapOpenCommand com = new MapOpenCommand(desktop, null, aC);
		com.execute();
		if(com.retCode == 1)
		{
			MapContext mc = (MapContext )Pool.get("mapcontext", com.retobj_id);
			if(mc.isOpened())
			{
				MessageBox mb = new MessageBox("Топологическая схема уже открыта в другом окне!");
				return;
			}
		
			NewMapViewCommand com2 = new NewMapViewCommand(dispatcher, desktop, aContext, factory);
			com2.execute();
			if(com2.frame == null)
				return;

			new ViewMapPropertiesCommand(desktop, aContext).execute();
			new ViewMapElementsCommand(desktop, aContext).execute();
			new ViewMapSetupCommand(desktop, aContext).execute();
//			new ViewMapNavigatorCommand(desktop, aContext, null).execute();

			com2.frame.setMapContext((MapContext )Pool.get("mapcontext", com.retobj_id));
			opened = true;
			mc_id = com.retobj_id;
		}
	}

}