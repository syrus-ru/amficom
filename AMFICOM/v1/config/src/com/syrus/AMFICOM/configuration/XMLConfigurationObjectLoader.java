/*
 * $Id: XMLConfigurationObjectLoader.java,v 1.3 2005/02/11 10:52:50 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.*;

/**
 * @version $Revision: 1.3 $, $Date: 2005/02/11 10:52:50 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public final class XMLConfigurationObjectLoader implements ConfigurationObjectLoader {

	private StorableObjectXML	configurationXML;

	public XMLConfigurationObjectLoader(final File path) {
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, "configuration");
		this.configurationXML = new StorableObjectXML(driver);
	}

	public void delete(Identifier id) throws CommunicationException, DatabaseException {
		try {
			this.configurationXML.delete(id);
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLConfigurationObjectLoader.delete | caught " + e.getMessage(), e);
		}
		this.configurationXML.flush();
	}

	public void delete(List ids) throws CommunicationException, DatabaseException {
		try {
			for (Iterator it = ids.iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				this.configurationXML.delete(id);
			}
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLConfigurationObjectLoader.delete | caught " + e.getMessage(), e);
		}
		this.configurationXML.flush();
	}

	public CableLinkType loadCableLinkType(Identifier id) throws DatabaseException, CommunicationException {
		return (CableLinkType) this.loadStorableObject(id);
	}

	public List loadCableLinkTypes(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadCableLinkTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public CableThread loadCableThread(Identifier id) throws DatabaseException, CommunicationException {
		return (CableThread) this.loadStorableObject(id);
	}

	public List loadCableThreads(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadCableThreadsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public CableThreadType loadCableThreadType(Identifier id) throws DatabaseException, CommunicationException {
		return (CableThreadType) this.loadStorableObject(id);
	}

	public List loadCableThreadTypes(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadCableThreadTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Equipment loadEquipment(Identifier id) throws DatabaseException, CommunicationException {
		return (Equipment) this.loadStorableObject(id);
	}

	public List loadEquipments(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadEquipmentsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public EquipmentType loadEquipmentType(Identifier id) throws DatabaseException, CommunicationException {
		return (EquipmentType) this.loadStorableObject(id);
	}

	public List loadEquipmentTypes(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadEquipmentTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public KIS loadKIS(Identifier id) throws DatabaseException, CommunicationException {
		return (KIS) this.loadStorableObject(id);
	}

	public List loadKISs(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadKISsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Link loadLink(Identifier id) throws DatabaseException, CommunicationException {
		return (Link) this.loadStorableObject(id);
	}

	public List loadLinks(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadLinksButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public LinkType loadLinkType(Identifier id) throws DatabaseException, CommunicationException {
		return (LinkType) this.loadStorableObject(id);
	}

	public List loadLinkTypes(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadLinkTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public MeasurementPort loadMeasurementPort(Identifier id) throws DatabaseException, CommunicationException {
		return (MeasurementPort) this.loadStorableObject(id);
	}

	public List loadMeasurementPorts(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadMeasurementPortsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public MeasurementPortType loadMeasurementPortType(Identifier id) throws DatabaseException, CommunicationException {
		return (MeasurementPortType) this.loadStorableObject(id);
	}

	public List loadMeasurementPortTypes(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadMeasurementPortTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public MonitoredElement loadMonitoredElement(Identifier id) throws DatabaseException, CommunicationException {
		return (MonitoredElement) this.loadStorableObject(id);
	}

	public List loadMonitoredElements(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadMonitoredElementsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Port loadPort(Identifier id) throws DatabaseException, CommunicationException {
		return (Port) this.loadStorableObject(id);
	}

	public List loadPorts(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadPortsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public PortType loadPortType(Identifier id) throws DatabaseException, CommunicationException {
		return (PortType) this.loadStorableObject(id);
	}

	public List loadPortTypes(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadPortTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public TransmissionPath loadTransmissionPath(Identifier id) throws DatabaseException, CommunicationException {
		return (TransmissionPath) this.loadStorableObject(id);
	}

	public List loadTransmissionPaths(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadTransmissionPathsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public TransmissionPathType loadTransmissionPathType(Identifier id) throws DatabaseException,
			CommunicationException {
		return (TransmissionPathType) this.loadStorableObject(id);
	}

	public List loadTransmissionPathTypes(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadTransmissionPathTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveCableLinkType(CableLinkType cableLinkType, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(cableLinkType, accessIdentity);
		this.configurationXML.flush();

	}

	public void saveCableLinkTypes(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);

	}

	public void saveCableThread(CableThread cableThread, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(cableThread, accessIdentity);
		this.configurationXML.flush();
	}

	public void saveCableThreads(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);
	}

	public void saveCableThreadType(CableThreadType cableThreadType, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(cableThreadType, accessIdentity);
		this.configurationXML.flush();
	}

	public void saveCableThreadTypes(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);
	}

	public void saveEquipment(Equipment equipment, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(equipment, accessIdentity);
		this.configurationXML.flush();
	}

	public void saveEquipments(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);
	}

	public void saveEquipmentType(EquipmentType equipmentType, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(equipmentType, accessIdentity);
		this.configurationXML.flush();
	}

	public void saveEquipmentTypes(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);
	}

	public void saveKIS(KIS kis, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(kis, accessIdentity);
		this.configurationXML.flush();
	}

	public void saveKISs(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);
	}

	public void saveLink(Link link, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(link, accessIdentity);
		this.configurationXML.flush();
	}

	public void saveLinks(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);
	}

	public void saveLinkType(LinkType linkType, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(linkType, accessIdentity);
		this.configurationXML.flush();
	}

	public void saveLinkTypes(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);
	}

	public void saveMeasurementPort(MeasurementPort measurementPort, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(measurementPort, accessIdentity);
		this.configurationXML.flush();
	}

	public void saveMeasurementPorts(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);
	}

	public void saveMeasurementPortType(MeasurementPortType measurementPortType, AccessIdentity accessIdentity, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		this.saveStorableObject(measurementPortType, accessIdentity);
		this.configurationXML.flush();
	}

	public void saveMeasurementPortTypes(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);
	}

	public void saveMonitoredElement(MonitoredElement monitoredElement, AccessIdentity accessIdentity, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		this.saveStorableObject(monitoredElement, accessIdentity);
		this.configurationXML.flush();
	}

	public void saveMonitoredElements(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);
	}

	public void savePort(Port port, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(port, accessIdentity);
		this.configurationXML.flush();
	}

	public void savePorts(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);
	}

	public void savePortType(PortType portType, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(portType, accessIdentity);
		this.configurationXML.flush();
	}

	public void savePortTypes(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);
	}

	public void saveTransmissionPath(TransmissionPath transmissionPath, AccessIdentity accessIdentity, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		this.saveStorableObject(transmissionPath, accessIdentity);
		this.configurationXML.flush();
	}

	public void saveTransmissionPaths(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);
	}

	public void saveTransmissionPathType(TransmissionPathType transmissionPathType, AccessIdentity accessIdentity, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		this.saveStorableObject(transmissionPathType, accessIdentity);
		this.configurationXML.flush();
	}

	public void saveTransmissionPathTypes(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObjects(list, accessIdentity);
	}

	private StorableObject loadStorableObject(Identifier id) throws CommunicationException {
		try {
			return this.configurationXML.retrieve(id);
		} catch (ObjectNotFoundException e) {
			throw new CommunicationException("XMLConfigurationObjectLoader.load"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		} catch (RetrieveObjectException e) {
			throw new CommunicationException("XMLConfigurationObjectLoader.load"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLConfigurationObjectLoader.load"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		}
	}

	private List loadStorableObjectButIds(StorableObjectCondition condition, List ids) throws CommunicationException {
		try {
			return this.configurationXML.retrieveByCondition(ids, condition);
		} catch (RetrieveObjectException e) {
			throw new CommunicationException("XMLConfigurationObjectLoader.loadParameterTypesButIds | caught "
					+ e.getMessage(), e);
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLConfigurationObjectLoader.loadParameterTypesButIds | caught "
					+ e.getMessage(), e);
		}
	}

	private void saveStorableObject(StorableObject storableObject, AccessIdentity accessIdentity) throws CommunicationException {
		Identifier id = storableObject.getId();
		try {
			this.configurationXML.updateObject(storableObject, accessIdentity.getUserId());
		} catch (UpdateObjectException e) {
			throw new CommunicationException("XMLConfigurationObjectLoader.save"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLConfigurationObjectLoader.save"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		} catch (VersionCollisionException e) {
			throw new CommunicationException("XMLConfigurationObjectLoader.save"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		}
	}

	private void saveStorableObjects(List storableObjects, AccessIdentity accessIdentity) throws CommunicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject, accessIdentity);
		}
		this.configurationXML.flush();
	}
}
