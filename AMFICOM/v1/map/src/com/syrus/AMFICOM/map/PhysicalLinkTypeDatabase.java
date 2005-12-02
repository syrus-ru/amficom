/*
 * $Id: PhysicalLinkTypeDatabase.java,v 1.36 2005/12/02 11:24:13 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_TYPE_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.map.corba.IdlPhysicalLinkTypePackage.PhysicalLinkTypeSort;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.36 $, $Date: 2005/12/02 11:24:13 $
 * @author $Author: bass $
 * @module map
 */
public final class PhysicalLinkTypeDatabase extends StorableObjectDatabase<PhysicalLinkType> {
	private static String columns;
	
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return PHYSICALLINK_TYPE_CODE;
	}	
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = PhysicalLinkTypeWrapper.COLUMN_SORT + COMMA
				+ StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ PhysicalLinkTypeWrapper.COLUMN_DIMENSION_X + COMMA
				+ PhysicalLinkTypeWrapper.COLUMN_DIMENSION_Y + COMMA
				+ PhysicalLinkTypeWrapper.COLUMN_TOPOLOGICAL + COMMA
				+ PhysicalLinkTypeWrapper.COLUMN_MAP_LIBRARY_ID;
				
		}
		return columns;
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
				+ QUESTION + COMMA				
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}
	
	
	@Override
	protected int setEntityForPreparedStatementTmpl(final PhysicalLinkType storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		preparedStatement.setInt(++startParameterNumber, storableObject.getSort().value());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setInt(++startParameterNumber, storableObject.getBindingDimension().getWidth());
		preparedStatement.setInt(++startParameterNumber, storableObject.getBindingDimension().getHeight());
		preparedStatement.setInt(++startParameterNumber, storableObject.isTopological() ? 1 : 0);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getMapLibraryId());
		return startParameterNumber;
	}
	
	@Override
	protected String getUpdateSingleSQLValuesTmpl(final PhysicalLinkType storableObject) throws IllegalDataException {
		final String values = storableObject.getSort().value() + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ storableObject.getBindingDimension().getWidth() + COMMA
				+ storableObject.getBindingDimension().getHeight() + COMMA
				+ (storableObject.isTopological() ? "1" : "0") + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getMapLibraryId());
				
		return values;
	}
	
	@Override
	protected PhysicalLinkType updateEntityFromResultSet(final PhysicalLinkType storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				SQLException {
		final PhysicalLinkType physicalLinkType = (storableObject == null)
				? new PhysicalLinkType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						null,
						false,
						null)
					: storableObject;
		physicalLinkType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				PhysicalLinkTypeSort.from_int(resultSet.getInt(PhysicalLinkTypeWrapper.COLUMN_SORT)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				resultSet.getInt(PhysicalLinkTypeWrapper.COLUMN_DIMENSION_X),
				resultSet.getInt(PhysicalLinkTypeWrapper.COLUMN_DIMENSION_Y),
				resultSet.getInt(PhysicalLinkTypeWrapper.COLUMN_TOPOLOGICAL) != 0,
				DatabaseIdentifier.getIdentifier(resultSet, PhysicalLinkTypeWrapper.COLUMN_MAP_LIBRARY_ID));
		return physicalLinkType;
	}

}
