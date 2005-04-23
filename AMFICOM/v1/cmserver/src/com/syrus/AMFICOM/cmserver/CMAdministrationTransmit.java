/*
 * $Id: CMAdministrationTransmit.java,v 1.16 2005/04/23 13:36:32 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
import com.syrus.AMFICOM.general.corba.AccessIdentity_Transferable;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.16 $, $Date: 2005/04/23 13:36:32 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */

public abstract class CMAdministrationTransmit extends CMGeneralTransmit {

	private static final long serialVersionUID = 5322471674445475587L;

	public User_Transferable transmitUser(Identifier_Transferable identifier_Transferable,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
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
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
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
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
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
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
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
			AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMAdministrationTransmit.transmitUsers | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjects(identifier_Transferables);

			User_Transferable[] transferables = new User_Transferable[objects.size()];
			int i = 0;
			User user;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				user = (User) it.next();
				transferables[i] = (User_Transferable) user.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Domain_Transferable[] transmitDomains(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMAdministrationTransmit.transmitDomains | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjects(identifier_Transferables);

			Domain_Transferable[] transferables = new Domain_Transferable[objects.size()];
			int i = 0;
			Domain domain;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				domain = (Domain) it.next();
				transferables[i] = (Domain_Transferable) domain.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Server_Transferable[] transmitServers(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMAdministrationTransmit.transmitServers | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjects(identifier_Transferables);

			Server_Transferable[] transferables = new Server_Transferable[objects.size()];
			int i = 0;
			Server server;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				server = (Server) it.next();
				transferables[i] = (Server_Transferable) server.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MCM_Transferable[] transmitMCMs(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMAdministrationTransmit.transmitMCMs | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjects(identifier_Transferables);

			MCM_Transferable[] transferables = new MCM_Transferable[objects.size()];
			int i = 0;
			MCM mcm;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				mcm = (MCM) it.next();
				transferables[i] = (MCM_Transferable) mcm.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}


	private Set getObjects(Identifier_Transferable[] identifier_Transferables) throws AMFICOMRemoteException {
		try {
			Set ids = Identifier.fromTransferables(identifier_Transferables);
			Set objects = AdministrationStorableObjectPool.getStorableObjects(ids, true);
			return objects;
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}





	public User_Transferable[] transmitUsersButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMAdministrationTransmit.transmitUsersButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.USER_ENTITY_CODE);

			User_Transferable[] transferables = new User_Transferable[objects.size()];
			int i = 0;
			User user;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				user = (User) it.next();
				transferables[i] = (User_Transferable) user.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Domain_Transferable[] transmitDomainsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMAdministrationTransmit.transmitDomainsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.DOMAIN_ENTITY_CODE);

			Domain_Transferable[] transferables = new Domain_Transferable[objects.size()];
			int i = 0;
			Domain domain;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				domain = (Domain) it.next();
				transferables[i] = (Domain_Transferable) domain.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Server_Transferable[] transmitServersButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMAdministrationTransmit.transmitServersButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.SERVER_ENTITY_CODE);

			Server_Transferable[] transferables = new Server_Transferable[objects.size()];
			int i = 0;
			Server server;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				server = (Server) it.next();
				transferables[i] = (Server_Transferable) server.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MCM_Transferable[] transmitMCMsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMAdministrationTransmit.transmitMCMsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.MCM_ENTITY_CODE);

			MCM_Transferable[] transferables = new MCM_Transferable[objects.size()];
			int i = 0;
			MCM mcm;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				mcm = (MCM) it.next();
				transferables[i] = (MCM_Transferable) mcm.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}


	private Set getObjectsButIds(Identifier_Transferable[] identifier_Transferables, short entityCode) throws AMFICOMRemoteException {
		try {
			Set ids = Identifier.fromTransferables(identifier_Transferables);
			Set objects = AdministrationStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(entityCode),
					true);
			return objects;
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}





	public User_Transferable[] transmitUsersButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMAdministrationTransmit.transmitUsersButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

			User_Transferable[] transferables = new User_Transferable[objects.size()];
			int i = 0;
			User user;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				user = (User) it.next();
				transferables[i] = (User_Transferable) user.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Domain_Transferable[] transmitDomainsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMAdministrationTransmit.transmitDomainsButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

			Domain_Transferable[] transferables = new Domain_Transferable[objects.size()];
			int i = 0;
			Domain domain;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				domain = (Domain) it.next();
				transferables[i] = (Domain_Transferable) domain.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Server_Transferable[] transmitServersButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMAdministrationTransmit.transmitServersButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

			Server_Transferable[] transferables = new Server_Transferable[objects.size()];
			int i = 0;
			Server server;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				server = (Server) it.next();
				transferables[i] = (Server_Transferable) server.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MCM_Transferable[] transmitMCMsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentity_Transferable accessIdentityT,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMAdministrationTransmit.transmitMCMsButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		try {
			Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

			MCM_Transferable[] transferables = new MCM_Transferable[objects.size()];
			int i = 0;
			MCM mcm;
			for (Iterator it = objects.iterator(); it.hasNext(); i++) {
				mcm = (MCM) it.next();
				transferables[i] = (MCM_Transferable) mcm.getTransferable();
			}
			return transferables;
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}


	private Set getObjectsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {

		StorableObjectCondition condition = null;
		try {
			condition = StorableObjectConditionBuilder.restoreCondition(condition_Transferable);
		}
		catch (IllegalDataException ide) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_DATA,
					CompletionStatus.COMPLETED_NO,
					"Cannot restore condition -- " + ide.getMessage());
		}

		try {
			Set ids = Identifier.fromTransferables(identifier_Transferables);
			Set objects = AdministrationStorableObjectPool.getStorableObjectsByConditionButIds(ids, condition, true);
			return objects;
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}





	// Refresh objects from a pool
	public Identifier_Transferable[] transmitRefreshedAdministrationObjects(StorableObject_Transferable[] storableObjects_Transferables,
			AccessIdentity_Transferable accessIdentityT) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentityT);
		Log.debugMessage("CMAdministrationTransmit.transmitRefreshedAdministrationObjects | Refreshing for user '"
				+ accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			Map storableObjectsTMap = new HashMap();
			for (int i = 0; i < storableObjects_Transferables.length; i++)
				storableObjectsTMap.put(new Identifier(storableObjects_Transferables[i].id), storableObjects_Transferables[i]);

			AdministrationStorableObjectPool.refresh();
			Set storableObjects = AdministrationStorableObjectPool.getStorableObjects(storableObjectsTMap.keySet(), true);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {
				StorableObject so = (StorableObject) it.next();
				StorableObject_Transferable sot = (StorableObject_Transferable) storableObjectsTMap.get(so.getId());
				//Remove objects with older versions as well as objects with the same versions -- not only with older ones!
				if (!so.hasNewerVersion(sot.version))
					it.remove();
			}

			return Identifier.createTransferables(storableObjects);
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
