/*
 * $Id: CharacteristicType.java,v 1.31 2005/06/06 12:22:32 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.AMFICOM.general.corba.CharacteristicType_TransferablePackage.CharacteristicTypeSort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.31 $, $Date: 2005/06/06 12:22:32 $
 * @author $Author: arseniy $
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

		CharacteristicTypeDatabase database = (CharacteristicTypeDatabase) DatabaseContext.getDatabase(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE);
		try {
			database.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public CharacteristicType(final CharacteristicType_Transferable ctt) {
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
	protected void fromTransferable(final IDLEntity transferable) {
		CharacteristicType_Transferable ctt = (CharacteristicType_Transferable) transferable;
		try {
			super.fromTransferable(ctt.header, ctt.codename, ctt.description);
		}
		catch (ApplicationException ae) {
			// Never
			Log.errorException(ae);
		}
		this.name = ctt.name;
		this.dataType = ctt.data_type.value();
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
			CharacteristicType characteristicType = new CharacteristicType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE),
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
		}
		catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public IDLEntity getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL + ", id: '" + this.id + "'";
		return new CharacteristicType_Transferable(super.getHeaderTransferable(),
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

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public Set getDependencies() {
		return Collections.EMPTY_SET;
	}	
}
