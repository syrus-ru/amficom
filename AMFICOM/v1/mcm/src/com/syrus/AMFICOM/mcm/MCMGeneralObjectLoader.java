/*
* $Id: MCMGeneralObjectLoader.java,v 1.19 2005/05/23 18:45:11 bass Exp $
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
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeDatabase;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.19 $, $Date: 2005/05/23 18:45:11 $
 * @author $Author: bass $
 * @module mcm_v1
 */
final class MCMGeneralObjectLoader extends DatabaseGeneralObjectLoader {

	/* Load multiple objects*/

	public Set loadParameterTypes(Set ids) throws RetrieveObjectException {
		ParameterTypeDatabase database = (ParameterTypeDatabase) GeneralDatabaseContext.getDatabase(ObjectEntities.PARAMETERTYPE_ENTITY_CODE);
		Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = super.createLoadIdsTransferable(ids, objects);
		if (loadIdsT.length == 0)
			return objects;

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MCMSessionEnvironment.getInstance().getMCMServantManager().getMServerReference();
			ParameterType_Transferable[] transferables = mServerRef.transmitParameterTypes(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new ParameterType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
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
		CharacteristicTypeDatabase database = (CharacteristicTypeDatabase) GeneralDatabaseContext.getDatabase(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE);
		Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = super.createLoadIdsTransferable(ids, objects);
		if (loadIdsT.length == 0)
			return objects;

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MCMSessionEnvironment.getInstance().getMCMServantManager().getMServerReference();
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
		CharacteristicDatabase database = (CharacteristicDatabase) GeneralDatabaseContext.getDatabase(ObjectEntities.CHARACTERISTIC_ENTITY_CODE);
		Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = super.createLoadIdsTransferable(ids, objects);
		if (loadIdsT.length == 0)
			return objects;

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MCMSessionEnvironment.getInstance().getMCMServantManager().getMServerReference();
			Characteristic_Transferable[] transferables = mServerRef.transmitCharacteristics(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new Characteristic(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
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





	public Set loadParameterTypesButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}
	public Set loadCharacteristicTypesButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}
	public Set loadCharacteristicsButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}



	/*
	 * MCM do not need in all below methods
	 * */

	public void saveParameterType(ParameterType parameterType, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, parameterType: " + parameterType.getId() + ", force: " + force);
	}

	public void saveCharacteristicType(CharacteristicType characteristicType, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, characteristicType: " + characteristicType.getId() + ", force: " + force);
	}

	public void saveCharacteristic(Characteristic characteristic, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, characteristic: " + characteristic.getId() + ", force: " + force);
	}




	public void saveParameterTypes(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveCharacteristicTypes(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveCharacteristics(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}




	public Set refresh(Set storableObjects) {
		throw new UnsupportedOperationException("Method not implemented, objects: " + storableObjects);
	}




	public void delete(final Set identifiables) {
		throw new UnsupportedOperationException("Method not implemented, objects: " + identifiables);
	}
}
