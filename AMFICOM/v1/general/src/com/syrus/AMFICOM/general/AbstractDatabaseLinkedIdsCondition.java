/*
 * $Id: AbstractDatabaseLinkedIdsCondition.java,v 1.10 2005/03/04 13:29:36 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Collection;
import java.util.Iterator;

/**
 * @version $Revision: 1.10 $, $Date: 2005/03/04 13:29:36 $
 * @author $Author: bass $
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
		buffer.append(columnName);
		buffer.append(StorableObjectDatabase.SQL_IN);
		buffer.append(StorableObjectDatabase.OPEN_BRACKET);
		int i = 1;
		Collection ids = this.condition.getLinkedIds();
		for (Iterator it = ids.iterator(); it.hasNext(); i++) {
			Object object = it.next();
			Identifier id = null;
			if (object instanceof Identifier)
				id = (Identifier) object;
			else if (object instanceof Identifiable)
				id = ((Identifiable) object).getId();
			else
				throw new IllegalDataException("AbstractDatabaseLinkedIdsCondition.getQuery | Object "
						+ object.getClass().getName() + " isn't Identifier or Identifiable");

			if (id != null) {
				buffer.append(DatabaseIdentifier.toSQLString(id));
				if (it.hasNext()) {
					if (((i + 1) % StorableObjectDatabase.MAXIMUM_EXPRESSION_NUMBER != 0))
						buffer.append(StorableObjectDatabase.COMMA);
					else {
						buffer.append(StorableObjectDatabase.CLOSE_BRACKET);
						buffer.append(StorableObjectDatabase.SQL_OR);
						buffer.append(columnName);
						buffer.append(StorableObjectDatabase.SQL_IN);
						buffer.append(StorableObjectDatabase.OPEN_BRACKET);
					}
				}
			}
		}
		buffer.append(StorableObjectDatabase.CLOSE_BRACKET);
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
