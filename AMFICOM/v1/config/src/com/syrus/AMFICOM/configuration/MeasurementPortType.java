/*
 * $Id: MeasurementPortType.java,v 1.34 2005/03/05 21:37:24 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.34 $, $Date: 2005/03/05 21:37:24 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public class MeasurementPortType extends StorableObjectType implements Characterizable {
	private static final long serialVersionUID = 7733425194674608181L;

	private String name;

	private Collection characteristics;

	private StorableObjectDatabase measurementPortTypeDatabase;

	public MeasurementPortType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new ArrayList();
		this.measurementPortTypeDatabase = ConfigurationDatabaseContext.measurementPortTypeDatabase;
		try {
			this.measurementPortTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public MeasurementPortType(MeasurementPortType_Transferable mptt) throws CreateObjectException {
		super(mptt.header,
			  new String(mptt.codename),
			  new String(mptt.description));		
		this.name = mptt.name;
		try {
			this.characteristics = new ArrayList(mptt.characteristic_ids.length);
			for (int i = 0; i < mptt.characteristic_ids.length; i++)
				this.characteristics.add(GeneralStorableObjectPool.getStorableObject(new Identifier(mptt.characteristic_ids[i]), true));
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}

		this.measurementPortTypeDatabase = ConfigurationDatabaseContext.measurementPortTypeDatabase;
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
			this.characteristics = new ArrayList();

			this.measurementPortTypeDatabase = ConfigurationDatabaseContext.measurementPortTypeDatabase;
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

	public Object getTransferable() {
		int i = 0;
		Identifier_Transferable[] charIds = new Identifier_Transferable[this.characteristics.size()];
		for (Iterator iterator = this.characteristics.iterator(); iterator.hasNext();)
			charIds[i++] = (Identifier_Transferable)((Characteristic)iterator.next()).getId().getTransferable();

		return new MeasurementPortType_Transferable(super.getHeaderTransferable(),
													new String(super.codename),
													(super.description != null) ? (new String(super.description)) : "",
													(this.name != null) ? (new String(this.name)) : "",
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

	public List getDependencies() {
		return Collections.EMPTY_LIST;
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

	public Collection getCharacteristics() {
		return Collections.unmodifiableCollection(this.characteristics);
	}

	public void setCharacteristics0(final Collection characteristics) {
		this.characteristics.clear();
		if (characteristics != null)
			this.characteristics.addAll(characteristics);
	}

	public void setCharacteristics(final Collection characteristics) {
		this.setCharacteristics0(characteristics);
		super.changed = true;
	}

	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE;
	}
}
