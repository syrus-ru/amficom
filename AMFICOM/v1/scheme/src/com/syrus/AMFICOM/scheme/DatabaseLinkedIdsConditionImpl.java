/*-
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.9 2005/06/21 12:21:40 max Exp $
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
 * @version $Revision: 1.9 $, $Date: 2005/06/21 12:21:40 $
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
			case ObjectEntities.SCHEMEPORT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.SCHEMEDEVICE_CODE:
						return super.getQuery(SchemePortWrapper.COLUMN_PARENT_DEVICE_ID);
					default:
						throw newIllegalObjectEntityException();	
				}
			case ObjectEntities.SCHEMECABLEPORT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.SCHEMEDEVICE_CODE:
						return super.getQuery(SchemeCablePortWrapper.COLUMN_PARENT_DEVICE_ID);
					default:
						throw newIllegalObjectEntityException();
				}
			case ObjectEntities.SCHEMELINK_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.SCHEMEPORT_CODE:
						return StorableObjectDatabase.OPEN_BRACKET
								+ super.getQuery(SchemeLinkWrapper.COLUMN_SOURCE_SCHEME_PORT_ID)
								+ StorableObjectDatabase.SQL_OR
								+ super.getQuery(SchemeLinkWrapper.COLUMN_TARGET_SCHEME_PORT_ID)
								+ StorableObjectDatabase.CLOSE_BRACKET;
					default:
						throw newIllegalObjectEntityException();
				}
			case ObjectEntities.SCHEMECABLELINK_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.SCHEMECABLEPORT_CODE:
						return StorableObjectDatabase.OPEN_BRACKET
								+ super.getQuery(SchemeCableLinkWrapper.COLUMN_SOURCE_SCHEME_CABLE_PORT_ID)
								+ StorableObjectDatabase.SQL_OR
								+ super.getQuery(SchemeCableLinkWrapper.COLUMN_TARGET_SCHEME_CABLE_PORT_ID)
								+ StorableObjectDatabase.CLOSE_BRACKET;
					default:
						throw newIllegalObjectEntityException();
				}
			case ObjectEntities.SCHEMECABLETHREAD_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.SCHEMEPORT_CODE:
						return StorableObjectDatabase.OPEN_BRACKET
								+ super.getQuery(SchemeCableThreadWrapper.COLUMN_SOURCE_SCHEME_PORT_ID)
								+ StorableObjectDatabase.SQL_OR
								+ super.getQuery(SchemeCableThreadWrapper.COLUMN_TARGET_SCHEME_PORT_ID)
								+ StorableObjectDatabase.CLOSE_BRACKET;
					case ObjectEntities.SCHEMECABLELINK_CODE:
						return super.getQuery(SchemeCableThreadWrapper.COLUMN_PARENT_SCHEME_CABLE_LINK_ID);
					default:
						throw newIllegalObjectEntityException();
				}
			case ObjectEntities.SCHEMEELEMENT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.SCHEMEELEMENT_CODE:
						return super.getQuery(SchemeElementWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID);
					default:
						throw newIllegalObjectEntityException();
				}
			case ObjectEntities.SCHEMEDEVICE_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.SCHEMEELEMENT_CODE:
						return super.getQuery(SchemeDeviceWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID);
					default:
						throw newIllegalObjectEntityException();
				}
			case ObjectEntities.PATHELEMENT_CODE:
				switch (super.condition.getLinkedEntityCode()) {
				case ObjectEntities.SCHEMEPATH_CODE:
					return super.getQuery(PathElementWrapper.COLUMN_PARENT_SCHEME_PATH_ID);
				default:
					throw newIllegalObjectEntityException();
			}
			default:
				throw newIllegalObjectEntityException();
		}
	}
}
