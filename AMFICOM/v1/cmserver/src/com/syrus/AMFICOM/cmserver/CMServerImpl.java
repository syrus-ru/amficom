/*
 * $Id: CMServerImpl.java,v 1.98 2005/04/12 17:16:21 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;


import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.administration.UserWrapper;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.AccessIdentity;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.98 $, $Date: 2005/04/12 17:16:21 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */

public class CMServerImpl extends CMMeasurementTransmit {

	private static final long serialVersionUID = 3760563104903672628L;

//    private MServer mServer;
    //////////////////////////////////Name Resolver/////////////////////////////////////////////////

    
	public String lookupDomainName(Identifier_Transferable idTransferable) throws AMFICOMRemoteException {
		try {
			Identifier id = new Identifier(idTransferable);
			return ( (Domain)AdministrationStorableObjectPool.getStorableObject(id, true) ).getName();
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public String lookupUserLogin(Identifier_Transferable identifier_Transferable) throws AMFICOMRemoteException {
		try {
			Identifier id = new Identifier(identifier_Transferable);
			return ( (User)AdministrationStorableObjectPool.getStorableObject(id, true) ).getLogin();
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public String lookupUserName(Identifier_Transferable identifier_Transferable) throws AMFICOMRemoteException {
		try {
			Identifier id = new Identifier(identifier_Transferable);
			return ( (User)AdministrationStorableObjectPool.getStorableObject(id, true) ).getName();
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Identifier_Transferable reverseLookupDomainName(String domainName) throws AMFICOMRemoteException {
		try {
			TypicalCondition condition = new TypicalCondition(domainName, OperationSort.OPERATION_EQUALS,
																ObjectEntities.DOMAIN_ENTITY_CODE,
																StorableObjectWrapper.COLUMN_NAME);
			Collection collection = AdministrationStorableObjectPool.getStorableObjectsByCondition(condition, true);
			if (collection.isEmpty())
				throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO,
													"list is empty");
			Identifier id = ((Domain) collection.iterator().next()).getId();
			return (Identifier_Transferable) id.getTransferable();
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Identifier_Transferable reverseLookupUserLogin(String userLogin) throws AMFICOMRemoteException {
		try {
			Log.debugMessage("CMServerImpl.reverseLookupUserLogin | userLogin " + userLogin, Log.DEBUGLEVEL07);
			TypicalCondition condition = new TypicalCondition(userLogin, OperationSort.OPERATION_EQUALS,
																ObjectEntities.USER_ENTITY_CODE,
																UserWrapper.COLUMN_LOGIN);
			Collection collection = AdministrationStorableObjectPool.getStorableObjectsByCondition(condition, true);
			if (collection.isEmpty())
				throw new AMFICOMRemoteException(ErrorCode.ERROR_LOGIN_NOT_FOUND, CompletionStatus.COMPLETED_NO, "User login '"
						+ userLogin + "' not found.");
			Identifier id = ((User) collection.iterator().next()).getId();
			return (Identifier_Transferable) id.getTransferable();
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Identifier_Transferable reverseLookupUserName(final String userName)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMServerImpl.reverseLookupUserName | userName = " + userName, Log.DEBUGLEVEL07);
		try {
			TypicalCondition condition = new TypicalCondition(userName, OperationSort.OPERATION_EQUALS,
																ObjectEntities.USER_ENTITY_CODE,
																StorableObjectWrapper.COLUMN_NAME);
			Collection collection = AdministrationStorableObjectPool.getStorableObjectsByCondition(condition, true);
			if (collection.isEmpty())
				throw new AMFICOMRemoteException(ErrorCode.ERROR_LOGIN_NOT_FOUND, CompletionStatus.COMPLETED_NO, "User name '"
						+ userName + "' not found.");
			Identifier id = ((User) collection.iterator().next()).getId();
			return (Identifier_Transferable) id.getTransferable();
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
				.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}



    // Delete methods

	public void delete(final Identifier_Transferable id,
			final AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		final Identifier id1 = new Identifier(id);
		Log.debugMessage("CMServerImpl.delete | trying to delete object '" + id1 //$NON-NLS-1$
				+ "' as requested by user '" + (new AccessIdentity(accessIdentifier)).getUserId() + "'", Log.DEBUGLEVEL07);  //$NON-NLS-1$//$NON-NLS-2$
		final short entityCode = id1.getMajor();
		if (ObjectGroupEntities.isInGeneralGroup(entityCode))
			GeneralStorableObjectPool.delete(id1);
		else if (ObjectGroupEntities.isInAdministrationGroup(entityCode))
			AdministrationStorableObjectPool.delete(id1);
		else if (ObjectGroupEntities.isInConfigurationGroup(entityCode))
			ConfigurationStorableObjectPool.delete(id1);
		else if (ObjectGroupEntities.isInMeasurementGroup(entityCode))
			MeasurementStorableObjectPool.delete(id1);
		else
			Log.errorMessage("CMServerImpl.delete | Wrong entity code: " + entityCode); //$NON-NLS-1$

	}

	public void deleteList(Identifier_Transferable[] id_Transferables, AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMServerImpl.deleteList | Trying to delete " + id_Transferables.length //$NON-NLS-1$
				+ " objects on request of user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07); //$NON-NLS-1$ //$NON-NLS-2$

		Set ids = Identifier.fromTransferables(id_Transferables);
		Set generalList = new HashSet(id_Transferables.length);
		Set administrationList = new HashSet(id_Transferables.length);
		Set configurationList = new HashSet(id_Transferables.length);
		Set measurementList = new HashSet(id_Transferables.length);
		Identifier id;
		for (Iterator it = ids.iterator(); it.hasNext();) {
			id = (Identifier) it.next();
			short entityCode = id.getMajor();
			if (ObjectGroupEntities.isInGeneralGroup(entityCode))
				generalList.add(id);
			else if (ObjectGroupEntities.isInAdministrationGroup(entityCode))
				administrationList.add(id);
			else if (ObjectGroupEntities.isInConfigurationGroup(entityCode))
				configurationList.add(id);
			else if (ObjectGroupEntities.isInMeasurementGroup(entityCode))
				measurementList.add(id);
			else
				Log.errorMessage("CMServerImpl.deleteList | Wrong entity code: " + entityCode); //$NON-NLS-1$
		}
		GeneralStorableObjectPool.delete(generalList);
		AdministrationStorableObjectPool.delete(administrationList);
		ConfigurationStorableObjectPool.delete(configurationList);
		MeasurementStorableObjectPool.delete(measurementList);
	}

///////////////////////////////////////// Identifier Generator 	//////////////////////////////////////////////////

	public Identifier_Transferable getGeneratedIdentifier(short entityCode) throws AMFICOMRemoteException {
		try {
			Log.debugMessage("CMServerImpl.getGeneratedIdentifier | generate new Identifer for "
					+ ObjectEntities.codeToString(entityCode), Log.DEBUGLEVEL07);
			Identifier identifier = IdentifierGenerator.generateIdentifier(entityCode);
			return (Identifier_Transferable) identifier.getTransferable();
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
					CompletionStatus.COMPLETED_NO,
					"Illegal object entity: '" + ObjectEntities.codeToString(entityCode) + "'");
		}
		catch (IdentifierGenerationException ige) {
			Log.errorException(ige);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE,
					CompletionStatus.COMPLETED_NO,
					"Cannot create major/minor entries of identifier for entity: '"
							+ ObjectEntities.codeToString(entityCode)
							+ "' -- "
							+ ige.getMessage());
		}
	}

	public Identifier_Transferable[] getGeneratedIdentifierRange(short entityCode, int size)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMServerImpl.getGeneratedIdentifierRange | generate new Identifer range "
				+ size + " for " + ObjectEntities.codeToString(entityCode), Log.DEBUGLEVEL07);
		try {
			Identifier[] identifiers = IdentifierGenerator.generateIdentifierRange(entityCode, size);
			Identifier_Transferable[] identifiersT = new Identifier_Transferable[identifiers.length];
			for (int i = 0; i < identifiersT.length; i++)
				identifiersT[i] = (Identifier_Transferable) identifiers[i].getTransferable();
			return identifiersT;
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
					CompletionStatus.COMPLETED_NO,
					"Illegal object entity: '" + ObjectEntities.codeToString(entityCode) + "'");
		}
		catch (IdentifierGenerationException ige) {
			Log.errorException(ige);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE,
					CompletionStatus.COMPLETED_NO,
					"Cannot create major/minor entries of identifier for entity: '"
							+ ObjectEntities.codeToString(entityCode) + "' -- " + ige.getMessage());
		}
	}

}
