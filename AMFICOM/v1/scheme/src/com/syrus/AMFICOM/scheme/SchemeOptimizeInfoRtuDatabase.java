/*-
 * $Id: SchemeOptimizeInfoRtuDatabase.java,v 1.10 2005/07/28 10:04:34 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFORTU_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.10 $, $Date: 2005/07/28 10:04:34 $
 * @module scheme
 */
public final class SchemeOptimizeInfoRtuDatabase extends StorableObjectDatabase<SchemeOptimizeInfoRtu> {
	/**
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#getEntityCode()
	 */
	@Override
	protected short getEntityCode() {
		return SCHEMEOPTIMIZEINFORTU_CODE;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#getColumnsTmpl()
	 */
	@Override
	protected String getColumnsTmpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#getUpdateMultipleSQLValuesTmpl()
	 */
	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObject
	 * @throws IllegalDataException
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#getUpdateSingleSQLValuesTmpl(com.syrus.AMFICOM.general.StorableObject)
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			SchemeOptimizeInfoRtu storableObject)
			throws IllegalDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObject
	 * @param preparedStatement
	 * @param startParameterNumber
	 * @throws IllegalDataException
	 * @throws SQLException
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#setEntityForPreparedStatementTmpl(com.syrus.AMFICOM.general.StorableObject, java.sql.PreparedStatement, int)
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			SchemeOptimizeInfoRtu storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException,
			SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws RetrieveObjectException
	 * @throws SQLException
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#updateEntityFromResultSet(com.syrus.AMFICOM.general.StorableObject, java.sql.ResultSet)
	 */
	@Override
	protected SchemeOptimizeInfoRtu updateEntityFromResultSet(
			SchemeOptimizeInfoRtu storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException,
			SQLException {
		throw new UnsupportedOperationException();
	}
}
