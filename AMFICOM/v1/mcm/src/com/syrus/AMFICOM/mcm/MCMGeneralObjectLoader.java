/*
* $Id: MCMGeneralObjectLoader.java,v 1.1 2005/01/17 09:02:19 bob Exp $
*
* Copyright © 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mcm;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.GeneralObjectLoader;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.1 $, $Date: 2005/01/17 09:02:19 $
 * @author $Author: bob $
 * @module mcm_v1
 */
final class MCMGeneralObjectLoader implements GeneralObjectLoader {

	public CharacteristicType loadCharacteristicType(Identifier id) throws RetrieveObjectException, CommunicationException {
		CharacteristicType characteristicType = null;
		try {
			characteristicType = new CharacteristicType(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Characteristic type '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
			try {
				characteristicType = new CharacteristicType(MeasurementControlModule.mServerRef.transmitCharacteristicType((Identifier_Transferable)id.getTransferable()));
				characteristicType.insert();
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("Characteristic type '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve characteristic type '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
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
				characteristic = new Characteristic(MeasurementControlModule.mServerRef.transmitCharacteristic((Identifier_Transferable)id.getTransferable()));
				characteristic.insert();
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				MeasurementControlModule.activateMServerReference();
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
					Log.errorMessage("Characteristic '" + id + "' not found on server database");
				else
					Log.errorMessage("Cannot retrieve characteristic '" + id + "' from server database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}
		}
		return characteristic;
	}

    public List loadCharacteristics(List ids) throws DatabaseException, CommunicationException {
		CharacteristicDatabase database = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		List list;
		List copyOfList;
		Characteristic characteristic;
		try {
			list = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(list);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Characteristic '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
				try {
					characteristic = new Characteristic(MeasurementControlModule.mServerRef.transmitCharacteristic((Identifier_Transferable)id.getTransferable()));
					characteristic.insert();
					list.add(characteristic);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						Log.errorMessage("Characteristic '" + id + "' not found on server database");
					else
						Log.errorMessage("Cannot retrieve characteristic '" + id + "' from server database -- " + are.message);
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMConfigurationObjectLoader.loadMeasurementSetups | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMConfigurationObjectLoader.loadMeasurementSetups | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadCharacteristicTypes(List ids) throws DatabaseException, CommunicationException {
		CharacteristicTypeDatabase database = (CharacteristicTypeDatabase)GeneralDatabaseContext.getCharacteristicTypeDatabase();
		List list;
		List copyOfList;
		CharacteristicType characteristicType;
		try {
			list = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(list);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Characteristic type '" + id + "' not found in database; trying to load from server", Log.DEBUGLEVEL07);
				try {
					characteristicType = new CharacteristicType(MeasurementControlModule.mServerRef.transmitCharacteristicType((Identifier_Transferable)id.getTransferable()));
					characteristicType.insert();
					list.add(characteristicType);
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementControlModule.activateMServerReference();
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						Log.errorMessage("Characteristic type '" + id + "' not found on server database");
					else
						Log.errorMessage("Cannot retrieve characteristic type '" + id + "' from server database -- " + are.message);
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MCMConfigurationObjectLoader.loadCharacteristicTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MCMConfigurationObjectLoader.loadCharacteristicTypes | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.general.GeneralObjectLoader#loadCharacteristicTypesButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.List)
	 */
	public List loadCharacteristicTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		// TODO Auto-generated method stub
		return null;
	}

	public List loadCharacteristicsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
	//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	
	public void saveCharacteristicType(CharacteristicType characteristicType, boolean force) throws DatabaseException, CommunicationException {
		//		 TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void saveCharacteristic(Characteristic characteristic, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void saveCharacteristicTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		//      TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void saveCharacteristics(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		//      TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		//      TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void delete(Identifier id) throws CommunicationException, DatabaseException {
		//      TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

	public void delete(List ids) throws CommunicationException, DatabaseException {
		//      TODO method isn't complete
		throw new UnsupportedOperationException("method isn't complete");
	}

}
