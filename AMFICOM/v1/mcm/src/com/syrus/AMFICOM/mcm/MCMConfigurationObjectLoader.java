/*
 * $Id: MCMConfigurationObjectLoader.java,v 1.40 2005/06/01 20:54:59 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.ConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.DatabaseConfigurationObjectLoader;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.40 $, $Date: 2005/06/01 20:54:59 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

final class MCMConfigurationObjectLoader extends MCMObjectLoader implements ConfigurationObjectLoader {

	public MCMConfigurationObjectLoader(final MCMServantManager mcmServantManager) {
		super(mcmServantManager, new DatabaseConfigurationObjectLoader());
	}

	/* Load multiple objects*/

	public Set loadMeasurementPortTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitMeasurementPortTypes(idsT, sessionKey);
			}
		});
	}




	public Set loadMeasurementPorts(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.MEASUREMENTPORT_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitMeasurementPorts(idsT, sessionKey);
			}
		});
	}

	public Set loadKISs(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.KIS_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitKISs(idsT, sessionKey);
			}
		});
	}

	public Set loadMonitoredElements(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.MONITORED_ELEMENT_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitKISs(idsT, sessionKey);
			}
		});
	}




	/* Load multiple objects but ids*/

	public Set loadKISsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids,
				condition,
				ObjectEntities.KIS_ENTITY_CODE,
				new TransmitButIdsConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							Identifier_Transferable[] idsT,
							SessionKey_Transferable sessionKey,
							StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitKISsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
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
}
