/*
 * $Id: EquipmentType.java,v 1.80 2005/08/08 14:23:52 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlEquipmentType;
import com.syrus.AMFICOM.configuration.corba.IdlEquipmentTypeHelper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CharacterizableDelegate;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;

/**
 * @version $Revision: 1.80 $, $Date: 2005/08/08 14:23:52 $
 * @author $Author: arseniy $
 * @module config
 */

public final class EquipmentType extends StorableObjectType implements Characterizable, Namable {
	private static final long serialVersionUID = 9157517478787463967L;

	private String name;
	private String manufacturer;
	private String manufacturerCode;

	private transient CharacterizableDelegate characterizableDelegate;

	EquipmentType(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		try {
			DatabaseContext.getDatabase(ObjectEntities.EQUIPMENT_TYPE_CODE).retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public EquipmentType(final IdlEquipmentType ett) throws CreateObjectException {
		try {
			this.fromTransferable(ett);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	EquipmentType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final String name,
			final String manufacturer,
			final String manufacturerCode) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);
		this.name = name;
		this.manufacturer = manufacturer;
		this.manufacturerCode = manufacturerCode;
	}


	/**
	 * Create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @throws CreateObjectException
	 */
	public static EquipmentType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final String name,
			final String manufacturer,
			final String manufacturerCode) throws CreateObjectException {
		if (creatorId == null || codename == null || description == null || name == null
								|| manufacturer == null || manufacturerCode == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final EquipmentType equipmentType = new EquipmentType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.EQUIPMENT_TYPE_CODE),
										creatorId,
										StorableObjectVersion.createInitial(),
										codename,
										description,
										name,
										manufacturer,
										manufacturerCode);

			assert equipmentType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

			equipmentType.markAsChanged();

			return equipmentType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlEquipmentType ett = (IdlEquipmentType) transferable;
		super.fromTransferable(ett, ett.codename, ett.description);
		this.name = ett.name;
		this.manufacturer = ett.manufacturer;
		this.manufacturerCode = ett.manufacturerCode;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlEquipmentType getTransferable(final ORB orb) {

		return IdlEquipmentTypeHelper.init(orb,
				super.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version.longValue(),
				super.codename,
				super.description != null ? super.description : "",
				this.name != null ? this.name : "",
				this.manufacturer != null ? this.manufacturer : "",
				this.manufacturerCode != null ? this.manufacturerCode : "");
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final String name,
			final String manufacturer,
			final String manufacturerCode) {
		super.setAttributes(created, modified, creatorId, modifierId, version, codename, description);
		this.name = name;
		this.manufacturer = manufacturer;
		this.manufacturerCode = manufacturerCode;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	@Override
	public Set<Identifiable> getDependencies() {
		return Collections.emptySet();
	}

	public Set<Characteristic> getCharacteristics(final boolean usePool) throws ApplicationException {
		if (this.characterizableDelegate == null) {
			this.characterizableDelegate = new CharacterizableDelegate(this.id);
		}
		return this.characterizableDelegate.getCharacteristics(usePool);
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(final String manufacturer) {
		this.manufacturer = manufacturer;
		super.markAsChanged();
	}

	public String getManufacturerCode() {
		return this.manufacturerCode;
	}

	public void setManufacturerCode(final String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
		super.markAsChanged();
	}
}
