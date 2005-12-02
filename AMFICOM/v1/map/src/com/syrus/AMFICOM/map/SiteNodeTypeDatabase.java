/*
 * $Id: SiteNodeTypeDatabase.java,v 1.36 2005/12/02 11:24:13 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_TYPE_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypePackage.SiteNodeTypeSort;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.36 $, $Date: 2005/12/02 11:24:13 $
 * @author $Author: bass $
 * @module map
 */
public final class SiteNodeTypeDatabase extends StorableObjectDatabase<SiteNodeType> {
	private static String columns;

	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return SITENODE_TYPE_CODE;
	}	

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = SiteNodeTypeWrapper.COLUMN_SORT + COMMA
				+ StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ SiteNodeTypeWrapper.COLUMN_IMAGE_ID + COMMA
				+ SiteNodeTypeWrapper.COLUMN_TOPOLOGICAL + COMMA
				+ SiteNodeTypeWrapper.COLUMN_MAP_LIBRARY_ID;
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
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final SiteNodeType storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		preparedStatement.setInt(++startParameterNumber, storableObject.getSort().value());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getImageId());
		preparedStatement.setInt(++startParameterNumber, storableObject.isTopological() ? 1 : 0);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getMapLibraryId());
		return startParameterNumber;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final SiteNodeType storableObject) throws IllegalDataException {
		final String values = storableObject.getSort().value() + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getImageId()) + COMMA
			+ (storableObject.isTopological() ? 1 : 0) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getMapLibraryId());
		return values;
	}

	@Override
	protected SiteNodeType updateEntityFromResultSet(final SiteNodeType storableObject, final ResultSet resultSet)
	throws IllegalDataException, SQLException {
		SiteNodeType siteNodeType = (storableObject == null)
				? new SiteNodeType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
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
		siteNodeType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				SiteNodeTypeSort.from_int(resultSet.getInt(SiteNodeTypeWrapper.COLUMN_SORT)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseIdentifier.getIdentifier(resultSet, SiteNodeTypeWrapper.COLUMN_IMAGE_ID),
				resultSet.getInt(SiteNodeTypeWrapper.COLUMN_TOPOLOGICAL) == 1,
				DatabaseIdentifier.getIdentifier(resultSet, SiteNodeTypeWrapper.COLUMN_MAP_LIBRARY_ID));		
		return siteNodeType;
	}

}
