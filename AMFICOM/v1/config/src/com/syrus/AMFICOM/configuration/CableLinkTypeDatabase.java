/*
 * $Id: CableLinkTypeDatabase.java,v 1.37 2005/07/24 17:38:08 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

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
 * @version $Revision: 1.37 $, $Date: 2005/07/24 17:38:08 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public final class CableLinkTypeDatabase extends StorableObjectDatabase {
	private static final int SIZE_MANUFACTURER_COLUMN = 64;

	private static final int SIZE_MANUFACTURER_CODE_COLUMN = 64;

	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.CABLELINK_TYPE_CODE;
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
				+ CableLinkTypeWrapper.COLUMN_KIND + COMMA
				+ CableLinkTypeWrapper.COLUMN_MANUFACTURER + COMMA
				+ CableLinkTypeWrapper.COLUMN_MANUFACTURER_CODE + COMMA
				+ CableLinkTypeWrapper.COLUMN_IMAGE_ID;
		}
		return columns;
	}
	
	protected boolean checkEntity(final short conditionCode) {
		Log.debugMessage("CableLinkTypeDatabase.checkEntity | conditionCode is " + conditionCode + ", self is " + ObjectEntities.CABLELINK_TYPE_CODE, Level.FINEST);
		return ObjectEntities.CABLELINK_TYPE_CODE == conditionCode;		
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final CableLinkType cableLinkType = this.fromStorableObject(storableObject);
		final String sql = APOSTROPHE + DatabaseString.toQuerySubString(cableLinkType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(cableLinkType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(cableLinkType.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ cableLinkType.getSort().value() + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(cableLinkType.getManufacturer(), SIZE_MANUFACTURER_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(cableLinkType.getManufacturerCode(), SIZE_MANUFACTURER_CODE_COLUMN) + APOSTROPHE + COMMA
			+ DatabaseIdentifier.toSQLString(cableLinkType.getImageId());
		return sql;
	}

	private CableLinkType fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof CableLinkType)
			return (CableLinkType)storableObject;
		throw new IllegalDataException("CableLinkTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject, final PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		final CableLinkType cableLinkType = this.fromStorableObject(storableObject);
		preparedStatement.setString( ++startParameterNumber, cableLinkType.getCodename());
		preparedStatement.setString( ++startParameterNumber, cableLinkType.getDescription());
		preparedStatement.setString( ++startParameterNumber, cableLinkType.getName());
		preparedStatement.setInt( ++startParameterNumber, cableLinkType.getSort().value());
		preparedStatement.setString( ++startParameterNumber, cableLinkType.getManufacturer());
		preparedStatement.setString( ++startParameterNumber, cableLinkType.getManufacturerCode());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, cableLinkType.getImageId());
		return startParameterNumber;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
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

}
