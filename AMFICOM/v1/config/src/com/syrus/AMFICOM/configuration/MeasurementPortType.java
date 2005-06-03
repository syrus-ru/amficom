/*
 * $Id: MeasurementPortType.java,v 1.51 2005/06/03 20:37:53 arseniy Exp $
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

import com.syrus.AMFICOM.configuration.corba.MeasurementPortType_Transferable;
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
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.51 $, $Date: 2005/06/03 20:37:53 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public class MeasurementPortType extends StorableObjectType implements Characterizable, Namable {
	private static final long serialVersionUID = 7733425194674608181L;

	private String name;

	private Set characteristics;

	MeasurementPortType(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristics = new HashSet();

		MeasurementPortTypeDatabase database = (MeasurementPortTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public MeasurementPortType(final MeasurementPortType_Transferable mptt) throws CreateObjectException {
		try {
			this.fromTransferable(mptt);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	MeasurementPortType(final Identifier id,
			final Identifier creatorId,
			final long version,
			final String codename,
			final String description,
			final String name) {
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
	public static MeasurementPortType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final String name) throws CreateObjectException{
		if (creatorId == null || codename == null || name == null || description == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try {
			MeasurementPortType measurementPortType = new MeasurementPortType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE),
					creatorId,
					0L,
					codename,
					description,
					name);

			assert measurementPortType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			measurementPortType.markAsChanged();

			return measurementPortType;
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	protected void fromTransferable(final IDLEntity transferable) throws ApplicationException {
		MeasurementPortType_Transferable mptt = (MeasurementPortType_Transferable) transferable;
		super.fromTransferable(mptt.header, mptt.codename, mptt.description);
		this.name = mptt.name;

		Set characteristicIds = Identifier.fromTransferables(mptt.characteristic_ids);
		this.characteristics = new HashSet(mptt.characteristic_ids.length);
		this.setCharacteristics0(StorableObjectPool.getStorableObjects(characteristicIds, true));
	}

	public IDLEntity getTransferable() {
		final Identifier_Transferable[] charIds = Identifier.createTransferables(this.characteristics);

		return new MeasurementPortType_Transferable(super.getHeaderTransferable(),
				super.codename,
				super.description != null ? super.description : "",
				this.name != null ? this.name : "",
				charIds);
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final long version,
			final String codename,
			final String description,
			final String name) {
		super.setAttributes(created, modified, creatorId, modifierId, version, codename, description);
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}	

	public Set getDependencies() {
		return Collections.EMPTY_SET;
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
}
