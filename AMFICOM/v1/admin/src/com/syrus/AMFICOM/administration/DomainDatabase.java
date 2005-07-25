/*
 * $Id: DomainDatabase.java,v 1.32 2005/07/25 20:49:23 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.32 $, $Date: 2005/07/25 20:49:23 $
 * @author $Author: arseniy $
 * @module administration_v1
 */

public final class DomainDatabase extends StorableObjectDatabase {
	private static String columns;
	private static String updateMultipleSQLValues;

	private Domain fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Domain)
			return (Domain) storableObject;
		throw new IllegalDataException("DomainDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.DOMAIN_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = DomainMember.COLUMN_DOMAIN_ID + COMMA
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
			+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final Domain domain = this.fromStorableObject(storableObject);
		final Identifier domainId = domain.getDomainId();
		final String sql = DatabaseIdentifier.toSQLString(domainId) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(domain.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(domain.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE;
		return sql;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		Domain domain = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (domain == null) {
			domain = new Domain(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					null,
					StorableObjectVersion.ILLEGAL_VERSION,
					null,
					null,
					null);			
		}
		final Identifier domainId = DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID);
		domain.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				domainId,
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));
		return domain;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final Domain domain = this.fromStorableObject(storableObject);
		final Identifier domainId = domain.getDomainId();
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, domainId);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, domain.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, domain.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

}
