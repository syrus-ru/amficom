/*
* $Id: MCMAdministrationObjectLoader.java,v 1.10 2005/03/30 15:46:34 arseniy Exp $
*
* Copyright © 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mcm;

import java.util.Collection;
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
import com.syrus.util.Log;


/**
 * @version $Revision: 1.10 $, $Date: 2005/03/30 15:46:34 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
final class MCMAdministrationObjectLoader extends DatabaseAdministrationObjectLoader {

	public User loadUser(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new User(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("User '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					User_Transferable transferable = mServerRef.transmitUser((Identifier_Transferable) id.getTransferable());
					User user = new User(transferable);

					try {
						AdministrationDatabaseContext.getUserDatabase().insert(user);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return user;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("Measurement '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve measurement '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.activateMServerReference();
			throw new CommunicationException(mesg);
		}
	}

	public Domain loadDomain(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new Domain(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Domain '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					Domain_Transferable transferable = mServerRef.transmitDomain((Identifier_Transferable) id.getTransferable());
					Domain domain = new Domain(transferable);

					try {
						AdministrationDatabaseContext.getDomainDatabase().insert(domain);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return domain;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("Domain '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve domain '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.activateMServerReference();
			throw new CommunicationException(mesg);
		}
	}

	public Server loadServer(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new Server(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Server '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					Server_Transferable transferable = mServerRef.transmitServer((Identifier_Transferable) id.getTransferable());
					Server server = new Server(transferable);

					try {
						AdministrationDatabaseContext.getServerDatabase().insert(server);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return server;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("Server '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve server '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.activateMServerReference();
			throw new CommunicationException(mesg);
		}
	}

	public MCM loadMCM(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new MCM(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("MCM '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					MCM_Transferable transferable = mServerRef.transmitMCM((Identifier_Transferable) id.getTransferable());
					MCM mcm = new MCM(transferable);

					try {
						AdministrationDatabaseContext.getMCMDatabase().insert(mcm);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return mcm;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("MCM '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve MCM '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.activateMServerReference();
			throw new CommunicationException(mesg);
		}
	}




	/*
	 * MCM do not need in all below methods
	 * */

	public Collection loadUsers(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadUsersButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Collection loadDomains(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadDomainsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Collection loadServers(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadServersButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Collection loadMCMs(Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Collection loadMCMsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
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





	public void saveUsers(Collection collection, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + collection + ", force: " + force);
	}

	public void saveDomains(Collection collection, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + collection + ", force: " + force);
	}

	public void saveServers(Collection collection, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + collection + ", force: " + force);
	}

	public void saveMCMs(Collection collection, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + collection + ", force: " + force);
	}





	public Set refresh(Set storableObjects) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, objects: " + storableObjects);
	}





	public void delete(Collection objects) throws IllegalDataException {
		throw new UnsupportedOperationException("Method not implemented, objects: " + objects);
	}

	public void delete(Identifier id) throws IllegalDataException {
		throw new UnsupportedOperationException("Method not implemented, id: " + id);
	}

}
