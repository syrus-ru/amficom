/*
 * $Id: MCMConfigurationObjectLoader.java,v 1.38 2005/05/24 13:24:57 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.configuration.DatabaseConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.KISDatabase;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MeasurementPortDatabase;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.MeasurementPortTypeDatabase;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.MonitoredElementDatabase;
import com.syrus.AMFICOM.configuration.corba.KIS_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPortType_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPort_Transferable;
import com.syrus.AMFICOM.configuration.corba.MonitoredElement_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.38 $, $Date: 2005/05/24 13:24:57 $
 * @author $Author: bass $
 * @module mcm_v1
 */

final class MCMConfigurationObjectLoader extends DatabaseConfigurationObjectLoader {

	/* Load multiple objects*/

	public Set loadMeasurementPortTypes(Set ids) throws RetrieveObjectException {
		MeasurementPortTypeDatabase database = (MeasurementPortTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE);
		Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = super.createLoadIdsTransferable(ids, objects);
		if (loadIdsT.length == 0)
			return objects;

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MCMSessionEnvironment.getInstance().getMCMServantManager().getMServerReference();
			MeasurementPortType_Transferable[] transferables = mServerRef.transmitMeasurementPortTypes(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new MeasurementPortType(transferables[i]));
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
			Log.errorMessage("MCMConfigurationObjectLoader.loadMeasurementPortTypes | Cannot load objects from MeasurementServer");
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



	public Set loadMeasurementPorts(Set ids) throws RetrieveObjectException {
		MeasurementPortDatabase database = (MeasurementPortDatabase) DatabaseContext.getDatabase(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE);
		Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = super.createLoadIdsTransferable(ids, objects);
		if (loadIdsT.length == 0)
			return objects;

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MCMSessionEnvironment.getInstance().getMCMServantManager().getMServerReference();
			MeasurementPort_Transferable[] transferables = mServerRef.transmitMeasurementPorts(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new MeasurementPort(transferables[i]));
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
			Log.errorMessage("MCMConfigurationObjectLoader.loadMeasurementPorts | Cannot load objects from MeasurementServer");
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

	public Set loadKISs(Set ids) throws RetrieveObjectException {
		KISDatabase database = (KISDatabase) DatabaseContext.getDatabase(ObjectEntities.KIS_ENTITY_CODE);
		Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = super.createLoadIdsTransferable(ids, objects);
		if (loadIdsT.length == 0)
			return objects;
	
		Set loadedObjects = new HashSet();
	
		try {
			MServer mServerRef = MCMSessionEnvironment.getInstance().getMCMServantManager().getMServerReference();
			KIS_Transferable[] transferables = mServerRef.transmitKISs(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new KIS(transferables[i]));
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
			Log.errorMessage("MCMConfigurationObjectLoader.loadKISs | Cannot load objects from MeasurementServer");
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

	public Set loadMonitoredElements(Set ids) throws RetrieveObjectException {
		MonitoredElementDatabase database = (MonitoredElementDatabase) DatabaseContext.getDatabase(ObjectEntities.MONITORED_ELEMENT_ENTITY_CODE);
		Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = super.createLoadIdsTransferable(ids, objects);
		if (loadIdsT.length == 0)
			return objects;

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MCMSessionEnvironment.getInstance().getMCMServantManager().getMServerReference();
			MonitoredElement_Transferable[] transferables = mServerRef.transmitMonitoredElements(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new MonitoredElement(transferables[i]));
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
			Log.errorMessage("MCMConfigurationObjectLoader.loadMonitoredElements | Cannot load objects from MeasurementServer");
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




	/* Load multiple objects but ids*/

	public Set loadKISsButIds(StorableObjectCondition condition, Set ids) throws RetrieveObjectException {
		KISDatabase database = (KISDatabase) DatabaseContext.getDatabase(ObjectEntities.KIS_ENTITY_CODE);
		Set objects = super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
		Identifier_Transferable[] loadButIdsT = super.createLoadButIdsTransferable(ids, objects);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MCMSessionEnvironment.getInstance().getMCMServantManager().getMServerReference();
			KIS_Transferable[] transferables = mServerRef.transmitKISsButIdsByCondition(loadButIdsT, conditionT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new KIS(transferables[i]));
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
			Log.errorMessage("MCMConfigurationObjectLoader.loadKISsButIds | Cannot load objects from MeasurementServer");
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





	/*
	 * MCM do not need in all below methods
	 * */

	public Set loadCableLinkTypes(Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadCableLinkTypesButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadCableThreadTypes(Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadCableThreadTypesButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadEquipmentTypes(Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadEquipmentTypesButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadTransmissionPathTypes(Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadTransmissionPathTypesButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadPortTypes(Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadLinkTypes(Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadLinkTypesButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadPortTypesButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadCableThreads(Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadCableThreadsButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadEquipments(Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadEquipmentsButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadLinks(Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadLinksButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadMeasurementPortsButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadMeasurementPortTypesButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadMonitoredElementsButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadPorts(Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadPortsButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadTransmissionPaths(Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadTransmissionPathsButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}



	public void saveCableLinkTypes(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveCableThreads(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveCableThreadTypes(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveEquipments(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveEquipmentTypes(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveKISs(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveLinks(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveLinkTypes(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveMeasurementPorts(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveMeasurementPortTypes(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveMonitoredElements(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void savePorts(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void savePortTypes(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	
	public void saveTransmissionPaths(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
	public void saveTransmissionPathTypes(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}





	public Set refresh(Set storableObjects) {
		throw new UnsupportedOperationException("Method not implemented, objects: " + storableObjects);
	}





	public void delete(final Set identifiables) {
		throw new UnsupportedOperationException("Method not implemented, objects: " + identifiables);
	}

}
