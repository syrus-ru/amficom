/*
 * $Id: CharacteristicType.java,v 1.3 2005/01/20 07:32:38 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.corba.DataType;

/**
 * @version $Revision: 1.3 $, $Date: 2005/01/20 07:32:38 $
 * @author $Author: stas $
 * @module general_v1
 */

public class CharacteristicType extends StorableObjectType {
	private static final long serialVersionUID = 6153350736368296076L;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_DATA_TYPE = "type";
	public static final String COLUMN_SORT = "sort";
	private static Map exportColumns = null;

	private int dataType;
	private int sort;

	private StorableObjectDatabase characteristicTypeDatabase;

	public CharacteristicType(Identifier id) throws ObjectNotFoundException, RetrieveObjectException {
		super(id);

		this.characteristicTypeDatabase = GeneralDatabaseContext.characteristicTypeDatabase;
		try {
			this.characteristicTypeDatabase.retrieve(this);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

	public CharacteristicType(CharacteristicType_Transferable ctt) {
		super(ctt.header,
				new String(ctt.codename),
				new String(ctt.description));
		this.dataType = ctt.data_type.value();
		this.sort = ctt.sort.value();

		this.characteristicTypeDatabase = GeneralDatabaseContext.characteristicTypeDatabase;
	}

	protected CharacteristicType(Identifier id,
							Identifier creatorId,
							String codename,
							String description,
							int dataType,
							int sort){
					super(id,
							new Date(System.currentTimeMillis()),
							new Date(System.currentTimeMillis()),
							creatorId,
							creatorId,
							codename,
							description);
		this.dataType = dataType;
		this.sort = sort;

		super.currentVersion = super.getNextVersion();

		this.characteristicTypeDatabase = GeneralDatabaseContext.characteristicTypeDatabase;
	}

	/**
	 * create new instance for client
	 * @param creatorId
	 * @param codename
	 * @param description
	 * @param dataType see {@link DataType}
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
			return new CharacteristicType(IdentifierPool.getGeneratedIdentifier(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE),
											creatorId,
											codename,
											description,
											dataType,
											sort.value());
		}
		catch (IllegalObjectEntityException e) {
			throw new CreateObjectException("CharacteristicType.createInstance | cannot generate identifier ", e);
		}
	}

	public void insert() throws CreateObjectException {
		try {
			if (this.characteristicTypeDatabase != null)
				this.characteristicTypeDatabase.update(this, StorableObjectDatabase.UPDATE_FORCE, null);
		}
		catch (ApplicationException ae) {
			throw new CreateObjectException(ae.getMessage(), ae);
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

	public CharacteristicTypeSort getSort(){
		return CharacteristicTypeSort.from_int(this.sort);
	}

	protected synchronized void setAttributes(Date created,
																						Date modified,
																						Identifier creatorId,
																						Identifier modifierId,
																						String codename,
																						String description,
																						int dataType,
																						int sort) {
		super.setAttributes(created,
												modified,
												creatorId,
												modifierId,
												codename,
												description);
		this.dataType = dataType;
		this.sort = sort;
	}

	public List getDependencies() {
		return Collections.EMPTY_LIST;
	}

	public Map exportColumns() {
		if (exportColumns == null) {
			exportColumns = new HashMap(4);
		}
		exportColumns.put(COLUMN_ID, getId());
		exportColumns.put(COLUMN_DESCRIPTION, getDescription());
		exportColumns.put(COLUMN_SORT, String.valueOf(getSort().value()));
		exportColumns.put(COLUMN_DATA_TYPE, String.valueOf(getDataType().value()));

		return exportColumns;
	}
}
