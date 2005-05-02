/*
 * $Id: LoginServerImplementation.java,v 1.5 2005/05/02 19:05:08 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import java.util.Set;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.administration.UserWrapper;
import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.SecurityKey;
import com.syrus.AMFICOM.leserver.corba.LoginServerPOA;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/05/02 19:05:08 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
final class LoginServerImplementation extends LoginServerPOA {
	private static final long serialVersionUID = -7190112124735462314L;

	private TypicalCondition tc;
	private ShadowDatabase shadowDatabase;

	protected LoginServerImplementation() {
		this.tc = new TypicalCondition("", OperationSort.OPERATION_EQUALS, ObjectEntities.USER_ENTITY_CODE, UserWrapper.COLUMN_LOGIN);
		this.shadowDatabase = new ShadowDatabase();
	}

	public SecurityKey login(String login, String password, Identifier_TransferableHolder userIdTH) throws AMFICOMRemoteException {
		this.tc.setValue(login);
		Set set = null;
		try {
			set = AdministrationStorableObjectPool.getStorableObjectsByCondition(this.tc, true);
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

		if (password.equals(localPassword)) {
			UserLogin userLogin = UserLogin.createInstance(userId, password);
			LoginProcessor.addUserLogin(userLogin);
			userIdTH.value = (Identifier_Transferable) userId.getTransferable();
			return userLogin.getSecurityKey();
		}
		throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_PASSWORD, CompletionStatus.COMPLETED_YES, "Illegal password");
	}

	public void logout(SecurityKey securityKey) throws AMFICOMRemoteException {
		UserLogin userLogin = LoginProcessor.removeUserLogin(securityKey);
		if (userLogin == null)
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_DATA, CompletionStatus.COMPLETED_YES, "Illegal security key");
	}

	public Domain_Transferable[] transmitAvailableDomains(SecurityKey security_key) throws AMFICOMRemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public void selectDomain(Identifier_Transferable domain_id) throws AMFICOMRemoteException {
		// TODO Auto-generated method stub
		
	}

	public void validateAccess(SecurityKey security_key, Identifier_TransferableHolder user_id, Identifier_TransferableHolder domain_id) throws AMFICOMRemoteException {
		// TODO Auto-generated method stub
		
	}

	public void verify(byte i) {
		Log.debugMessage("Verify value: " + i, Log.DEBUGLEVEL10);
	}

}
