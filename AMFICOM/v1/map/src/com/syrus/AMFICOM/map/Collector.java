/*-
 * $Id: Collector.java,v 1.34 2005/04/08 14:51:00 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
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
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.map.corba.Collector_Transferable;
import com.syrus.util.Log;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

/**
 * ��������� �� �������������� �����, ������� ��������������� ������� ��������
 * � ���� �����. ����� �� ������� ���� ��������.
 * 
 * @author $Author: arseniy $
 * @version $Revision: 1.34 $, $Date: 2005/04/08 14:51:00 $
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
	 * ����� ���������� ��� ��������. ���������������� ������ � ������
	 * ������������� ��������
	 */
	private static java.util.Map exportMap = null;

	private String name;
	private String description;

	private Set physicalLinks;
	private Set characteristics;

	protected transient Map map;
	protected transient boolean selected = false;
	protected transient boolean removed = false;
	protected transient boolean alarmState = false;

	Collector(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		this.physicalLinks = new HashSet();
		this.characteristics = new HashSet();

		CollectorDatabase database = MapDatabaseContext.getCollectorDatabase();
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	Collector(Collector_Transferable ct) throws CreateObjectException {
		try {
			this.fromTransferable(ct);
		}
		catch (ApplicationException ae) {
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

	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		Collector_Transferable ct = (Collector_Transferable) transferable;
		super.fromTransferable(ct.header);

		this.name = ct.name;
		this.description = ct.description;

		Set ids;

		ids = Identifier.fromTransferables(ct.physicalLinkIds);
		this.physicalLinks = MapStorableObjectPool.getStorableObjects(ids, true);

		ids = Identifier.fromTransferables(ct.characteristicIds);
		this.characteristics = GeneralStorableObjectPool.getStorableObjects(ids, true);
	}

	public Set getDependencies() {
		Set dependencies = new HashSet();
		dependencies.addAll(this.physicalLinks);
		return dependencies;
	}

	public IDLEntity getTransferable() {
		Identifier_Transferable[] physicalLinkIds = null;
		Identifier_Transferable[] charIds = null;
		try {
			physicalLinkIds = Identifier.createTransferables(this.physicalLinks);
			charIds = Identifier.createTransferables(this.characteristics);
		}
		catch (IllegalDataException ide) {
			// Never
			Log.errorException(ide);
		}

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
	 * ������ ����� �� ������� ����������. ��������! �������� ����� ����� �� �����������.
	 * @param link �����
	 */
	public void removePhysicalLink(PhysicalLink link) {
		this.physicalLinks.remove(link);
		this.changed = true;
	}

	/**
	 * �������� ����� � ������ ����������. ��������! �������� ����� ����� �� �����������.
	 * @param link �����
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
	 * ���������� ��������� �������������� ������ ���� ����� � ������� 
	 * ���������� � ������.
	 * @return ��������� �����
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
			Collection physicalLinkIds = new HashSet(getPhysicalLinks().size());
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
		Collection physicalLinkIds1 = (Collection) exportMap1.get(COLUMN_LINKS);

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
