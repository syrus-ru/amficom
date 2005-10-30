/*
 * $Id: CharacteristicType.java,v 1.58 2005/10/30 15:20:43 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.Identifier.XmlConversionMode.MODE_RETURN_VOID_IF_ABSENT;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlCharacteristicType;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypeHelper;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.xml.XmlCharacteristicType;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.58 $, $Date: 2005/10/30 15:20:43 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */

public final class CharacteristicType
		extends StorableObjectType<CharacteristicType>
		implements Namable, XmlBeansTransferable<XmlCharacteristicType> {
	private static final long serialVersionUID = 6153350736368296076L;

	private String name;
	private DataType dataType;
	private int sort;

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
			final int sort) {
		super(id,
				new Date(System.currentTimeMillis()),
				new Date(System.currentTimeMillis()),
				creatorId,
				creatorId,
				version,
				codename,
				description);
		this.name = name;
		this.dataType = dataType;
		this.sort = sort;
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
			assert Log.errorMessage(ae);
		}
		this.name = ctt.name;
		this.dataType = DataType.fromTransferable(ctt.dataType);
		this.sort = ctt.sort.value();
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
	}

	/**
	 * @param characteristicType
	 * @param importType
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	public void fromXmlTransferable(
			final XmlCharacteristicType characteristicType,
			final String importType)
	throws ApplicationException {
		throw new UnsupportedOperationException();
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
			final CharacteristicTypeSort sort) throws CreateObjectException {
		try {
			CharacteristicType characteristicType = new CharacteristicType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CHARACTERISTIC_TYPE_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					codename,
					description,
					name,
					dataType,
					sort.value());

			assert characteristicType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

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
						assert Log.debugMessage("WARNING: expected counterpart ("
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
							assert Log.debugMessage("WARNING: "
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
						assert Log.debugMessage("WARNING: expected counterpart ("
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
			assert Log.debugMessage(ae, SEVERE);
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	public IdlCharacteristicType getTransferable(final ORB orb) {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL + ", id: '" + this.id + "'";
		return IdlCharacteristicTypeHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				super.codename,
				super.description != null ? super.description : "",
				this.name,
				this.dataType.getTransferable(orb),
				CharacteristicTypeSort.from_int(this.sort));
	}

	/**
	 * @param characteristicType
	 * @param importType
	 * @param usePool
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#getXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String, boolean)
	 */
	public void getXmlTransferable(
			final XmlCharacteristicType characteristicType,
			final String importType,
			final boolean usePool)
	throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public DataType getDataType() {
		return this.dataType;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setDataType0(final DataType dataType) {
		this.dataType = dataType;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}

	public CharacteristicTypeSort getSort() {
		return CharacteristicTypeSort.from_int(this.sort);
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setSort0(final CharacteristicTypeSort sort) {
		this.sort = sort.value();
	}
	
	public void setSort(final CharacteristicTypeSort sort) {
		this.setSort(sort);
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
			final int sort) {
		super.setAttributes(created, modified, creatorId, modifierId, version, codename, description);
		this.name = name;
		this.dataType = dataType;
		this.sort = sort;
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
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
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
