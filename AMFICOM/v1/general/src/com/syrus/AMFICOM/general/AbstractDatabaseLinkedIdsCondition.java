/*
* $Id: AbstractDatabaseLinkedIdsCondition.java,v 1.1 2005/02/03 14:56:10 bob Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * @version $Revision: 1.1 $, $Date: 2005/02/03 14:56:10 $
 * @author $Author: bob $
 * @module general_v1
 */
public abstract class AbstractDatabaseLinkedIdsCondition implements DatabaseStorableObjectCondition {

	protected LinkedIdsCondition condition;
	
	public AbstractDatabaseLinkedIdsCondition(LinkedIdsCondition delegate) {
		this.condition = delegate;
	}

	protected abstract String getColumnName();
	
	public Short getEntityCode() {
		return this.condition.getEntityCode();
	}
	
	public String getSQLQuery() throws IllegalDataException {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.getColumnName());
		buffer.append(StorableObjectDatabase.SQL_IN);
		buffer.append(StorableObjectDatabase.OPEN_BRACKET);
		int i = 1;
		List ids = this.condition.getLinkedIds();
		if (ids == null)
			ids = Collections.singletonList(this.condition.getIdentifier());
		for (Iterator it = ids.iterator(); it.hasNext(); i++) {
			Object object = it.next();
			Identifier id = null;
			if (object instanceof Identifier)
				id = (Identifier) object;
			else
				if (object instanceof Identified)
					id = ((Identified) object).getId();
				else
					throw new IllegalDataException("AbstractDatabaseLinkedIdsCondition.getSQLQuery | Object "
							+ object.getClass().getName()
							+ " isn't Identifier or Identified");

			if (id != null) {
				buffer.append(DatabaseIdentifier.toSQLString(id));
				if (it.hasNext()) {
					if (((i + 1) % StorableObjectDatabase.MAXIMUM_EXPRESSION_NUMBER != 0))
						buffer.append(StorableObjectDatabase.COMMA);
					else {
						buffer.append(StorableObjectDatabase.CLOSE_BRACKET);
						buffer.append(StorableObjectDatabase.SQL_OR);
						buffer.append(this.getColumnName());
						buffer.append(StorableObjectDatabase.SQL_IN);
						buffer.append(StorableObjectDatabase.OPEN_BRACKET);
					}
				}
			}
		}
		buffer.append(StorableObjectDatabase.CLOSE_BRACKET);
		return buffer.toString();
	}

}
