/*
 * $Id: ImageResourceDatabase.java,v 1.1 2004/12/03 19:11:29 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.*;
import java.sql.*;
import java.util.List;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/12/03 19:11:29 $
 * @module resource_v1
 */
final class ImageResourceDatabase extends StorableObjectDatabase {

	public void insert(List storableObjects) throws IllegalDataException,
			CreateObjectException {
		throw new UnsupportedOperationException();
	}
	public void insert(StorableObject storableObject)
			throws IllegalDataException, CreateObjectException {
		throw new UnsupportedOperationException();
	}

	public void retrieve(StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException,
			RetrieveObjectException {
		throw new UnsupportedOperationException();
	}

	public List retrieveByCondition(List ids,
			StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		throw new UnsupportedOperationException();
	}

	public List retrieveByIds(List ids, String condition)
			throws IllegalDataException, RetrieveObjectException {
		throw new UnsupportedOperationException();
	}

	public Object retrieveObject(StorableObject storableObject,
			int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException,
			RetrieveObjectException {
		throw new UnsupportedOperationException();
	}

	public void update(List storableObjects, int updateKind, Object arg)
			throws IllegalDataException, VersionCollisionException,
			UpdateObjectException {
		throw new UnsupportedOperationException();
	}

	public void update(StorableObject storableObject, int updateKind,
			Object arg) throws IllegalDataException,
			VersionCollisionException, UpdateObjectException {
		throw new UnsupportedOperationException();
	}

	protected String getEnityName() {
		throw new UnsupportedOperationException();
	}

	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException,
			SQLException {
		throw new UnsupportedOperationException();
	}
}
