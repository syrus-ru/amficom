/*
 * $Id: MeasurementPortDatabase.java,v 1.17 2004/10/03 12:43:06 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;


/**
 * @version $Revision: 1.17 $, $Date: 2004/10/03 12:43:06 $
 * @author $Author: bob $
 * @module configuration_v1
 */
public class MeasurementPortDatabase extends StorableObjectDatabase {
	// table :: MeasurementPort

    // type_id VARCHAR2(32) NOT NULL,
    public static final String COLUMN_TYPE_ID       = "type_id";
    // name VARCHAR2(64) NOT NULL,
    public static final String COLUMN_NAME  = "name";
	// description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
    // kis_id VARCHAR2(32),
    public static final String COLUMN_KIS_ID        = "kis_id";
    // port_id VARCHAR2(32),
    public static final String COLUMN_PORT_ID       = "port_id";
    
    public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
    
    private String updateColumns;
	private String updateMultiplySQLValues;
	
    protected String getEnityName() {
		return "MeasurementPort";
	}
	
	protected String getTableName() {
		return ObjectEntities.MEASUREMENTPORT_ENTITY;
	}
	
	protected String getUpdateColumns() {
		if (this.updateColumns == null){
    		this.updateColumns = super.getUpdateColumns() + COMMA
				+ COLUMN_TYPE_ID + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_KIS_ID + COMMA
				+ COLUMN_PORT_ID;
		}
		return this.updateColumns; 
	}
	
	protected String getUpdateMultiplySQLValues() {
		if (this.updateMultiplySQLValues == null){
    		this.updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
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
		MeasurementPort measurementPort = fromStorableObject(storableObject);
		Identifier typeId = measurementPort.getType().getId();
		Identifier kisId = measurementPort.getKISId();
		Identifier portId = measurementPort.getPortId();
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
				+ ((typeId != null)?typeId.toSQLString():Identifier.getNullSQLString())
				+ COMMA
				+ APOSTOPHE + measurementPort.getName() + APOSTOPHE	+ COMMA
				+ APOSTOPHE + measurementPort.getDescription() + APOSTOPHE + COMMA
				+ ((kisId != null)?kisId.toSQLString():Identifier.getNullSQLString()) 
				+ COMMA
				+ ((portId != null)?portId.toSQLString():Identifier.getNullSQLString());
		return sql;
	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement) throws IllegalDataException,
			UpdateObjectException {
		MeasurementPort measurementPort = fromStorableObject(storableObject);
		Identifier typeId = measurementPort.getType().getId();
		Identifier kisId = measurementPort.getKISId();
		Identifier portId = measurementPort.getPortId();
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
			preparedStatement.setString( ++i, (typeId != null)?typeId.toString():"");
			preparedStatement.setString( ++i, measurementPort.getName());
			preparedStatement.setString( ++i, measurementPort.getDescription());
			preparedStatement.setString( ++i, (kisId != null)?kisId.toString():""); 
			preparedStatement.setString( ++i, (portId != null)?portId.toString():"");
		}catch (SQLException sqle) {
			throw new UpdateObjectException("MeasurmentPortDatabase." +
					"setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
    private MeasurementPort fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MeasurementPort)
			return (MeasurementPort)storableObject;
		throw new IllegalDataException("MeasurementPortDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementPort measurementPort = this.fromStorableObject(storableObject);
		super.retrieveEntity(measurementPort);	
	}
	
	protected String retrieveQuery(String condition){
		return super.retrieveQuery(condition) + COMMA
			+ COLUMN_TYPE_ID +	COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_KIS_ID + COMMA
			+ COLUMN_PORT_ID 
			+ SQL_FROM + ObjectEntities.MEASUREMENTPORT_ENTITY
			+ (((condition == null) || (condition.length() == 0)) ? "" : SQL_WHERE + condition);
	}
	
	public List retrieveByIds(List ids, String condition)
			throws IllegalDataException, RetrieveObjectException {
		List list = null;
		if ((ids == null) || (ids.isEmpty()))
			list = super.retrieveByIdsOneQuery(null, condition);
		else 
			list = super.retrieveByIdsOneQuery(ids, condition);		
		return list;	
		//return retriveByIdsPreparedStatement(ids);
	}
		
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		MeasurementPort measurementPort = storableObject == null ? null : fromStorableObject(storableObject);
		if (measurementPort == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			measurementPort = new MeasurementPort(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, null, null, null);			
		}
		MeasurementPortType measurementPortType;
		try {
			/**
			 * @todo when change DB Identifier model ,change String to long
			 */
			String measurementPortTypeIdCode = resultSet.getString(COLUMN_TYPE_ID);
			measurementPortType = (measurementPortTypeIdCode != null) ? (MeasurementPortType)ConfigurationStorableObjectPool.getStorableObject(new Identifier(measurementPortTypeIdCode), true) : null;
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		
		String name = resultSet.getString(COLUMN_NAME);
		
		String description = resultSet.getString(COLUMN_DESCRIPTION);
		measurementPort.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
						  DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),								  
						  
						  /**
							* @todo when change DB Identifier model ,change getString() to getLong()
							*/												
						  new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
						  /**
							* @todo when change DB Identifier model ,change getString() to getLong()
							*/
						  new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
						  
						  measurementPortType,
						  (name != null) ? name : "",
						  (description != null) ? description : "",
						  /**
							* @todo when change DB Identifier model ,change getString() to getLong()
							*/
						  new Identifier(resultSet.getString(COLUMN_KIS_ID)),
						  /**
							* @todo when change DB Identifier model ,change getString() to getLong()
							*/
						  new Identifier(resultSet.getString(COLUMN_PORT_ID)));
		return measurementPort;
	}
	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementPort measurementPort = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, 
			CreateObjectException {
		MeasurementPort measurementPort = this.fromStorableObject(storableObject);
		super.insertEntity(measurementPort);		
	}
	
	public void insert(List storableObjects) throws IllegalDataException,
			CreateObjectException {
		super.insertEntities(storableObjects);
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
            Log.debugMessage("MeasurementPortDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
            throw new RetrieveObjectException(ide);
        }
        return list;
    }

	public void delete(MeasurementPort measurementPort) {
		String mpIdStr = measurementPort.getId().toSQLString();
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
        try {
			statement = connection.createStatement();
			String sql = SQL_DELETE_FROM
						+ ObjectEntities.MEASUREMENTPORT_ENTITY
						+ SQL_WHERE
						+ COLUMN_ID + EQUALS
						+ mpIdStr;
			Log.debugMessage("MeasurementPortDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle1) {
			Log.errorException(sqle1);
		}
		finally {
			try {
				if(statement != null)
					statement.close();
				statement = null;
			}
			catch(SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
                DatabaseConnection.closeConnection(connection);
            }
		}
	}
	
	public List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
        List list = null;
        
        String condition = COLUMN_KIS_ID + SQL_IN + OPEN_BRACKET
				+ SQL_SELECT + COLUMN_ID + SQL_FROM + ObjectEntities.KIS_ENTITY 
				+ SQL_WHERE + DomainMember.COLUMN_DOMAIN_ID + EQUALS + domain.getId().toSQLString() 
			+ CLOSE_BRACKET;
        
        try {
            list = retrieveButIds(ids, condition);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage("MeasurementPortDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
        }
        
        return list;
    }
}
