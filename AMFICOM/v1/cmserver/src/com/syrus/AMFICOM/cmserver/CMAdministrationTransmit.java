/*
 * $Id: CMAdministrationTransmit.java,v 1.9 2005/03/29 20:24:27 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
import com.syrus.AMFICOM.administration.corba.MCM_Transferable;
import com.syrus.AMFICOM.administration.corba.Server_Transferable;
import com.syrus.AMFICOM.administration.corba.User_Transferable;
import com.syrus.AMFICOM.general.AccessIdentity;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.9 $, $Date: 2005/03/29 20:24:27 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */

public abstract class CMAdministrationTransmit extends CMGeneralTransmit {

	private static final long serialVersionUID = 5322471674445475587L;

	public User_Transferable transmitUser(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMAdministrationTransmit.transmitUser | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			User user = (User) AdministrationStorableObjectPool.getStorableObject(id, true);
			return (User_Transferable) user.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Domain_Transferable transmitDomain(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMAdministrationTransmit.transmitDomain | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(id, true);
			return (Domain_Transferable) domain.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Server_Transferable transmitServer(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMAdministrationTransmit.transmitServer | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			Server server = (Server) AdministrationStorableObjectPool.getStorableObject(id, true);
			return (Server_Transferable) server.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MCM_Transferable transmitMCM(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMAdministrationTransmit.transmitMCM | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			MCM mcm = (MCM) AdministrationStorableObjectPool.getStorableObject(id, true);
			return (MCM_Transferable) mcm.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}






	public User_Transferable[] transmitUsers(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMAdministrationTransmit.transmitUsers | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Collection ids = new HashSet(identifier_Transferables.length);
		for (int i = 0; i < identifier_Transferables.length; i++)
			ids.add(new Identifier(identifier_Transferables[i]));

		Collection objects = null;
		try {
			objects = AdministrationStorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		User_Transferable[] transferables = new User_Transferable[objects.size()];
		int i = 0;
		User user;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			user = (User) it.next();
			transferables[i] = (User_Transferable) user.getTransferable();
		}
		return transferables;
	}

	public Domain_Transferable[] transmitDomains(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMAdministrationTransmit.transmitDomains | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Collection ids = new HashSet(identifier_Transferables.length);
		for (int i = 0; i < identifier_Transferables.length; i++)
			ids.add(new Identifier(identifier_Transferables[i]));

		Collection objects = null;
		try {
			objects = AdministrationStorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		Domain_Transferable[] transferables = new Domain_Transferable[objects.size()];
		int i = 0;
		Domain domain;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			domain = (Domain) it.next();
			transferables[i] = (Domain_Transferable) domain.getTransferable();
		}
		return transferables;
	}

	public Server_Transferable[] transmitServers(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMAdministrationTransmit.transmitServers | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Collection ids = new HashSet(identifier_Transferables.length);
		for (int i = 0; i < identifier_Transferables.length; i++)
			ids.add(new Identifier(identifier_Transferables[i]));

		Collection objects = null;
		try {
			objects = AdministrationStorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		Server_Transferable[] transferables = new Server_Transferable[objects.size()];
		int i = 0;
		Server server;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			server = (Server) it.next();
			transferables[i] = (Server_Transferable) server.getTransferable();
		}
		return transferables;
	}

	public MCM_Transferable[] transmitMCMs(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMAdministrationTransmit.transmitMCMs | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Collection ids = new HashSet(identifier_Transferables.length);
		for (int i = 0; i < identifier_Transferables.length; i++)
			ids.add(new Identifier(identifier_Transferables[i]));

		Collection objects = null;
		try {
			objects = AdministrationStorableObjectPool.getStorableObjects(ids, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		MCM_Transferable[] transferables = new MCM_Transferable[objects.size()];
		int i = 0;
		MCM mcm;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			mcm = (MCM) it.next();
			transferables[i] = (MCM_Transferable) mcm.getTransferable();
		}
		return transferables;
	}





	public User_Transferable[] transmitUsersButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMAdministrationTransmit.transmitUsersButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Collection ids = new HashSet(identifier_Transferables.length);
		for (int i = 0; i < identifier_Transferables.length; i++)
			ids.add(new Identifier(identifier_Transferables[i]));

		Collection objects = null;
		try {
			objects = AdministrationStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(ObjectEntities.USER_ENTITY_CODE),
					true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		User_Transferable[] transferables = new User_Transferable[objects.size()];
		int i = 0;
		User user;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			user = (User) it.next();
			transferables[i] = (User_Transferable) user.getTransferable();
		}
		return transferables;
	}

	public Domain_Transferable[] transmitDomainsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMAdministrationTransmit.transmitDomainsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Collection ids = new HashSet(identifier_Transferables.length);
		for (int i = 0; i < identifier_Transferables.length; i++)
			ids.add(new Identifier(identifier_Transferables[i]));

		Collection objects = null;
		try {
			objects = AdministrationStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(ObjectEntities.DOMAIN_ENTITY_CODE),
					true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		Domain_Transferable[] transferables = new Domain_Transferable[objects.size()];
		int i = 0;
		Domain domain;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			domain = (Domain) it.next();
			transferables[i] = (Domain_Transferable) domain.getTransferable();
		}
		return transferables;
	}

	public Server_Transferable[] transmitServersButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMAdministrationTransmit.transmitServersButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Collection ids = new HashSet(identifier_Transferables.length);
		for (int i = 0; i < identifier_Transferables.length; i++)
			ids.add(new Identifier(identifier_Transferables[i]));

		Collection objects = null;
		try {
			objects = AdministrationStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(ObjectEntities.SERVER_ENTITY_CODE),
					true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		Server_Transferable[] transferables = new Server_Transferable[objects.size()];
		int i = 0;
		Server server;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			server = (Server) it.next();
			transferables[i] = (Server_Transferable) server.getTransferable();
		}
		return transferables;
	}

	public MCM_Transferable[] transmitMCMsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMAdministrationTransmit.transmitMCMsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Collection ids = new HashSet(identifier_Transferables.length);
		for (int i = 0; i < identifier_Transferables.length; i++)
			ids.add(new Identifier(identifier_Transferables[i]));

		Collection objects = null;
		try {
			objects = AdministrationStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(ObjectEntities.MCM_ENTITY_CODE),
					true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		MCM_Transferable[] transferables = new MCM_Transferable[objects.size()];
		int i = 0;
		MCM mcm;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			mcm = (MCM) it.next();
			transferables[i] = (MCM_Transferable) mcm.getTransferable();
		}
		return transferables;
	}






