/*
 * $Id: SessionChangePasswordCommand.java,v 1.4 2004/08/06 06:42:46 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.Command.Session;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.Session.ChangePasswordDialog;
import java.awt.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2004/08/06 06:42:46 $
 * @module generalclient_v1
 */
public class SessionChangePasswordCommand extends VoidCommand
{
	private Dispatcher dispatcher;
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

	/**
	 * @return a clone of this command.
	 * @deprecated do not use this method -- the class doesn't realize
	 *             {@link Cloneable}, and {@link Cloneable#clone()} is
	 *             implemented improperly.
	 */
	public Object clone()
	{
		return new SessionChangePasswordCommand(this.dispatcher, this.aContext);
	}

	public void execute()
	{
		ChangePasswordDialog cDialog = new ChangePasswordDialog(this.aContext);

//		cDialog.si = aContext.getSessionInterface();

		if(this.dispatcher != null)
			this.dispatcher.notify(new ContextChangeEvent(
					this.aContext.getSessionInterface(),
					ContextChangeEvent.PASSWORD_CHANGING_EVENT));
		this.dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Изменение пароля"));
		cDialog.setModal(true);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = cDialog.getSize();
		cDialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		cDialog.show();

		int returnCode = cDialog.getReturnCode();
		if (returnCode == ChangePasswordDialog.RET_CANCEL)
		{
//			statusBar.setText("status", LangModelMain.String("statusCancelled"));
			this.dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Операция прервана"));
			return;
		}

		if (returnCode == ChangePasswordDialog.RET_OK)
		{
			this.dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Новый пароль установлен"));
			if(this.dispatcher != null)
				this.dispatcher.notify(new ContextChangeEvent(
						this.aContext.getSessionInterface(),
						ContextChangeEvent.PASSWORD_CHANGED_EVENT));
		}
	}
}
