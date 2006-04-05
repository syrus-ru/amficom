/*-
 * $Id: EquipmentType.java,v 1.110.4.5 2006/04/05 09:45:16 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ErrorMessages.METHOD_NOT_NEEDED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.ErrorMessages.ONLY_ONE_EXPECTED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.EQUIPMENT_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.EXPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.POST_IMPORT;
import static com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode.PRE_IMPORT;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.configuration.corba.IdlEquipmentType;
import com.syrus.AMFICOM.configuration.corba.IdlEquipmentTypeHelper;
import com.syrus.AMFICOM.configuration.xml.XmlEquipmentType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Codename;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * @version $Revision: 1.110.4.5 $, $Date: 2006/04/05 09:45:16 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
*/
 /* 	REFLECTOMETER("reflectometer"),
	OPTICAL_SWITCH("optical_switch"),
	MUFF("muff"),
	CABLE_PANEL("cable_panel"),
	TRANSMITTER("transmitter"),
	RECEIVER("receiver"),
	MULTIPLEXOR("multiplexor"),
	CROSS("cross"),
	FILTER("filter"),
	OTHER("other"),
	UNKNOWN("unknown"),
	RACK("rack"),

	@Crutch136(notes = "Stub for SchemeElement without Equipment")
	BUG_136("bug136");

 */
public final class EquipmentType extends StorableObjectType implements Namable, XmlTransferableObject<XmlEquipmentType>, IdlTransferableObjectExt<IdlEquipmentType> {
	private static final long serialVersionUID = 361767579292639873L;

	private static TypicalCondition codenameCondition;
	private static EquivalentCondition equivalentCondition;

