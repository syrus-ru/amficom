/*
 * $Id: MeasurementPort.java,v 1.42 2005/04/01 11:02:30 bass Exp $
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

import com.syrus.AMFICOM.configuration.corba.MeasurementPort_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
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
 * @version $Revision: 1.42 $, $Date: 2005/04/01 11:02:30 $
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

	private StorableObjectDatabase measurementPortDatabase;

	public MeasurementPort(Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		this.measurementPortDatabase = ConfigurationDatabaseContext.getMeasurementPortDatabase();
		this.characteristics = new HashSet();
		try {
			this.measurementPortDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public MeasurementPort(MeasurementPort_Transferable mpt) throws CreateObjectException {
		super(mpt.header);

		try {
			this.type = (MeasurementPortType)ConfigurationStorableObjectPool.getStorableObject(new Identifier(mpt.type_id), true);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.name = new String(mpt.name);
		this.description = new String(mpt.description);

		this.kisId = new Identifier(mpt.kis_id);
		this.portId = new Identifier(mpt.port_id);	
		try {
			this.characteristics = new HashSet(mpt.characteristic_ids.length);
			for (int i = 0; i < mpt.characteristic_ids.length; i++)
				this.characteristics.add(GeneralStorableObjectPool.getStorableObject(new Identifier(mpt.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.measurementPortDatabase = ConfigurationDatabaseContext.getMeasurementPortDatabase();
	}

	protected MeasurementPort(Identifier id,
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

		this.measurementPortDatabase = ConfigurationDatabaseContext.getMeasurementPortDatabase();
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
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("MeasurementPort.createInstance | cannot generate identifier ", e);
		}
	}

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();

		return new MeasurementPort_Transferable(super.getHeaderTransferable(),
												(Identifier_Transferable)this.type.getId().getTransferable(),
												new String(this.name),
												new String(this.description),
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

	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORT;
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
