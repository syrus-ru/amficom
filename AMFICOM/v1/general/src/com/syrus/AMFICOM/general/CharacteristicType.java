/*-
 * $Id: CharacteristicType.java,v 1.69.2.1 2006/02/28 15:20:02 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_TYPE_CODE;
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

import com.syrus.AMFICOM.general.corba.IdlCharacteristicType;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypeHelper;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.xml.XmlCharacteristicType;
import com.syrus.AMFICOM.general.xml.XmlCharacteristicTypeSort;
import com.syrus.AMFICOM.general.xml.XmlDataType;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Log;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * @version $Revision: 1.69.2.1 $, $Date: 2006/02/28 15:20:02 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */

public final class CharacteristicType
		extends StorableObjectType<CharacteristicType>
		implements Namable, XmlTransferableObject<XmlCharacteristicType> {
	private static final long serialVersionUID = 6153350736368296076L;

	private String name;
	private final DataType.Proxy dataType = new DataType.Proxy();
	private final CharacteristicTypeSort.Proxy sort = new CharacteristicTypeSort.Proxy();

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public CharacteristicType(final IdlCharacteristicType ctt) {
		this.fromTransferable(ctt);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	CharacteristicType(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final String name,
			final DataType dataType,
			final CharacteristicTypeSort sort) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);
		this.name = name;
		this.dataType.setValue(dataType);
		this.sort.setValue(sort);
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
	private CharacteristicType(final XmlIdentifier id,
			final String importType,
			final Date created,
			final Identifier creatorId)
	throws IdentifierGenerationException {
		super(id, importType, CHARACTERISTIC_TYPE_CODE, created, creatorId);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) {
		final IdlCharacteristicType ctt = (IdlCharacteristicType) transferable;
		try {
			super.fromTransferable(ctt, ctt.codename, ctt.description);
		} catch (ApplicationException ae) {
			// Never
			Log.errorMessage(ae);
		}
		this.name = ctt.name;
		this.dataType.fromIdlTransferable(ctt.dataType);
		this.sort.fromIdlTransferable(ctt.sort);
		
		assert this.isValid() : OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @param characteristicType
	 * @param importType
	 * @throws XmlConversionException
	 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 */
	public void fromXmlTransferable(
			final XmlCharacteristicType characteristicType,
			final String importType)
	throws XmlConversionException {
		try {
			XmlComplementorRegistry.complementStorableObject(characteristicType, CHARACTERISTIC_TYPE_CODE, importType, PRE_IMPORT);
	
			this.codename = characteristicType.getCodename();
			this.description = characteristicType.isSetDescription()
					? characteristicType.getDescription()
					: "";
			this.name = characteristicType.getName();
			this.dataType.fromXmlTransferable(characteristicType.xgetDataType(), importType);
			this.sort.fromXmlTransferable(characteristicType.xgetSort(), importType);
	
			XmlComplementorRegistry.complementStorableObject(characteristicType, CHARACTERISTIC_TYPE_CODE, importType, POST_IMPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	/**
	 * create new instance for client
	 *
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param dataType
	 *            see {@link DataType}
	 * @throws CreateObjectException
	 */
	public static CharacteristicType createInstance(final Identifier creatorId,
			final String codename,
			final String description,
			final String name,
			final DataType dataType,
			final CharacteristicTypeSort sort)
	throws CreateObjectException {
		try {
			CharacteristicType characteristicType = new CharacteristicType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CHARACTERISTIC_TYPE_CODE),
					creatorId,
					INITIAL_VERSION,
					codename,
					description,
					name,
					dataType,
					sort);

			assert characteristicType.isValid() : OBJECT_STATE_ILLEGAL;

			characteristicType.markAsChanged();

			return characteristicType;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * @param creatorId
	 * @param xmlCharacteristicType
	 * @param importType
	 * @throws CreateObjectException
	 */
	public static CharacteristicType createInstance(
			final Identifier creatorId,
			final XmlCharacteristicType xmlCharacteristicType,
			final String importType)
	throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;

		try {
			final String newCodename = xmlCharacteristicType.getCodename();
			final Set<CharacteristicType> characteristicTypes = StorableObjectPool.getStorableObjectsByCondition(
					new TypicalCondition(newCodename, OPERATION_EQUALS, CHARACTERISTIC_TYPE_CODE, COLUMN_CODENAME),
					true);

			assert characteristicTypes.size() <= 1;

			final XmlIdentifier xmlId = xmlCharacteristicType.getId();
			final Identifier expectedId = Identifier.fromXmlTransferable(xmlId, importType, MODE_RETURN_VOID_IF_ABSENT);

			CharacteristicType characteristicType;
			if (characteristicTypes.isEmpty()) {
				/*
				 * No objects found with the specified codename.
				 * Continue normally.
				 */
				final Date created = new Date();
				if (expectedId.isVoid()) {
					/*
					 * First import.
					 */
					characteristicType = new CharacteristicType(xmlId,
							importType,
							created,
							creatorId);
				} else {
					characteristicType = StorableObjectPool.getStorableObject(expectedId, true);
					if (characteristicType == null) {
						Log.debugMessage("WARNING: expected counterpart ("
								+ expectedId
								+ ") for XML identifier: " + xmlId.getStringValue()
								+ " and actual one (" + VOID_IDENTIFIER
								+ ") do not match; expected one will be deleted",
								WARNING);
						LocalXmlIdentifierPool.remove(xmlId, importType);
						characteristicType = new CharacteristicType(xmlId,
								importType,
								created,
								creatorId);
					} else {
						final String oldCodename = characteristicType.getCodename();
						if (!oldCodename.equals(newCodename)) {
							Log.debugMessage("WARNING: "
									+ expectedId + " will change its codename from ``"
									+ oldCodename + "'' to ``"
									+ newCodename + "''",
									WARNING);
						}
					}
				}
			} else {
				characteristicType = characteristicTypes.iterator().next();
				if (expectedId.isVoid()) {
					/*
					 * First import.
					 */
					characteristicType.insertXmlMapping(xmlId, importType);
				} else {
					final Identifier actualId = characteristicType.getId();
					if (!actualId.equals(expectedId)) {
						/*
						 * Arghhh, no match.
						 */
						Log.debugMessage("WARNING: expected counterpart ("
								+ expectedId
								+ ") for XML identifier: " + xmlId.getStringValue()
								+ " and actual one (" + actualId
								+ ") do not match; expected one will be deleted",
								WARNING);
						LocalXmlIdentifierPool.remove(xmlId, importType);
						characteristicType.insertXmlMapping(xmlId, importType);
					}
				}
			}
			characteristicType.fromXmlTransferable(xmlCharacteristicType, importType);
			assert characteristicType.isValid() : OBJECT_BADLY_INITIALIZED;
			characteristicType.markAsChanged();
			return characteristicType;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		} catch (final XmlConversionException xce) {
			throw new CreateObjectException(xce);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlCharacteristicType getIdlTransferable(final ORB orb) {
		assert this.isValid() : OBJECT_STATE_ILLEGAL + ", id: '" + this.id + "'";

		return IdlCharacteristicTypeHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				super.codename,
				super.description != null ? super.description : "",
				this.name,
				this.dataType.getIdlTransferable(orb),
				this.sort.getIdlTransferable(orb));
	}

	/**
	 * @param characteristicType
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	public void getXmlTransferable(
			final XmlCharacteristicType characteristicType,
			final String importType,
			final boolean usePool)
	throws XmlConversionException {
		try {
			this.id.getXmlTransferable(characteristicType.addNewId(), importType);

			characteristicType.setCodename(this.codename);

			if (characteristicType.isSetDescription()) {
				characteristicType.unsetDescription();
			}
			if (this.description.length() != 0) {
				characteristicType.setDescription(this.description);
			}

			final XmlDataType xmlDataType = XmlDataType.Factory.newInstance();
			this.dataType.getXmlTransferable(xmlDataType, importType, usePool);
			characteristicType.xsetDataType(xmlDataType);

			final XmlCharacteristicTypeSort xmlCharacteristicTypeSort = XmlCharacteristicTypeSort.Factory.newInstance();
			this.sort.getXmlTransferable(xmlCharacteristicTypeSort, importType, usePool);
			characteristicType.xsetSort(xmlCharacteristicTypeSort);

			XmlComplementorRegistry.complementStorableObject(characteristicType, CHARACTERISTIC_TYPE_CODE, importType, EXPORT);
		} catch (final ApplicationException ae) {
			throw new XmlConversionException(ae);
		}
	}

	public DataType getDataType() {
		return this.dataType.getValue();
	}

	private void setDataType0(final DataType dataType) {
		this.dataType.setValue(dataType);
	}

	/**
	 * <em>As long as</em> client is allowed to set {@code dataType}
	 * property via the corresponding wrapper, this modifier method should
	 * also remain public.
	 *
	 * @param dataType
	 */
	public void setDataType(final DataType dataType) {
		this.setDataType0(dataType);
		this.markAsChanged();
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	public CharacteristicTypeSort getSort() {
		return this.sort.getValue();
	}

	private void setSort0(final CharacteristicTypeSort sort) {
		this.sort.setValue(sort);
	}
	
	/**
	 * <em>As long as</em> client is allowed to set {@code sort} property
	 * via the corresponding wrapper, this modifier method should also
	 * remain public.
	 *
	 * @param sort
	 */
	public void setSort(final CharacteristicTypeSort sort) {
		this.setSort0(sort);
		super.markAsChanged();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String codename,
			final String description,
			final String name,
			final DataType dataType,
			final CharacteristicTypeSort sort) {
		super.setAttributes(created, modified, creatorId, modifierId, version, codename, description);
		this.name = name;
		this.dataType.setValue(dataType);
		this.sort.setValue(sort);
	}

	@Override
	protected boolean isValid() {
		return super.isValid()
				&& this.name != null && this.name.length() != 0;
	}
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected Set<Identifiable> getDependenciesTmpl() {
		return Collections.emptySet();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected CharacteristicTypeWrapper getWrapper() {
		return CharacteristicTypeWrapper.getInstance();
	}
}
