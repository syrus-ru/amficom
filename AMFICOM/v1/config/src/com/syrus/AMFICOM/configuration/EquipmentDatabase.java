/*
 * $Id: EquipmentDatabase.java,v 1.7 2004/07/28 12:49:46 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $ $, $ $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class EquipmentDatabase {

//	 description VARCHAR2(256),
	public static final String COLUMN_DESCRIPTION	= "description";
	public static final String COLUMN_EQCLASS	= "eq_class";
	// hw_serial VARCHAR2(256),
	public static final String COLUMN_HW_SERIAL	= "hw_serial";
	// hw_version VARCHAR2(32),
	public static final String COLUMN_HW_VERSION	= "hw_version";
	// image_id VARCHAR2(32),
	public static final String COLUMN_IMAGE_ID	= "image_id";
	// inventory_number VARCHAR2(64),
	public static final String COLUMN_INVENTORY_NUMBER	= "inventory_number";
	// latitude VARCHAR2(10),
	public static final String COLUMN_LATITUDE	= "latitude";
	// longitude VARCHAR2(10),
	public static final String COLUMN_LONGITUDE	= "longitude";
	// manufacturer VARCHAR2(64),
	public static final String COLUMN_MANUFACTURER	= "manufacturer";
	// manufacturer_code VARCHAR2(64),
	public static final String COLUMN_MANUFACTURER_CODE	= "manufacturer_code";
	// mcm_id VARCHAR2(32) NOT NULL,
	public static final String COLUMN_MCM_ID	= "mcm_id";
	// name VARCHAR2(64) NOT NULL,
	public static final String COLUMN_NAME	= "name";
	// sort NUMBER(2) NOT NULL,
	public static final String COLUMN_SORT	= "sort";
	// supplier VARCHAR2(64),
	public static final String COLUMN_SUPPLIER	= "supplier";
	// supplier_code VARCHAR2(64),
	public static final String COLUMN_SUPPLIER_CODE	= "supplier_code";
	// sw_serial VARCHAR2(256),
	public static final String COLUMN_SW_SERIAL	= "sw_serial";
	// sw_version VARCHAR2(32),
	public static final String COLUMN_SW_VERSION	= "sw_version";
	// type_id VARCHAR2(32) NOT NULL,
	public static final String COLUMN_TYPE_ID	= "type_id";
	
	private EquipmentDatabase(){
		// nothing
	}

	protected static String retriveSQL(Equipment eq,
												   String tableName,
												   String columnSQL,
												   String whereSQL ){
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
		buffer.append(COLUMN_TYPE_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_NAME);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_DESCRIPTION);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_SORT);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_LATITUDE);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_LONGITUDE);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_HW_SERIAL);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_SW_SERIAL);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_HW_VERSION);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_SW_VERSION);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_INVENTORY_NUMBER);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_MANUFACTURER);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_MANUFACTURER_CODE);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_SUPPLIER);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_SUPPLIER_CODE);		
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_EQCLASS);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_IMAGE_ID);
		if ((columnSQL!=null)&&(columnSQL.length()>0)){
			buffer.append(StorableObjectDatabase.COMMA);
			buffer.append(columnSQL);
		}
		buffer.append(StorableObjectDatabase.SQL_FROM);
		buffer.append(tableName);
		buffer.append(StorableObjectDatabase.SQL_WHERE);
		buffer.append(StorableObjectDatabase.COLUMN_ID);
		buffer.append(StorableObjectDatabase.EQUALS);
		buffer.append(eq.getId().toSQLString());
		if ((whereSQL!=null)&&(whereSQL.length()>0)){
			buffer.append(StorableObjectDatabase.SQL_AND);
			buffer.append(whereSQL);
		}

		return buffer.toString();
	}


	protected static String insertSQL(Equipment eq, 
						   String tableName,
						   String columnSQL,
						   String valuesSQL  ) {
		String cIdStr = eq.getId().toSQLString();		
		StringBuffer buffer = new StringBuffer(StorableObjectDatabase.SQL_INSERT_INTO);
		buffer.append(tableName);
		buffer.append(StorableObjectDatabase.OPEN_BRACKET);
		buffer.append(StorableObjectDatabase.COLUMN_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.COLUMN_CREATED);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.COLUMN_MODIFIED);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.COLUMN_CREATOR_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.COLUMN_MODIFIER_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(DomainMember.COLUMN_DOMAIN_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_TYPE_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_NAME);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_DESCRIPTION);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_SORT);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_LATITUDE);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_LONGITUDE);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_HW_SERIAL);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_SW_SERIAL);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_HW_VERSION);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_SW_VERSION);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_INVENTORY_NUMBER);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_MANUFACTURER);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_MANUFACTURER_CODE);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_SUPPLIER);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_SUPPLIER_CODE);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_EQCLASS);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_IMAGE_ID);
		if ((columnSQL!=null)&&(columnSQL.length()>0)){
			buffer.append(StorableObjectDatabase.COMMA);
			buffer.append(columnSQL);
		}			
		buffer.append(StorableObjectDatabase.CLOSE_BRACKET);
		buffer.append(StorableObjectDatabase.SQL_VALUES);
		buffer.append(StorableObjectDatabase.OPEN_BRACKET);
		buffer.append(cIdStr);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(DatabaseDate.toUpdateSubString(eq.getCreated()));
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(DatabaseDate.toUpdateSubString(eq.getModified()));
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(eq.getCreatorId().toSQLString());
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(eq.getModifierId().toSQLString());
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(eq.getDomainId().toSQLString());			
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(eq.getType().getId().toSQLString());
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(eq.getName());
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(eq.getDescription());
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(eq.getSort());
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(eq.getLatitude());
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(eq.getLongitude());
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(eq.getHWSerial());
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(eq.getSWSerial());
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(eq.getHWVersion());
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(eq.getSWVersion());
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(eq.getInventoryNumber());
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(eq.getManufacturer());
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(eq.getManufacturerCode());
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(eq.getSupplier());
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(eq.getSupplierCode());
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(eq.getEqClass());
		buffer.append(StorableObjectDatabase.APOSTOPHE);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(eq.getImageId().toSQLString());
		if ((valuesSQL!=null)&&(valuesSQL.length()>0)){
			buffer.append(StorableObjectDatabase.COMMA);
			buffer.append(valuesSQL);
		}
		buffer.append(StorableObjectDatabase.CLOSE_BRACKET);
		return buffer.toString();		
	}
}
