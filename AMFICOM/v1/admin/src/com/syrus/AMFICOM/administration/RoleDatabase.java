/*
 * $Id: RoleDatabase.java,v 1.4 2006/04/10 16:56:18 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import static com.syrus.AMFICOM.administration.RoleWrapper.LINK_COLUMN_ROLE_ID;
import static com.syrus.AMFICOM.administration.RoleWrapper.LINK_COLUMN_SYSTEM_USER_ID;
import static com.syrus.AMFICOM.general.ObjectEntities.ROLE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.TableNames.SYSTEM_USER_ROLE_LINK;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.4 $, $Date: 2006/04/10 16:56:18 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module administration
 */

public final class RoleDatabase extends StorableObjectDatabase<Role> {
	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {
		return ROLE_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_CODENAME + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION;
		}
		return columns;
	}	

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION;		
		}
		return updateMultipleSQLValues;
	}	

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final Role storableObject) throws IllegalDataException {
		return APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE;
	}

	@Override
	protected Role updateEntityFromResultSet(final Role storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				SQLException {
		final Role role = (storableObject == null)
				? new Role(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						ILLEGAL_VERSION,
						null,
						null,
						null)
					: storableObject;
		role.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));
		return role;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final Role role,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, role.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, role.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	@Override
	protected Set<Role> retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set<Role> roles = super.retrieveByCondition(conditionQuery);

		this.retrieveLinksByOneQuery(roles);

		return roles;
	}

	private void retrieveLinksByOneQuery(final Set<Role> roles) throws RetrieveObjectException {
		if (roles == null || roles.isEmpty()) {
			return;
		}

		final Map<Identifier, Set<Identifier>> systemUserIdsMap = this.retrieveLinkedEntityIds(roles,
				SYSTEM_USER_ROLE_LINK,
				LINK_COLUMN_ROLE_ID,
				LINK_COLUMN_SYSTEM_USER_ID);

		for (final Role role : roles) {
			final Identifier roleId = role.getId();

			final Set<Identifier> systemUserIds = systemUserIdsMap.get(roleId);
			role.setSystemUserIds0(systemUserIds);
		}
	}

	@Override
	protected void insert(final Set<Role> roles) throws CreateObjectException, IllegalDataException {
		super.insert(roles);

		final Map<Identifier, Set<Identifier>> systemUserIdsMap = StorableObject.createValuesMap(roles, LINK_COLUMN_SYSTEM_USER_ID);
		super.insertLinkedEntityIds(systemUserIdsMap,
				SYSTEM_USER_ROLE_LINK,
				LINK_COLUMN_ROLE_ID,
				LINK_COLUMN_SYSTEM_USER_ID);
	}

	@Override
	protected void update(final Set<Role> roles) throws UpdateObjectException {
		super.update(roles);

		final Map<Identifier, Set<Identifier>> systemUserIdsMap = StorableObject.createValuesMap(roles, LINK_COLUMN_SYSTEM_USER_ID);
		super.updateLinkedEntityIds(systemUserIdsMap,
				SYSTEM_USER_ROLE_LINK,
				LINK_COLUMN_ROLE_ID,
				LINK_COLUMN_SYSTEM_USER_ID);
	}
}
