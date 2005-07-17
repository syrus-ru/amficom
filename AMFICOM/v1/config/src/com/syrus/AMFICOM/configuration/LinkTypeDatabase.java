/*
 * $Id: LinkTypeDatabase.java,v 1.39 2005/07/17 05:19:00 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.39 $, $Date: 2005/07/17 05:19:00 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public final class LinkTypeDatabase extends StorableObjectDatabase {
	private static final int SIZE_MANUFACTURER_COLUMN = 64;

	private static final int SIZE_MANUFACTURER_CODE_COLUMN = 64;

	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.LINK_TYPE_CODE;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ LinkTypeWrapper.COLUMN_KIND + COMMA
				+ LinkTypeWrapper.COLUMN_MANUFACTURER + COMMA
				+ LinkTypeWrapper.COLUMN_MANUFACTURER_CODE + COMMA
				+ LinkTypeWrapper.COLUMN_IMAGE_ID;
		}
		return columns;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final LinkType linkType = this.fromStorableObject(storableObject);
		final String sql = APOSTROPHE + DatabaseString.toQuerySubString(linkType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(linkType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(linkType.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ linkType.getSort().value() + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(linkType.getManufacturer(), SIZE_MANUFACTURER_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(linkType.getManufacturerCode(), SIZE_MANUFACTURER_CODE_COLUMN) + APOSTROPHE + COMMA
			+ DatabaseIdentifier.toSQLString(linkType.getImageId());
		return sql;
	}

	private LinkType fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof LinkType)
			return (LinkType)storableObject;
		throw new IllegalDataException("LinkTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final LinkType linkType = this.fromStorableObject(storableObject);
		preparedStatement.setString( ++startParameterNumber, linkType.getCodename());
		preparedStatement.setString( ++startParameterNumber, linkType.getDescription());
		preparedStatement.setString( ++startParameterNumber, linkType.getName());
		preparedStatement.setInt( ++startParameterNumber, linkType.getSort().value());
		preparedStatement.setString( ++startParameterNumber, linkType.getManufacturer());
		preparedStatement.setString( ++startParameterNumber, linkType.getManufacturerCode());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, linkType.getImageId());
		return startParameterNumber;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
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

	@Override
	public Object retrieveObject(final StorableObject storableObject, final int retrieveKind, final Object arg) throws IllegalDataException {
		final LinkType linkType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  linkType.getId() + "'; argument: " + arg);
				return null;
		}
	}

}
