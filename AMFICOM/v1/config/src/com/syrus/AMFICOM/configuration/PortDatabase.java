/*
 * $Id: PortDatabase.java,v 1.51 2005/03/04 19:50:00 bass Exp $
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
 * @version $Revision: 1.51 $, $Date: 2005/03/04 19:50:00 $
 * @author $Author: bass $
 * @module config_v1
 */
public class PortDatabase extends StorableObjectDatabase {
	// table :: Port

	private static String columns;
	private static String updateMultipleSQLValues;

	private Port fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Port)
			return (Port)storableObject;
		throw new IllegalDataException("PortDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {		
		return ObjectEntities.PORT_ENTITY;
	}

	protected String getColumns(int mode) {		
		if (columns == null) {
			columns = COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ PortWrapper.COLUMN_EQUIPMENT_ID + COMMA
				+ PortWrapper.COLUMN_SORT;		
		}
		return super.getColumns(mode) + columns;
	}

	protected String getUpdateMultipleSQLValues() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = super.getUpdateMultipleSQLValues() + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;

		}
		return updateMultipleSQLValues;
	}	

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
		Port port = this.fromStorableObject(storableObject);
		Identifier typeId = port.getType().getId();
		Identifier equipmentId = port.getEquipmentId();
		return super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(typeId) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(port.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE	+ COMMA
			+ DatabaseIdentifier.toSQLString(equipmentId) + COMMA 
			+ port.getSort();
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		Port port = this.fromStorableObject(storableObject);
		this.retrieveEntity(port);
		port.setCharacteristics0(characteristicDatabase.retrieveCharacteristics(port.getId(), CharacteristicSort.CHARACTERISTIC_SORT_MCM));
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Port port = (storableObject==null) ?
				new Port(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
								null,
								0L,
								null,
								null,
								null,
								0) :
				this.fromStorableObject(storableObject);
		PortType portType;
		try {			
			Identifier portTypeId = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID);
			portType = (portTypeId != null) ? (PortType)ConfigurationStorableObjectPool.getStorableObject(portTypeId, true) : null;
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}

		String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));
		port.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
							DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),								  
							DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
							DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),		
							resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
							portType,								  
							(description != null) ? description : "",
							DatabaseIdentifier.getIdentifier(resultSet, PortWrapper.COLUMN_EQUIPMENT_ID),
							resultSet.getInt(PortWrapper.COLUMN_SORT));
		return port;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		Port port = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Port port = this.fromStorableObject(storableObject);
		this.insertEntity(port);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		try {
			characteristicDatabase.updateCharacteristics(port);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		try {
			characteristicDatabase.updateCharacteristics(storableObjects);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
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
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) GeneralDatabaseContext.getCharacteristicDatabase();
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
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) GeneralDatabaseContext.getCharacteristicDatabase();
		characteristicDatabase.updateCharacteristics(storableObjects);
	}

	public Collection retrieveAll() throws RetrieveObjectException {
		Collection objects = null;
		try {
			objects = this.retrieveByIds(null, null);
		}
		catch (IllegalDataException ide) {           
			Log.debugMessage("PortDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
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
			CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) (GeneralDatabaseContext.getCharacteristicDatabase());
			Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(objects, CharacteristicSort.CHARACTERISTIC_SORT_PORT);
			if (characteristicMap != null)
				for (Iterator iter = objects.iterator(); iter.hasNext();) {
					Port port = (Port) iter.next();
					List characteristics = (List) characteristicMap.get(port.getId());
					port.setCharacteristics0(characteristics);
				}
		}
		return objects;
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
		throws IllegalDataException, SQLException {
		Port port = this.fromStorableObject(storableObject);
		Identifier typeId = port.getType().getId();
		Identifier equipmentId = port.getEquipmentId();

		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, typeId);
		DatabaseString.setString(preparedStatement, ++i, port.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, equipmentId);
		preparedStatement.setInt(++i, port.getSort());
		return i;
	}	
}
