/*
 * $Id: IdentifierGenerator.java,v 1.15 2005/10/31 12:29:53 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.List;
import java.util.LinkedList;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.15 $, $Date: 2005/10/31 12:29:53 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public class IdentifierGenerator {
	
	private IdentifierGenerator() {
		// singleton constructor
		assert false;
	}
	
	protected static synchronized Identifier generateIdentifier(final short entityCode)
			throws IllegalObjectEntityException,
				IdentifierGenerationException {
		final short major = generateMajor(entityCode);
		final long minor = generateMinor(entityCode);
		return new Identifier(major, minor);
	}

	protected static synchronized Identifier[] generateIdentifierRange(final short entityCode, final int rangeSize)
			throws IllegalObjectEntityException,
				IdentifierGenerationException {
		final List<Identifier> list = new LinkedList<Identifier>();
		final short major = generateMajor(entityCode);
		for (int i = 0; i < rangeSize; i++) {
			final long minor = generateMinor(entityCode);
			list.add(new Identifier(major, minor));
		}
		return list.toArray(new Identifier[list.size()]);
	}

	private static short generateMajor(final short entityCode) throws IllegalObjectEntityException {
		if (ObjectEntities.isEntityCodeValid(entityCode)) {
			return entityCode;
		}
		throw new IllegalObjectEntityException(ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode,
				IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

	private static long generateMinor(final short entityCode) throws IdentifierGenerationException {
		final String entity = ObjectEntities.codeToString(entityCode);
		if (entity != null) {
			return generateMinor(entity);
		}
		throw new IdentifierGenerationException("NULL entity");
	}

	private static long generateMinor(final String entity) throws IdentifierGenerationException {
		long minor = 0L;

		final String seqName = entity + "_seq";
		final String sql = "SELECT " + seqName + ".NEXTVAL FROM sys.dual";
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null; 
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL08);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				minor = resultSet.getLong(1);
			}
			else {
				throw new IdentifierGenerationException("Cannot get nextval from sequence: " + seqName);
			}
		}
		catch (SQLException sqle) {
			Log.errorMessage(sqle);
			throw new IdentifierGenerationException("Cannot generate minor for entity: '" + entity + "' -- " + sqle.getMessage(), sqle);
		}
		finally {
			try {
				try {
					if (resultSet != null) {
						resultSet.close();
						resultSet = null;
					}
				} finally {
					try {
						if (statement != null) {
							statement.close();
							statement = null;
						}
					} finally {
						if (connection != null) {
							DatabaseConnection.releaseConnection(connection);
							connection = null;
						}
					}
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
		}

		return minor;
	}
}
