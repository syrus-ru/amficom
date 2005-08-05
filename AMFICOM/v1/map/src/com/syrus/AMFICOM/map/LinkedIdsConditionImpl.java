/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.13 2005/08/05 10:48:05 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;

/**
 * @version $Revision: 1.13 $
 * @author $Author: max $
 * @module map_v1
 */
final class LinkedIdsConditionImpl extends LinkedIdsCondition {

	@SuppressWarnings("unused")
	private LinkedIdsConditionImpl(final Set<Identifier> linkedIds, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIds = linkedIds;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	@Override
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.NODELINK_CODE:
				final NodeLink nodeLink = (NodeLink) storableObject;
				switch (this.linkedEntityCode) {
				case ObjectEntities.PHYSICALLINK_CODE:
					return super.conditionTest(nodeLink.getPhysicalLink().getId());
				case ObjectEntities.TOPOLOGICALNODE_CODE:
					// fall through.
					// finding a node link by endpoint is the same for
					// topological node and site node
				case ObjectEntities.SITENODE_CODE:
					boolean condition1 = super.conditionTest(nodeLink.getStartNode().getId());
					boolean condition2 = super.conditionTest(nodeLink.getEndNode().getId());
					return condition1 | condition2;
				default:
					throw newExceptionLinkedEntityIllegal();
				}
			case ObjectEntities.MAP_CODE:
				final Map map = (Map) storableObject;
				switch (this.linkedEntityCode) {
				case ObjectEntities.DOMAIN_CODE:
					return super.conditionTest(map.getId());
				default:
					throw newExceptionLinkedEntityIllegal();
				}
			case ObjectEntities.SITENODE_TYPE_CODE:
				final SiteNodeType siteNodeType = (SiteNodeType) storableObject;
				switch (this.linkedEntityCode) {
				case ObjectEntities.MAPLIBRARY_CODE:
					return super.conditionTest(siteNodeType.getMapLibrary().getId());
				default:
					throw newExceptionLinkedEntityIllegal();
				}
			case ObjectEntities.PHYSICALLINK_TYPE_CODE:
				final PhysicalLinkType physicalLinkType = (PhysicalLinkType) storableObject;
				switch (this.linkedEntityCode) {
				case ObjectEntities.MAPLIBRARY_CODE:
					return super.conditionTest(physicalLinkType.getMapLibrary().getId());
				default:
					throw newExceptionLinkedEntityIllegal();
				}
			default:
				throw newExceptionEntityIllegal();
		}		
	}

	@Override
	public void setEntityCode(final Short entityCode) throws IllegalObjectEntityException {
		switch (entityCode.shortValue()) {
			case ObjectEntities.MAP_CODE:
				this.entityCode = entityCode;
				break;
			default:
				throw newExceptionEntityIllegal();
		}
	}

	@Override
	public boolean isNeedMore(final Set<? extends StorableObject> storableObjects) {
		return true;
	}
}
