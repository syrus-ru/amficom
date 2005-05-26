/*
 * $Id: MeasurementPort.java,v 1.55 2005/05/26 15:31:16 bass Exp $
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

import com.syrus.AMFICOM.configuration.corba.MeasurementPort_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
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
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.55 $, $Date: 2005/05/26 15:31:16 $
 * @author $Author: bass $
 * @module config_v1
 */
public class MeasurementPort extends StorableObject implements Characterizable, TypedObject {
	private static final long serialVersionUID = -5100885507408715167L;

	private MeasurementPortType type;

	private String name;
	private String description;

	private Identifier kisId;
	private Identifier portId;

	private Set characteristics;

	MeasurementPort(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.characteristics = new HashSet();

		MeasurementPortDatabase database = (MeasurementPortDatabase) DatabaseContext.getDatabase(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public MeasurementPort(MeasurementPort_Transferable mpt) throws CreateObjectException {
		try {
			this.fromTransferable(mpt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	MeasurementPort(Identifier id,
							Identifier creatorId,
							long version,
							MeasurementPortType type,
							String name,
							String description,	
							Identifier kisId,
							Identifier portId) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.type = type;
		this.name = name;
		this.description = description;
		this.kisId = kisId;
		this.portId = portId;
		this.characteristics = new HashSet();
	}
	
	/**
	 * create new instance for client
	 * @param creatorId
	 * @param type
	 * @param name
	 * @param description
	 * @param kisId
	 * @param portId
	 * @throws CreateObjectException
	 */
	public static MeasurementPort createInstance(	Identifier creatorId,
																		MeasurementPortType type,
																		String name,
																		String description,	
																		Identifier kisId,
																		Identifier portId) throws CreateObjectException{
		if (creatorId == null || type == null || name == null || description == null ||
				kisId == null || portId == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			MeasurementPort measurementPort = new MeasurementPort(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE),
									   creatorId,
									   0L,
									   type,
									   name,
									   description,
									   kisId,
									   portId);
			measurementPort.changed = true;
			return measurementPort;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		MeasurementPort_Transferable mpt = (MeasurementPort_Transferable) transferable;
		super.fromTransferable(mpt.header);

		this.type = (MeasurementPortType) StorableObjectPool.getStorableObject(new Identifier(mpt.type_id), true);

		this.name = mpt.name;
		this.description = mpt.description;

		this.kisId = new Identifier(mpt.kis_id);
		this.portId = new Identifier(mpt.port_id);

		Set characteristicIds = Identifier.fromTransferables(mpt.characteristic_ids);
		this.characteristics = new HashSet(mpt.characteristic_ids.length);
		this.setCharacteristics0(StorableObjectPool.getStorableObjects(characteristicIds, true));
	}

	public IDLEntity getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();

		return new MeasurementPort_Transferable(super.getHeaderTransferable(),
												(Identifier_Transferable)this.type.getId().getTransferable(),
												this.name,
												this.description,
												(Identifier_Transferable)this.kisId.getTransferable(),
												(Identifier_Transferable)this.portId.getTransferable(),
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
	
	public String getName() {
		return this.name;
	}
	
	public Identifier getKISId() {
		return this.kisId;
	}

	public Identifier getPortId() {
		return this.portId;
	}

	protected synchronized void setAttributes(Date created,
											Date modified,
											Identifier creatorId,
											Identifier modifierId,
											long version,
											MeasurementPortType type,
											String name,
											String description,	
											Identifier kisId,
											Identifier portId) {
		super.setAttributes(created,
						modified,
						creatorId,
						modifierId,
						version);
		this.type = type;
		this.name = name;
		this.description = description;
		this.kisId = kisId;
		this.portId = portId;
	}

	public Set getDependencies() {
		Set dependencies = new HashSet();
		dependencies.add(this.type);
		dependencies.add(this.kisId);
		dependencies.add(this.portId);
		return dependencies;
	}

	public void setName(String name) {
		super.changed = true;
		this.name = name;
	}
	
	public void setType(MeasurementPortType type) {
		super.changed = true;
		this.type = type;
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
	 * @param kisId The kisId to set.
	 */
	public void setKISId(Identifier kisId) {
		this.kisId = kisId;
		super.changed = true;
	}
	/**
	 * @param portId The portId to set.
	 */
	public void setPortId(Identifier portId) {
		this.portId = portId;
		super.changed = true;
	}
}
