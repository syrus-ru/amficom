/*-
 * $Id: Collector.java,v 1.72 2005/08/31 13:08:04 krupenn Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.COLLECTOR_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_CODE;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacterizableDelegate;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ImportUidMapDatabase;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.general.xml.XmlIdentifierSeq;
import com.syrus.AMFICOM.map.corba.IdlCollector;
import com.syrus.AMFICOM.map.corba.IdlCollectorHelper;
import com.syrus.AMFICOM.map.xml.XmlCollector;
import com.syrus.AMFICOM.resource.DoublePoint;

/**
 * Коллектор на топологической схеме, который характеризуется набором входящих
 * в него линий. Линии не обязаны быть связными.
 *
 * @author $Author: krupenn $
 * @version $Revision: 1.72 $, $Date: 2005/08/31 13:08:04 $
 * @module map
 */
public final class Collector extends StorableObject implements MapElement, XmlBeansTransferable<XmlCollector> {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 4049922679379212598L;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_LINKS = "links";

	private String name;
	private String description;

	private Set<PhysicalLink> physicalLinks;

	private transient CharacterizableDelegate characterizableDelegate;

	protected transient boolean selected = false;
	protected transient boolean removed = false;
	protected transient boolean alarmState = false;

	Collector(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		this.physicalLinks = new HashSet<PhysicalLink>();

		try {
			DatabaseContext.getDatabase(COLLECTOR_CODE).retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

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

		this.physicalLinks = new HashSet<PhysicalLink>();
	}

	public static Collector createInstance(final Identifier creatorId, final Map map, final String name, final String description)
			throws CreateObjectException {

		if (creatorId == null || map == null || name == null || description == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final Collector collector = new Collector(IdentifierPool.getGeneratedIdentifier(COLLECTOR_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					name,
					description);

			assert collector.isValid() : OBJECT_STATE_ILLEGAL;

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
		this.physicalLinks = StorableObjectPool.getStorableObjects(ids, true);
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(this.physicalLinks);
		return dependencies;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlCollector getTransferable(final ORB orb) {
		final IdlIdentifier[] physicalLinkIds = Identifier.createTransferables(this.physicalLinks);
		return IdlCollectorHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.name,
				this.description,
				physicalLinkIds);
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

	public Set<PhysicalLink> getPhysicalLinks() {
		return Collections.unmodifiableSet(this.physicalLinks);
	}

	protected void setPhysicalLinks0(final Set<PhysicalLink> physicalLinks) {
		this.physicalLinks.clear();
		if (physicalLinks != null)
			this.physicalLinks.addAll(physicalLinks);
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

	public Set<Characteristic> getCharacteristics(final boolean usePool) throws ApplicationException {
		if (this.characterizableDelegate == null) {
			this.characterizableDelegate = new CharacterizableDelegate(this.id);
		}
		return this.characterizableDelegate.getCharacteristics(usePool);
	}

	public XmlCollector getXmlTransferable() {
		final XmlCollector xmlCollector = XmlCollector.Factory.newInstance();
		final XmlIdentifier uid = xmlCollector.addNewId();
		uid.setStringValue(this.id.getIdentifierString());
		xmlCollector.setName(this.name);
		xmlCollector.setDescription(this.description);
		
		final XmlIdentifierSeq xmlPhysicalLinkUIds = xmlCollector.addNewPhysicalLinkIds();
		
		for (final PhysicalLink link : this.getPhysicalLinks()) {
			final XmlIdentifier xmlPhysicalLinkUId = xmlPhysicalLinkUIds.addNewId();
			xmlPhysicalLinkUId.setStringValue(link.getId().getIdentifierString());
		}
		return xmlCollector;
	}

	Collector(final Identifier creatorId,
			final StorableObjectVersion version,
			final XmlCollector xmlCollector,
			final ClonedIdsPool clonedIdsPool,
			final String importType) throws CreateObjectException, ApplicationException {

		super(clonedIdsPool.getClonedId(COLLECTOR_CODE, xmlCollector.getId().getStringValue()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);

		this.physicalLinks = new HashSet<PhysicalLink>();

		this.fromXmlTransferable(xmlCollector, clonedIdsPool, importType);
	}

	public void fromXmlTransferable(final XmlCollector xmlCollector, final ClonedIdsPool clonedIdsPool, final String importType) throws ApplicationException {
		this.name = xmlCollector.getName();
		this.description = xmlCollector.getDescription();

		for (final XmlIdentifier xmlId : xmlCollector.getPhysicalLinkIds().getIdArray()) {
			final Identifier physicalLinkId = clonedIdsPool.getClonedId(PHYSICALLINK_CODE,
					xmlId.getStringValue());
			final PhysicalLink physicalLink = StorableObjectPool.getStorableObject(physicalLinkId, false);
			this.addPhysicalLink(physicalLink);
		}
	}

	public static Collector createInstance(
			final Identifier creatorId, 
			final String importType,
			final XmlCollector xmlCollector, 
			final ClonedIdsPool clonedIdsPool) throws CreateObjectException {
		try {
			String uid = xmlCollector.getId().getStringValue();
			Identifier existingIdentifier = ImportUidMapDatabase.retrieve(importType, uid);
			Collector collector = null;
			if(existingIdentifier != null) {
				collector = StorableObjectPool.getStorableObject(existingIdentifier, true);
				if(collector != null) {
					clonedIdsPool.setExistingId(uid, existingIdentifier);
					collector.fromXmlTransferable(xmlCollector, clonedIdsPool, importType);
				}
				else{
					ImportUidMapDatabase.delete(importType, uid);
				}
			}
			if(collector == null) {
				collector = new Collector(creatorId, StorableObjectVersion.createInitial(), xmlCollector, clonedIdsPool, importType);
				ImportUidMapDatabase.insert(importType, uid, collector.id);
			}
			assert collector.isValid() : OBJECT_STATE_ILLEGAL;
			collector.markAsChanged();
			return collector;
		} catch (Exception e) {
			throw new CreateObjectException("Collector.createInstance |  ", e);
		}
	}
}
