package com.syrus.AMFICOM.Client.General.Command.Session;

import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Event.ContextChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.Session.SessionOpenDialog;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;

import java.util.Date;

public class SessionOpenCommand extends VoidCommand //implements Command
{
	private Dispatcher dispatcher;
	private ApplicationContext aContext;

	public SessionOpenCommand()
	{
	}

	public SessionOpenCommand(Dispatcher dispatcher, ApplicationContext aContext)
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
		return new SessionOpenCommand(dispatcher, aContext);
	}

	public void execute()
	{
		if(aContext.getConnectionInterface() == null)
		{
			System.out.println("Connection null!!! :(");
			dispatcher.notify(new ContextChangeEvent(
					aContext.getConnectionInterface(),
					ContextChangeEvent.CONNECTION_FAILED_EVENT));
			return;
		}
		if(aContext.getConnectionInterface().isConnected() == false)
		{
			new CheckConnectionCommand(dispatcher, aContext).execute();
//			aContext.getConnectionInterface().Connect();
		}
		if(aContext.getConnectionInterface().isConnected() == false)
		{
			System.out.println("Not connected!!! :(");
			dispatcher.notify(new ContextChangeEvent(
					aContext.getConnectionInterface(),
					ContextChangeEvent.CONNECTION_FAILED_EVENT));
			return;
		}
/*
// for development purpose only!
		SessionInterface si = aContext.getSessionInterface();
		si.setUser("sys");
		si.setPassword("sys");
		if(si.OpenSession() == null)
			return;
		DataSourceInterface dsi = aContext.getApplicationModel().getDataSource(aContext.getSessionInterface());
		dsi.LoadUserDescriptors();
		dsi.LoadExecs();
		si.setDomainId("sysdomain");
		dispatcher.notify(new ContextChangeEvent(si, ContextChangeEvent.SESSION_OPENED_EVENT));
//		dispatcher.notify(new ContextChangeEvent("sysdomain", ContextChangeEvent.DOMAIN_SELECTED_EVENT));

		if(true)
			return;
*/
		SessionOpenDialog sDialog = new SessionOpenDialog();

		dispatcher.notify(new StatusMessageEvent("Открытие сессии..."));
		dispatcher.notify(new ContextChangeEvent(
				aContext.getSessionInterface(),
				ContextChangeEvent.SESSION_CHANGING_EVENT));

		sDialog.ci = aContext.getConnectionInterface();
		sDialog.si = aContext.getSessionInterface();

		sDialog.setModal(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = sDialog.getSize();
		sDialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

		sDialog.show();

		if(sDialog.retCode == sDialog.RET_CANCEL)
		{
			dispatcher.notify(new StatusMessageEvent("Операция отменена"));
			return;
		}

		if(sDialog.retCode == sDialog.RET_OK)
		{
			DataSourceInterface dataSource = aContext.getApplicationModel().getDataSource(aContext.getSessionInterface());

			dispatcher.notify(new StatusMessageEvent("Инициализация начальных данных"));
			dataSource.LoadUserDescriptors();
			dataSource.LoadExecs();

			dispatcher.notify(new StatusMessageEvent("Сессия открыта"));
			dispatcher.notify(new ContextChangeEvent(
					sDialog.si,
					ContextChangeEvent.SESSION_OPENED_EVENT));
		}
	}
}
