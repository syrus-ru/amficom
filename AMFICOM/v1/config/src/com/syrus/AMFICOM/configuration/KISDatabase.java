/*
 * $Id: KISDatabase.java,v 1.40 2004/11/23 15:24:41 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.40 $, $Date: 2004/11/23 15:24:41 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class KISDatabase extends StorableObjectDatabase {
	// table :: kis
	// description VARCHAR2(256),
	public static final String COLUMN_DESCRIPTION   = "description";
	// name VARCHAR2(64) NOT NULL,
	public static final String COLUMN_NAME  		= "name";
	// hostname VARCHAR2(64),
	public static final String COLUMN_HOSTNAME  	= "hostname";
	// tcp_port NUMBER(5,0),
	public static final String COLUMN_TCP_PORT  		= "tcp_port";
	// equipment_id Identifier NOT NULL
	public static final String COLUMN_EQUIPMENT_ID 	= "equipment_id";
	// mcm_id Identifier NOT NULL
	public static final String COLUMN_MCM_ID 		= "mcm_id";

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private static String columns;
	private static String updateMultiplySQLValues;

	private KIS fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof KIS)
			return (KIS) storableObject;
		throw new IllegalDataException("KISDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.KIS_ENTITY;
	}

	protected String getColumns() {
		if (columns == null) {
			columns = super.getColumns() + COMMA
				+ DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_HOSTNAME + COMMA
				+ COLUMN_TCP_PORT + COMMA
				+ COLUMN_EQUIPMENT_ID + COMMA
				+ COLUMN_MCM_ID;
		}
		return columns;
	}

	protected String getUpdateMultiplySQLValues() {
		if (updateMultiplySQLValues == null){
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		KIS kis = fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(kis.getDomainId()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(kis.getName()) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(kis.getDescription()) + APOSTOPHE + COMMA
			+ APOSTOPHE + kis.getHostName() + APOSTOPHE + COMMA
			+ kis.getTCPPort() + COMMA
			+ DatabaseIdentifier.toSQLString(kis.getEquipmentId()) + COMMA
			+ DatabaseIdentifier.toSQLString(kis.getMCMId());
		return sql;
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		KIS kis = this.fromStorableObject(storableObject);
		super.retrieveEntity(kis);
		this.retrieveKISMeasurementPortIds(kis);
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
			throws IllegalDataException, UpdateObjectException {
		KIS kis = fromStorableObject(storableObject);
		int i;
		try {
			Identifier equipmentId = kis.getEquipmentId();
			Identifier mcmId = kis.getMCMId();
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, kis.getDomainId());
			preparedStatement.setString( ++i, kis.getName());
			preparedStatement.setString( ++i, kis.getDescription());
			preparedStatement.setString( ++i, kis.getHostName());
			preparedStatement.setInt( ++i, kis.getTCPPort());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, equipmentId);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, mcmId);
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException("KISDatabase." + "setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		KIS kis = storableObject == null ? null : fromStorableObject(storableObject);
		if (kis == null) {
			kis = new KIS(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
										null,
										null,
										null,
										null,
										null,
										(short)0,
										null,
										null);			
		}

		kis.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
											DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
											DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
											DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
											DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),
											DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
											DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
											resultSet.getString(COLUMN_HOSTNAME),
											resultSet.getShort(COLUMN_TCP_PORT),
											DatabaseIdentifier.getIdentifier(resultSet, COLUMN_EQUIPMENT_ID),
											DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MCM_ID));
		
		return kis;
	}

	private void retrieveKISMeasurementPortIds(KIS kis) throws RetrieveObjectException {
		List measurementPortIds = new ArrayList();

		String kisIdStr = DatabaseIdentifier.toSQLString(kis.getId());
		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM + ObjectEntities.MEASUREMENTPORT_ENTITY
			+ SQL_WHERE + MeasurementPortDatabase.COLUMN_KIS_ID + EQUALS + kisIdStr;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("KISDatabase.retrieveKISMeasurementPortIds | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				measurementPortIds.add(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID));
			}
		}
		catch (SQLException sqle) {
			String mesg = "KISDatabase.retrieveKISMeasurementPortIds | Cannot retrieve measurement port ids for kis " + kisIdStr;
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
		kis.setMeasurementPortIds(measurementPortIds);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		KIS kis = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			case KIS.RETRIEVE_MONITORED_ELEMENTS:
				return this.retrieveMonitoredElements(kis);
			default:
				return null;
		}
	}

  private void retrieveKISMeasurementPortIdsByOneQuery(List kiss) throws RetrieveObjectException {
  	if ((kiss == null) || (kiss.isEmpty()))
			return;     

    StringBuffer sql = new StringBuffer(SQL_SELECT
			+ COLUMN_ID + COMMA
			+ MeasurementPortDatabase.COLUMN_KIS_ID
			+ SQL_FROM + ObjectEntities.MEASUREMENTPORT_ENTITY
			+ SQL_WHERE + MeasurementPortDatabase.COLUMN_KIS_ID
			+ SQL_IN + OPEN_BRACKET);
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
					sql.append(COLUMN_ID);
					sql.append(SQL_IN);
					sql.append(OPEN_BRACKET);
				}                   
			}
		}
		sql.append(CLOSE_BRACKET);

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("KISDatabase.retrieveKISMeasurementPortIdsByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			Map mpIdMap = new HashMap();
			while (resultSet.next()) {
				KIS kis = null;
				Identifier kisId = DatabaseIdentifier.getIdentifier(resultSet, MeasurementPortDatabase.COLUMN_KIS_ID);
				for (Iterator it = kiss.iterator(); it.hasNext();) {
					KIS kisToCompare = (KIS) it.next();
					if (kisToCompare.getId().equals(kisId)) {
						kis = kisToCompare;
						break;
					}
				}

				if (kis == null) {
					String mesg = "KISDatabase.retrieveKISMeasurementPortIdsByOneQuery | Cannot found correspond result for '" + kisId.getIdentifierString() +"'" ;
					throw new RetrieveObjectException(mesg);
				}

				Identifier mpId = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID);
				List mpIds = (List)mpIdMap.get(kis);
				if (mpIds == null) {
					mpIds = new LinkedList();
					mpIdMap.put(kis, mpIds);
				}
				mpIds.add(mpId);              
			}

      for (Iterator iter = kiss.iterator(); iter.hasNext();) {
				KIS kis = (KIS) iter.next();
				List mpIds = (List)mpIdMap.get(kis);
				kis.setMeasurementPortIds(mpIds);
			}
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.retrieveMeasurementSetupTestLinksByOneQuery | Cannot retrieve parameters for result -- " + sqle.getMessage();
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	private List retrieveMonitoredElements(KIS kis) throws RetrieveObjectException {
		List monitoredElements = new ArrayList();

		String kisIdStr = DatabaseIdentifier.toSQLString(kis.getId());
		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM + ObjectEntities.ME_ENTITY
			+ SQL_WHERE + MonitoredElementDatabase.COLUMN_MEASUREMENT_PORT_ID + SQL_IN + OPEN_BRACKET
				+ SQL_SELECT
				+ COLUMN_ID
				+ SQL_FROM + ObjectEntities.MEASUREMENTPORT_ENTITY
				+ SQL_WHERE + MeasurementPortDatabase.COLUMN_KIS_ID + EQUALS + kisIdStr
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
					monitoredElements.add((MonitoredElement)ConfigurationStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), true));
				}
				catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
			}
		}
		catch (SQLException sqle) {
			String mesg = "KISDatabase.retrieveMonitoredElements | Cannot retrieve monitored elements for kis " + kisIdStr;
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
			} finally {
                DatabaseConnection.releaseConnection(connection);
            }
		}
		return monitoredElements;
	}

  private void retrieveMonitoredElementsByOneQuery(List kiss) throws RetrieveObjectException {
		if ((kiss == null) || (kiss.isEmpty()))
			return;     

		StringBuffer sql = new StringBuffer(SQL_SELECT
			+ COLUMN_ID + COMMA
			+ MeasurementPortDatabase.COLUMN_KIS_ID
			+ SQL_FROM + ObjectEntities.MEASUREMENTPORT_ENTITY
			+ SQL_WHERE + MeasurementPortDatabase.COLUMN_KIS_ID
			+ SQL_IN + OPEN_BRACKET);
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
					sql.append(COLUMN_ID);
					sql.append(SQL_IN);
					sql.append(OPEN_BRACKET);
				}
			}
		}
		sql.append(CLOSE_BRACKET);

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("KISDatabase.retrieveKISMeasurementPortIdsByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			Map mpIdMap = new HashMap();
			while (resultSet.next()) {
				KIS kis = null;
				Identifier kisId = DatabaseIdentifier.getIdentifier(resultSet, MeasurementPortDatabase.COLUMN_KIS_ID);
				for (Iterator it = kiss.iterator(); it.hasNext();) {
					KIS kisToCompare = (KIS) it.next();
					if (kisToCompare.getId().equals(kisId)) {
						kis = kisToCompare;
						break;
					}
				}

				if (kis == null) {
					String mesg = "KISDatabase.retrieveKISMeasurementPortIdsByOneQuery | Cannot found correspond result for '" + kisId.getIdentifierString() +"'" ;
					throw new RetrieveObjectException(mesg);
				}

				Identifier mpId = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID);
				List mpIds = (List)mpIdMap.get(kis);
				if (mpIds == null) {
					mpIds = new LinkedList();
					mpIdMap.put(kis, mpIds);
				}
				mpIds.add(mpId);              
			}

      for (Iterator iter = kiss.iterator(); iter.hasNext();) {
				KIS kis = (KIS) iter.next();
				List mpIds = (List)mpIdMap.get(kis);
				kis.setMeasurementPortIds(mpIds);
			}

		}
		catch (SQLException sqle) {
			String mesg = "KISDatabase.retrieveKISMeasurementPortIdsByOneQuery | Cannot retrieve parameters for result -- " + sqle.getMessage();
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		KIS kis = this.fromStorableObject(storableObject);
		super.insertEntity(kis);		
	}

	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
	}	

	public void update(StorableObject storableObject, int updateKind, Object obj)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntity(storableObject, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntity(storableObject, false);
			break;
		}
	}

	public void update(List storableObjects, int updateKind, Object arg)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntities(storableObjects, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntities(storableObjects, false);
			break;
		}
	}

	public List retrieveAll() throws RetrieveObjectException {
		List list = null;
		try {
			list = retrieveByIds(null, null);
		}
		catch (IllegalDataException ide) {
			Log.debugMessage("KISDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
			throw new RetrieveObjectException(ide);
		}
		return list;
	}

	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		List list = null;
		if ((ids == null) || (ids.isEmpty()))
			list = super.retrieveByIdsOneQuery(null, condition);
		else
			list = super.retrieveByIdsOneQuery(ids, condition);

    if (list != null) {
			retrieveKISMeasurementPortIdsByOneQuery(list);
			retrieveMonitoredElementsByOneQuery(list);		
		}
        
		return list;
	}

	private List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
		List list = null;

		String condition = DomainMember.COLUMN_DOMAIN_ID + EQUALS + DatabaseIdentifier.toSQLString(domain.getId());

    try {
			list = retrieveButIds(ids, condition);
		}
		catch (IllegalDataException ide) {
			Log.debugMessage("KISDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
		}
		return list;
	}

	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException, IllegalDataException {
		List list;
		if (condition instanceof DomainCondition) {
			DomainCondition domainCondition = (DomainCondition)condition;
			list = this.retrieveButIdsByDomain(ids, domainCondition.getDomain());
		}
		else {
			Log.errorMessage("KISDatabase.retrieveByCondition | Unknown condition class: " + condition.getClass().getName());
			list = this.retrieveButIds(ids);
		}
		return list;
	}
}
