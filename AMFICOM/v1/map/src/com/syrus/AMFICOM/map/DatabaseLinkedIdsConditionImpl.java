/*-
 * $Id: DatabaseLinkedIdsConditionImpl.java,v 1.14 2005/10/17 15:25:35 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ObjectEntities.COLLECTOR_CODE;
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
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @author Maxim Selivanov
 * @author $Author: max $
 * @version $Revision: 1.14 $, $Date: 2005/10/17 15:25:35 $
 * @module map
 */
public class DatabaseLinkedIdsConditionImpl extends AbstractDatabaseLinkedIdsCondition {

	@SuppressWarnings("unused")
	private DatabaseLinkedIdsConditionImpl(final LinkedIdsCondition linkedIdsCondition) {
		super(linkedIdsCondition);
	}

	public String getSQLQuery() throws IllegalObjectEntityException {
		switch (super.condition.getEntityCode().shortValue()) {

			case COLLECTOR_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case PHYSICALLINK_CODE:
						return super.getLinkedQuery(CollectorWrapper.LINK_COLUMN_COLLECTOR_ID,
								CollectorWrapper.LINK_COLUMN_PHYSICAL_LINK_ID,
								CollectorWrapper.COLLECTOR_PHYSICAL_LINK);
					default:
						throw newExceptionLinkedEntityIllegal();
				}
			case PHYSICALLINK_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case SITENODE_CODE:
					case TOPOLOGICALNODE_CODE:
						return OPEN_BRACKET
								+ super.getQuery(PhysicalLinkWrapper.COLUMN_START_NODE_ID)
								+ SQL_OR
								+ super.getQuery(PhysicalLinkWrapper.COLUMN_END_NODE_ID)
								+ CLOSE_BRACKET;
					case PHYSICALLINK_TYPE_CODE:
						return super.getQuery(StorableObjectWrapper.COLUMN_TYPE_ID);
					default:
						throw newExceptionLinkedEntityIllegal();
				}
			case NODELINK_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case PHYSICALLINK_CODE:
						return super.getQuery(NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID);
					case TOPOLOGICALNODE_CODE:
					case SITENODE_CODE:
						return OPEN_BRACKET
								+ super.getQuery(NodeLinkWrapper.COLUMN_START_NODE_ID)
								+ SQL_OR
								+ super.getQuery(NodeLinkWrapper.COLUMN_END_NODE_ID)
								+ CLOSE_BRACKET;
					default:
						throw newExceptionLinkedEntityIllegal();
				}
			case MAP_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case DOMAIN_CODE:
						return super.getQuery(MapWrapper.COLUMN_DOMAIN_ID);
					case MAPLIBRARY_CODE:
						return super.getLinkedQuery(MapWrapper.LINK_COLUMN_MAP_ID, MapWrapper.LINK_COLUMN_MAP_LIBRARY_ID, MapDatabase.MAP_MAP_LIBRARY);
					case MAP_CODE:
						return super.getLinkedQuery(MapWrapper.LINK_COLUMN_MAP_ID, MapWrapper.LINK_COLUMN_CHILD_MAP_ID, MapDatabase.MAP_MAP);
					case SITENODE_CODE:
						return super.getLinkedQuery(MapWrapper.LINK_COLUMN_MAP_ID, MapWrapper.LINK_COLUMN_EXTERNAL_NODE_ID, MapDatabase.MAP_EXTERNAL_NODE);
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
			case SITENODE_CODE:
				switch (super.condition.getLinkedEntityCode()) {
					case SITENODE_CODE:
						return super.getQuery(SiteNodeWrapper.COLUMN_ATTACHMENT_SITE_NODE_ID);
					case SITENODE_TYPE_CODE:
						return super.getQuery(StorableObjectWrapper.COLUMN_TYPE_ID);
					default:
						throw newExceptionLinkedEntityIllegal();
				}
			default:
				throw newExceptionEntityIllegal();
		}
	}
}
