/*
 * $Id: SiteNodeTypeDatabase.java,v 1.28 2005/07/26 11:41:05 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.map;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.28 $, $Date: 2005/07/26 11:41:05 $
 * @author $Author: arseniy $
 * @module map_v1
 */
public final class SiteNodeTypeDatabase extends StorableObjectDatabase {
	private static String columns;

	private static String updateMultipleSQLValues;

	private SiteNodeType fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof SiteNodeType)
			return (SiteNodeType) storableObject;
		throw new IllegalDataException("SiteNodeTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.SITENODE_TYPE_CODE;
	}	

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ SiteNodeTypeWrapper.COLUMN_IMAGE_ID + COMMA
				+ SiteNodeTypeWrapper.COLUMN_TOPOLOGICAL;
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
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final SiteNodeType siteNodeType = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, siteNodeType.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, siteNodeType.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, siteNodeType.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, siteNodeType.getImageId());
		preparedStatement.setInt(++startParameterNumber, siteNodeType.isTopological() ? 1 : 0);
		return startParameterNumber;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final SiteNodeType siteNodeType = fromStorableObject(storableObject);
		final String values = APOSTROPHE + DatabaseString.toQuerySubString(siteNodeType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(siteNodeType.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(siteNodeType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ DatabaseIdentifier.toSQLString(siteNodeType.getImageId()) + COMMA
			+ (siteNodeType.isTopological() ? 1 : 0);
		return values;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
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
						false)
					: fromStorableObject(storableObject);
		siteNodeType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseIdentifier.getIdentifier(resultSet, SiteNodeTypeWrapper.COLUMN_IMAGE_ID),
				resultSet.getInt(SiteNodeTypeWrapper.COLUMN_TOPOLOGICAL) == 1);		
		return siteNodeType;
	}

}
