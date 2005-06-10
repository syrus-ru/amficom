/*
 * $Id: AbstractDatabaseLinkedIdsCondition.java,v 1.18 2005/06/10 18:39:53 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.18 $, $Date: 2005/06/10 18:39:53 $
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

	protected String getQuery(final String columnName) {
		assert (columnName != null) : "Column name is null -- maybe entity '"
				+ ObjectEntities.codeToString(this.getEntityCode()) + " isn't supported";

		return StorableObjectDatabase.idsEnumerationString(this.condition.getLinkedIds(), columnName, true).toString();
	}

	protected String getLinkedQuery(final String linkedColumnIdName,
									final String linkedColumnTargetName,
									final String linkedTableName) {
		return this.getLinkedQuery(StorableObjectWrapper.COLUMN_ID,
				linkedColumnIdName,
				linkedColumnTargetName,
				linkedTableName);

	}

	protected String getLinkedQuery(final String columnName,
									final String linkedColumnIdName,
									final String linkedColumnTargetName,
									final String linkedTableName) {
		StringBuffer query = new StringBuffer();
		query.append(columnName);
		query.append(StorableObjectDatabase.SQL_IN);
		query.append(StorableObjectDatabase.OPEN_BRACKET);
		query.append(StorableObjectDatabase.SQL_SELECT);
		query.append(linkedColumnIdName);
		query.append(StorableObjectDatabase.SQL_FROM);
		query.append(linkedTableName);
		query.append(StorableObjectDatabase.SQL_WHERE);
		query.append(this.getQuery(linkedColumnTargetName));
		query.append(StorableObjectDatabase.CLOSE_BRACKET);
		return query.toString();
	}

	protected IllegalObjectEntityException newIllegalObjectEntityException() {
		final short entityCode = getEntityCode().shortValue();
		return new IllegalObjectEntityException(
				"Unsupported entity type -- " + entityCode
				+ ", " + ObjectEntities.codeToString(entityCode),
				IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

}
