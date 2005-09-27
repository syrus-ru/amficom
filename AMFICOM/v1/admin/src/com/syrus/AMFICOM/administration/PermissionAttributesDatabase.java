/*
 * $Id: PermissionAttributesDatabase.java,v 1.4 2005/09/27 14:05:04 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.PermissionAttributesWrapper.COLUMN_MODULE_CODE;
import static com.syrus.AMFICOM.administration.PermissionAttributesWrapper.COLUMN_USER_ID;
import static com.syrus.AMFICOM.administration.PermissionAttributesWrapper.COLUMN_PERMISSION_MASK;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.administration.PermissionAttributes.Module;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;


/**
 * @version $Revision: 1.4 $, $Date: 2005/09/27 14:05:04 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module administration
 */

public final class PermissionAttributesDatabase extends StorableObjectDatabase<PermissionAttributes> {
	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.PERMATTR_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_USER_ID + COMMA
			+ COLUMN_MODULE_CODE + COMMA
			+ COLUMN_PERMISSION_MASK;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final PermissionAttributes permissionAttributes) 
	throws IllegalDataException {
		final String sql = DatabaseIdentifier.toSQLString(permissionAttributes.getDomainId()) + COMMA
			+ DatabaseIdentifier.toSQLString(permissionAttributes.getUserId()) + COMMA
			+ permissionAttributes.getModule().ordinal() + COMMA
			+ permissionAttributes.getPermissions().toString();
		return sql;
	}

	@Override
	protected PermissionAttributes updateEntityFromResultSet(final PermissionAttributes permissionAttributes, final ResultSet resultSet)
	throws IllegalDataException, SQLException {
		final PermissionAttributes attributes = (permissionAttributes == null) ? new PermissionAttributes(DatabaseIdentifier.getIdentifier(resultSet,
				StorableObjectWrapper.COLUMN_ID), null, StorableObjectVersion.ILLEGAL_VERSION, null, null, null, null) : permissionAttributes;
		attributes.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_USER_ID),
				Module.valueOf(resultSet.getInt(COLUMN_MODULE_CODE)),
				// store without DatabaseString.fromQuerySubString because of contains only numbers
				new BigInteger(resultSet.getString(COLUMN_PERMISSION_MASK)));
		return attributes;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final PermissionAttributes permissionAttributes,
			final PreparedStatement preparedStatement,
			int startParameterNumber) 
	throws IllegalDataException, SQLException {
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, permissionAttributes.getDomainId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, permissionAttributes.getUserId());
		preparedStatement.setInt(++startParameterNumber, permissionAttributes.getModule().ordinal());
		preparedStatement.setString(++startParameterNumber, permissionAttributes.getPermissions().toString());
		return startParameterNumber;
	}

}
