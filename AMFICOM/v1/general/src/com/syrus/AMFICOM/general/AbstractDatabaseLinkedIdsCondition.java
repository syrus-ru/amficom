/*
 * $Id: AbstractDatabaseLinkedIdsCondition.java,v 1.5 2005/02/08 13:56:53 max Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @version $Revision: 1.5 $, $Date: 2005/02/08 13:56:53 $
 * @author $Author: max $
 * @module general_v1
 */
public abstract class AbstractDatabaseLinkedIdsCondition implements
		DatabaseStorableObjectCondition {

	protected LinkedIdsCondition	condition;

	public AbstractDatabaseLinkedIdsCondition(LinkedIdsCondition delegate) {
		this.condition = delegate;
	}

	protected abstract String getColumnName(short entityCode);

	public Short getEntityCode() {
		return this.condition.getEntityCode();
	}

	public String getSQLQuery() throws IllegalDataException {
		Map codeIdsMap = this.condition.sort(this.condition.linkedIds);

		StringBuffer buffer = new StringBuffer();
		buffer.append(" 1=0 ");

		for (Iterator iter = codeIdsMap.keySet().iterator(); iter.hasNext();) {
			Short entityCode = (Short) iter.next();
			List ids = (List) codeIdsMap.get(entityCode);
			String columnName = this.getColumnName(entityCode.shortValue());
			if (columnName == null)
				throw new IllegalDataException(
						"AbstractDatabaseLinkedIdsCondition.getSQLQuery | "
								+ ObjectEntities.codeToString(this
										.getEntityCode()) + " isn't supported");
			buffer.append(StorableObjectDatabase.SQL_OR);
			buffer.append(columnName);
			buffer.append(StorableObjectDatabase.SQL_IN);
			buffer.append(StorableObjectDatabase.OPEN_BRACKET);
			int i = 1;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Object object = it.next();
				Identifier id = null;
				if (object instanceof Identifier)
					id = (Identifier) object;
				else if (object instanceof Identified)
					id = ((Identified) object).getId();
				else
					throw new IllegalDataException(
							"AbstractDatabaseLinkedIdsCondition.getSQLQuery | Object "
									+ object.getClass().getName()
									+ " isn't Identifier or Identified");

				if (id != null) {
					buffer.append(DatabaseIdentifier.toSQLString(id));
					if (it.hasNext()) {
						if (((i + 1)
								% StorableObjectDatabase.MAXIMUM_EXPRESSION_NUMBER != 0))
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
		}
		return buffer.toString();
	}

	public String getLinkedQuery(final String linkedColumnName,
			final String linkedTableName) throws IllegalDataException {
		StringBuffer query = new StringBuffer();
		query.append(StorableObjectWrapper.COLUMN_ID);
		query.append(StorableObjectDatabase.SQL_IN);
		query.append(StorableObjectDatabase.OPEN_BRACKET);
		query.append(StorableObjectDatabase.SQL_SELECT);
		query.append(linkedColumnName);
		query.append(StorableObjectDatabase.SQL_FROM);
		query.append(linkedTableName);
		query.append(StorableObjectDatabase.SQL_WHERE);
		query.append(this.getSQLQuery());
		return query.toString();
	}

}
