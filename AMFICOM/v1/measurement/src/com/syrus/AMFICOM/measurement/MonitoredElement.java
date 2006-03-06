/*
 * $Id: MonitoredElement.java,v 1.13.2.3 2006/03/06 19:00:09 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlMonitoredElement;
import com.syrus.AMFICOM.measurement.corba.IdlMonitoredElementHelper;
import com.syrus.AMFICOM.measurement.corba.IdlMonitoredElementPackage.IdlMonitoredElementKind;

/**
 * @version $Revision: 1.13.2.3 $, $Date: 2006/03/06 19:00:09 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class MonitoredElement extends DomainMember<MonitoredElement> {
	private static final long serialVersionUID = 5689746173688711494L;

	private Identifier measurementPortId;
	private int kind;
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
			final int kind,
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
		this.kind = kind;
		this.localAddress = localAddress;

		this.monitoredDomainMemberIds = new HashSet<Identifier>();
		this.setMonitoredDomainMemberIds0(monitoredDomainMemberIds);
	}
	
	/**
	 * create new instance for client
	 * @param creatorId
	 * @param domainId
	 * @param measurementPortId
	 * @param kind
	 * @param localAddress
	 * @throws CreateObjectException
	 */
	public static MonitoredElement createInstance(final Identifier creatorId,
			final Identifier domainId,
			final String name,
			final Identifier measurementPortId,
			final IdlMonitoredElementKind kind,
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
			final MonitoredElement monitoredElement = new MonitoredElement(IdentifierPool.getGeneratedIdentifier(MONITOREDELEMENT_CODE),
					creatorId,
					INITIAL_VERSION,
					domainId,
					name,
					measurementPortId,
					kind.value(),
					localAddress,
					monitoredDomainMemberIds);

			assert monitoredElement.isValid() : OBJECT_STATE_ILLEGAL;

			monitoredElement.markAsChanged();

			return monitoredElement;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws CreateObjectException {
		final IdlMonitoredElement met = (IdlMonitoredElement) transferable;
		super.fromTransferable(met, Identifier.valueOf(met.domainId));
		this.measurementPortId = Identifier.valueOf(met.measurementPortId);
		this.kind = met.kind.value();
		this.localAddress = met.localAddress;

		this.name = met.name;

		this.monitoredDomainMemberIds = Identifier.fromTransferables(met.monitoredDomainMemberIds);

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlMonitoredElement getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

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
				IdlMonitoredElementKind.from_int(this.kind),
				this.localAddress,
				Identifier.createTransferables(this.monitoredDomainMemberIds));
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

	public IdlMonitoredElementKind getKind() {
		return IdlMonitoredElementKind.from_int(this.kind);
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
			final int kind,
			final String localAddress) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version,
				domainId);
		this.name = name;
		this.measurementPortId = measurementPortId;
		this.kind = kind;
		this.localAddress = localAddress;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
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
	 * @param kind The kind to set.
	 */
	public void setKind(final IdlMonitoredElementKind kind) {
		this.kind = kind.value();
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
