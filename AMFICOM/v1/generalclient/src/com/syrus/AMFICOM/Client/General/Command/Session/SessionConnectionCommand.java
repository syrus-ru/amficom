
// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.General.Command.Session;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.ConnectionInterface;
import com.syrus.AMFICOM.Client.General.Event.ContextChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.General.UI.Session.ConnectionDialog;

import java.awt.Dimension;
import java.awt.Toolkit;

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
		ConnectionInterface ci = aContext.getConnectionInterface();
		if(ci == null)
			return;
			
		if(dispatcher != null)
			dispatcher.notify(new ContextChangeEvent(
					ci,
					ContextChangeEvent.CONNECTION_CHANGING_EVENT));

		ConnectionDialog cDialog = new ConnectionDialog(aContext);
/*
		cDialog.fieldIP.setText(ci.getServiceURL());
		cDialog.fieldTCP.setEnabled(false);
		cDialog.fieldSID.setEnabled(false);
		cDialog.fieldObject.setText(ci.getObjectName());
		cDialog.fieldUser.setText(ci.getUser());
		cDialog.fieldPassword.setText(ci.getPassword());
*/
		cDialog.setModal(true);

		if(SessionInterface.getActiveSession() != null)
			if(SessionInterface.getActiveSession().getConnectionInterface().equals(ci))
				if(aContext.getSessionInterface().isOpened())
				{
					cDialog.buttonOk.setEnabled(false);
					cDialog.buttonCheck.setEnabled(false);
				}

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = cDialog.getSize();
		cDialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

		cDialog.show();

		if(cDialog.retCode == ConnectionDialog.RET_CANCEL)
		{
//			statusBar.setText("status", LangModelMain.String("statusCancelled"));
			return;
		}

		if(cDialog.retCode == ConnectionDialog.RET_OK)
		{

//			if(aContext.getConnectionInterface().isConnected())
			{
//				statusBar.setText("status", LangModelMain.String("statusDisconnecting"));
//				ci.Disconnect();

				if(dispatcher != null)
					dispatcher.notify(new ContextChangeEvent(
							ci,
							ContextChangeEvent.CONNECTION_CLOSED_EVENT));
			}

//			ci.setServiceURL(cDialog.fieldIP.getText());
			ci.setServerIP(cDialog.fieldIP.getText());
			ci.setTCPport(cDialog.fieldTCP.getText());
			ci.setSID(cDialog.fieldSID.getText());
			ci.setObjectName(cDialog.fieldObject.getText());
			ci.setUser(cDialog.fieldUser.getText());
			ci.setPassword(cDialog.fieldPassword.getText());

//			new CheckConnectionCommand(dispatcher, aContext).execute();
		}
	}

}
