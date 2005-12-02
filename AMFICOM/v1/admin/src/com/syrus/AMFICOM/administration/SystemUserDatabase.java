/*
 * $Id: SystemUserDatabase.java,v 1.20 2005/12/02 11:24:11 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.SystemUserWrapper.LINK_COLUMN_ROLE_ID;
import static com.syrus.AMFICOM.administration.SystemUserWrapper.LINK_COLUMN_SYSTEM_USER_ID;
import static com.syrus.AMFICOM.general.TableNames.SYSTEM_USER_ROLE_LINK;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.20 $, $Date: 2005/12/02 11:24:11 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module administration
 */

public final class SystemUserDatabase extends StorableObjectDatabase<SystemUser> {
	static final int SIZE_LOGIN_COLUMN = 32;

	private static String columns;
	private static String updateMultipleSQLValues;

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
				? new SystemUser(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						0,
						null,
						null)
					: storableObject;
		user.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(SystemUserWrapper.COLUMN_LOGIN)),
				resultSet.getInt(SystemUserWrapper.COLUMN_SORT),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));
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
	
	private Map<Identifier, Set<Identifier>> createRoleIdsMap(final Set<SystemUser> systemUsers) {
		final Map<Identifier, Set<Identifier>> measurementSetupIdsMap = 
			new HashMap<Identifier, Set<Identifier>>();
		for (final SystemUser systemUser : systemUsers) {
			measurementSetupIdsMap.put(systemUser.getId(), systemUser.getRoleIds());
		}
		return measurementSetupIdsMap;
	}
	
	@Override
	protected void insert(final Set<SystemUser> systemUsers) throws IllegalDataException, CreateObjectException {
		super.insert(systemUsers);

		final Map<Identifier, Set<Identifier>> roleIdsMap = this.createRoleIdsMap(systemUsers);
		super.insertLinkedEntityIds(roleIdsMap,
				SYSTEM_USER_ROLE_LINK,
				LINK_COLUMN_SYSTEM_USER_ID,
				LINK_COLUMN_ROLE_ID);
	}

	@Override
	protected void update(final Set<SystemUser> systemUsers) throws UpdateObjectException {
		super.update(systemUsers);

		final Map<Identifier, Set<Identifier>> roleIdsMap = this.createRoleIdsMap(systemUsers);
		super.updateLinkedEntityIds(roleIdsMap,
				SYSTEM_USER_ROLE_LINK,
				LINK_COLUMN_SYSTEM_USER_ID,
				LINK_COLUMN_ROLE_ID);
	}
	
	@Override
	protected Set<SystemUser> retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set<SystemUser> systemUsers = super.retrieveByCondition(conditionQuery);
		
		this.retrieveLinksByOneQuery(systemUsers);
		
		return systemUsers;
	}
	
	private void retrieveLinksByOneQuery(final Set<SystemUser> systemUsers) throws RetrieveObjectException {
		if ((systemUsers == null) || (systemUsers.isEmpty())) {
			return;
		}

		final Map<Identifier, Set<Identifier>> roleIdsMap = this.retrieveLinkedEntityIds(systemUsers,
			SYSTEM_USER_ROLE_LINK,
			LINK_COLUMN_SYSTEM_USER_ID,
			LINK_COLUMN_ROLE_ID);

		for (final SystemUser systemUser : systemUsers) {
			final Identifier systemUserId = systemUser.getId();
			
			final Set<Identifier> roleIds = roleIdsMap.get(systemUserId);
			systemUser.setRoleIds0(roleIds);
		}
	}
}
