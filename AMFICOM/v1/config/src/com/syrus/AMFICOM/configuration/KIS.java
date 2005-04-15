/*
 * $Id: KIS.java,v 1.74 2005/04/15 19:22:12 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.configuration.corba.KIS_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.74 $, $Date: 2005/04/15 19:22:12 $
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

	private Set characteristics;

	public KIS(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new HashSet();

		KISDatabase database = ConfigurationDatabaseContext.getKISDatabase();
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public KIS(KIS_Transferable kt) throws CreateObjectException {
		try {
			this.fromTransferable(kt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected KIS(Identifier id,
					Identifier creatorId,
					long version,
					Identifier domainId,
					String name,
					String description,
					String hostname,
					short tcpPort,
					Identifier equipmentId,
					Identifier mcmId) {
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

		this.characteristics = new HashSet();
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
	public static KIS createInstance(Identifier creatorId,
											 Identifier domainId,
											 String name,
											 String description,
											 String hostname,
											 short tcpPort,
											 Identifier equipmentId,
											 Identifier mcmId) throws CreateObjectException {
		if (creatorId == null || domainId == null || name == null ||
				description == null || hostname == null || equipmentId == null || mcmId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			KIS kis = new KIS(IdentifierPool.getGeneratedIdentifier(ObjectEntities.KIS_ENTITY_CODE),
					creatorId,
					0L,
					domainId,
					name,
					description,
					hostname,
					tcpPort,
					equipmentId,
					mcmId);
			kis.changed = true;
			return kis;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		KIS_Transferable kt = (KIS_Transferable) transferable;
		super.fromTransferable(kt.header, new Identifier(kt.domain_id));

		this.equipmentId = new Identifier(kt.equipment_id);
		this.mcmId = new Identifier(kt.mcm_id);
		this.name = kt.name;
		this.description = kt.description;
		this.hostname = kt.hostname;
		this.tcpPort = kt.tcp_port;

		Set characteristicIds = Identifier.fromTransferables(kt.characteristic_ids);
		this.characteristics = new HashSet(kt.characteristic_ids.length);
		this.setCharacteristics0(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));

	}

	public IDLEntity getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();

		return new KIS_Transferable(super.getHeaderTransferable(),
				(Identifier_Transferable) this.getDomainId().getTransferable(),
				this.name,
				this.description,
				this.hostname,
				this.tcpPort,
				(Identifier_Transferable) this.equipmentId.getTransferable(),
				(Identifier_Transferable) this.mcmId.getTransferable(),
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
		super.changed = true;
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
		super.changed = true;
	}

	protected synchronized void setAttributes(Date created,
											Date modified,
											Identifier creatorId,
											Identifier modifierId,
											long version,
											Identifier domainId,
											String name,
											String description,
											String hostname,
											short tcpPort,
											Identifier equipmentId,
											Identifier mcmId) {
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

	public Set getDependencies() {
		Set dependencies = new HashSet();
		dependencies.add(this.equipmentId);
		dependencies.add(this.mcmId);
		return dependencies;
	}

	public void addCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.add(characteristic);
			super.changed = true;
		}
	}

	public void removeCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.remove(characteristic);
			super.changed = true;
		}
	}

	public Set getCharacteristics() {
		return Collections.unmodifiableSet(this.characteristics);
	}

	public void setCharacteristics0(final Set characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public void setCharacteristics(final Set characteristics) {
		this.setCharacteristics0(characteristics);
		super.changed = true;
	}

	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_KIS;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
		super.changed = true;
	}
	/**
	 * @param equipmentId The equipmentId to set.
	 */
	public void setEquipmentId(final Identifier equipmentId) {
		this.equipmentId = equipmentId;
		super.changed = true;
	}
	/**
	 * @param hostname The hostname to set.
	 */
	public void setHostName(final String hostname) {
		this.hostname = hostname;
		super.changed = true;
	}
	/**
	 * @param tcpPort The tcpPort to set.
	 */
	public void setTCPPort(final short tcpPort) {
		this.tcpPort = tcpPort;
		super.changed = true;
	}

	/**
	 * @return <code>Set&lt;MeasurementPort&gt;</code>
	 */
	public Set getMeasurementPorts() {
		try {
			return ConfigurationStorableObjectPool
					.getStorableObjectsByCondition(
							new LinkedIdsCondition(
									this.id,
									ObjectEntities.MEASUREMENTPORT_ENTITY_CODE),
							true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}
}
