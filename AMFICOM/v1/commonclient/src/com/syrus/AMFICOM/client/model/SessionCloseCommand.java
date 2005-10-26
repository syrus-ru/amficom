
package com.syrus.AMFICOM.client.model;

import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClientSessionEnvironment;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.8 $, $Date: 2005/10/26 07:28:07 $
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
			this.dispatcher.firePropertyChange(
				new StatusMessageEvent(this, 
					StatusMessageEvent.STATUS_MESSAGE, 
					I18N.getString("Common.Login.LoggedOut")));
			this.dispatcher.firePropertyChange(
				new ContextChangeEvent(this, 
					ContextChangeEvent.LOGGED_OUT_EVENT));
			final ClientSessionEnvironment clientSessionEnvironment = 
				ClientSessionEnvironment.getInstance();

			clientSessionEnvironment.logout();
		} catch (ApplicationException e) {
			Log.errorException(e);
			this.dispatcher.firePropertyChange(new StatusMessageEvent(this,
					StatusMessageEvent.STATUS_MESSAGE,
					I18N.getString("Error.SessionClose")));
		}
	}
}
