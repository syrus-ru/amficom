/*
 * $Id: LoginServerImplementation.java,v 1.45 2006/06/02 13:48:21 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.administration.SystemUserWrapper.COLUMN_LOGIN;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NOT_LOGGED_IN;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;
import static com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlCompletionStatus.COMPLETED_NO;
import static com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlCompletionStatus.COMPLETED_YES;
import static com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode.ERROR_ACCESS_VALIDATION;
import static com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode.ERROR_ILLEGAL_PASSWORD;
import static com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode.ERROR_LOGIN_NOT_FOUND;
import static com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode.ERROR_NOT_LOGGED_IN;
import static com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode.ERROR_NO_DOMAINS_AVAILABLE;
import static com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode.ERROR_RETRIEVE;
import static com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode.ERROR_UPDATE;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;

import java.util.Set;

import org.omg.CORBA.LongHolder;
import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.corba.IdlDomain;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonUser;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlIdentifierHolder;
import com.syrus.AMFICOM.leserver.corba.LoginServerPOA;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.ShadowDatabase;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.AMFICOM.security.corba.IdlSessionKeyHolder;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.45 $, $Date: 2006/06/02 13:48:21 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
final class LoginServerImplementation extends LoginServerPOA {
	private static final long serialVersionUID = -7190112124735462314L;

	private TypicalCondition tc;
	private ShadowDatabase shadowDatabase;

	protected LoginServerImplementation() {
		this.tc = new TypicalCondition("",
				OPERATION_EQUALS,
				SYSTEMUSER_CODE,
				COLUMN_LOGIN);
		this.shadowDatabase = new ShadowDatabase();
	}


	public IdlDomain[] transmitAvailableDomains() throws AMFICOMRemoteException {
		final EquivalentCondition ec = new EquivalentCondition(DOMAIN_CODE);
		try {
			final Set<Domain> domains = StorableObjectPool.getStorableObjectsByCondition(ec, true, true);

			if (domains.isEmpty()) {
				throw new AMFICOMRemoteException(ERROR_NO_DOMAINS_AVAILABLE,
						COMPLETED_YES,
						"No domains found");
			}

			final ORB orb = LEServerSessionEnvironment.getInstance().getLEServerServantManager().getCORBAServer().getOrb();
			final IdlDomain[] idlDomains = new IdlDomain[domains.size()];
			int i = 0;
			for (final Domain domain : domains) {
				Log.debugMessage("Domain '" + domain.getId() + "', '" + domain.getName() + "'", Log.DEBUGLEVEL08);
				idlDomains[i++] = domain.getIdlTransferable(orb);
			}
			return idlDomains;
		} catch (ApplicationException ae) {
			throw new AMFICOMRemoteException(ERROR_RETRIEVE, COMPLETED_NO, ae.getMessage());
		}
	}

	public void login(final String login,
			final String password,
			final IdlIdentifier idlDomainId,
			final CommonUser commonUser,
			final IdlSessionKeyHolder idlSessionKeyHolder,
			final IdlIdentifierHolder userIdTH) throws AMFICOMRemoteException {
		this.tc.setValue(login);
		Set<SystemUser> systemUsers = null;
		try {
			systemUsers = StorableObjectPool.getStorableObjectsByCondition(this.tc, true, true);
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			throw new AMFICOMRemoteException(ERROR_RETRIEVE, COMPLETED_NO, ae.getMessage());
		}
		if (systemUsers.isEmpty()) {
			throw new AMFICOMRemoteException(ERROR_LOGIN_NOT_FOUND,
					COMPLETED_YES,
					"Illegal login -- '" + login + "'");
		}

		final SystemUser user = systemUsers.iterator().next();
		final Identifier userId = user.getId();
		String localPassword = null;
		try {
			localPassword = this.shadowDatabase.retrieve(userId);
		} catch (RetrieveObjectException roe) {
			Log.errorMessage(roe);
			throw new AMFICOMRemoteException(ERROR_RETRIEVE, COMPLETED_NO, roe.getMessage());
		} catch (ObjectNotFoundException onfe) {
			throw new AMFICOMRemoteException(ERROR_LOGIN_NOT_FOUND, COMPLETED_YES, onfe.getMessage());
		}

		if (!Encryptor.crypt(password).equals(localPassword)) {
			throw new AMFICOMRemoteException(ERROR_ILLEGAL_PASSWORD, COMPLETED_YES, "Illegal password");
		}

		final Identifier domainId = Identifier.valueOf(idlDomainId);
		if (!this.validateDomainAccess(userId, domainId)) {
			throw new AMFICOMRemoteException(ERROR_ACCESS_VALIDATION, COMPLETED_YES, "Access to domain denied");
		}

		final String userIOR = LEServerSessionEnvironment.getInstance().getLEServerServantManager().getCORBAServer().objectToString(commonUser);
		idlSessionKeyHolder.value = LoginProcessor.getInstance().addUserLogin(userId, domainId, userIOR).getIdlTransferable();
		userIdTH.value = userId.getIdlTransferable();
		return;
	}

	/**
	 * @todo Implement
	 * @param userId
	 * @param domainId
	 */
	private boolean validateDomainAccess(final Identifier userId, final Identifier domainId) {
		assert userId != null : NON_NULL_EXPECTED;
		assert domainId != null : NON_NULL_EXPECTED;

		return true;
	}

	public void logout(final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final SessionKey sessionKey = new SessionKey(sessionKeyT);
		if (!LoginProcessor.getInstance().removeUserLogin(sessionKey)) {
			throw new AMFICOMRemoteException(ERROR_NOT_LOGGED_IN,
					COMPLETED_YES,
					NOT_LOGGED_IN);
		}
	}

	public void validateLogin(final IdlSessionKey idlSessionKey, final LongHolder loginValidationPeriodHolder) throws AMFICOMRemoteException {
		final long loginValidationPeriod = LoginProcessor.getInstance().getLoginValidationPeriod(new SessionKey(idlSessionKey));
		if (loginValidationPeriod < 0) {
			throw new AMFICOMRemoteException(ERROR_NOT_LOGGED_IN,
					COMPLETED_YES,
					NOT_LOGGED_IN);
		}

		loginValidationPeriodHolder.value = loginValidationPeriod;
	}

	public void setPassword(final IdlSessionKey sessionKeyT, final IdlIdentifier userIdT, final String password)
			throws AMFICOMRemoteException {
		final Identifier userId = Identifier.valueOf(userIdT);
		try {
			final SystemUser systemUser = (SystemUser) StorableObjectPool.getStorableObject(userId, true);
			Log.debugMessage("Setting password to user '" + systemUser.getLogin() + "'/'" + userId + "'", Log.DEBUGLEVEL08);
		}
		catch (ApplicationException ae) {
			Log.errorMessage(ae);
			throw new AMFICOMRemoteException(ERROR_RETRIEVE, COMPLETED_NO, ae.getMessage());
		}

		try {
			this.shadowDatabase.updateOrInsert(userId, Encryptor.crypt(password));
		}
		catch (UpdateObjectException uoe) {
			Log.errorMessage(uoe);
			throw new AMFICOMRemoteException(ERROR_UPDATE, COMPLETED_NO, uoe.getMessage());
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.Verifiable#verify(byte)
	 */
	public void verify(final byte i) {
		Log.debugMessage("Verify value: " + i, Log.DEBUGLEVEL10);
	}

}
