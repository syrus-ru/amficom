/*
 * $Id: SystemUserDatabase.java,v 1.11 2005/07/24 17:37:58 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.11 $, $Date: 2005/07/24 17:37:58 $
 * @author $Author: arseniy $
 * @module administration_v1
 */

public final class SystemUserDatabase extends StorableObjectDatabase {
	static final int SIZE_LOGIN_COLUMN = 32;

	private static String columns;
	private static String updateMultipleSQLValues;

	private SystemUser fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof SystemUser)
			return (SystemUser) storableObject;
		throw new IllegalDataException("SystemUserDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	@Override
	protected short getEntityCode() {
		return ObjectEntities.SYSTEMUSER_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = SystemUserWrapper.COLUMN_LOGIN + COMMA
					+ SystemUserWrapper.COLUMN_SORT + COMMA
					+ StorableObjectWrapper.COLUMN_NAME + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION;
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
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		SystemUser user = this.fromStorableObject(storableObject);
		return APOSTROPHE + DatabaseString.toQuerySubString(user.getLogin(), SIZE_LOGIN_COLUMN) + APOSTROPHE + COMMA
			+ Integer.toString(user.getSort().value()) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(user.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(user.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		final SystemUser user = (storableObject == null) ? new SystemUser(DatabaseIdentifier.getIdentifier(resultSet,
				StorableObjectWrapper.COLUMN_ID), null, 0L, null, 0, null, null) : this.fromStorableObject(storableObject);
		user.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseString.fromQuerySubString(resultSet.getString(SystemUserWrapper.COLUMN_LOGIN)),
				resultSet.getInt(SystemUserWrapper.COLUMN_SORT),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));
		return user;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final SystemUser user = this.fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, user.getLogin(), SIZE_LOGIN_COLUMN);
		preparedStatement.setInt(++startParameterNumber, user.getSort().value());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, user.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, user.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	public SystemUser retrieveForLogin(final String login) throws RetrieveObjectException, ObjectNotFoundException {
		final String condition = SystemUserWrapper.COLUMN_LOGIN
				+ EQUALS
				+ APOSTROPHE + DatabaseString.toQuerySubString(login, SIZE_LOGIN_COLUMN) + APOSTROPHE;
		try {
			final Set set = this.retrieveByCondition(condition);
			if (!set.isEmpty())
				return (SystemUser) set.iterator().next();
			throw new ObjectNotFoundException("SystemUser for login '" + login + "' not found");
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide);
		}
	}

}
