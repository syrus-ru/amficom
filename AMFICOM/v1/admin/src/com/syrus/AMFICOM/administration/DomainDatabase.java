/*
 * $Id: DomainDatabase.java,v 1.23 2005/03/11 09:26:27 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.CharacterizableDatabase;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.23 $, $Date: 2005/03/11 09:26:27 $
 * @author $Author: bob $
 * @module administration_v1
 */

public class DomainDatabase extends CharacterizableDatabase {
	private static String columns;
	private static String updateMultipleSQLValues;
	
	private Domain fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Domain)
			return (Domain) storableObject;
		throw new IllegalDataException("DomainDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	protected String getEnityName() {
		return ObjectEntities.DOMAIN_ENTITY;
	}
	
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ StorableObjectWrapper.COLUMN_NAME + COMMA
			+ StorableObjectWrapper.COLUMN_DESCRIPTION;
		}
		return columns;
	}
	
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION;
		}
		return updateMultipleSQLValues;
	}
	
	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		Domain domain = this.fromStorableObject(storableObject);
		Identifier domainId = domain.getDomainId();
		String sql = DatabaseIdentifier.toSQLString(domainId) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(domain.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(domain.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;
		return sql;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Domain domain = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (domain == null) {
			domain = new Domain(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					null,
					0L,
					null,
					null,
					null);			
		}
		Identifier domainId = DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID);
		domain.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
							 DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
							 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
							 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
							 resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
							 domainId,
							 DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
							 DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));
		return domain;
	}

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject,
			PreparedStatement preparedStatement, int startParameterNumber) throws IllegalDataException, SQLException {
		Domain domain = this.fromStorableObject(storableObject);
		Identifier domainId = domain.getDomainId();
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, domainId);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, domain.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, domain.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}
		
	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Domain domain = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  domain.getId() + "'; argument: " + arg);
				return null;
		}
	}

}
