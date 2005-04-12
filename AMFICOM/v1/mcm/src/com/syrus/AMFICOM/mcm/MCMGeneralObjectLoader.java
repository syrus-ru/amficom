/*
* $Id: MCMGeneralObjectLoader.java,v 1.12 2005/04/12 08:29:16 bass Exp $
*
* Copyright © 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mcm;

import java.util.HashSet;
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
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.12 $, $Date: 2005/04/12 08:29:16 $
 * @author $Author: bass $
 * @module mcm_v1
 */
final class MCMGeneralObjectLoader extends DatabaseGeneralObjectLoader {

	public ParameterType loadParameterType(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new ParameterType(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMGeneralObjectLoader.loadParameterType | ParameterType '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			ParameterType parameterType = null;

			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				ParameterType_Transferable transferable = mServerRef.transmitParameterType((Identifier_Transferable) id.getTransferable());
				parameterType = new ParameterType(transferable);
				Log.debugMessage("MCMGeneralObjectLoader.loadParameterType | ParameterType '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("ParameterType '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve ParameterType '" + id + "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (parameterType != null) {
				try {
					GeneralDatabaseContext.getParameterTypeDatabase().insert(parameterType);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return parameterType;
			}
			throw new ObjectNotFoundException("ParameterType '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}

	public CharacteristicType loadCharacteristicType(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new CharacteristicType(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMGeneralObjectLoader.loadCharacteristicType | CharacteristicType '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			CharacteristicType characteristicType = null;

			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				CharacteristicType_Transferable transferable = mServerRef.transmitCharacteristicType((Identifier_Transferable) id.getTransferable());
				characteristicType = new CharacteristicType(transferable);
				Log.debugMessage("MCMGeneralObjectLoader.loadCharacteristicType | CharacteristicType '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("CharacteristicType '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve CharacteristicType '" + id + "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (characteristicType != null) {
				try {
					GeneralDatabaseContext.getCharacteristicTypeDatabase().insert(characteristicType);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return characteristicType;
			}
			throw new ObjectNotFoundException("CharacteristicType '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}

	public Characteristic loadCharacteristic(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new Characteristic(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MCMGeneralObjectLoader.loadCharacteristic | Characteristic '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			Characteristic characteristic = null;

			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			try {
				Characteristic_Transferable transferable = mServerRef.transmitCharacteristic((Identifier_Transferable) id.getTransferable());
				characteristic = new Characteristic(transferable);
				Log.debugMessage("MCMGeneralObjectLoader.loadCharacteristic | Characteristic '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("Characteristic '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve Characteristic '" + id + "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (characteristic != null) {
				try {
					GeneralDatabaseContext.getCharacteristicDatabase().insert(characteristic);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return characteristic;
			}
			throw new ObjectNotFoundException("Characteristic '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}





	public Set loadParameterTypes(Set ids) throws RetrieveObjectException {
		ParameterTypeDatabase database = GeneralDatabaseContext.getParameterTypeDatabase();
		Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = null;
		try {
			loadIdsT = super.createLoadIdsTransferable(ids, objects);
		}
		catch (IllegalDataException ide) {
			// Never
			Log.errorException(ide);
		}
		if (loadIdsT.length == 0)
			return objects;

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			ParameterType_Transferable[] transferables = mServerRef.transmitParameterTypes(loadIdsT);
			for (int i = 0; i < transferables.length; i++)
				loadedObjects.add(new ParameterType(transferables[i]));
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MCMGeneralObjectLoader.loadParameterTypes | Cannot load objects from MeasurementServer");
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);

			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}

	public Set loadCharacteristicTypes(Set ids) throws RetrieveObjectException {
		CharacteristicTypeDatabase database = GeneralDatabaseContext.getCharacteristicTypeDatabase();
		Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = null;
		try {
			loadIdsT = super.createLoadIdsTransferable(ids, objects);
		}
		catch (IllegalDataException ide) {
			// Never
			Log.errorException(ide);
		}
		if (loadIdsT.length == 0)
			return objects;

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			CharacteristicType_Transferable[] transferables = mServerRef.transmitCharacteristicTypes(loadIdsT);
			for (int i = 0; i < transferables.length; i++)
				loadedObjects.add(new CharacteristicType(transferables[i]));
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MCMGeneralObjectLoader.loadCharacteristicTypes | Cannot load objects from MeasurementServer");
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);

			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}

	public Set loadCharacteristics(Set ids) throws RetrieveObjectException {
		CharacteristicDatabase database = GeneralDatabaseContext.getCharacteristicDatabase();
		Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = null;
		try {
			loadIdsT = super.createLoadIdsTransferable(ids, objects);
		}
		catch (IllegalDataException ide) {
			// Never
			Log.errorException(ide);
		}
		if (loadIdsT.length == 0)
			return objects;

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MeasurementControlModule.mServerConnectionManager.getVerifiedMServerReference();
			Characteristic_Transferable[] transferables = mServerRef.transmitCharacteristics(loadIdsT);
			for (int i = 0; i < transferables.length; i++)
				loadedObjects.add(new Characteristic(transferables[i]));
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MCMGeneralObjectLoader.loadCharacteristics | Cannot load objects from MeasurementServer");
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);

			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}





	public Set loadParameterTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}
	public Set loadCharacteristicTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}
	public Set loadCharacteristicsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
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




	public void saveParameterTypes(Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveCharacteristicTypes(Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveCharacteristics(Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}




	public Set refresh(Set storableObjects) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, objects: " + storableObjects);
	}




	public void delete(final Set identifiables) {
		throw new UnsupportedOperationException("Method not implemented, objects: " + identifiables);
	}

	public void delete(Identifier id) throws IllegalDataException {
		throw new UnsupportedOperationException("Method not implemented, id: " + id);
	}
}
