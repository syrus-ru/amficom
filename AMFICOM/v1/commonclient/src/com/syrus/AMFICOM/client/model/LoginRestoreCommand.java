/*-
* $Id: LoginRestoreCommand.java,v 1.6 2005/08/09 16:06:38 arseniy Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.model;

import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.6 $, $Date: 2005/08/09 16:06:38 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class LoginRestoreCommand extends OpenSessionCommand 
implements LoginRestorer{
	
	public LoginRestoreCommand(Dispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	protected boolean logging() throws CommunicationException, LoginException {
		if (!this.logged && !this.showOpenSessionDialog(Environment.getActiveWindow())) {
			super.logged = false;
		} else {
			super.logged = true;
			super.disposeDialog();
		}
		return super.logged;
	}

	@Override
	protected String getDialogTitle() {
		return LangModelGeneral.getString("Login.ReLogin");
	}	

	public String getLogin() {
		assert super.logged : ErrorMessages.NATURE_INVALID;

		String value = super.login;
		super.login = null;
		return value;
	}

	public String getPassword() {
		assert super.logged : ErrorMessages.NATURE_INVALID;

		String value = super.password;
		super.password = null;
		return value;
	}

	@Override
	protected void createUIItems() {
		super.createUIItems();
		if (super.login != null) {
			super.loginTextField.setEditable(false);
		}
	}

	public boolean restoreLogin() {
		super.logged = false;

		SystemUser systemUser = null;
		try {
			systemUser = StorableObjectPool.getStorableObject(LoginManager.getUserId(), false);
		} catch (ApplicationException e) {
			// Never
			Log.errorException(e);
		}
		if (systemUser != null) {
			super.login = systemUser.getLogin();
		} else {
			super.login = null;
		}
		super.password = null;
		super.execute();
		return super.logged;
	}
}

