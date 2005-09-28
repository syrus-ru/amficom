/*-
 * $Id: ProtoEquipment.java,v 1.1 2005/09/28 10:00:14 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_THROW_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.PROTOEQUIPMENT_CODE;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.POST_IMPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.PRE_IMPORT;
import static java.util.logging.Level.SEVERE;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlProtoEquipment;
import com.syrus.AMFICOM.configuration.corba.IdlProtoEquipmentHelper;
import com.syrus.AMFICOM.configuration.xml.XmlProtoEquipment;
import com.syrus.AMFICOM.general.ApplicationException;
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
import com.syrus.AMFICOM.general.TypedObject;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;

/**
 * @version $Revision: 1.1 $, $Date: 2005/09/28 10:00:14 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */
public final class ProtoEquipment extends StorableObject implements Namable, TypedObject<EquipmentType>,
		XmlBeansTransferable<XmlProtoEquipment> {
	private static final long serialVersionUID = 3221583036378568005L;

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
		super(Identifier.fromXmlTransferable(id, importType, PROTOEQUIPMENT_CODE),
				created,
				created,
				creatorId,
				creatorId,
				StorableObjectVersion.createInitial());
	}

	/**
	 * Create new instance for client
	 * 
	 * @param creatorId
	 * @param domainId
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
			Log.debugException(ae, SEVERE);
			throw new CreateObjectException(ae);
		}
	}

	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		final IdlProtoEquipment pet = (IdlProtoEquipment) transferable;
		super.fromTransferable(pet);
		this.type = (EquipmentType) StorableObjectPool.getStorableObject(new Identifier(pet._typeId), true);
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

		this.type = StorableObjectPool.getStorableObject(Identifier.fromXmlTransferable(protoEquipment.getEquipmentTypeId(),
				importType,
				MODE_THROW_IF_ABSENT), true);
		this.name = protoEquipment.getName();
		this.description = protoEquipment.isSetDescription() ? protoEquipment.getDescription() : "";
		this.manufacturer = protoEquipment.isSetManufacturer() ? protoEquipment.getManufacturer() : "";
		this.manufacturerCode = protoEquipment.isSetManufacturerCode() ? protoEquipment.getManufacturerCode() : "";

		XmlComplementorRegistry.complementStorableObject(protoEquipment, PROTOEQUIPMENT_CODE, importType, POST_IMPORT);
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
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
				this.type.getId().getTransferable(),
				this.name != null ? this.name : "",
				this.description != null ? this.description : "",
				this.manufacturer,
				this.manufacturerCode);
	}

	public void getXmlTransferable(final XmlProtoEquipment protoEquipment, final String importType) throws ApplicationException {
		super.id.getXmlTransferable(protoEquipment.addNewId(), importType);
		this.type.getId().getXmlTransferable(protoEquipment.addNewEquipmentTypeId(), importType);
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

		final Set<Identifiable> dependencies =  new HashSet<Identifiable>();
		dependencies.add(this.type);
		return dependencies;
	}

}
