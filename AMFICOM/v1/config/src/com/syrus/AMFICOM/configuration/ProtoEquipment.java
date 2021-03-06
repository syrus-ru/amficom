/*-
 * $Id: ProtoEquipment.java,v 1.34 2006/06/08 15:54:58 arseniy Exp $
 *
 * Copyright ? 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_THROW_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PROTOEQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.EXPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.POST_IMPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.PRE_IMPORT;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlProtoEquipment;
import com.syrus.AMFICOM.configuration.corba.IdlProtoEquipmentHelper;
import com.syrus.AMFICOM.configuration.xml.XmlProtoEquipment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.xml.XmlCharacteristic;
import com.syrus.AMFICOM.general.xml.XmlCharacteristicSeq;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Shitlet;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * @version $Revision: 1.34 $, $Date: 2006/06/08 15:54:58 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */
public final class ProtoEquipment extends StorableObject
		implements Characterizable, Namable,
		XmlTransferableObject<XmlProtoEquipment>,
		IdlTransferableObjectExt<IdlProtoEquipment> {
	private static final long serialVersionUID = 7066410483749919904L;

	private Identifier typeId;

	private String name;
	private String description;
	private String manufacturer;
	private String manufacturerCode;

	public ProtoEquipment(final IdlProtoEquipment et) throws CreateObjectException {
		try {
			this.fromIdlTransferable(et);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
	}

	ProtoEquipment(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier typeId,
			final String name,
			final String description,
			final String manufacturer,
			final String manufacturerCode) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version);
		this.typeId = typeId;
		this.name = name;
		this.description = description;
		this.manufacturer = manufacturer;
		this.manufacturerCode = manufacturerCode;
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 * 
	 * @param id
	 * @param importType
	 * @param created
	 * @param creatorId
	 * @throws IdentifierGenerationException
	 */
	private ProtoEquipment(final XmlIdentifier id, final String importType, final Date created, final Identifier creatorId)
			throws IdentifierGenerationException {
		super(id, importType, PROTOEQUIPMENT_CODE, created, creatorId);
	}

	/**
	 * Create new instance for client
	 * 
	 * @param creatorId
	 * @param typeId
	 * @param manufacturer
	 * @param manufacturerCode
	 * @return new instance.
	 * @throws CreateObjectException
	 */
	public static ProtoEquipment createInstance(final Identifier creatorId,
			final Identifier typeId,
			final String name,
			final String description,
			final String manufacturer,
			final String manufacturerCode) throws CreateObjectException {
		if (creatorId == null || typeId == null || manufacturer == null || manufacturerCode == null) {
			throw new IllegalArgumentException("Argument is 'null'");
		}

		try {
			final ProtoEquipment protoEquipment = new ProtoEquipment(IdentifierPool.getGeneratedIdentifier(PROTOEQUIPMENT_CODE),
					creatorId,
					INITIAL_VERSION,
					typeId,
					name,
					description,
					manufacturer,
					manufacturerCode);
			assert protoEquipment.isValid() : OBJECT_STATE_ILLEGAL;

			protoEquipment.markAsChanged();

			return protoEquipment;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * Create new instance on import/export from XML
	 * 
	 * @param creatorId
	 * @param xmlProtoEquipment
	 * @param importType
	 * @return new instance
	 * @throws CreateObjectException
	 */
	public static ProtoEquipment createInstance(final Identifier creatorId,
			final XmlProtoEquipment xmlProtoEquipment,
			final String importType) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final XmlIdentifier xmlId = xmlProtoEquipment.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			ProtoEquipment protoEquipment;
			if (id.isVoid()) {
				protoEquipment = new ProtoEquipment(xmlId,
						importType,
						created,
						creatorId);
			} else {
				protoEquipment = StorableObjectPool.getStorableObject(id, true);
				if (protoEquipment == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					protoEquipment = new ProtoEquipment(xmlId,
							importType,
							created,
							creatorId);
				}
			}
			protoEquipment.fromXmlTransferable(xmlProtoEquipment, importType);
			assert protoEquipment.isValid() : OBJECT_BADLY_INITIALIZED;
			protoEquipment.markAsChanged();
			return protoEquipment;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		} catch (final XmlConversionException xce) {
			throw new CreateObjectException(xce);
		}
	}

	public synchronized void fromIdlTransferable(final IdlProtoEquipment pet) throws IdlConversionException {
		super.fromIdlTransferable(pet);
		this.typeId = Identifier.valueOf(pet._typeId);
		this.name = pet.name;
		this.description = pet.description;
		this.manufacturer = pet.manufacturer;
		this.manufacturerCode = pet.manufacturerCode;

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 * @param protoEquipment
	 * @param importType
	 * @throws XmlConversionException
	 */
	@Shitlet
	public void fromXmlTransferable(final XmlProtoEquipment protoEquipment, final String importType)
			throws XmlConversionException {
		try {
			XmlComplementorRegistry.complementStorableObject(protoEquipment, PROTOEQUIPMENT_CODE, importType, PRE_IMPORT);
	
			this.typeId = Identifier.fromXmlTransferable(protoEquipment.getEquipmentTypeId(), importType, MODE_THROW_IF_ABSENT);
	
			this.name = protoEquipment.getName();
			this.description = protoEquipment.isSetDescription()
					? protoEquipment.getDescription()
					: "";
			this.manufacturer = protoEquipment.isSetManufacturer()
					? protoEquipment.getManufacturer()
					: "";
			this.manufacturerCode = protoEquipment.isSetManufacturerCode()
					? protoEquipment.getManufacturerCode()
					: "";
			if (protoEquipment.isSetCharacteristics()) {
				for (final XmlCharacteristic characteristic : protoEquipment.getCharacteristics().getCharacteristicList()) {
					Characteristic.createInstance(super.creatorId, characteristic, importType);
				}
			}
	
			XmlComplementorRegistry.complementStorableObject(protoEquipment, PROTOEQUIPMENT_CODE, importType, POST_IMPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae); 
		}
	}

	/**
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 * @param orb
	 */
	@Override
	public IdlProtoEquipment getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlProtoEquipmentHelper.init(orb, this.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				this.typeId.getIdlTransferable(orb),
				this.name != null ? this.name : "",
				this.description != null ? this.description : "",
				this.manufacturer,
				this.manufacturerCode);
	}

	/**
	 * @param protoEquipment
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	public void getXmlTransferable(final XmlProtoEquipment protoEquipment, final String importType, final boolean usePool)
			throws XmlConversionException {
		try {
			super.id.getXmlTransferable(protoEquipment.addNewId(), importType);

			this.typeId.getXmlTransferable(protoEquipment.addNewEquipmentTypeId(), importType);

			protoEquipment.setName(this.name);
			if (protoEquipment.isSetDescription()) {
				protoEquipment.unsetDescription();
			}
			if (this.description != null && this.description.length() != 0) {
				protoEquipment.setDescription(this.description);
			}
			if (protoEquipment.isSetManufacturer()) {
				protoEquipment.unsetManufacturer();
			}
			if (this.manufacturer != null && this.manufacturer.length() != 0) {
				protoEquipment.setManufacturer(this.manufacturer);
			}
			if (protoEquipment.isSetManufacturerCode()) {
				protoEquipment.unsetManufacturerCode();
			}
			if (this.manufacturerCode != null && this.manufacturerCode.length() != 0) {
				protoEquipment.setManufacturerCode(this.manufacturerCode);
			}
			if (protoEquipment.isSetCharacteristics()) {
				protoEquipment.unsetCharacteristics();
			}
			final Set<Characteristic> characteristics = this.getCharacteristics(false);
			if (!characteristics.isEmpty()) {
				final XmlCharacteristicSeq characteristicSeq = protoEquipment.addNewCharacteristics();
				for (final Characteristic characteristic : characteristics) {
					characteristic.getXmlTransferable(characteristicSeq.addNewCharacteristic(), importType, usePool);
				}
			}
	
			XmlComplementorRegistry.complementStorableObject(protoEquipment, PROTOEQUIPMENT_CODE, importType, EXPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	public Identifier getTypeId() {
		return this.typeId;
	}

	public EquipmentType getType() throws ApplicationException {
		return StorableObjectPool.getStorableObject(this.typeId, true);
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public String getManufacturerCode() {
		return this.manufacturerCode;
	}

	public void setTypeId(final Identifier typeId) {
		this.typeId = typeId;
		super.markAsChanged();
	}

	public void setType(final EquipmentType equipmentType) {
		this.setTypeId(equipmentType.getId());
	}

	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	public void setManufacturer(final String manufacturer) {
		this.manufacturer = manufacturer;
		super.markAsChanged();
	}

	public void setManufacturerCode(final String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
		super.markAsChanged();
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier typeId,
			final String name,
			final String description,
			final String manufacturer,
			final String manufacturerCode) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.typeId = typeId;
		this.name = name;
		this.description = description;
		this.manufacturer = manufacturer;
		this.manufacturerCode = manufacturerCode;
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		final Set<Identifiable> dependencies =  new HashSet<Identifiable>();
		dependencies.add(this.typeId);
		return dependencies;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected ProtoEquipmentWrapper getWrapper() {
		return ProtoEquipmentWrapper.getInstance();
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.StorableObject#getReverseDependencies(boolean)
	 */
	@Override
	protected Set<Identifiable> getReverseDependencies(final boolean usePool) throws ApplicationException {
		final Set<Identifiable> reverseDependencies = new HashSet<Identifiable>();
		reverseDependencies.addAll(super.getReverseDependencies(usePool));
		for (final StorableObject storableObject : this.getCharacteristics0(usePool)) {
			reverseDependencies.addAll(getReverseDependencies(storableObject, usePool));
		}
		reverseDependencies.remove(null);
		reverseDependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(reverseDependencies);
	}

	/*-********************************************************************
	 * Children manipulation: characteristics                             *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<Characteristic> characteristicContainerWrappee;

	/**
	 * @see Characterizable#getCharacteristicContainerWrappee()
	 */
	public StorableObjectContainerWrappee<Characteristic> getCharacteristicContainerWrappee() {
		return (this.characteristicContainerWrappee == null)
				? this.characteristicContainerWrappee = new StorableObjectContainerWrappee<Characteristic>(this, CHARACTERISTIC_CODE)
				: this.characteristicContainerWrappee;
	}

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#addCharacteristic(com.syrus.AMFICOM.general.Characteristic, boolean)
	 */
	public void addCharacteristic(final Characteristic characteristic, final boolean usePool) throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param characteristic
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#removeCharacteristic(com.syrus.AMFICOM.general.Characteristic,
	 *      boolean)
	 */
	public void removeCharacteristic(final Characteristic characteristic, final boolean usePool) throws ApplicationException {
		assert characteristic != null : NON_NULL_EXPECTED;
		assert characteristic.getParentCharacterizableId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		characteristic.setParentCharacterizable(this, usePool);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics(boolean)
	 */
	public Set<Characteristic> getCharacteristics(boolean usePool) throws ApplicationException {
		return Collections.unmodifiableSet(this.getCharacteristics0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<Characteristic> getCharacteristics0(final boolean usePool) throws ApplicationException {
		return this.getCharacteristicContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param characteristics
	 * @param usePool
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set, boolean)
	 */
	public void setCharacteristics(final Set<Characteristic> characteristics, final boolean usePool) throws ApplicationException {
		assert characteristics != null : NON_NULL_EXPECTED;

		final Set<Characteristic> oldCharacteristics = this.getCharacteristics0(usePool);

		final Set<Characteristic> toRemove = new HashSet<Characteristic>(oldCharacteristics);
		toRemove.removeAll(characteristics);
		for (final Characteristic characteristic : toRemove) {
			this.removeCharacteristic(characteristic, usePool);
		}

		final Set<Characteristic> toAdd = new HashSet<Characteristic>(characteristics);
		toAdd.removeAll(oldCharacteristics);
		for (final Characteristic characteristic : toAdd) {
			this.addCharacteristic(characteristic, usePool);
		}
	}
}
