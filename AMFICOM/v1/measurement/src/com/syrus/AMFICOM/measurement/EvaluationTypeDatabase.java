package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Iterator;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

public class EvaluationTypeDatabase extends StorableObjectDatabase {

	public static final String	MODE_IN							= "IN";
	public static final String	MODE_THRESHOLD					= "THS";
	public static final String	MODE_ETALON						= "ETA";
	public static final String	MODE_OUT						= "OUT";

	public static final String	COLUMN_CODENAME					= "codename";
	public static final String	COLUMN_DESCRIPTION				= "description";

	public static final String	LINK_COLUMN_EVALUATION_TYPE_ID	= "evaluationTypeId";

	private EvaluationType fromStorableObject(StorableObject storableObject)
			throws Exception {
		if (storableObject instanceof EvaluationType)
			return (EvaluationType) storableObject;
		else
			throw new Exception(
					"EvaluationTypeDatabase.fromStorableObject | Illegal Storable Object: "
							+ storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		this.retrieveEvaluationType(evaluationType);
		this.retrieveParameterTypes(evaluationType);
	}

	private void retrieveEvaluationType(EvaluationType evaluationType)
			throws Exception {
		String evaluationTypeIdStr = evaluationType.getId().toSQLString();
		String sql;
		{
			StringBuffer buffer = new StringBuffer(SQL_SELECT);
			buffer.append(DatabaseDate.toQuerySubString(COLUMN_CREATED));
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toQuerySubString(COLUMN_MODIFIED));
			buffer.append(COMMA);
			buffer.append(COLUMN_CREATOR_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_MODIFIER_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_CODENAME);
			buffer.append(COMMA);
			buffer.append(COLUMN_DESCRIPTION);
			buffer.append(SQL_FROM);
			buffer.append(ObjectEntities.EVALUATIONTYPE_ENTITY);
			buffer.append(SQL_WHERE);
			buffer.append(COLUMN_ID);
			buffer.append(EQUALS);
			buffer.append(evaluationTypeIdStr);
			sql = buffer.toString();
		}
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage(
					"EvaluationTypeDatabase.retrieveEvaluationType | Trying: "
							+ sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				evaluationType.setAttributes(DatabaseDate.fromQuerySubString(
						resultSet, COLUMN_CREATED), DatabaseDate
						.fromQuerySubString(resultSet, COLUMN_MODIFIED),
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
						resultSet.getString(COLUMN_CODENAME), resultSet
								.getString(COLUMN_DESCRIPTION));
			else
				throw new Exception("No such evaluation type: "
						+ evaluationTypeIdStr);
		} catch (SQLException sqle) {
			String mesg = "EvaluationTypeDatabase.retrieveEvaluationType | Cannot retrieve evaluation type "
					+ evaluationTypeIdStr;
			throw new Exception(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			} catch (SQLException sqle1) {
			}
		}
	}

