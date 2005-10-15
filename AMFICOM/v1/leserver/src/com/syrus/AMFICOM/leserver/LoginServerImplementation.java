/*
 * $Id: LoginServerImplementation.java,v 1.31 2005/10/15 16:48:19 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.SystemUserWrapper;
import com.syrus.AMFICOM.administration.corba.IdlDomain;
import com.syrus.AMFICOM.general.ApplicationException;
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
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlCompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.leserver.corba.LoginServerPOA;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.ShadowDatabase;
import com.syrus.AMFICOM.security.UserLogin;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.31 $, $Date: 2005/10/15 16:48:19 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
final class LoginServerImplementation extends LoginServerPOA {
	private static final long serialVersionUID = -7190112124735462314L;

	private TypicalCondition tc;
	private ShadowDatabase shadowDatabase;

	protected LoginServerImplementation() {
		this.tc = new TypicalCondition("", OperationSort.OPERATION_EQUALS, ObjectEntities.SYSTEMUSER_CODE, SystemUserWrapper.COLUMN_LOGIN);
		this.shadowDatabase = new ShadowDatabase();
	}

	public IdlSessionKey login(final String login, final String password, final String userHostName, final IdlIdentifierHolder userIdTH)
			throws AMFICOMRemoteException {
		this.tc.setValue(login);
		Set<SystemUser> systemUsers = null;
		try {
			systemUsers = StorableObjectPool.getStorableObjectsByCondition(this.tc, true, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(IdlErrorCode.ERROR_RETRIEVE, IdlCompletionStatus.COMPLETED_NO, ae.getMessage());
		}
		if (systemUsers.isEmpty()) {
			throw new AMFICOMRemoteException(IdlErrorCode.ERROR_LOGIN_NOT_FOUND, IdlCompletionStatus.COMPLETED_YES, "Illegal login -- '" + login + "'");
		}

		final SystemUser user = systemUsers.iterator().next();
		final Identifier userId = user.getId();
		String localPassword = null;
		try {
			localPassword = this.shadowDatabase.retrieve(userId);
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(IdlErrorCode.ERROR_RETRIEVE, IdlCompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (ObjectNotFoundException onfe) {
			throw new AMFICOMRemoteException(IdlErrorCode.ERROR_LOGIN_NOT_FOUND, IdlCompletionStatus.COMPLETED_YES, onfe.getMessage());
		}

		if (Encryptor.crypt(password).equals(localPassword)) {
			userIdTH.value = userId.getTransferable();
			return LoginProcessor.addUserLogin(userId, userHostName).getTransferable();
		}
		throw new AMFICOMRemoteException(IdlErrorCode.ERROR_ILLEGAL_PASSWORD, IdlCompletionStatus.COMPLETED_YES, "Illegal password");
	}

	public void logout(final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final SessionKey sessionKey = new SessionKey(sessionKeyT);
		if (!LoginProcessor.removeUserLogin(sessionKey)) {
			throw new AMFICOMRemoteException(IdlErrorCode.ERROR_NOT_LOGGED_IN,
					IdlCompletionStatus.COMPLETED_YES,
					ErrorMessages.NOT_LOGGED_IN);
		}
	}

	/**
	 * Currently this method simply returns all existing domains.
	 * No checks user's access on domains.
	 * TODO Implement check user access on domains and return only accesible for the user.
	 */
	public IdlDomain[] transmitAvailableDomains(final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final SessionKey sessionKey = new SessionKey(sessionKeyT);
		if (!LoginProcessor.isUserLoginPresent(sessionKey)) {
			throw new AMFICOMRemoteException(IdlErrorCode.ERROR_NOT_LOGGED_IN,
					IdlCompletionStatus.COMPLETED_YES,
					ErrorMessages.NOT_LOGGED_IN);
		}

		final EquivalentCondition ec = new EquivalentCondition(ObjectEntities.DOMAIN_CODE);
		try {
			final Set<Domain> domains = StorableObjectPool.getStorableObjectsByCondition(ec, true, true);

			if (domains.size() == 0) {
				throw new AMFICOMRemoteException(IdlErrorCode.ERROR_NO_DOMAINS_AVAILABLE,
						IdlCompletionStatus.COMPLETED_YES,
						"No domains found for session key '" + sessionKey + "'");
			}

			final ORB orb = LEServerSessionEnvironment.getInstance().getLEServerServantManager().getCORBAServer().getOrb();
			final IdlDomain[] domainsT = new IdlDomain[domains.size()];
			int i = 0;
			for (final Domain domain : domains) {
				Log.debugMessage("LoginServerImplementation.transmitAvailableDomains | Domain '" + domain.getId()
						+ "', '" + domain.getName() + "'", Log.DEBUGLEVEL08);
				domainsT[i] = domain.getTransferable(orb);
			}
			return domainsT;
		}
		catch (ApplicationException ae) {
			throw new AMFICOMRemoteException(IdlErrorCode.ERROR_RETRIEVE, IdlCompletionStatus.COMPLETED_NO, ae.getMessage());
		}
	}

	public void selectDomain(final IdlSessionKey sessionKeyT, final IdlIdentifier domainIdT) throws AMFICOMRemoteException {
		final SessionKey sessionKey = new SessionKey(sessionKeyT);
		if (!LoginProcessor.isUserLoginPresent(sessionKey)) {
			throw new AMFICOMRemoteException(IdlErrorCode.ERROR_NOT_LOGGED_IN,
					IdlCompletionStatus.COMPLETED_YES,
					ErrorMessages.NOT_LOGGED_IN);
		}
		final Identifier domainId = new Identifier(domainIdT);
		LoginProcessor.setUserLoginDomain(sessionKey, domainId);
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
		final SessionKey sessionKey = new SessionKey(sessionKeyT);
		if (!LoginProcessor.isUserLoginPresent(sessionKey)) {
			throw new AMFICOMRemoteException(IdlErrorCode.ERROR_NOT_LOGGED_IN,
					IdlCompletionStatus.COMPLETED_YES,
					ErrorMessages.NOT_LOGGED_IN);
		}

		LoginProcessor.updateUserLoginLastActivityDate(sessionKey);

		final UserLogin userLogin = LoginProcessor.getUserLogin(sessionKey);
		userIdTH.value = userLogin.getUserId().getTransferable();
		final Identifier domainId = userLogin.getDomainId();
		domainIdTH.value = (domainId == null ? Identifier.VOID_IDENTIFIER : domainId).getTransferable();
	}

	public void setPassword(final IdlSessionKey sessionKeyT, final IdlIdentifier userIdT, final String password)
			throws AMFICOMRemoteException {
		this.validateAccess(sessionKeyT, new IdlIdentifierHolder(), new IdlIdentifierHolder());

		final Identifier userId = new Identifier(userIdT);
		try {
			final SystemUser systemUser = (SystemUser) StorableObjectPool.getStorableObject(userId, true);
			Log.debugMessage("Setting password to user '" + systemUser.getLogin() + "'/'" + userId + "'", Log.DEBUGLEVEL08);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(IdlErrorCode.ERROR_RETRIEVE, IdlCompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		try {
			this.shadowDatabase.updateOrInsert(userId, Encryptor.crypt(password));
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(IdlErrorCode.ERROR_UPDATE, IdlCompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.Verifiable#verify(byte)
	 */
	public void verify(final byte i) {
		Log.debugMessage("Verify value: " + i, Log.DEBUGLEVEL10);
	}

}
