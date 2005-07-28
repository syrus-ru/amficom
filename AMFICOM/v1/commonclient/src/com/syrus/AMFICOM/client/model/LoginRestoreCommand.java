/*-
* $Id: LoginRestoreCommand.java,v 1.2 2005/07/28 09:45:34 bob Exp $
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
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.AMFICOM.general.StorableObjectPool;


/**
 * @version $Revision: 1.2 $, $Date: 2005/07/28 09:45:34 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient_v1
 */
public class LoginRestoreCommand extends OpenSessionCommand 
implements LoginRestorer{
	
	public LoginRestoreCommand(Dispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	protected boolean logging() 
	throws CommunicationException, IllegalDataException, LoginException {
		if (!this.logged && !this.showOpenSessionDialog(
				Environment.getActiveWindow())) {
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
		super.loginTextField.setEditable(false);
	}
	
	public boolean restoreLogin() {
		super.logged = false;
		try {
			SystemUser user = 
				(SystemUser) StorableObjectPool.getStorableObject(
					LoginManager.getUserId(), 
					true);
			super.login = user.getLogin();
		} catch (ApplicationException e) {
			return false;
		}
		super.password = null;
		super.execute();
		return super.logged;
	}	
}

