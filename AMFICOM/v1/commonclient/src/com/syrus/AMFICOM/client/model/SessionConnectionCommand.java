/*
 * $Id: SessionConnectionCommand.java,v 1.1 2005/05/19 14:06:42 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client.model;

import com.syrus.AMFICOM.client.event.Dispatcher;

/**
 * TODO
 * 
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2005/05/19 14:06:42 $
 * @module generalclient_v1
 */
public class SessionConnectionCommand extends AbstractCommand {

	private Dispatcher			dispatcher;
	private ApplicationContext	aContext;

	public SessionConnectionCommand(Dispatcher dispatcher, ApplicationContext aContext) {
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	public void setParameter(	String field,
								Object value) {
		if (field.equals("dispatcher"))
			setDispatcher((Dispatcher) value);
		else if (field.equals("aContext"))
			setApplicationContext((ApplicationContext) value);
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void setApplicationContext(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public Object clone() {
		return new SessionConnectionCommand(dispatcher, aContext);
	}

	public void execute() {
//		ConnectionInterface connection = ConnectionInterface.getInstance();
//		if (dispatcher != null)
//			dispatcher.notify(new ContextChangeEvent(connection, ContextChangeEvent.CONNECTION_CHANGING_EVENT));
//		ConnectionDialog cDialog = new ConnectionDialog(aContext);
//		cDialog.setModal(true);
//
//		SessionInterface session = SessionInterface.getActiveSession();
//		if ((session != null) && session.getConnectionInterface().equals(connection) && session.isOpened()) {
//			cDialog.buttonOk.setEnabled(false);
//			cDialog.buttonCheck.setEnabled(false);
//		}
//
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		Dimension frameSize = cDialog.getSize();
//		cDialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
//		cDialog.show();
//
//		if ((cDialog.retCode == ConnectionDialog.RET_OK) && (dispatcher != null))
//			dispatcher.notify(new ContextChangeEvent(connection, ContextChangeEvent.CONNECTION_CLOSED_EVENT));
//		/**
//		 * @todo Here, update the connection instance with user-specified
//		 *       parameters (server name and, probably,
//		 *       ORBInitialHost:ORBInitialPort)
//		 */
	}
}
