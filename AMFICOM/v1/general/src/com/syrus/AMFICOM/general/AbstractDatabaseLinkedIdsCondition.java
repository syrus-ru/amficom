/*
 * $Id: AbstractDatabaseLinkedIdsCondition.java,v 1.28 2006/03/16 08:38:51 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_FROM;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_IN;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_SELECT;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_WHERE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;

/**
 * @version $Revision: 1.28 $, $Date: 2006/03/16 08:38:51 $
 * @author $Author: arseniy $
 * @module general
 */
public abstract class AbstractDatabaseLinkedIdsCondition implements DatabaseStorableObjectCondition {

	protected LinkedIdsCondition condition;

	public AbstractDatabaseLinkedIdsCondition(final LinkedIdsCondition delegate) {
		this.condition = delegate;
	}

	public Short getEntityCode() {
		return this.condition.getEntityCode();
	}

	protected String getQuery(final String columnName) {
		assert (columnName != null) : "Column name is null -- maybe entity '"
				+ ObjectEntities.codeToString(this.getEntityCode()) + " isn't supported";

		return StorableObjectDatabase.idsEnumerationString(this.condition.getLinkedIdentifiables(), columnName, true).toString();
	}

	protected String getLinkedQuery(final String linkedColumnIdName,
			final String linkedColumnTargetName,
			final String linkedTableName) {
		return this.getLinkedQuery(COLUMN_ID, linkedColumnIdName, linkedColumnTargetName, linkedTableName);
	}

	protected String getLinkedQuery(final String columnName,
			final String linkedColumnIdName,
			final String linkedColumnTargetName,
			final String linkedTableName) {
		final StringBuffer query = new StringBuffer();
		query.append(columnName);
		query.append(SQL_IN);
		query.append(OPEN_BRACKET);
		query.append(SQL_SELECT);
		query.append(linkedColumnIdName);
		query.append(SQL_FROM);
		query.append(linkedTableName);
		query.append(SQL_WHERE);
		query.append(this.getQuery(linkedColumnTargetName));
		query.append(CLOSE_BRACKET);
		return query.toString();
	}

	protected IllegalObjectEntityException newExceptionEntityIllegal() {
		final short entityCode = this.getEntityCode().shortValue();
		final short linkedEntityCode = this.condition.getLinkedEntityCode();
		return new IllegalObjectEntityException("Unsupported entity '"
				+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode + "; linked entity '"
				+ ObjectEntities.codeToString(linkedEntityCode) + "'/" + linkedEntityCode,
				ENTITY_NOT_REGISTERED_CODE);
	}

	protected IllegalObjectEntityException newExceptionLinkedEntityIllegal() {
		final short entityCode = this.getEntityCode().shortValue();
		final short linkedEntityCode = this.condition.getLinkedEntityCode();
		return new IllegalObjectEntityException("Unsupported linked entity '"
				+ ObjectEntities.codeToString(linkedEntityCode) + "'/" + linkedEntityCode + " for entity '"
				+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode,
				ENTITY_NOT_REGISTERED_CODE);
	}

}
