/**
 * $Id: Collector.java,v 1.30 2005/04/05 12:02:16 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.Collector_Transferable;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

/**
 * Коллектор на топологической схеме, который характеризуется набором входящих
 * в него линий. Линии не обязаны быть связными.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.30 $, $Date: 2005/04/05 12:02:16 $
 * @module map_v1
 */
public class Collector extends StorableObject implements MapElement {

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

	private Set physicalLinks;
	private Set characteristics;

	private StorableObjectDatabase collectorDatabase;

	protected transient Map map;
	protected transient boolean selected = false;
	protected transient boolean removed = false;
	protected transient boolean alarmState = false;

	public Collector(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		this.physicalLinks = new HashSet();
		this.characteristics = new HashSet();

		this.collectorDatabase = MapDatabaseContext.getCollectorDatabase();
		try {
			this.collectorDatabase.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	public Collector(Collector_Transferable ct) throws CreateObjectException {
		this.collectorDatabase = MapDatabaseContext.getCollectorDatabase();
		this.fromTransferable(ct);
	}

	protected Collector(final Identifier id,
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

		this.collectorDatabase = MapDatabaseContext.getCollectorDatabase();
	}

	public static Collector createInstance(Identifier creatorId, Map map, String name, String description)
			throws CreateObjectException {

		if (creatorId == null || map == null || name == null || description == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			Collector collector = new Collector(IdentifierPool.getGeneratedIdentifier(ObjectEntities.COLLECTOR_ENTITY_CODE),
					creatorId,
					0L,
					name,
					description);
			collector.changed = true;
			return collector;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Collector.createInstance | cannot generate identifier ", e);
		}
	}

	protected void fromTransferable(IDLEntity transferable) throws CreateObjectException {
		Collector_Transferable ct = (Collector_Transferable) transferable;
		super.fromTransferable(ct.header);

		this.name = ct.name;
		this.description = ct.description;

		try {
			this.physicalLinks = new HashSet(ct.physicalLinkIds.length);
			HashSet physicalLinkIds = new HashSet(ct.physicalLinkIds.length);
			for (int i = 0; i < ct.characteristicIds.length; i++)
				physicalLinkIds.add(new Identifier(ct.physicalLinkIds[i]));
			this.physicalLinks.addAll(MapStorableObjectPool.getStorableObjects(physicalLinkIds, true));

			this.characteristics = new HashSet(ct.characteristicIds.length);
			HashSet characteristicIds = new HashSet(ct.characteristicIds.length);
			for (int i = 0; i < ct.characteristicIds.length; i++)
				characteristicIds.add(new Identifier(ct.characteristicIds[i]));
			this.characteristics.addAll(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	public Set getDependencies() {
		Set dependencies = new HashSet();
		dependencies.addAll(this.physicalLinks);
		return dependencies;
	}

	public IDLEntity getTransferable() {
		int i = 0;
		Identifier_Transferable[] physicalLinkIds = new Identifier_Transferable[this.physicalLinks.size()];
		for (Iterator iterator = this.physicalLinks.iterator(); iterator.hasNext();)
			physicalLinkIds[i++] = (Identifier_Transferable) ((PhysicalLink) iterator.next()).getId().getTransferable();

		i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();

		return new Collector_Transferable(super.getHeaderTransferable(), this.name, this.description, physicalLinkIds, charIds);
	}

	public String getDescription() {
		return this.description;
	}

	protected void setDescription0(String description) {
		this.description = description;
	}

	public void setDescription(String description) {
		this.setDescription0(description);
		this.changed = true;
	}

	public String getName() {
		return this.name;
	}

	protected void setName0(String name) {
		this.name = name;
	}

	public void setName(String name) {
		this.setName0(name);
		this.changed = true;
	}

	public Set getPhysicalLinks() {
		return Collections.unmodifiableSet(this.physicalLinks);
	}

	protected void setPhysicalLinks0(Set physicalLinks) {
		this.physicalLinks.clear();
		if (physicalLinks != null)
			this.physicalLinks.addAll(physicalLinks);
	}

	public void setPhysicalLinks(Set physicalLinks) {
		this.setPhysicalLinks0(physicalLinks);
		this.changed = true;
	}

	protected synchronized void setAttributes(Date created,
			Date modified,
			Identifier creatorId,
			Identifier modifierId,
			long version,
			String name,
			String description) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
	}

	/**
	 * Убрать линию из состава коллектора. Внимание! концевые точки линии не обновляются.
	 * @param link линия
	 */
	public void removePhysicalLink(PhysicalLink link) {
		this.physicalLinks.remove(link);
		this.changed = true;
	}

	/**
	 * Добавить линию в состав коллектора. Внимание! концевые точки линии не обновляются.
	 * @param link линия
	 */
	public void addPhysicalLink(PhysicalLink link) {
		this.physicalLinks.add(link);
		this.changed = true;
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
	public void setRemoved(boolean removed) {
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
	public void setSelected(boolean selected) {
		this.selected = selected;
		getMap().setSelected(this, selected);
	}

	/**
	 * {@inheritDoc}
	 */
	public Map getMap() {
		return this.map;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMap(Map map) {
		this.map = map;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAlarmState(boolean alarmState) {
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
	public void revert(MapElementState state) {
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
			Set physicalLinkIds = new HashSet(getPhysicalLinks().size());
			for (Iterator it = getPhysicalLinks().iterator(); it.hasNext();) {
				PhysicalLink link = (PhysicalLink) it.next();
				physicalLinkIds.add(link.getId());
			}
			exportMap.put(COLUMN_LINKS, physicalLinkIds);
			return Collections.unmodifiableMap(exportMap);
		}
	}

	public static Collector createInstance(Identifier creatorId, java.util.Map exportMap1) throws CreateObjectException {
		Identifier id1 = (Identifier) exportMap1.get(COLUMN_ID);
		String name1 = (String) exportMap1.get(COLUMN_NAME);
		String description1 = (String) exportMap1.get(COLUMN_DESCRIPTION);
		Set physicalLinkIds1 = (Set) exportMap1.get(COLUMN_LINKS);

		if (id1 == null || creatorId == null || name1 == null || description1 == null || physicalLinkIds1 == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			Collector collector = new Collector(id1, creatorId, 0L, name1, description1);
			collector.changed = true;
			for (Iterator it = physicalLinkIds1.iterator(); it.hasNext();) {
				Identifier physicalLinkId = (Identifier) it.next();
				PhysicalLink physicalLink = (PhysicalLink) MapStorableObjectPool.getStorableObject(physicalLinkId, false);
				collector.addPhysicalLink(physicalLink);
			}
			return collector;
		}
		catch (ApplicationException e) {
			throw new CreateObjectException("Collector.createInstance |  ", e);
		}
	}

	public Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	public void addCharacteristic(Characteristic characteristic) {
		this.characteristics.add(characteristic);
		this.changed = true;
	}

	public void removeCharacteristic(Characteristic characteristic) {
		this.characteristics.remove(characteristic);
		this.changed = true;
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
		this.changed = true;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_COLLECTOR;
	}
}
