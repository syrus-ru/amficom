/*
 * $Id: LinkDatabase.java,v 1.22 2005/01/28 10:23:01 arseniy Exp $
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

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.22 $, $Date: 2005/01/28 10:23:01 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public class LinkDatabase extends StorableObjectDatabase {
	private static final int SIZE_INVENTORY_NO_COLUMN  = 64;

	private static final int SIZE_SUPPLIER_COLUMN  = 128;

	private static final int SIZE_SUPPLIER_CODE_COLUMN  = 64;

	private static final int SIZE_MARK_COLUMN  = 32;

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

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA
				+ DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ LinkWrapper.COLUMN_TYPE_ID + COMMA
				+ LinkWrapper.COLUMN_SORT + COMMA
				+ LinkWrapper.COLUMN_NAME + COMMA
				+ LinkWrapper.COLUMN_DESCRIPTION + COMMA
				+ LinkWrapper.COLUMN_INVENTORY_NO + COMMA
				+ LinkWrapper.COLUMN_SUPPLIER + COMMA
				+ LinkWrapper.COLUMN_SUPPLIER_CODE + COMMA
				+ LinkWrapper.COLUMN_COLOR + COMMA
				+ LinkWrapper.COLUMN_MARK;
		}
		return columns;
	}
	
	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null) {
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA 
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
		Link link = this.fromStorableObject(storableObject);
		String inventoryNo = DatabaseString.toQuerySubString(link.getInventoryNo(), SIZE_INVENTORY_NO_COLUMN);
		String supplier = DatabaseString.toQuerySubString(link.getSupplier(), SIZE_SUPPLIER_COLUMN);
		String supplierCode = DatabaseString.toQuerySubString(link.getSupplierCode(), SIZE_SUPPLIER_CODE_COLUMN);
		String mark = DatabaseString.toQuerySubString(link.getMark(),SIZE_MARK_COLUMN);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(link.getDomainId()) + COMMA
			+ DatabaseIdentifier.toSQLString(link.getType().getId()) + COMMA
			+ link.getSort().value() + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(link.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(link.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + (inventoryNo != null ? inventoryNo : "") + APOSTOPHE + COMMA
			+ APOSTOPHE + (supplier != null ? supplier : "") + APOSTOPHE + COMMA
			+ APOSTOPHE + (supplierCode != null ? supplierCode : "") + APOSTOPHE + COMMA
			+ APOSTOPHE + link.getColor() + APOSTOPHE + COMMA
			+ APOSTOPHE + (mark != null ? mark : "") + APOSTOPHE;
		return sql;
	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		Link link = this.fromStorableObject(storableObject);
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, link.getDomainId());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, link.getType().getId());
			preparedStatement.setInt( ++i, link.getSort().value());
			preparedStatement.setString( ++i, link.getName());
			preparedStatement.setString( ++i, link.getDescription());
			preparedStatement.setString( ++i, link.getInventoryNo());
			preparedStatement.setString( ++i, link.getSupplier());
			preparedStatement.setString( ++i, link.getSupplierCode());
			preparedStatement.setInt( ++i, link.getColor());
			preparedStatement.setString( ++i, link.getMark());
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException("LinkDatabase." + "setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Link link = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (link == null){			
			link = new Link(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
										null,
										null,
										null,
										null,
										null,
										null,
										null,
										null,
										0,
										0,
										null);			
		}
		String name = DatabaseString.fromQuerySubString(resultSet.getString(LinkWrapper.COLUMN_NAME));
		String description = DatabaseString.fromQuerySubString(resultSet.getString(LinkWrapper.COLUMN_DESCRIPTION));
		String inventoryNo = DatabaseString.fromQuerySubString(resultSet.getString(LinkWrapper.COLUMN_INVENTORY_NO));
		String supplier = DatabaseString.fromQuerySubString(resultSet.getString(LinkWrapper.COLUMN_SUPPLIER));
		String supplierCode = DatabaseString.fromQuerySubString(resultSet.getString(LinkWrapper.COLUMN_SUPPLIER_CODE));
		AbstractLinkType linkType;
		try {
			linkType = (AbstractLinkType)ConfigurationStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, LinkWrapper.COLUMN_TYPE_ID), true);
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
								resultSet.getInt(LinkWrapper.COLUMN_SORT),
								resultSet.getInt(LinkWrapper.COLUMN_COLOR),
								DatabaseString.fromQuerySubString(resultSet.getString(LinkWrapper.COLUMN_MARK)));


		return link;
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		Link link = this.fromStorableObject(storableObject);
		super.retrieveEntity(link);
		link.setCharacteristics(characteristicDatabase.retrieveCharacteristics(link.getId(), CharacteristicSort.CHARACTERISTIC_SORT_LINK));
	}	

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		//Link link = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Link link = this.fromStorableObject(storableObject);
		super.insertEntity(link);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());			
		characteristicDatabase.insert(link.getCharacteristics());		
	}

	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		for (Iterator iter = storableObjects.iterator(); iter.hasNext();) {
			Link link = (Link) iter.next();
			characteristicDatabase.insert(link.getCharacteristics());
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		switch (updateKind) {
			case UPDATE_FORCE:
				super.checkAndUpdateEntity(storableObject, true);
				characteristicDatabase.updateCharacteristics(storableObject);
				break;
			case UPDATE_CHECK:
			default:
				super.checkAndUpdateEntity(storableObject, false);
				characteristicDatabase.updateCharacteristics(storableObject);
				break;
		}
	}

	public void update(List storableObjects, int updateKind, Object arg)
		throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		switch (updateKind) {
			case UPDATE_FORCE:
				super.checkAndUpdateEntities(storableObjects, true);
				for (Iterator it = storableObjects.iterator(); it.hasNext();) {
					StorableObject storableObject = (StorableObject) it.next();
					characteristicDatabase.updateCharacteristics(storableObject);
				}
				break;
			case UPDATE_CHECK:
			default:
				super.checkAndUpdateEntities(storableObjects, false);
				for (Iterator it = storableObjects.iterator(); it.hasNext();) {
					StorableObject storableObject = (StorableObject) it.next();
					characteristicDatabase.updateCharacteristics(storableObject);
				}
				break;
		}
	}

	public List retrieveAll() throws RetrieveObjectException {
		List list = null;
		try {
			list = this.retrieveByIds(null, null);
		}
		catch (IllegalDataException ide) {           
			Log.debugMessage("LinkDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
			throw new RetrieveObjectException(ide);
		}
		return list;
	}

	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		List list = null;
		if ((ids == null) || (ids.isEmpty()))
			list = this.retrieveByIdsOneQuery(null, condition);
		else
			list = this.retrieveByIdsOneQuery(ids, condition);

		if(list != null) {
			CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
			Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(list,
					CharacteristicSort.CHARACTERISTIC_SORT_LINK);
			if (characteristicMap != null)
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					Link link = (Link) iter.next();
					List characteristics = (List) characteristicMap.get(link.getId());
					link.setCharacteristics(characteristics);
				}
		}
		return list;
	}

	public List retrieveByCondition(List ids, StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		List list;
		{
			Log.errorMessage("LinkDatabase.retrieveByCondition | Unknown condition class: " + condition.getClass().getName());
			list = this.retrieveButIds(ids);
		}
		return list;
	}
}
