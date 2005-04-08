/*
 * $Id: ClientAdministrationObjectLoader.java,v 1.13 2005/04/08 15:47:27 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
import com.syrus.AMFICOM.administration.corba.MCM_Transferable;
import com.syrus.AMFICOM.administration.corba.Server_Transferable;
import com.syrus.AMFICOM.administration.corba.User_Transferable;
import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.AbstractClientObjectLoader;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;

/**
 * @version $Revision: 1.13 $, $Date: 2005/04/08 15:47:27 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class ClientAdministrationObjectLoader extends AbstractClientObjectLoader implements AdministrationObjectLoader {

	private CMServer								cmserver;

	public ClientAdministrationObjectLoader(CMServer server) {
		this.cmserver = server;
	}
	
	private AccessIdentifier_Transferable getAccessIdentifierTransferable() {
		return  (AccessIdentifier_Transferable) SessionContext.getAccessIdentity().getTransferable();
	}
	
	private StorableObject fromTransferable(Identifier id, IDLEntity transferable) throws ApplicationException {
		StorableObject so = null;
		try {
			so = AdministrationStorableObjectPool.getStorableObject(id, false);
		} catch (ApplicationException e) {
			// nothing do
		}
		if (so != null)
			super.fromTransferable(so, transferable);
		return so;
	}
	
	public void delete(Identifier id) throws IllegalDataException {
		Identifier_Transferable identifier_Transferable = (Identifier_Transferable) id.getTransferable();
		try {
			this.cmserver.delete(identifier_Transferable, getAccessIdentifierTransferable());
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.delete | Couldn't delete id =" + id.toString() + ")";
			throw new IllegalDataException(msg, e);
		}
	}

	public void delete(Set ids) throws IllegalDataException {
		Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
		int i = 0;
		for (Iterator it = ids.iterator(); it.hasNext(); i++) {
			Identifier id = (Identifier) it.next();
			identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
		}
		try {
			this.cmserver.deleteList(identifier_Transferables, getAccessIdentifierTransferable());
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.delete | AMFICOMRemoteException ";
			throw new IllegalDataException(msg, e);
		}
	}

	public Domain loadDomain(Identifier id) throws ApplicationException {
		try {
			Domain_Transferable dt = this.cmserver.transmitDomain((Identifier_Transferable) id.getTransferable(),
				getAccessIdentifierTransferable());
			Domain domain = (Domain) this.fromTransferable(id, dt);
			if (domain == null)
				domain = new Domain(dt);
			return domain;			
		} catch (CreateObjectException e) {
			String msg = "ClientAdministrationObjectLoader.loadDomain | new Domain(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.loadDomain | server.transmitDomain(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Set loadDomains(Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Domain_Transferable[] transferables = this.cmserver.transmitDomains(identifierTransferables,
				getAccessIdentifierTransferable());
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				Domain domain = (Domain) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (domain == null)
					domain = new Domain(transferables[j]);
				set.add(domain);
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Set loadDomainsButIds(StorableObjectCondition condition, Set ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Domain_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.cmserver.transmitDomainsButIdsCondition(identifierTransferables,
				getAccessIdentifierTransferable(), StorableObjectConditionBuilder.getConditionTransferable(condition));
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new Domain(transferables[j]));
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public MCM loadMCM(Identifier id) throws ApplicationException {
		try {
			MCM_Transferable mt = this.cmserver.transmitMCM((Identifier_Transferable) id.getTransferable(),
				getAccessIdentifierTransferable());
			MCM mcm = (MCM) this.fromTransferable(id, mt);
			if (mcm == null)
				mcm = new MCM(mt);
			return mcm;			
		} catch (CreateObjectException e) {
			String msg = "ClientAdministrationObjectLoader.loadMCM | new MCM(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.loadMCM | server.transmitMCM(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Set loadMCMs(Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MCM_Transferable[] transferables = this.cmserver.transmitMCMs(identifierTransferables,
				getAccessIdentifierTransferable());
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				MCM mcm = (MCM) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (mcm == null)
					mcm = new MCM(transferables[j]);
				set.add(mcm);
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Set loadMCMsButIds(StorableObjectCondition condition, Set ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			MCM_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.cmserver.transmitMCMsButIdsCondition(identifierTransferables,
				getAccessIdentifierTransferable(), StorableObjectConditionBuilder.getConditionTransferable(condition));
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new MCM(transferables[j]));
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Server loadServer(Identifier id) throws ApplicationException {
		try {
			Server_Transferable st = this.cmserver.transmitServer((Identifier_Transferable) id.getTransferable(),
				getAccessIdentifierTransferable());
			Server server = (Server) this.fromTransferable(id, st);
			if (server == null)
				server = new Server(st);
			return server;		
		} catch (CreateObjectException e) {
			String msg = "ClientAdministrationObjectLoader.loadServer | new Server(" + id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.loadServer | server.transmiServer(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Set loadServers(Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Server_Transferable[] transferables = this.cmserver.transmitServers(identifierTransferables,
				getAccessIdentifierTransferable());
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				Server server = (Server) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (server == null)
					server = new Server(transferables[j]);
				set.add(server);
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Set loadServersButIds(StorableObjectCondition condition, Set ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Server_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.cmserver.transmitServersButIdsCondition(identifierTransferables,
				getAccessIdentifierTransferable(), StorableObjectConditionBuilder.getConditionTransferable(condition));
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new Server(transferables[j]));
			}
			return set;
		} catch (CreateObjectException e) {
			throw new RetrieveObjectException(e);
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public User loadUser(Identifier id) throws ApplicationException {
		try {
			User_Transferable ut = this.cmserver.transmitUser((Identifier_Transferable) id.getTransferable(),
				getAccessIdentifierTransferable());
			User user = (User) this.fromTransferable(id, ut);
			if (user == null)
				user = new User(ut);
			return user;		
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.loadUser | server.transmitUser(" + id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public Set loadUsers(Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			User_Transferable[] transferables = this.cmserver.transmitUsers(identifierTransferables,
				getAccessIdentifierTransferable());
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				User user = (User) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (user == null)
					user = new User(transferables[j]);
				set.add(user);
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public Set loadUsersButIds(StorableObjectCondition condition, Set ids) throws DatabaseException,
			CommunicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			User_Transferable[] transferables = this.cmserver.transmitUsersButIdsCondition(identifierTransferables,
				getAccessIdentifierTransferable(), StorableObjectConditionBuilder.getConditionTransferable(condition));
			Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new User(transferables[j]));
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	public java.util.Set refresh(Set storableObjects) throws CommunicationException {
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
			identifier_Transferables = this.cmserver.transmitRefreshedMeasurementObjects(storableObject_Transferables,
				getAccessIdentifierTransferable());

			for (int j = 0; j < identifier_Transferables.length; j++) {
				refreshedIds.add(new Identifier(identifier_Transferables[j]));
			}

			return refreshedIds;
		} catch (AMFICOMRemoteException e) {
			throw new CommunicationException(e);
		}
	}

	private void updateStorableObjectHeader(Set storableObjects, StorableObject_Transferable[] transferables) {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			Identifier_Transferable id = (Identifier_Transferable) storableObject.getId().getTransferable();
			for (int i = 0; i < transferables.length; i++) {
				if (transferables[i].id.equals(id)) {
					storableObject.updateFromHeaderTransferable(transferables[i]);
				}
			}
		}
	}
	
	public void saveDomain(Domain domain, boolean force) throws VersionCollisionException, CommunicationException {
		Domain_Transferable transferables = (Domain_Transferable) domain.getTransferable();
		try {
			domain.updateFromHeaderTransferable(this.cmserver.receiveDomain(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.saveDomain ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveDomains(Set set, boolean force) throws VersionCollisionException, CommunicationException {
		Domain_Transferable[] transferables = new Domain_Transferable[set.size()];
		int i = 0;
		for (Iterator it = set.iterator(); it.hasNext(); i++) {
			transferables[i] = (Domain_Transferable) ((Domain) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(set, this.cmserver.receiveDomains(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.saveDomains ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMCM(MCM mcm, boolean force) throws VersionCollisionException, CommunicationException {
		MCM_Transferable transferables = (MCM_Transferable) mcm.getTransferable();
		try {
			mcm.updateFromHeaderTransferable(this.cmserver.receiveMCM(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.saveMCM ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveMCMs(Set set, boolean force) throws VersionCollisionException, CommunicationException {
		MCM_Transferable[] transferables = new MCM_Transferable[set.size()];
		int i = 0;
		for (Iterator it = set.iterator(); it.hasNext(); i++) {
			transferables[i] = (MCM_Transferable) ((MCM) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(set, this.cmserver.receiveMCMs(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.saveMCMs ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveServer(Server server, boolean force) throws VersionCollisionException, CommunicationException {
		Server_Transferable transferables = (Server_Transferable) server.getTransferable();
		try {
			server.updateFromHeaderTransferable(this.cmserver.receiveServer(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.saveDomain ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveServers(Set set, boolean force) throws VersionCollisionException, CommunicationException {
		Server_Transferable[] transferables = new Server_Transferable[set.size()];
		int i = 0;
		for (Iterator it = set.iterator(); it.hasNext(); i++) {
			transferables[i] = (Server_Transferable) ((Server) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(set, this.cmserver.receiveServers(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.saveServers ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveUser(User user, boolean force) throws VersionCollisionException, CommunicationException {
		User_Transferable transferables = (User_Transferable) user.getTransferable();
		try {
			user.updateFromHeaderTransferable(this.cmserver.receiveUser(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.saveUser ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

	public void saveUsers(Set set, boolean force) throws VersionCollisionException, CommunicationException {
		User_Transferable[] transferables = new User_Transferable[set.size()];
		int i = 0;
		for (Iterator it = set.iterator(); it.hasNext(); i++) {
			transferables[i] = (User_Transferable) ((User) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(set, this.cmserver.receiveUsers(transferables, force, getAccessIdentifierTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientAdministrationObjectLoader.saveUsers ";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new CommunicationException(msg, e);
		}
	}

}
