/*
 * $Id: EvaluationTypeDatabase.java,v 1.13 2004/08/10 19:05:19 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.13 $, $Date: 2004/08/10 19:05:19 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class EvaluationTypeDatabase extends StorableObjectDatabase {

	public static final String	MODE_IN = "IN";
	public static final String	MODE_THRESHOLD = "THS";
	public static final String	MODE_ETALON = "ETA";
	public static final String	MODE_OUT = "OUT";

	public static final String	COLUMN_CODENAME = "codename";
	public static final String	COLUMN_DESCRIPTION = "description";

	public static final String	LINK_COLUMN_EVALUATION_TYPE_ID = "evaluation_type_id";

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private EvaluationType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof EvaluationType)
			return (EvaluationType) storableObject;
		throw new IllegalDataException("EvaluationTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		this.retrieveEvaluationType(evaluationType);
		this.retrieveParameterTypes(evaluationType);
	}

	private void retrieveEvaluationType(EvaluationType evaluationType) throws ObjectNotFoundException, RetrieveObjectException {
		String evaluationTypeIdStr = evaluationType.getId().toSQLString();
		String sql = SQL_SELECT
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_CODENAME + COMMA
			+ COLUMN_DESCRIPTION
			+ SQL_FROM + ObjectEntities.EVALUATIONTYPE_ENTITY
			+ SQL_WHERE + COLUMN_ID + EQUALS + evaluationTypeIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EvaluationTypeDatabase.retrieveEvaluationType | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				evaluationType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
																		 DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
																			new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
																			new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
																			resultSet.getString(COLUMN_CODENAME),
																			resultSet.getString(COLUMN_DESCRIPTION));
			else
				throw new ObjectNotFoundException("No such evaluation type: " + evaluationTypeIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "EvaluationTypeDatabase.retrieveEvaluationType | Cannot retrieve evaluation type " + evaluationTypeIdStr;
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

	private void retrieveParameterTypes(EvaluationType evaluationType) throws RetrieveObjectException {	
		List inParTyps = new ArrayList();
		List thresholdParTyps = new ArrayList();
		List etalonParTyps = new ArrayList();
		List outParTyps = new ArrayList();

		String evaluationTypeIdStr = evaluationType.getId().toSQLString();
		String sql = SQL_SELECT
			+ LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
			+ LINK_COLUMN_PARAMETER_MODE
			+ SQL_FROM + ObjectEntities.EVATYPPARTYPLINK_ENTITY
			+ SQL_WHERE + LINK_COLUMN_EVALUATION_TYPE_ID + EQUALS + evaluationTypeIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage(
					"EvaluationTypeDatabase.retrieveParameterTypes | Trying: "
							+ sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			String parameterMode;
			/**
			 * @todo when change DB Identifier model ,change String to long
			 */
			String parameterTypeIdCode;
			while (resultSet.next()) {
				parameterMode = resultSet.getString(LINK_COLUMN_PARAMETER_MODE);
				/**
				 * @todo when change DB Identifier model ,change setString() to
				 *       setLong()
				 */
				parameterTypeIdCode = resultSet.getString(LINK_COLUMN_PARAMETER_TYPE_ID);
				if (parameterMode.equals(MODE_IN))
					inParTyps.add(new Identifier(parameterTypeIdCode));
					else
						if (parameterMode.equals(MODE_THRESHOLD))
							thresholdParTyps.add(new Identifier(parameterTypeIdCode));
						else
							if (parameterMode.equals(MODE_ETALON))
								etalonParTyps.add(new Identifier(parameterTypeIdCode));
							else
								if (parameterMode.equals(MODE_OUT))
									outParTyps.add(new Identifier(parameterTypeIdCode));
								else
									Log.errorMessage("EvaluationTypeDatabase.retrieveParameterTypes | ERROR: Unknown parameter mode for parameterTypeId " + parameterTypeIdCode);
			}
		}
		catch (SQLException sqle) {
			String mesg = "EvaluationTypeDatabase.retrieveParameterTypes | Cannot retrieve parameter types for evaluation type " + evaluationTypeIdStr;
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
		((ArrayList)inParTyps).trimToSize();
		((ArrayList)thresholdParTyps).trimToSize();
		((ArrayList)etalonParTyps).trimToSize();
		((ArrayList)outParTyps).trimToSize();
		evaluationType.setParameterTypes(inParTyps,
																		 thresholdParTyps,
																		 etalonParTyps,
																		 outParTyps);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		try {
			this.insertEvaluationType(evaluationType);
			this.insertParameterTypes(evaluationType);
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

	private void insertEvaluationType(EvaluationType evaluationType) throws CreateObjectException {
		String evaluationTypeIdStr = evaluationType.getId().toSQLString();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.EVALUATIONTYPE_ENTITY + OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_CODENAME + COMMA
			+ COLUMN_DESCRIPTION
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ evaluationTypeIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(evaluationType.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(evaluationType.getModified()) + COMMA
			+ evaluationType.getCreatorId().toSQLString() + COMMA
			+ evaluationType.getModifierId().toSQLString() + COMMA
			+ APOSTOPHE + evaluationType.getCodename() + APOSTOPHE + COMMA
			+ APOSTOPHE + evaluationType.getDescription() + APOSTOPHE
			+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage(
					"EvaluationTypeDatabase.insertEvaluationType | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "EvaluationTypeDatabase.insertEvaluationType | Cannot insert evaluation type " + evaluationTypeIdStr;
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

	private void insertParameterTypes(EvaluationType evaluationType) throws CreateObjectException {
		List inParTyps = evaluationType.getInParameterTypes();
		List thresholdParTyps = evaluationType.getThresholdParameterTypes();
		List etalonParTyps = evaluationType.getEtalonParameterTypes();
		List outParTyps = evaluationType.getOutParameterTypes();
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String evaluationTypeIdCode = evaluationType.getId().getCode();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.EVATYPPARTYPLINK_ENTITY + OPEN_BRACKET
			+ LINK_COLUMN_EVALUATION_TYPE_ID + COMMA
			+ LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
			+ LINK_COLUMN_PARAMETER_MODE
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION
			+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String parameterTypeIdCode = null;
		String parameterMode = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = inParTyps.iterator(); iterator.hasNext();) {
				preparedStatement.setString(1, evaluationTypeIdCode);
				parameterTypeIdCode = ((Identifier) iterator.next()).getCode();
				preparedStatement.setString(2, parameterTypeIdCode);
				parameterMode = MODE_IN;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("EvaluationTypeDatabase.insertParameterTypes | Inserting parameter type "
								+ parameterTypeIdCode
								+ " of parameter mode '" + parameterMode
								+ "' for evaluation type "
								+ evaluationTypeIdCode, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = thresholdParTyps.iterator(); iterator.hasNext();) {
				preparedStatement.setString(1, evaluationTypeIdCode);
				parameterTypeIdCode = ((Identifier) iterator.next()).getCode();
				preparedStatement.setString(2, parameterTypeIdCode);
				parameterMode = MODE_THRESHOLD;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("EvaluationTypeDatabase.insertParameterTypes | Inserting parameter type "
								+ parameterTypeIdCode
								+ " of parameter mode '" + parameterMode
								+ "' for evaluation type "
								+ evaluationTypeIdCode, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = etalonParTyps.iterator(); iterator.hasNext();) {
				preparedStatement.setString(1, evaluationTypeIdCode);
				parameterTypeIdCode = ((Identifier) iterator.next()).getCode();
				preparedStatement.setString(2, parameterTypeIdCode);
				parameterMode = MODE_ETALON;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("EvaluationTypeDatabase.insertParameterTypes | Inserting parameter type "
								+ parameterTypeIdCode
								+ " of parameter mode '" + parameterMode
								+ "' for evaluation type "
								+ evaluationTypeIdCode, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = outParTyps.iterator(); iterator.hasNext();) {
				preparedStatement.setString(1, evaluationTypeIdCode);
				parameterTypeIdCode = ((Identifier) iterator.next()).getCode();
				preparedStatement.setString(2, parameterTypeIdCode);
				parameterMode = MODE_OUT;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("EvaluationTypeDatabase.insertParameterTypes | Inserting parameter type "
								+ parameterTypeIdCode
								+ " of parameter mode '" + parameterMode
								+ "' for evaluation type "
								+ evaluationTypeIdCode, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			String mesg = "EvaluationTypeDatabase.insertParameterTypes | Cannot insert parameter type "
					+ parameterTypeIdCode
					+ " of parameter mode '"
					+ parameterMode
					+ "' for evaluation type "
					+ evaluationTypeIdCode;
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

	public void update(StorableObject storableObject, int updateKind, Object obj)
			throws IllegalDataException, UpdateObjectException {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				return;
		}
	}

	public void delete(EvaluationType evaluationType) {
		String evaluationTypeIdStr = evaluationType.getId().toSQLString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.EVATYPPARTYPLINK_ENTITY
					+ SQL_WHERE + LINK_COLUMN_EVALUATION_TYPE_ID + EQUALS + evaluationTypeIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.EVALUATIONTYPE_ENTITY
					+ SQL_WHERE + COLUMN_ID + EQUALS + evaluationTypeIdStr);
			connection.commit();
		}
		catch (SQLException sqle1) {
			Log.errorException(sqle1);
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

	public static EvaluationType retrieveForCodename(String codename) throws ObjectNotFoundException, RetrieveObjectException {
		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM + ObjectEntities.EVALUATIONTYPE_ENTITY
			+ SQL_WHERE + COLUMN_CODENAME + EQUALS + APOSTOPHE + codename + APOSTOPHE;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EvaluationTypeDatabase.retrieveForCodename | Trying: "+ sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				return new EvaluationType(new Identifier(resultSet.getString(COLUMN_ID)));
			} 
			throw new ObjectNotFoundException("No evaluation type with codename: '" + codename + "'");
		}
		catch (SQLException sqle) {
			String mesg = "EvaluationTypeDatabase.retrieveForCodename | Cannot retrieve evaluation type with codename: '" + codename + "'";
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
	
	public static List retrieveAll() throws RetrieveObjectException {
		List evaluationTypes = new ArrayList(CHARACTER_NUMBER_OF_RECORDS);
		String sql = SQL_SELECT
				+ COLUMN_ID
				+ SQL_FROM + ObjectEntities.EVALUATIONTYPE_ENTITY;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EvaluationTypeDatabase.retrieveAll | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				evaluationTypes.add(new EvaluationType(new Identifier(resultSet.getString(COLUMN_ID))));			
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
		}
		catch (SQLException sqle) {
			String mesg = "EvaluationTypeDatabase.retrieveAll | Cannot retrieve evaluation type";
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
		return evaluationTypes;
	}
}
