/*
 * $Id: ClientAdministrationObjectLoader.java,v 1.2 2005/01/24 11:51:05 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import com.syrus.AMFICOM.administration.corba.DomainCondition_Transferable;
import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
import com.syrus.AMFICOM.administration.corba.MCM_Transferable;
import com.syrus.AMFICOM.administration.corba.Server_Transferable;
import com.syrus.AMFICOM.administration.corba.User_Transferable;
import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StringFieldCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.general.corba.StringFieldCondition_Transferable;
import com.syrus.AMFICOM.measurement.DomainCondition;
import com.syrus.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * @version $Revision: 1.2 $, $Date: 2005/01/24 11:51:05 $
 * @author $Author: krupenn $
 * @module generalclient_v1
 */
public class ClientAdministrationObjectLoader implements AdministrationObjectLoader {

	private static AccessIdentifier_Transferable	accessIdentifierTransferable;

	private CMServer								server;

	public ClientAdministrationObjectLoader(CMServer server) {
		this.server = server;
	}

	public static void setAccessIdentifierTransferable(AccessIdentifier_Transferable accessIdentifier_Transferable) {
		accessIdentifierTransferable = accessIdentifier_Transferable;
	}

	public void delete(Identifier id) throws CommunicationException {
		Identifier_Transferable identifier_Transferable = (Identifier_Transferable) id.getTransferable();
		try {
			this.server.delete(identifier_Transferable, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.delete | Couldn't delete id =" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public void delete(List ids) throws CommunicationException {
		Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
		int i = 0;
		for (Iterator it = ids.iterator(); it.hasNext(); i++) {
			Identifier id = (Identifier) it.next();
			identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
		}
		try {
			this.server.deleteList(identifier_Transferables, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.delete | AMFICOMRemoteException ";
			throw new CommunicationException(msg, e);
		}
	}

	public Domain loadDomain(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new Domain(this.server.transmitDomain((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientAdministrationObjectLoader.loadDomain | new Domain(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.loadDomain | server.transmitDomain(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public List loadDomains(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Domain_Transferable[] transferables = this.server.transmitDomains(identifierTransferables,
				accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new Domain(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadDomainsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Domain_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			if (condition instanceof DomainCondition) {
				transferables = this.server.transmitDomainsButIdsCondition(identifierTransferables,
					accessIdentifierTransferable, (DomainCondition_Transferable) condition.getTransferable());

			} else {
				transferables = this.server
						.transmitDomainsButIds(identifierTransferables, accessIdentifierTransferable);
				if (condition != null && !(condition instanceof DomainCondition)) {
					Log.errorMessage("ClientAdministrationObjectLoader.loadMeasurementsButIds | " + "Class '"
							+ condition.getClass().getName() + "' is not instanse of DomainCondition or ");
				}

			}

			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new Domain(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public MCM loadMCM(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new MCM(this.server.transmitMCM((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientAdministrationObjectLoader.loadMCM | new MCM(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.loadMCM | server.transmitMCM(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public List loadMCMs(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MCM_Transferable[] transferables = this.server.transmitMCMs(identifierTransferables,
				accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new MCM(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadMCMsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			MCM_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			if (condition instanceof DomainCondition) {
				transferables = this.server.transmitMCMsButIdsCondition(identifierTransferables,
					accessIdentifierTransferable, (DomainCondition_Transferable) condition.getTransferable());

			} else {
				transferables = this.server.transmitMCMsButIds(identifierTransferables, accessIdentifierTransferable);
				if (condition != null && !(condition instanceof DomainCondition)) {
					Log.errorMessage("ClientAdministrationObjectLoader.loadMCMsButIds | " + "Class '"
							+ condition.getClass().getName() + "' is not instanse of DomainCondition or ");
				}

			}
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new MCM(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Server loadServer(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new Server(this.server.transmitServer((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientAdministrationObjectLoader.loadServer | new Server(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.loadServer | server.transmiServer(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public List loadServers(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Server_Transferable[] transferables = this.server.transmitServers(identifierTransferables,
				accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new Server(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadServersButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Server_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			if (condition instanceof DomainCondition) {
				transferables = this.server.transmitServersButIdsCondition(identifierTransferables,
					accessIdentifierTransferable, (DomainCondition_Transferable) condition.getTransferable());
			} else {
				transferables = this.server
						.transmitServersButIds(identifierTransferables, accessIdentifierTransferable);
				if (condition != null && !(condition instanceof DomainCondition)) {
					Log.errorMessage("ClientAdministrationObjectLoader.loadMeasurementPortsButIds | " + "Class '"
							+ condition.getClass().getName() + "' is not instanse of DomainCondition");
				}
			}
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new Server(transferables[j]));
			}
			return list;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public User loadUser(Identifier id) throws RetrieveObjectException, CommunicationException {
		try {
			return new User(this.server.transmitUser((Identifier_Transferable) id.getTransferable(),
				accessIdentifierTransferable));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.loadUser | server.transmitUser(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public List loadUsers(List ids) throws DatabaseException, CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			User_Transferable[] transferables = this.server.transmitUsers(identifierTransferables,
				accessIdentifierTransferable);
			List list = new ArrayList(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				list.add(new User(transferables[j]));
			}
			return list;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public List loadUsersButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			List list = null;
			if (condition instanceof StringFieldCondition) {
				StringFieldCondition stringFieldCondition = (StringFieldCondition) condition;
				User_Transferable[] transferables = this.server.transmitUsersButIdsCondition(identifierTransferables,
					accessIdentifierTransferable, (StringFieldCondition_Transferable) stringFieldCondition
							.getTransferable());
				list = new ArrayList(transferables.length);
				for (int j = 0; j < transferables.length; j++) {
					list.add(new User(transferables[j]));
				}
			}
			return list;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public java.util.Set refresh(java.util.Set storableObjects) throws CommunicationException {
		try {
			java.util.Set refreshedIds = new HashSet();
			Identifier_Transferable[] identifier_Transferables;
			StorableObject_Transferable[] storableObject_Transferables = new StorableObject_Transferable[storableObjects
					.size()];
			int i = 0;
			for (Iterator it = storableObjects.iterator(); it.hasNext(); i++) {
				StorableObject storableObject = (StorableObject) it.next();
				storableObject_Transferables[i] = storableObject.getHeaderTransferable();
			}
			identifier_Transferables = this.server.transmitRefreshedMeasurementObjects(storableObject_Transferables,
				accessIdentifierTransferable);

			for (int j = 0; j < identifier_Transferables.length; j++) {
				refreshedIds.add(new Identifier(identifier_Transferables[j]));
			}

			return refreshedIds;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public void saveDomain(Domain domain, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Domain_Transferable transferables = (Domain_Transferable) domain.getTransferable();
		try {
			this.server.receiveDomain(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.saveDomain ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveDomains(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Domain_Transferable[] transferables = new Domain_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (Domain_Transferable) ((Domain) it.next()).getTransferable();
		}
		try {
			this.server.receiveDomains(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.saveDomains ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMCM(MCM mcm, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		MCM_Transferable transferables = (MCM_Transferable) mcm.getTransferable();
		try {
			this.server.receiveMCM(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.saveMCM ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMCMs(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		MCM_Transferable[] transferables = new MCM_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (MCM_Transferable) ((MCM) it.next()).getTransferable();
		}
		try {
			this.server.receiveMCMs(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.saveMCMs ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveServer(Server server, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Server_Transferable transferables = (Server_Transferable) server.getTransferable();
		try {
			this.server.receiveServer(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.saveDomain ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveServers(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		Server_Transferable[] transferables = new Server_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (Server_Transferable) ((Server) it.next()).getTransferable();
		}
		try {
			this.server.receiveServers(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.saveServers ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveUser(User user, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		User_Transferable transferables = (User_Transferable) user.getTransferable();
		try {
			this.server.receiveUser(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.saveUser ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveUsers(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		User_Transferable[] transferables = new User_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			transferables[i] = (User_Transferable) ((User) it.next()).getTransferable();
		}
		try {
			this.server.receiveUsers(transferables, force, accessIdentifierTransferable);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.saveUsers ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, e);

			throw new CommunicationException(msg, e);
		}
	}

}
