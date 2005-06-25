/*
 * $Id: CharacteristicType.java,v 1.39 2005/06/25 17:07:46 bass Exp $
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
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.IdlCharacteristicType;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.39 $, $Date: 2005/06/25 17:07:46 $
 * @author $Author: bass $
 * @module general_v1
 */

public final class CharacteristicType extends StorableObjectType implements Namable {
	private static final long serialVersionUID = 6153350736368296076L;

	private String name;
	private int dataType;
	private int sort;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	CharacteristicType(final Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		final CharacteristicTypeDatabase database = (CharacteristicTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.CHARACTERISTIC_TYPE_CODE);
		try {
			database.retrieve(this);
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
			final long version,
			final String codename,
			final String description,
			final String name,
			final int dataType,
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
	protected void fromTransferable(final IDLEntity transferable) {
		final IdlCharacteristicType ctt = (IdlCharacteristicType) transferable;
		try {
			super.fromTransferable(ctt.header, ctt.codename, ctt.description);
		} catch (ApplicationException ae) {
			// Never
			Log.errorException(ae);
		}
		this.name = ctt.name;
		this.dataType = ctt.dataType.value();
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
					0L,
					codename,
					description,
					name,
					dataType.value(),
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
		return new IdlCharacteristicType(super.getHeaderTransferable(orb),
				super.codename,
				super.description != null ? super.description : "",
				this.name,
				DataType.from_int(this.dataType),
				CharacteristicTypeSort.from_int(this.sort));
	}

	public DataType getDataType() {
		return DataType.from_int(this.dataType);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setDataType0(final DataType dataType) {
		this.dataType = dataType.value();
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
			final long version,
			final String codename,
			final String description,
			final String name,
			final int dataType,
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
		return Collections.emptySet();
	}	
}
