/*
 * $Id: Port.java,v 1.50 2005/04/04 13:09:40 bass Exp $
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

import com.syrus.AMFICOM.configuration.corba.PortSort;
import com.syrus.AMFICOM.configuration.corba.Port_Transferable;
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
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.50 $, $Date: 2005/04/04 13:09:40 $
 * @author $Author: bass $
 * @module config_v1
 */
public class Port extends StorableObject implements Characterizable, TypedObject {
	private static final long serialVersionUID = -5139393638116159453L;

	private PortType type;
	private String description;
	private Identifier equipmentId;
	private int sort;

	private Set characteristics;

	private StorableObjectDatabase portDatabase;

	public Port(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.characteristics = new HashSet();
		this.portDatabase = ConfigurationDatabaseContext.getPortDatabase();
		try {
			this.portDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public Port(Port_Transferable pt) throws CreateObjectException {
		this.portDatabase = ConfigurationDatabaseContext.getPortDatabase();
		fromTransferable(pt);
	}

	protected Port(Identifier id,
					 Identifier creatorId,
					 long version,
					 PortType type,
					 String description,
					 Identifier equipmentId,
					 int sort) {
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

		this.portDatabase = ConfigurationDatabaseContext.getPortDatabase();
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
	public static Port createInstance(Identifier creatorId,
										PortType type,
										String description,
										Identifier equipmentId,
										PortSort sort) throws CreateObjectException {
		if (creatorId == null || type == null || description == null ||
				type == null || equipmentId == null || sort == null )
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			Port port = new Port(IdentifierPool.getGeneratedIdentifier(ObjectEntities.PORT_ENTITY_CODE),
						creatorId,
						0L,
						type,
						description,
						equipmentId,
						sort.value());
			port.changed = true;
			return port;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("Port.createInstance | cannot generate identifier ", e);
		}
	}
	
	protected void fromTransferable(IDLEntity transferable)
			throws CreateObjectException {
		Port_Transferable pt = (Port_Transferable) transferable;
		super.fromTransferable(pt.header);

		try {
			this.type = (PortType)ConfigurationStorableObjectPool.getStorableObject(new Identifier(pt.type_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.description = new String(pt.description);
		this.equipmentId = new Identifier(pt.equipment_id);

		this.sort = pt.sort.value();

		try {
			this.characteristics = new HashSet(pt.characteristic_ids.length);
			for (int i = 0; i < pt.characteristic_ids.length; i++)
				this.characteristics.add(GeneralStorableObjectPool.getStorableObject(new Identifier(pt.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}
	

	public IDLEntity getTransferable() {
		int i = 0;

		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();

		return new Port_Transferable(super.getHeaderTransferable(),
									 (Identifier_Transferable)this.type.getId().getTransferable(),
									 new String(this.description),
									 (Identifier_Transferable)this.equipmentId.getTransferable(),
									 PortSort.from_int(this.sort),
									 charIds);
	}

	public StorableObjectType getType() {
		return this.type;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description){
		this.description = description;
		super.changed = true;
	}

	public Identifier getEquipmentId() {
		return this.equipmentId;
	}

	public int getSort() {
		return this.sort;
	}
	
	public void setSort(PortSort sort) {
		this.sort = sort.value();
		super.changed = true;
	}


	public void addCharacteristic(Characteristic characteristic) {
		if (characteristic != null) {
			this.characteristics.add(characteristic);
			super.changed = true;
		}
	}

	public void removeCharacteristic(Characteristic characteristic) {
		if (characteristic != null){
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
		return CharacteristicSort.CHARACTERISTIC_SORT_PORT;
	}

	protected synchronized void setAttributes(Date created,
												Date modified,
												Identifier creatorId,
												Identifier modifierId,
												long version,
												PortType type,
												String description,
												Identifier equipmentId,
												int sort) {
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
	public void setEquipmentId(Identifier equipmentId) {
		this.equipmentId = equipmentId;
		super.changed = true;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(PortType type) {
		this.type = type;
		super.changed = true;
	}
}
