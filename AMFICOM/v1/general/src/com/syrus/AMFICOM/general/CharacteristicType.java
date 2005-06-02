/*
 * $Id: CharacteristicType.java,v 1.27 2005/06/02 14:26:37 arseniy Exp $
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

import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.27 $, $Date: 2005/06/02 14:26:37 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public final class CharacteristicType extends StorableObjectType {
	private static final long serialVersionUID = 6153350736368296076L;

	private int dataType;
	private int sort;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	CharacteristicType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
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
	public CharacteristicType(CharacteristicType_Transferable ctt) {
		this.fromTransferable(ctt);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	CharacteristicType(Identifier id,
							Identifier creatorId,
							long version,
							String codename,
							String description,
							int dataType,
							int sort){
					super(id,
							new Date(System.currentTimeMillis()),
							new Date(System.currentTimeMillis()),
							creatorId,
							creatorId,
							version,
							codename,
							description);
		this.dataType = dataType;
		this.sort = sort;
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void fromTransferable(IDLEntity transferable) {
		CharacteristicType_Transferable ctt = (CharacteristicType_Transferable) transferable;
		try {
			super.fromTransferable(ctt.header, ctt.codename, ctt.description);
		}
		catch (ApplicationException ae) {
			// Never
			Log.errorException(ae);
		}
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
	public static CharacteristicType createInstance(Identifier creatorId,
							String codename,
							String description,
							DataType dataType,
							CharacteristicTypeSort sort) throws CreateObjectException{
		try {
			CharacteristicType characteristicType = new CharacteristicType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE),
											creatorId,
											0L,
											codename,
											description,
											dataType.value(),
											sort.value());
			assert characteristicType.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
			characteristicType.changed = true;
			try {
				StorableObjectPool.putStorableObject(characteristicType);
			}
			catch (IllegalObjectEntityException ioee) {
				Log.errorException(ioee);
			}

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
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		return new CharacteristicType_Transferable(super.getHeaderTransferable(),
													 super.codename,
													 super.description != null ? super.description : "",
													 DataType.from_int(this.dataType),
													 CharacteristicTypeSort.from_int(this.sort));
	}	

	public DataType getDataType() {
		return DataType.from_int(this.dataType);
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setDataType0(DataType dataType) {
		this.dataType = dataType.value();
	}

	public CharacteristicTypeSort getSort(){
		return CharacteristicTypeSort.from_int(this.sort);
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected void setSort0(CharacteristicTypeSort sort) {
		this.sort = sort.value();
	}
	
	public void setSort(CharacteristicTypeSort sort) {
		this.setSort(sort);
		this.changed = true;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected synchronized void setAttributes(Date created,
												Date modified,
												Identifier creatorId,
												Identifier modifierId,
												long version,
												String codename,
												String description,
												int dataType,
												int sort) {
		super.setAttributes(created,
							modified,
							creatorId,
							modifierId,
							version,
							codename,
							description);
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
