/*
* $Id: MCMAdministrationObjectLoader.java,v 1.6 2005/03/22 16:46:45 arseniy Exp $
*
* Copyright © 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mcm;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.syrus.AMFICOM.administration.AdministrationDatabaseContext;
import com.syrus.AMFICOM.administration.DatabaseAdministrationObjectLoader;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainDatabase;
import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.administration.MCMDatabase;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.ServerDatabase;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.administration.UserDatabase;
import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
import com.syrus.AMFICOM.administration.corba.MCM_Transferable;
import com.syrus.AMFICOM.administration.corba.Server_Transferable;
import com.syrus.AMFICOM.administration.corba.User_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.6 $, $Date: 2005/03/22 16:46:45 $
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
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("Measurement '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve measurement '" + id + "' from MServer database -- " + are.message);
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
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("Domain '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve domain '" + id + "' from MServer database -- " + are.message);
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
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("Server '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve server '" + id + "' from MServer database -- " + are.message);
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
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("MCM '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve MCM '" + id + "' from MServer database -- " + are.message);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.activateMServerReference();
			throw new CommunicationException(mesg);
		}
	}





	public Collection loadUsers(Collection ids) throws DatabaseException, CommunicationException {
		UserDatabase database = (UserDatabase)AdministrationDatabaseContext.getUserDatabase();
		Collection collection;
		Collection copyOfList;
		Collection loadedObjects = new LinkedList();
		User user;
		try {
			collection = database.retrieveByIdsByCondition(ids, null);
			copyOfList = new LinkedList(collection);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("User '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
				try {
					user = new User(MeasurementControlModule.mServerRef.transmitUser((Identifier_Transferable)id.getTransferable()));
					collection.add(user);
					loadedObjects.add(user);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					String mesg = null;
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						mesg = "User '" + id + "' not found on server database";
					else
						mesg = "Cannot retrieve user '" + id + "' from server database -- " + are.message;
					Log.errorMessage(mesg);
					throw new RetrieveObjectException(mesg);
				}
			}
			try {
				database.update(loadedObjects, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (VersionCollisionException vce) {
				//This never be caught
				Log.errorException(vce);
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMConfigurationObjectLoader.loadUsers | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMConfigurationObjectLoader.loadUsers | Illegal Storable Object: " + e.getMessage());
		}
		return collection;
	}

	public Collection loadDomains(Collection ids) throws DatabaseException, CommunicationException {
		DomainDatabase database = (DomainDatabase) AdministrationDatabaseContext.getDomainDatabase();
		Collection collection;
		Collection copyOfList;
		Collection loadedObjects = new LinkedList();
		Domain domain;
		try {
			collection = database.retrieveByIdsByCondition(ids, null);
			copyOfList = new LinkedList(collection);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if (ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Domain '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
				try {
					domain = new Domain(MeasurementControlModule.mServerRef.transmitDomain((Identifier_Transferable) id.getTransferable()));
					collection.add(domain);
					loadedObjects.add(domain);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					String mesg = null;
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						mesg = "Domain '" + id + "' not found on server database";
					else
						mesg = "Cannot retrieve domain '" + id + "' from server database -- " + are.message;
					Log.errorMessage(mesg);
					throw new RetrieveObjectException(mesg);
				}
			}
			try {
				database.update(loadedObjects, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (VersionCollisionException vce) {
				//This never be caught
				Log.errorException(vce);
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMConfigurationObjectLoader.loadDomains | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMConfigurationObjectLoader.loadDomains | Illegal Storable Object: " + e.getMessage());
		}
		return collection;
	}

	public Collection loadServers(Collection ids) throws DatabaseException, CommunicationException {
		ServerDatabase database = (ServerDatabase) AdministrationDatabaseContext.getServerDatabase();
		Collection collection;
		Collection copyOfList;
		Collection loadedObjects = new LinkedList();
		Server server;
		try {
			collection = database.retrieveByIdsByCondition(ids, null);
			copyOfList = new LinkedList(collection);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if (ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Server '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
				try {
					server = new Server(MeasurementControlModule.mServerRef.transmitServer((Identifier_Transferable) id.getTransferable()));
					collection.add(server);
					loadedObjects.add(server);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					String mesg = null;
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						mesg = "Server '" + id + "' not found on server database";
					else
						mesg = "Cannot retrieve Server '" + id + "' from server database -- " + are.message;
					Log.errorMessage(mesg);
					throw new RetrieveObjectException(mesg);
				}
			}
			try {
				database.update(loadedObjects, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (VersionCollisionException vce) {
				//This never be caught
				Log.errorException(vce);
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMConfigurationObjectLoader.loadServers | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMConfigurationObjectLoader.loadServers | Illegal Storable Object: " + e.getMessage());
		}
		return collection;
	}

	public Collection loadMCMs(Collection ids) throws DatabaseException, CommunicationException {
		MCMDatabase database = (MCMDatabase) AdministrationDatabaseContext.getMCMDatabase();
		Collection collection;
		Collection copyOfList;
		Collection loadedObjects = new LinkedList();
		MCM mcm;
		try {
			collection = database.retrieveByIdsByCondition(ids, null);
			copyOfList = new LinkedList(collection);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if (ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("MCM '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
				try {
					mcm = new MCM(MeasurementControlModule.mServerRef.transmitMCM((Identifier_Transferable) id.getTransferable()));
					collection.add(mcm);
					loadedObjects.add(mcm);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					String mesg = null;
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						mesg = "MCM '" + id + "' not found on server database";
					else
						mesg = "Cannot retrieve MCM '" + id + "' from server database -- " + are.message;
					Log.errorMessage(mesg);
					throw new RetrieveObjectException(mesg);
				}
			}
			try {
				database.update(loadedObjects, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (VersionCollisionException vce) {
				//This never be caught
				Log.errorException(vce);
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMConfigurationObjectLoader.loadKISs | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMConfigurationObjectLoader.loadKISs | Illegal Storable Object: " + e.getMessage());
		}
		return collection;
	}

}
