/*
 * $Id: TransmissionPathDatabase.java,v 1.1 2004/08/03 17:15:58 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/03 17:15:58 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class MonitoredElementDatabase extends StorableObjectDatabase {
    public static final String COLUMN_KIS_ID = "kis_id";
    public static final String COLUMN_LOCAL_ADDRESS = "local_address";

	private MonitoredElement fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MonitoredElement)
			return (MonitoredElement)storableObject;
		throw new IllegalDataException("MonitoredElement_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		this.retrieveMonitoredElement(monitoredElement);
	}

	private void retrieveMonitoredElement(MonitoredElement monitoredElement) throws ObjectNotFoundException, RetrieveObjectException {
		String meIdStr = monitoredElement.getId().toSQLString();
		String sql = SQL_SELECT
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA		
			+ COLUMN_KIS_ID + COMMA
			+ COLUMN_LOCAL_ADDRESS
			+ SQL_FROM + ObjectEntities.ME_ENTITY
			+ SQL_WHERE + COLUMN_ID + EQUALS + meIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MonitoredElement_Database.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				monitoredElement.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
											   DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
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
											   /**
												 * @todo when change DB Identifier model ,change getString() to
												 *       getLong()
												 */
											   new Identifier(resultSet.getString(DomainMember.COLUMN_DOMAIN_ID)),
											   /**
												 * @todo when change DB Identifier model ,change getString() to
												 *       getLong()
												 */
											   new Identifier(resultSet.getString(COLUMN_KIS_ID)),
											   resultSet.getString(COLUMN_LOCAL_ADDRESS));
			else
				throw new ObjectNotFoundException("No such monitored element: " + meIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "MonitoredElement_Database.retrieve | Cannot retrieve monitored element " + meIdStr;
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
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException{
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		try {
			this.insertMonitoredElement(monitoredElement);
		}
		catch (CreateObjectException coe) {
			try {
				connection.rollback();
			}
			catch (SQLException sqle) {
				Log.errorMessage("Exception in rolling back");
				Log.errorException(sqle);
			}
			throw coe;
		}
		try {
			connection.commit();
		}
		catch (SQLException sqle) {
			Log.errorMessage("Exception in commiting");
			Log.errorException(sqle);
		}
	}

	private void insertMonitoredElement(MonitoredElement monitoredElement) throws CreateObjectException {
		String meIdStr = monitoredElement.getId().toSQLString();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.ME_ENTITY + OPEN_BRACKET 
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_KIS_ID + COMMA
			+ COLUMN_LOCAL_ADDRESS
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET			
			+ meIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(monitoredElement.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(monitoredElement.getModified()) + COMMA
			+ monitoredElement.getCreatorId().toSQLString() + COMMA
			+ monitoredElement.getModifierId().toSQLString() + COMMA
			+ monitoredElement.getDomainId().toSQLString() + COMMA
			+ monitoredElement.getKISId().toSQLString() + COMMA
			+ APOSTOPHE + monitoredElement.getLocalAddress() + APOSTOPHE
			+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MonitoredElement_Database.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "MonitoredElement_Database.insert | Cannot insert monitored element " + meIdStr;
			throw new CreateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

	public void update(StorableObject storableObject, int update_kind, Object obj) throws IllegalDataException, UpdateObjectException {
		MonitoredElement monitoredElement = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}
}
