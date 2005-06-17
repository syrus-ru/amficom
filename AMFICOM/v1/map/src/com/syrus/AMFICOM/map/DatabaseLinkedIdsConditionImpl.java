/*-
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.2 2005/06/17 11:01:12 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.map;

import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @author Maxim Selivanov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/06/17 11:01:12 $
 * @module map_v1
 */
public class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	private DatabaseLinkedIdsConditionImpl(final LinkedIdsCondition linkedIdsCondition) {
		super(linkedIdsCondition);
	}
	
	public String getSQLQuery() throws IllegalObjectEntityException {
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.NODELINK_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.PHYSICALLINK_CODE:
						return super.getQuery(NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID);
					case ObjectEntities.TOPOLOGICALNODE_CODE:
					case ObjectEntities.SITENODE_CODE:
						return StorableObjectDatabase.OPEN_BRACKET +
								super.getQuery(NodeLinkWrapper.COLUMN_START_NODE_ID)
								+ StorableObjectDatabase.SQL_OR + 
								super.getQuery(NodeLinkWrapper.COLUMN_END_NODE_ID) + 
								StorableObjectDatabase.CLOSE_BRACKET;
					default:
						throw newIllegalObjectEntityException();
				}
			case ObjectEntities.MAP_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.DOMAIN_CODE:
						return super.getQuery(MapWrapper.COLUMN_DOMAIN_ID);
					default:
						throw newIllegalObjectEntityException();
				}
			default:
				throw newIllegalObjectEntityException();
		}
	}
}
