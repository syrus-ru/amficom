/*
 * $Id: MCMConfigurationObjectLoader.java,v 1.47 2005/06/17 13:06:56 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.ConfigurationObjectLoader;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.47 $, $Date: 2005/06/17 13:06:56 $
 * @author $Author: bass $
 * @module mcm_v1
 */

final class MCMConfigurationObjectLoader extends MCMObjectLoader implements ConfigurationObjectLoader {

	public MCMConfigurationObjectLoader(final MCMServantManager mcmServantManager) {
		super(mcmServantManager);
	}

	/* Load multiple objects*/

	public Set loadMeasurementPortTypes(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MEASUREMENTPORT_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					IdlIdentifier[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitMeasurementPortTypes(idsT, sessionKey);
			}
		});
	}




	public Set loadMeasurementPorts(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MEASUREMENTPORT_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					IdlIdentifier[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitMeasurementPorts(idsT, sessionKey);
			}
		});
	}

	public Set loadKISs(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.KIS_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					IdlIdentifier[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitKISs(idsT, sessionKey);
			}
		});
	}

	public Set loadMonitoredElements(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MONITOREDELEMENT_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					IdlIdentifier[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitKISs(idsT, sessionKey);
			}
		});
	}




	/* Load multiple objects but ids by condition*/

	public Set loadKISsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.KIS_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							IdlIdentifier[] idsT,
							SessionKey_Transferable sessionKey,
							StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitKISsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}





	/*
	 * MCM do not need in all below methods
	 * */
	
	public Set loadEquipmentTypes(final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public Set loadPortTypes(final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public Set loadTransmissionPathTypes(final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public Set loadLinkTypes(final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public Set loadCableLinkTypes(final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public Set loadCableThreadTypes(final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public Set loadEquipments(final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public Set loadPorts(final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public Set loadTransmissionPaths(final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public Set loadLinks(final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public Set loadCableThreads(final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}



	public Set loadEquipmentTypesButIds(final StorableObjectCondition condition, final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadPortTypesButIds(final StorableObjectCondition condition, final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadMeasurementPortTypesButIds(final StorableObjectCondition condition, final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadTransmissionPathTypesButIds(final StorableObjectCondition condition, final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadLinkTypesButIds(final StorableObjectCondition condition, final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadCableLinkTypesButIds(final StorableObjectCondition condition, final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadCableThreadTypesButIds(final StorableObjectCondition condition, final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadEquipmentsButIds(final StorableObjectCondition condition, final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadPortsButIds(final StorableObjectCondition condition, final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadMeasurementPortsButIds(final StorableObjectCondition condition, final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadTransmissionPathsButIds(final StorableObjectCondition condition, final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadMonitoredElementsButIds(final StorableObjectCondition condition, final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadCableThreadsButIds(final StorableObjectCondition condition, final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadLinksButIds(final StorableObjectCondition condition, final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}



	public void saveCableLinkTypes(final Set objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveCableThreads(final Set objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveCableThreadTypes(final Set objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveEquipments(final Set objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}
	
	public void saveEquipmentTypes(final Set objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}
	
	public void saveKISs(final Set objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}
	
	public void saveLinks(final Set objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}
	
	public void saveLinkTypes(final Set objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}
	
	public void saveMeasurementPorts(final Set objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}
	
	public void saveMeasurementPortTypes(final Set objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}
	
	public void saveMonitoredElements(final Set objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}
	
	public void savePorts(final Set objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}
	
	public void savePortTypes(final Set objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}
	
	public void saveTransmissionPaths(final Set objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveTransmissionPathTypes(final Set objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}
}
