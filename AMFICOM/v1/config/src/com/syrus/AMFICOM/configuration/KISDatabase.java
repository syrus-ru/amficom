/*
 * $Id: KISDatabase.java,v 1.10 2004/08/09 14:13:54 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.10 $, $Date: 2004/08/09 14:13:54 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class KISDatabase extends StorableObjectDatabase {
	// table :: kis
    // description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
    // domain_id Identifier,
    public static final String COLUMN_DOMAIN_ID     = "domain_id";
    // name VARCHAR2(64) NOT NULL,
    public static final String COLUMN_NAME  = "name";	
	// mcm_id Identifier NOT NULL
	public static final String COLUMN_MCM_ID = "mcm_id";
	

	private KIS fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof KIS)
			return (KIS) storableObject;
		throw new IllegalDataException("KISDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		KIS kis = this.fromStorableObject(storableObject);
		this.retrieveKIS(kis);	
	}

	private void retrieveKIS(KIS kis) throws ObjectNotFoundException, RetrieveObjectException {
		String kisIdStr = kis.getId().toSQLString();
		String sql;		
		{
		StringBuffer buffer = new StringBuffer();
		buffer.append(StorableObjectDatabase.SQL_SELECT);
		buffer.append(DatabaseDate.toQuerySubString(StorableObjectDatabase.COLUMN_CREATED));
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(DatabaseDate.toQuerySubString(StorableObjectDatabase.COLUMN_MODIFIED));
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.COLUMN_CREATOR_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.COLUMN_MODIFIER_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(DomainMember.COLUMN_DOMAIN_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_NAME);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_DESCRIPTION);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_MCM_ID);
		buffer.append(StorableObjectDatabase.SQL_FROM);
		buffer.append(ObjectEntities.KIS_ENTITY);
		buffer.append(StorableObjectDatabase.SQL_WHERE);
		buffer.append(StorableObjectDatabase.COLUMN_ID);
		buffer.append(StorableObjectDatabase.EQUALS);
		buffer.append(kisIdStr);
		sql = buffer.toString();
		}
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("KISDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {				
				kis.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
													DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
													/**
														* @todo when change DB Identifier model ,change getString() to getLong()
														*/
													new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
													/**
														* @todo when change DB Identifier model ,change getString() to getLong()
														*/
													new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
													/**
														* @todo when change DB Identifier model ,change getString() to getLong()
														*/
													new Identifier(resultSet.getString(DomainMember.COLUMN_DOMAIN_ID)),													
													resultSet.getString(EquipmentDatabase.COLUMN_NAME),
													resultSet.getString(EquipmentDatabase.COLUMN_DESCRIPTION),													
													/**
														* @todo when change DB Identifier model ,change getString() to getLong()
														*/
													new Identifier(resultSet.getString(COLUMN_MCM_ID)));
			}
			else
				throw new ObjectNotFoundException("No such kis: " + kisIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "KISDatabase.retrieve | Cannot retrieve kis " + kisIdStr;
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
	
	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		KIS kis = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		KIS kis = this.fromStorableObject(storableObject);
		try {
			this.insertKIS(kis);
		}
		catch (CreateObjectException e) {
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

	private void insertKIS(KIS kis) throws CreateObjectException {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String kisIdCode = kis.getId().getCode();

		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		Identifier domainId = kis.getDomainId();
		
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		Identifier mcmId = kis.getMCMId();

		
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.KIS_ENTITY
			+ OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_MCM_ID
			+ CLOSE_BRACKET
			+ SQL_VALUES + OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION
			+ CLOSE_BRACKET;
		
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(1, kisIdCode);
			preparedStatement.setDate(2, new java.sql.Date(kis.getCreated().getTime()));
			preparedStatement.setDate(3, new java.sql.Date(kis.getModified().getTime()));
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(4, kis.getCreatorId().getCode());
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(5, kis.getModifierId().getCode());
			
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(6, (domainId != null)?domainId.getCode():Identifier.getNullSQLString());

			preparedStatement.setString(7, kis.getName());
			
			preparedStatement.setString(8, kis.getDescription());			
			
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(9, (mcmId != null)?mcmId.getCode():Identifier.getNullSQLString());
										
			Log.debugMessage("KISDatabase.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			preparedStatement.executeUpdate();
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "KISDatabase.insert | Cannot insert kis " + kisIdCode;
			throw new CreateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, UpdateObjectException {
		KIS kis = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				return;
		}
	}
}
