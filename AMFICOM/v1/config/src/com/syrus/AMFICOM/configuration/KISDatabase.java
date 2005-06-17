/*
 * $Id: KISDatabase.java,v 1.76 2005/06/17 12:32:20 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacterizableDatabase;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.76 $, $Date: 2005/06/17 12:32:20 $
 * @author $Author: bass $
 * @module config_v1
 */

public final class KISDatabase extends CharacterizableDatabase {
	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	protected static final int RETRIEVE_MONITORED_ELEMENTS = 1;

	private static final int SIZE_HOSTNAME_COLUMN = 256;
	private static String columns;
	private static String updateMultipleSQLValues;

	private KIS fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof KIS)
			return (KIS) storableObject;
		throw new IllegalDataException("KISDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected short getEntityCode() {		
		return ObjectEntities.KIS_CODE;
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ KISWrapper.COLUMN_HOSTNAME + COMMA
				+ KISWrapper.COLUMN_TCP_PORT + COMMA
				+ KISWrapper.COLUMN_EQUIPMENT_ID + COMMA
				+ KISWrapper.COLUMN_MCM_ID;
		}
		return columns;
	}

	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		KIS kis = this.fromStorableObject(storableObject);
		String sql = DatabaseIdentifier.toSQLString(kis.getDomainId()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(kis.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(kis.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(kis.getHostName(), SIZE_HOSTNAME_COLUMN) + APOSTOPHE + COMMA
			+ kis.getTCPPort() + COMMA
			+ DatabaseIdentifier.toSQLString(kis.getEquipmentId()) + COMMA
			+ DatabaseIdentifier.toSQLString(kis.getMCMId());
		return sql;
	}

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		KIS kis = this.fromStorableObject(storableObject);
		Identifier equipmentId = kis.getEquipmentId();
		Identifier mcmId = kis.getMCMId();
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, kis.getDomainId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, kis.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, kis.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, kis.getHostName(), SIZE_HOSTNAME_COLUMN);
		preparedStatement.setInt( ++startParameterNumber, kis.getTCPPort());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, equipmentId);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, mcmId);
		return startParameterNumber;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, SQLException {
		KIS kis = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (kis == null) {
			kis = new KIS(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					null,
					0L,
					null,
					null,
					null,
					null,
					(short) 0,
					null,
					null);
		}

		kis.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				resultSet.getString(KISWrapper.COLUMN_HOSTNAME),
				resultSet.getShort(KISWrapper.COLUMN_TCP_PORT),
				DatabaseIdentifier.getIdentifier(resultSet, KISWrapper.COLUMN_EQUIPMENT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, KISWrapper.COLUMN_MCM_ID));

		return kis;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, RetrieveObjectException {
		KIS kis = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			case RETRIEVE_MONITORED_ELEMENTS:
				return this.retrieveMonitoredElements(kis);
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  kis.getId() + "'; argument: " + arg);
				return null;
		}
	}

	private List retrieveMonitoredElements(KIS kis) throws RetrieveObjectException {
		List monitoredElements = new ArrayList();

		String kisIdStr = DatabaseIdentifier.toSQLString(kis.getId());
		String sql = SQL_SELECT
			+ StorableObjectWrapper.COLUMN_ID
			+ SQL_FROM + ObjectEntities.MONITOREDELEMENT
			+ SQL_WHERE + MonitoredElementWrapper.COLUMN_MEASUREMENT_PORT_ID + SQL_IN + OPEN_BRACKET
				+ SQL_SELECT
				+ StorableObjectWrapper.COLUMN_ID
				+ SQL_FROM + ObjectEntities.MEASUREMENTPORT
				+ SQL_WHERE + MeasurementPortWrapper.COLUMN_KIS_ID + EQUALS + kisIdStr
			+ CLOSE_BRACKET;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
        try {
			statement = connection.createStatement();
			Log.debugMessage("KISDatabase.retrieveMonitoredElements | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				try {
					monitoredElements.add(StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), true));
				} catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
			}
		} catch (SQLException sqle) {
			String mesg = "KISDatabase.retrieveMonitoredElements | Cannot retrieve monitored elements for kis " + kisIdStr;
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
                DatabaseConnection.releaseConnection(connection);
            }
		}
		return monitoredElements;
	}

  public Map retrieveMonitoredElementsByOneQuery(List kiss) throws RetrieveObjectException {
		if ((kiss == null) || (kiss.isEmpty()))
			return null;

		StringBuffer sql = new StringBuffer(SQL_SELECT
		+ ObjectEntities.MONITOREDELEMENT + DOT + StorableObjectWrapper.COLUMN_ID + COMMA
		+ ObjectEntities.MEASUREMENTPORT + DOT + MeasurementPortWrapper.COLUMN_KIS_ID
		+ SQL_FROM + ObjectEntities.MONITOREDELEMENT + COMMA + ObjectEntities.MEASUREMENTPORT
		+ SQL_WHERE + ObjectEntities.MONITOREDELEMENT + DOT + MonitoredElementWrapper.COLUMN_MEASUREMENT_PORT_ID + SQL_IN
		+ OPEN_BRACKET
			+ SQL_SELECT
			+ StorableObjectWrapper.COLUMN_ID
			+ SQL_FROM + ObjectEntities.MEASUREMENTPORT
			+ SQL_WHERE + MeasurementPortWrapper.COLUMN_KIS_ID + SQL_IN + OPEN_BRACKET);

		int i = 1;
		for (Iterator it = kiss.iterator(); it.hasNext();i++) {
			KIS kis = (KIS)it.next();
			sql.append(DatabaseIdentifier.toSQLString(kis.getId()));
			if (it.hasNext()) {
				if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
					sql.append(COMMA);
				else {
					sql.append(CLOSE_BRACKET);
					sql.append(SQL_OR);
					sql.append(MeasurementPortWrapper.COLUMN_KIS_ID);
					sql.append(SQL_IN);
					sql.append(OPEN_BRACKET);
				}
			}
		}
		sql.append(CLOSE_BRACKET);
		sql.append(CLOSE_BRACKET);

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("KISDatabase.retrieveKISMonitoredElementsByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			Map meIdMap = new HashMap();
			Identifier kisId;
			List meIds;
			while (resultSet.next()) {
				kisId = DatabaseIdentifier.getIdentifier(resultSet, MeasurementPortWrapper.COLUMN_KIS_ID);
				meIds = (List) meIdMap.get(kisId);
				if (meIds == null) {
					meIds = new LinkedList();
					meIdMap.put(kisId, meIds);
				}
				meIds.add(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID));
			}

			return meIdMap;
		} catch (SQLException sqle) {
			String mesg = "KISDatabase.retrieveKISMonitoredElementsByOneQuery | Cannot retrieve parameters for result -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

}
