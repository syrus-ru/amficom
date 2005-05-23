
package com.syrus.AMFICOM.client.model;

import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.general.ClientSessionEnvironment;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/23 08:19:09 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient_v1
 */
public class SessionCloseCommand extends AbstractCommand {

	private Dispatcher			dispatcher;

	public SessionCloseCommand(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void setParameter(	String field,
								Object value) {
		if (field.equals("dispatcher"))
			setDispatcher((Dispatcher) value);
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void execute() {
		this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, "Закрытие сессии..."));		
		final ClientSessionEnvironment clientSessionEnvironment = ClientSessionEnvironment.getInstance();
		try {
			clientSessionEnvironment.logout();
			this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, "Сессия закрыта"));
			this.dispatcher.firePropertyChange(new ContextChangeEvent(this, ContextChangeEvent.SESSION_CLOSED_EVENT));
		} catch (CommunicationException e) {
			Log.errorException(e);
			this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, "Ошибка при закрытии сессии..."));
		} catch (LoginException e) {
			Log.errorException(e);
			this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, "Ошибка при закрытии сессии..."));
		}
	}
}
