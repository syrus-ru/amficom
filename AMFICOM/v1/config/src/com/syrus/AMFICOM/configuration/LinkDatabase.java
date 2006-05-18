/*
 * $Id: LinkDatabase.java,v 1.51 2005/12/02 11:24:19 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ObjectEntities.LINK_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.51 $, $Date: 2005/12/02 11:24:19 $
 * @author $Author: bass $
 * @module config
 */

public final class LinkDatabase extends StorableObjectDatabase<Link> {
	private static final int SIZE_INVENTORY_NO_COLUMN  = 64;
	private static final int SIZE_SUPPLIER_COLUMN  = 128;
	private static final int SIZE_SUPPLIER_CODE_COLUMN  = 64;
	private static final int SIZE_MARK_COLUMN  = 32;

	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return LINK_CODE;
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
	protected String getUpdateSingleSQLValuesTmpl(final Link storableObject) throws IllegalDataException {
		final String inventoryNo = DatabaseString.toQuerySubString(storableObject.getInventoryNo(), SIZE_INVENTORY_NO_COLUMN);
		final String supplier = DatabaseString.toQuerySubString(storableObject.getSupplier(), SIZE_SUPPLIER_COLUMN);
		final String supplierCode = DatabaseString.toQuerySubString(storableObject.getSupplierCode(), SIZE_SUPPLIER_CODE_COLUMN);
		final String mark = DatabaseString.toQuerySubString(storableObject.getMark(),SIZE_MARK_COLUMN);
		final String sql = DatabaseIdentifier.toSQLString(storableObject.getDomainId()) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getType().getId()) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + (inventoryNo != null ? inventoryNo : "") + APOSTROPHE + COMMA
			+ APOSTROPHE + (supplier != null ? supplier : "") + APOSTROPHE + COMMA
			+ APOSTROPHE + (supplierCode != null ? supplierCode : "") + APOSTROPHE + COMMA
			+ APOSTROPHE + storableObject.getColor() + APOSTROPHE + COMMA
			+ APOSTROPHE + (mark != null ? mark : "") + APOSTROPHE;
		return sql;
	}
	
	@Override
	protected int setEntityForPreparedStatementTmpl(final Link storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getDomainId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getType().getId());
		preparedStatement.setString( ++startParameterNumber, storableObject.getName());
		preparedStatement.setString( ++startParameterNumber, storableObject.getDescription());
		preparedStatement.setString( ++startParameterNumber, storableObject.getInventoryNo());
		preparedStatement.setString( ++startParameterNumber, storableObject.getSupplier());
		preparedStatement.setString( ++startParameterNumber, storableObject.getSupplierCode());
		preparedStatement.setInt( ++startParameterNumber, storableObject.getColor());
		preparedStatement.setString( ++startParameterNumber, storableObject.getMark());
		return startParameterNumber;
	}

	@Override
	protected Link updateEntityFromResultSet(final Link storableObject, final ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Link link = storableObject == null
				? new Link(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						null)
				: storableObject;
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
				StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
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

}
