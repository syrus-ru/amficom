/*
 * $Id: PortDatabase.java,v 1.36 2005/01/14 18:07:08 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainCondition;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.36 $, $Date: 2005/01/14 18:07:08 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public class PortDatabase extends StorableObjectDatabase {
	// table :: Port

	// type_id VARCHAR2(32) NOT NULL,
	public static final String COLUMN_TYPE_ID       = "type_id";
	// description VARCHAR2(256),
	public static final String COLUMN_DESCRIPTION   = "description";
	// equipment_id VARCHAR2(32),
	public static final String COLUMN_EQUIPMENT_ID  = "equipment_id";
	// sort NUMBER(2) NOT NULL,
	public static final String COLUMN_SORT  = "sort";    

	private static String columns;
	private static String updateMultiplySQLValues;

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
			columns = super.getColumns(mode) + COMMA
				+ COLUMN_TYPE_ID + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_EQUIPMENT_ID + COMMA
				+ COLUMN_SORT;		
		}
		return columns;
	}

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null) {
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;

		}
		return updateMultiplySQLValues;
	}	

	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
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
				new Port(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
								null,
								null,
								null,
								null,
								0) :
				this.fromStorableObject(storableObject);
		PortType portType;
		try {			
			Identifier portTypeId = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_TYPE_ID);
			portType = (portTypeId != null) ? (PortType)ConfigurationStorableObjectPool.getStorableObject(portTypeId, true) : null;
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}

		String description = DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION));
		port.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
							DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),								  
							DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
							DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),						  
							portType,								  
							(description != null) ? description : "",
							DatabaseIdentifier.getIdentifier(resultSet, COLUMN_EQUIPMENT_ID),
							resultSet.getInt(COLUMN_SORT));
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

	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		try {
			characteristicDatabase.updateCharacteristics(storableObjects);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		Port port = this.fromStorableObject(storableObject);
			switch (updateKind) {
				case UPDATE_CHECK:
					super.checkAndUpdateEntity(port, false);
					break;
				case UPDATE_FORCE:					
				default:
					super.checkAndUpdateEntity(port, true);		
					return;
			}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(port);
	}

	public void update(List storableObjects, int updateKind, Object arg)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		switch (updateKind) {	
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, true);		
				return;
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(storableObjects);
	}

	public List retrieveAll() throws RetrieveObjectException {
		List list = null;
		try {
			list = this.retrieveByIds(null, null);
		}
		catch (IllegalDataException ide) {           
			Log.debugMessage("PortDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
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
			CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
			Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(list, CharacteristicSort.CHARACTERISTIC_SORT_PORT);
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Port port = (Port) iter.next();
				List characteristics = (List)characteristicMap.get(port);
				port.setCharacteristics0(characteristics);
			}
		}
		return list;
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
		throws IllegalDataException, UpdateObjectException {
		Port port = this.fromStorableObject(storableObject);
		Identifier typeId = port.getType().getId();
		Identifier equipmentId = port.getEquipmentId();

		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, typeId);
			DatabaseString.setString(preparedStatement, ++i, port.getDescription(), SIZE_DESCRIPTION_COLUMN);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, equipmentId);
			preparedStatement.setInt(++i, port.getSort());
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	private List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
		List list = null;

		String condition = COLUMN_EQUIPMENT_ID + SQL_IN
			+ OPEN_BRACKET
				+ SQL_SELECT + COLUMN_ID
				+ SQL_FROM + ObjectEntities.EQUIPMENT_ENTITY
				+ SQL_WHERE + DomainMember.COLUMN_DOMAIN_ID + EQUALS + DatabaseIdentifier.toSQLString(domain.getId())
			+ CLOSE_BRACKET;

		try {
			list = retrieveButIds(ids, condition);
		}
		catch (IllegalDataException ide) {           
			Log.debugMessage("PortDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
		}

		return list;
	}

	public List retrieveByCondition(List ids, StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		List list;
		if (condition instanceof DomainCondition) {
			DomainCondition domainCondition = (DomainCondition)condition;
			list = this.retrieveButIdsByDomain(ids, domainCondition.getDomain());
		}
		else {
			Log.errorMessage("PortDatabase.retrieveByCondition | Unknown condition class: " + condition.getClass().getName());
			list = this.retrieveButIds(ids);
		}
		return list;
	}
}
