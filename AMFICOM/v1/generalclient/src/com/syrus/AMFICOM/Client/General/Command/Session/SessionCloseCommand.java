package com.syrus.AMFICOM.Client.General.Command.Session;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Event.ContextChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

public class SessionCloseCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private ApplicationContext aContext;

	public SessionCloseCommand()
	{
	}

	public SessionCloseCommand(Dispatcher dispatcher, ApplicationContext aContext)
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
		return new SessionCloseCommand(dispatcher, aContext);
	}

	public void execute()
	{
		dispatcher.notify(new StatusMessageEvent("Закрытие сессии..."));
		aContext.getSessionInterface().CloseSession();
		dispatcher.notify(new StatusMessageEvent("Сессия закрыта"));
		dispatcher.notify(new ContextChangeEvent(
				aContext.getSessionInterface(),
				ContextChangeEvent.SESSION_CLOSED_EVENT));
	}
}

