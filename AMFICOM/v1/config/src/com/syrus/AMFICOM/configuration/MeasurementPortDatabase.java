/*
 * $Id: MeasurementPortDatabase.java,v 1.10 2004/08/30 09:53:43 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
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
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;


/**
 * @version $Revision: 1.10 $, $Date: 2004/08/30 09:53:43 $
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
    
	private MeasurementPort fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MeasurementPort)
			return (MeasurementPort)storableObject;
		throw new IllegalDataException("MeasurementPortDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementPort measurementPort = this.fromStorableObject(storableObject);
		this.retrieveMeasurementPort(measurementPort);	
	}
	
	private String retrieveMeasurementPortQuery(String condition){
		StringBuffer buffer = new StringBuffer(SQL_SELECT);
		buffer.append(COLUMN_ID);
		buffer.append(COMMA);
		buffer.append(DatabaseDate.toQuerySubString(COLUMN_CREATED));
		buffer.append(COMMA);
		buffer.append(DatabaseDate.toQuerySubString(COLUMN_MODIFIED));
		buffer.append(COMMA);
		buffer.append(COLUMN_CREATOR_ID);
		buffer.append(COMMA);
		buffer.append(COLUMN_MODIFIER_ID);
		buffer.append(COMMA);
		buffer.append(COLUMN_TYPE_ID);
		buffer.append(COMMA);
		buffer.append(COLUMN_NAME);
		buffer.append(COMMA);
		buffer.append(COLUMN_DESCRIPTION);
		buffer.append(COMMA);
		buffer.append(COLUMN_KIS_ID);
		buffer.append(COMMA);
		buffer.append(COLUMN_PORT_ID);
		buffer.append(SQL_FROM);
		buffer.append(ObjectEntities.MEASUREMENTPORT_ENTITY);
		if ((condition!=null) && (condition.length()>0)){
			buffer.append(SQL_WHERE);
			buffer.append(condition);
		}
		return buffer.toString();

	}
	
	private MeasurementPort updateMeasurementPortFromResultSet(MeasurementPort measurementPort, ResultSet resultSet) throws RetrieveObjectException, SQLException{
		MeasurementPort measurementPort1 = measurementPort;
		if (measurementPort1 == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			measurementPort1 = new MeasurementPort(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, null, null, null);			
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
		measurementPort1.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
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
		return measurementPort1;
	}
	


	private void retrieveMeasurementPort(MeasurementPort measurementPort) throws ObjectNotFoundException, RetrieveObjectException{
		
		String mpIdStr = measurementPort.getId().toSQLString();
		String sql = retrieveMeasurementPortQuery(COLUMN_ID + EQUALS + mpIdStr);
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementPortDatabase.retrieveMeasurementPort | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) 
				updateMeasurementPortFromResultSet(measurementPort, resultSet);
			else
				throw new ObjectNotFoundException("No such measurement port: " + mpIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementPortDatabase.retrieveMeasurementPort | Cannot retrieve measurement port " + mpIdStr;
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementPort measurementPort = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		MeasurementPort measurementPort = this.fromStorableObject(storableObject);
		
		try {
			this.insertMeasurementPort(measurementPort);			
		} catch (CreateObjectException e) {
			try {
				connection.rollback();
			} catch (SQLException sqle) {
				Log.errorMessage("Exception in rolling back");
				Log.errorException(sqle);
			}
			throw e;
		}
		try {
			connection.commit();
		} catch (SQLException sqle) {
			Log.errorMessage("Exception in commiting");
			Log.errorException(sqle);
		}
		
	}

	private void insertMeasurementPort(MeasurementPort measurementPort) throws CreateObjectException {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String mpIdCode = measurementPort.getId().toSQLString();	

		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		Identifier typeId = measurementPort.getType().getId();

		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		Identifier kisId = measurementPort.getKISId();
		
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		Identifier portId = measurementPort.getPortId();
		
		String sql = null;
		{
			StringBuffer buffer = new StringBuffer(SQL_INSERT_INTO);
			buffer.append(ObjectEntities.MEASUREMENTPORT_ENTITY);
			buffer.append(OPEN_BRACKET);
			buffer.append(COLUMN_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_CREATED); 
			buffer.append(COMMA);
			buffer.append(COLUMN_MODIFIED);
			buffer.append(COMMA);
			buffer.append(COLUMN_CREATOR_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_MODIFIER_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_TYPE_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_NAME);
			buffer.append(COMMA);
			buffer.append(COLUMN_DESCRIPTION);
			buffer.append(COMMA);
			buffer.append(COLUMN_KIS_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_PORT_ID);
			buffer.append(CLOSE_BRACKET);
			buffer.append(SQL_VALUES);
			buffer.append(OPEN_BRACKET);
			buffer.append(mpIdCode);
			buffer.append(COMMA);			
			buffer.append(DatabaseDate.toUpdateSubString(measurementPort.getCreated()));
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toUpdateSubString(measurementPort.getModified()));
			buffer.append(COMMA);
			buffer.append(measurementPort.getCreatorId().toSQLString());
			buffer.append(COMMA);
			buffer.append(measurementPort.getModifierId().toSQLString());
			buffer.append(COMMA);
			buffer.append((typeId != null)?typeId.toSQLString():Identifier.getNullSQLString());
			buffer.append(COMMA);
			buffer.append(APOSTOPHE);
			buffer.append(measurementPort.getName());
			buffer.append(APOSTOPHE);
			buffer.append(COMMA);
			buffer.append(APOSTOPHE);
			buffer.append(measurementPort.getDescription());
			buffer.append(APOSTOPHE);
			buffer.append(COMMA);
			buffer.append((kisId != null)?kisId.toSQLString():Identifier.getNullSQLString());
			buffer.append(COMMA);
			buffer.append((portId != null)?portId.toSQLString():Identifier.getNullSQLString());
			buffer.append(CLOSE_BRACKET);
			sql = buffer.toString();
		}
		
		Statement statement = null;
		try {
			statement = connection.createStatement();										
			Log.debugMessage("MeasurementPortDatabase.insertMeasurementPort | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementPortDatabase.insertMeasurementPort | Cannot insert measurement port " + mpIdCode;
			throw new CreateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, UpdateObjectException {
		MeasurementPort measurementPort = this.fromStorableObject(storableObject);
			switch (updateKind) {
				default:
					return;
			}
	}
	
	public List retrieveAll() throws RetrieveObjectException {		
		return retriveByIdsOneQuery(null);
	}

	public void delete(MeasurementPort measurementPort) {
		String mpIdStr = measurementPort.getId().toSQLString();
		Statement statement = null;
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
			}
		}
	}

	public List retrieveByIds(List ids) throws RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return retriveByIdsOneQuery(null);
		return retriveByIdsOneQuery(ids);	
		//return retriveByIdsPreparedStatement(ids);
	}
	
	private List retriveByIdsOneQuery(List ids) throws RetrieveObjectException {
		List result = new LinkedList();
		String sql;
		{
			String condition = null;
			if (ids!=null){
				StringBuffer buffer = new StringBuffer(COLUMN_ID);
				int idsLength = ids.size();
				if (idsLength == 1){
					buffer.append(EQUALS);
					buffer.append(((Identifier)ids.iterator().next()).toSQLString());
				} else{
					buffer.append(SQL_IN);
					buffer.append(OPEN_BRACKET);
					
					int i = 1;
					for(Iterator it=ids.iterator();it.hasNext();i++){
						Identifier id = (Identifier)it.next();
						buffer.append(id.toSQLString());
						if (i < idsLength)
							buffer.append(COMMA);
					}
					
					buffer.append(CLOSE_BRACKET);
					condition = buffer.toString();
				}
			}
			sql = retrieveMeasurementPortQuery(condition);
		}
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementPortDatabase.retriveByIdsOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				result.add(updateMeasurementPortFromResultSet(null, resultSet));
			}
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementPortDatabase.retriveByIdsOneQuery | Cannot execute query " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
		return result;
	}
	
	private List retriveByIdsPreparedStatement(List ids) throws RetrieveObjectException {
		List result = new LinkedList();
		String sql;
		{
			
			int idsLength = ids.size();
			if (idsLength == 1){
				return retriveByIdsOneQuery(ids);
			}
			StringBuffer buffer = new StringBuffer(COLUMN_ID);
			buffer.append(EQUALS);							
			buffer.append(QUESTION);
			
			sql = retrieveMeasurementPortQuery(buffer.toString());
		}
			
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		try {
			stmt = connection.prepareStatement(sql.toString());
			for(Iterator it = ids.iterator();it.hasNext();){
				Identifier id = (Identifier)it.next(); 
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				String idStr = id.getIdentifierString();
				stmt.setString(1, idStr);
				resultSet = stmt.executeQuery();
				if (resultSet.next()){
					result.add(updateMeasurementPortFromResultSet(null, resultSet));
				} else{
					Log.errorMessage("MeasurementPortDatabase.retriveByIdsPreparedStatement | No such measurement port: " + idStr);									
				}
				
			}
		}catch (SQLException sqle) {
			String mesg = "MeasurementPortDatabase.retriveByIdsPreparedStatement | Cannot retrieve measurement port " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				if (stmt != null)
					stmt.close();
				if (stmt != null)
					stmt.close();
				stmt = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}			
		
		return result;
	}

}
