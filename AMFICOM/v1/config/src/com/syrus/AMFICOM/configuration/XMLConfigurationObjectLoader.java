/*
 * $Id: XMLConfigurationObjectLoader.java,v 1.1 2005/01/31 14:44:49 bob Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/01/31 14:44:49 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public final class XMLConfigurationObjectLoader implements ConfigurationObjectLoader {

	private StorableObjectXML	configurationXML;

	public XMLConfigurationObjectLoader(final File path) {
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, "configuration");
		this.configurationXML = new StorableObjectXML(driver);
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

	public CableThreadType loadCableThreadType(Identifier id) throws DatabaseException, CommunicationException {
		return (CableThreadType) this.loadStorableObject(id);
	}

	public EquipmentType loadEquipmentType(Identifier id) throws DatabaseException, CommunicationException {
		return (EquipmentType) this.loadStorableObject(id);
	}

	public PortType loadPortType(Identifier id) throws DatabaseException, CommunicationException {
		return (PortType) this.loadStorableObject(id);
	}

	public MeasurementPortType loadMeasurementPortType(Identifier id) throws DatabaseException, CommunicationException {
		return (MeasurementPortType) this.loadStorableObject(id);
	}

	public LinkType loadLinkType(Identifier id) throws DatabaseException, CommunicationException {
		return (LinkType) this.loadStorableObject(id);
	}

	public TransmissionPathType loadTransmissionPathType(Identifier id) throws DatabaseException,
			CommunicationException {
		return (TransmissionPathType) this.loadStorableObject(id);
	}

	public Equipment loadEquipment(Identifier id) throws DatabaseException, CommunicationException {
		return (Equipment) this.loadStorableObject(id);
	}

	public Port loadPort(Identifier id) throws DatabaseException, CommunicationException {
		return (Port) this.loadStorableObject(id);
	}

	public TransmissionPath loadTransmissionPath(Identifier id) throws DatabaseException, CommunicationException {
		return (TransmissionPath) this.loadStorableObject(id);
	}

	public KIS loadKIS(Identifier id) throws DatabaseException, CommunicationException {
		return (KIS) this.loadStorableObject(id);
	}

	public Link loadLink(Identifier id) throws DatabaseException, CommunicationException {
		return (Link) this.loadStorableObject(id);
	}

	public MeasurementPort loadMeasurementPort(Identifier id) throws DatabaseException, CommunicationException {
		return (MeasurementPort) this.loadStorableObject(id);
	}

	public MonitoredElement loadMonitoredElement(Identifier id) throws DatabaseException, CommunicationException {
		return (MonitoredElement) this.loadStorableObject(id);
	}

	public List loadCableThreadTypes(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadEquipmentTypes(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadPortTypes(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadMeasurementPortTypes(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadLinkTypes(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadTransmissionPathTypes(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadEquipments(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadPorts(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadTransmissionPaths(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadKISs(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadLinks(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadMeasurementPorts(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadMonitoredElements(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
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

	public List loadCableThreadTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadEquipmentTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadPortTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadMeasurementPortTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadLinkTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadTransmissionPathTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadEquipmentsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadPortsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadTransmissionPathsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadKISsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadLinksButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadMeasurementPortsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadMonitoredElementsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	private void saveStorableObject(StorableObject storableObject) throws CommunicationException {
		Identifier id = storableObject.getId();
		try {
			this.configurationXML.updateObject(storableObject);
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

	public void saveCableThreadType(CableThreadType cableThreadType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(cableThreadType);
		this.configurationXML.flush();
	}

	public void saveEquipmentType(EquipmentType equipmentType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(equipmentType);
		this.configurationXML.flush();
	}

	public void savePortType(PortType portType, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(portType);
		this.configurationXML.flush();
	}

	public void saveMeasurementPortType(MeasurementPortType measurementPortType, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		this.saveStorableObject(measurementPortType);
		this.configurationXML.flush();
	}

	public void saveLinkType(LinkType linkType, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(linkType);
		this.configurationXML.flush();
	}

	public void saveTransmissionPathType(TransmissionPathType transmissionPathType, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		this.saveStorableObject(transmissionPathType);
		this.configurationXML.flush();
	}

	public void saveCableThread(CableThread cableThread, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(cableThread);
		this.configurationXML.flush();
	}

	public void saveEquipment(Equipment equipment, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(equipment);
		this.configurationXML.flush();
	}

	public void savePort(Port port, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(port);
		this.configurationXML.flush();
	}

	public void saveMeasurementPort(MeasurementPort measurementPort, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(measurementPort);
		this.configurationXML.flush();
	}

	public void saveLink(Link link, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(link);
		this.configurationXML.flush();
	}

	public void saveTransmissionPath(TransmissionPath transmissionPath, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		this.saveStorableObject(transmissionPath);
		this.configurationXML.flush();
	}

	public void saveKIS(KIS kis, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(kis);
		this.configurationXML.flush();
	}

	public void saveMonitoredElement(MonitoredElement monitoredElement, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		this.saveStorableObject(monitoredElement);
		this.configurationXML.flush();
	}

	private void saveStorableObjects(List storableObjects) throws CommunicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject);
		}
		this.configurationXML.flush();
	}

	public void saveCableThreadTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public void saveEquipmentTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public void savePortTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public void saveMeasurementPortTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public void saveLinkTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public void saveTransmissionPathTypes(List list, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObjects(list);
	}

	public void saveCableThreads(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public void saveEquipments(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public void savePorts(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public void saveMeasurementPorts(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public void saveLinks(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public void saveTransmissionPaths(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public void saveKISs(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public void saveMonitoredElements(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		// TODO Auto-generated method stub
		return null;
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
}
