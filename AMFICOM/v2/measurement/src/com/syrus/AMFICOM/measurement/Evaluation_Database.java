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

public class Evaluation_Database extends StorableObject_Database {

	public void retrieve(StorableObject storableObject) throws Exception {
		Evaluation evaluation = null;
		if (storableObject instanceof Evaluation)
			evaluation = (Evaluation)storableObject;
		else
			throw new Exception("Evaluation_Database.retrieve | Illegal Storable Object: " + storableObject.getClass().getName());

		String evaluation_id_str = evaluation.getId().toString();
		String sql = "SELECT type_id, threshold_set_id, etalon_id, monitored_element_id FROM " + ObjectEntities.EVALUATION_ENTITY + " WHERE id = " + evaluation_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Evaluation_Database.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				Set threshold_set = new Set(new Identifier(resultSet.getLong("threshold_set_id")));
				Set etalon = new Set(new Identifier(resultSet.getLong("etalon_id")));
				evaluation.setAttributes(new Identifier(resultSet.getLong("type_id")),
																 threshold_set,
																 etalon,
																 new Identifier(resultSet.getLong("monitored_element_id")));
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
		Evaluation evaluation = null;
		if (storableObject instanceof Evaluation)
			evaluation = (Evaluation)storableObject;
		else
			throw new Exception("Evaluation_Database.retrieveObject | Illegal Storable Object: " + storableObject.getClass().getName());

		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		Evaluation evaluation = null;
		if (storableObject instanceof Evaluation)
			evaluation = (Evaluation)storableObject;
		else
			throw new Exception("Evaluation_Database.insert | Illegal Storable Object: " + storableObject.getClass().getName());

		String evaluation_id_str = evaluation.getId().toString();
		String sql = "INSERT INTO " + ObjectEntities.EVALUATION_ENTITY + " (id, type_id, threshold_set_id, etalon_id, monitored_element_id) VALUES (" + evaluation_id_str + ", " + evaluation.getTypeId().toString() + ", " + evaluation.getThresholdSet().getId().toString() + ", " + evaluation.getEtalon().getId().toString() + ", " + evaluation.getMonitoredElementId().toString() + ")";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Evaluation_Database.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
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
		Evaluation evaluation = null;
		if (storableObject instanceof Evaluation)
			evaluation = (Evaluation)storableObject;
		else
			throw new Exception("Evaluation_Database.update | Illegal Storable Object: " + storableObject.getClass().getName());
	}
}