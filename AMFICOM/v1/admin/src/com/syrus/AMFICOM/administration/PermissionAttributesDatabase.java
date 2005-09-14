/*
 * $Id: PermissionAttributesDatabase.java,v 1.3 2005/09/14 19:01:23 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.PermissionAttributesWrapper.COLUMN_PERMISSION_MASK;
import static com.syrus.AMFICOM.administration.PermissionAttributesWrapper.COLUMN_USER_ID;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;


/**
 * @version $Revision: 1.3 $, $Date: 2005/09/14 19:01:23 $
 * @author $Author: arseniy $
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
			+ COLUMN_PERMISSION_MASK;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final PermissionAttributes storableObject) throws IllegalDataException {
		final String sql = DatabaseIdentifier.toSQLString(storableObject.getDomainId()) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getUserId()) + COMMA
			+ storableObject.getPermissionMask();
		return sql;
	}

	@Override
	protected PermissionAttributes updateEntityFromResultSet(final PermissionAttributes storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		final PermissionAttributes permissionAttributes = (storableObject == null) ? new PermissionAttributes(DatabaseIdentifier.getIdentifier(resultSet,
				StorableObjectWrapper.COLUMN_ID), null, StorableObjectVersion.ILLEGAL_VERSION, null, null, null) : storableObject;
		permissionAttributes.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_USER_ID),
				// store without DatabaseString.fromQuerySubString because of contains only numbers
				new BigInteger(resultSet.getString(COLUMN_PERMISSION_MASK)));
		return permissionAttributes;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final PermissionAttributes storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getDomainId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getUserId());
		preparedStatement.setString(++startParameterNumber, storableObject.getPermissionMask().toString());
		return startParameterNumber;
	}

}
