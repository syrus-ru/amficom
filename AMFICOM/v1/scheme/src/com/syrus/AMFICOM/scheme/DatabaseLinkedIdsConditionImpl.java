/*-
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.6 2005/06/14 10:32:56 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: max $
 * @version $Revision: 1.6 $, $Date: 2005/06/14 10:32:56 $
 * @module scheme_v1
 */
final class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {
	private DatabaseLinkedIdsConditionImpl(final LinkedIdsCondition linkedIdsCondition) {
		super(linkedIdsCondition);
	}

	/**
	 * @see com.syrus.AMFICOM.general.DatabaseStorableObjectCondition#getSQLQuery()
	 */
	public String getSQLQuery() throws IllegalObjectEntityException {
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.SCHEME_PORT_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.SCHEME_DEVICE_ENTITY_CODE:
						return super.getQuery(SchemePortWrapper.COLUMN_PARENT_DEVICE_ID);
					default:
						throw newIllegalObjectEntityException();	
				}
			case ObjectEntities.SCHEME_CABLE_PORT_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.SCHEME_DEVICE_ENTITY_CODE:
						return super.getQuery(SchemeCablePortWrapper.COLUMN_PARENT_DEVICE_ID);
					default:
						throw newIllegalObjectEntityException();
				}
			case ObjectEntities.SCHEME_LINK_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.SCHEME_PORT_ENTITY_CODE:
						return StorableObjectDatabase.OPEN_BRACKET
								+ super.getQuery(SchemeLinkWrapper.COLUMN_SOURCE_SCHEME_PORT_ID)
								+ StorableObjectDatabase.SQL_OR
								+ super.getQuery(SchemeLinkWrapper.COLUMN_TARGET_SCHEME_PORT_ID)
								+ StorableObjectDatabase.CLOSE_BRACKET;
					default:
						throw newIllegalObjectEntityException();
				}
			case ObjectEntities.SCHEME_CABLE_LINK_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.SCHEME_CABLE_PORT_ENTITY_CODE:
						return StorableObjectDatabase.OPEN_BRACKET
								+ super.getQuery(SchemeCableLinkWrapper.COLUMN_SOURCE_SCHEME_CABLE_PORT_ID)
								+ StorableObjectDatabase.SQL_OR
								+ super.getQuery(SchemeCableLinkWrapper.COLUMN_TARGET_SCHEME_CABLE_PORT_ID)
								+ StorableObjectDatabase.CLOSE_BRACKET;
					default:
						throw newIllegalObjectEntityException();
				}
			case ObjectEntities.SCHEME_CABLE_THREAD_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.SCHEME_PORT_ENTITY_CODE:
						return StorableObjectDatabase.OPEN_BRACKET
								+ super.getQuery(SchemeCableThreadWrapper.COLUMN_SOURCE_SCHEME_PORT_ID)
								+ StorableObjectDatabase.SQL_OR
								+ super.getQuery(SchemeCableThreadWrapper.COLUMN_TARGET_SCHEME_PORT_ID)
								+ StorableObjectDatabase.CLOSE_BRACKET;
					default:
						throw newIllegalObjectEntityException();
				}
			default:
				throw newIllegalObjectEntityException();
		}
	}
}
