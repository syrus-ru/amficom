/*
 * $Id: CharacteristicType.java,v 1.15 2005/04/01 15:21:09 arseniy Exp $
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
 * @version $Revision: 1.15 $, $Date: 2005/04/01 15:21:09 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public class CharacteristicType extends StorableObjectType {
	private static final long serialVersionUID = 6153350736368296076L;

	private int dataType;
	private int sort;

	private StorableObjectDatabase characteristicTypeDatabase;

	public CharacteristicType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristicTypeDatabase = GeneralDatabaseContext.getCharacteristicTypeDatabase();
		try {
			this.characteristicTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public CharacteristicType(CharacteristicType_Transferable ctt) {
		this.characteristicTypeDatabase = GeneralDatabaseContext.getCharacteristicTypeDatabase();
		
		try {
			this.fromTransferable(ctt);
		} catch (CreateObjectException e) {
			Log.debugException(e, Log.WARNING);
		}
	}

	protected CharacteristicType(Identifier id,
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

		this.characteristicTypeDatabase = GeneralDatabaseContext.getCharacteristicTypeDatabase();
	}
	
	protected void fromTransferable(IDLEntity transferable) throws CreateObjectException {
		CharacteristicType_Transferable ctt = (CharacteristicType_Transferable) transferable;
		super.fromTransferable(ctt.header, new String(ctt.codename), new String(ctt.description));
		this.dataType = ctt.data_type.value();
		this.sort = ctt.sort.value();
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
							int dataType,
							CharacteristicTypeSort sort) throws CreateObjectException{
		if (creatorId == null || codename == null || description == null ||
				sort == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			CharacteristicType characteristicType = new CharacteristicType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE),
											creatorId,
											0L,
											codename,
											description,
											dataType,
											sort.value());
			characteristicType.changed = true;
			return characteristicType;
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("CharacteristicType.createInstance | cannot generate identifier ", e);
		}
	}

	public Object getTransferable() {
		return new CharacteristicType_Transferable(super.getHeaderTransferable(),
													 new String(super.codename),
													 (super.description != null) ? (new String(super.description)) : "",
													 DataType.from_int(this.dataType),
													 CharacteristicTypeSort.from_int(this.sort));
	}

	public DataType getDataType() {
		return DataType.from_int(this.dataType);
	}
	
	protected void setDataType0(DataType dataType) {
		this.dataType = dataType.value();
	}

	public CharacteristicTypeSort getSort(){
		return CharacteristicTypeSort.from_int(this.sort);
	}
	
	protected void setSort0(CharacteristicTypeSort sort) {
		this.sort = sort.value();
	}
	
	public void setSort(CharacteristicTypeSort sort) {
		this.setSort(sort);
		this.changed = true;
	}

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

	public Set getDependencies() {
		return Collections.EMPTY_SET;
	}	
}
