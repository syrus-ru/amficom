/*-
 * $Id: DeliveryAttributesDatabase.java,v 1.1 2005/11/11 05:19:19 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.event;

import static com.syrus.AMFICOM.general.ObjectEntities.DELIVERYATTRIBUTES_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/11/11 05:19:19 $
 * @module event
 */
public final class DeliveryAttributesDatabase extends
		StorableObjectDatabase<DeliveryAttributes> {
	/**
	 * @see StorableObjectDatabase#getEntityCode()
	 */
	@Override
	protected short getEntityCode() {
		return DELIVERYATTRIBUTES_CODE;
	}

	/**
	 * @see StorableObjectDatabase#getColumnsTmpl()
	 */
	@Override
	protected String getColumnsTmpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObjectDatabase#getUpdateMultipleSQLValuesTmpl()
	 */
	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObject
	 * @throws IllegalDataException
	 * @see StorableObjectDatabase#getUpdateSingleSQLValuesTmpl(com.syrus.AMFICOM.general.StorableObject)
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			final DeliveryAttributes storableObject)
	throws IllegalDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param deliveryAttributes
	 * @param preparedStatement
	 * @param startParameterNumber
	 * @throws IllegalDataException
	 * @throws SQLException
	 * @see StorableObjectDatabase#setEntityForPreparedStatementTmpl(com.syrus.AMFICOM.general.StorableObject, PreparedStatement, int)
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			final DeliveryAttributes deliveryAttributes,
			final PreparedStatement preparedStatement,
			final int startParameterNumber)
	throws IllegalDataException, SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param deliveryAttributes
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws RetrieveObjectException
	 * @throws SQLException
	 * @see StorableObjectDatabase#updateEntityFromResultSet(com.syrus.AMFICOM.general.StorableObject, ResultSet)
	 */
	@Override
	protected DeliveryAttributes updateEntityFromResultSet(
			final DeliveryAttributes deliveryAttributes,
			final ResultSet resultSet)
	throws IllegalDataException, RetrieveObjectException, SQLException {
		throw new UnsupportedOperationException();
	}
}
