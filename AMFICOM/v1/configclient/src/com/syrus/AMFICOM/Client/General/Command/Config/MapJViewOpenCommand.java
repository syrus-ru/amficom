package com.syrus.AMFICOM.Client.General.Command.Config;

import java.awt.*;
import javax.swing.*;
import java.util.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
//import com.syrus.AMFICOM.Client.Test.*;
import com.syrus.AMFICOM.Client.Configure.*;
import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Map.*;

public class MapJViewOpenCommand extends VoidCommand
{
	Dispatcher dispatcher;
	ApplicationContext aContext;
	JDesktopPane desktop;
	ApplicationModelFactory factory;

	public MapJViewOpenCommand()
	{
	}

	public MapJViewOpenCommand(Dispatcher dispatcher, JDesktopPane desktop, ApplicationContext aContext, ApplicationModelFactory factory)
	{
		this.dispatcher = dispatcher;
		this.desktop = desktop;
		this.aContext = aContext;
		this.factory = factory;
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals("dispatcher"))
			setDispatcher((Dispatcher)value);
		else
		if(field.equals("aContext"))
			setApplicationContext((ApplicationContext )value);
	}

	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public void setApplicationContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new MapJViewOpenCommand(dispatcher, desktop, aContext, factory);
	}

	public void execute()
	{
		ApplicationContext aC = new ApplicationContext();
		aC.setApplicationModel(factory.create());
		aC.setConnectionInterface(aContext.getConnectionInterface());
		aC.setSessionInterface(aContext.getSessionInterface());
		aC.setDataSourceInterface(aC.getApplicationModel().getDataSource(aContext.getSessionInterface()));
		aC.setDispatcher(dispatcher);

//		MapJOpenCommand com = new MapJOpenCommand(desktop, null, aContext);
		MapJOpenCommand com = new MapJOpenCommand(desktop, null, aC);
		com.execute();
		if(com.retCode == 1)
		{
			NewJMapViewCommand com2 = new NewJMapViewCommand(dispatcher, desktop, aContext, factory);
			com2.execute();
			com2.frame.setMapContext((MapContext )Pool.get("ismmapcontext", com.retobj_id));

			dispatcher.notify(new OperationEvent(this, 0, "mapjframeopenevent"));
		}
	}

}