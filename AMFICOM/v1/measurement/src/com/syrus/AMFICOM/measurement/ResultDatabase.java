/*
 * $Id: ResultDatabase.java,v 1.16 2004/08/27 12:14:57 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import oracle.sql.BLOB;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.measurement.corba.ResultSort;

/**
 * @version $Revision: 1.16 $, $Date: 2004/08/27 12:14:57 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class ResultDatabase extends StorableObjectDatabase {
	
//	 measurementId VARCHAR2(32) NOT NULL,
	public static final String COLUMN_MEASUREMENT_ID		=	"measurement_id";
//	 analysisId VARCHAR2(32),
	public static final String COLUMN_ANALYSIS_ID			=	"analysis_id";
//	 evaluationId VARCHAR2(32),
	public static final String COLUMN_EVALUATION_ID			=	"evaluation_id";
//	 sort NUMBER(2, 0) NOT NULL,
	public static final String COLUMN_SORT					=	"sort";
//	 alarmLevel NUMBER(2, 0) NOT NULL,
	public static final String COLUMN_ALARM_LEVEL			=	"alarm_level";
	
	public static final String LINK_COLUMN_TYPE_ID			=	"type_id";
	public static final String LINK_COLUMN_RESULT_ID		=	"result_id";
	public static final String LINK_COLUMN_VALUE			=	"value";

	private Result fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Result)
			return (Result)storableObject;
		throw new IllegalDataException("ResultDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Result result = this.fromStorableObject(storableObject);
		this.retrieveResult(result);
		this.retrieveResultParameters(result);
	}

	private String retrieveSetQuery(String condition){
		return SQL_SELECT
		+ COLUMN_ID + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA 
		+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
		+ COLUMN_CREATOR_ID + COMMA
		+ COLUMN_MODIFIER_ID + COMMA
		+ COLUMN_MEASUREMENT_ID + COMMA
		+ COLUMN_ANALYSIS_ID + COMMA
		+ COLUMN_EVALUATION_ID + COMMA
		+ COLUMN_SORT + COMMA
		+ COLUMN_ALARM_LEVEL
		+ SQL_FROM + ObjectEntities.RESULT_ENTITY
		+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}
	
	private Result updateSetFromResultSet(Result result, ResultSet resultSet) throws RetrieveObjectException, SQLException{
		Result result1 = result;
		if (result == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			result1 = new Result(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, 0, 0, null);
		}
		/**
		 * @todo when change DB Identifier model ,change getString() to getLong()
		 */
		Measurement measurement = null;
		try {
			measurement = (Measurement)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_MEASUREMENT_ID)), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		int resultSort = resultSet.getInt(COLUMN_SORT);
		Action action = null;
		switch (resultSort) {
			case ResultSort._RESULT_SORT_MEASUREMENT:
				action = measurement;
				break;
			case ResultSort._RESULT_SORT_ANALYSIS:
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				try {
					action = (Analysis)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_ANALYSIS_ID)), true);
				}
				catch (Exception e) {
					throw new RetrieveObjectException(e);
				}
				break;
			case ResultSort._RESULT_SORT_EVALUATION:
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				try {
					action = (Evaluation)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_EVALUATION_ID)), true);
				}
				catch (Exception e) {
					throw new RetrieveObjectException(e);
				}
				break;
			default:
				Log.errorMessage("Unkown sort: " + resultSort + " of result " + result1.getId().getCode());
		}
		result1.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
												 DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
												 /**
												  * @todo when change DB Identifier model ,change getString() to getLong()
												  */
												 new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
												 /**
												  * @todo when change DB Identifier model ,change getString() to getLong()
												  */
												 new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
												 measurement,
												 action,
												 resultSort,
												 resultSet.getInt(COLUMN_ALARM_LEVEL));


		return result1;
	}

	
	private void retrieveResult(Result result) throws ObjectNotFoundException, RetrieveObjectException {
		String resultIdStr = result.getId().toSQLString();
		String sql = retrieveSetQuery(COLUMN_ID + EQUALS + resultIdStr);
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("ResultDatabase.retrieveResult | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
			}
			else
				throw new ObjectNotFoundException("No such result: " + resultIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "ResultDatabase.retrieveResult | Cannot retrieve result '" + resultIdStr + "' -- " + sqle.getMessage();
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

	private void retrieveResultParameters(Result result) throws RetrieveObjectException {
		List parameters = new ArrayList();

		String resultIdStr = result.getId().toSQLString();
		String sql = SQL_SELECT
			+ COLUMN_ID + COMMA
			+ LINK_COLUMN_TYPE_ID + COMMA
			+ LINK_COLUMN_VALUE
			+ SQL_FROM
			+ ObjectEntities.RESULTPARAMETER_ENTITY
			+ SQL_WHERE
			+ LINK_COLUMN_RESULT_ID + EQUALS
			+ resultIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("ResultDatabase.retrieveResultParameters | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			SetParameter parameter;
			ParameterType parameterType;
			while (resultSet.next()) {
				try {
					/**
					 * @todo when change DB Identifier model ,change getString() to getLong()
					 */
					parameterType = (ParameterType)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(LINK_COLUMN_TYPE_ID)), true);
				}
				catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
				parameter = new SetParameter(/**
																			* @todo when change DB Identifier model ,change getString() to getLong()
																			*/
																			new Identifier(resultSet.getString(COLUMN_ID)),
																			/**
																				* @todo when change DB Identifier model ,change getString() to getLong()
																				*/
																			parameterType,
																			ByteArrayDatabase.toByteArray((BLOB)resultSet.getBlob(LINK_COLUMN_VALUE)));
				parameters.add(parameter);
			}
		}
		catch (SQLException sqle) {
			String mesg = "ResultDatabase.retrieveResultParameters | Cannot retrieve parameters for result '" + resultIdStr + "' -- " + sqle.getMessage();
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
		result.setParameters((SetParameter[])parameters.toArray(new SetParameter[parameters.size()]));
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Result result = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Result result = this.fromStorableObject(storableObject);
		try {
			this.insertResult(result);
			this.insertResultParameters(result);
		}
		catch (CreateObjectException e) {
			this.delete(result);
			throw e;
		}
	}

	private void insertResult(Result result) throws CreateObjectException {
		String resultIdStr = result.getId().toSQLString();
		String sql;
		{			
			StringBuffer buffer = new StringBuffer(SQL_INSERT_INTO);
			buffer.append(ObjectEntities.RESULT_ENTITY);
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
			buffer.append(COLUMN_MEASUREMENT_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_ANALYSIS_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_EVALUATION_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_SORT);
			buffer.append(COMMA);
			buffer.append(COLUMN_ALARM_LEVEL);
			buffer.append(CLOSE_BRACKET);
			buffer.append(SQL_VALUES);
			buffer.append(OPEN_BRACKET);			
			buffer.append(resultIdStr);
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toUpdateSubString(result.getCreated()));
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toUpdateSubString(result.getModified()));
			buffer.append(COMMA);
			buffer.append(result.getCreatorId().toSQLString());
			buffer.append(COMMA);
			buffer.append(result.getModifierId().toSQLString());
			buffer.append(COMMA);
			buffer.append(result.getMeasurement().getId().toSQLString());
			buffer.append(COMMA);
		int resultSort = result.getSort().value();
		switch (resultSort) {
			case ResultSort._RESULT_SORT_MEASUREMENT:				
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);				
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				break;
			case ResultSort._RESULT_SORT_ANALYSIS:
				buffer.append(result.getAction().getId().toSQLString());
				buffer.append(COMMA);				
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				break;
			case ResultSort._RESULT_SORT_EVALUATION:				
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(result.getAction().getId().toSQLString());
				buffer.append(COMMA);
				break;
			default:
				Log.errorMessage("ResultDatabase.insertResult | Illegal sort: " + resultSort + " of result '" + resultIdStr + "'");
		}
			buffer.append(Integer.toString(resultSort));
			buffer.append(COMMA);
			buffer.append(Integer.toString(result.getAlarmLevel().value()));
			buffer.append(CLOSE_BRACKET);
			sql = buffer.toString();
		}
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("ResultDatabase.insertResult | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "ResultDatabase.insertResult | Cannot insert result '" + resultIdStr + "' -- " + sqle.getMessage();
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

	private void insertResultParameters(Result result) throws CreateObjectException {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String resultIdCode = result.getId().getCode();
		SetParameter[] setParameters = result.getParameters();
		String sql = SQL_INSERT_INTO 
			+ ObjectEntities.RESULTPARAMETER_ENTITY
			+ OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ LINK_COLUMN_TYPE_ID + COMMA
			+ LINK_COLUMN_RESULT_ID + COMMA
			+ LINK_COLUMN_VALUE
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION 
			+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		int i = 0;
		Identifier parameterId = null;
		Identifier parameterTypeId = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (i = 0; i < setParameters.length; i++) {
				parameterId = setParameters[i].getId();
				parameterTypeId = setParameters[i].getType().getId();
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(1, parameterId.getCode());
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(2, parameterTypeId.getCode());
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(3, resultIdCode);
				preparedStatement.setBlob(4, BLOB.empty_lob());
				Log.debugMessage("ResultDatabase.insertResultParameters | Inserting parameter " + parameterTypeId.toString() + " for result " + resultIdCode, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
//				ByteArrayDatabase badb = new ByteArrayDatabase(setParameters[i].getValue());
				ByteArrayDatabase.saveAsBlob(setParameters[i].getValue(),
																		 connection,
																		 ObjectEntities.RESULTPARAMETER_ENTITY,
																		 LINK_COLUMN_VALUE,
																		 COLUMN_ID + EQUALS + parameterId.toSQLString());
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "ResultDatabase.insertResultParameters | Cannot insert parameter '" + parameterId.toString() + "' of type '" + parameterTypeId.toString() + "' for result '" + resultIdCode + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, UpdateObjectException {
		Result result = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				return;
		}
	}

	private void delete(Result result) {
		String resultIdStr = result.getId().toSQLString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM 
									+ ObjectEntities.RESULTPARAMETER_ENTITY
									+ SQL_WHERE
									+ LINK_COLUMN_RESULT_ID
									+ EQUALS
									+ resultIdStr);
			statement.executeUpdate(SQL_DELETE_FROM 
									+ ObjectEntities.RESULT_ENTITY
									+ SQL_WHERE
									+ COLUMN_ID
									+ EQUALS
									+ resultIdStr);
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
			return new LinkedList();
		return retriveByIdsOneQuery(ids);	
		//return retriveByIdsPreparedStatement(ids);
	}
	
	private List retriveByIdsOneQuery(List ids) throws RetrieveObjectException {
		List result = new LinkedList();
		String sql;
		{
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
			}
			sql = retrieveSetQuery(buffer.toString());
		}
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("ResultDatabase.retriveByIdsOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				result.add(updateSetFromResultSet(null, resultSet));
			}
		}
		catch (SQLException sqle) {
			String mesg = "ResultDatabase.retriveByIdsOneQuery | Cannot execute query " + sqle.getMessage();
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
			
			sql =retrieveSetQuery(buffer.toString());
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
					result.add(updateSetFromResultSet(null, resultSet));
				} else{
					Log.errorMessage("ResultDatabase.retriveByIdsPreparedStatement | No such result: " + idStr);									
				}
				
			}
		}catch (SQLException sqle) {
			String mesg = "ResultDatabase.retriveByIdsPreparedStatement | Cannot retrieve result " + sqle.getMessage();
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
