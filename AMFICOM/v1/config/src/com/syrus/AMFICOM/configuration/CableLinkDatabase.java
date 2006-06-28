/*-
 * $Id: CableLinkDatabase.java,v 1.3 2005/12/02 11:24:19 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.configuration;

import static com.syrus.AMFICOM.general.ObjectEntities.CABLELINK_CODE;

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
 * @author max
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/12/02 11:24:19 $
 * @module config
 */

public class CableLinkDatabase extends StorableObjectDatabase<CableLink> {
	private static final int SIZE_INVENTORY_NO_COLUMN  = 64;
	private static final int SIZE_SUPPLIER_COLUMN  = 128;
	private static final int SIZE_SUPPLIER_CODE_COLUMN  = 64;
	private static final int SIZE_MARK_COLUMN  = 32;
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected short getEntityCode() {		
		return CABLELINK_CODE;		
	}
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ CableLinkWrapper.COLUMN_INVENTORY_NO + COMMA
				+ CableLinkWrapper.COLUMN_SUPPLIER + COMMA
				+ CableLinkWrapper.COLUMN_SUPPLIER_CODE + COMMA
				+ CableLinkWrapper.COLUMN_COLOR + COMMA
				+ CableLinkWrapper.COLUMN_MARK;
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
	protected String getUpdateSingleSQLValuesTmpl(final CableLink storableObject) throws IllegalDataException {
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
	protected int setEntityForPreparedStatementTmpl(final CableLink storableObject,
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
	protected CableLink updateEntityFromResultSet(final CableLink storableObject, final ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		CableLink cableLink = storableObject == null
				? new CableLink(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
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
		final String inventoryNo = DatabaseString.fromQuerySubString(resultSet.getString(CableLinkWrapper.COLUMN_INVENTORY_NO));
		final String supplier = DatabaseString.fromQuerySubString(resultSet.getString(CableLinkWrapper.COLUMN_SUPPLIER));
		final String supplierCode = DatabaseString.fromQuerySubString(resultSet.getString(CableLinkWrapper.COLUMN_SUPPLIER_CODE));
		CableLinkType cableLinkType;
		try {
			cableLinkType = StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					StorableObjectWrapper.COLUMN_TYPE_ID), true);
		} catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		cableLink.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),
				(name != null) ? name : "",
				(description != null) ? description : "",
				cableLinkType,
				(inventoryNo != null) ? inventoryNo : "",
				(supplier != null) ? supplier : "",
				(supplierCode != null) ? supplierCode : "",
				resultSet.getInt(CableLinkWrapper.COLUMN_COLOR),
				DatabaseString.fromQuerySubString(resultSet.getString(CableLinkWrapper.COLUMN_MARK)));

		return cableLink;
	}
}
