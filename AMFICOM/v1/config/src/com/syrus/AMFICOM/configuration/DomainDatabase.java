/*
 * $Id: DomainDatabase.java,v 1.17 2004/10/29 12:48:24 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;


/**
 * @version $Revision: 1.17 $, $Date: 2004/10/29 12:48:24 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class DomainDatabase extends StorableObjectDatabase {
	public static final String COLUMN_NAME  = "name";
	public static final String COLUMN_DESCRIPTION   = "description";

	private String updateColumns;
	private String updateMultiplySQLValues;
	
	private Domain fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Domain)
			return (Domain) storableObject;
		throw new IllegalDataException("DomainDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	protected String getEnityName() {
		return ObjectEntities.DOMAIN_ENTITY;
	}
	
	protected String getTableName() {
		return ObjectEntities.DOMAIN_ENTITY;
	}
	
	protected String getUpdateColumns() {
		if (this.updateColumns == null){
			this.updateColumns = super.getUpdateColumns() + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION;
		}
		return this.updateColumns;
	}
	
	protected String getUpdateMultiplySQLValues() {
		if (this.updateMultiplySQLValues == null){
			this.updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION;
		}
		return this.updateMultiplySQLValues;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		Domain domain = fromStorableObject(storableObject);
		Identifier domainId = domain.getDomainId();
		String domainIdSubstr = (domainId != null) ? domainId.toSQLString() : Identifier.getNullSQLString();
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ domainIdSubstr + COMMA
			+ APOSTOPHE + domain.getName() + APOSTOPHE + COMMA
			+ APOSTOPHE + domain.getDescription() + APOSTOPHE;
		return sql;
	}
	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
		Domain domain = this.fromStorableObject(storableObject);
		this.retrieveEntity(domain);
		domain.setCharacteristics(characteristicDatabase.retrieveCharacteristics(domain.getId(), CharacteristicSort.CHARACTERISTIC_SORT_DOMAIN));
	}

	protected String retrieveQuery(String condition){
		return super.retrieveQuery(condition) + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION
			+ SQL_FROM + ObjectEntities.DOMAIN_ENTITY
			+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}
	
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Domain domain = storableObject == null ? null : fromStorableObject(storableObject);
		if (domain == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			domain = new Domain(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, null);			
		}
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
		
		return domain;
	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement) throws IllegalDataException,
			UpdateObjectException {
		Domain domain = fromStorableObject(storableObject);
			Identifier domainId = domain.getDomainId();
		String domainIdSubstr = (domainId != null) ? domainId.getCode() : "";
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
			preparedStatement.setString( ++i, domainIdSubstr);
			preparedStatement.setString( ++i, domain.getName());
			preparedStatement.setString( ++i, domain.getDescription());
		}catch (SQLException sqle) {
			throw new UpdateObjectException("DomainDatabase." +
					"setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
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
		super.insertEntity(domain);		
	}

	public void insert(List storableObjects) throws IllegalDataException,
			CreateObjectException {
		super.insertEntities(storableObjects);
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) 
			throws IllegalDataException, VersionCollisionException, 
			UpdateObjectException {
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntity(storableObject, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntity(storableObject, false);
			break;
		}
	}
	
	public void update(List storableObjects, int updateKind, Object arg)
			throws IllegalDataException, VersionCollisionException,
			UpdateObjectException {
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntities(storableObjects, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntities(storableObjects, false);
			break;
		}
	}
	
	public List retrieveByIds(List ids ,String condition) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return super.retrieveByIdsOneQuery(null, condition);
		return super.retrieveByIdsOneQuery(ids, condition);	
		//return retriveByIdsPreparedStatement(ids);
	}
	
	private List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
        List list = null;
        
        String condition = COLUMN_ID + EQUALS + domain.getId().toSQLString();
        
        try {
            list = retrieveButIds(ids, condition);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage("DomainDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
        }
        
        return list;
    }
	
	private List retrieveButIdsByName(List ids, String name) throws RetrieveObjectException {
        List list = null;
        
        String condition = COLUMN_NAME + EQUALS + APOSTOPHE + name + APOSTOPHE;
        
        try {
            list = retrieveButIds(ids, condition);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage("DomainDatabase.retrieveButIdsByName | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
        }
        
        return list;
    }

	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		List list = null;
		if (condition instanceof DomainCondition){
			DomainCondition domainCondition = (DomainCondition)condition;
			list = this.retrieveButIdsByDomain(ids, domainCondition.getDomain());
		} else if (condition instanceof StringFieldCondition){
			StringFieldCondition stringFieldCondition = (StringFieldCondition)condition;
			list = this.retrieveButIdsByName(ids, stringFieldCondition.getString());
		} else {
			Log.errorMessage("DomainDatabase.retrieveByCondition | Unknown condition class: " + condition.getClass().getName());
			list = this.retrieveButIds(ids);
		} 
		return list;
	}
}
