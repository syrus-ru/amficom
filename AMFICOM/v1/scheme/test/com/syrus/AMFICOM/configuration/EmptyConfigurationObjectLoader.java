/*-
 * $Id: EmptyConfigurationObjectLoader.java,v 1.2 2005/07/24 17:07:45 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/07/24 17:07:45 $
 * @module scheme
 */
public final class EmptyConfigurationObjectLoader implements
		ConfigurationObjectLoader {

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadEquipmentTypes(java.util.Set)
	 */
	public Set loadEquipmentTypes(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadPortTypes(java.util.Set)
	 */
	public Set loadPortTypes(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadMeasurementPortTypes(java.util.Set)
	 */
	public Set loadMeasurementPortTypes(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadTransmissionPathTypes(java.util.Set)
	 */
	public Set loadTransmissionPathTypes(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadLinkTypes(java.util.Set)
	 */
	public Set loadLinkTypes(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadCableLinkTypes(java.util.Set)
	 */
	public Set loadCableLinkTypes(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadCableThreadTypes(java.util.Set)
	 */
	public Set loadCableThreadTypes(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadEquipments(java.util.Set)
	 */
	public Set loadEquipments(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadPorts(java.util.Set)
	 */
	public Set loadPorts(Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadMeasurementPorts(java.util.Set)
	 */
	public Set loadMeasurementPorts(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadTransmissionPaths(java.util.Set)
	 */
	public Set loadTransmissionPaths(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadKISs(java.util.Set)
	 */
	public Set loadKISs(Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadMonitoredElements(java.util.Set)
	 */
	public Set loadMonitoredElements(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadLinks(java.util.Set)
	 */
	public Set loadLinks(Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadCableLinks(java.util.Set)
	 */
	public Set loadCableLinks(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadCableThreads(java.util.Set)
	 */
	public Set loadCableThreads(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param condition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadEquipmentTypesButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadEquipmentTypesButIds(StorableObjectCondition condition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param condition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadPortTypesButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadPortTypesButIds(StorableObjectCondition condition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param condition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadMeasurementPortTypesButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadMeasurementPortTypesButIds(
			StorableObjectCondition condition, Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param condition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadTransmissionPathTypesButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadTransmissionPathTypesButIds(
			StorableObjectCondition condition, Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param condition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadLinkTypesButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadLinkTypesButIds(StorableObjectCondition condition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param condition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadCableLinkTypesButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadCableLinkTypesButIds(StorableObjectCondition condition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param condition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadCableThreadTypesButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadCableThreadTypesButIds(
			StorableObjectCondition condition, Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param condition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadEquipmentsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadEquipmentsButIds(StorableObjectCondition condition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param condition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadPortsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadPortsButIds(StorableObjectCondition condition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param condition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadMeasurementPortsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadMeasurementPortsButIds(
			StorableObjectCondition condition, Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param condition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadTransmissionPathsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadTransmissionPathsButIds(
			StorableObjectCondition condition, Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param condition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadKISsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadKISsButIds(StorableObjectCondition condition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param condition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadMonitoredElementsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadMonitoredElementsButIds(
			StorableObjectCondition condition, Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param condition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadLinksButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadLinksButIds(StorableObjectCondition condition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param condition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadCableLinksButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadCableLinksButIds(StorableObjectCondition condition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param condition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#loadCableThreadsButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadCableThreadsButIds(StorableObjectCondition condition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param objects
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#saveEquipmentTypes(java.util.Set, boolean)
	 */
	public void saveEquipmentTypes(Set<EquipmentType> objects, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param objects
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#savePortTypes(java.util.Set, boolean)
	 */
	public void savePortTypes(Set<PortType> objects, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param objects
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#saveMeasurementPortTypes(java.util.Set, boolean)
	 */
	public void saveMeasurementPortTypes(Set<MeasurementPortType> objects,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param objects
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#saveTransmissionPathTypes(java.util.Set, boolean)
	 */
	public void saveTransmissionPathTypes(
			Set<TransmissionPathType> objects, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param objects
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#saveLinkTypes(java.util.Set, boolean)
	 */
	public void saveLinkTypes(Set<LinkType> objects, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param objects
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#saveCableLinkTypes(java.util.Set, boolean)
	 */
	public void saveCableLinkTypes(Set<CableLinkType> objects, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param objects
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#saveCableThreadTypes(java.util.Set, boolean)
	 */
	public void saveCableThreadTypes(Set<CableThreadType> objects,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param objects
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#saveEquipments(java.util.Set, boolean)
	 */
	public void saveEquipments(Set<Equipment> objects, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param objects
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#savePorts(java.util.Set, boolean)
	 */
	public void savePorts(Set<Port> objects, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param objects
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#saveMeasurementPorts(java.util.Set, boolean)
	 */
	public void saveMeasurementPorts(Set<MeasurementPort> objects,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param objects
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#saveTransmissionPaths(java.util.Set, boolean)
	 */
	public void saveTransmissionPaths(Set<TransmissionPath> objects,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param objects
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#saveKISs(java.util.Set, boolean)
	 */
	public void saveKISs(Set<KIS> objects, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param objects
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#saveMonitoredElements(java.util.Set, boolean)
	 */
	public void saveMonitoredElements(Set<MonitoredElement> objects,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param objects
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#saveLinks(java.util.Set, boolean)
	 */
	public void saveLinks(Set<Link> objects, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param objects
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#saveCableLinks(java.util.Set, boolean)
	 */
	public void saveCableLinks(Set<CableLink> objects, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param objects
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#saveCableThreads(java.util.Set, boolean)
	 */
	public void saveCableThreads(Set<CableThread> objects, boolean force)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjects
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#refresh(java.util.Set)
	 */
	public Set refresh(Set< ? extends StorableObject> storableObjects)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param identifiables
	 * @see com.syrus.AMFICOM.configuration.ConfigurationObjectLoader#delete(java.util.Set)
	 */
	public void delete(Set< ? extends Identifiable> identifiables) {
		throw new UnsupportedOperationException();
	}

}
