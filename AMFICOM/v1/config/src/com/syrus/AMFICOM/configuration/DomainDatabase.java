/*
 * $Id: DomainDatabase.java,v 1.5 2004/08/30 14:39:41 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;


/**
 * @version $Revision: 1.5 $, $Date: 2004/08/30 14:39:41 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class DomainDatabase extends StorableObjectDatabase {
	public static final String COLUMN_NAME  = "name";
	public static final String COLUMN_DESCRIPTION   = "description";

	private Domain fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Domain)
			return (Domain) storableObject;
		throw new IllegalDataException("DomainDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
		Domain domain = this.fromStorableObject(storableObject);
		this.retrieveDomain(domain);
		domain.setCharacteristics(characteristicDatabase.retrieveCharacteristics(domain.getId(), CharacteristicSort.CHARACTERISTIC_SORT_DOMAIN));
	}

	private String retrieveDomainQuery(String condition){
		return SQL_SELECT
		+ COLUMN_ID + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
		+ COLUMN_CREATOR_ID + COMMA
		+ COLUMN_MODIFIER_ID + COMMA
		+ DomainMember.COLUMN_DOMAIN_ID + COMMA
		+ COLUMN_NAME + COMMA
		+ COLUMN_DESCRIPTION
		+ SQL_FROM + ObjectEntities.DOMAIN_ENTITY
		+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}
	
	private Domain updateDomainFromResultSet(Domain domain, ResultSet resultSet) throws SQLException{
		Domain domain1 = domain;
		if (domain == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			domain1 = new Domain(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, null);			
		}
		/**
		 * @todo when change DB Identifier model ,change getString() to getLong()
		 */
		String idCode = resultSet.getString(DomainMember.COLUMN_DOMAIN_ID);
		domain1.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
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
												 (idCode != null) ? (new Identifier(idCode)) : null,
												 resultSet.getString(COLUMN_NAME),
												 resultSet.getString(COLUMN_DESCRIPTION));
		
		return domain1;
	}
	
	
	private void retrieveDomain(Domain domain) throws ObjectNotFoundException, RetrieveObjectException {
		String domainIdStr = domain.getId().toSQLString();
		String sql = retrieveDomainQuery(COLUMN_ID + EQUALS + domainIdStr);

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("DomainDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) 
				updateDomainFromResultSet(domain, resultSet);
			else
				throw new ObjectNotFoundException("No such domain: " + domainIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "DomainDatabase.retrieve | Cannot retrieve domain " + domainIdStr;
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

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Domain domain = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Domain domain = this.fromStorableObject(storableObject);
		try {
			this.insertDomain(domain);
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

	private void insertDomain(Domain domain) throws CreateObjectException {
		String domainIdStr = domain.getId().toSQLString();

		Identifier domainId = domain.getDomainId();
		String domainIdSubstr = (domainId != null) ? domainId.toSQLString() : Identifier.getNullSQLString();

		String sql = SQL_INSERT_INTO
			+ ObjectEntities.DOMAIN_ENTITY + OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ domainIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(domain.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(domain.getModified()) + COMMA
			+ domain.getCreatorId().toSQLString() + COMMA
			+ domain.getModifierId().toSQLString() + COMMA
			+ domainIdSubstr + COMMA
			+ APOSTOPHE + domain.getName() + APOSTOPHE + COMMA
			+ APOSTOPHE + domain.getDescription() + APOSTOPHE
			+ CLOSE_BRACKET;

		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("DomainDatabase.insertDomain | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "DomainDatabase.insertDomain | Cannot insert domain " + domainIdStr;
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

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, UpdateObjectException {
		Domain domain = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				return;
		}
	}
	
	public List retrieveByIds(List ids) throws RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return retriveByIdsOneQuery(null);
		return retriveByIdsOneQuery(ids);	
		//return retriveByIdsPreparedStatement(ids);
	}
	
	private List retriveByIdsOneQuery(List ids) throws RetrieveObjectException {
		List result = new LinkedList();
		String sql;
		{
			String condition = null;
			if (ids!=null){
				StringBuffer buffer = new StringBuffer(COLUMN_ID);
				int idsLength = ids.size();
				if (idsLength == 1){
					buffer.append(EQUALS);
					buffer.append(((Identifier)ids.iterator().next()).toSQLString());
				} else{
					buffer.append(SQL_IN);
					buffer.append(OPEN_BRACKET);
					
					int i = 1;
					for(Iterator it=ids.iterator();it.hasNext();i++){
						Identifier id = (Identifier)it.next();
						buffer.append(id.toSQLString());
						if (i < idsLength)
							buffer.append(COMMA);
					}
					
					buffer.append(CLOSE_BRACKET);
					condition = buffer.toString();
				}
			}
			sql = retrieveDomainQuery(condition);
		}
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("DomainDatabase.retriveByIdsOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				result.add(updateDomainFromResultSet(null, resultSet));
			}
		}
		catch (SQLException sqle) {
			String mesg = "DomainDatabase.retriveByIdsOneQuery | Cannot execute query " + sqle.getMessage();
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
		return result;
	}
	
	private List retriveByIdsPreparedStatement(List ids) throws RetrieveObjectException {
		List result = new LinkedList();
		String sql;
		{
			
			int idsLength = ids.size();
			if (idsLength == 1){
				return retriveByIdsOneQuery(ids);
			}
			StringBuffer buffer = new StringBuffer(COLUMN_ID);
			buffer.append(EQUALS);							
			buffer.append(QUESTION);
			
			sql = retrieveDomainQuery(buffer.toString());
		}
			
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		try {
			stmt = connection.prepareStatement(sql.toString());
			for(Iterator it = ids.iterator();it.hasNext();){
				Identifier id = (Identifier)it.next(); 
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				String idStr = id.getIdentifierString();
				stmt.setString(1, idStr);
				resultSet = stmt.executeQuery();
				if (resultSet.next()){
					result.add(updateDomainFromResultSet(null, resultSet));
				} else{
					Log.errorMessage("DomainDatabase.retriveByIdsPreparedStatement | No such domain: " + idStr);									
				}
				
			}
		}catch (SQLException sqle) {
			String mesg = "DomainDatabase.retriveByIdsPreparedStatement | Cannot retrieve domain " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				if (stmt != null)
					stmt.close();
				if (stmt != null)
					stmt.close();
				stmt = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}			
		
		return result;
	}

}
