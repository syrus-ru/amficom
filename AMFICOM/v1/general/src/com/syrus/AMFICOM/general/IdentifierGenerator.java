/*
 * $Id: IdentifierGenerator.java,v 1.5 2004/12/20 13:32:57 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.List;
import java.util.LinkedList;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.5 $, $Date: 2004/12/20 13:32:57 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public class IdentifierGenerator {
	
	private IdentifierGenerator() {
		// singleton constructor
	}
	
	public static synchronized Identifier generateIdentifier(short entityCode) throws IllegalObjectEntityException, IdentifierGenerationException {
		short major = generateMajor(entityCode);
		long minor = generateMinor(entityCode);
		return new Identifier(major, minor);
	}

	public static synchronized Identifier[] generateIdentifierRange(short entityCode, int rangeSize) throws IllegalObjectEntityException, IdentifierGenerationException {
		List list = new LinkedList();
		short major = generateMajor(entityCode);
		long minor;
		for (int i = 0; i < rangeSize; i++) {
			minor = generateMinor(entityCode);
			list.add(new Identifier(major, minor));
		}
		return (Identifier[])list.toArray(new Identifier[list.size()]);
	}

	private static short generateMajor(short entityCode) throws IllegalObjectEntityException {
		if (ObjectEntities.codeIsValid(entityCode))
			return entityCode;
		throw new IllegalObjectEntityException("Illegal or unknown entity code supplied: " + entityCode, IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

	private static long generateMinor(short entityCode) throws IdentifierGenerationException {
		String entity = ObjectEntities.codeToString(entityCode);
		if (entity != null)
			return generateMinor(entity);
		throw new IdentifierGenerationException("NULL entity");
	}

	private static long generateMinor(String entity) throws IdentifierGenerationException {
		long minor = 0L;

		String seqName = entity + "_seq";
		String sql = "SELECT " + seqName + ".NEXTVAL FROM sys.dual";
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = DatabaseConnection.getConnection().createStatement();
			Log.debugMessage("IdentifierGenerator.generateMinor | Trying: " + sql, Log.DEBUGLEVEL08);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				minor = resultSet.getLong(1);
			else
				throw new IdentifierGenerationException("Cannot get nextval from sequence: " + seqName);
		}
		catch (SQLException sqle) {
			Log.errorException(sqle);
			throw new IdentifierGenerationException("Cannot generate minor for entity: '" + entity + "' -- " + sqle.getMessage(), sqle);
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
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}

		return minor;
	}
}
