/*
 * $Id: LinkDatabase.java,v 1.35 2005/03/05 09:57:16 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.35 $, $Date: 2005/03/05 09:57:16 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public class LinkDatabase extends StorableObjectDatabase {
	private static final int SIZE_INVENTORY_NO_COLUMN  = 64;

	private static final int SIZE_SUPPLIER_COLUMN  = 128;

	private static final int SIZE_SUPPLIER_CODE_COLUMN  = 64;

	private static final int SIZE_MARK_COLUMN  = 32;

	private static String columns;
	private static String updateMultipleSQLValues;

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
			columns = COMMA
				+ DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ LinkWrapper.COLUMN_SORT + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ LinkWrapper.COLUMN_INVENTORY_NO + COMMA
				+ LinkWrapper.COLUMN_SUPPLIER + COMMA
				+ LinkWrapper.COLUMN_SUPPLIER_CODE + COMMA
				+ LinkWrapper.COLUMN_COLOR + COMMA
				+ LinkWrapper.COLUMN_MARK;
		}
		return super.getColumns(mode) + columns;
	}
	
	protected String getUpdateMultipleSQLValues() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = super.getUpdateMultipleSQLValues() + COMMA 
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
		return updateMultipleSQLValues;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
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
			throws IllegalDataException, SQLException {
		Link link = this.fromStorableObject(storableObject);
		int i;
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
		return i;
	}

	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Link link = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (link == null){			
			link = new Link(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
										null,
										0L,
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
		String name = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME));
		String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));
		String inventoryNo = DatabaseString.fromQuerySubString(resultSet.getString(LinkWrapper.COLUMN_INVENTORY_NO));
		String supplier = DatabaseString.fromQuerySubString(resultSet.getString(LinkWrapper.COLUMN_SUPPLIER));
		String supplierCode = DatabaseString.fromQuerySubString(resultSet.getString(LinkWrapper.COLUMN_SUPPLIER_CODE));
		AbstractLinkType linkType;
		try {
			linkType = (AbstractLinkType) ConfigurationStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		link.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
								DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
								DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
								DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
								resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
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
		Link link = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  link.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Link link = this.fromStorableObject(storableObject);
		super.insertEntity(link);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());			
		characteristicDatabase.insert(link.getCharacteristics());		
	}

	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		for (Iterator iter = storableObjects.iterator(); iter.hasNext();) {
			Link link = (Link) iter.next();
			characteristicDatabase.insert(link.getCharacteristics());
		}
	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_FORCE:
				super.checkAndUpdateEntity(storableObject, modifierId, true);
				break;
			case UPDATE_CHECK:
			default:
				super.checkAndUpdateEntity(storableObject, modifierId, false);
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) (GeneralDatabaseContext.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(storableObject);
	}

	public void update(Collection storableObjects, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_FORCE:
				super.checkAndUpdateEntities(storableObjects, modifierId, true);
				break;
			case UPDATE_CHECK:
			default:
				super.checkAndUpdateEntities(storableObjects, modifierId, false);
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) (GeneralDatabaseContext.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(storableObjects);
	}

	public Collection retrieveAll() throws RetrieveObjectException {
		Collection objects = null;
		try {
			objects = this.retrieveByIds(null, null);
		}
		catch (IllegalDataException ide) {           
			Log.debugMessage("LinkDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
			throw new RetrieveObjectException(ide);
		}
		return objects;
	}

	public Collection retrieveByIds(Collection ids, String condition) throws IllegalDataException, RetrieveObjectException {
		Collection objects = null;
		if ((ids == null) || (ids.isEmpty()))
			objects = this.retrieveByIdsOneQuery(null, condition);
		else
			objects = this.retrieveByIdsOneQuery(ids, condition);

		if(objects != null) {
			CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
			Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(objects,
					CharacteristicSort.CHARACTERISTIC_SORT_LINK);
			if (characteristicMap != null)
				for (Iterator iter = objects.iterator(); iter.hasNext();) {
					Link link = (Link) iter.next();
					List characteristics = (List) characteristicMap.get(link.getId());
					link.setCharacteristics(characteristics);
				}
		}
		return objects;
	}
}
