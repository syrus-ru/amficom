/*
 * $Id: CableLinkTypeDatabase.java,v 1.24 2005/03/05 21:37:24 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.CharacterizableDatabase;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.24 $, $Date: 2005/03/05 21:37:24 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public class CableLinkTypeDatabase extends CharacterizableDatabase {
	private static final int SIZE_MANUFACTURER_COLUMN = 64; 

	private static final int SIZE_MANUFACTURER_CODE_COLUMN = 64;

	private static final String LINK_COLUMN_LINK_TYPE_ID = "link_type_id";

	private static String columns;
	private static String updateMultipleSQLValues;

	protected String getEnityName() {
		return ObjectEntities.LINKTYPE_ENTITY;
	}

	protected String getUpdateMultipleSQLValues() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = super.getUpdateMultipleSQLValues() + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = COMMA
				+ StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ CableLinkTypeWrapper.COLUMN_KIND + COMMA
				+ CableLinkTypeWrapper.COLUMN_MANUFACTURER + COMMA
				+ CableLinkTypeWrapper.COLUMN_MANUFACTURER_CODE + COMMA
				+ CableLinkTypeWrapper.COLUMN_IMAGE_ID;
		}
		return super.getColumns(mode) + columns;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
		CableLinkType cableLinkType = this.fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(cableLinkType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(cableLinkType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE 
			+ APOSTOPHE + DatabaseString.toQuerySubString(cableLinkType.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ cableLinkType.getSort().value() + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(cableLinkType.getManufacturer(), SIZE_MANUFACTURER_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(cableLinkType.getManufacturerCode(), SIZE_MANUFACTURER_CODE_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(cableLinkType.getImageId());
		return sql;
	}

	private CableLinkType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof CableLinkType)
			return (CableLinkType)storableObject;
		throw new IllegalDataException("CableLinkTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, SQLException {
		CableLinkType cableLinkType = this.fromStorableObject(storableObject);
		int i;
		i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		preparedStatement.setString( ++i, cableLinkType.getCodename());
		preparedStatement.setString( ++i, cableLinkType.getDescription());
		preparedStatement.setString( ++i, cableLinkType.getName());
		preparedStatement.setInt( ++i, cableLinkType.getSort().value());
		preparedStatement.setString( ++i, cableLinkType.getManufacturer());
		preparedStatement.setString( ++i, cableLinkType.getManufacturerCode());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, cableLinkType.getImageId());
		return i;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		CableLinkType cableLinkType = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (cableLinkType == null) {
			cableLinkType = new CableLinkType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
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
		cableLinkType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				resultSet.getInt(CableLinkTypeWrapper.COLUMN_KIND),
				DatabaseString.fromQuerySubString(resultSet.getString(CableLinkTypeWrapper.COLUMN_MANUFACTURER)),
				DatabaseString.fromQuerySubString(resultSet.getString(CableLinkTypeWrapper.COLUMN_MANUFACTURER_CODE)),
				DatabaseIdentifier.getIdentifier(resultSet, CableLinkTypeWrapper.COLUMN_IMAGE_ID));

		return cableLinkType;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
      CableLinkType cableLinkType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  cableLinkType.getId() + "'; argument: " + arg);
				return null;
		}
	}

}
