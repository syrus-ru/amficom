package com.syrus.AMFICOM.measurement;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.ObjectEntities;

public class Evaluation_Database extends StorableObject_Database {

	private Evaluation fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof Evaluation)
			return (Evaluation)storableObject;
		else
			throw new Exception("Evaluation_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		this.retrieveEvaluation(evaluation);
	}

	private void retrieveEvaluation(Evaluation evaluation) throws Exception {
		String evaluation_id_str = evaluation.getId().toString();
		String sql = "SELECT "
			+ DatabaseDate.toQuerySubString("created") + ", " 
			+ DatabaseDate.toQuerySubString("modified") + ", "
			+ "creator_id, "
			+ "modifier_id, "
			+ "type_id, "
			+ "monitored_element_id"
			+ "threshold_set_id, "
			+ "etalon_id, "
			+ " FROM " + ObjectEntities.EVALUATION_ENTITY
			+ " WHERE id = " + evaluation_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Evaluation_Database.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				Set threshold_set = new Set(new Identifier(resultSet.getLong("threshold_set_id")));
				Set etalon = new Set(new Identifier(resultSet.getLong("etalon_id")));
				evaluation.setAttributes(DatabaseDate.fromQuerySubString(resultSet, "created"),
																 DatabaseDate.fromQuerySubString(resultSet, "modified"),
																 new Identifier(resultSet.getLong("creator_id")),
																 new Identifier(resultSet.getLong("modifier_id")),
																 new Identifier(resultSet.getLong("type_id")),
																 new Identifier(resultSet.getLong("monitored_element_id")),
																 threshold_set,
																 etalon);
			}
			else
				throw new Exception("No such evaluation: " + evaluation_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "Evaluation_Database.retrieve | Cannot retrieve evaluation " + evaluation_id_str;
			throw new Exception(mesg, sqle);
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
			catch (SQLException sqle1) {}
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws Exception {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		try {
			this.insertEvaluation(evaluation);
		}
		catch (Exception e) {
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

	public void insertEvaluation(Evaluation evaluation) throws Exception {
		String evaluation_id_str = evaluation.getId().toString();
		String sql = "INSERT INTO " + ObjectEntities.EVALUATION_ENTITY
			+ " (id, created, modified, creator_id, modifier_id, type_id, monitored_element_id, threshold_set_id, etalon_id)"
			+ " VALUES ("
			+ evaluation_id_str + ", "
			+ DatabaseDate.toUpdateSubString(evaluation.getCreated()) + ", "
			+ DatabaseDate.toUpdateSubString(evaluation.getModified()) + ", "
			+ evaluation.getCreatorId().toString() + ", "
			+ evaluation.getModifierId().toString() + ", "
			+ evaluation.getTypeId().toString() + ", "
			+ evaluation.getMonitoredElementId().toString() + ", "
			+ evaluation.getThresholdSet().getId().toString() + ", "
			+ evaluation.getEtalon().getId().toString()
			+ ")";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Evaluation_Database.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "Evaluation_Database.insert | Cannot insert evaluation " + evaluation_id_str;
			throw new Exception(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {}
		}
	}

	public void update(StorableObject storableObject, int update_kind, Object obj) throws Exception {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}
}