
package com.syrus.AMFICOM.client.model;

import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClientSessionEnvironment;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.7 $, $Date: 2005/10/24 11:54:31 $
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
		this.dispatcher.firePropertyChange(
			new StatusMessageEvent(this, 
				StatusMessageEvent.STATUS_MESSAGE, 
				I18N.getString("Common.Login.LogOut")));
		try {
			final ClientSessionEnvironment clientSessionEnvironment = 
				ClientSessionEnvironment.getInstance();

			clientSessionEnvironment.logout();
			this.dispatcher.firePropertyChange(
				new StatusMessageEvent(this, 
					StatusMessageEvent.STATUS_MESSAGE, 
					I18N.getString("Common.Login.LoggedOut")));
			this.dispatcher.firePropertyChange(
				new ContextChangeEvent(this, 
					ContextChangeEvent.LOGGED_OUT_EVENT));
		} catch (ApplicationException e) {
			Log.errorException(e);
			this.dispatcher.firePropertyChange(new StatusMessageEvent(this,
					StatusMessageEvent.STATUS_MESSAGE,
					I18N.getString("Error.SessionClose")));
		}
	}
}
