/*
 * $Id: CMAdministrationTransmit.java,v 1.8 2005/02/25 09:16:07 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.cmserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
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
 * @version $Revision: 1.8 $, $Date: 2005/02/25 09:16:07 $
 * @author $Author: bob $
 * @module cmserver_v1
 */

public abstract class CMAdministrationTransmit extends CMGeneralTransmit {

	private static final long	serialVersionUID	= 5322471674445475587L;

	public User_Transferable transmitUser(	Identifier_Transferable id_Transferable,
											AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(id_Transferable);
		Log.debugMessage("CMAdministrationTransmit.User | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			User user = (User) AdministrationStorableObjectPool.getStorableObject(id, true);
			return (User_Transferable) user.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe
					.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Domain_Transferable transmitDomain(	Identifier_Transferable identifier_Transferable,
												AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMAdministrationTransmit.transmitDomain | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(id, true);
			return (Domain_Transferable) domain.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe
					.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Server_Transferable transmitServer(	Identifier_Transferable id_Transferable,
												AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(id_Transferable);
		Log.debugMessage("CMAdministrationTransmit.Server | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Server server = (Server) AdministrationStorableObjectPool.getStorableObject(id, true);
			return (Server_Transferable) server.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe
					.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MCM_Transferable transmitMCM(Identifier_Transferable id_Transferable,
										AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Identifier id = new Identifier(id_Transferable);
		Log.debugMessage("CMAdministrationTransmit.MCM | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			MCM mcm = (MCM) AdministrationStorableObjectPool.getStorableObject(id, true);
			return (MCM_Transferable) mcm.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe
					.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public User_Transferable[] transmitUsers(	Identifier_Transferable[] ids_Transferable,
												AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Log.debugMessage("CMAdministrationTransmit.transmitUsers | requiere "
					+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			Collection collection;
			if (ids_Transferable.length > 0) {
				List idsList = new ArrayList(ids_Transferable.length);
				for (int i = 0; i < ids_Transferable.length; i++)
					idsList.add(new Identifier(ids_Transferable[i]));
				collection = AdministrationStorableObjectPool.getStorableObjects(idsList, true);
			} else
				collection = AdministrationStorableObjectPool.getStorableObjectsByCondition(
					new EquivalentCondition(ObjectEntities.USER_ENTITY_CODE), true);

			User_Transferable[] transferables = new User_Transferable[collection.size()];
			int i = 0;
			for (Iterator it = collection.iterator(); it.hasNext(); i++) {
				User user = (User) it.next();
				transferables[i] = (User_Transferable) user.getTransferable();
			}
			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public User_Transferable[] transmitUsersButIdsCondition(Identifier_Transferable[] ids_Transferable,
															AccessIdentifier_Transferable accessIdentifier,
															StorableObjectCondition_Transferable condition_Transferable)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Log.debugMessage("CMAdministrationTransmit.transmitUsersButIds | requiere "
					+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			Collection collection;
			StorableObjectCondition condition = StorableObjectConditionBuilder.restoreCondition(condition_Transferable);
			if (ids_Transferable.length > 0) {
				List idsList = new ArrayList(ids_Transferable.length);
				for (int i = 0; i < ids_Transferable.length; i++)
					idsList.add(new Identifier(ids_Transferable[i]));
				collection = AdministrationStorableObjectPool.getStorableObjectsByConditionButIds(idsList, condition, true);
			} else {
				collection = AdministrationStorableObjectPool.getStorableObjectsByCondition(condition, true);
			}

			User_Transferable[] transferables = new User_Transferable[collection.size()];
			int i = 0;
			for (Iterator it = collection.iterator(); it.hasNext(); i++) {
				User user = (User) it.next();
				transferables[i] = (User_Transferable) user.getTransferable();
			}
			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Domain_Transferable[] transmitDomainsButIds(	Identifier_Transferable[] ids_Transferable,
														AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Log.debugMessage("CMAdministrationTransmit.transmitDomainsButIds | requiere "
					+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			Collection collection;
			if (ids_Transferable.length > 0) {
				List idsList = new ArrayList(ids_Transferable.length);
				for (int i = 0; i < ids_Transferable.length; i++)
					idsList.add(new Identifier(ids_Transferable[i]));
				collection = AdministrationStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
					new EquivalentCondition(ObjectEntities.DOMAIN_ENTITY_CODE), true);
			} else
				collection = AdministrationStorableObjectPool.getStorableObjectsByCondition(
					new EquivalentCondition(ObjectEntities.DOMAIN_ENTITY_CODE), true);

			Domain_Transferable[] transferables = new Domain_Transferable[collection.size()];
			int i = 0;
			for (Iterator it = collection.iterator(); it.hasNext(); i++) {
				Domain domain2 = (Domain) it.next();
				transferables[i] = (Domain_Transferable) domain2.getTransferable();
			}
			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Domain_Transferable[] transmitDomainsButIdsCondition(Identifier_Transferable[] ids_Transferable,
																AccessIdentifier_Transferable accessIdentifier,
																StorableObjectCondition_Transferable condition_Transferable)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMAdministrationTransmit.transmitDomainsButIdsCondition | requiere "
				+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length)) + " item(s) ",
			Log.DEBUGLEVEL07);
		try {
			Collection collection;
			StorableObjectCondition condition = StorableObjectConditionBuilder.restoreCondition(condition_Transferable);
			if (ids_Transferable.length > 0) {
				List idsList = new ArrayList(ids_Transferable.length);
				for (int i = 0; i < ids_Transferable.length; i++)
					idsList.add(new Identifier(ids_Transferable[i]));
				collection = AdministrationStorableObjectPool.getStorableObjectsByConditionButIds(idsList, condition, true);
			} else
				collection = AdministrationStorableObjectPool.getStorableObjectsByCondition(condition, true);

			Domain_Transferable[] transferables = new Domain_Transferable[collection.size()];
			int i = 0;
			for (Iterator it = collection.iterator(); it.hasNext(); i++) {
				Domain domain2 = (Domain) it.next();
				transferables[i] = (Domain_Transferable) domain2.getTransferable();
			}
			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Server_Transferable[] transmitServers(	Identifier_Transferable[] ids_Transferable,
													AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Log.debugMessage("CMAdministrationTransmit.transmitServers | requiere "
					+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			Collection collection;
			if (ids_Transferable.length > 0) {
				List idsList = new ArrayList(ids_Transferable.length);
				for (int i = 0; i < ids_Transferable.length; i++)
					idsList.add(new Identifier(ids_Transferable[i]));
				collection = AdministrationStorableObjectPool.getStorableObjects(idsList, true);
			} else
				collection = AdministrationStorableObjectPool.getStorableObjectsByCondition(
					new EquivalentCondition(ObjectEntities.SERVER_ENTITY_CODE), true);

			Server_Transferable[] transferables = new Server_Transferable[collection.size()];
			int i = 0;
			for (Iterator it = collection.iterator(); it.hasNext(); i++) {
				Server server = (Server) it.next();
				transferables[i] = (Server_Transferable) server.getTransferable();
			}
			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Server_Transferable[] transmitServersButIds(	Identifier_Transferable[] ids_Transferable,
														AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Log.debugMessage("CMAdministrationTransmit.transmitServersButIds | requiere "
					+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			Collection collection;
			if (ids_Transferable.length > 0) {
				List idsList = new ArrayList(ids_Transferable.length);
				for (int i = 0; i < ids_Transferable.length; i++)
					idsList.add(new Identifier(ids_Transferable[i]));
				collection = AdministrationStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
					new EquivalentCondition(ObjectEntities.SERVER_ENTITY_CODE), true);
			} else
				collection = AdministrationStorableObjectPool.getStorableObjectsByCondition(
					new EquivalentCondition(ObjectEntities.SERVER_ENTITY_CODE), true);

			Server_Transferable[] transferables = new Server_Transferable[collection.size()];
			int i = 0;
			for (Iterator it = collection.iterator(); it.hasNext(); i++) {
				Server server = (Server) it.next();
				transferables[i] = (Server_Transferable) server.getTransferable();
			}
			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Server_Transferable[] transmitServersButIdsCondition(Identifier_Transferable[] ids_Transferable,
																AccessIdentifier_Transferable accessIdentifier,
																StorableObjectCondition_Transferable condition_Transferable)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMAdministrationTransmit.transmitServersButIdsCondition | requiere "
				+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length)) + " item(s)",
			Log.DEBUGLEVEL07);
		try {
			Collection collection;
			StorableObjectCondition condition = StorableObjectConditionBuilder.restoreCondition(condition_Transferable);
			if (ids_Transferable.length > 0) {
				List idsList = new ArrayList(ids_Transferable.length);
				for (int i = 0; i < ids_Transferable.length; i++)
					idsList.add(new Identifier(ids_Transferable[i]));
				collection = AdministrationStorableObjectPool.getStorableObjectsByConditionButIds(idsList, condition, true);
			} else
				collection = AdministrationStorableObjectPool.getStorableObjectsByCondition(condition, true);

			Server_Transferable[] transferables = new Server_Transferable[collection.size()];
			int i = 0;
			for (Iterator it = collection.iterator(); it.hasNext(); i++) {
				Server server = (Server) it.next();
				transferables[i] = (Server_Transferable) server.getTransferable();
			}
			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MCM_Transferable[] transmitMCMs(	Identifier_Transferable[] ids_Transferable,
											AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Log.debugMessage("CMAdministrationTransmit.transmitMCMs | requiere "
					+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			Collection collection;
			if (ids_Transferable.length > 0) {
				List idsList = new ArrayList(ids_Transferable.length);
				for (int i = 0; i < ids_Transferable.length; i++)
					idsList.add(new Identifier(ids_Transferable[i]));
				collection = AdministrationStorableObjectPool.getStorableObjects(idsList, true);
			} else
				collection = AdministrationStorableObjectPool.getStorableObjectsByCondition(
					new EquivalentCondition(ObjectEntities.MCM_ENTITY_CODE), true);

			MCM_Transferable[] transferables = new MCM_Transferable[collection.size()];
			int i = 0;
			for (Iterator it = collection.iterator(); it.hasNext(); i++) {
				MCM mcm = (MCM) it.next();
				transferables[i] = (MCM_Transferable) mcm.getTransferable();
			}
			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MCM_Transferable[] transmitMCMsButIds(	Identifier_Transferable[] ids_Transferable,
													AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Log.debugMessage("CMAdministrationTransmit.transmitMCMsButIds | requiere "
					+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			Collection collection;
			if (ids_Transferable.length > 0) {
				List idsList = new ArrayList(ids_Transferable.length);
				for (int i = 0; i < ids_Transferable.length; i++)
					idsList.add(new Identifier(ids_Transferable[i]));
				collection = AdministrationStorableObjectPool.getStorableObjectsByConditionButIds(idsList,
					new EquivalentCondition(ObjectEntities.MCM_ENTITY_CODE), true);
			} else
				collection = AdministrationStorableObjectPool.getStorableObjectsByCondition(
					new EquivalentCondition(ObjectEntities.MCM_ENTITY_CODE), true);

			MCM_Transferable[] transferables = new MCM_Transferable[collection.size()];
			int i = 0;
			for (Iterator it = collection.iterator(); it.hasNext(); i++) {
				MCM mcm = (MCM) it.next();
				transferables[i] = (MCM_Transferable) mcm.getTransferable();
			}
			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MCM_Transferable[] transmitMCMsButIdsCondition(	Identifier_Transferable[] ids_Transferable,
															AccessIdentifier_Transferable accessIdentifier,
															StorableObjectCondition_Transferable condition_Transferable)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMAdministrationTransmit.transmitMCMsButIdsCondition | requiere "
				+ (ids_Transferable.length == 0 ? "all" : Integer.toString(ids_Transferable.length)) + " item(s) ",
			Log.DEBUGLEVEL07);
		try {
			Collection collection;
			StorableObjectCondition condition = StorableObjectConditionBuilder.restoreCondition(condition_Transferable);
			if (ids_Transferable.length > 0) {
				List idsList = new ArrayList(ids_Transferable.length);
				for (int i = 0; i < ids_Transferable.length; i++)
					idsList.add(new Identifier(ids_Transferable[i]));
				collection = AdministrationStorableObjectPool.getStorableObjectsByConditionButIds(idsList, condition, true);
			} else
				collection = AdministrationStorableObjectPool.getStorableObjectsByCondition(condition, true);

			MCM_Transferable[] transferables = new MCM_Transferable[collection.size()];
			int i = 0;
			for (Iterator it = collection.iterator(); it.hasNext(); i++) {
				MCM mcm = (MCM) it.next();
				transferables[i] = (MCM_Transferable) mcm.getTransferable();
			}
			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Domain_Transferable[] transmitDomains(	Identifier_Transferable[] identifier_Transferables,
													AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		List idsList = new ArrayList();

		Log.debugMessage("CMAdministrationTransmit.transmitDomains | requiere "
				+ (identifier_Transferables.length == 0 ? "all" : Integer.toString(identifier_Transferables.length))
				+ " item(s)", Log.DEBUGLEVEL07);
		try {
			Collection collection = null;
			if (identifier_Transferables.length > 0) {
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));
				collection = AdministrationStorableObjectPool.getStorableObjects(idsList, true);
			}

			if (collection == null)
				collection = AdministrationStorableObjectPool.getStorableObjectsByCondition(
					new EquivalentCondition(ObjectEntities.DOMAIN_ENTITY_CODE), true);

			int i = 0;
			Domain_Transferable[] transferables = new Domain_Transferable[collection.size()];
			for (Iterator it = collection.iterator(); it.hasNext(); i++) {
				Domain domain2 = (Domain) it.next();
				transferables[i] = (Domain_Transferable) domain2.getTransferable();

			}

			return transferables;
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe
					.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	// Refresh objects from a pool
	public Identifier_Transferable[] transmitRefreshedAdministrationObjects(StorableObject_Transferable[] storableObjects_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
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
