/*
 * $Id: ConfigurationObjectLoader.java,v 1.38 2005/05/01 17:28:19 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.38 $, $Date: 2005/05/01 17:28:19 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public interface ConfigurationObjectLoader {

	/* Load multiple objects*/

	Set loadEquipmentTypes(Set ids) throws ApplicationException;

	Set loadPortTypes(Set ids) throws ApplicationException;

	Set loadMeasurementPortTypes(Set ids) throws ApplicationException;

	Set loadTransmissionPathTypes(Set ids) throws ApplicationException;

	Set loadLinkTypes(Set ids) throws ApplicationException;

	Set loadCableLinkTypes(Set ids) throws ApplicationException;

	Set loadCableThreadTypes(Set ids) throws ApplicationException;



	Set loadEquipments(Set ids) throws ApplicationException;

	Set loadPorts(Set ids) throws ApplicationException;

	Set loadMeasurementPorts(Set ids) throws ApplicationException;

	Set loadTransmissionPaths(Set ids) throws ApplicationException;

	Set loadKISs(Set ids) throws ApplicationException;

	Set loadMonitoredElements(Set ids) throws ApplicationException;

	Set loadLinks(Set ids) throws ApplicationException;

	Set loadCableThreads(Set ids) throws ApplicationException;



	/* Load multiple objects but ids*/

	Set loadEquipmentTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadMeasurementPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadTransmissionPathTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadCableLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadCableThreadTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;



	Set loadEquipmentsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadMeasurementPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadTransmissionPathsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadKISsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadMonitoredElementsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadLinksButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadCableThreadsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;



	/* Save multiple objects*/

	void saveEquipmentTypes(Set objects, boolean force) throws ApplicationException;

	void savePortTypes(Set objects, boolean force) throws ApplicationException;

	void saveMeasurementPortTypes(Set objects, boolean force) throws ApplicationException;

	void saveTransmissionPathTypes(Set objects, boolean force) throws ApplicationException;

	void saveLinkTypes(Set objects, boolean force) throws ApplicationException;

	void saveCableLinkTypes(Set objects, boolean force) throws ApplicationException;

	void saveCableThreadTypes(Set objects, boolean force) throws ApplicationException;



	void saveEquipments(Set objects, boolean force) throws ApplicationException;

	void savePorts(Set objects, boolean force) throws ApplicationException;

	void saveMeasurementPorts(Set objects, boolean force) throws ApplicationException;

	void saveTransmissionPaths(Set objects, boolean force) throws ApplicationException;

	void saveKISs(Set objects, boolean force) throws ApplicationException;

	void saveMonitoredElements(Set objects, boolean force) throws ApplicationException;

	void saveLinks(Set objects, boolean force) throws ApplicationException;

	void saveCableThreads(Set objects, boolean force) throws ApplicationException;



	/*	Refresh*/

	Set refresh(Set storableObjects) throws ApplicationException;



	/*	Delete*/

	void delete(final Set identifiables);

}
