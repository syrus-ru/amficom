/*
 * $Id: LinkDatabase.java,v 1.9 2004/11/25 10:44:55 max Exp $
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
import java.util.Map;

import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
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
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.9 $, $Date: 2004/11/25 10:44:55 $
 * @author $Author: max $
 * @module configuration_v1
 */

public class LinkDatabase extends StorableObjectDatabase {
	// table :: Link
    public static final String COLUMN_TYPE_ID       = "type_id";
    // sort NUMBER(2,0),
    public static final String COLUMN_SORT  = "sort";
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
    // link_id VARCHAR2(32),
    public static final String COLUMN_LINK_ID       = "link_id";
    // color VARCHAR(32),
    public static final String COLUMN_COLOR = "color";
    // mark VARCHAR(32),
    public static final String COLUMN_MARK  = "mark";


	
	private static String columns;
	private static String updateMultiplySQLValues;
	
	private Link fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Link)
			return (Link)storableObject;
		throw new IllegalDataException("LinkDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.LINK_ENTITY;
	}
	
	protected String getColumns() {
		if (columns == null){
			columns = super.getColumns() + COMMA
				+ DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ COLUMN_TYPE_ID + COMMA
				+ COLUMN_SORT + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_INVENTORY_NO + COMMA
				+ COLUMN_SUPPLIER + COMMA
				+ COLUMN_SUPPLIER_CODE + COMMA
				+ COLUMN_LINK_ID + COMMA
				+ COLUMN_COLOR + COMMA
				+ COLUMN_MARK;
		}
		return columns;
	}
	
	protected String getUpdateMultiplySQLValues() {
		if (updateMultiplySQLValues == null){
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA 
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		Link link = fromStorableObject(storableObject);
		String inventoryNo = DatabaseString.toQuerySubString(link.getInventoryNo());
		String supplier = DatabaseString.toQuerySubString(link.getSupplier());
		String supplierCode = DatabaseString.toQuerySubString(link.getSupplierCode());
		Identifier linkId = link.getLinkId();
		String color = DatabaseString.toQuerySubString(link.getColor());
		String mark = DatabaseString.toQuerySubString(link.getMark());
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(link.getDomainId()) + COMMA
			+ DatabaseIdentifier.toSQLString(link.getType().getId()) + COMMA
			+ link.getSort().value() + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(link.getName()) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(link.getDescription()) + APOSTOPHE + COMMA
			+ APOSTOPHE + (inventoryNo != null ? inventoryNo : "") + APOSTOPHE + COMMA
			+ APOSTOPHE + (supplier != null ? supplier : "") + APOSTOPHE + COMMA
			+ APOSTOPHE + (supplierCode != null ? supplierCode : "") + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(linkId) + COMMA
			+ APOSTOPHE + (color != null ? color : "") + APOSTOPHE + COMMA
			+ APOSTOPHE + (mark != null ? mark : "") + APOSTOPHE;
		return sql;
	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement) throws IllegalDataException,
			UpdateObjectException {
		Link link = fromStorableObject(storableObject);
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
			Identifier linkId = link.getLinkId();
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, link.getDomainId());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, link.getType().getId());
			preparedStatement.setInt( ++i, link.getSort().value());
			preparedStatement.setString( ++i, link.getName());
			preparedStatement.setString( ++i, link.getDescription());
			preparedStatement.setString( ++i, link.getInventoryNo());
			preparedStatement.setString( ++i, link.getSupplier());
			preparedStatement.setString( ++i, link.getSupplierCode());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, linkId);
			preparedStatement.setString( ++i, link.getColor());
			preparedStatement.setString( ++i, link.getMark());
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
			link = new Link(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), null, null, null,
									   null, null, null, null, null, 0, null, null, null);			
		}
		String name = DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME));
		String description = DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION));
		String inventoryNo = DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_INVENTORY_NO));
		String supplier = DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_SUPPLIER));
		String supplierCode = DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_SUPPLIER_CODE));
		AbstractLinkType linkType;
		try {
			linkType = (AbstractLinkType)ConfigurationStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_TYPE_ID), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		link.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
														DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
														DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
														DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
														DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),
														(name != null) ? name : "",
														(description != null) ? description : "",
														linkType,
														(inventoryNo != null) ? inventoryNo : "",
														(supplier != null) ? supplier : "",
														(supplierCode != null) ? supplierCode : "",
														resultSet.getInt(COLUMN_SORT),
														DatabaseIdentifier.getIdentifier(resultSet, COLUMN_LINK_ID),
														DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_COLOR)),
														DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_MARK)));

		
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
		
		if(list != null) {
			CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
			Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(list, CharacteristicSort.CHARACTERISTIC_SORT_LINK);
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Link link = (Link) iter.next();
                List characteristics = (List)characteristicMap.get(link);
                link.setCharacteristics(characteristics);
            }
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


