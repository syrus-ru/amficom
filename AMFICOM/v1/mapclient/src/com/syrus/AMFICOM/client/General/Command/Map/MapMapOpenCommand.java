package com.syrus.AMFICOM.Client.General.Command.Map;

import javax.swing.*;

import com.syrus.AMFICOM.Client.Configure.Map.Editor.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Config.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.Configure.Map.UI.*;
import com.syrus.AMFICOM.Client.Configure.Map.UI.Display.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;

public class MapMapOpenCommand extends VoidCommand
{
	MapMDIMain mapFrame;
	ApplicationContext aContext;
	JDesktopPane desktop;

	public MapMapOpenCommand()
	{
	}

	public MapMapOpenCommand(JDesktopPane desktop, MapMDIMain myMapFrame, ApplicationContext aContext)
	{
		this.desktop = desktop;
		this.mapFrame = myMapFrame;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new MapMapOpenCommand(desktop, mapFrame, aContext);
	}

	public void execute()
	{
		ApplicationModelFactory factory = new MapConfigureApplicationModelFactory();
		ApplicationContext aC = new ApplicationContext();
		aC.setApplicationModel(factory.create());
		aC.setConnectionInterface(aContext.getConnectionInterface());
		aC.setSessionInterface(aContext.getSessionInterface());
		aC.setDataSourceInterface(aC.getApplicationModel().getDataSource(aContext.getSessionInterface()));
		aC.setDispatcher(aContext.getDispatcher());

//		MapOpenCommand moc = new MapOpenCommand(desktop, mapFrame.mapFrame, aContext);
//		MapOpenCommand moc = new MapOpenCommand(desktop, mapFrame.mapFrame, aC);
		MapOpenCommand moc = new MapOpenCommand(desktop, null, aC);
		moc.can_delete = true;
		moc.execute();
		if (moc.retCode == 1)
		{
			NewMapViewCommand com2 = new NewMapViewCommand(aContext.getDispatcher(), desktop, aContext, factory);
			com2.execute();
			if(com2.frame == null)
				return;

			new ViewMapPropertiesCommand(desktop, aContext).execute();
			new ViewMapElementsCommand(desktop, aContext).execute();
			new ViewMapSchemeNavigatorCommand(desktop, aContext).execute();

			com2.frame.setMapContext((MapContext )Pool.get("mapcontext", moc.retobj_id));
//			new ViewMapAllCommand(mapFrame.desktopPane, aContext, factory).execute();
//			mapFrame.mapFrame.setMapContext((MapContext)Pool.get("mapcontext", moc.retobj_id));
		}
	}

}
