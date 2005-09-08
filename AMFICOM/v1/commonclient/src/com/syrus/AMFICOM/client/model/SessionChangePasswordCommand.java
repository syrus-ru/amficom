/*
 * $Id: SessionChangePasswordCommand.java,v 1.3 2005/09/08 14:25:57 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.client.model;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.util.Shitlet;

/**
 * TODO
 * 
 * @author $Author: bob $
 * @version $Revision: 1.3 $, $Date: 2005/09/08 14:25:57 $
 * @module commonclient
 */
@Shitlet
public class SessionChangePasswordCommand extends AbstractCommand {

	@SuppressWarnings("unused")
	private Dispatcher	dispatcher;
	ApplicationContext	aContext;

	public SessionChangePasswordCommand(Dispatcher dispatcher, ApplicationContext aContext) {
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	@Override
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

//	public void execute() {
//		ChangePasswordDialog cDialog = new ChangePasswordDialog(this.aContext);
//
//		// cDialog.si = aContext.getSessionInterface();
//
//		if (this.dispatcher != null)
//			this.dispatcher.notify(new ContextChangeEvent(this.aContext.getSessionInterface(),
//															ContextChangeEvent.PASSWORD_CHANGING_EVENT));
//		this.dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Изменение пароля"));
//		cDialog.setModal(true);
//
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		Dimension frameSize = cDialog.getSize();
//		cDialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
//		cDialog.show();
//
//		int returnCode = cDialog.getReturnCode();
//		if (returnCode == ChangePasswordDialog.RET_CANCEL) {
//			// statusBar.setText("status",
//			// LangModelMain.String("statusCancelled"));
//			this.dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Операция прервана"));
//			return;
//		}
//
//		if (returnCode == ChangePasswordDialog.RET_OK) {
//			this.dispatcher
//					.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "Новый пароль установлен"));
//			if (this.dispatcher != null)
//				this.dispatcher.notify(new ContextChangeEvent(this.aContext.getSessionInterface(),
//																ContextChangeEvent.PASSWORD_CHANGED_EVENT));
//		}
//	}
}
