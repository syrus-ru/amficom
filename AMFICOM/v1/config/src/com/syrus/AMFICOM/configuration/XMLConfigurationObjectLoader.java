/*
 * $Id: XMLConfigurationObjectLoader.java,v 1.6 2005/02/15 07:11:39 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectXML;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.6 $, $Date: 2005/02/15 07:11:39 $
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

	public void delete(Collection ids) throws CommunicationException, DatabaseException {
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

	public Collection loadCableLinkTypes(Collection ids) throws DatabaseException, CommunicationException {
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Collection loadCableLinkTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public CableThread loadCableThread(Identifier id) throws DatabaseException, CommunicationException {
		return (CableThread) this.loadStorableObject(id);
	}

	public Collection loadCableThreads(Collection ids) throws DatabaseException, CommunicationException {
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Collection loadCableThreadsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public CableThreadType loadCableThreadType(Identifier id) throws DatabaseException, CommunicationException {
		return (CableThreadType) this.loadStorableObject(id);
	}

	public Collection loadCableThreadTypes(Collection ids) throws DatabaseException, CommunicationException {
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Collection loadCableThreadTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Equipment loadEquipment(Identifier id) throws DatabaseException, CommunicationException {
		return (Equipment) this.loadStorableObject(id);
	}

	public Collection loadEquipments(Collection ids) throws DatabaseException, CommunicationException {
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Collection loadEquipmentsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public EquipmentType loadEquipmentType(Identifier id) throws DatabaseException, CommunicationException {
		return (EquipmentType) this.loadStorableObject(id);
	}

	public Collection loadEquipmentTypes(Collection ids) throws DatabaseException, CommunicationException {
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Collection loadEquipmentTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public KIS loadKIS(Identifier id) throws DatabaseException, CommunicationException {
		return (KIS) this.loadStorableObject(id);
	}

	public Collection loadKISs(Collection ids) throws DatabaseException, CommunicationException {
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Collection loadKISsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Link loadLink(Identifier id) throws DatabaseException, CommunicationException {
		return (Link) this.loadStorableObject(id);
	}

	public Collection loadLinks(Collection ids) throws DatabaseException, CommunicationException {
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Collection loadLinksButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public LinkType loadLinkType(Identifier id) throws DatabaseException, CommunicationException {
		return (LinkType) this.loadStorableObject(id);
	}

	public Collection loadLinkTypes(Collection ids) throws DatabaseException, CommunicationException {
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Collection loadLinkTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public MeasurementPort loadMeasurementPort(Identifier id) throws DatabaseException, CommunicationException {
		return (MeasurementPort) this.loadStorableObject(id);
	}

	public Collection loadMeasurementPorts(Collection ids) throws DatabaseException, CommunicationException {
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Collection loadMeasurementPortsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public MeasurementPortType loadMeasurementPortType(Identifier id) throws DatabaseException, CommunicationException {
		return (MeasurementPortType) this.loadStorableObject(id);
	}

	public Collection loadMeasurementPortTypes(Collection ids) throws DatabaseException, CommunicationException {
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Collection loadMeasurementPortTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public MonitoredElement loadMonitoredElement(Identifier id) throws DatabaseException, CommunicationException {
		return (MonitoredElement) this.loadStorableObject(id);
	}

	public Collection loadMonitoredElements(Collection ids) throws DatabaseException, CommunicationException {
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Collection loadMonitoredElementsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Port loadPort(Identifier id) throws DatabaseException, CommunicationException {
		return (Port) this.loadStorableObject(id);
	}

	public Collection loadPorts(Collection ids) throws DatabaseException, CommunicationException {
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Collection loadPortsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public PortType loadPortType(Identifier id) throws DatabaseException, CommunicationException {
		return (PortType) this.loadStorableObject(id);
	}

	public Collection loadPortTypes(Collection ids) throws DatabaseException, CommunicationException {
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Collection loadPortTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public TransmissionPath loadTransmissionPath(Identifier id) throws DatabaseException, CommunicationException {
		return (TransmissionPath) this.loadStorableObject(id);
	}

	public Collection loadTransmissionPaths(Collection ids) throws DatabaseException, CommunicationException {
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Collection loadTransmissionPathsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public TransmissionPathType loadTransmissionPathType(Identifier id) throws DatabaseException,
			CommunicationException {
		return (TransmissionPathType) this.loadStorableObject(id);
	}

	public Collection loadTransmissionPathTypes(Collection ids) throws DatabaseException, CommunicationException {
		Collection list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Collection loadTransmissionPathTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveCableLinkType(CableLinkType cableLinkType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(cableLinkType, force);
		this.configurationXML.flush();

	}

	public void saveCableLinkTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, force);

	}

	public void saveCableThread(CableThread cableThread, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(cableThread, force);
		this.configurationXML.flush();
	}

	public void saveCableThreads(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveCableThreadType(CableThreadType cableThreadType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(cableThreadType, force);
		this.configurationXML.flush();
	}

	public void saveCableThreadTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveEquipment(Equipment equipment, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(equipment, force);
		this.configurationXML.flush();
	}

	public void saveEquipments(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveEquipmentType(EquipmentType equipmentType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(equipmentType, force);
		this.configurationXML.flush();
	}

	public void saveEquipmentTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveKIS(KIS kis, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(kis, force);
		this.configurationXML.flush();
	}

	public void saveKISs(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveLink(Link link, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(link, force);
		this.configurationXML.flush();
	}

	public void saveLinks(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveLinkType(LinkType linkType, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(linkType, force);
		this.configurationXML.flush();
	}

	public void saveLinkTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveMeasurementPort(MeasurementPort measurementPort, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(measurementPort, force);
		this.configurationXML.flush();
	}

	public void saveMeasurementPorts(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveMeasurementPortType(MeasurementPortType measurementPortType, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		this.saveStorableObject(measurementPortType, force);
		this.configurationXML.flush();
	}

	public void saveMeasurementPortTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveMonitoredElement(MonitoredElement monitoredElement, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		this.saveStorableObject(monitoredElement, force);
		this.configurationXML.flush();
	}

	public void saveMonitoredElements(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, force);
	}

	public void savePort(Port port, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(port, force);
		this.configurationXML.flush();
	}

	public void savePorts(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, force);
	}

	public void savePortType(PortType portType, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(portType, force);
		this.configurationXML.flush();
	}

	public void savePortTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveTransmissionPath(TransmissionPath transmissionPath, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		this.saveStorableObject(transmissionPath, force);
		this.configurationXML.flush();
	}

	public void saveTransmissionPaths(Collection list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, force);
	}

	public void saveTransmissionPathType(TransmissionPathType transmissionPathType, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		this.saveStorableObject(transmissionPathType, force);
		this.configurationXML.flush();
	}

	public void saveTransmissionPathTypes(Collection list, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObjects(list, force);
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

	private Collection loadStorableObjectButIds(StorableObjectCondition condition, Collection ids) throws CommunicationException {
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

	private void saveStorableObject(StorableObject storableObject, boolean force) throws CommunicationException {
		Identifier id = storableObject.getId();
		try {
			this.configurationXML.updateObject(storableObject, force, SessionContext.getAccessIdentity().getUserId());
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

	private void saveStorableObjects(Collection storableObjects, boolean force) throws CommunicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject, force);
		}
		this.configurationXML.flush();
	}
}
