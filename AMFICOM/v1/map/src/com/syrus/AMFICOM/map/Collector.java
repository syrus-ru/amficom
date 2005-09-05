/*-
 * $Id: Collector.java,v 1.75 2005/09/05 17:43:15 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ObjectEntities.COLLECTOR_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_CODE;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacterizableDelegate;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
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
import com.syrus.util.Log;

/**
 * Коллектор на топологической схеме, который характеризуется набором входящих
 * в него линий. Линии не обязаны быть связными.
 *
 * @author $Author: bass $
 * @version $Revision: 1.75 $, $Date: 2005/09/05 17:43:15 $
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

	private Set<Identifier> physicalLinkIds;

	private transient CharacterizableDelegate characterizableDelegate;

	protected transient boolean selected = false;
	protected transient boolean removed = false;
	protected transient boolean alarmState = false;

	Collector(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
		this.physicalLinkIds = new HashSet<Identifier>();

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

		this.physicalLinkIds = new HashSet<Identifier>();
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
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : OBJECT_BADLY_INITIALIZED;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(this.physicalLinkIds);
		return dependencies;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlCollector getTransferable(final ORB orb) {
		final IdlIdentifier[] idlPhysicalLinkIds = Identifier.createTransferables(this.physicalLinkIds);
		return IdlCollectorHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
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
			Log.errorException(e);
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

	public Set<Characteristic> getCharacteristics(final boolean usePool) throws ApplicationException {
		if (this.characterizableDelegate == null) {
			this.characterizableDelegate = new CharacterizableDelegate(this.id);
		}
		return this.characterizableDelegate.getCharacteristics(usePool);
	}

	public XmlCollector getXmlTransferable(final String importType) {
		final XmlCollector xmlCollector = XmlCollector.Factory.newInstance();
		xmlCollector.setId(this.id.getXmlTransferable(importType));
		xmlCollector.setName(this.name);
		xmlCollector.setDescription(this.description);		
		
		final Set<PhysicalLink> physicalLinks1 = this.getPhysicalLinks();
		final XmlIdentifierSeq xmlPhysicalLinkIdSeq = XmlIdentifierSeq.Factory.newInstance();
		final XmlIdentifier xmlPhysicalLinkIds[] = new XmlIdentifier[physicalLinks1.size()];
		int i = 0;
		for (final PhysicalLink physicalLink : physicalLinks1) {
			xmlPhysicalLinkIds[i++] = physicalLink.getId().getXmlTransferable(importType);
		}
		xmlPhysicalLinkIdSeq.setIdArray(xmlPhysicalLinkIds);
		xmlCollector.setPhysicalLinkIds(xmlPhysicalLinkIdSeq);

		return xmlCollector;
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param created
	 * @param creatorId
	 */
	private Collector(final Identifier id,
			final Date created,
			final Identifier creatorId) {
		super(id,
				created,
				created,
				creatorId,
				creatorId,
				StorableObjectVersion.createInitial());
		this.physicalLinkIds = new HashSet<Identifier>();
	}

	public void fromXmlTransferable(final XmlCollector xmlCollector, final String importType) throws ApplicationException {
		this.name = xmlCollector.getName();
		this.description = xmlCollector.getDescription();

		for (final XmlIdentifier physicalLinkId : xmlCollector.getPhysicalLinkIds().getIdArray()) {
			this.addPhysicalLinkId(Identifier.fromXmlTransferable(physicalLinkId, PHYSICALLINK_CODE, importType));
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
		try {
			final Identifier id = Identifier.fromXmlTransferable(xmlCollector.getId(), COLLECTOR_CODE, importType);
			Collector collector = StorableObjectPool.getStorableObject(id, true);
			if (collector == null) {
				collector = new Collector(id, new Date(), creatorId);
			}
			collector.fromXmlTransferable(xmlCollector, importType);
			assert collector.isValid() : OBJECT_BADLY_INITIALIZED;
			collector.markAsChanged();
			return collector;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			throw new CreateObjectException(ae);
		}
	}
}
