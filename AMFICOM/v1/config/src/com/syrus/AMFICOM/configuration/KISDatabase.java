package com.syrus.AMFICOM.configuration;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.configuration.corba.EquipmentSort;

public class KISDatabase extends StorableObjectDatabase {
	
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
		String mIdStr = kis.getId().toSQLString();
		String sql = EquipmentDatabase.retriveSQL(kis,
													 ObjectEntities.KIS_ENTITY,
													 COLUMN_MCM_ID,
													 EquipmentDatabase.COLUMN_SORT + EQUALS + EquipmentSort._EQUIPMENT_SORT_KIS);
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("KISDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				kis.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED), DatabaseDate
						.fromQuerySubString(resultSet, COLUMN_MODIFIED),
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
				new Identifier(resultSet.getString(EquipmentDatabase.COLUMN_TYPE_ID)),
				resultSet.getString(EquipmentDatabase.COLUMN_NAME),
				resultSet.getString(EquipmentDatabase.COLUMN_DESCRIPTION),
				resultSet.getString(EquipmentDatabase.COLUMN_LATITUDE),
				resultSet.getString(EquipmentDatabase.COLUMN_LONGITUDE),
				resultSet.getString(EquipmentDatabase.COLUMN_HW_SERIAL),
				resultSet.getString(EquipmentDatabase.COLUMN_SW_SERIAL),
				resultSet.getString(EquipmentDatabase.COLUMN_HW_VERSION),
				resultSet.getString(EquipmentDatabase.COLUMN_SW_VERSION),
				resultSet.getString(EquipmentDatabase.COLUMN_INVENTORY_NUMBER),
				resultSet.getString(EquipmentDatabase.COLUMN_MANUFACTURER),
				resultSet.getString(EquipmentDatabase.COLUMN_MANUFACTURER_CODE),
				resultSet.getString(EquipmentDatabase.COLUMN_SUPPLIER),
				resultSet.getString(EquipmentDatabase.COLUMN_SUPPLIER_CODE),
				resultSet.getString(EquipmentDatabase.COLUMN_EQCLASS),
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				new Identifier(resultSet.getString(EquipmentDatabase.COLUMN_IMAGE_ID)),
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				new Identifier(resultSet.getString(COLUMN_MCM_ID)));
			}
			else
				throw new ObjectNotFoundException("No such kis: " + mIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "KISDatabase.retrieve | Cannot retrieve kis " + mIdStr;
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
		String cIdStr = kis.getId().toSQLString();		
		String sql = EquipmentDatabase.insertSQL(kis,ObjectEntities.KIS_ENTITY,COLUMN_MCM_ID,kis.getMCMId().toSQLString());
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("KISDatabase.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "KISDatabase.insert | Cannot insert kis " + cIdStr;
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
		KIS kis = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				return;
		}
	}
}
