/*
 * $Id: LinkDatabase.java,v 1.1 2004/10/22 13:03:43 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.1 $, $Date: 2004/10/22 13:03:43 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class LinkDatabase extends StorableObjectDatabase {
	// table :: Link
	//	 type_id VARCHAR2(32) NOT NULL,
    public static final String COLUMN_TYPE_ID       = "type_id";
    // name VARCHAR2(64) NOT NULL,
    public static final String COLUMN_NAME  = "name";
    // description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
    // inventory_no VARCHAR2(64),
    public static final String COLUMN_INVENTORY_NO  = "inventory_no";
    // supplier VARCHAR2(64),
    public static final String COLUMN_SUPPLIER      = "supplier";
    // supplier_code VARCHAR2(64),
    public static final String COLUMN_SUPPLIER_CODE = "supplier_code";

	
	private String updateColumns;
	private String updateMultiplySQLValues;
	
	private Link fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Link)
			return (Link)storableObject;
		throw new IllegalDataException("LinkDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.LINK_ENTITY;
	}
	
	protected String getTableName() {
		return ObjectEntities.LINK_ENTITY;
	}
	
	protected String getUpdateColumns() {
		if (this.updateColumns == null){
			this.updateColumns = super.getUpdateColumns() + COMMA
				+ DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ COLUMN_TYPE_ID + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_INVENTORY_NO + COMMA
				+ COLUMN_SUPPLIER + COMMA
				+ COLUMN_SUPPLIER_CODE;
		}
		return this.updateColumns;
	}
	
	protected String getUpdateMultiplySQLValues() {
		if (this.updateColumns == null){
			this.updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA 
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return this.updateMultiplySQLValues;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		Link link = fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ link.getDomainId().toSQLString() + COMMA
			+ link.getType().getId().toSQLString() + COMMA
			+ APOSTOPHE + link.getName() + APOSTOPHE + COMMA
			+ APOSTOPHE + link.getDescription() + APOSTOPHE + COMMA
			+ APOSTOPHE + link.getInventoryNo() + APOSTOPHE + COMMA
			+ APOSTOPHE + link.getSupplier() + APOSTOPHE + COMMA
			+ APOSTOPHE + link.getSupplierCode() + APOSTOPHE + COMMA;
		return sql;
	}
	
	protected String retrieveQuery(String condition){
		return super.retrieveQuery(condition) + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_TYPE_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_INVENTORY_NO + COMMA
			+ COLUMN_SUPPLIER + COMMA
			+ COLUMN_SUPPLIER_CODE
			+ SQL_FROM + ObjectEntities.LINK_ENTITY
			+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement) throws IllegalDataException,
			UpdateObjectException {
		Link link = fromStorableObject(storableObject);
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
			preparedStatement.setString( ++i , link.getDomainId().getCode());
			preparedStatement.setString( ++i, link.getType().getId().getCode());
			preparedStatement.setString( ++i, link.getName());
			preparedStatement.setString( ++i, link.getDescription());
			preparedStatement.setString( ++i, link.getInventoryNo());
			preparedStatement.setString( ++i, link.getSupplier());
			preparedStatement.setString( ++i, link.getSupplierCode());
		} catch (SQLException sqle) {
			throw new UpdateObjectException("LinkDatabase." +
					"setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Link link = storableObject == null ? null : fromStorableObject(storableObject);
		if (link == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			link = new Link(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null,
									   null, null, null, null, null);			
		}
		String name = resultSet.getString(COLUMN_NAME);
		String description = resultSet.getString(COLUMN_DESCRIPTION);
		String inventoryNo = resultSet.getString(COLUMN_INVENTORY_NO);
		String supplier = resultSet.getString(COLUMN_SUPPLIER);
		String supplierCode = resultSet.getString(COLUMN_SUPPLIER_CODE);
		LinkType linkType;
		try {
			linkType = (LinkType)ConfigurationStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_TYPE_ID)), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		link.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
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
														new Identifier(resultSet.getString(DomainMember.COLUMN_DOMAIN_ID)),
														(name != null) ? name : "",
														(description != null) ? description : "",
														linkType,
														(inventoryNo != null) ? inventoryNo : "",
														(supplier != null) ? supplier : "",
														(supplierCode != null) ? supplierCode : "");

		
		return link;
	}

	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
		Link link = this.fromStorableObject(storableObject);
		super.retrieveEntity(link);
		link.setCharacteristics(characteristicDatabase.retrieveCharacteristics(link.getId(), CharacteristicSort.CHARACTERISTIC_SORT_LINK));
	}	

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Link link = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Link link = this.fromStorableObject(storableObject);
		super.insertEntity(link);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);			
		characteristicDatabase.insert(link.getCharacteristics());		
	}
	
	public void insert(List storableObjects) throws IllegalDataException,
			CreateObjectException {
		super.insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
		for (Iterator iter = storableObjects.iterator(); iter.hasNext();) {
			Link link = (Link) iter.next();
			characteristicDatabase.insert(link.getCharacteristics());
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		//TODO Check this method on errors		
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
		throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		//TODO Check this method on errors
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
	
	
	public List retrieveAll() throws RetrieveObjectException {
        List list = null;
        try {
            list = retrieveByIds(null, null);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage("LinkDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
            throw new RetrieveObjectException(ide);
        }
        return list;
    }
	
	
	public List retrieveByIds(List ids, String condition) throws IllegalDataException, 
			RetrieveObjectException {
		List list = null;
		if ((ids == null) || (ids.isEmpty()))
			list = super.retrieveByIdsOneQuery(null, condition);
		else list = super.retrieveByIdsOneQuery(ids, condition);
		
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
		
		for (Iterator it = list.iterator(); it.hasNext();) {
			Link link = (Link) it.next();
			link.setCharacteristics(characteristicDatabase.retrieveCharacteristics(link.getId(), CharacteristicSort.CHARACTERISTIC_SORT_LINK));
		}
		return list;
	}	


	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		List list;
		{
			Log.errorMessage("LinkDatabase.retrieveByCondition | Unknown condition class: " + condition.getClass().getName());
			list = this.retrieveButIds(ids);
		}
		return list;
	}
}
