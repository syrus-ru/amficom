/*
 * $Id: AbstractDatabaseLinkedIdsCondition.java,v 1.11 2005/03/05 21:31:51 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.11 $, $Date: 2005/03/05 21:31:51 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public abstract class AbstractDatabaseLinkedIdsCondition implements DatabaseStorableObjectCondition {

	protected LinkedIdsCondition	condition;

	public AbstractDatabaseLinkedIdsCondition(LinkedIdsCondition delegate) {
		this.condition = delegate;
	}

	public Short getEntityCode() {
		return this.condition.getEntityCode();
	}

	protected String getQuery(String columnName) throws IllegalDataException {

		if (columnName == null)
			throw new IllegalDataException("AbstractDatabaseLinkedIdsCondition.getQuery | "
					+ ObjectEntities.codeToString(this.getEntityCode()) + " isn't supported");
		StringBuffer buffer = new StringBuffer();
		buffer.append(" 1=0 ");
		buffer.append(StorableObjectDatabase.SQL_OR);
		buffer.append(StorableObjectDatabase.idsEnumerationString(this.condition.getLinkedIds(), columnName, true));
		return buffer.toString();
	}

	protected String getLinkedQuery(final String linkedColumnIdName,
									final String linkedColumnTargetName,
									final String linkedTableName) throws IllegalDataException {
		return this.getLinkedQuery(StorableObjectWrapper.COLUMN_ID, linkedColumnIdName, linkedColumnTargetName,
			linkedTableName);

	}

	protected String getLinkedQuery(final String columnName,
									final String linkedColumnIdName,
									final String linkedColumnTargetName,
									final String linkedTableName) throws IllegalDataException {
		StringBuffer query = new StringBuffer();
		query.append(" 1=0 ");
		query.append(StorableObjectDatabase.SQL_OR);
		query.append(columnName);
		query.append(StorableObjectDatabase.SQL_IN);
		query.append(StorableObjectDatabase.OPEN_BRACKET);
		query.append(StorableObjectDatabase.SQL_SELECT);
		query.append(linkedColumnIdName);
		query.append(StorableObjectDatabase.SQL_FROM);
		query.append(linkedTableName);
		query.append(StorableObjectDatabase.SQL_WHERE);
		query.append(this.getQuery(linkedColumnTargetName));
		return query.toString();
	}

}
