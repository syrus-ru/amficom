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
import com.syrus.AMFICOM.general.StorableObject_DatabaseContext;
import com.syrus.AMFICOM.general.ObjectEntities;

public class Analysis_Database extends StorableObject_Database {

	private Analysis fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof Analysis)
			return (Analysis)storableObject;
		else
			throw new Exception("Analysis_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		Analysis analysis = this.fromStorableObject(storableObject);
		this.retrieveAnalysis(analysis);
	}

	private void retrieveAnalysis(Analysis analysis) throws Exception {
		String analysis_id_str = analysis.getId().toString();
		String sql = "SELECT "
			+ DatabaseDate.toQuerySubString("created") + ", " 
			+ DatabaseDate.toQuerySubString("modified") + ", "
			+ "creator_id, "
			+ "modifier_id, "
			+ "type_id, "
			+ "monitored_element_id"
			+ "criteria_set_id, "
			+ " FROM " + ObjectEntities.ANALYSIS_ENTITY
			+ " WHERE id = " + analysis_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Analysis_Database.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				Set criteria_set = new Set(new Identifier(resultSet.getLong("criteria_set_id")));
				analysis.setAttributes(DatabaseDate.fromQuerySubString(resultSet, "created"),
															 DatabaseDate.fromQuerySubString(resultSet, "modified"),
															 new Identifier(resultSet.getLong("creator_id")),
															 new Identifier(resultSet.getLong("modifier_id")),
															 new Identifier(resultSet.getLong("type_id")),
															 new Identifier(resultSet.getLong("monitored_element_id")),
															 criteria_set);
			}
			else
				throw new Exception("No such analysis: " + analysis_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "Analysis_Database.retrieve | Cannot retrieve analysis " + analysis_id_str;
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
		Analysis analysis = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		Analysis analysis = this.fromStorableObject(storableObject);
		try {
			this.insertAnalysis(analysis);
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

	private void insertAnalysis(Analysis analysis) throws Exception {
		String analysis_id_str = analysis.getId().toString();
		String sql = "INSERT INTO " + ObjectEntities.ANALYSIS_ENTITY
			+ " (id, created, modified, creator_id, modifier_id, type_id, monitored_element_id, criteria_set_id)"
			+ " VALUES ("
			+ analysis_id_str + ", "
			+ DatabaseDate.toUpdateSubString(analysis.getCreated()) + ", "
			+ DatabaseDate.toUpdateSubString(analysis.getModified()) + ", "
			+ analysis.getCreatorId().toString() + ", "
			+ analysis.getModifierId().toString() + ", "
			+ analysis.getTypeId().toString() + ", "
			+ analysis.getMonitoredElementId().toString() + ", "
			+ analysis.getCriteriaSet().getId().toString()
			+ ")";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Analysis_Database.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "Analysis_Database.insert | Cannot insert analysis " + analysis_id_str;
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
		Analysis analysis = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}
}