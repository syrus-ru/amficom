/*
 * $Id: XMLClientServantManager.java,v 1.14 2005/07/28 19:45:42 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.METHOD_NOT_NEEDED;

import java.util.Date;
import java.util.Set;

import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.DomainManager;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.Request;
import org.omg.CORBA.SetOverrideType;

import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.SystemUserWrapper;
import com.syrus.AMFICOM.administration.corba.IdlDomain;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlIdentifierHolder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlCompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.leserver.corba.EventServer;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.14 $, $Date: 2005/07/28 19:45:42 $
 * @author $Author: arseniy $
 * @module commonclient_v1
 */
abstract class XMLClientServantManager implements BaseConnectionManager {

	protected LoginServer loginServer;
	protected EventServer eventServer;
	protected IdentifierGeneratorServer identifierGeneratorServer;

	public XMLClientServantManager() {
		this.createLoginServer();

	}

	private void createLoginServer() {
		this.loginServer = new LoginServer() {
			private static final long serialVersionUID = 6881608272719879636L;

			public Request _create_request(Context ctx, String operation, NVList arg_list, NamedValue result) {
				assert false : METHOD_NOT_NEEDED;
				return null;
			}

			public Request _create_request(Context ctx,
					String operation,
					NVList arg_list,
					NamedValue result,
					ExceptionList exclist,
					ContextList ctxlist) {
				assert false : METHOD_NOT_NEEDED;
				return null;
			}

			public Object _duplicate() {
				assert false : METHOD_NOT_NEEDED;
				return null;
			}

			public DomainManager[] _get_domain_managers() {
				assert false : METHOD_NOT_NEEDED;
				return null;
			}

			public Object _get_interface_def() {
				assert false : METHOD_NOT_NEEDED;
				return null;
			}

			public Policy _get_policy(int policy_type) {
				assert false : METHOD_NOT_NEEDED;
				return null;
			}

			public int _hash(int maximum) {
				assert false : METHOD_NOT_NEEDED;
				return 0;
			}

			public boolean _is_a(String repositoryIdentifier) {
				assert false : METHOD_NOT_NEEDED;
				return false;
			}

			public boolean _is_equivalent(Object other) {
				assert false : METHOD_NOT_NEEDED;
				return false;
			}

			public boolean _non_existent() {
				assert false : METHOD_NOT_NEEDED;
				return false;
			}

			public void _release() {
				assert false : METHOD_NOT_NEEDED;
			}

			public Request _request(String operation) {
				assert false : METHOD_NOT_NEEDED;
				return null;
			}

			public Object _set_policy_override(Policy[] policies, SetOverrideType set_add) {
				assert false : METHOD_NOT_NEEDED;
				return null;
			}

			public IdlSessionKey login(String login, String password, IdlIdentifierHolder identifierTransferableHolder)
					throws AMFICOMRemoteException {
				IdlSessionKey transferable = new IdlSessionKey(new Date().toString());
				try {
					Set users = StorableObjectPool.getStorableObjectsByCondition(new TypicalCondition(login, OperationSort.OPERATION_EQUALS, ObjectEntities.SYSTEMUSER_CODE, SystemUserWrapper.COLUMN_LOGIN), true, true);
					if (users.isEmpty()) {
						throw new AMFICOMRemoteException(IdlErrorCode.ERROR_ILLEGAL_LOGIN, IdlCompletionStatus.COMPLETED_NO, "Error during acquire user");
					}
					identifierTransferableHolder.value = ((SystemUser) users.iterator().next()).getId().getTransferable();
				} catch (ApplicationException e) {
					throw new AMFICOMRemoteException(IdlErrorCode.ERROR_ILLEGAL_LOGIN, IdlCompletionStatus.COMPLETED_NO, "Error during acquire user");
				}
								
				return transferable;
			}
			
			public void logout(IdlSessionKey arg0) throws AMFICOMRemoteException {
				// Nothing
			}

			public IdlDomain[] transmitAvailableDomains(IdlSessionKey arg0) throws AMFICOMRemoteException {
				return new IdlDomain[0];
			}

			public void selectDomain(IdlSessionKey arg0, IdlIdentifier arg1) throws AMFICOMRemoteException {
				// Nothing
			}

			public void validateAccess(IdlSessionKey arg0, IdlIdentifierHolder arg1, IdlIdentifierHolder arg2)
					throws AMFICOMRemoteException {
				// Nothing
			}

			public void verify(byte i) {
				assert false : METHOD_NOT_NEEDED;
			}

			public void setPassword(IdlSessionKey arg0, IdlIdentifier arg1, String arg2) throws AMFICOMRemoteException {
				throw new UnsupportedOperationException(METHOD_NOT_NEEDED);
			}

		};

		this.identifierGeneratorServer = new LocalIdentifierGeneratorServer();
	}

	public final LoginServer getLoginServerReference() {
		return this.loginServer;
	}

	public final EventServer getEventServerReference() {
		return this.eventServer;
	}

	public final IdentifierGeneratorServer getIGSReference() {
		return this.identifierGeneratorServer;
	}	

	public final CommonServer getServerReference() {
		assert false : METHOD_NOT_NEEDED;
		return null;
	}

	public CORBAServer getCORBAServer() {
		assert false : METHOD_NOT_NEEDED;
		return null;
	}

}
