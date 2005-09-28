/*-
 * $Id: EquipmentType.java,v 1.98 2005/09/28 11:33:20 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.POST_IMPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.PRE_IMPORT;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlEquipmentType;
import com.syrus.AMFICOM.configuration.corba.IdlEquipmentTypeHelper;
import com.syrus.AMFICOM.configuration.xml.XmlEquipmentType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;

/**
 * @version $Revision: 1.98 $, $Date: 2005/09/28 11:33:20 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */

public final class EquipmentType extends StorableObjectType implements Namable, XmlBeansTransferable<XmlEquipmentType> {
	private static final long serialVersionUID = -728421290623578368L;

	private String name;

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
	private EquipmentType(final XmlIdentifier id, final String importType, final Date created, final Identifier creatorId)
			throws IdentifierGenerationException {
		super(Identifier.fromXmlTransferable(id, importType, EQUIPMENT_TYPE_CODE),
				created,
				created,
				creatorId,
				creatorId,
				StorableObjectVersion.createInitial(),
				null,
				null);
	}

	/**
	 * Create new instance on import/export from XML
	 * @param creatorId
	 * @param importType
	 * @param xmlEquipmentType
	 * @return new instance
	 * @throws CreateObjectException
	 */
	public static EquipmentType createInstance(final Identifier creatorId,
			final String importType,
			final XmlEquipmentType xmlEquipmentType) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final XmlIdentifier xmlId = xmlEquipmentType.getId();
			final Date created = new Date();
			final Identifier id = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);
			EquipmentType equipmentType;
			if (id.isVoid()) {
				equipmentType = new EquipmentType(xmlId, importType, created, creatorId);
			} else {
				equipmentType = StorableObjectPool.getStorableObject(id, true);
				if (equipmentType == null) {
					LocalXmlIdentifierPool.remove(xmlId, importType);
					equipmentType = new EquipmentType(xmlId, importType, created, creatorId);
				}
			}
			equipmentType.fromXmlTransferable(xmlEquipmentType, importType);
			assert equipmentType.isValid() : OBJECT_BADLY_INITIALIZED;
			equipmentType.markAsChanged();
			return equipmentType;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * Create new instance for client
	 * 
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @throws CreateObjectException
	 */
	public static EquipmentType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final String name) throws CreateObjectException {
		if (creatorId == null || codename == null || description == null || name == null) {
			throw new IllegalArgumentException("Argument is 'null'");
		}

		try {
			final EquipmentType equipmentType = new EquipmentType(IdentifierPool.getGeneratedIdentifier(EQUIPMENT_TYPE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					codename,
					description,
					name);

			assert equipmentType.isValid() : OBJECT_BADLY_INITIALIZED;

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
	}

	/**
	 * @param equipmentType
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	@Shitlet
	public void fromXmlTransferable(final XmlEquipmentType equipmentType, final String importType) throws ApplicationException {
		XmlComplementorRegistry.complementStorableObject(equipmentType, EQUIPMENT_TYPE_CODE, importType, PRE_IMPORT);

		this.name = equipmentType.getName();
		this.codename = equipmentType.getCodename();
		this.description = equipmentType.isSetDescription() ? equipmentType.getDescription() : "";

		XmlComplementorRegistry.complementStorableObject(equipmentType, EQUIPMENT_TYPE_CODE, importType, POST_IMPORT);
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
				this.name != null ? this.name : "");
	}

	/**
	 * @param equipmentType
	 * @param importType
	 * @throws ApplicationException
	 */
	@Shitlet
	public void getXmlTransferable(
			final XmlEquipmentType equipmentType,
			final String importType)
	throws ApplicationException {
		this.id.getXmlTransferable(equipmentType.addNewId(), importType);
		equipmentType.setName(this.name);
		equipmentType.setCodename(this.codename);
		if (equipmentType.isSetDescription()) {
			equipmentType.unsetDescription();
		}
		if (this.description.length() != 0) {
			equipmentType.setDescription(this.description);
		}
	}

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
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

	@Override
	public Set<Identifiable> getDependencies() {
		return Collections.emptySet();
	}
}
