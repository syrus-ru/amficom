/*
 * $Id: SessionConnectionCommand.java,v 1.4 2004/09/27 12:17:50 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.Command.Session;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.Session.ConnectionDialog;
import java.awt.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2004/09/27 12:17:50 $
 * @module generalclient_v1
 */
public class SessionConnectionCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private ApplicationContext aContext;

	public SessionConnectionCommand()
	{
	}

	public SessionConnectionCommand(Dispatcher dispatcher, ApplicationContext aContext)
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
		return new SessionConnectionCommand(dispatcher, aContext);
	}

	public void execute()
	{
		ConnectionInterface connection = ConnectionInterface.getInstance();
		if (dispatcher != null)
			dispatcher.notify(new ContextChangeEvent(connection, ContextChangeEvent.CONNECTION_CHANGING_EVENT));
		ConnectionDialog cDialog = new ConnectionDialog(aContext);
		cDialog.setModal(true);

		SessionInterface session = SessionInterface.getActiveSession(); 
		if ((session != null)
				&& session.getConnectionInterface().equals(connection)
				&& session.isOpened())
		{
			cDialog.buttonOk.setEnabled(false);
			cDialog.buttonCheck.setEnabled(false);
		}

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = cDialog.getSize();
		cDialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		cDialog.show();

		if ((cDialog.retCode == ConnectionDialog.RET_OK) && (dispatcher != null))
			dispatcher.notify(new ContextChangeEvent(connection, ContextChangeEvent.CONNECTION_CLOSED_EVENT));
			/**
			 * @todo Here, update the connection instance with
			 *       user-specified parameters (server name and,
			 *       probably, ORBInitialHost:ORBInitialPort)
			 */
	}
}
