/*
 * $Id: CableLinkTypeDatabase.java,v 1.23 2005/03/05 09:57:16 arseniy Exp $
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
 * @version $Revision: 1.23 $, $Date: 2005/03/05 09:57:16 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public class CableLinkTypeDatabase extends StorableObjectDatabase {
	private static final int SIZE_MANUFACTURER_COLUMN = 64; 

	private static final int SIZE_MANUFACTURER_CODE_COLUMN = 64;

	private static final String LINK_COLUMN_LINK_TYPE_ID = "link_type_id";

	private static String columns;
	private static String updateMultipleSQLValues;

	protected String getEnityName() {
		return ObjectEntities.LINKTYPE_ENTITY;
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
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = COMMA
				+ StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ CableLinkTypeWrapper.COLUMN_KIND + COMMA
				+ CableLinkTypeWrapper.COLUMN_MANUFACTURER + COMMA
				+ CableLinkTypeWrapper.COLUMN_MANUFACTURER_CODE + COMMA
				+ CableLinkTypeWrapper.COLUMN_IMAGE_ID;
		}
		return super.getColumns(mode) + columns;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
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

	private void retrieveCableThreadTypes(Collection cableLinkTypes) throws RetrieveObjectException, IllegalDataException {
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
			catch (ApplicationException e) {
				throw new RetrieveObjectException("Cannot get cable thread types from pool -- " + e.getMessage(), e);
			}
		}
	}

	private void updateCableThreadTypes(Collection cableLinkTypes) throws UpdateObjectException {
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

		try {
			super.updateLinkedEntities(cableThreadTypeIdsMap,
					ObjectEntities.CABLETHREADTYPE_ENTITY,
					LINK_COLUMN_LINK_TYPE_ID,
					StorableObjectWrapper.COLUMN_ID);
		}
		catch (IllegalDataException e) {
			throw new UpdateObjectException("Cannot update cable thread types -- " + e.getMessage(), e);
		}
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CableLinkType cableLinkType = this.fromStorableObject(storableObject);
		super.retrieveEntity(cableLinkType);
		this.retrieveCableThreadTypes(Collections.singletonList(cableLinkType));
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) (GeneralDatabaseContext.getCharacteristicDatabase());
		cableLinkType.setCharacteristics0(characteristicDatabase.retrieveCharacteristics(cableLinkType.getId(), CharacteristicSort.CHARACTERISTIC_SORT_CABLELINKTYPE));
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, SQLException {
		CableLinkType cableLinkType = this.fromStorableObject(storableObject);
		int i;
		i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		preparedStatement.setString( ++i, cableLinkType.getCodename());
		preparedStatement.setString( ++i, cableLinkType.getDescription());
		preparedStatement.setString( ++i, cableLinkType.getName());
		preparedStatement.setInt( ++i, cableLinkType.getSort().value());
		preparedStatement.setString( ++i, cableLinkType.getManufacturer());
		preparedStatement.setString( ++i, cableLinkType.getManufacturerCode());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, cableLinkType.getImageId());
		return i;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		CableLinkType cableLinkType = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (cableLinkType == null) {
			cableLinkType = new CableLinkType(DatabaseIdentifier.getIdentifier(resultSet,StorableObjectWrapper.COLUMN_ID),
															null,
															0L,
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
																	resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
																	DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
																	DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
																	DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
																	resultSet.getInt(CableLinkTypeWrapper.COLUMN_KIND),
																	DatabaseString.fromQuerySubString(resultSet.getString(CableLinkTypeWrapper.COLUMN_MANUFACTURER)),
																	DatabaseString.fromQuerySubString(resultSet.getString(CableLinkTypeWrapper.COLUMN_MANUFACTURER_CODE)),                                   
																	DatabaseIdentifier.getIdentifier(resultSet, CableLinkTypeWrapper.COLUMN_IMAGE_ID));

			return cableLinkType;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
      CableLinkType cableLinkType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  cableLinkType.getId() + "'; argument: " + arg);
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

	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
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
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		characteristicDatabase.updateCharacteristics(storableObject);
		this.updateCableThreadTypes(Collections.singletonList(storableObject));
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
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		characteristicDatabase.updateCharacteristics(storableObjects);
		this.updateCableThreadTypes(storableObjects);
	}

	public Collection retrieveAll() throws RetrieveObjectException {
		Collection objects = null;
		try {
			objects = this.retrieveByIds(null, null);
		}
		catch (IllegalDataException ide) {           
			Log.debugMessage("CableLinkTypeDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
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

		if (objects != null) {
			this.retrieveCableThreadTypes(objects);

			CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
			Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(objects,
					CharacteristicSort.CHARACTERISTIC_SORT_LINKTYPE);
			if (characteristicMap != null)
				for (Iterator it = objects.iterator(); it.hasNext();) {
					CableLinkType cableLinkType = (CableLinkType) it.next();
					List characteristics = (List) characteristicMap.get(cableLinkType.getId());
					cableLinkType.setCharacteristics0(characteristics);
				}
		}
		return objects;
	}

}
