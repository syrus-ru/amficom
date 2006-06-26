/*-
 * $Id: ServerCore.java,v 1.1 2006/06/26 10:49:41 cvsadmin Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.systemserver;

import static com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlCompletionStatus.COMPLETED_NO;
import static com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlCompletionStatus.COMPLETED_PARTIALLY;
import static com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode.ERROR_ACCESS_VALIDATION;
import static com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode.ERROR_UNKNOWN;
import static java.util.logging.Level.CONFIG;
import static java.util.logging.Level.SEVERE;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.LongHolder;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.systemserver.corba.LoginServer;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: cvsadmin $
 * @version $Revision: 1.1 $, $Date: 2006/06/26 10:49:41 $
 * @module systemserver
 */
public abstract class ServerCore {
	private final Map<SessionKey, Date> loginValidationComingDates;

	protected ServerCore() {
		this.loginValidationComingDates = new HashMap<SessionKey, Date>();
	}


	// /////////////////////////////// Verifiable /////////////////////////////////////////////

	public final void verify(final byte b) {
		try {
			Log.debugMessage("Verify value: " + b, CONFIG);
		} catch (final Throwable t) {
			Log.debugMessage(t, SEVERE);
		}
	}


	// ///////////////////////////// Validate login ///////////////////////////////////////////////

	final void validateLogin(final SessionKey sessionKey) throws AMFICOMRemoteException {
		final Date comingDate = this.loginValidationComingDates.get(sessionKey);
		if (comingDate == null || comingDate.getTime() <= System.currentTimeMillis()) {
			final LongHolder loginValidationTimeoutHolder = new LongHolder();
			try {
				final LoginServer loginServer = SystemServerServantManager.getInstance().getLoginServerReference();
				loginServer.validateLogin(sessionKey.getIdlTransferable(), loginValidationTimeoutHolder);
				this.loginValidationComingDates.put(sessionKey,
						new Date(System.currentTimeMillis() + loginValidationTimeoutHolder.value));
			} catch (final CommunicationException ce) {
				throw new AMFICOMRemoteException(ERROR_ACCESS_VALIDATION, COMPLETED_NO, ce.getMessage());
			} catch (AMFICOMRemoteException are) {
				throw are;
			} catch (final Throwable throwable) {
				throw this.processDefaultThrowable(throwable);
			}
		}
	}


	// ///////////////////////////// helper methods ///////////////////////////////////////////////

	final AMFICOMRemoteException processDefaultApplicationException(final ApplicationException ae, final IdlErrorCode errorCode) {
		Log.debugMessage(ae, SEVERE);
		return new AMFICOMRemoteException(errorCode, COMPLETED_NO, ae.getMessage());
	}

	final AMFICOMRemoteException processDefaultThrowable(final Throwable throwable) {
		Log.debugMessage(throwable, SEVERE);
		return new AMFICOMRemoteException(ERROR_UNKNOWN, COMPLETED_PARTIALLY, throwable.getMessage());
	}

}
