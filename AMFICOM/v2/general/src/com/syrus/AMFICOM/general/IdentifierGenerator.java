package com.syrus.AMFICOM.general;

import java.util.HashMap;
import java.util.LinkedList;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

public class IdentifierGenerator {
	private static HashMap entityMap;

	static {
		entityMap = new HashMap(30);

		addEntity(ObjectEntities.SET_ENTITY, ObjectEntities.SET_ENTITY_CODE);
		addEntity(ObjectEntities.SETPARAMETER_ENTITY, ObjectEntities.SETPARAMETER_ENTITY_CODE);
		addEntity(ObjectEntities.MS_ENTITY, ObjectEntities.MS_ENTITY_CODE);
		addEntity(ObjectEntities.MEASUREMENT_ENTITY, ObjectEntities.MEASUREMENT_ENTITY_CODE);
		addEntity(ObjectEntities.ANALYSIS_ENTITY, ObjectEntities.ANALYSIS_ENTITY_CODE);
		addEntity(ObjectEntities.EVALUATION_ENTITY, ObjectEntities.EVALUATION_ENTITY_CODE);
		addEntity(ObjectEntities.TEST_ENTITY, ObjectEntities.TEST_ENTITY_CODE);
		addEntity(ObjectEntities.RESULT_ENTITY, ObjectEntities.RESULT_ENTITY_CODE);
		addEntity(ObjectEntities.RESULTPARAMETER_ENTITY, ObjectEntities.RESULTPARAMETER_ENTITY_CODE);
		addEntity(ObjectEntities.PTTEMPORALTEMPLATE_ENTITY, ObjectEntities.PTTEMPORALTEMPLATE_ENTITY_CODE);

		addEntity(ObjectEntities.ME_ENTITY, ObjectEntities.ME_ENTITY_CODE);
		addEntity(ObjectEntities.KIS_ENTITY, ObjectEntities.KIS_ENTITY_CODE);
		addEntity(ObjectEntities.MCM_ENTITY, ObjectEntities.MCM_ENTITY_CODE);
		addEntity(ObjectEntities.SERVER_ENTITY, ObjectEntities.SERVER_ENTITY_CODE);

		addEntity(ObjectEntities.PARAMETERTYPE_ENTITY, ObjectEntities.PARAMETERTYPE_ENTITY_CODE);
		addEntity(ObjectEntities.MEASUREMENTTYPE_ENTITY, ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
		addEntity(ObjectEntities.ANALYSISTYPE_ENTITY, ObjectEntities.ANALYSISTYPE_ENTITY_CODE);
		addEntity(ObjectEntities.EVALUATIONTYPE_ENTITY, ObjectEntities.EVALUATIONTYPE_ENTITY_CODE);
	}

	public IdentifierGenerator() {
	}

	public static synchronized Identifier generateIdentifier(String entity) {
		if (entity != null) {
			short major = generateMajor(entity);
			long minor = generateMinor(entity);
			return new Identifier(major, minor);
		}
		else {
			Log.errorMessage("NULL entity supplied");
			return null;
		}
	}

	public static synchronized Identifier[] generateIdentifierRange(String entity, int range_size) {
		if (entity != null) {
			LinkedList linkedlist = new LinkedList();
			short major = generateMajor(entity);
			long minor;
			for (int i = 0; i < range_size; i++) {
				minor = generateMinor(entity);
				linkedlist.add(new Identifier(major, minor));
			}
			return (Identifier[])linkedlist.toArray(new Identifier[linkedlist.size()]);
		}
		else {
			Log.errorMessage("NULL entity supplied");
			return null;
		}
	}

	private static short generateMajor(String entity) {
		short table_code = ObjectEntities.UNKNOWN_ENTITY_CODE;
		Short val = (Short)entityMap.get(entity);
		if (val != null)
			table_code = val.shortValue();
		else
				Log.errorMessage("NO table code for entity: " + entity + "; leaving default: " + table_code);
		return table_code;
	}

	private static long generateMinor(String entity) {
		long minor = 0L;

		String seq_name = entity + "_seq";
		String sql = "SELECT " + seq_name + ".NEXTVAL FROM sys.dual";
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = DatabaseConnection.getConnection().createStatement();
			Log.debugMessage("IdentifierGenerator.generateMinor | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				minor = resultSet.getLong(1);
			else
				throw new Exception("Cannot get nextval from sequence: " + seq_name);
		}
		catch (Exception e) {
			Log.errorException(e);
		}
		finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (statement != null)
					statement.close();
				resultSet = null;
				statement = null;
			}
			catch (SQLException sqle1) {}
		}

		return minor;
	}

	private static void addEntity(String entity, short table_code) {
		Short val = new Short(table_code);
		if (! entityMap.containsValue(val))
			entityMap.put(entity, val);
		else
			Log.errorMessage("Duplicate table code: " + table_code + " specified for entity: " + entity);
	}
}