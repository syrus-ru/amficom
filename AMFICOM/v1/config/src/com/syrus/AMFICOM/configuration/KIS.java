/*
 * $Id: KIS.java,v 1.89 2005/06/22 20:11:26 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.configuration.corba.IdlKIS;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.89 $, $Date: 2005/06/22 20:11:26 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public final class KIS extends DomainMember implements Characterizable {

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

	private Set<Characteristic> characteristics;

	KIS(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new HashSet<Characteristic>();

		final KISDatabase database = (KISDatabase) DatabaseContext.getDatabase(ObjectEntities.KIS_CODE);
		try {
			database.retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public KIS(final IdlKIS kt) throws CreateObjectException {
		try {
			this.fromTransferable(kt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	KIS(final Identifier id,
			final Identifier creatorId,
			final long version,
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

		this.characteristics = new HashSet<Characteristic>();
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
					0L,
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
	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		final IdlKIS kt = (IdlKIS) transferable;
		super.fromTransferable(kt.header, new Identifier(kt.domainId));

		this.equipmentId = new Identifier(kt.equipmentId);
		this.mcmId = new Identifier(kt.mcmId);
		this.name = kt.name;
		this.description = kt.description;
		this.hostname = kt.hostname;
		this.tcpPort = kt.tcpPort;

		final Set characteristicIds = Identifier.fromTransferables(kt.characteristicIds);
		this.characteristics = new HashSet<Characteristic>(kt.characteristicIds.length);
		this.setCharacteristics0(StorableObjectPool.getStorableObjects(characteristicIds, true));

	}

	public IdlKIS getTransferable() {
		IdlIdentifier[] charIds = Identifier.createTransferables(this.characteristics);

		return new IdlKIS(super.getHeaderTransferable(),
				this.getDomainId().getTransferable(),
				this.name,
				this.description,
				this.hostname,
				this.tcpPort,
				this.equipmentId.getTransferable(),
				this.mcmId.getTransferable(),
				charIds);
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
			final long version,
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
		Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.equipmentId);
		dependencies.add(this.mcmId);
		return dependencies;
	}

	public void addCharacteristic(final Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.add(characteristic);
			super.markAsChanged();
		}
	}

	public void removeCharacteristic(final Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.remove(characteristic);
			super.markAsChanged();
		}
	}

	public Set<Characteristic> getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	public void setCharacteristics0(final Set<Characteristic> characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public void setCharacteristics(final Set<Characteristic> characteristics) {
		this.setCharacteristics0(characteristics);
		super.markAsChanged();
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
	public Set getMeasurementPorts(final boolean breakOnLoadError) {
		try {
			return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id,
					ObjectEntities.MEASUREMENTPORT_CODE), true, breakOnLoadError);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}
}
