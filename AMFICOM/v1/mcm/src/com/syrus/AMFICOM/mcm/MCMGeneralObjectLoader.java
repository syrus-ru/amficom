/*
* $Id: MCMGeneralObjectLoader.java,v 1.9 2005/03/31 16:01:56 arseniy Exp $
*
* Copyright © 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mcm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseGeneralObjectLoader;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeDatabase;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.9 $, $Date: 2005/03/31 16:01:56 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
final class MCMGeneralObjectLoader extends DatabaseGeneralObjectLoader {

	public ParameterType loadParameterType(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new ParameterType(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("ParameterType '" + id + "' not found in database; trying to load from MServer", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					ParameterType_Transferable transferable = mServerRef.transmitParameterType((Identifier_Transferable) id.getTransferable());
					ParameterType parameterType = new ParameterType(transferable);

					try {
						GeneralDatabaseContext.getParameterTypeDatabase().insert(parameterType);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return parameterType;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("ParameterType '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve ParameterType '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.resetMServerConnection();
			throw new CommunicationException(mesg);
		}
	}

	public CharacteristicType loadCharacteristicType(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new CharacteristicType(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("CharacteristicType '" + id + "' not found in database; trying to load from MServer", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					CharacteristicType_Transferable transferable = mServerRef.transmitCharacteristicType((Identifier_Transferable) id.getTransferable());
					CharacteristicType characteristicType = new CharacteristicType(transferable);

					try {
						GeneralDatabaseContext.getCharacteristicTypeDatabase().insert(characteristicType);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return characteristicType;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("CharacteristicType '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve CharacteristicType '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.resetMServerConnection();
			throw new CommunicationException(mesg);
		}
	}

	public Characteristic loadCharacteristic(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new Characteristic(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("Characteristic '" + id + "' not found in database; trying to load from MServer", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {

				try {
					Characteristic_Transferable transferable = mServerRef.transmitCharacteristic((Identifier_Transferable) id.getTransferable());
					Characteristic characteristic = new Characteristic(transferable);

					try {
						GeneralDatabaseContext.getCharacteristicDatabase().insert(characteristic);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return characteristic;
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("Characteristic '" + id + "' not found in MServer database");
					throw new RetrieveObjectException("Cannot retrieve Characteristic '" + id + "' from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
			}
			String mesg = "Remote reference for server is null; will try to reactivate it";
			Log.errorMessage(mesg);
			MeasurementControlModule.resetMServerConnection();
			throw new CommunicationException(mesg);
		}
	}





	public Collection loadParameterTypes(Collection ids) throws ApplicationException {
		ParameterTypeDatabase database = (ParameterTypeDatabase) GeneralDatabaseContext.getParameterTypeDatabase();
		Collection objects;
		try {
			objects = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			String mesg = "MCMGeneralObjectLoader.loadParameterTypes | Cannot load objects from database: " + ide.getMessage();
			throw new RetrieveObjectException(mesg, ide);
		}

		Identifier id;
		Collection loadIds = new HashSet(ids);
		for (Iterator it = objects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadIds.remove(id);
		}

		if (!loadIds.isEmpty()) {
			Identifier_Transferable[] loadIdsT = Identifier.createTransferables(loadIds);

			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {
				Collection loadedObjects = null;
				try {
					ParameterType_Transferable[] transferables = mServerRef.transmitParameterTypes(loadIdsT);
					loadedObjects = new ArrayList(transferables.length);
					for (int i = 0; i < transferables.length; i++)
						loadedObjects.add(new ParameterType(transferables[i]));
				}
				catch (AMFICOMRemoteException are) {
					Log.errorMessage("Cannot retrieve parameter types from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}

				if (loadedObjects != null && !loadedObjects.isEmpty()) {
					objects.addAll(loadedObjects);

					try {
						database.insert(loadedObjects);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}
				}

			}
			else {
				Log.errorMessage("Remote reference for server is null; will try to reactivate it");
				MeasurementControlModule.resetMServerConnection();
			}
		}

		return objects;
	}

	public Collection loadCharacteristicTypes(Collection ids) throws ApplicationException {
		CharacteristicTypeDatabase database = (CharacteristicTypeDatabase) GeneralDatabaseContext.getCharacteristicTypeDatabase();
		Collection objects;
		try {
			objects = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			String mesg = "MCMGeneralObjectLoader.loadCharacteristicTypes | Cannot load objects from database: " + ide.getMessage();
			throw new RetrieveObjectException(mesg, ide);
		}

		Identifier id;
		Collection loadIds = new HashSet(ids);
		for (Iterator it = objects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadIds.remove(id);
		}

		if (!loadIds.isEmpty()) {
			Identifier_Transferable[] loadIdsT = Identifier.createTransferables(loadIds);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {
				Collection loadedObjects = null;
				try {
					CharacteristicType_Transferable[] transferables = mServerRef.transmitCharacteristicTypes(loadIdsT);
					loadedObjects = new ArrayList(transferables.length);
					for (int i = 0; i < transferables.length; i++)
						loadedObjects.add(new CharacteristicType(transferables[i]));
				}
				catch (AMFICOMRemoteException are) {
					Log.errorMessage("Cannot retrieve characteristic types from MServer database -- " + are.message);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}

				if (loadedObjects != null && !loadedObjects.isEmpty()) {
					objects.addAll(loadedObjects);

					try {
						database.insert(loadedObjects);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}
				}

			}
			else {
				Log.errorMessage("Remote reference for server is null; will try to reactivate it");
				MeasurementControlModule.resetMServerConnection();
			}
		}

		return objects;
	}

	public Collection loadCharacteristics(Collection ids) throws ApplicationException {
		CharacteristicDatabase database = (CharacteristicDatabase) GeneralDatabaseContext.getCharacteristicDatabase();
		Collection objects;
		try {
			objects = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			String mesg = "MCMGeneralObjectLoader.loadCharacteristics | Cannot load objects from database: " + ide.getMessage();
			throw new RetrieveObjectException(mesg, ide);
		}

		Identifier id;
		Collection loadIds = new HashSet(ids);
		for (Iterator it = objects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadIds.remove(id);
		}

		if (!loadIds.isEmpty()) {
			Identifier_Transferable[] loadIdsT = Identifier.createTransferables(loadIds);
			com.syrus.AMFICOM.mserver.corba.MServer mServerRef = MeasurementControlModule.mServerRef;
			if (mServerRef != null) {
				Collection loadedObjects = null;
				try {
					Characteristic_Transferable[] transferables = mServerRef.transmitCharacteristics(loadIdsT);
					loadedObjects = new ArrayList(transferables.length);
					for (int i = 0; i < transferables.length; i++)
						loadedObjects.add(new Characteristic(transferables[i]));
				}
				catch (AMFICOMRemoteException are) {
					Log.errorMessage("Cannot retrieve characteristics from MServer database -- " + are.message);
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.resetMServerConnection();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}

				if (loadedObjects != null && !loadedObjects.isEmpty()) {
					objects.addAll(loadedObjects);

					try {
						database.insert(loadedObjects);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}
				}

			}
			else {
				Log.errorMessage("Remote reference for server is null; will try to reactivate it");
				MeasurementControlModule.resetMServerConnection();
			}
		}

		return objects;
	}





	public Collection loadParameterTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}
	public Collection loadCharacteristicTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}
	public Collection loadCharacteristicsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}



	/*
	 * MCM do not need in all below methods
	 * */

	public void saveParameterType(ParameterType parameterType, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, parameterType: " + parameterType.getId() + ", force: " + force);
	}

	public void saveCharacteristicType(CharacteristicType characteristicType, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, characteristicType: " + characteristicType.getId() + ", force: " + force);
	}

	public void saveCharacteristic(Characteristic characteristic, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, characteristic: " + characteristic.getId() + ", force: " + force);
	}




	public void saveParameterTypes(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveCharacteristicTypes(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveCharacteristics(Collection objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
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
