/*
 * $Id: SystemUserDatabase.java,v 1.22 2006/04/10 16:56:18 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.SystemUserWrapper.COLUMN_LOGIN;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.COLUMN_SORT;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.22 $, $Date: 2006/04/10 16:56:18 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module administration
 */

public final class SystemUserDatabase extends StorableObjectDatabase<SystemUser> {
	static final int SIZE_LOGIN_COLUMN = 32;

	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {
		return SYSTEMUSER_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_LOGIN + COMMA
					+ COLUMN_SORT + COMMA
					+ COLUMN_NAME + COMMA
					+ COLUMN_DESCRIPTION;
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
	protected String getUpdateSingleSQLValuesTmpl(final SystemUser storableObject) throws IllegalDataException {
		return APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getLogin(), SIZE_LOGIN_COLUMN) + APOSTROPHE + COMMA
			+ Integer.toString(storableObject.getSort().value()) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE;
	}

	@Override
	protected SystemUser updateEntityFromResultSet(final SystemUser storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				SQLException {
		final SystemUser user = (storableObject == null)
				? new SystemUser(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						0,
						null,
						null)
					: storableObject;
		user.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(SystemUserWrapper.COLUMN_LOGIN)),
				resultSet.getInt(SystemUserWrapper.COLUMN_SORT),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)));
		return user;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final SystemUser storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getLogin(), SIZE_LOGIN_COLUMN);
		preparedStatement.setInt(++startParameterNumber, storableObject.getSort().value());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	public SystemUser retrieveForLogin(final String login) throws RetrieveObjectException, ObjectNotFoundException {
		final String condition = SystemUserWrapper.COLUMN_LOGIN
				+ EQUALS
				+ APOSTROPHE + DatabaseString.toQuerySubString(login, SIZE_LOGIN_COLUMN) + APOSTROPHE;
		try {
			final Set<SystemUser> set = this.retrieveByCondition(condition);
			if (!set.isEmpty()) {
				return set.iterator().next();
			}
			throw new ObjectNotFoundException("SystemUser for login '" + login + "' not found");
		} catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide);
		}
	}
}
