/*
 * $Id: LinkTypeDatabase.java,v 1.31 2005/03/05 21:37:24 arseniy Exp $
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
 * @version $Revision: 1.31 $, $Date: 2005/03/05 21:37:24 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public class LinkTypeDatabase extends CharacterizableDatabase {
	private static final int SIZE_MANUFACTURER_COLUMN = 64;

	private static final int SIZE_MANUFACTURER_CODE_COLUMN = 64;

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
				+ LinkTypeWrapper.COLUMN_KIND + COMMA
				+ LinkTypeWrapper.COLUMN_MANUFACTURER + COMMA
				+ LinkTypeWrapper.COLUMN_MANUFACTURER_CODE + COMMA
				+ LinkTypeWrapper.COLUMN_IMAGE_ID;
		}
		return super.getColumns(mode) + columns;
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
									resultSet.getInt(LinkTypeWrapper.COLUMN_KIND),
									DatabaseString.fromQuerySubString(resultSet.getString(LinkTypeWrapper.COLUMN_MANUFACTURER)),
									DatabaseString.fromQuerySubString(resultSet.getString(LinkTypeWrapper.COLUMN_MANUFACTURER_CODE)),									
									DatabaseIdentifier.getIdentifier(resultSet, LinkTypeWrapper.COLUMN_IMAGE_ID));

		return linkType;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		LinkType linkType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  linkType.getId() + "'; argument: " + arg);
				return null;
		}
	}

}
