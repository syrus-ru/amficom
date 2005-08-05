/*-
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.5 2005/08/05 10:48:05 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MAP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.NODELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TOPOLOGICALNODE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MAPLIBRARY_CODE;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_OR;

import com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;

/**
 * @author Maxim Selivanov
 * @author $Author: max $
 * @version $Revision: 1.5 $, $Date: 2005/08/05 10:48:05 $
 * @module map_v1
 */
public class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	@SuppressWarnings("unused")
	private DatabaseLinkedIdsConditionImpl(final LinkedIdsCondition linkedIdsCondition) {
		super(linkedIdsCondition);
	}
	
	public String getSQLQuery() throws IllegalObjectEntityException {
		switch (super.condition.getEntityCode().shortValue()) {
			case NODELINK_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case PHYSICALLINK_CODE:
						return super.getQuery(NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID);
					case TOPOLOGICALNODE_CODE:
					case SITENODE_CODE:
						return OPEN_BRACKET +
								super.getQuery(NodeLinkWrapper.COLUMN_START_NODE_ID)
								+ SQL_OR + 
								super.getQuery(NodeLinkWrapper.COLUMN_END_NODE_ID) + 
								CLOSE_BRACKET;
					default:
						throw newExceptionLinkedEntityIllegal();
				}
			case MAP_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case DOMAIN_CODE:
						return super.getQuery(MapWrapper.COLUMN_DOMAIN_ID);
					default:
						throw newExceptionLinkedEntityIllegal();
				}
			case SITENODE_TYPE_CODE:
				switch (super.condition.getLinkedEntityCode()) {
				case MAPLIBRARY_CODE:
					return super.getQuery(SiteNodeTypeWrapper.COLUMN_MAP_LIBRARY_ID);
				default:
					throw newExceptionLinkedEntityIllegal();
				}
			case PHYSICALLINK_TYPE_CODE:
				switch (super.condition.getLinkedEntityCode()) {
				case MAPLIBRARY_CODE:
					return super.getQuery(PhysicalLinkTypeWrapper.COLUMN_MAP_LIBRARY_ID);
				default:
					throw newExceptionLinkedEntityIllegal();
				}
			default:
				throw newExceptionEntityIllegal();
		}
	}
}
