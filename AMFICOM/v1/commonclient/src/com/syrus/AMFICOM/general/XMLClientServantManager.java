/*
 * $Id: XMLClientServantManager.java,v 1.11 2005/06/21 12:51:12 bass Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Date;
import java.util.Iterator;
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

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.SystemUserWrapper;
import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlIdentifierHolder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.CompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.ErrorCode;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.leserver.corba.EventServer;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.11 $, $Date: 2005/06/21 12:51:12 $
 * @author $Author: bass $
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

			public Domain_Transferable[] transmitAvailableDomains(IdlSessionKey arg0) throws AMFICOMRemoteException {
				try {
					Set storableObjectsByCondition = StorableObjectPool.getStorableObjectsByCondition(new EquivalentCondition(ObjectEntities.DOMAIN_CODE), true, true);
					Domain_Transferable[] transferables = new Domain_Transferable[storableObjectsByCondition.size()];
					int i = 0;
					for (Iterator iterator = storableObjectsByCondition.iterator(); iterator.hasNext();i++) {
						Domain domain = (Domain) iterator.next();
						transferables[i] = (Domain_Transferable) domain.getTransferable();
					}
					return transferables;
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				throw new AMFICOMRemoteException(ErrorCode.ERROR_NO_DOMAINS_AVAILABLE, CompletionStatus.COMPLETED_NO, "There is no available domains.");
			}
			
			public Request _create_request(	Context ctx,
											String operation,
											NVList arg_list,
											NamedValue result) {
				return null;
			}
			
			public Request _create_request(	Context ctx,
											String operation,
											NVList arg_list,
											NamedValue result,
											ExceptionList exclist,
											ContextList ctxlist) {
				return null;
			}
			
			public Object _duplicate() {
				return null;
			}
			
			public DomainManager[] _get_domain_managers() {
				return null;
			}
			public Object _get_interface_def() {
				return null;
			}
			
			public Policy _get_policy(int policy_type) {
				return null;
			}
			
			public int _hash(int maximum) {
				return 0;
			}
			
			public boolean _is_a(String repositoryIdentifier) {
				return false;
			}
			
			public boolean _is_equivalent(Object other) {
				return false;
			}
			
			public boolean _non_existent() {
				return false;
			}
			
			public void _release() {
				// nothing
			}
			
			public Request _request(String operation) {
				return null;
			}
			
			public Object _set_policy_override(	Policy[] policies,
												SetOverrideType set_add) {
				return null;
			}
			
			public IdlSessionKey login(	String login,
													String password,
													IdlIdentifierHolder identifierTransferableHolder) throws AMFICOMRemoteException {
				IdlSessionKey transferable = new IdlSessionKey(new Date().toString());
				try {
					Set users = StorableObjectPool.getStorableObjectsByCondition(new TypicalCondition(login, OperationSort.OPERATION_EQUALS, ObjectEntities.SYSTEMUSER_CODE, SystemUserWrapper.COLUMN_LOGIN), true, true);
					if (users.isEmpty()) {
						throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_LOGIN, CompletionStatus.COMPLETED_NO, "Error during acquire user");
					}
					identifierTransferableHolder.value = (IdlIdentifier) ((SystemUser)users.iterator().next()).getId().getTransferable();
				} catch (ApplicationException e) {
					throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_LOGIN, CompletionStatus.COMPLETED_NO, "Error during acquire user");
				}
								
				return transferable;
			}
			
			public void logout(IdlSessionKey arg0) throws AMFICOMRemoteException {
				// nothing				
			}
			
			/* (non-Javadoc)
			 * @see com.syrus.AMFICOM.leserver.corba.LoginServerOperations#selectDomain(com.syrus.AMFICOM.security.corba.IdlSessionKey, com.syrus.AMFICOM.general.corba.IdlIdentifier)
			 */
			public void selectDomain(	IdlSessionKey arg0,
										IdlIdentifier arg1) throws AMFICOMRemoteException {
				// TODO Auto-generated method stub
				
			}
			
			public void validateAccess(	IdlSessionKey arg0,
										IdlIdentifierHolder arg1,
										IdlIdentifierHolder arg2) throws AMFICOMRemoteException {
				//	nothing		
			}
			
			public void verify(byte i) {
				// nothing				
			}

			/**
			 * @todo This is just to enable compatibility and compile. No sense. 
			 */
			public void setPassword(IdlSessionKey arg0, IdlIdentifier arg1, String arg2) throws AMFICOMRemoteException {
				/*Don't know, what to write here*/
				throw new UnsupportedOperationException(ErrorMessages.METHOD_NOT_NEEDED);
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
		assert false : ErrorMessages.METHOD_NOT_NEEDED;
		return null;
	}
	
	public CORBAServer getCORBAServer() {		
		return null;
	}

}
