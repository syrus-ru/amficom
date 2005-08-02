/*
 * $Id: CharacteristicType.java,v 1.44 2005/08/02 18:07:34 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlDataType;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicType;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypeHelper;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.44 $, $Date: 2005/08/02 18:07:34 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public final class CharacteristicType extends StorableObjectType implements Namable {
	private static final long serialVersionUID = 6153350736368296076L;

	private String name;
	private DataType dataType;
	private int sort;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	CharacteristicType(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		try {
			DatabaseContext.getDatabase(ObjectEntities.CHARACTERISTIC_TYPE_CODE).retrieve(this);
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

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
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	@Override
	protected void fromTransferable(final IdlStorableObject transferable) {
		final IdlCharacteristicType ctt = (IdlCharacteristicType) transferable;
		try {
			super.fromTransferable(ctt, ctt.codename, ctt.description);
		} catch (ApplicationException ae) {
			// Never
			Log.errorException(ae);
		}
		this.name = ctt.name;
		this.dataType = DataType.fromTransferable(ctt.dataType);
		this.sort = ctt.sort.value();
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
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
				(IdlDataType) this.dataType.getTransferable(orb),
				CharacteristicTypeSort.from_int(this.sort));
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
}
