/*
 * $Id: CMServerImpl.java,v 1.88 2005/02/08 09:51:41 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StringFieldCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StringFieldSort;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.88 $, $Date: 2005/02/08 09:51:41 $
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
			List list = AdministrationStorableObjectPool.getStorableObjectsByCondition(new StringFieldCondition(domainName, ObjectEntities.DOMAIN_ENTITY_CODE), true);
			if (list.isEmpty())
				throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, "list is empty");
			Identifier id = ( (Domain)list.get(0) ).getId();
			return (Identifier_Transferable)id.getTransferable();
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

	public Identifier_Transferable reverseLookupUserLogin(String userLogin) throws AMFICOMRemoteException {
		try {
			Log.debugMessage("CMServerImpl.reverseLookupUserLogin | userLogin " + userLogin, Log.DEBUGLEVEL07);
			StringFieldCondition stringFieldCondition = new StringFieldCondition(userLogin,
					ObjectEntities.USER_ENTITY_CODE,
					StringFieldSort.STRINGSORT_USERLOGIN);
			List list = AdministrationStorableObjectPool.getStorableObjectsByCondition(stringFieldCondition, true);
			Identifier id = ((User) list.get(0)).getId();
			return (Identifier_Transferable)id.getTransferable();
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

	public Identifier_Transferable reverseLookupUserName(final String userName)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMServerImpl.reverseLookupUserName | userName = " + userName, Log.DEBUGLEVEL07);
		try {
			StringFieldCondition stringFieldCondition = new StringFieldCondition(userName,
					ObjectEntities.USER_ENTITY_CODE,
					StringFieldSort.STRINGSORT_USERNAME);
			List list = AdministrationStorableObjectPool.getStorableObjectsByCondition(stringFieldCondition, true);
			Identifier id = ((User) list.get(0)).getId();
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

	public void delete(Identifier_Transferable id_Transferable, AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMServerImpl.delete | trying to delete... ", Log.DEBUGLEVEL03);
		Identifier id = new Identifier(id_Transferable);
		short entityCode = id.getMajor();
		try {
			if (ObjectGroupEntities.isInGeneralGroup(entityCode))
				GeneralStorableObjectPool.delete(id);
			if (ObjectGroupEntities.isInAdministrationGroup(entityCode))
				AdministrationStorableObjectPool.delete(id);
			if (ObjectGroupEntities.isInConfigurationGroup(entityCode))
				ConfigurationStorableObjectPool.delete(id);
			if (ObjectGroupEntities.isInMeasurementGroup(entityCode))
				MeasurementStorableObjectPool.delete(id);
			Log.errorMessage("CMServerImpl.delete | Wrong entity code: " + entityCode);
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}

	}

	public void deleteList(Identifier_Transferable[] id_Transferables, AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMServerImpl.deleteList | Trying to delete... ", Log.DEBUGLEVEL03);
		List idList = new ArrayList(id_Transferables.length);
		List generalList = new ArrayList(id_Transferables.length);
		List administrationList = new ArrayList(id_Transferables.length);
		List configurationList = new ArrayList(id_Transferables.length);
		List measurementList = new ArrayList(id_Transferables.length);
		for (int i = 0; i < id_Transferables.length; i++) {
			idList.add(new Identifier(id_Transferables[i]));
		}
		for (Iterator iter = idList.iterator(); iter.hasNext();) {
			Identifier id = (Identifier) iter.next();
			short entityCode = id.getMajor();
			if (ObjectGroupEntities.isInGeneralGroup(entityCode))
				generalList.add(id);
			if (ObjectGroupEntities.isInAdministrationGroup(entityCode))
				administrationList.add(id);
			if (ObjectGroupEntities.isInConfigurationGroup(entityCode))
				configurationList.add(id);
			if (ObjectGroupEntities.isInMeasurementGroup(entityCode))
				measurementList.add(id);
			Log.errorMessage("CMServerImpl.deleteList | Wrong entity code: " + entityCode);
		}
		try {
			GeneralStorableObjectPool.delete(generalList);
			AdministrationStorableObjectPool.delete(administrationList);
			ConfigurationStorableObjectPool.delete(configurationList);
			MeasurementStorableObjectPool.delete(measurementList);
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		}
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
