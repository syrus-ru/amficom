/*
* $Id: MCMGeneralObjectLoader.java,v 1.5 2005/03/10 15:23:06 arseniy Exp $
*
* Copyright © 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mcm;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.DatabaseGeneralObjectLoader;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeDatabase;
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
final class MCMGeneralObjectLoader extends DatabaseGeneralObjectLoader {

	public ParameterType loadParameterType(Identifier id) throws RetrieveObjectException, CommunicationException {
		ParameterType parameterType = null;
		try {
			parameterType = new ParameterType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("ParameterType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
			try {
				parameterType = new ParameterType(MeasurementControlModule.mServerRef.transmitParameterType((Identifier_Transferable) id.getTransferable()));
				GeneralDatabaseContext.getParameterTypeDatabase().update(parameterType, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				String mesg = null;
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					mesg = "Parameter type '" + id + "' not found on server database";
				else
					mesg = "Cannot retrieve parameter type '" + id + "' from server database -- " + are.message;
				Log.errorMessage(mesg);
				throw new RetrieveObjectException(mesg);
			}
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
			}
		}
		return parameterType;
	}

	public CharacteristicType loadCharacteristicType(Identifier id) throws RetrieveObjectException, CommunicationException {
		CharacteristicType characteristicType = null;
		try {
			characteristicType = new CharacteristicType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Characteristic type '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				characteristicType = new CharacteristicType(MeasurementControlModule.mServerRef.transmitCharacteristicType((Identifier_Transferable) id.getTransferable()));
				GeneralDatabaseContext.getCharacteristicTypeDatabase().update(characteristicType, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				String mesg = null;
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					mesg = "Characteristic type '" + id + "' not found on server database";
				else
					mesg = "Cannot retrieve characteristic type '" + id + "' from server database -- " + are.message;
				Log.errorMessage(mesg);
				throw new RetrieveObjectException(mesg);
			}
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
			}
		}
		return characteristicType;
	}

	public Characteristic loadCharacteristic(Identifier id) throws RetrieveObjectException, CommunicationException {
		Characteristic characteristic = null;
		try {
			characteristic = new Characteristic(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Characteristic '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				characteristic = new Characteristic(MeasurementControlModule.mServerRef.transmitCharacteristic((Identifier_Transferable) id.getTransferable()));
				GeneralDatabaseContext.getCharacteristicDatabase().update(characteristic, null, StorableObjectDatabase.UPDATE_FORCE);
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				String mesg = null;
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					mesg = "Characteristic '" + id + "' not found on server database";
				else
					mesg = "Cannot retrieve characteristic '" + id + "' from server database -- " + are.message;
				Log.errorMessage(mesg);
				throw new RetrieveObjectException(mesg);
			}
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
			}
		}
		return characteristic;
	}




	public Collection loadParameterTypes(Collection ids) throws DatabaseException, CommunicationException {
		ParameterTypeDatabase database = (ParameterTypeDatabase) GeneralDatabaseContext.getParameterTypeDatabase();
		Collection collection;
		Collection copyOfList;
		Collection loadedObjects = new LinkedList();
		ParameterType parameterType;
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
				Log.debugMessage("ParameterType '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL08);
				try {
					parameterType = new ParameterType(MeasurementControlModule.mServerRef.transmitParameterType((Identifier_Transferable) id.getTransferable()));
					collection.add(parameterType);
					loadedObjects.add(parameterType);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					String mesg = null;
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						mesg = "Parameter type '" + id + "' not found on server database";
					else
						mesg = "Cannot retrieve parameter type '" + id + "' from server database -- " + are.message;
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
			Log.errorMessage("MCMMeasumentObjectLoader.loadParameterTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMMeasumentObjectLoader.loadParameterTypes | Illegal Storable Object: " + e.getMessage());
		}
		return collection;
	}

	public Collection loadCharacteristics(Collection ids) throws DatabaseException, CommunicationException {
		CharacteristicDatabase database = (CharacteristicDatabase) GeneralDatabaseContext.getCharacteristicDatabase();
		Collection collection;
		Collection copyOfList;
		Collection loadedObjects = new LinkedList();
		Characteristic characteristic;
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
				Log.debugMessage("Characteristic '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
				try {
					characteristic = new Characteristic(MeasurementControlModule.mServerRef.transmitCharacteristic((Identifier_Transferable) id.getTransferable()));
					collection.add(characteristic);
					loadedObjects.add(characteristic);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					String mesg = null;
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						mesg = "Characteristic '" + id + "' not found on server database";
					else
						mesg = "Cannot retrieve characteristic '" + id + "' from server database -- " + are.message;
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
			Log.errorMessage("MCMConfigurationObjectLoader.loadMeasurementSetups | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMConfigurationObjectLoader.loadMeasurementSetups | Illegal Storable Object: "
					+ e.getMessage());
		}
		return collection;
	}

	public Collection loadCharacteristicTypes(Collection ids) throws DatabaseException, CommunicationException {
		CharacteristicTypeDatabase database = (CharacteristicTypeDatabase) GeneralDatabaseContext.getCharacteristicTypeDatabase();
		Collection collection;
		Collection copyOfList;
		Collection loadedObjects = new LinkedList();
		CharacteristicType characteristicType;
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
				Log.debugMessage("Characteristic type '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
				try {
					characteristicType = new CharacteristicType(MeasurementControlModule.mServerRef.transmitCharacteristicType((Identifier_Transferable) id.getTransferable()));
					collection.add(characteristicType);
					loadedObjects.add(characteristicType);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					String mesg = null;
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						mesg = "Characteristic type '" + id + "' not found on server database";
					else
						mesg = "Cannot retrieve characteristic type '" + id + "' from server database -- " + are.message;
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
			Log.errorMessage("MCMConfigurationObjectLoader.loadCharacteristicTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMConfigurationObjectLoader.loadCharacteristicTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		return collection;
	}

}
