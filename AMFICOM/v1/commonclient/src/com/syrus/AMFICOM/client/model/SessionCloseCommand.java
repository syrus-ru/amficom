
package com.syrus.AMFICOM.client.model;

import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClientSessionEnvironment;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.6 $, $Date: 2005/10/21 08:48:21 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class SessionCloseCommand extends AbstractCommand {

	private Dispatcher			dispatcher;

	public SessionCloseCommand(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Override
	public void setParameter(String field, Object value) {
		if (field.equals("dispatcher"))
			setDispatcher((Dispatcher) value);
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Override
	public void execute() {
		this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, "Закрытие сессии..."));
		try {
			final ClientSessionEnvironment clientSessionEnvironment = ClientSessionEnvironment.getInstance();

			clientSessionEnvironment.logout();
			this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, "Сессия закрыта"));
//			this.dispatcher.firePropertyChange(new ContextChangeEvent(this, ContextChangeEvent.SESSION_CLOSED_EVENT));
			this.dispatcher.firePropertyChange(new ContextChangeEvent(this, ContextChangeEvent.LOGGED_OUT_EVENT));
		} catch (ApplicationException e) {
			Log.errorException(e);
			this.dispatcher.firePropertyChange(new StatusMessageEvent(this,
					StatusMessageEvent.STATUS_MESSAGE,
					"Ошибка при закрытии сессии..."));
		}
	}
}
