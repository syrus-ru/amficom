/*
* $Id: MCMAdministrationObjectLoader.java,v 1.5 2005/03/10 15:23:06 arseniy Exp $
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
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
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
 * @version $Revision: 1.5 $, $Date: 2005/03/10 15:23:06 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
final class MCMAdministrationObjectLoader extends DatabaseAdministrationObjectLoader {

	public User loadUser(Identifier id) throws RetrieveObjectException, CommunicationException {
		User user = null;
		try {
			user = new User(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("User '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				user = new User(MeasurementControlModule.mServerRef.transmitUser((Identifier_Transferable) id.getTransferable()));
				AdministrationDatabaseContext.getUserDatabase().update(user, null, StorableObjectDatabase.UPDATE_FORCE);
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
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
			}
		}
		return user;
	}

	public Domain loadDomain(Identifier id) throws RetrieveObjectException, CommunicationException {
		Domain domain = null;
		try {
			domain = new Domain(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Domain '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				domain = new Domain(MeasurementControlModule.mServerRef.transmitDomain((Identifier_Transferable) id.getTransferable()));
				AdministrationDatabaseContext.getDomainDatabase().update(domain, null, StorableObjectDatabase.UPDATE_FORCE);
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
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
			}
		}
		return domain;
	}

	public Server loadServer(Identifier id) throws RetrieveObjectException, CommunicationException {
		Server server = null;
		try {
			server = new Server(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Server '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				server = new Server(MeasurementControlModule.mServerRef.transmitServer((Identifier_Transferable) id.getTransferable()));
				AdministrationDatabaseContext.getServerDatabase().update(server, null, StorableObjectDatabase.UPDATE_FORCE);
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
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
			}
		}
		return server;
	}

	public MCM loadMCM(Identifier id) throws RetrieveObjectException, CommunicationException {
		MCM mcm = null;
		try {
			mcm = new MCM(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("MCM '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				mcm = new MCM(MeasurementControlModule.mServerRef.transmitMCM((Identifier_Transferable)id.getTransferable()));
				AdministrationDatabaseContext.getMCMDatabase().update(mcm, null, StorableObjectDatabase.UPDATE_FORCE);
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
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
			}
		}
		return mcm;
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
