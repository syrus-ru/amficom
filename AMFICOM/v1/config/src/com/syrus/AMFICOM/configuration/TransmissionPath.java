/*
 * $Id: TransmissionPath.java,v 1.55 2005/04/04 13:09:40 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.configuration.corba.TransmissionPath_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
/**
 * @version $Revision: 1.55 $, $Date: 2005/04/04 13:09:40 $
 * @author $Author: bass $
 * @module config_v1
 */

public class TransmissionPath extends DomainMember implements MonitoredDomainMember, Characterizable, TypedObject {

	private static final long serialVersionUID = 8129503678304843903L;

	private TransmissionPathType type;
	private String name;
	private String description;
	private Identifier startPortId;
	private Identifier finishPortId;

	private Set characteristics;

	private StorableObjectDatabase transmissionPathDatabase;

	public TransmissionPath(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.characteristics = new HashSet();

		this.transmissionPathDatabase = ConfigurationDatabaseContext.getTransmissionPathDatabase();
		try {
			this.transmissionPathDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public TransmissionPath(TransmissionPath_Transferable tpt) throws CreateObjectException {
		this.transmissionPathDatabase = ConfigurationDatabaseContext.getTransmissionPathDatabase();
		fromTransferable(tpt);
	}

	protected TransmissionPath(Identifier id,
			Identifier creatorId,
			long version,
			Identifier domainId,
			String name,
			String description,
			TransmissionPathType type,
			Identifier startPortId,
			Identifier finishPortId) {
		super(id, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), creatorId, creatorId, version, domainId);
		this.name = name;
		this.description = description;
		this.type = type;
		this.startPortId = startPortId;
		this.finishPortId = finishPortId;

		this.characteristics = new HashSet();

		this.transmissionPathDatabase = ConfigurationDatabaseContext.getTransmissionPathDatabase();
	}

	/**
	 * create new instance for client
	 * 
	 * @param creatorId
	 * @param domainId
	 * @param name
	 * @param description
	 * @param startPortId
	 * @param finishPortId
	 * @throws CreateObjectException
	 */
	public static TransmissionPath createInstance(Identifier creatorId,
																		Identifier domainId,
																		String name,
																		String description,
																		TransmissionPathType type,
																		Identifier startPortId,
																		Identifier finishPortId) throws CreateObjectException {
		if (creatorId == null || domainId == null || name == null || description == null ||
				type == null || startPortId == null || finishPortId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			TransmissionPath transmissionPath = new TransmissionPath(IdentifierPool.getGeneratedIdentifier(ObjectEntities.TRANSPATH_ENTITY_CODE),
					creatorId,
					0L,
					domainId,
					name,
					description,
					type,
					startPortId,
					finishPortId);
			transmissionPath.changed = true;
			return transmissionPath;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("TransmissionPath.createInstance | cannot generate identifier ", e);
		}
	}
	
	protected void fromTransferable(IDLEntity transferable)
			throws CreateObjectException {
		TransmissionPath_Transferable tpt = (TransmissionPath_Transferable) transferable;
		super.fromTransferable(tpt.header,
				new Identifier(tpt.domain_id));

		this.name = new String(tpt.name);
		this.description = new String(tpt.description);
		this.startPortId = new Identifier(tpt.start_port_id);
		this.finishPortId = new Identifier(tpt.finish_port_id);

		try {
			this.characteristics = new HashSet(tpt.characteristic_ids.length);
			for (int i = 0; i < tpt.characteristic_ids.length; i++)
				this.characteristics.add(GeneralStorableObjectPool.getStorableObject(new Identifier(tpt.characteristic_ids[i]), true));

			this.type = (TransmissionPathType) ConfigurationStorableObjectPool.getStorableObject(new Identifier(tpt.type_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	public IDLEntity getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable) ((Characteristic) iterator.next()).getId().getTransferable();

		return new TransmissionPath_Transferable(super.getHeaderTransferable(),
				(Identifier_Transferable) this.getDomainId().getTransferable(),
				new String(this.name),
				new String(this.description),
				(Identifier_Transferable) this.type.getId().getTransferable(),
				(Identifier_Transferable) this.startPortId.getTransferable(),
				(Identifier_Transferable) this.finishPortId.getTransferable(),
				charIds);
	}

	public String getName() {
		return this.name;
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
		super.changed = true;
	}

	public Identifier getFinishPortId() {
		return this.finishPortId;
	}

	public Identifier getStartPortId() {
		return this.startPortId;
	}

	protected synchronized void setAttributes(Date created,
												Date modified,
												Identifier creatorId,
												Identifier modifierId,
												long version,
												Identifier domainId,
												String name,
												String description,
												TransmissionPathType type,
												Identifier startPortId,
												Identifier finishPortId) {
		super.setAttributes(created,
							modified,
							creatorId,
							modifierId,
							version,
							domainId);
		this.name = name;
		this.description = description;
		this.type = type;
		this.startPortId = startPortId;
		this.finishPortId = finishPortId;
	}

	public Set getDependencies() {
		Set dependencies = new HashSet();
		dependencies.add(this.startPortId);
		dependencies.add(this.finishPortId);
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
	
	/**
	 * @param finishPortId The finishPortId to set.
	 */
	public void setFinishPortId(Identifier finishPortId) {
		this.finishPortId = finishPortId;
		super.changed = true;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
		super.changed = true;
	}
	/**
	 * @param startPortId The startPortId to set.
	 */
	public void setStartPortId(Identifier startPortId) {
		this.startPortId = startPortId;
		super.changed = true;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(TransmissionPathType type) {
		this.type = type;
		super.changed = true;
	}

	/**
	 * @see com.syrus.AMFICOM.configuration.MonitoredDomainMember#getMonitoredElementIds()
	 */
	public Set getMonitoredElementIds() {
		//TODO Implement
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_TRANSMISSIONPATH;
	}
}
