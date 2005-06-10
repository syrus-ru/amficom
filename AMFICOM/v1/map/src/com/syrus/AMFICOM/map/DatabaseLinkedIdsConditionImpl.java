/*-
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.1 2005/06/10 16:18:11 max Exp $
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
 * @author $Author: max $
 * @version $Revision: 1.1 $, $Date: 2005/06/10 16:18:11 $
 * @module map_v1
 */
public class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	private DatabaseLinkedIdsConditionImpl(final LinkedIdsCondition linkedIdsCondition) {
		super(linkedIdsCondition);
	}
	
	public String getSQLQuery() throws IllegalObjectEntityException {
		switch (super.condition.getEntityCode().shortValue()) {
			case ObjectEntities.NODE_LINK_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.PHYSICAL_LINK_ENTITY_CODE:
						return super.getQuery(NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID);
					case ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE:
					case ObjectEntities.SITE_NODE_ENTITY_CODE:
						return StorableObjectDatabase.OPEN_BRACKET +
								super.getQuery(NodeLinkWrapper.COLUMN_START_NODE_ID)
								+ StorableObjectDatabase.SQL_OR + 
								super.getQuery(NodeLinkWrapper.COLUMN_END_NODE_ID) + 
								StorableObjectDatabase.CLOSE_BRACKET;
					default:
						throw newIllegalObjectEntityException();
				}
			case ObjectEntities.MAP_ENTITY_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case ObjectEntities.DOMAIN_ENTITY_CODE:
						return super.getQuery(MapWrapper.COLUMN_DOMAIN_ID);
					default:
						throw newIllegalObjectEntityException();
				}
			default:
				throw newIllegalObjectEntityException();
		}
	}
}
