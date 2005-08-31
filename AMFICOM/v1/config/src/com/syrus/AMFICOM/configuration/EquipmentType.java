/*
 * $Id: EquipmentType.java,v 1.85 2005/08/31 13:25:08 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_TYPE_CODE;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlEquipmentType;
import com.syrus.AMFICOM.configuration.corba.IdlEquipmentTypeHelper;
import com.syrus.AMFICOM.configuration.xml.XmlEquipmentType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CharacterizableDelegate;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ImportUidMapDatabase;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Shitlet;

/**
 * @version $Revision: 1.85 $, $Date: 2005/08/31 13:25:08 $
 * @author $Author: bass $
 * @module config
 */

public final class EquipmentType extends StorableObjectType implements Characterizable, Namable, XmlBeansTransferable<XmlEquipmentType> {
	private static final long serialVersionUID = 9157517478787463967L;

	private String name;
	private String manufacturer;
	private String manufacturerCode;

	private transient CharacterizableDelegate characterizableDelegate;

	EquipmentType(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		try {
			DatabaseContext.getDatabase(EQUIPMENT_TYPE_CODE).retrieve(this);
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


	@Shitlet
	EquipmentType(final Identifier creatorId,
			final StorableObjectVersion version,
			final XmlEquipmentType xmlEquipmentType,
			final ClonedIdsPool clonedIdsPool,
			final String importType) throws CreateObjectException, ApplicationException {

		super(clonedIdsPool.getClonedId(PORT_TYPE_CODE, xmlEquipmentType.getId()),
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				"",
				"");
		this.fromXmlTransferable(xmlEquipmentType, clonedIdsPool, importType);
	}

	@SuppressWarnings("unused")
	@Shitlet
	private static EquipmentType createInstance(
			final Identifier creatorId,
			final String importType,
			final XmlEquipmentType xmlEquipmentType,
			final ClonedIdsPool clonedIdsPool) throws CreateObjectException {

		try {
			final XmlIdentifier xmlId = xmlEquipmentType.getId();
			Identifier existingIdentifier = Identifier.fromXmlTransferable(xmlId, importType);
			EquipmentType portType = null;
			if(existingIdentifier != null) {
				portType = StorableObjectPool.getStorableObject(existingIdentifier, true);
				if(portType != null) {
					portType.fromXmlTransferable(xmlEquipmentType, clonedIdsPool, importType);
				}
				else{
					ImportUidMapDatabase.delete(importType, xmlId);
				}
			}
			if(portType == null) {
				portType = portType = new EquipmentType(
						creatorId,
						StorableObjectVersion.createInitial(),
						xmlEquipmentType,
						clonedIdsPool,
						importType);
				ImportUidMapDatabase.insert(importType, xmlId, portType.id);
			}
			assert portType.isValid() : OBJECT_STATE_ILLEGAL;
			portType.markAsChanged();
			return portType;
		} catch (Exception e) {
			System.out.println(xmlEquipmentType);
			throw new CreateObjectException("EquipmentType.createInstance |  ", e);
		}
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
			final EquipmentType equipmentType = new EquipmentType(IdentifierPool.getGeneratedIdentifier(EQUIPMENT_TYPE_CODE),
										creatorId,
										StorableObjectVersion.createInitial(),
										codename,
										description,
										name,
										manufacturer,
										manufacturerCode);

			assert equipmentType.isValid() : OBJECT_STATE_ILLEGAL;

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
	 * @param xmlEquipmentType
	 * @param clonedIdsPool
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, ClonedIdsPool, String)
	 */
	@Shitlet
	public void fromXmlTransferable(final XmlEquipmentType xmlEquipmentType,
			final ClonedIdsPool clonedIdsPool,
			final String importType)
	throws ApplicationException {
		this.name = xmlEquipmentType.getName();
		this.codename = xmlEquipmentType.getCodename();
		this.description = xmlEquipmentType.getDescription();
		this.manufacturer = xmlEquipmentType.getManufacturer();
		this.manufacturerCode = xmlEquipmentType.getManufacturerCode();
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

	/**
	 * @see XmlBeansTransferable#getXmlTransferable()
	 */
	@Shitlet
	public XmlEquipmentType getXmlTransferable() {
		final XmlEquipmentType xmlEquipmentType = XmlEquipmentType.Factory.newInstance();
		xmlEquipmentType.setId(this.id.getXmlTransferable());
		xmlEquipmentType.setName(this.name);
		xmlEquipmentType.setCodename(this.codename);
		xmlEquipmentType.setDescription(this.description);
		xmlEquipmentType.setManufacturer(this.manufacturer);
		xmlEquipmentType.setManufacturerCode(this.manufacturerCode);
		return xmlEquipmentType;
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
