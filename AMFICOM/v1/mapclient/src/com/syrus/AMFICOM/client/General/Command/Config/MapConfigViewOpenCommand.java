package com.syrus.AMFICOM.Client.General.Command.Config;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.Map.MapOpenCommand;
import com.syrus.AMFICOM.Client.General.Command.Map.ViewMapElementsCommand;
import com.syrus.AMFICOM.Client.General.Command.Map.ViewMapPropertiesCommand;
import com.syrus.AMFICOM.Client.General.Command.Map.ViewMapSetupCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.UI.MessageBox;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;
import com.syrus.AMFICOM.Client.Resource.Pool;

import javax.swing.JDesktopPane;

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
			MapContext mc = (MapContext )Pool.get(MapContext.typ, com.retobj_id);
			if(mc.isOpened())
			{
				MessageBox mb = new MessageBox(LangModelMap.getString("MapAlreadyOpened"));
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