/*-
 * $Id: Collector.java,v 1.52 2005/06/25 17:07:48 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.XMLBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.map.corba.IdlCollector;

/**
 * Коллектор на топологической схеме, который характеризуется набором входящих
 * в него линий. Линии не обязаны быть связными.
 *
 * @author $Author: bass $
 * @version $Revision: 1.52 $, $Date: 2005/06/25 17:07:48 $
 * @module map_v1
 */
public final class Collector extends StorableObject implements MapElement, XMLBeansTransferable {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 4049922679379212598L;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_LINKS = "links";

	/**
	 * набор параметров для экспорта. инициализируется только в случае
	 * необходимости экспорта
	 */
	private static java.util.Map exportMap = null;

	private String name;
	private String description;

	private Set<PhysicalLink> physicalLinks;
	private Set<Characteristic> characteristics;

	protected transient boolean selected = false;
	protected transient boolean removed = false;
	protected transient boolean alarmState = false;

	Collector(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		this.physicalLinks = new HashSet();
		this.characteristics = new HashSet();

		final CollectorDatabase database = (CollectorDatabase) DatabaseContext.getDatabase(ObjectEntities.COLLECTOR_CODE);
		try {
			database.retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	Collector(final IdlCollector ct) throws CreateObjectException {
		try {
			this.fromTransferable(ct);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	Collector(final Identifier id,
			final Identifier creatorId,
			final long version,
			final String name,
			final String description) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.name = name;
		this.description = description;

		this.physicalLinks = new HashSet();
		this.characteristics = new HashSet();
	}

	public static Collector createInstance(final Identifier creatorId, final Map map, final String name, final String description)
			throws CreateObjectException {

		if (creatorId == null || map == null || name == null || description == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			Collector collector = new Collector(IdentifierPool.getGeneratedIdentifier(ObjectEntities.COLLECTOR_CODE),
					creatorId,
					0L,
					name,
					description);

			assert collector.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			collector.markAsChanged();

			return collector;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		IdlCollector ct = (IdlCollector) transferable;
		super.fromTransferable(ct.header);

		this.name = ct.name;
		this.description = ct.description;

		Set ids;

		ids = Identifier.fromTransferables(ct.physicalLinkIds);
		this.physicalLinks = StorableObjectPool.getStorableObjects(ids, true);

		ids = Identifier.fromTransferables(ct.characteristicIds);
		this.characteristics = StorableObjectPool.getStorableObjects(ids, true);
	}

	public Set getDependencies() {
		Set dependencies = new HashSet();
		dependencies.addAll(this.physicalLinks);
		return dependencies;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlCollector getTransferable(final ORB orb) {
		IdlIdentifier[] physicalLinkIds = Identifier.createTransferables(this.physicalLinks);
		IdlIdentifier[] charIds = Identifier.createTransferables(this.characteristics);
		return new IdlCollector(super.getHeaderTransferable(orb), this.name, this.description, physicalLinkIds, charIds);
	}

	public String getDescription() {
		return this.description;
	}

	protected void setDescription0(final String description) {
		this.description = description;
	}

	public void setDescription(final String description) {
		this.setDescription0(description);
		super.markAsChanged();
	}

	public String getName() {
		return this.name;
	}

	protected void setName0(final String name) {
		this.name = name;
	}

	public void setName(final String name) {
		this.setName0(name);
		super.markAsChanged();
	}

	public Set getPhysicalLinks() {
		return Collections.unmodifiableSet(this.physicalLinks);
	}

	protected void setPhysicalLinks0(final Set physicalLinks) {
		this.physicalLinks.clear();
		if (physicalLinks != null)
			this.physicalLinks.addAll(physicalLinks);
	}

	public void setPhysicalLinks(final Set physicalLinks) {
		this.setPhysicalLinks0(physicalLinks);
		super.markAsChanged();
	}

	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final String name,
			final String description) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
	}

	/**
	 * Убрать линию из состава коллектора. Внимание! концевые точки линии не обновляются.
	 * @param link линия
	 */
	public void removePhysicalLink(final PhysicalLink link) {
		this.physicalLinks.remove(link);
		super.markAsChanged();
	}

	/**
	 * Добавить линию в состав коллектора. Внимание! концевые точки линии не обновляются.
	 * @param link линия
	 */
	public void addPhysicalLink(final PhysicalLink link) {
		this.physicalLinks.add(link);
		super.markAsChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	public DoublePoint getLocation() {
		int count = 0;
		DoublePoint point = new DoublePoint(0.0, 0.0);

		double x = 0.0;
		double y = 0.0;
		for (Iterator it = getPhysicalLinks().iterator(); it.hasNext();) {
			PhysicalLink mle = (PhysicalLink) it.next();
			DoublePoint an = mle.getLocation();
			x += an.getX();
			y += an.getY();
			count++;
		}
		if (count > 0) {
			x /= count;
			y /= count;
			point.setLocation(x, y);
		}

		return point;
	}

	/**
	 * Возвращает суммарную топологическую длинну всех линий в составе
	 * коллектора в метрах.
	 * @return суммарная длина
	 */
	public double getLengthLt() {
		double length = 0;
		Iterator e = getPhysicalLinks().iterator();
		while (e.hasNext()) {
			PhysicalLink mle = (PhysicalLink) e.next();
			length = length + mle.getLengthLt();
		}
		return length;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isRemoved() {
		return this.removed;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRemoved(final boolean removed) {
		this.removed = removed;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelected() {
		return this.selected;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSelected(final boolean selected) {
		this.selected = selected;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAlarmState(final boolean alarmState) {
		this.alarmState = alarmState;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean getAlarmState() {
		return this.alarmState;
	}

	/**
	 * {@inheritDoc}
	 */
	public MapElementState getState() {
		throw new UnsupportedOperationException("Not implemented");
	}

	/**
	 * {@inheritDoc}
	 */
	public void revert(final MapElementState state) {
		throw new UnsupportedOperationException("Not implemented; MapElementState: " + state);
	}

	/**
	 * {@inheritDoc}
	 */
	public java.util.Map getExportMap() {
		if (exportMap == null)
			exportMap = new HashMap();
		synchronized (exportMap) {
			exportMap.clear();
			exportMap.put(COLUMN_ID, this.id);
			exportMap.put(COLUMN_NAME, this.name);
			exportMap.put(COLUMN_DESCRIPTION, this.description);
			Collection physicalLinkIds = new HashSet(getPhysicalLinks().size());
			for (Iterator it = getPhysicalLinks().iterator(); it.hasNext();) {
				PhysicalLink link = (PhysicalLink) it.next();
				physicalLinkIds.add(link.getId());
			}
			exportMap.put(COLUMN_LINKS, physicalLinkIds);
			return Collections.unmodifiableMap(exportMap);
		}
	}

	public static Collector createInstance(final Identifier creatorId, final java.util.Map exportMap1) throws CreateObjectException {
		Identifier id1 = (Identifier) exportMap1.get(COLUMN_ID);
		String name1 = (String) exportMap1.get(COLUMN_NAME);
		String description1 = (String) exportMap1.get(COLUMN_DESCRIPTION);
		Collection physicalLinkIds1 = (Collection) exportMap1.get(COLUMN_LINKS);

		if (id1 == null || creatorId == null || name1 == null || description1 == null || physicalLinkIds1 == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			Collector collector = new Collector(id1, creatorId, 0L, name1, description1);
			for (Iterator it = physicalLinkIds1.iterator(); it.hasNext();) {
				Identifier physicalLinkId = (Identifier) it.next();
				PhysicalLink physicalLink = (PhysicalLink) StorableObjectPool.getStorableObject(physicalLinkId, false);
				collector.addPhysicalLink(physicalLink);
			}

			assert collector.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			collector.markAsChanged();

			return collector;
		} catch (ApplicationException e) {
			throw new CreateObjectException("Collector.createInstance |  ", e);
		}
	}

	public Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	public void addCharacteristic(final Characteristic characteristic) {
		this.characteristics.add(characteristic);
		super.markAsChanged();
	}

	public void removeCharacteristic(final Characteristic characteristic) {
		this.characteristics.remove(characteristic);
		super.markAsChanged();
	}

	public void setCharacteristics0(final Set characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(java.util.Set)
	 */
	public void setCharacteristics(final Set characteristics) {
		this.setCharacteristics0(characteristics);
		super.markAsChanged();
	}

	public XmlObject getXMLTransferable() {
		com.syrus.amficom.map.xml.Collector xmlCollector = com.syrus.amficom.map.xml.Collector.Factory.newInstance();
		fillXMLTransferable(xmlCollector);
		return xmlCollector;
	}

	public void fillXMLTransferable(final XmlObject xmlObject) {
		com.syrus.amficom.map.xml.Collector xmlCollector = (com.syrus.amficom.map.xml.Collector )xmlObject; 

		com.syrus.amficom.general.xml.UID uid = xmlCollector.addNewUid();
		uid.setStringValue(this.id.toString());
		xmlCollector.setName(this.name);
		xmlCollector.setDescription(this.description);

		com.syrus.amficom.map.xml.PhysicalLinkUIds xmlPhysicalLinkUIds = xmlCollector.addNewPhysicallinkuids();

		for (Iterator it = getPhysicalLinks().iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink) it.next();
			com.syrus.amficom.general.xml.UID xmlPhysicalLinkUId = xmlPhysicalLinkUIds.addNewPhysicallinkuid();
			xmlPhysicalLinkUId.setStringValue(link.getId().toString());
		}
	}

	Collector(
			Identifier creatorId, 
			com.syrus.amficom.map.xml.Collector xmlCollector, 
			ClonedIdsPool clonedIdsPool) 
		throws CreateObjectException, ApplicationException {

		super(
				clonedIdsPool.getClonedId(
						ObjectEntities.COLLECTOR_CODE, 
						xmlCollector.getUid().getStringValue()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				0);

		this.physicalLinks = new HashSet();
		this.characteristics = new HashSet();
		
		this.fromXMLTransferable(xmlCollector, clonedIdsPool);
	}

	public void fromXMLTransferable(final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool) throws ApplicationException {
		com.syrus.amficom.map.xml.Collector xmlCollector = (com.syrus.amficom.map.xml.Collector )xmlObject; 

		this.name = xmlCollector.getName();
		this.description = xmlCollector.getDescription();

		com.syrus.amficom.map.xml.PhysicalLinkUIds[] xmlPhysicalLinkUIdsArray = 
			xmlCollector.getPhysicallinkuidsArray();
		com.syrus.amficom.general.xml.UID[] xmlUIDsArray = 
			xmlPhysicalLinkUIdsArray[0].getPhysicallinkuidArray();
		for(int i = 0; i < xmlUIDsArray.length; i++) {
			Identifier physicalLinkId = clonedIdsPool.getClonedId(
					ObjectEntities.PHYSICALLINK_CODE, 
					xmlUIDsArray[i].getStringValue());
			PhysicalLink physicalLink = (PhysicalLink) StorableObjectPool.getStorableObject(physicalLinkId, false);
			this.addPhysicalLink(physicalLink);
		}
	}

	public static Collector createInstance(final Identifier creatorId, final XmlObject xmlObject, final ClonedIdsPool clonedIdsPool)
			throws CreateObjectException {

		com.syrus.amficom.map.xml.Collector xmlCollector = (com.syrus.amficom.map.xml.Collector )xmlObject;

		try {
			Collector collector = new Collector(creatorId, xmlCollector, clonedIdsPool);
			assert collector.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			collector.markAsChanged();
			return collector;
		} catch (Exception e) {
			throw new CreateObjectException("Collector.createInstance |  ", e);
		}
	}
}