	public User_Transferable[] transmitUsersButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMAdministrationTransmit.transmitUsersButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Collection ids = new HashSet(identifier_Transferables.length);
		for (int i = 0; i < identifier_Transferables.length; i++)
			ids.add(new Identifier(identifier_Transferables[i]));

		StorableObjectCondition condition = null;
		try {
			condition = StorableObjectConditionBuilder.restoreCondition(condition_Transferable);
		}
		catch (IllegalDataException ide) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_DATA,
					CompletionStatus.COMPLETED_NO,
					"Cannot restore condition -- " + ide.getMessage());
		}

		Collection objects = null;
		try {
			objects = AdministrationStorableObjectPool.getStorableObjectsByConditionButIds(ids, condition, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		User_Transferable[] transferables = new User_Transferable[objects.size()];
		int i = 0;
		User user;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			user = (User) it.next();
			transferables[i] = (User_Transferable) user.getTransferable();
		}
		return transferables;
	}

	public Domain_Transferable[] transmitDomainsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMAdministrationTransmit.transmitDomainsButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Collection ids = new HashSet(identifier_Transferables.length);
		for (int i = 0; i < identifier_Transferables.length; i++)
			ids.add(new Identifier(identifier_Transferables[i]));

		StorableObjectCondition condition = null;
		try {
			condition = StorableObjectConditionBuilder.restoreCondition(condition_Transferable);
		}
		catch (IllegalDataException ide) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_DATA,
					CompletionStatus.COMPLETED_NO,
					"Cannot restore condition -- " + ide.getMessage());
		}

		Collection objects = null;
		try {
			objects = AdministrationStorableObjectPool.getStorableObjectsByConditionButIds(ids, condition, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		Domain_Transferable[] transferables = new Domain_Transferable[objects.size()];
		int i = 0;
		Domain domain;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			domain = (Domain) it.next();
			transferables[i] = (Domain_Transferable) domain.getTransferable();
		}
		return transferables;
	}

	public Server_Transferable[] transmitServersButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMAdministrationTransmit.transmitServersButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Collection ids = new HashSet(identifier_Transferables.length);
		for (int i = 0; i < identifier_Transferables.length; i++)
			ids.add(new Identifier(identifier_Transferables[i]));

		StorableObjectCondition condition = null;
		try {
			condition = StorableObjectConditionBuilder.restoreCondition(condition_Transferable);
		}
		catch (IllegalDataException ide) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_DATA,
					CompletionStatus.COMPLETED_NO,
					"Cannot restore condition -- " + ide.getMessage());
		}

		Collection objects = null;
		try {
			objects = AdministrationStorableObjectPool.getStorableObjectsByConditionButIds(ids, condition, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		Server_Transferable[] transferables = new Server_Transferable[objects.size()];
		int i = 0;
		Server server;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			server = (Server) it.next();
			transferables[i] = (Server_Transferable) server.getTransferable();
		}
		return transferables;
	}

	public MCM_Transferable[] transmitMCMsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMAdministrationTransmit.transmitMCMsButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		Collection ids = new HashSet(identifier_Transferables.length);
		for (int i = 0; i < identifier_Transferables.length; i++)
			ids.add(new Identifier(identifier_Transferables[i]));

		StorableObjectCondition condition = null;
		try {
			condition = StorableObjectConditionBuilder.restoreCondition(condition_Transferable);
		}
		catch (IllegalDataException ide) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_DATA,
					CompletionStatus.COMPLETED_NO,
					"Cannot restore condition -- " + ide.getMessage());
		}

		Collection objects = null;
		try {
			objects = AdministrationStorableObjectPool.getStorableObjectsByConditionButIds(ids, condition, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}

		MCM_Transferable[] transferables = new MCM_Transferable[objects.size()];
		int i = 0;
		MCM mcm;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			mcm = (MCM) it.next();
			transferables[i] = (MCM_Transferable) mcm.getTransferable();
		}
		return transferables;
	}





	// Refresh objects from a pool
	public Identifier_Transferable[] transmitRefreshedAdministrationObjects(StorableObject_Transferable[] storableObjects_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("Refreshing for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			Map storableObjectsTMap = new HashMap();
			for (int i = 0; i < storableObjects_Transferables.length; i++)
				storableObjectsTMap.put(new Identifier(storableObjects_Transferables[i].id), storableObjects_Transferables[i]);

			AdministrationStorableObjectPool.refresh();
			Collection storableObjects = AdministrationStorableObjectPool.getStorableObjects(storableObjectsTMap.keySet(), true);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {
				StorableObject so = (StorableObject) it.next();
				StorableObject_Transferable sot = (StorableObject_Transferable) storableObjectsTMap.get(so.getId());
				//Remove objects with older versions as well as objects with the same versions -- not only with older ones!
				if (!so.hasNewerVersion(sot.version))
					it.remove();
			}

			int i = 0;
			Identifier_Transferable[] idsT = new Identifier_Transferable[storableObjects.size()];
			for (Iterator it = storableObjects.iterator(); it.hasNext(); i++)
				idsT[i] = (Identifier_Transferable) ((StorableObject) it.next()).getId().getTransferable();
			Log.debugMessage("CMServer.transmitRefreshedAdministrationObjects | return " + idsT.length + " item(s)", Log.DEBUGLEVEL05);
			return idsT;
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
	}
}
