package com.syrus.AMFICOM.Client.General.Command.Session;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.ConnectionInterface;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Event.ContextChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

public class CheckConnectionCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private ApplicationContext aContext;

	public CheckConnectionCommand()
	{
	}

	public CheckConnectionCommand(Dispatcher dispatcher, ApplicationContext aContext)
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
		return new CheckConnectionCommand(dispatcher, aContext);
	}

	public void execute()
	{
		ConnectionInterface ci = aContext.getConnectionInterface();
		System.out.println("Checking connection " + ci.toString());
		dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Проверка соединения с сервером..."));
		if(ConnectionInterface.getActiveConnection() != null)
			if(ConnectionInterface.getActiveConnection().isConnected())
				return;
		if(ci.Connect() != null)

		{
			if(dispatcher != null)
			{
				dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Соединение установлено"));
				dispatcher.notify(new ContextChangeEvent(
						ci,
						ContextChangeEvent.CONNECTION_OPENED_EVENT));
			}
		}
		else
		{
			if(dispatcher != null)
			{
				dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Ошибка установления соединения"));
				dispatcher.notify(new ContextChangeEvent(
						ci,
						ContextChangeEvent.CONNECTION_FAILED_EVENT));
			}
		}
	}

}
