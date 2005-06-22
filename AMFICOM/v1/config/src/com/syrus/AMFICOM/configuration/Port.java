/*
 * $Id: Port.java,v 1.69 2005/06/22 10:05:17 bass Exp $
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

import com.syrus.AMFICOM.configuration.corba.IdlPort;
import com.syrus.AMFICOM.configuration.corba.IdlPortPackage.PortSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;

/**
 * @version $Revision: 1.69 $, $Date: 2005/06/22 10:05:17 $
 * @author $Author: bass $
 * @module config_v1
 */
public final class Port extends StorableObject implements Characterizable, TypedObject {
	private static final long serialVersionUID = -5139393638116159453L;

	private PortType type;
	private String description;
	private Identifier equipmentId;
	private int sort;

	private Set<Characteristic> characteristics;

	Port(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.characteristics = new HashSet();

		PortDatabase database = (PortDatabase) DatabaseContext.getDatabase(ObjectEntities.PORT_CODE);
		try {
			database.retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	Port(final IdlPort pt) throws CreateObjectException {
		try {
			this.fromTransferable(pt);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	Port(final Identifier id,
			final Identifier creatorId,
			final long version,
			final PortType type,
			final String description,
			final Identifier equipmentId,
			final int sort) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.type = type;
		this.description = description;
		this.equipmentId = equipmentId;
		this.sort = sort;

		this.characteristics = new HashSet();
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param type
	 * @param description
	 * @param equipmentId
	 * @param sort
	 * @throws CreateObjectException
	 */
	public static Port createInstance(final Identifier creatorId,
			final PortType type,
			final String description,
			final Identifier equipmentId,
			final PortSort sort) throws CreateObjectException {
		if (creatorId == null || type == null || description == null ||
				type == null || equipmentId == null || sort == null )
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			Port port = new Port(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PORT_CODE),
						creatorId,
						0L,
						type,
						description,
						equipmentId,
						sort.value());

			assert port.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			port.markAsChanged();

			return port;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		IdlPort pt = (IdlPort) transferable;
		super.fromTransferable(pt.header);

		this.type = (PortType) StorableObjectPool.getStorableObject(new Identifier(pt._typeId), true);

		this.description = pt.description;
		this.equipmentId = new Identifier(pt.equipmentId);

		this.sort = pt.sort.value();

		Set characteristicIds = Identifier.fromTransferables(pt.characteristicIds);
		this.characteristics = new HashSet(pt.characteristicIds.length);
		this.setCharacteristics0(StorableObjectPool.getStorableObjects(characteristicIds, true));
	}
	

	public IdlPort getTransferable() {
		IdlIdentifier[] charIds = Identifier.createTransferables(this.characteristics);

		return new IdlPort(super.getHeaderTransferable(),
				 this.type.getId().getTransferable(),
				 this.description,
				 this.equipmentId.getTransferable(),
				 PortSort.from_int(this.sort),
				 charIds);
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	public Identifier getEquipmentId() {
		return this.equipmentId;
	}

	public PortSort getSort() {
		return PortSort.from_int(this.sort);
	}
	
	public void setSort(final PortSort sort) {
		this.sort = sort.value();
		super.markAsChanged();
	}


	public void addCharacteristic(final Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.add(characteristic);
			super.markAsChanged();
		}
	}

	public void removeCharacteristic(final Characteristic characteristic) {
		if (characteristic != null){
			this.characteristics.remove(characteristic);
			super.markAsChanged();
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
		super.markAsChanged();
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final PortType type,
			final String description,
			final Identifier equipmentId,
			final int sort) {
		super.setAttributes(created,
				modified,
				creatorId,
				modifierId,
				version);
		this.type = type;
		this.description = description;
		this.equipmentId = equipmentId;
		this.sort = sort;
	}

	public Set getDependencies() {
		Set dependencies = new HashSet(2);
		dependencies.add(this.type);
		dependencies.add(this.equipmentId);
		return dependencies;
	}	
	/**
	 * @param equipmentId The equipmentId to set.
	 */
	public void setEquipmentId(final Identifier equipmentId) {
		this.equipmentId = equipmentId;
		super.markAsChanged();
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(final PortType type) {
		this.type = type;
		super.markAsChanged();
	}
}
