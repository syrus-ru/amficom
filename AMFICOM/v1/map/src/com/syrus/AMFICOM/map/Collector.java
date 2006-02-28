/*-
 * $Id: Collector.java,v 1.107 2006/02/28 15:20:01 arseniy Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_THROW_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.COLLECTOR_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.general.xml.XmlIdentifierSeq;
import com.syrus.AMFICOM.map.corba.IdlCollector;
import com.syrus.AMFICOM.map.corba.IdlCollectorHelper;
import com.syrus.AMFICOM.map.xml.XmlCollector;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * Коллектор на топологической схеме, который характеризуется набором входящих
 * в него линий. Линии не обязаны быть связными.
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.107 $, $Date: 2006/02/28 15:20:01 $
 * @module map
 */
public final class Collector extends StorableObject<Collector>
		implements Describable, Characterizable,
		MapElement, XmlTransferableObject<XmlCollector> {
	private static final long serialVersionUID = 4049922679379212598L;

	private String name;
	private String description;

	private Set<Identifier> physicalLinkIds;

	private transient boolean selected = false;
	private transient boolean removed = false;
	private transient boolean alarmState = false;

	public Collector(final IdlCollector ct) throws CreateObjectException {
		try {
			this.fromTransferable(ct);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	Collector(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
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

		this.physicalLinkIds = new HashSet<Identifier>();
	}

	public static Collector createInstance(final Identifier creatorId, final Map map, final String name, final String description)
			throws CreateObjectException {

		if (creatorId == null || map == null || name == null || description == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final Collector collector = new Collector(IdentifierPool.getGeneratedIdentifier(COLLECTOR_CODE),
					creatorId,
					INITIAL_VERSION,
					name,
					description);

			assert collector.isValid() : OBJECT_BADLY_INITIALIZED;

			collector.markAsChanged();

			return collector;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlCollector ct = (IdlCollector) transferable;
		super.fromTransferable(ct);

		this.name = ct.name;
		this.description = ct.description;

		final Set<Identifier> ids = Identifier.fromTransferables(ct.physicalLinkIds);
		this.physicalLinkIds = ids;
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.isValid() : OBJECT_BADLY_INITIALIZED;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(this.physicalLinkIds);
		return dependencies;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlCollector getIdlTransferable(final ORB orb) {
		final IdlIdentifier[] idlPhysicalLinkIds = Identifier.createTransferables(this.physicalLinkIds);
		return IdlCollectorHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				this.name,
				this.description,
				idlPhysicalLinkIds);
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
	
	protected Set<Identifier> getPhysicalLinkIds() {
		return Collections.unmodifiableSet(this.physicalLinkIds);
	}

	public Set<PhysicalLink> getPhysicalLinks() {
		try {
			return Collections.unmodifiableSet(StorableObjectPool.<PhysicalLink>getStorableObjects(this.physicalLinkIds, true));
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			return Collections.emptySet();
		}
	}

	protected void setPhysicalLinks0(final Set<PhysicalLink> physicalLinks) {
		this.physicalLinkIds.clear();
		if (physicalLinks != null) {
			for (PhysicalLink link : physicalLinks) {
				this.physicalLinkIds.add(link.getId());
			}
		}
	}
	
	protected void setPhysicalLinkIds(final Set<Identifier> physicalLinkIds) {
		setPhysicalLinkIds0(physicalLinkIds);
		super.markAsChanged();
	}
	
	protected void setPhysicalLinkIds0(final Set<Identifier> physicalLinkIds) {
		this.physicalLinkIds.clear();
		this.physicalLinkIds.addAll(physicalLinkIds);
	}

	public void setPhysicalLinks(final Set<PhysicalLink> physicalLinks) {
		this.setPhysicalLinks0(physicalLinks);
		super.markAsChanged();
	}

	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
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
		this.physicalLinkIds.remove(link.getId());
		super.markAsChanged();
	}

	private void addPhysicalLinkId(final Identifier linkId) {
		this.physicalLinkIds.add(linkId);
		super.markAsChanged();
	}
	
	/**
	 * Добавить линию в состав коллектора. Внимание! концевые точки линии не обновляются.
	 * @param link линия
	 */
	public void addPhysicalLink(final PhysicalLink link) {
		this.physicalLinkIds.add(link.getId());
		super.markAsChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	public DoublePoint getLocation() {
		int count = 0;
		final DoublePoint point = new DoublePoint(0.0, 0.0);

		double x = 0.0;
		double y = 0.0;
		for (final PhysicalLink mle : this.getPhysicalLinks()) {
			final DoublePoint an = mle.getLocation();
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
		for (final PhysicalLink mle : this.getPhysicalLinks()) {
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
	 * @param collector
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	public void getXmlTransferable(final XmlCollector collector,
			final String importType,
			final boolean usePool)
	throws XmlConversionException {
		this.id.getXmlTransferable(collector.addNewId(), importType);
		collector.setName(this.name);
		if(this.description != null && this.description.length() != 0) {
			collector.setDescription(this.description);		
		}
		
		if (collector.isSetPhysicalLinkIds()) {
			collector.unsetPhysicalLinkIds();
		}
		final Set<PhysicalLink> physicalLinks1 = this.getPhysicalLinks();
		if (!physicalLinks1.isEmpty()) {
			final XmlIdentifierSeq physicalLinkIdSeq = collector.addNewPhysicalLinkIds();
			for (final PhysicalLink physicalLink : physicalLinks1) {
				 physicalLink.getId().getXmlTransferable(physicalLinkIdSeq.addNewId(), importType);
			}
		}
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param importType
	 * @param created
	 * @param creatorId
	 * @throws IdentifierGenerationException
	 */
	private Collector(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, COLLECTOR_CODE, created, creatorId);
		this.physicalLinkIds = new HashSet<Identifier>();
	}

	public void fromXmlTransferable(final XmlCollector xmlCollector, final String importType)
	throws XmlConversionException {
		try {
			this.name = xmlCollector.getName();
			if (xmlCollector.isSetDescription()) {
				this.description = xmlCollector.getDescription();
			} else {
				this.description = "";
			}
	
			if (xmlCollector.isSetPhysicalLinkIds()) {
				for (final XmlIdentifier physicalLinkId : xmlCollector.getPhysicalLinkIds().getIdArray()) {
					this.addPhysicalLinkId(Identifier.fromXmlTransferable(physicalLinkId, importType, MODE_THROW_IF_ABSENT));
				}
			}
		} catch (final ObjectNotFoundException onfe) {
			throw new XmlConversionException(onfe);
		}
	}

	/**
	 * @param creatorId
	 * @param importType
	 * @param xmlCollector
	 * @throws CreateObjectException
	 */
	public static Collector createInstance(
			final Identifier creatorId, 
			final String importType,
			final XmlCollector xmlCollector)
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final XmlIdentifier xmlId = xmlCollector.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			Collector collector;
			if (id.isVoid()) {
				collector = new Collector(xmlId,
						importType,
						created,
						creatorId);
			} else {
				collector = StorableObjectPool.getStorableObject(id, true);
				if (collector == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					collector = new Collector(xmlId,
							importType,
							created,
							creatorId);
				}
			}
			collector.fromXmlTransferable(xmlCollector, importType);
			assert collector.isValid() : OBJECT_BADLY_INITIALIZED;
			collector.markAsChanged();
			return collector;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		} catch (final XmlConversionException xce) {
			throw new CreateObjectException(xce);
		}
	}

	public Characterizable getCharacterizable() {
		return this;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected CollectorWrapper getWrapper() {
		return CollectorWrapper.getInstance();
	}

	/*-********************************************************************
	 * Children manipulation: characteristics                             *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<Characteristic> characteristicContainerWrappee;

	/**
	 * @see Characterizable#getCharacteristicContainerWrappee()
	 */
	public final StorableObjectContainerWrappee<Characteristic> getCharacteristicContainerWrappee() {
		return (this.characteristicContainerWrappee == null)
				? this.characteristicContainerWrappee = new StorableObjectContainerWrappee<Characteristic>(this, CHARACTERISTIC_CODE)
				: this.characteristicContainerWrappee;
	}

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(com.syrus.AMFICOM.general.Characteristic, boolean)
	 */
	public void addCharacteristic(final Characteristic characteristic,
			final boolean usePool)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(com.syrus.AMFICOM.general.Characteristic, boolean)
	 */
	public void removeCharacteristic(
			final Characteristic characteristic,
			final boolean usePool)
	throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		assert characteristic.getParentCharacterizableId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics(boolean)
	 */
	public Set<Characteristic> getCharacteristics(boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getCharacteristics0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<Characteristic> getCharacteristics0(final boolean usePool)
	throws ApplicationException {
		return this.getCharacteristicContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param characteristics
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set, boolean)
	 */
	public void setCharacteristics(final Set<Characteristic> characteristics,
			final boolean usePool)
	throws ApplicationException {
		assert characteristics != null : NON_NULL_EXPECTED;

		final Set<Characteristic> oldCharacteristics = this.getCharacteristics0(usePool);

		final Set<Characteristic> toRemove = new HashSet<Characteristic>(oldCharacteristics);
		toRemove.removeAll(characteristics);
		for (final Characteristic characteristic : toRemove) {
			this.removeCharacteristic(characteristic, usePool);
		}

		final Set<Characteristic> toAdd = new HashSet<Characteristic>(characteristics);
		toAdd.removeAll(oldCharacteristics);
		for (final Characteristic characteristic : toAdd) {
			this.addCharacteristic(characteristic, usePool);
		}
	}
}