	private void retrieveParameterTypes(EvaluationType evaluationType)
			throws Exception {	

		 ArrayList inParTyps = new ArrayList();
		 ArrayList thresholdParTyps = new ArrayList();
		 ArrayList etalonParTyps = new ArrayList();
		 ArrayList outParTyps = new ArrayList();
		
		String evaluationTypeIdStr = evaluationType.getId().toSQLString();
		String sql;
		{
			StringBuffer buffer = new StringBuffer(SQL_SELECT);
			buffer.append(LINK_COLUMN_PARAMETER_TYPE_ID);
			buffer.append(COMMA);
			buffer.append(LINK_COLUMN_PARAMETER_MODE);
			buffer.append(SQL_FROM);
			buffer.append(ObjectEntities.EVATYPPARTYPLINK_ENTITY);
			buffer.append(SQL_WHERE);
			buffer.append(LINK_COLUMN_EVALUATION_TYPE_ID);
			buffer.append(EQUALS);
			buffer.append(evaluationTypeIdStr);
			sql = buffer.toString();
		}
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
				parameterMode = resultSet
						.getString(LINK_COLUMN_PARAMETER_MODE);
				/**
				 * @todo when change DB Identifier model ,change setString() to
				 *       setLong()
				 */
				parameterTypeIdCode = resultSet
						.getString(LINK_COLUMN_PARAMETER_TYPE_ID);
				if (parameterMode.equals(MODE_IN))
					inParTyps.add(new Identifier(parameterTypeIdCode));
				else if (parameterMode.equals(MODE_THRESHOLD))
					thresholdParTyps.add(new Identifier(
							parameterTypeIdCode));
				else if (parameterMode.equals(MODE_ETALON))
					etalonParTyps.add(new Identifier(parameterTypeIdCode));
				else if (parameterMode.equals(MODE_OUT))
					outParTyps.add(new Identifier(parameterTypeIdCode));
				else
					Log
							.errorMessage("EvaluationTypeDatabase.retrieveParameterTypes | ERROR: Unknown parameter mode for parameterTypeId "
									+ parameterTypeIdCode);
			}
		} catch (SQLException sqle) {
			String mesg = "EvaluationTypeDatabase.retrieveParameterTypes | Cannot retrieve parameter types for evaluation type "
					+ evaluationTypeIdStr;
			throw new Exception(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			} catch (SQLException sqle1) {
			}
		}
		inParTyps.trimToSize();
		thresholdParTyps.trimToSize();
		etalonParTyps.trimToSize();
		outParTyps.trimToSize();
		evaluationType.setParameterTypes(inParTyps, thresholdParTyps,
				etalonParTyps, outParTyps);
	}

	public Object retrieveObject(StorableObject storableObject,
			int retrieveKind, Object arg) throws Exception {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		try {
			this.insertEvaluationType(evaluationType);
			this.insertParameterTypes(evaluationType);
		} catch (Exception e) {
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

	private void insertEvaluationType(EvaluationType evaluationType)
			throws Exception {
		String evaluationTypeIdStr = evaluationType.getId().toSQLString();
		String sql;
		{
			StringBuffer buffer = new StringBuffer(SQL_INSERT_INTO);
			buffer.append(ObjectEntities.EVALUATIONTYPE_ENTITY);
			buffer.append(OPEN_BRACKET);
			buffer.append(COLUMN_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_CODENAME);
			buffer.append(COMMA);
			buffer.append(COLUMN_DESCRIPTION);
			buffer.append(CLOSE_BRACKET);
			buffer.append(SQL_VALUES);
			buffer.append(OPEN_BRACKET);
			buffer.append(evaluationTypeIdStr);
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toUpdateSubString(evaluationType
					.getCreated()));
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toUpdateSubString(evaluationType
					.getModified()));
			buffer.append(COMMA);
			buffer.append(evaluationType.getCreatorId().toSQLString());
			buffer.append(COMMA);
			buffer.append(evaluationType.getModifierId().toSQLString());
			buffer.append(COMMA);
			buffer.append(APOSTOPHE);
			buffer.append(evaluationType.getCodename());
			buffer.append(APOSTOPHE);
			buffer.append(COMMA);
			buffer.append(APOSTOPHE);
			buffer.append(evaluationType.getDescription());
			buffer.append(APOSTOPHE);
			buffer.append(CLOSE_BRACKET);
			sql = buffer.toString();
		}
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage(
					"EvaluationTypeDatabase.insertEvaluationType | Trying: "
							+ sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		} catch (SQLException sqle) {
			String mesg = "EvaluationTypeDatabase.insertEvaluationType | Cannot insert evaluation type "
					+ evaluationTypeIdStr;
			throw new Exception(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			} catch (SQLException sqle1) {
			}
		}
	}

	private void insertParameterTypes(EvaluationType evaluationType)
			throws Exception {
		ArrayList inParTyps = evaluationType.getInParameterTypes();
		ArrayList thresholdParTyps = evaluationType
				.getThresholdParameterTypes();
		ArrayList etalonParTyps = evaluationType.getEtalonParameterTypes();
		ArrayList outParTyps = evaluationType.getOutParameterTypes();
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String evaluationTypeIdCode = evaluationType.getId().getCode();
		String sql;
		{
			StringBuffer buffer = new StringBuffer(SQL_INSERT_INTO);
			buffer.append(ObjectEntities.EVATYPPARTYPLINK_ENTITY);
			buffer.append(OPEN_BRACKET);
			buffer.append(LINK_COLUMN_EVALUATION_TYPE_ID);
			buffer.append(COMMA);
			buffer.append(LINK_COLUMN_PARAMETER_TYPE_ID);
			buffer.append(COMMA);
			buffer.append(LINK_COLUMN_PARAMETER_MODE);
			buffer.append(CLOSE_BRACKET);
			buffer.append(SQL_VALUES);
			buffer.append(OPEN_BRACKET);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(CLOSE_BRACKET);
			sql = buffer.toString();
		}
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
				parameterTypeIdCode = ((Identifier) iterator.next())
						.getCode();
				preparedStatement.setString(2, parameterTypeIdCode);
				parameterMode = MODE_IN;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage(
						"EvaluationTypeDatabase.insertParameterTypes | Inserting parameter type "
								+ parameterTypeIdCode
								+ " of parameter mode '" + parameterMode
								+ "' for evaluation type "
								+ evaluationTypeIdCode, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = thresholdParTyps.iterator(); iterator
					.hasNext();) {
				preparedStatement.setString(1, evaluationTypeIdCode);
				parameterTypeIdCode = ((Identifier) iterator.next())
						.getCode();
				preparedStatement.setString(2, parameterTypeIdCode);
				parameterMode = MODE_THRESHOLD;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage(
						"EvaluationTypeDatabase.insertParameterTypes | Inserting parameter type "
								+ parameterTypeIdCode
								+ " of parameter mode '" + parameterMode
								+ "' for evaluation type "
								+ evaluationTypeIdCode, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = etalonParTyps.iterator(); iterator
					.hasNext();) {
				preparedStatement.setString(1, evaluationTypeIdCode);
				parameterTypeIdCode = ((Identifier) iterator.next())
						.getCode();
				preparedStatement.setString(2, parameterTypeIdCode);
				parameterMode = MODE_ETALON;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage(
						"EvaluationTypeDatabase.insertParameterTypes | Inserting parameter type "
								+ parameterTypeIdCode
								+ " of parameter mode '" + parameterMode
								+ "' for evaluation type "
								+ evaluationTypeIdCode, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = outParTyps.iterator(); iterator
					.hasNext();) {
				preparedStatement.setString(1, evaluationTypeIdCode);
				parameterTypeIdCode = ((Identifier) iterator.next())
						.getCode();
				preparedStatement.setString(2, parameterTypeIdCode);
				parameterMode = MODE_OUT;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage(
						"EvaluationTypeDatabase.insertParameterTypes | Inserting parameter type "
								+ parameterTypeIdCode
								+ " of parameter mode '" + parameterMode
								+ "' for evaluation type "
								+ evaluationTypeIdCode, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
		} catch (SQLException sqle) {
			String mesg = "EvaluationTypeDatabase.insertParameterTypes | Cannot insert parameter type "
					+ parameterTypeIdCode
					+ " of parameter mode '"
					+ parameterMode
					+ "' for evaluation type "
					+ evaluationTypeIdCode;
			throw new Exception(mesg, sqle);
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			} catch (SQLException sqle1) {
			}
		}
	}

	public void update(StorableObject storableObject, int updateKind,
			Object obj) throws Exception {
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
					+ ObjectEntities.EVATYPPARTYPLINK_ENTITY + SQL_WHERE
					+ LINK_COLUMN_EVALUATION_TYPE_ID + EQUALS
					+ evaluationTypeIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.EVALUATIONTYPE_ENTITY + SQL_WHERE
					+ COLUMN_ID + EQUALS + evaluationTypeIdStr);
			connection.commit();
		} catch (SQLException sqle1) {
			Log.errorException(sqle1);
		} finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			} catch (SQLException ex) {
			}
		}
	}

	public static EvaluationType retrieveForCodename(String codename)
			throws Exception {
		String sql;
		{
			StringBuffer buffer = new StringBuffer(SQL_SELECT);
			buffer.append(COLUMN_ID);
			buffer.append(SQL_FROM);
			buffer.append(ObjectEntities.EVALUATIONTYPE_ENTITY);
			buffer.append(SQL_WHERE);
			buffer.append(COLUMN_CODENAME);
			buffer.append(EQUALS);
			buffer.append(APOSTOPHE);
			buffer.append(codename);
			buffer.append(APOSTOPHE);
			sql = buffer.toString();
		}
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage(
					"EvaluationTypeDatabase.retrieveForCodename | Trying: "
							+ sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				return new EvaluationType(new Identifier(resultSet
						.getString(COLUMN_ID)));
			} else
				throw new Exception("No evaluation type with codename: '"
						+ codename + "'");
		} catch (SQLException sqle) {
			String mesg = "EvaluationTypeDatabase.retrieveForCodename | Cannot retrieve evaluation type with codename: '"
					+ codename + "'";
			throw new Exception(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			} catch (SQLException sqle1) {
			}
		}
	}
}