package com.syrus.AMFICOM.Client.General.Command.Model;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

public class MapCloseCommand extends VoidCommand
{
	Dispatcher dispatcher;
	ApplicationContext aContext;

	public MapCloseCommand()
	{
	}

	public MapCloseCommand(Dispatcher dispatcher, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
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
		return new MapCloseCommand(dispatcher, aContext);
	}

	public void execute()
	{
		dispatcher.notify(new OperationEvent(this, 0, "mapcloseevent"));
	}

}