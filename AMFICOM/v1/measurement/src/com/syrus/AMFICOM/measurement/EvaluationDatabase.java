/*
 * $Id: EvaluationDatabase.java,v 1.14 2004/08/27 12:14:57 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
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

/**
 * @version $Revision: 1.14 $, $Date: 2004/08/27 12:14:57 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class EvaluationDatabase extends StorableObjectDatabase {

	public static final String	COLUMN_TYPE_ID				= "type_id";
	public static final String	COLUMN_MONITORED_ELEMENT_ID	= "monitored_element_id";
	public static final String	COLUMN_THRESHOLD_SET_ID		= "threshold_set_id";

	private Evaluation fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Evaluation)
			return (Evaluation) storableObject;
		throw new IllegalDataException("EvaluationDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		this.retrieveEvaluation(evaluation);
	}
	
	private String retrieveEvaluationQuery(String condition){
		return SQL_SELECT
		+ COLUMN_ID + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA 
		+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
		+ COLUMN_CREATOR_ID + COMMA
		+ COLUMN_MODIFIER_ID + COMMA
		+ COLUMN_TYPE_ID + COMMA
		+ COLUMN_MONITORED_ELEMENT_ID + COMMA
		+ COLUMN_THRESHOLD_SET_ID
		+ SQL_FROM + ObjectEntities.EVALUATION_ENTITY
		+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}
	
	private Evaluation updateEvaluationFromResultSet(Evaluation evaluation, ResultSet resultSet) throws RetrieveObjectException, SQLException{
		Evaluation evaluation1 = evaluation;
		if (evaluation == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			evaluation1 = new Evaluation(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, null);			
		}
		EvaluationType evaluationType;
		Set thresholdSet;
		try {
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			evaluationType = (EvaluationType)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_TYPE_ID)), true);
			/**
			 * @todo when change DB Identifier model ,change getString() to
			 *       getLong()
			 */
			thresholdSet = (Set)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_THRESHOLD_SET_ID)), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		/**
		 * @todo when change DB Identifier model ,change getString() to
		 *       getLong()
		 */
		evaluation1.setAttributes(DatabaseDate.fromQuerySubString(resultSet,COLUMN_CREATED),
														 DatabaseDate.fromQuerySubString(resultSet,COLUMN_MODIFIED),
														 new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
														 new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
														 evaluationType,
														 new Identifier(resultSet.getString(COLUMN_MONITORED_ELEMENT_ID)),
														 thresholdSet);


		return evaluation1;
	}


	private void retrieveEvaluation(Evaluation evaluation) throws ObjectNotFoundException, RetrieveObjectException {
		String evaluationIdStr = evaluation.getId().toSQLString();
		String sql = retrieveEvaluationQuery(COLUMN_ID + EQUALS + evaluationIdStr);
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EvaluationDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) 
				updateEvaluationFromResultSet(evaluation, resultSet);
			else
				throw new ObjectNotFoundException("No such evaluation: " + evaluationIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "EvaluationDatabase.retrieve | Cannot retrieve evaluation '" + evaluationIdStr + "' -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
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
		Evaluation evaluation = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		try {
			this.insertEvaluation(evaluation);
		}
		catch (CreateObjectException e) {
			try {
				connection.rollback();
			}
			catch (SQLException sqle) {
				Log.errorMessage("Exception in rolling back");
				Log.errorException(sqle);
			}
			throw e;
		}
		try {
			connection.commit();
		}
		catch (SQLException sqle) {
			Log.errorMessage("Exception in commiting");
			Log.errorException(sqle);
		}
	}

	public void insertEvaluation(Evaluation evaluation) throws CreateObjectException {
		String evaluationIdStr = evaluation.getId().toSQLString();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.EVALUATION_ENTITY + OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_TYPE_ID + COMMA
			+ COLUMN_MONITORED_ELEMENT_ID + COMMA
			+ COLUMN_THRESHOLD_SET_ID
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ evaluationIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(evaluation.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(evaluation.getModified()) + COMMA
			+ evaluation.getCreatorId().toSQLString() + COMMA
			+ evaluation.getModifierId().toSQLString() + COMMA
			+ evaluation.getType().getId().toSQLString() + COMMA
			+ evaluation.getMonitoredElementId().toSQLString() + COMMA
			+ evaluation.getThresholdSet().getId().toSQLString()
			+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EvaluationDatabase.insert | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "EvaluationDatabase.insert | Cannot insert evaluation '" + evaluationIdStr + "' -- " + sqle.getMessage();
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
		Evaluation evaluation = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				return;
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
			sql = retrieveEvaluationQuery(buffer.toString());
		}
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EvaluationDatabase.retriveByIdsOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				result.add(updateEvaluationFromResultSet(null, resultSet));
			}
		}
		catch (SQLException sqle) {
			String mesg = "EvaluationDatabase.retriveByIdsOneQuery | Cannot execute query " + sqle.getMessage();
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
			
			sql = retrieveEvaluationQuery(buffer.toString());
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
					result.add(updateEvaluationFromResultSet(null, resultSet));
				} else{
					Log.errorMessage("EvaluationDatabase.retriveByIdsPreparedStatement | No such evalution: " + idStr);									
				}
				
			}
		}catch (SQLException sqle) {
			String mesg = "EvaluationDatabase.retriveByIdsPreparedStatement | Cannot retrieve evalution " + sqle.getMessage();
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