	EquipmentType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);
	}

	public EquipmentType(final IdlEquipmentType idlEquipmentType) throws CreateObjectException {
		try {
			this.fromIdlTransferable(idlEquipmentType);
		} catch (final IdlConversionException ice) {
			throw new CreateObjectException(ice);
		}
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
		super(id, importType, EQUIPMENT_TYPE_CODE, created, creatorId);
	}

	public static EquipmentType createInstance(final Identifier creatorId,
			final String codename,
			final String description) throws CreateObjectException {
		if (creatorId == null || codename == null || description == null) {
			throw new IllegalArgumentException(NON_NULL_EXPECTED);
		}

		try {
			final EquipmentType equipmentType = new EquipmentType(IdentifierPool.getGeneratedIdentifier(EQUIPMENT_TYPE_CODE),
					creatorId,
					INITIAL_VERSION,
					codename,
					description);

			assert equipmentType.isValid() : OBJECT_STATE_ILLEGAL;

			equipmentType.markAsChanged();

			return equipmentType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
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
			final String newCodename = xmlEquipmentType.getCodename();
			final Set<EquipmentType> equipmentTypes = StorableObjectPool.getStorableObjectsByCondition(new TypicalCondition(newCodename,
					OPERATION_EQUALS,
					EQUIPMENT_TYPE_CODE,
					COLUMN_CODENAME),
					true);

			assert equipmentTypes.size() <= 1;

			final XmlIdentifier xmlId = xmlEquipmentType.getId();
			final Identifier expectedId = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);

			EquipmentType equipmentType;
			if (equipmentTypes.isEmpty()) {
				/*
				 * No objects found with the specified codename.
				 * Continue normally.
				 */
				final Date created = new Date();
				if (expectedId.isVoid()) {
					/*
					 * First import.
					 */
					equipmentType = new EquipmentType(xmlId, importType, created, creatorId);
				} else {
					equipmentType = StorableObjectPool.getStorableObject(expectedId, true);
					if (equipmentType == null) {
						Log.debugMessage("WARNING: expected counterpart (" + expectedId + ") for XML identifier: " + xmlId.getStringValue()
								+ " and actual one (" + VOID_IDENTIFIER + ") do not match; expected one will be deleted", WARNING);
						LocalXmlIdentifierPool.remove(xmlId, importType);
						equipmentType = new EquipmentType(xmlId, importType, created, creatorId);
					} else {
						final String oldCodename = equipmentType.getCodename();
						if (!oldCodename.equals(newCodename)) {
							Log.debugMessage("WARNING: " + expectedId + " will change its codename from ``"
									+ oldCodename + "'' to ``" + newCodename + "''", WARNING);
						}
					}
				}
			} else {
				equipmentType = equipmentTypes.iterator().next();
				if (expectedId.isVoid()) {
					/*
					 * First import.
					 */
					equipmentType.insertXmlMapping(xmlId, importType);
				} else {
					final Identifier actualId = equipmentType.getId();
					if (!actualId.equals(expectedId)) {
						/*
						 * Arghhh, no match.
						 */
						Log.debugMessage("WARNING: expected counterpart (" + expectedId + ") for XML identifier: " + xmlId.getStringValue()
								+ " and actual one (" + actualId + ") do not match; expected one will be deleted", WARNING);
						LocalXmlIdentifierPool.remove(xmlId, importType);
						equipmentType.insertXmlMapping(xmlId, importType);
					}
				}
			}

			equipmentType.fromXmlTransferable(xmlEquipmentType, importType);
			assert equipmentType.isValid() : OBJECT_BADLY_INITIALIZED;
			equipmentType.markAsChanged();
			return equipmentType;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		} catch (final XmlConversionException xce) {
			throw new CreateObjectException(xce);
		}
	}

	@Override
	public IdlEquipmentType getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		return IdlEquipmentTypeHelper.init(orb,
				super.id.getIdlTransferable(),
				super.created.getTime(),
				super.modified.getTime(),
				super.creatorId.getIdlTransferable(),
				super.modifierId.getIdlTransferable(),
				super.version.longValue(),
				super.codename,
				super.description != null ? super.description : "");
	}

	@Shitlet
	public void getXmlTransferable(final XmlEquipmentType equipmentType, final String importType, final boolean usePool)
			throws XmlConversionException {
		try {
			this.id.getXmlTransferable(equipmentType.addNewId(), importType);
			equipmentType.setCodename(super.codename);
			if (equipmentType.isSetDescription()) {
				equipmentType.unsetDescription();
			}
			if (super.description.length() != 0) {
				equipmentType.setDescription(super.description);
			}

			XmlComplementorRegistry.complementStorableObject(equipmentType, EQUIPMENT_TYPE_CODE, importType, EXPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	public synchronized void fromIdlTransferable(final IdlEquipmentType idlEquipmentType) throws IdlConversionException {
		super.fromIdlTransferable(idlEquipmentType, idlEquipmentType.codename, idlEquipmentType.description);

		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	@Shitlet
	public void fromXmlTransferable(final XmlEquipmentType equipmentType, final String importType) throws XmlConversionException {
		try {
			XmlComplementorRegistry.complementStorableObject(equipmentType, EQUIPMENT_TYPE_CODE, importType, PRE_IMPORT);

			super.codename = equipmentType.getCodename();
			super.description = equipmentType.isSetDescription() ? equipmentType.getDescription() : "";

			XmlComplementorRegistry.complementStorableObject(equipmentType, EQUIPMENT_TYPE_CODE, importType, POST_IMPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	public String getName() {
		return super.getDescription();
	}

	public void setName(final String name) {
		throw new UnsupportedOperationException(METHOD_NOT_NEEDED);
	}

	/**
	 * If add additional fields to class,
	 * remove Override annotation.
	 */
	@Override
	protected synchronized final void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String codename,
			final String description) {
		super.setAttributes(created, modified, creatorId, modifierId, version, codename, description);
	}

	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		return Collections.emptySet();
	}

	@Override
	public EquipmentTypeWrapper getWrapper() {
		return EquipmentTypeWrapper.getInstance();
	}

	public static Identifier idOf(final String codename) throws ApplicationException {
		if (codenameCondition == null) {
			codenameCondition = new TypicalCondition(codename, OPERATION_EQUALS, EQUIPMENT_TYPE_CODE, COLUMN_CODENAME);
		} else {
			codenameCondition.setValue(codename);
		}

		final Set<Identifier> equipmentTypeIds = StorableObjectPool.getIdentifiersByCondition(codenameCondition, true);
		if (equipmentTypeIds.isEmpty()) {
			throw new ObjectNotFoundException("EquipmentType '" + codename + "' not found");
		}

		assert equipmentTypeIds.size() == 1 : ONLY_ONE_EXPECTED;
		return equipmentTypeIds.iterator().next();
	}

	public static EquipmentType valueOf(final Codename codename) throws ApplicationException {
		assert codename != null : NON_NULL_EXPECTED;
		return valueOf(codename.stringValue());
	}

	public static EquipmentType valueOf(final String codename) throws ApplicationException {
		if (codenameCondition == null) {
			codenameCondition = new TypicalCondition(codename, OPERATION_EQUALS, EQUIPMENT_TYPE_CODE, COLUMN_CODENAME);
		} else {
			codenameCondition.setValue(codename);
		}

		final Set<EquipmentType> equipmentTypes = StorableObjectPool.getStorableObjectsByCondition(codenameCondition, true);
		if (equipmentTypes.isEmpty()) {
			throw new ObjectNotFoundException("EquipmentType '" + codename + "' not found");
		}

		assert equipmentTypes.size() == 1 : ONLY_ONE_EXPECTED;
		return equipmentTypes.iterator().next();
	}

	public static Set<EquipmentType> values() throws ApplicationException {
		if (equivalentCondition == null) {
			equivalentCondition = new EquivalentCondition(EQUIPMENT_TYPE_CODE);
		}

		return StorableObjectPool.getStorableObjectsByCondition(equivalentCondition, true);
	}

	public static EquipmentType[] valuesArray() throws ApplicationException {
		final Set<EquipmentType> equipmentTypes = values();
		return equipmentTypes.toArray(new EquipmentType[equipmentTypes.size()]);
	}
}
