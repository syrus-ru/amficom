/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.19 2005/09/02 13:54:24 krupenn Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MAPLIBRARY_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MAP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.NODELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TOPOLOGICALNODE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.UPDIKE_CODE;

import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.UpdateObjectException;

/**
 * @version $Revision: 1.19 $
 * @author $Author: krupenn $
 * @module map
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
			case NODELINK_CODE:
				final NodeLink nodeLink = (NodeLink) storableObject;
				switch (this.linkedEntityCode) {
				case PHYSICALLINK_CODE:
					return super.conditionTest(nodeLink.getPhysicalLink().getId());
				case TOPOLOGICALNODE_CODE:
					// fall through.
					// finding a node link by endpoint is the same for
					// topological node and site node
				case SITENODE_CODE:
					boolean condition1 = super.conditionTest(nodeLink.getStartNode().getId());
					boolean condition2 = super.conditionTest(nodeLink.getEndNode().getId());
					return condition1 | condition2;
				default:
					throw newExceptionLinkedEntityIllegal();
				}
			case MAP_CODE:
				final Map map = (Map) storableObject;
				switch (this.linkedEntityCode) {
				case DOMAIN_CODE:
					return super.conditionTest(map.getDomainId());
				default:
					throw newExceptionLinkedEntityIllegal();
				}
			case SITENODE_TYPE_CODE:
				final SiteNodeType siteNodeType = (SiteNodeType) storableObject;
				switch (this.linkedEntityCode) {
				case MAPLIBRARY_CODE:
					return super.conditionTest(siteNodeType.getMapLibrary().getId());
				default:
					throw newExceptionLinkedEntityIllegal();
				}
			case PHYSICALLINK_TYPE_CODE:
				final PhysicalLinkType physicalLinkType = (PhysicalLinkType) storableObject;
				switch (this.linkedEntityCode) {
				case MAPLIBRARY_CODE:
					return super.conditionTest(physicalLinkType.getMapLibrary().getId());
				default:
					throw newExceptionLinkedEntityIllegal();
				}
			case SITENODE_CODE:
				final SiteNode siteNode = (SiteNode) storableObject;
				switch (this.linkedEntityCode) {
				case SITENODE_CODE:
					return super.conditionTest(siteNode.getAttachmentSiteNodeId());
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
			case MAP_CODE:
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
