
package com.syrus.AMFICOM.client.model;

import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;

/**
 * TODO
 * 
 * @version $Revision: 1.1 $, $Date: 2005/05/19 14:06:42 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient_v1
 */
public class SessionCloseCommand extends AbstractCommand {

	private Dispatcher			dispatcher;
	private ApplicationContext	aContext;

	public SessionCloseCommand() {
	}

	public SessionCloseCommand(Dispatcher dispatcher, ApplicationContext aContext) {
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

	public void execute() {
		this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, "Закрытие сессии..."));
//		aContext.getSessionInterface().closeSession();
//		dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, "Сессия закрыта"));
//		dispatcher.firePropertyChange(new ContextChangeEvent(this, aContext.getSessionInterface(), ContextChangeEvent.SESSION_CLOSED_EVENT));
	}
}
