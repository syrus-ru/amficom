/*
 * $Id: KIS.java,v 1.13 2005/12/07 17:17:16 bass Exp $
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
import java.util.logging.Level;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.IdlKIS;
import com.syrus.AMFICOM.measurement.corba.IdlKISHelper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.13 $, $Date: 2005/12/07 17:17:16 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class KIS extends DomainMember<KIS> implements Namable {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3257281422661466166L;

	private Identifier equipmentId;
	private Identifier mcmId;
	private String name;
	private String description;
	private String hostname;
	private short tcpPort;

	public KIS(final IdlKIS kt) throws CreateObjectException {
		try {
			this.fromTransferable(kt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	KIS(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final String name,
			final String description,
			final String hostname,
			final short tcpPort,
			final Identifier equipmentId,
			final Identifier mcmId) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				domainId);
		this.name = name;
		this.description = description;
		this.hostname = hostname;
		this.tcpPort = tcpPort;
		this.equipmentId = equipmentId;
		this.mcmId = mcmId;
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param domainId
	 * @param name
	 * @param description
	 * @param mcmId
	 * @throws CreateObjectException
	 */
	public static KIS createInstance(final Identifier creatorId,
			final Identifier domainId,
			final String name,
			final String description,
			final String hostname,
			final short tcpPort,
			final Identifier equipmentId,
			final Identifier mcmId) throws CreateObjectException {
		if (creatorId == null || domainId == null || name == null ||
				description == null || hostname == null || equipmentId == null || mcmId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final KIS kis = new KIS(IdentifierPool.getGeneratedIdentifier(ObjectEntities.KIS_CODE),
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					domainId,
					name,
					description,
					hostname,
					tcpPort,
					equipmentId,
					mcmId);

			assert kis.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			kis.markAsChanged();

			return kis;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlKIS kt = (IdlKIS) transferable;
		super.fromTransferable(kt, new Identifier(kt.domainId));

		this.equipmentId = new Identifier(kt.equipmentId);
		this.mcmId = new Identifier(kt.mcmId);
		this.name = kt.name;
		this.description = kt.description;
		this.hostname = kt.hostname;
		this.tcpPort = kt.tcpPort;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlKIS getIdlTransferable(final ORB orb) {

		return IdlKISHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				this.getDomainId().getIdlTransferable(),
				this.name,
				this.description,
				this.hostname,
				this.tcpPort,
				this.equipmentId.getIdlTransferable(),
				this.mcmId.getIdlTransferable());
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	public String getHostName() {
		return this.hostname;
	}

	public short getTCPPort() {
		return this.tcpPort;
	}

	public Identifier getEquipmentId() {
		return this.equipmentId;
	}

	public Identifier getMCMId() {
		return this.mcmId;
	}

	public void setMCMId(final Identifier mcmId) {
		this.mcmId = mcmId;
		super.markAsChanged();
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final String name,
			final String description,
			final String hostname,
			final short tcpPort,
			final Identifier equipmentId,
			final Identifier mcmId) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version,
				domainId);
		this.name = name;
		this.description = description;
		this.hostname = hostname;
		this.tcpPort = tcpPort;
		this.equipmentId = equipmentId;
		this.mcmId = mcmId;
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.equipmentId);
		dependencies.add(this.mcmId);
		return dependencies;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}
	/**
	 * @param equipmentId The equipmentId to set.
	 */
	public void setEquipmentId(final Identifier equipmentId) {
		this.equipmentId = equipmentId;
		super.markAsChanged();
	}
	/**
	 * @param hostname The hostname to set.
	 */
	public void setHostName(final String hostname) {
		this.hostname = hostname;
		super.markAsChanged();
	}
	/**
	 * @param tcpPort The tcpPort to set.
	 */
	public void setTCPPort(final short tcpPort) {
		this.tcpPort = tcpPort;
		super.markAsChanged();
	}

	/**
	 * @return <code>Set&lt;MeasurementPort&gt;</code>
	 */
	public Set<MeasurementPort> getMeasurementPorts(final boolean breakOnLoadError) {
		try {
			return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id,
					ObjectEntities.MEASUREMENTPORT_CODE), true, breakOnLoadError);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, Level.SEVERE);
			return Collections.emptySet();
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected KISWrapper getWrapper() {
		return KISWrapper.getInstance();
	}
}
