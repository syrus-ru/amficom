/*
 * $Id: SessionChangePasswordCommand.java,v 1.3 2004/08/05 12:07:03 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.Command.Session;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.General.UI.Session.ChangePasswordDialog;
import java.awt.*;

/**
 * @version $Revision: 1.3 $, $Date: 2004/08/05 12:07:03 $
 * @author $Author: bass $
 * @module generalclient_v1
 */
public class SessionChangePasswordCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private SessionInterface si;
	ApplicationContext aContext;

	public SessionChangePasswordCommand()
	{
	}

	public SessionChangePasswordCommand(Dispatcher dispatcher, ApplicationContext aContext)
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
		return new SessionChangePasswordCommand(dispatcher, aContext);
	}

	public void execute()
	{
		ChangePasswordDialog cDialog = new ChangePasswordDialog(aContext);

//		cDialog.si = aContext.getSessionInterface();

		if(dispatcher != null)
			dispatcher.notify(new ContextChangeEvent(
					aContext.getSessionInterface(),
					ContextChangeEvent.PASSWORD_CHANGING_EVENT));
		dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Изменение пароля"));
		cDialog.setModal(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = cDialog.getSize();
		cDialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		cDialog.show();

		if(cDialog.retCode == cDialog.RET_CANCEL)
		{
//			statusBar.setText("status", LangModelMain.String("statusCancelled"));
			dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Операция прервана"));
			return;
		}

		if(cDialog.retCode == cDialog.RET_OK)
		{
			dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Новый пароль установлен"));
			if(dispatcher != null)
				dispatcher.notify(new ContextChangeEvent(
						aContext.getSessionInterface(),
						ContextChangeEvent.PASSWORD_CHANGED_EVENT));
		}
	}

}
