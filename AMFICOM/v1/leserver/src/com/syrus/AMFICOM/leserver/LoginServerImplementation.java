/*
 * $Id: LoginServerImplementation.java,v 1.26 2005/06/25 18:05:56 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.SystemUserWrapper;
import com.syrus.AMFICOM.administration.corba.IdlDomain;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlIdentifierHolder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.CompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.ErrorCode;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.leserver.corba.LoginServerPOA;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.ShadowDatabase;
import com.syrus.AMFICOM.security.UserLogin;
import com.syrus.AMFICOM.security.UserLoginDatabase;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.26 $, $Date: 2005/06/25 18:05:56 $
 * @author $Author: bass $
 * @module leserver_v1
 */
final class LoginServerImplementation extends LoginServerPOA {
	private static final long serialVersionUID = -7190112124735462314L;

	private TypicalCondition tc;
	private UserLoginDatabase userLoginDatabase;
	private ShadowDatabase shadowDatabase;

	private ORB orb;

	protected LoginServerImplementation(final ORB orb) {
		this.tc = new TypicalCondition("", OperationSort.OPERATION_EQUALS, ObjectEntities.SYSTEMUSER_CODE, SystemUserWrapper.COLUMN_LOGIN);
		this.userLoginDatabase = new UserLoginDatabase();
		this.shadowDatabase = new ShadowDatabase();
		this.orb = orb;
	}

	public IdlSessionKey login(String login, String password, IdlIdentifierHolder userIdTH)
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

		final SystemUser user = (SystemUser) set.iterator().next();
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

			userIdTH.value = userId.getTransferable();
			return userLogin.getSessionKey().getTransferable();
		}
		throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_PASSWORD, CompletionStatus.COMPLETED_YES, "Illegal password");
	}

	public void logout(IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		SessionKey sessionKey = new SessionKey(sessionKeyT);
		final UserLogin userLogin = LoginProcessor.removeUserLogin(sessionKey);
		if (userLogin != null)
			this.userLoginDatabase.delete(userLogin);
		else
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_LOGGED_IN, CompletionStatus.COMPLETED_YES, ErrorMessages.NOT_LOGGED_IN);
	}

	/**
	 * Currently this method simply returns all existing domains.
	 * No checks user's access on domains.
	 * TODO Implement check user access on domains and return only accesible for the user.
	 */
	public IdlDomain[] transmitAvailableDomains(IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		SessionKey sessionKey = new SessionKey(sessionKeyT);
		UserLogin userLogin = LoginProcessor.getUserLogin(sessionKey);
		if (userLogin == null)
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_LOGGED_IN, CompletionStatus.COMPLETED_YES, ErrorMessages.NOT_LOGGED_IN);

		EquivalentCondition ec = new EquivalentCondition(ObjectEntities.DOMAIN_CODE);
		try {
			Set domains = StorableObjectPool.getStorableObjectsByCondition(ec, true, true);

			if (domains.size() == 0)
				throw new AMFICOMRemoteException(ErrorCode.ERROR_NO_DOMAINS_AVAILABLE,
						CompletionStatus.COMPLETED_YES,
						"No domains found for user '" + userLogin.getUserId() + "'");

			IdlDomain[] domainsT = new IdlDomain[domains.size()];
			int i = 0;
			for (Iterator it = domains.iterator(); it.hasNext(); i++)
				domainsT[i] = ((Domain) it.next()).getTransferable(this.orb);
			return domainsT;
		}
		catch (ApplicationException ae) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}
	}

	public void selectDomain(IdlSessionKey sessionKeyT, IdlIdentifier domainIdT) throws AMFICOMRemoteException {
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
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_LOGGED_IN, CompletionStatus.COMPLETED_YES, ErrorMessages.NOT_LOGGED_IN);
	}

	/**
	 * @param sessionKeyT an "in" parameter.
	 * @param userIdTH an "out" parameter representing a user id.
	 * @param domainIdTH an "out" parameter representing a domain id.
	 * @throws AMFICOMRemoteException if user not logged in.
	 * @see com.syrus.AMFICOM.leserver.corba.LoginServerOperations#validateAccess(com.syrus.AMFICOM.security.corba.IdlSessionKey, com.syrus.AMFICOM.general.corba.IdlIdentifierHolder, com.syrus.AMFICOM.general.corba.IdlIdentifierHolder)
	 */
	public void validateAccess(final IdlSessionKey sessionKeyT,
			final IdlIdentifierHolder userIdTH,
			final IdlIdentifierHolder domainIdTH) throws AMFICOMRemoteException {
		final UserLogin userLogin = LoginProcessor.getUserLogin(new SessionKey(sessionKeyT));
		if (userLogin == null)
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_LOGGED_IN, CompletionStatus.COMPLETED_YES, ErrorMessages.NOT_LOGGED_IN);

		userLogin.updateLastActivityDate();
		try {
			this.userLoginDatabase.update(userLogin);
		}
		catch (final UpdateObjectException uoe) {
			Log.errorException(uoe);
		}

		userIdTH.value = userLogin.getUserId().getTransferable();
		final Identifier domainId = userLogin.getDomainId();
		domainIdTH.value = (domainId == null ? Identifier.VOID_IDENTIFIER : domainId).getTransferable();
	}

	public void setPassword(final IdlSessionKey sessionKeyT, final IdlIdentifier userIdT, final String password) throws AMFICOMRemoteException {
		this.validateAccess(sessionKeyT, new IdlIdentifierHolder(), new IdlIdentifierHolder());

		final Identifier userId = new Identifier(userIdT);
		try {
			final SystemUser systemUser = (SystemUser) StorableObjectPool.getStorableObject(userId, true);
			Log.debugMessage("Setting password to user '" + systemUser.getLogin() + "'/'" + userId + "'", Log.DEBUGLEVEL08);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		try {
			this.shadowDatabase.updateOrInsert(userId, Encryptor.crypt(password));
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.Verifiable#verify(byte)
	 */
	public void verify(byte i) {
		Log.debugMessage("Verify value: " + i, Log.DEBUGLEVEL10);
	}

}
