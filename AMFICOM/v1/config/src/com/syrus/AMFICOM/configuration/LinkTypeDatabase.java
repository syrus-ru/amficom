/*
 * $Id: LinkTypeDatabase.java,v 1.27 2005/02/28 11:13:42 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.27 $, $Date: 2005/02/28 11:13:42 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public class LinkTypeDatabase extends StorableObjectDatabase {
	private static final int SIZE_MANUFACTURER_COLUMN = 64;

	private static final int SIZE_MANUFACTURER_CODE_COLUMN = 64;

	private static String columns;
	private static String updateMultiplySQLValues;

	protected String getEnityName() {
		return ObjectEntities.LINKTYPE_ENTITY;
	}

	protected String getUpdateMultiplySQLValues() {
		if (updateMultiplySQLValues == null) {
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION;
		}
		return updateMultiplySQLValues;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA
				+ StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ LinkTypeWrapper.COLUMN_SORT + COMMA
				+ LinkTypeWrapper.COLUMN_MANUFACTURER + COMMA
				+ LinkTypeWrapper.COLUMN_MANUFACTURER_CODE + COMMA
				+ LinkTypeWrapper.COLUMN_IMAGE_ID;
		}
		return columns;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
		LinkType linkType = this.fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(linkType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(linkType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE 
			+ APOSTOPHE + DatabaseString.toQuerySubString(linkType.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ linkType.getSort().value() + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(linkType.getManufacturer(), SIZE_MANUFACTURER_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(linkType.getManufacturerCode(), SIZE_MANUFACTURER_CODE_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(linkType.getImageId());
		return sql;
	}

	private LinkType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof LinkType)
			return (LinkType)storableObject;
		throw new IllegalDataException("LinkTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		LinkType linkType = this.fromStorableObject(storableObject);
		super.retrieveEntity(linkType);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		linkType.setCharacteristics(characteristicDatabase.retrieveCharacteristics(linkType.getId(), CharacteristicSort.CHARACTERISTIC_SORT_LINKTYPE));
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, SQLException {
		LinkType linkType = this.fromStorableObject(storableObject);
		int i;
		i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		preparedStatement.setString( ++i, linkType.getCodename());
		preparedStatement.setString( ++i, linkType.getDescription());
		preparedStatement.setString( ++i, linkType.getName());
		preparedStatement.setInt( ++i, linkType.getSort().value());
		preparedStatement.setString( ++i, linkType.getManufacturer());
		preparedStatement.setString( ++i, linkType.getManufacturerCode());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, linkType.getImageId());
		return i;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		LinkType linkType = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (linkType == null) {
			linkType = new LinkType(DatabaseIdentifier.getIdentifier(resultSet,StorableObjectWrapper.COLUMN_ID),
												null,
												0L,
												null,
												null,
												null,
												0,
												null,
												null,
												null);			
		}
		linkType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
									DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),									
									DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
									DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
									resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
									DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
									DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
									DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
									resultSet.getInt(LinkTypeWrapper.COLUMN_SORT),
									DatabaseString.fromQuerySubString(resultSet.getString(LinkTypeWrapper.COLUMN_MANUFACTURER)),
									DatabaseString.fromQuerySubString(resultSet.getString(LinkTypeWrapper.COLUMN_MANUFACTURER_CODE)),									
									DatabaseIdentifier.getIdentifier(resultSet, LinkTypeWrapper.COLUMN_IMAGE_ID));

		return linkType;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		LinkType linkType = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		LinkType linkType = this.fromStorableObject(storableObject);
		super.insertEntity(linkType);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		try {
			characteristicDatabase.updateCharacteristics(storableObject);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException("LinkTypeDatabase.insert | UpdateObjectException " + e);
		}
	}

	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		try {
			characteristicDatabase.updateCharacteristics(storableObjects);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException("LinkTypeDatabase.insert | UpdateObjectException " + e);
		}
	}
	
	public Collection retrieveAll() throws RetrieveObjectException {
		Collection objects = null;
		try {
			objects = this.retrieveByIds(null, null);
		}
		catch (IllegalDataException ide) {
			Log.debugMessage("LinkTypeDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
			throw new RetrieveObjectException(ide);
		}
		return objects;
	}

	public Collection retrieveByIds(Collection ids, String condition) throws IllegalDataException, RetrieveObjectException {
		Collection objects = null;
		if ((ids == null) || (ids.isEmpty()))
			objects = this.retrieveByIdsOneQuery(null, condition);
		else
			objects = this.retrieveByIdsOneQuery(ids, condition);

		if (objects != null) {
			CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
			Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(objects, CharacteristicSort.CHARACTERISTIC_SORT_LINKTYPE);
			if (characteristicMap != null)
				for (Iterator iter = objects.iterator(); iter.hasNext();) {
					LinkType linkType = (LinkType) iter.next();
					List characteristics = (List) characteristicMap.get(linkType.getId());
					linkType.setCharacteristics0(characteristics);
				}
		}
		return objects;
	}
}
