/*
 * $Id: MeasurementPortType.java,v 1.43 2005/04/14 16:00:32 bass Exp $
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

import com.syrus.AMFICOM.configuration.corba.MeasurementPortType_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.43 $, $Date: 2005/04/14 16:00:32 $
 * @author $Author: bass $
 * @module config_v1
 */

public class MeasurementPortType extends StorableObjectType implements Characterizable, Namable {
	private static final long serialVersionUID = 7733425194674608181L;

	private String name;

	private Set characteristics;

	public MeasurementPortType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new HashSet();

		MeasurementPortTypeDatabase database = ConfigurationDatabaseContext.getMeasurementPortTypeDatabase();
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public MeasurementPortType(MeasurementPortType_Transferable mptt) throws CreateObjectException {
		try {
			this.fromTransferable(mptt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	protected MeasurementPortType(Identifier id,
								Identifier creatorId,
								long version,
								String codename,
								String description,
								String name) {
			super(id,
				  new Date(System.currentTimeMillis()),
				  new Date(System.currentTimeMillis()),
				  creatorId,
				  creatorId,
				  version,
				  codename,
				  description);				
			this.name = name;
			this.characteristics = new HashSet();
	}
	
	/**
	 * create new instance for client 
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @throws CreateObjectException
	 */
	public static MeasurementPortType createInstance(Identifier creatorId,
													 String codename,
													 String description,
													 String name) throws CreateObjectException{
		if (creatorId == null || codename == null || name == null || description == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			MeasurementPortType measurementPortType = new MeasurementPortType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE),
										   creatorId,
										   0L,
										   codename,
										   description,
										   name);
			measurementPortType.changed = true;
			return measurementPortType;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("MeasurementPortType.createInstance | cannot generate identifier ", e);
		}
	}

	protected void fromTransferable(IDLEntity transferable) throws ApplicationException {
		MeasurementPortType_Transferable mptt = (MeasurementPortType_Transferable) transferable;
		super.fromTransferable(mptt.header, mptt.codename, mptt.description);
		this.name = mptt.name;

		Set characteristicIds = Identifier.fromTransferables(mptt.characteristic_ids);
		this.characteristics = new HashSet(mptt.characteristic_ids.length);
		this.setCharacteristics0(GeneralStorableObjectPool.getStorableObjects(characteristicIds, true));
	}

	public IDLEntity getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();

		return new MeasurementPortType_Transferable(super.getHeaderTransferable(),
													super.codename,
													super.description != null ? super.description : "",
													this.name != null ? this.name : "",
													charIds);
	}

	protected synchronized void setAttributes(Date created,
												Date modified,
												Identifier creatorId,
												Identifier modifierId,
												long version,
												String codename,
												String description,
												String name) {
		super.setAttributes(created,
							modified,
							creatorId,
							modifierId,
							version,
							codename,
							description);
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		super.changed = true;
		this.name = name;
	}	

	public Set getDependencies() {
		return Collections.EMPTY_SET;
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
		return CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE;
	}
}
