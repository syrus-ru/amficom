/*
 * $Id: EvaluationDatabase.java,v 1.11 2004/08/17 14:58:58 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;

/**
 * @version $Revision: 1.11 $, $Date: 2004/08/17 14:58:58 $
 * @author $Author: arseniy $
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

	private void retrieveEvaluation(Evaluation evaluation) throws ObjectNotFoundException, RetrieveObjectException {
		String evaluationIdStr = evaluation.getId().toSQLString();
		String sql = SQL_SELECT
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA 
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_TYPE_ID + COMMA
			+ COLUMN_MONITORED_ELEMENT_ID + COMMA
			+ COLUMN_THRESHOLD_SET_ID
			+ SQL_FROM + ObjectEntities.EVALUATION_ENTITY
			+ SQL_WHERE + COLUMN_ID + EQUALS + evaluationIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EvaluationDatabase.retrieve | Trying: " + sql,
					Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				EvaluationType evaluationType = (EvaluationType)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_TYPE_ID)), true);
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				Set thresholdSet = (Set)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_THRESHOLD_SET_ID)), true);
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				evaluation.setAttributes(DatabaseDate.fromQuerySubString(resultSet,COLUMN_CREATED),
																 DatabaseDate.fromQuerySubString(resultSet,COLUMN_MODIFIED),
																 new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
																 new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
																 evaluationType,
																 new Identifier(resultSet.getString(COLUMN_MONITORED_ELEMENT_ID)),
																 thresholdSet);
			}
			else
				throw new ObjectNotFoundException("No such evaluation: " + evaluationIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "EvaluationDatabase.retrieve | Cannot retrieve evaluation '" + evaluationIdStr + "' -- " + sqle.getMessage();
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
			Log.debugMessage("EvaluationDatabase.insert | Trying: " + sql, Log.DEBUGLEVEL05);
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
}
