/*
* $Id: MCMAdministrationObjectLoader.java,v 1.12 2005/04/01 21:54:55 arseniy Exp $
*
* Copyright © 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mcm;

import java.util.Set;

import com.syrus.AMFICOM.administration.AdministrationDatabaseContext;
import com.syrus.AMFICOM.administration.DatabaseAdministrationObjectLoader;
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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.12 $, $Date: 2005/04/01 21:54:55 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
final class MCMAdministrationObjectLoader extends DatabaseAdministrationObjectLoader {

	public User loadUser(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new User(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMAdministrationObjectLoader.loadUser | User '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			User user = null;

			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				User_Transferable transferable = mServerRef.transmitUser((Identifier_Transferable) id.getTransferable());
				user = new User(transferable);
				Log.debugMessage("MCMAdministrationObjectLoader.loadUser | User '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("User '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve User '" + id + "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (user != null) {
				try {
					AdministrationDatabaseContext.getUserDatabase().insert(user);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return user;
			}
			throw new ObjectNotFoundException("User '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}

	public Domain loadDomain(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new Domain(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMAdministrationObjectLoader.loadDomain | Domain '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			Domain domain = null;

			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				Domain_Transferable transferable = mServerRef.transmitDomain((Identifier_Transferable) id.getTransferable());
				domain = new Domain(transferable);
				Log.debugMessage("MCMAdministrationObjectLoader.loadDomain | Domain '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("Domain '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve Domain '" + id + "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (domain != null) {
				try {
					AdministrationDatabaseContext.getDomainDatabase().insert(domain);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return domain;
			}
			throw new ObjectNotFoundException("Domain '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}

	public Server loadServer(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new Server(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMAdministrationObjectLoader.loadServer | Server '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			Server server = null;

			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				Server_Transferable transferable = mServerRef.transmitServer((Identifier_Transferable) id.getTransferable());
				server = new Server(transferable);
				Log.debugMessage("MCMAdministrationObjectLoader.loadServer | Server '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("Server '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve Server '" + id + "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (server != null) {
				try {
					AdministrationDatabaseContext.getServerDatabase().insert(server);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return server;
			}
			throw new ObjectNotFoundException("Server '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}

	public MCM loadMCM(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new MCM(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMAdministrationObjectLoader.loadMCM | MCM '" + id
					+ "' not found in database; trying to load from Measurement MCM", Log.DEBUGLEVEL08);
			MCM mcm = null;

			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				MCM_Transferable transferable = mServerRef.transmitMCM((Identifier_Transferable) id.getTransferable());
				mcm = new MCM(transferable);
				Log.debugMessage("MCMAdministrationObjectLoader.loadMCM | MCM '" + id
						+ "' loaded from MeasurementMCM", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("MCM '" + id + "' not found on Measurement MCM -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve MCM '" + id + "' from Measurement MCM -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (mcm != null) {
				try {
					AdministrationDatabaseContext.getMCMDatabase().insert(mcm);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return mcm;
			}
			throw new ObjectNotFoundException("MCM '" + id + "' not found on Measurement MCM");
		}	//catch (ObjectNotFoundException e)
	}




	/*
	 * MCM do not need in all below methods
	 * */

	public Set loadUsers(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadUsersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadDomains(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadDomainsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadServers(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadServersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadMCMs(Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadMCMsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}





	public void saveUser(User user, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, user: " + user.getId() + ", force: " + force);
	}

	public void saveDomain(Domain domain, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, domain: " + domain.getId() + ", force: " + force);
	}

	public void saveServer(Server server, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, server: " + server.getId() + ", force: " + force);
	}

	public void saveMCM(MCM mcm, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, mcm: " + mcm.getId() + ", force: " + force);
	}





	public void saveUsers(Set collection, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + collection + ", force: " + force);
	}

	public void saveDomains(Set collection, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + collection + ", force: " + force);
	}

	public void saveServers(Set collection, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + collection + ", force: " + force);
	}

	public void saveMCMs(Set collection, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + collection + ", force: " + force);
	}





	public Set refresh(Set storableObjects) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, objects: " + storableObjects);
	}





	public void delete(Set objects) throws IllegalDataException {
		throw new UnsupportedOperationException("Method not implemented, objects: " + objects);
	}

	public void delete(Identifier id) throws IllegalDataException {
		throw new UnsupportedOperationException("Method not implemented, id: " + id);
	}

}
