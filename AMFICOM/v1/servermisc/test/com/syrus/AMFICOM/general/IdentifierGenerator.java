package com.syrus.AMFICOM.general;

import com.syrus.util.Log;
import com.syrus.AMFICOM.server.ResourcedbInterface;

public class IdentifierGenerator {

	public IdentifierGenerator() {
	}

	public static synchronized Identifier generateIdentifier(String entity) {
		if (entity != null) {
//			String major = generateMajor(entity);
//			long minor = generateMinor(entity);

			try {
				return new Identifier(ResourcedbInterface.getUId(entity));
			}
			catch (Exception e) {
				Log.errorException(e);
				return null;
			}
		}
		else {
			Log.errorMessage("NULL entity supplied");
			return null;
		}
	}
/*
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
		return new String(entity);
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
*/
}
