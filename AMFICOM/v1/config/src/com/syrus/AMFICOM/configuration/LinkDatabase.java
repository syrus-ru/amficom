/*
 * $Id: LinkDatabase.java,v 1.46 2005/07/17 05:19:00 arseniy Exp $
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
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.46 $, $Date: 2005/07/17 05:19:00 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public final class LinkDatabase extends StorableObjectDatabase {
	private static final int SIZE_INVENTORY_NO_COLUMN  = 64;

	private static final int SIZE_SUPPLIER_COLUMN  = 128;

	private static final int SIZE_SUPPLIER_CODE_COLUMN  = 64;

	private static final int SIZE_MARK_COLUMN  = 32;

	private static String columns;
	private static String updateMultipleSQLValues;

	private Link fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Link)
			return (Link) storableObject;
		throw new IllegalDataException("LinkDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.LINK_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
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
	
	@Override
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
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}
	
	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final Link link = this.fromStorableObject(storableObject);
		final String inventoryNo = DatabaseString.toQuerySubString(link.getInventoryNo(), SIZE_INVENTORY_NO_COLUMN);
		final String supplier = DatabaseString.toQuerySubString(link.getSupplier(), SIZE_SUPPLIER_COLUMN);
		final String supplierCode = DatabaseString.toQuerySubString(link.getSupplierCode(), SIZE_SUPPLIER_CODE_COLUMN);
		final String mark = DatabaseString.toQuerySubString(link.getMark(),SIZE_MARK_COLUMN);
		final String sql = DatabaseIdentifier.toSQLString(link.getDomainId()) + COMMA
			+ DatabaseIdentifier.toSQLString(link.getType().getId()) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(link.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(link.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + (inventoryNo != null ? inventoryNo : "") + APOSTROPHE + COMMA
			+ APOSTROPHE + (supplier != null ? supplier : "") + APOSTROPHE + COMMA
			+ APOSTROPHE + (supplierCode != null ? supplierCode : "") + APOSTROPHE + COMMA
			+ APOSTROPHE + link.getColor() + APOSTROPHE + COMMA
			+ APOSTROPHE + (mark != null ? mark : "") + APOSTROPHE;
		return sql;
	}
	
	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final Link link = this.fromStorableObject(storableObject);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, link.getDomainId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, link.getType().getId());
		preparedStatement.setString( ++startParameterNumber, link.getName());
		preparedStatement.setString( ++startParameterNumber, link.getDescription());
		preparedStatement.setString( ++startParameterNumber, link.getInventoryNo());
		preparedStatement.setString( ++startParameterNumber, link.getSupplier());
		preparedStatement.setString( ++startParameterNumber, link.getSupplierCode());
		preparedStatement.setInt( ++startParameterNumber, link.getColor());
		preparedStatement.setString( ++startParameterNumber, link.getMark());
		return startParameterNumber;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
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
					null);
		}
		final String name = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME));
		final String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));
		final String inventoryNo = DatabaseString.fromQuerySubString(resultSet.getString(LinkWrapper.COLUMN_INVENTORY_NO));
		final String supplier = DatabaseString.fromQuerySubString(resultSet.getString(LinkWrapper.COLUMN_SUPPLIER));
		final String supplierCode = DatabaseString.fromQuerySubString(resultSet.getString(LinkWrapper.COLUMN_SUPPLIER_CODE));
		LinkType linkType;
		try {
			linkType = (LinkType) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					StorableObjectWrapper.COLUMN_TYPE_ID), true);
		} catch (ApplicationException ae) {
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
				resultSet.getInt(LinkWrapper.COLUMN_COLOR),
				DatabaseString.fromQuerySubString(resultSet.getString(LinkWrapper.COLUMN_MARK)));

		return link;
	}

	@Override
	public Object retrieveObject(final StorableObject storableObject, final int retrieveKind, final Object arg) throws IllegalDataException {
		final Link link = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  link.getId() + "'; argument: " + arg);
				return null;
		}
	}

}
