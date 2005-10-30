/*-
 * $Id: ProtoEquipment.java,v 1.17 2005/10/30 14:48:45 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PROTOEQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.EXPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.POST_IMPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.PRE_IMPORT;
import static java.util.logging.Level.SEVERE;

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
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlCharacteristic;
import com.syrus.AMFICOM.general.xml.XmlCharacteristicSeq;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;

/**
 * @version $Revision: 1.17 $, $Date: 2005/10/30 14:48:45 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */
public final class ProtoEquipment extends StorableObject<ProtoEquipment>
		implements Characterizable, Namable,
		XmlBeansTransferable<XmlProtoEquipment> {
	private static final long serialVersionUID = 7066410483749919904L;

	private EquipmentType type;

	private String name;
	private String description;
	private String manufacturer;
	private String manufacturerCode;

	public ProtoEquipment(final IdlProtoEquipment et) throws CreateObjectException {
		try {
			this.fromTransferable(et);
		} catch (ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	ProtoEquipment(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final EquipmentType type,
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
		this.type = type;
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
	 * @param type
	 * @param manufacturer
	 * @param manufacturerCode
	 * @return new instance.
	 * @throws CreateObjectException
	 */
	public static ProtoEquipment createInstance(final Identifier creatorId,
			final EquipmentType type,
			final String name,
			final String description,
			final String manufacturer,
			final String manufacturerCode) throws CreateObjectException {
		if (creatorId == null || type == null || manufacturer == null || manufacturerCode == null) {
			throw new IllegalArgumentException("Argument is 'null'");
		}

		try {
			final ProtoEquipment protoEquipment = new ProtoEquipment(IdentifierPool.getGeneratedIdentifier(PROTOEQUIPMENT_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					type,
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
	 * @param creatorId
	 * @param xmlProtoEquipment
	 * @param importType
	 * @return new instance
	 * @throws CreateObjectException
	 */
	public static ProtoEquipment createInstance(final Identifier creatorId,
			final XmlProtoEquipment xmlProtoEquipment,
			final String importType)
	throws CreateObjectException {
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
			Log.debugMessage(ae, SEVERE);
			throw new CreateObjectException(ae);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlProtoEquipment pet = (IdlProtoEquipment) transferable;
		super.fromTransferable(pet);
		this.type = EquipmentType.fromTransferable(pet.type);
		this.name = pet.name;
		this.description = pet.description;
		this.manufacturer = pet.manufacturer;
		this.manufacturerCode = pet.manufacturerCode;
	}

	/**
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 * @param protoEquipment
	 * @param importType
	 * @throws ApplicationException
	 */
	@Shitlet
	public void fromXmlTransferable(final XmlProtoEquipment protoEquipment, final String importType) throws ApplicationException {
		XmlComplementorRegistry.complementStorableObject(protoEquipment, PROTOEQUIPMENT_CODE, importType, PRE_IMPORT);

		this.type = EquipmentType.fromXmlTransferable(protoEquipment.getXmlEquipmentType());

		this.name = protoEquipment.getName();
		this.description = protoEquipment.isSetDescription() ? protoEquipment.getDescription() : "";
		this.manufacturer = protoEquipment.isSetManufacturer() ? protoEquipment.getManufacturer() : "";
		this.manufacturerCode = protoEquipment.isSetManufacturerCode() ? protoEquipment.getManufacturerCode() : "";
		if (protoEquipment.isSetCharacteristics()) {
			for (final XmlCharacteristic characteristic : protoEquipment.getCharacteristics().getCharacteristicArray()) {
				Characteristic.createInstance(super.creatorId, characteristic, importType);
			}
		}

		XmlComplementorRegistry.complementStorableObject(protoEquipment, PROTOEQUIPMENT_CODE, importType, POST_IMPORT);
	}

	/**
	 * @see com.syrus.util.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 * @param orb
	 */
	@Override
	public IdlProtoEquipment getTransferable(final ORB orb) {
		return IdlProtoEquipmentHelper.init(orb, this.id.getTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getTransferable(),
				super.modifierId.getTransferable(),
				super.version.longValue(),
				this.type.getTransferable(orb),
				this.name != null ? this.name : "",
				this.description != null ? this.description : "",
				this.manufacturer,
				this.manufacturerCode);
	}

	/**
	 * @param protoEquipment
	 * @param importType
	 * @param usePool
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#getXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String, boolean)
	 */
	public void getXmlTransferable(final XmlProtoEquipment protoEquipment,
			final String importType,
			final boolean usePool)
	throws ApplicationException {
		super.id.getXmlTransferable(protoEquipment.addNewId(), importType);

		protoEquipment.setXmlEquipmentType(this.type.getXmlTransferable());

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
		if (false && !characteristics.isEmpty()) {
			final XmlCharacteristicSeq characteristicSeq = protoEquipment.addNewCharacteristics();
			for (final Characteristic characteristic : characteristics) {
				characteristic.getXmlTransferable(characteristicSeq.addNewCharacteristic(), importType, usePool);
			}
		}

		XmlComplementorRegistry.complementStorableObject(protoEquipment, PROTOEQUIPMENT_CODE, importType, EXPORT);
	}

	public EquipmentType getType() {
		return this.type;
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

	public void setType(final EquipmentType type) {
		this.type = type;
		super.markAsChanged();
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
			final EquipmentType type,
			final String name,
			final String description,
			final String manufacturer,
			final String manufacturerCode) {
		super.setAttributes(created, modified, creatorId, modifierId, version);
		this.type = type;
		this.name = name;
		this.description = description;
		this.manufacturer = manufacturer;
		this.manufacturerCode = manufacturerCode;
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return Collections.emptySet();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected ProtoEquipmentWrapper getWrapper() {
		return ProtoEquipmentWrapper.getInstance();
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
