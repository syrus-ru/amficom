/*
 * $Id: MonitoredElement.java,v 1.15 2006/02/16 13:02:18 saa Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlMonitoredElement;
import com.syrus.AMFICOM.measurement.corba.IdlMonitoredElementHelper;
import com.syrus.AMFICOM.measurement.corba.IdlMonitoredElementPackage.MonitoredElementSort;

/**
 * @version $Revision: 1.15 $, $Date: 2006/02/16 13:02:18 $
 * @author $Author: saa $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class MonitoredElement extends DomainMember<MonitoredElement> {
	private static final long serialVersionUID = 5689746173688711494L;

	private Identifier measurementPortId;
	private int sort;
	private String name;
	private String localAddress;

	private Set<Identifier> monitoredDomainMemberIds;


	public MonitoredElement(final IdlMonitoredElement met) throws CreateObjectException {
		this.fromTransferable(met);
	}
	
	MonitoredElement(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final String name,
			final Identifier measurementPortId,
			final int sort,
			final String localAddress,
			final Set<Identifier> monitoredDomainMemberIds) {
		super(id,
			new Date(System.currentTimeMillis()),
			new Date(System.currentTimeMillis()),
			creatorId,
			creatorId,
			version,
			domainId);
		this.name = name;
		this.measurementPortId = measurementPortId;
		this.sort = sort;
		this.localAddress = localAddress;

		this.monitoredDomainMemberIds = new HashSet<Identifier>();
		this.setMonitoredDomainMemberIds0(monitoredDomainMemberIds);
	}
	
	/**
	 * create new instance for client
	 * @param creatorId
	 * @param domainId
	 * @param measurementPortId
	 * @param sort
	 * @param localAddress
	 * @throws CreateObjectException
	 */
	public static MonitoredElement createInstance(final Identifier creatorId,
			final Identifier domainId,
			final String name,
			final Identifier measurementPortId,
			final MonitoredElementSort sort,
			final String localAddress,
			final Set<Identifier> monitoredDomainMemberIds) throws CreateObjectException {
		if (creatorId == null
				|| domainId == null
				|| name == null
				|| measurementPortId == null
				|| localAddress == null
				|| monitoredDomainMemberIds == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			final MonitoredElement monitoredElement = new MonitoredElement(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MONITOREDELEMENT_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					domainId,
					name,
					measurementPortId,
					sort.value(),
					localAddress,
					monitoredDomainMemberIds);

			assert monitoredElement.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			monitoredElement.markAsChanged();

			return monitoredElement;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws CreateObjectException {
		IdlMonitoredElement met = (IdlMonitoredElement) transferable;
		super.fromTransferable(met, new Identifier(met.domainId));
		this.measurementPortId = new Identifier(met.measurementPortId);
		this.sort = met.sort.value();
		this.localAddress = met.localAddress;

		this.name = met.name;

		this.monitoredDomainMemberIds = Identifier.fromTransferables(met.monitoredDomainMemberIds);
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlMonitoredElement getIdlTransferable(final ORB orb) {
		final IdlIdentifier[] mdmIds = Identifier.createTransferables(this.monitoredDomainMemberIds);

		return IdlMonitoredElementHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				this.getDomainId().getIdlTransferable(),
				this.name,
				this.measurementPortId.getIdlTransferable(),
				MonitoredElementSort.from_int(this.sort),
				this.localAddress,
				mdmIds);
	}

	public Identifier getMeasurementPortId() {
		return this.measurementPortId;
	}

	/**
	 * @throws ApplicationException
	 */
	public MeasurementPort getMeasurementPort() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.getMeasurementPortId(), true);
	}

	public MonitoredElementSort getSort() {
		return MonitoredElementSort.from_int(this.sort);
	}

	public String getLocalAddress() {
		return this.localAddress;
	}
	
	public void setLocalAddress(final String localAddress) {
		this.localAddress = localAddress;
		super.markAsChanged();
	}

	public Set<Identifier> getMonitoredDomainMemberIds() {
		return Collections.unmodifiableSet(this.monitoredDomainMemberIds);
	}

	protected synchronized void setMonitoredDomainMemberIds0(final Set<Identifier> monitoredDomainMemberIds) {
		this.monitoredDomainMemberIds.clear();
		if (monitoredDomainMemberIds != null) {
			this.monitoredDomainMemberIds.addAll(monitoredDomainMemberIds);
		}
	}

	protected synchronized void setMonitoredDomainMemberIds(final Set<Identifier> monitoredDomainMemberIds) {
		this.setMonitoredDomainMemberIds0(monitoredDomainMemberIds);
		super.markAsChanged();
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final String name,
			final Identifier measurementPortId,
			final int sort,
			final String localAddress) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version,
				domainId);
		this.name = name;
		this.measurementPortId = measurementPortId;
		this.sort = sort;
		this.localAddress = localAddress;
	}

	/**
	 * Возвращает свое базовое название 
	 * @return свое базовое название
	 */
	public String getName() {
		return this.name;
	}

	public static String getNamePrefix() {
		// @todo: move the construction code to module util
		// the same may be done for "RR"
		// XXX: cache?
		return LangModelMeasurement.getString("ME");
	}

	/**
	 * Возвращает название, отображаемое пользователю
	 * @return название, отображаемое пользователю
	 */
	public String getDisplayedName() {
		// XXX: not cached yet; creates new string every invocation
		return getNamePrefix() + " " + this.getName();
	}

	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(this.monitoredDomainMemberIds);
		dependencies.add(this.measurementPortId);
		return dependencies;
	}
	/**
	 * @param measurementPortId The measurementPortId to set.
	 */
	public void setMeasurementPortId(final Identifier measurementPortId) {
		this.measurementPortId = measurementPortId;
		super.markAsChanged();
	}
	/**
	 * @param sort The sort to set.
	 */
	public void setSort(final MonitoredElementSort sort) {
		this.sort = sort.value();
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected MonitoredElementWrapper getWrapper() {
		return MonitoredElementWrapper.getInstance();
	}
}
