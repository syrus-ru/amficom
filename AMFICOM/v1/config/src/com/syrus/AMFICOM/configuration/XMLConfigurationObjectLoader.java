/*
 * $Id: XMLConfigurationObjectLoader.java,v 1.11 2005/04/01 11:02:30 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectXML;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;

/**
 * @version $Revision: 1.11 $, $Date: 2005/04/01 11:02:30 $
 * @author $Author: bass $
 * @module configuration_v1
 */

public final class XMLConfigurationObjectLoader implements ConfigurationObjectLoader {

	private StorableObjectXML	configurationXML;

	public XMLConfigurationObjectLoader(final File path) {
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, "configuration");
		this.configurationXML = new StorableObjectXML(driver);
	}

	public void delete(Identifier id) throws IllegalDataException {
		this.configurationXML.delete(id);
		this.configurationXML.flush();
	}

	public void delete(Set ids) throws IllegalDataException {
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			this.configurationXML.delete(id);
		}
		this.configurationXML.flush();
	}

	public CableLinkType loadCableLinkType(Identifier id) throws ApplicationException {
		return (CableLinkType) this.loadStorableObject(id);
	}

	public Set loadCableLinkTypes(Set ids) throws ApplicationException {
		Set list = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Set loadCableLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public CableThread loadCableThread(Identifier id) throws ApplicationException {
		return (CableThread) this.loadStorableObject(id);
	}

	public Set loadCableThreads(Set ids) throws ApplicationException {
		Set list = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Set loadCableThreadsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public CableThreadType loadCableThreadType(Identifier id) throws ApplicationException {
		return (CableThreadType) this.loadStorableObject(id);
	}

	public Set loadCableThreadTypes(Set ids) throws ApplicationException {
		Set list = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Set loadCableThreadTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Equipment loadEquipment(Identifier id) throws ApplicationException {
		return (Equipment) this.loadStorableObject(id);
	}

	public Set loadEquipments(Set ids) throws ApplicationException {
		Set list = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Set loadEquipmentsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public EquipmentType loadEquipmentType(Identifier id) throws ApplicationException {
		return (EquipmentType) this.loadStorableObject(id);
	}

	public Set loadEquipmentTypes(Set ids) throws ApplicationException {
		Set list = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Set loadEquipmentTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public KIS loadKIS(Identifier id) throws ApplicationException {
		return (KIS) this.loadStorableObject(id);
	}

	public Set loadKISs(Set ids) throws ApplicationException {
		Set list = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Set loadKISsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Link loadLink(Identifier id) throws ApplicationException {
		return (Link) this.loadStorableObject(id);
	}

	public Set loadLinks(Set ids) throws ApplicationException {
		Set list = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Set loadLinksButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public LinkType loadLinkType(Identifier id) throws ApplicationException {
		return (LinkType) this.loadStorableObject(id);
	}

	public Set loadLinkTypes(Set ids) throws ApplicationException {
		Set list = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Set loadLinkTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public MeasurementPort loadMeasurementPort(Identifier id) throws ApplicationException {
		return (MeasurementPort) this.loadStorableObject(id);
	}

	public Set loadMeasurementPorts(Set ids) throws ApplicationException {
		Set list = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Set loadMeasurementPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public MeasurementPortType loadMeasurementPortType(Identifier id) throws ApplicationException {
		return (MeasurementPortType) this.loadStorableObject(id);
	}

	public Set loadMeasurementPortTypes(Set ids) throws ApplicationException {
		Set list = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Set loadMeasurementPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public MonitoredElement loadMonitoredElement(Identifier id) throws ApplicationException {
		return (MonitoredElement) this.loadStorableObject(id);
	}

	public Set loadMonitoredElements(Set ids) throws ApplicationException {
		Set list = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Set loadMonitoredElementsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Port loadPort(Identifier id) throws ApplicationException {
		return (Port) this.loadStorableObject(id);
	}

	public Set loadPorts(Set ids) throws ApplicationException {
		Set list = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Set loadPortsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public PortType loadPortType(Identifier id) throws ApplicationException {
		return (PortType) this.loadStorableObject(id);
	}

	public Set loadPortTypes(Set ids) throws ApplicationException {
		Set list = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Set loadPortTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public TransmissionPath loadTransmissionPath(Identifier id) throws ApplicationException {
		return (TransmissionPath) this.loadStorableObject(id);
	}

	public Set loadTransmissionPaths(Set ids) throws ApplicationException {
		Set list = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Set loadTransmissionPathsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public TransmissionPathType loadTransmissionPathType(Identifier id) throws ApplicationException {
		return (TransmissionPathType) this.loadStorableObject(id);
	}

	public Set loadTransmissionPathTypes(Set ids) throws ApplicationException {
		Set list = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Set loadTransmissionPathTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Set refresh(Set storableObjects) throws ApplicationException {
		// TODO Auto-generated method stub
		return Collections.EMPTY_SET;
	}

	public void saveCableLinkType(CableLinkType cableLinkType, boolean force) throws ApplicationException {
		this.saveStorableObject(cableLinkType, force);
		this.configurationXML.flush();

	}

	public void saveCableLinkTypes(Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);

	}

	public void saveCableThread(CableThread cableThread, boolean force) throws ApplicationException {
		this.saveStorableObject(cableThread, force);
		this.configurationXML.flush();
	}

	public void saveCableThreads(Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveCableThreadType(CableThreadType cableThreadType, boolean force) throws ApplicationException {
		this.saveStorableObject(cableThreadType, force);
		this.configurationXML.flush();
	}

	public void saveCableThreadTypes(Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveEquipment(Equipment equipment, boolean force) throws ApplicationException {
		this.saveStorableObject(equipment, force);
		this.configurationXML.flush();
	}

	public void saveEquipments(Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveEquipmentType(EquipmentType equipmentType, boolean force) throws ApplicationException {
		this.saveStorableObject(equipmentType, force);
		this.configurationXML.flush();
	}

	public void saveEquipmentTypes(Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveKIS(KIS kis, boolean force) throws ApplicationException {
		this.saveStorableObject(kis, force);
		this.configurationXML.flush();
	}

	public void saveKISs(Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveLink(Link link, boolean force) throws ApplicationException {
		this.saveStorableObject(link, force);
		this.configurationXML.flush();
	}

	public void saveLinks(Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveLinkType(LinkType linkType, boolean force) throws ApplicationException {
		this.saveStorableObject(linkType, force);
		this.configurationXML.flush();
	}

	public void saveLinkTypes(Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveMeasurementPort(MeasurementPort measurementPort, boolean force) throws ApplicationException {
		this.saveStorableObject(measurementPort, force);
		this.configurationXML.flush();
	}

	public void saveMeasurementPorts(Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveMeasurementPortType(MeasurementPortType measurementPortType, boolean force)
			throws ApplicationException {
		this.saveStorableObject(measurementPortType, force);
		this.configurationXML.flush();
	}

	public void saveMeasurementPortTypes(Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveMonitoredElement(MonitoredElement monitoredElement, boolean force)
			throws ApplicationException {
		this.saveStorableObject(monitoredElement, force);
		this.configurationXML.flush();
	}

	public void saveMonitoredElements(Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	public void savePort(Port port, boolean force) throws ApplicationException {
		this.saveStorableObject(port, force);
		this.configurationXML.flush();
	}

	public void savePorts(Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	public void savePortType(PortType portType, boolean force) throws ApplicationException {
		this.saveStorableObject(portType, force);
		this.configurationXML.flush();
	}

	public void savePortTypes(Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveTransmissionPath(TransmissionPath transmissionPath, boolean force)
			throws ApplicationException {
		this.saveStorableObject(transmissionPath, force);
		this.configurationXML.flush();
	}

	public void saveTransmissionPaths(Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveTransmissionPathType(TransmissionPathType transmissionPathType, boolean force)
			throws ApplicationException {
		this.saveStorableObject(transmissionPathType, force);
		this.configurationXML.flush();
	}

	public void saveTransmissionPathTypes(Set list, boolean force) throws ApplicationException {
		this.saveStorableObjects(list, force);
	}

	private StorableObject loadStorableObject(Identifier id) throws ApplicationException {
		return this.configurationXML.retrieve(id);
	}

	private Set loadStorableObjectButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.configurationXML.retrieveByCondition(ids, condition);
	}

	private void saveStorableObject(StorableObject storableObject, boolean force) throws ApplicationException {
		this.configurationXML.updateObject(storableObject, force, SessionContext.getAccessIdentity().getUserId());
	}

	private void saveStorableObjects(Set storableObjects, boolean force) throws ApplicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject, force);
		}
		this.configurationXML.flush();
	}
}
