/*
 * $Id: CableLinkTypeDatabase.java,v 1.11 2005/02/03 20:15:05 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.11 $, $Date: 2005/02/03 20:15:05 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public class CableLinkTypeDatabase extends StorableObjectDatabase {
	private static final int SIZE_MANUFACTURER_COLUMN = 64; 

	private static final int SIZE_MANUFACTURER_CODE_COLUMN = 64;

	private static final String LINK_COLUMN_LINK_TYPE_ID = "link_type_id";

	private static String columns;
	private static String updateMultiplySQLValues;

	protected String getEnityName() {
		return ObjectEntities.LINKTYPE_ENTITY;
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
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA
				+ StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ CableLinkTypeWrapper.COLUMN_SORT + COMMA
				+ CableLinkTypeWrapper.COLUMN_MANUFACTURER + COMMA
				+ CableLinkTypeWrapper.COLUMN_MANUFACTURER_CODE + COMMA
				+ CableLinkTypeWrapper.COLUMN_IMAGE_ID;
		}
		return columns;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		CableLinkType cableLinkType = this.fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(cableLinkType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(cableLinkType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE 
			+ APOSTOPHE + DatabaseString.toQuerySubString(cableLinkType.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ cableLinkType.getSort().value() + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(cableLinkType.getManufacturer(), SIZE_MANUFACTURER_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(cableLinkType.getManufacturerCode(), SIZE_MANUFACTURER_CODE_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(cableLinkType.getImageId());
		return sql;
	}

	private CableLinkType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof CableLinkType)
			return (CableLinkType)storableObject;
		throw new IllegalDataException("CableLinkTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	private void retrieveCableThreadTypes(List cableLinkTypes) throws RetrieveObjectException, IllegalDataException {
		if (cableLinkTypes == null || cableLinkTypes.isEmpty())
			return;

		Map cableThreadTypeIdsMap = super.retrieveLinkedEntityIds(cableLinkTypes,
				ObjectEntities.CABLETHREADTYPE_ENTITY,
				LINK_COLUMN_LINK_TYPE_ID,
				StorableObjectWrapper.COLUMN_ID);
		CableLinkType cableLinkType;
		List cableThreadTypeIds;
		for (Iterator it = cableLinkTypes.iterator(); it.hasNext();) {
			cableLinkType = (CableLinkType) it.next();
			cableThreadTypeIds = (List) cableThreadTypeIdsMap.get(cableLinkType.getId());

			try {
				cableLinkType.setCableThreadTypes0(ConfigurationStorableObjectPool.getStorableObjects(cableThreadTypeIds, true));
			}
			catch (DatabaseException e) {
				throw new RetrieveObjectException("Cannot get cable thread types from pool -- " + e.getMessage(), e);
			}
			catch (CommunicationException e) {
				throw new RetrieveObjectException("Cannot get cable thread types from pool -- " + e.getMessage(), e);
			}
		}
	}

	private void updateCableThreadTypes(List cableLinkTypes) throws UpdateObjectException {
		if (cableLinkTypes == null || cableLinkTypes.isEmpty())
			return;

		Map cableThreadTypeIdsMap = new HashMap();
		CableLinkType cableLinkType;
		List cableThreadTypes;
		List cableThreadTypeIds;
		for (Iterator it = cableLinkTypes.iterator(); it.hasNext();) {
			cableLinkType = (CableLinkType) it.next();
			cableThreadTypes = cableLinkType.getCableThreadTypes();
			cableThreadTypeIds = new ArrayList(cableThreadTypes.size());
			for (Iterator it1 = cableThreadTypes.iterator(); it1.hasNext();)
				cableThreadTypeIds.add(((CableThreadType) it1.next()).getId());

			cableThreadTypeIdsMap.put(cableLinkType.getId(), cableThreadTypeIds);
		}

		super.updateLinkedEntities(cableThreadTypeIdsMap,
				ObjectEntities.CABLETHREADTYPE_ENTITY,
				LINK_COLUMN_LINK_TYPE_ID,
				StorableObjectWrapper.COLUMN_ID);
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CableLinkType cableLinkType = this.fromStorableObject(storableObject);
		super.retrieveEntity(cableLinkType);
		this.retrieveCableThreadTypes(Collections.singletonList(cableLinkType));
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) (GeneralDatabaseContext.getCharacteristicDatabase());
		cableLinkType.setCharacteristics0(characteristicDatabase.retrieveCharacteristics(cableLinkType.getId(), CharacteristicSort.CHARACTERISTIC_SORT_CABLELINKTYPE));
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		CableLinkType cableLinkType = this.fromStorableObject(storableObject);
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
			preparedStatement.setString( ++i, cableLinkType.getCodename());
			preparedStatement.setString( ++i, cableLinkType.getDescription());
			preparedStatement.setString( ++i, cableLinkType.getName());
			preparedStatement.setInt( ++i, cableLinkType.getSort().value());
			preparedStatement.setString( ++i, cableLinkType.getManufacturer());
			preparedStatement.setString( ++i, cableLinkType.getManufacturerCode());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, cableLinkType.getImageId());
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException("LinkDatabase." +
							"setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		CableLinkType cableLinkType = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (cableLinkType == null) {
			cableLinkType = new CableLinkType(DatabaseIdentifier.getIdentifier(resultSet,StorableObjectWrapper.COLUMN_ID),
															null,
															null,
															null,
															null,
															0,
															null,
															null,
															null);         
			}
			cableLinkType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
																	DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),                                    
																	DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
																	DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
																	DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
																	DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
																	DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
																	resultSet.getInt(CableLinkTypeWrapper.COLUMN_SORT),
																	DatabaseString.fromQuerySubString(resultSet.getString(CableLinkTypeWrapper.COLUMN_MANUFACTURER)),
																	DatabaseString.fromQuerySubString(resultSet.getString(CableLinkTypeWrapper.COLUMN_MANUFACTURER_CODE)),                                   
																	DatabaseIdentifier.getIdentifier(resultSet, CableLinkTypeWrapper.COLUMN_IMAGE_ID));

			return cableLinkType;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//      CableLinkType cableLinkType = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		CableLinkType cableLinkType = this.fromStorableObject(storableObject);
		super.insertEntity(cableLinkType);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		try {
			characteristicDatabase.updateCharacteristics(storableObject);
			this.updateCableThreadTypes(Collections.singletonList(storableObject));
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException("LinkTypeDatabase.insert | UpdateObjectException " + e);
		}
	}

	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		try {
			characteristicDatabase.updateCharacteristics(storableObjects);
			this.updateCableThreadTypes(storableObjects);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException("LinkTypeDatabase.insert | UpdateObjectException " + e);
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		switch (updateKind) {
			case UPDATE_FORCE:
				super.checkAndUpdateEntity(storableObject, true);
				break;
			case UPDATE_CHECK:
			default:
				super.checkAndUpdateEntity(storableObject, false);
				break;
		}
		characteristicDatabase.updateCharacteristics(storableObject);
		this.updateCableThreadTypes(Collections.singletonList(storableObject));
	}

	public void update(List storableObjects, int updateKind, Object arg)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		switch (updateKind) {
			case UPDATE_FORCE:
				super.checkAndUpdateEntities(storableObjects, true);            
				break;
			case UPDATE_CHECK:
			default:
				super.checkAndUpdateEntities(storableObjects, false);
				break;
		}
		characteristicDatabase.updateCharacteristics(storableObjects);
		this.updateCableThreadTypes(storableObjects);
	}

	public List retrieveAll() throws RetrieveObjectException {
		List list = null;
		try {
			list = this.retrieveByIds(null, null);
		}
		catch (IllegalDataException ide) {           
			Log.debugMessage("CableLinkTypeDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
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

		if (list != null) {
			this.retrieveCableThreadTypes(list);

			CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
			Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(list,
					CharacteristicSort.CHARACTERISTIC_SORT_LINKTYPE);
			if (characteristicMap != null)
				for (Iterator it = list.iterator(); it.hasNext();) {
					CableLinkType cableLinkType = (CableLinkType) it.next();
					List characteristics = (List) characteristicMap.get(cableLinkType.getId());
					cableLinkType.setCharacteristics0(characteristics);
				}
		}
		return list;
	}

	public List retrieveByCondition(List ids, StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		return this.retrieveButIds(ids);
	}
}
