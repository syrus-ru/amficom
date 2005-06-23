/*
 * $Id: MCMConfigurationObjectLoader.java,v 1.49 2005/06/23 12:35:04 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.CableLink;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableThread;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.ConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.TransmissionPathType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.49 $, $Date: 2005/06/23 12:35:04 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

final class MCMConfigurationObjectLoader extends MCMObjectLoader implements ConfigurationObjectLoader {

	public MCMConfigurationObjectLoader(final MCMServantManager mcmServantManager) {
		super(mcmServantManager);
	}

	/* Load multiple objects*/

	public Set loadMeasurementPortTypes(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MEASUREMENTPORT_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					IdlIdentifier[] idsT,
					IdlSessionKey sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitMeasurementPortTypes(idsT, sessionKey);
			}
		});
	}




	public Set loadMeasurementPorts(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MEASUREMENTPORT_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					IdlIdentifier[] idsT,
					IdlSessionKey sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitMeasurementPorts(idsT, sessionKey);
			}
		});
	}

	public Set loadKISs(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.KIS_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					IdlIdentifier[] idsT,
					IdlSessionKey sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitKISs(idsT, sessionKey);
			}
		});
	}

	public Set loadMonitoredElements(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MONITOREDELEMENT_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					IdlIdentifier[] idsT,
					IdlSessionKey sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitKISs(idsT, sessionKey);
			}
		});
	}




	/* Load multiple objects but ids by condition*/

	public Set loadKISsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.KIS_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							IdlIdentifier[] idsT,
							IdlSessionKey sessionKey,
							IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitKISsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}





	/*
	 * MCM do not need in all below methods
	 * */
	
	public Set loadEquipmentTypes(final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public Set loadPortTypes(final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public Set loadTransmissionPathTypes(final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public Set loadLinkTypes(final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public Set loadCableLinkTypes(final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public Set loadCableThreadTypes(final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public Set loadEquipments(final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public Set loadPorts(final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public Set loadTransmissionPaths(final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public Set loadLinks(final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public Set loadCableLinks(final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public Set loadCableThreads(final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}



	public Set loadEquipmentTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadPortTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadMeasurementPortTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadTransmissionPathTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadLinkTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadCableLinkTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadCableThreadTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}



	public Set loadEquipmentsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadPortsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadMeasurementPortsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadTransmissionPathsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadMonitoredElementsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadLinksButIds(final StorableObjectCondition condition, final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadCableLinksButIds(final StorableObjectCondition condition, final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadCableThreadsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}



	public void saveEquipmentTypes(final Set<EquipmentType> objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void savePortTypes(final Set<PortType> objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveMeasurementPortTypes(final Set<MeasurementPortType> objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveTransmissionPathTypes(final Set<TransmissionPathType> objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveLinkTypes(final Set<LinkType> objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveCableLinkTypes(final Set<CableLinkType> objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveCableThreadTypes(final Set<CableThreadType> objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}



	public void saveEquipments(final Set<Equipment> objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void savePorts(final Set<Port> objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveMeasurementPorts(final Set<MeasurementPort> objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveTransmissionPaths(final Set<TransmissionPath> objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveKISs(final Set<KIS> objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveMonitoredElements(final Set<MonitoredElement> objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveLinks(final Set<Link> objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveCableLinks(final Set<CableLink> objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveCableThreads(final Set<CableThread> objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}
}
