/*
 * $Id: LoginServerImplementation.java,v 1.15 2005/06/07 15:44:22 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.administration.UserWrapper;
import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.CompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.ErrorCode;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.TypicalCondition_TransferablePackage.OperationSort;
import com.syrus.AMFICOM.leserver.corba.LoginServerPOA;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.ShadowDatabase;
import com.syrus.AMFICOM.security.UserLogin;
import com.syrus.AMFICOM.security.UserLoginDatabase;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.15 $, $Date: 2005/06/07 15:44:22 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
final class LoginServerImplementation extends LoginServerPOA {
	private static final long serialVersionUID = -7190112124735462314L;

	private TypicalCondition tc;
	private UserLoginDatabase userLoginDatabase;
	private ShadowDatabase shadowDatabase;

	protected LoginServerImplementation() {
		this.tc = new TypicalCondition("", OperationSort.OPERATION_EQUALS, ObjectEntities.USER_ENTITY_CODE, UserWrapper.COLUMN_LOGIN);
		this.userLoginDatabase = new UserLoginDatabase();
		this.shadowDatabase = new ShadowDatabase();
	}

	public SessionKey_Transferable login(String login, String password, Identifier_TransferableHolder userIdTH)
			throws AMFICOMRemoteException {
		this.tc.setValue(login);
		Set set = null;
		try {
			set = StorableObjectPool.getStorableObjectsByCondition(this.tc, true, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}
		if (set.isEmpty())
			throw new AMFICOMRemoteException(ErrorCode.ERROR_LOGIN_NOT_FOUND, CompletionStatus.COMPLETED_YES, "Illegal login -- '" + login + "'");

		final User user = (User) set.iterator().next();
		final Identifier userId = user.getId();
		String localPassword = null;
		try {
			localPassword = this.shadowDatabase.retrieve(userId);
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (ObjectNotFoundException onfe) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_LOGIN_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}

		if (Encryptor.crypt(password).equals(localPassword)) {
			final UserLogin userLogin = UserLogin.createInstance(userId);
			LoginProcessor.addUserLogin(userLogin);

			try {
				this.userLoginDatabase.insert(userLogin);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}

			userIdTH.value = (Identifier_Transferable) userId.getTransferable();
			return (SessionKey_Transferable) userLogin.getSessionKey().getTransferable();
		}
		throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_PASSWORD, CompletionStatus.COMPLETED_YES, "Illegal password");
	}

	public void logout(SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		SessionKey sessionKey = new SessionKey(sessionKeyT);
		final UserLogin userLogin = LoginProcessor.removeUserLogin(sessionKey);
		if (userLogin != null)
			this.userLoginDatabase.delete(userLogin);
		else
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_LOGGED_IN, CompletionStatus.COMPLETED_YES, "Not logged in");
	}

	/**
	 * Currently this method simply returns all existing domains.
	 * No checks user's access on domains.
	 * TODO Implement check user access on domains and return only accesible for the user.
	 */
	public Domain_Transferable[] transmitAvailableDomains(SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		SessionKey sessionKey = new SessionKey(sessionKeyT);
		UserLogin userLogin = LoginProcessor.getUserLogin(sessionKey);
		if (userLogin == null)
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_LOGGED_IN, CompletionStatus.COMPLETED_YES, "Not logged in");

		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.DOMAIN_ENTITY_CODE);
		try {
			Set domains = StorableObjectPool.getStorableObjectsByCondition(ec, true, true);

			if (domains.size() == 0)
				throw new AMFICOMRemoteException(ErrorCode.ERROR_NO_DOMAINS_AVAILABLE,
						CompletionStatus.COMPLETED_YES,
						"No domains found for user '" + userLogin.getUserId() + "'");

			Domain_Transferable[] domainsT = new Domain_Transferable[domains.size()];
			int i = 0;
			for (Iterator it = domains.iterator(); it.hasNext(); i++)
				domainsT[i] = (Domain_Transferable) ((Domain) it.next()).getTransferable();
			return domainsT;
		}
		catch (ApplicationException ae) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}
	}

	public void selectDomain(SessionKey_Transferable sessionKeyT, Identifier_Transferable domainIdT) throws AMFICOMRemoteException {
		SessionKey sessionKey = new SessionKey(sessionKeyT);
		UserLogin userLogin = LoginProcessor.getUserLogin(sessionKey);
		if (userLogin != null) {
			userLogin.setDomainId(new Identifier(domainIdT));
			try {
				this.userLoginDatabase.update(userLogin);
			}
			catch (UpdateObjectException uoe) {
				Log.errorException(uoe);
			}
		}
		else
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_LOGGED_IN, CompletionStatus.COMPLETED_YES, "Not logged in");
	}

	/**
	 * @param sessionKeyT an "in" parameter.
	 * @param userIdTH an "out" parameter representing a user id.
	 * @param domainIdTH an "out" parameter representing a domain id.
	 * @throws AMFICOMRemoteException if user not logged in.
	 * @see com.syrus.AMFICOM.leserver.corba.LoginServerOperations#validateAccess(com.syrus.AMFICOM.security.corba.SessionKey_Transferable, com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder, com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder)
	 */
	public void validateAccess(final SessionKey_Transferable sessionKeyT,
			final Identifier_TransferableHolder userIdTH,
			final Identifier_TransferableHolder domainIdTH) throws AMFICOMRemoteException {
		final UserLogin userLogin = LoginProcessor.getUserLogin(new SessionKey(sessionKeyT));
		if (userLogin == null)
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_LOGGED_IN, CompletionStatus.COMPLETED_YES, "Not logged in");

		userLogin.updateLastActivityDate();
		try {
			this.userLoginDatabase.update(userLogin);
		}
		catch (final UpdateObjectException uoe) {
			Log.errorException(uoe);
		}

		userIdTH.value = (Identifier_Transferable) userLogin.getUserId().getTransferable();
		final Identifier domainId = userLogin.getDomainId();
		domainIdTH.value = (Identifier_Transferable) (domainId == null ? Identifier.VOID_IDENTIFIER : domainId).getTransferable();
	}

	public void verify(byte i) {
		Log.debugMessage("Verify value: " + i, Log.DEBUGLEVEL10);
	}

}
