/*-
 * $Id: SchemeOptimizeInfoRtuDatabase.java,v 1.3 2005/06/15 13:17:17 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/06/15 13:17:17 $
 * @module scheme_v1
 */
public final class SchemeOptimizeInfoRtuDatabase extends StorableObjectDatabase {
	/**
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#getEntityCode()
	 */
	protected short getEntityCode() {
		return ObjectEntities.SCHEME_OPTIMIZE_INFO_RTU_ENTITY_CODE;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#getColumnsTmpl()
	 */
	protected String getColumnsTmpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#getUpdateMultipleSQLValuesTmpl()
	 */
	protected String getUpdateMultipleSQLValuesTmpl() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObject
	 * @throws IllegalDataException
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#getUpdateSingleSQLValuesTmpl(com.syrus.AMFICOM.general.StorableObject)
	 */
	protected String getUpdateSingleSQLValuesTmpl(
			StorableObject storableObject)
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
	protected int setEntityForPreparedStatementTmpl(
			StorableObject storableObject,
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
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException,
			SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObject
	 * @throws IllegalDataException
	 * @throws ObjectNotFoundException
	 * @throws RetrieveObjectException
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#retrieve(com.syrus.AMFICOM.general.StorableObject)
	 */
	public void retrieve(StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException,
			RetrieveObjectException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObject
	 * @param retrieveKind
	 * @param arg
	 * @throws IllegalDataException
	 * @throws ObjectNotFoundException
	 * @throws RetrieveObjectException
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#retrieveObject(com.syrus.AMFICOM.general.StorableObject, int, java.lang.Object)
	 */
	public Object retrieveObject(StorableObject storableObject,
			int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException,
			RetrieveObjectException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObject
	 * @throws IllegalDataException
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#insert(com.syrus.AMFICOM.general.StorableObject)
	 */
	public void insert(StorableObject storableObject)
			throws IllegalDataException, CreateObjectException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjects
	 * @throws IllegalDataException
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#insert(java.util.Set)
	 */
	public void insert(Set storableObjects) throws IllegalDataException,
			CreateObjectException {
		throw new UnsupportedOperationException();
	}
}
