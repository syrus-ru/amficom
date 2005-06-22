/*
 * $Id: ConfigurationObjectLoader.java,v 1.39 2005/06/22 19:23:01 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.39 $, $Date: 2005/06/22 19:23:01 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public interface ConfigurationObjectLoader {

	/* Load multiple objects*/

	Set loadEquipmentTypes(final Set<Identifier> ids) throws ApplicationException;

	Set loadPortTypes(final Set<Identifier> ids) throws ApplicationException;

	Set loadMeasurementPortTypes(final Set<Identifier> ids) throws ApplicationException;

	Set loadTransmissionPathTypes(final Set<Identifier> ids) throws ApplicationException;

	Set loadLinkTypes(final Set<Identifier> ids) throws ApplicationException;

	Set loadCableLinkTypes(final Set<Identifier> ids) throws ApplicationException;

	Set loadCableThreadTypes(final Set<Identifier> ids) throws ApplicationException;



	Set loadEquipments(final Set<Identifier> ids) throws ApplicationException;

	Set loadPorts(final Set<Identifier> ids) throws ApplicationException;

	Set loadMeasurementPorts(final Set<Identifier> ids) throws ApplicationException;

	Set loadTransmissionPaths(final Set<Identifier> ids) throws ApplicationException;

	Set loadKISs(final Set<Identifier> ids) throws ApplicationException;

	Set loadMonitoredElements(final Set<Identifier> ids) throws ApplicationException;

	Set loadLinks(final Set<Identifier> ids) throws ApplicationException;

	Set loadCableLinks(final Set<Identifier> ids) throws ApplicationException;

	Set loadCableThreads(final Set<Identifier> ids) throws ApplicationException;



	/* Load multiple objects but ids*/

	Set loadEquipmentTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadPortTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadMeasurementPortTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadTransmissionPathTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadLinkTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadCableLinkTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadCableThreadTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;



	Set loadEquipmentsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadPortsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadMeasurementPortsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadTransmissionPathsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadKISsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadMonitoredElementsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadLinksButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadCableLinksButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadCableThreadsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;



	/* Save multiple objects*/

	void saveEquipmentTypes(final Set<EquipmentType> objects, final boolean force) throws ApplicationException;

	void savePortTypes(final Set<PortType> objects, final boolean force) throws ApplicationException;

	void saveMeasurementPortTypes(final Set<MeasurementPortType> objects, final boolean force) throws ApplicationException;

	void saveTransmissionPathTypes(final Set<TransmissionPathType> objects, final boolean force) throws ApplicationException;

	void saveLinkTypes(final Set<LinkType> objects, final boolean force) throws ApplicationException;

	void saveCableLinkTypes(final Set<CableLinkType> objects, final boolean force) throws ApplicationException;

	void saveCableThreadTypes(final Set<CableThreadType> objects, final boolean force) throws ApplicationException;



	void saveEquipments(final Set<Equipment> objects, final boolean force) throws ApplicationException;

	void savePorts(final Set<Port> objects, final boolean force) throws ApplicationException;

	void saveMeasurementPorts(final Set<MeasurementPort> objects, final boolean force) throws ApplicationException;

	void saveTransmissionPaths(final Set<TransmissionPath> objects, final boolean force) throws ApplicationException;

	void saveKISs(final Set<KIS> objects, final boolean force) throws ApplicationException;

	void saveMonitoredElements(final Set<MonitoredElement> objects, final boolean force) throws ApplicationException;

	void saveLinks(final Set<Link> objects, final boolean force) throws ApplicationException;

	void saveCableLinks(final Set<CableLink> objects, final boolean force) throws ApplicationException;

	void saveCableThreads(final Set<CableThread> objects, final boolean force) throws ApplicationException;



	/*	Refresh*/

	Set refresh(final Set<? extends StorableObject> storableObjects) throws ApplicationException;



	/*	Delete*/

	void delete(final Set<? extends Identifiable> identifiables);

}
