/*
 * $Id: CableThreadTypeDatabase.java,v 1.45 2005/12/02 11:24:19 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ObjectEntities.CABLETHREAD_TYPE_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.45 $, $Date: 2005/12/02 11:24:19 $
 * @author $Author: bass $
 * @module config
 */

public final class CableThreadTypeDatabase extends StorableObjectDatabase<CableThreadType> {
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected short getEntityCode() {		
		return CABLETHREAD_TYPE_CODE;
	}
	
	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
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
				+ CableThreadTypeWrapper.COLUMN_COLOR + COMMA
				+ CableThreadTypeWrapper.COLUMN_LINK_TYPE_ID + COMMA
				+ CableThreadTypeWrapper.COLUMN_CABLE_LINK_TYPE_ID;
		}
		return columns;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final CableThreadType storableObject) throws IllegalDataException {
		final String sql = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + storableObject.getColor() + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getLinkType().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getCableLinkType().getId());
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final CableThreadType storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		preparedStatement.setString(++startParameterNumber, storableObject.getCodename());
		preparedStatement.setString(++startParameterNumber, storableObject.getDescription());
		preparedStatement.setString(++startParameterNumber, storableObject.getName());
		preparedStatement.setInt(++startParameterNumber, storableObject.getColor());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getLinkType().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getCableLinkType().getId());
		return startParameterNumber;
	}

	@Override
	protected CableThreadType updateEntityFromResultSet(final CableThreadType storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				RetrieveObjectException,
				SQLException {
		CableThreadType cableThreadType = storableObject == null
				? new CableThreadType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						0,
						null,
						null)
				: storableObject;
		
		cableThreadType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				resultSet.getInt(CableThreadTypeWrapper.COLUMN_COLOR),
				DatabaseIdentifier.getIdentifier(resultSet, CableThreadTypeWrapper.COLUMN_LINK_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet,	CableThreadTypeWrapper.COLUMN_CABLE_LINK_TYPE_ID));
		return cableThreadType;
	}

}
