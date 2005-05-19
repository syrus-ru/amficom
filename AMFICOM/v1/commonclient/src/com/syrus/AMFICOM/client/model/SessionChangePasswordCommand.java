/*
 * $Id: SessionChangePasswordCommand.java,v 1.1 2005/05/19 14:06:42 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
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
public class SessionChangePasswordCommand extends AbstractCommand {

	private Dispatcher	dispatcher;
	ApplicationContext	aContext;

	public SessionChangePasswordCommand(Dispatcher dispatcher, ApplicationContext aContext) {
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

//	public void execute() {
//		ChangePasswordDialog cDialog = new ChangePasswordDialog(this.aContext);
//
//		// cDialog.si = aContext.getSessionInterface();
//
//		if (this.dispatcher != null)
//			this.dispatcher.notify(new ContextChangeEvent(this.aContext.getSessionInterface(),
//															ContextChangeEvent.PASSWORD_CHANGING_EVENT));
//		this.dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "��������� ������"));
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
//			this.dispatcher.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "�������� ��������"));
//			return;
//		}
//
//		if (returnCode == ChangePasswordDialog.RET_OK) {
//			this.dispatcher
//					.notify(new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, "����� ������ ����������"));
//			if (this.dispatcher != null)
//				this.dispatcher.notify(new ContextChangeEvent(this.aContext.getSessionInterface(),
//																ContextChangeEvent.PASSWORD_CHANGED_EVENT));
//		}
//	}
}
