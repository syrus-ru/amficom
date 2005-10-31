/*-
* $Id: LoginRestoreCommand.java,v 1.13 2005/10/31 12:30:02 bass Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.model;

import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.13 $, $Date: 2005/10/31 12:30:02 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public final class LoginRestoreCommand extends OpenSessionCommand implements LoginRestorer {
	
	public LoginRestoreCommand(Dispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	protected boolean logging() {
		super.logged = this.showOpenSessionDialog(Environment.getActiveWindow());
		return true;
	}

	@Override
	protected String getDialogTitle() {
		return I18N.getString("Common.Login.ReLogin");
	}	

	public String getLogin() {
		assert super.logged : ErrorMessages.NATURE_INVALID;

		final String value = super.login;
		super.login = null;
		return value;
	}

	public String getPassword() {
		assert super.logged : ErrorMessages.NATURE_INVALID;

		final String value = super.password;
		super.password = null;
		return value;
	}

	public Identifier getDomainId() {
		assert super.logged : ErrorMessages.NATURE_INVALID;

		final Identifier value = super.domainId;
		super.domainId = null;
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
		} catch (ApplicationException ae) {
			// Never
			Log.errorMessage(ae);
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

