/*-
 * $Id: DefaultCORBAActionProcessor.java,v 1.9 2005/11/28 12:16:16 arseniy Exp $
 *
 * Copyright ? 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.logging.Level;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.9 $, $Date: 2005/11/28 12:16:16 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public class DefaultCORBAActionProcessor implements CORBAActionProcessor {

	public DefaultCORBAActionProcessor() {
		//????? ????
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
							Log.debugMessage("Login not restored", Level.INFO);
							return;
						}
						throw new LoginException(are.message);
					default:
						throw new RetrieveObjectException(are.message);
				}
			}
		}
	}

	final boolean restoreLogin() throws LoginException, CommunicationException {
		return LoginManager.restoreLogin();
	}
}
