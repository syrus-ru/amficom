/*
 * $Id: CableLinkTypeDatabase.java,v 1.44 2005/12/02 11:24:18 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_TYPE_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.44 $, $Date: 2005/12/02 11:24:18 $
 * @author $Author: bass $
 * @module config
 */
public final class CableLinkTypeDatabase extends StorableObjectDatabase<CableLinkType> {
	private static final int SIZE_MANUFACTURER_COLUMN = 64;

	private static final int SIZE_MANUFACTURER_CODE_COLUMN = 64;

	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return CABLELINK_TYPE_CODE;
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
		Log.debugMessage("conditionCode is " + conditionCode + ", self is " + CABLELINK_TYPE_CODE, Level.FINEST);
		return CABLELINK_TYPE_CODE == conditionCode;		
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final CableLinkType storableObject) throws IllegalDataException {
		final String sql = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ storableObject.getSort().value() + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getManufacturer(), SIZE_MANUFACTURER_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getManufacturerCode(), SIZE_MANUFACTURER_CODE_COLUMN) + APOSTROPHE + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getImageId());
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final CableLinkType storableObject, final PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		preparedStatement.setString( ++startParameterNumber, storableObject.getCodename());
		preparedStatement.setString( ++startParameterNumber, storableObject.getDescription());
		preparedStatement.setString( ++startParameterNumber, storableObject.getName());
		preparedStatement.setInt( ++startParameterNumber, storableObject.getSort().value());
		preparedStatement.setString( ++startParameterNumber, storableObject.getManufacturer());
		preparedStatement.setString( ++startParameterNumber, storableObject.getManufacturerCode());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getImageId());
		return startParameterNumber;
	}

	@Override
	protected CableLinkType updateEntityFromResultSet(final CableLinkType storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		CableLinkType cableLinkType = storableObject == null
				? new CableLinkType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						0,
						null,
						null,
						null)
				: storableObject;
		cableLinkType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
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
