/*
 * $Id: KIS.java,v 1.57 2005/01/31 13:47:50 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.KIS_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterized;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.administration.DomainMember;

/**
 * @version $Revision: 1.57 $, $Date: 2005/01/31 13:47:50 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public class KIS extends DomainMember implements Characterized {

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

	private List measurementPortIds;	//List <Identifier>

	private List characteristics;
	private StorableObjectDatabase kisDatabase;

	public KIS(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.measurementPortIds = new LinkedList();
		this.characteristics = new ArrayList();
		this.kisDatabase = ConfigurationDatabaseContext.kisDatabase;
		try {
			this.kisDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public KIS(KIS_Transferable kt) throws CreateObjectException {
		super(kt.header, new Identifier(kt.domain_id));

		this.equipmentId = new Identifier(kt.equipment_id);
		this.mcmId = new Identifier(kt.mcm_id);
		this.name = kt.name;
		this.description = kt.description;
		this.hostname = kt.hostname;
		this.tcpPort = kt.tcp_port;

		this.measurementPortIds = new ArrayList(kt.measurement_port_ids.length);
		for (int i = 0; i < kt.measurement_port_ids.length; i++)
			this.measurementPortIds.add(new Identifier(kt.measurement_port_ids[i]));

		try {
			this.characteristics = new ArrayList(kt.characteristic_ids.length);
			for (int i = 0; i < kt.characteristic_ids.length; i++)
				this.characteristics.add(GeneralStorableObjectPool.getStorableObject(new Identifier(kt.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.kisDatabase = ConfigurationDatabaseContext.kisDatabase;
	}

	protected KIS(Identifier id,
								Identifier creatorId,
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
					domainId);
		this.name = name;
		this.description = description;
		this.hostname = hostname;
		this.tcpPort = tcpPort;
		this.equipmentId = equipmentId;
		this.mcmId = mcmId;
		this.measurementPortIds = new LinkedList();
		this.characteristics = new ArrayList();

		super.currentVersion = super.getNextVersion();

		this.kisDatabase = ConfigurationDatabaseContext.kisDatabase;
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
			return new KIS(IdentifierPool.getGeneratedIdentifier(ObjectEntities.KIS_ENTITY_CODE),
					creatorId,
					domainId,
					name,
					description,
					hostname,
					tcpPort,
					equipmentId,
					mcmId);
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("KIS.createInstance | cannot generate identifier ", e);
		}
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.kisDatabase != null)
				this.kisDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
		}
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] mportIds = new Identifier_Transferable[this.measurementPortIds.size()];
		for (Iterator iterator = this.measurementPortIds.iterator(); iterator.hasNext();)
			mportIds[i++] = (Identifier_Transferable)((Identifier)iterator.next()).getTransferable();

		i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();

		return new KIS_Transferable(super.getHeaderTransferable(),
															(Identifier_Transferable)this.getDomainId().getTransferable(),
															new String(this.name),
															new String(this.description),
															this.hostname,
															this.tcpPort,
															(Identifier_Transferable)this.equipmentId.getTransferable(),
															(Identifier_Transferable)this.mcmId.getTransferable(),
															charIds,
															mportIds);
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
		super.currentVersion = super.getNextVersion();
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

	public void setMCMId(Identifier mcmId) {
		this.mcmId = mcmId;
		super.currentVersion = super.getNextVersion();
	}

	public List getMeasurementPortIds() {
		return this.measurementPortIds;
	}

	public List retrieveMonitoredElements() throws RetrieveObjectException, ObjectNotFoundException {
		try {
			return (List)this.kisDatabase.retrieveObject(this, KISDatabase.RETRIEVE_MONITORED_ELEMENTS, null);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
		}
	}

	protected synchronized void setAttributes(Date created,
													Date modified,
													Identifier creatorId,
													Identifier modifierId,
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
							domainId);
		this.name = name;
		this.description = description;
		this.hostname = hostname;
		this.tcpPort = tcpPort;
		this.equipmentId = equipmentId;
		this.mcmId = mcmId;
	}

	protected synchronized void setMeasurementPortIds0(List measurementPortIds) {
		this.measurementPortIds.clear();
		if (measurementPortIds != null)
			this.measurementPortIds.addAll(measurementPortIds);
	}

	public void setMeasurementPortIds(List measurementPortIds) {
		this.setMeasurementPortIds0(measurementPortIds);
		super.currentVersion = super.getNextVersion();
	}

	public List getDependencies() {
		List dependencies = new LinkedList();
		dependencies.add(this.equipmentId);
		dependencies.add(this.mcmId);
		dependencies.addAll(this.measurementPortIds);
		dependencies.addAll(this.characteristics);
		return dependencies;
	}

	public void addCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.add(characteristic);
			super.currentVersion = super.getNextVersion();
		}
	}

	public void removeCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.remove(characteristic);
			super.currentVersion = super.getNextVersion();
		}
	}

	public List getCharacteristics() {
		return Collections.unmodifiableList(this.characteristics);
	}

	protected void setCharacteristics0(final List characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public void setCharacteristics(final List characteristics) {
		this.setCharacteristics0(characteristics);
		super.currentVersion = super.getNextVersion();
	}	
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
		super.currentVersion = super.getNextVersion();
	}
	/**
	 * @param equipmentId The equipmentId to set.
	 */
	public void setEquipmentId(Identifier equipmentId) {
		this.equipmentId = equipmentId;
		super.currentVersion = super.getNextVersion();
	}
	/**
	 * @param hostname The hostname to set.
	 */
	public void setHostName(String hostname) {
		this.hostname = hostname;
		super.currentVersion = super.getNextVersion();
	}
	/**
	 * @param tcpPort The tcpPort to set.
	 */
	public void setTCPPort(short tcpPort) {
		this.tcpPort = tcpPort;
		super.currentVersion = super.getNextVersion();
	}
}
