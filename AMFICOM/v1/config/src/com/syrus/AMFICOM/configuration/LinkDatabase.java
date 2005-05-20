/*
 * $Id: LinkDatabase.java,v 1.39 2005/05/20 21:11:34 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacterizableDatabase;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.39 $, $Date: 2005/05/20 21:11:34 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public class LinkDatabase extends CharacterizableDatabase {
	private static final int SIZE_INVENTORY_NO_COLUMN  = 64;

	private static final int SIZE_SUPPLIER_COLUMN  = 128;

	private static final int SIZE_SUPPLIER_CODE_COLUMN  = 64;

	private static final int SIZE_MARK_COLUMN  = 32;

	private static String columns;
	private static String updateMultipleSQLValues;

	private Link fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Link)
			return (Link)storableObject;
		throw new IllegalDataException("LinkDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.LINK_ENTITY;
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ LinkWrapper.COLUMN_SORT + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ LinkWrapper.COLUMN_INVENTORY_NO + COMMA
				+ LinkWrapper.COLUMN_SUPPLIER + COMMA
				+ LinkWrapper.COLUMN_SUPPLIER_CODE + COMMA
				+ LinkWrapper.COLUMN_COLOR + COMMA
				+ LinkWrapper.COLUMN_MARK;
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
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}
	
	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		Link link = this.fromStorableObject(storableObject);
		String inventoryNo = DatabaseString.toQuerySubString(link.getInventoryNo(), SIZE_INVENTORY_NO_COLUMN);
		String supplier = DatabaseString.toQuerySubString(link.getSupplier(), SIZE_SUPPLIER_COLUMN);
		String supplierCode = DatabaseString.toQuerySubString(link.getSupplierCode(), SIZE_SUPPLIER_CODE_COLUMN);
		String mark = DatabaseString.toQuerySubString(link.getMark(),SIZE_MARK_COLUMN);
		String sql = DatabaseIdentifier.toSQLString(link.getDomainId()) + COMMA
			+ DatabaseIdentifier.toSQLString(link.getType().getId()) + COMMA
			+ link.getSort().value() + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(link.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(link.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + (inventoryNo != null ? inventoryNo : "") + APOSTOPHE + COMMA
			+ APOSTOPHE + (supplier != null ? supplier : "") + APOSTOPHE + COMMA
			+ APOSTOPHE + (supplierCode != null ? supplierCode : "") + APOSTOPHE + COMMA
			+ APOSTOPHE + link.getColor() + APOSTOPHE + COMMA
			+ APOSTOPHE + (mark != null ? mark : "") + APOSTOPHE;
		return sql;
	}
	
	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		Link link = this.fromStorableObject(storableObject);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, link.getDomainId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, link.getType().getId());
		preparedStatement.setInt( ++startParameterNumber, link.getSort().value());
		preparedStatement.setString( ++startParameterNumber, link.getName());
		preparedStatement.setString( ++startParameterNumber, link.getDescription());
		preparedStatement.setString( ++startParameterNumber, link.getInventoryNo());
		preparedStatement.setString( ++startParameterNumber, link.getSupplier());
		preparedStatement.setString( ++startParameterNumber, link.getSupplierCode());
		preparedStatement.setInt( ++startParameterNumber, link.getColor());
		preparedStatement.setString( ++startParameterNumber, link.getMark());
		return startParameterNumber;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Link link = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (link == null) {
			link = new Link(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					null,
					0L,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					0,
					0,
					null);
		}
		String name = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME));
		String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));
		String inventoryNo = DatabaseString.fromQuerySubString(resultSet.getString(LinkWrapper.COLUMN_INVENTORY_NO));
		String supplier = DatabaseString.fromQuerySubString(resultSet.getString(LinkWrapper.COLUMN_SUPPLIER));
		String supplierCode = DatabaseString.fromQuerySubString(resultSet.getString(LinkWrapper.COLUMN_SUPPLIER_CODE));
		AbstractLinkType linkType;
		try {
			linkType = (AbstractLinkType) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					StorableObjectWrapper.COLUMN_TYPE_ID), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		link.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),
				(name != null) ? name : "",
				(description != null) ? description : "",
				linkType,
				(inventoryNo != null) ? inventoryNo : "",
				(supplier != null) ? supplier : "",
				(supplierCode != null) ? supplierCode : "",
				resultSet.getInt(LinkWrapper.COLUMN_SORT),
				resultSet.getInt(LinkWrapper.COLUMN_COLOR),
				DatabaseString.fromQuerySubString(resultSet.getString(LinkWrapper.COLUMN_MARK)));

		return link;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException {
		Link link = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  link.getId() + "'; argument: " + arg);
				return null;
		}
	}

}
