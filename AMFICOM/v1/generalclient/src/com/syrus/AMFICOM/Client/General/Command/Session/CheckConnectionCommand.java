/*
 * $Id: CheckConnectionCommand.java,v 1.6 2005/05/18 14:01:20 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.Command.Session;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.ContextChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

/**
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/05/18 14:01:20 $
 * @module generalclient_v1
 */
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
		ConnectionInterface connection = ConnectionInterface.getInstance();
		dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Проверка соединения с сервером..."));
		if (connection.isConnected())
			return;
		try
		{
			connection.setConnected(true);
			if (dispatcher != null)
			{
				dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Соединение установлено"));
				dispatcher.notify(new ContextChangeEvent(connection, ContextChangeEvent.CONNECTION_OPENED_EVENT));
			}
		}
		catch (Exception e)
		{
			/**
			 * @todo Catch different exceptions separately.
			 */
			if(dispatcher != null)
			{
				dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Ошибка установления соединения"));
				dispatcher.notify(new ContextChangeEvent(connection, ContextChangeEvent.CONNECTION_FAILED_EVENT));
			}
		}
	}
}
