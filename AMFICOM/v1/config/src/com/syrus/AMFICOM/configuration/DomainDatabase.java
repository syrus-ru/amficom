/*
 * $Id: DomainDatabase.java,v 1.3 2004/08/22 18:49:19 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;


/**
 * @version $Revision: 1.3 $, $Date: 2004/08/22 18:49:19 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class DomainDatabase extends StorableObjectDatabase {
	public static final String COLUMN_NAME  = "name";
	public static final String COLUMN_DESCRIPTION   = "description";

	private Domain fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Domain)
			return (Domain) storableObject;
		throw new IllegalDataException("DomainDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Domain domain = this.fromStorableObject(storableObject);
		this.retrieveDomain(domain);
		domain.setCharacteristics(CharacteristicDatabase.retrieveCharacteristics(domain.getId(), CharacteristicSort.CHARACTERISTIC_SORT_DOMAIN));
	}

	private void retrieveDomain(Domain domain) throws ObjectNotFoundException, RetrieveObjectException {
		String domainIdStr = domain.getId().toSQLString();
		String sql = SQL_SELECT
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION
			+ SQL_FROM + ObjectEntities.DOMAIN_ENTITY
			+ SQL_WHERE + COLUMN_ID + EQUALS + domainIdStr;

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("DomainDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				String idCode = resultSet.getString(DomainMember.COLUMN_DOMAIN_ID);
				domain.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
														 DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
														 /**
															* @todo when change DB Identifier model ,change getString() to getLong()
															*/
														 new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
														 /**
															* @todo when change DB Identifier model ,change getString() to getLong()
															*/
														 new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
														 /**
															* @todo when change DB Identifier model ,change getString() to getLong()
															*/
														 (idCode != null) ? (new Identifier(idCode)) : null,
														 resultSet.getString(COLUMN_NAME),
														 resultSet.getString(COLUMN_DESCRIPTION));
			}
			else
				throw new ObjectNotFoundException("No such domain: " + domainIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "DomainDatabase.retrieve | Cannot retrieve domain " + domainIdStr;
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Domain domain = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Domain domain = this.fromStorableObject(storableObject);
		try {
			this.insertDomain(domain);
		}
		catch (CreateObjectException e) {
			try {
				connection.rollback();
			}
			catch (SQLException sqle) {
				Log.errorMessage("Exception in rolling back");
				Log.errorException(sqle);
			}
			throw e;
		}
		try {
			connection.commit();
		}
		catch (SQLException sqle) {
			Log.errorMessage("Exception in commiting");
			Log.errorException(sqle);
		}
	}

	private void insertDomain(Domain domain) throws CreateObjectException {
		String domainIdStr = domain.getId().toSQLString();

		Identifier domainId = domain.getDomainId();
		String domainIdSubstr = (domainId != null) ? domainId.toSQLString() : Identifier.getNullSQLString();

		String sql = SQL_INSERT_INTO
			+ ObjectEntities.DOMAIN_ENTITY + OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ domainIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(domain.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(domain.getModified()) + COMMA
			+ domain.getCreatorId().toSQLString() + COMMA
			+ domain.getModifierId().toSQLString() + COMMA
			+ domainIdSubstr + COMMA
			+ APOSTOPHE + domain.getName() + APOSTOPHE + COMMA
			+ APOSTOPHE + domain.getDescription() + APOSTOPHE
			+ CLOSE_BRACKET;

		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("DomainDatabase.insertDomain | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "DomainDatabase.insertDomain | Cannot insert domain " + domainIdStr;
			throw new CreateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, UpdateObjectException {
		Domain domain = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				return;
		}
	}
}
