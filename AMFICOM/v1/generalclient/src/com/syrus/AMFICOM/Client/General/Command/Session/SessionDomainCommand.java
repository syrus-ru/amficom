package com.syrus.AMFICOM.Client.General.Command.Session;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Event.ContextChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.Session.SessionDomainDialog;

import java.awt.Dimension;
import java.awt.Toolkit;


public class SessionDomainCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private ApplicationContext aContext;

	public SessionDomainCommand()
	{
	}

	public SessionDomainCommand(Dispatcher dispatcher, ApplicationContext aContext)
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
		return new SessionDomainCommand(dispatcher, aContext);
	}

	public void execute()
	{
		if(aContext.getSessionInterface() == null)
		{
			System.out.println("Session null!!! :(");
			dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Нет сессии!"));
			return;
		}

		dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Выбор домена..."));

		SessionDomainDialog sDialog = new SessionDomainDialog(aContext.getSessionInterface().getUserId());

		sDialog.setDomain(aContext.getSessionInterface().getDomainId());

		sDialog.setModal(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = sDialog.getSize();
		sDialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

		sDialog.show();

		if(sDialog.retCode == sDialog.RET_CANCEL)
		{
			dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, LangModel.getString("Aborted")));
			return;
		}

		if(sDialog.retCode == sDialog.RET_OK)
		{
			try
			{
//				RISDSessionInfo rsi = (RISDSessionInfo )aContext.getSessionInterface();
//				rsi.accessIdentity.domain_id = sDialog.domain_id;
				aContext.getSessionInterface().setDomainId(sDialog.domain_id);
			}
			catch(Exception ex)
			{
				dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Ошибка соединения!"));
				System.out.println("DOMAIN not set - not a RISD connection!");
			}
			
			dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Новый домен выбран"));
			dispatcher.notify(new ContextChangeEvent(
					sDialog.domain_id,
					ContextChangeEvent.DOMAIN_SELECTED_EVENT));
		}
	}
}
