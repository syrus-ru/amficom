/*-
 * $Id: DefaultCORBAActionProcessor.java,v 1.4 2005/10/20 14:17:47 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.logging.Level;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.4 $, $Date: 2005/10/20 14:17:47 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public class DefaultCORBAActionProcessor implements CORBAActionProcessor {
	LoginRestorer loginRestorer;

	public DefaultCORBAActionProcessor(final LoginRestorer loginRestorer) {
		this.loginRestorer = loginRestorer;
	}

	public void performAction(final CORBAAction action) throws ApplicationException {
		int numEfforts = 0;
		while (true) {
			try {
				action.perform();
				return;
			} catch (final AMFICOMRemoteException are) {
				switch (are.errorCode.value()) {
					case IdlErrorCode._ERROR_NOT_LOGGED_IN:
						if (++numEfforts == 1) {
							if (this.restoreLogin()) {
								continue;
							}
							Log.debugMessage("DefaultCORBAActionProcessor.performAction | Login not restored", Level.INFO);
							return;
						}
						throw new LoginException(are.message);
					default:
						throw new RetrieveObjectException(are.message);
				}
			}
		}
	}

	/**
	 *
	 * @return true, only when LoginRestorer.restoreLogin() returned true,
	 * i.e., when user or application decided to restore login
	 * @throws LoginException
	 * @throws CommunicationException
	 */
	boolean restoreLogin() throws LoginException, CommunicationException {
		if (this.loginRestorer != null && this.loginRestorer.restoreLogin()) {
			LoginManager.login(this.loginRestorer.getLogin(), this.loginRestorer.getPassword(), this.loginRestorer.getDomainId());
			return true;
		}
		return false;
	}
}
