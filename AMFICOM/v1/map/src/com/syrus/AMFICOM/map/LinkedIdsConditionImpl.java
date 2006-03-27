/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.26 2006/03/27 11:21:42 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import static com.syrus.AMFICOM.general.ObjectEntities.COLLECTOR_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MAPLIBRARY_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MAP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.NODELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PHYSICALLINK_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TOPOLOGICALNODE_CODE;

import java.util.Set;

import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObject;

/**
 * @version $Revision: 1.26 $
 * @author $Author: bass $
 * @module map
 */
final class LinkedIdsConditionImpl extends LinkedIdsCondition {
	@SuppressWarnings("unused")
	private LinkedIdsConditionImpl(final Set<? extends Identifiable> linkedIdentifiables, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIdentifiables = linkedIdentifiables;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	@Override
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		switch (this.entityCode.shortValue()) {
			case COLLECTOR_CODE:
				final Collector collector = (Collector) storableObject;
				switch (this.linkedEntityCode) {
				case PHYSICALLINK_CODE:
					return super.conditionTest(collector.getPhysicalLinkIds());
				default:
					throw newExceptionLinkedEntityIllegal();
				}
			case PHYSICALLINK_CODE:
				final PhysicalLink physicalLink = (PhysicalLink) storableObject;
				switch (this.linkedEntityCode) {
				case TOPOLOGICALNODE_CODE:
					// fall through.
					// finding a physical link by endpoint is the same for
					// topological node and site node
				case SITENODE_CODE:
					final boolean condition1 = super.conditionTest(physicalLink.getStartNodeId());
					final boolean condition2 = super.conditionTest(physicalLink.getEndNodeId());
					return condition1 | condition2;
				case PHYSICALLINK_TYPE_CODE:
					return super.conditionTest(physicalLink.getType().getId());
				default:
					throw newExceptionLinkedEntityIllegal();
				}
			case NODELINK_CODE:
				final NodeLink nodeLink = (NodeLink) storableObject;
				switch (this.linkedEntityCode) {
				case PHYSICALLINK_CODE:
					return super.conditionTest(nodeLink.getPhysicalLinkId());
				case TOPOLOGICALNODE_CODE:
					// fall through.
					// finding a node link by endpoint is the same for
					// topological node and site node
				case SITENODE_CODE:
					final boolean condition1 = super.conditionTest(nodeLink.getStartNodeId());
					final boolean condition2 = super.conditionTest(nodeLink.getEndNodeId());
					return condition1 | condition2;
				default:
					throw newExceptionLinkedEntityIllegal();
				}
			case MAP_CODE:
				final Map map = (Map) storableObject;
				switch (this.linkedEntityCode) {
				case DOMAIN_CODE:
					return super.conditionTest(map.getDomainId());
				case MAPLIBRARY_CODE:
					return super.conditionTest(map.getMapLibraryIds());
				case MAP_CODE:
					return super.conditionTest(map.getMapIds());
				case SITENODE_CODE:
					return super.conditionTest(map.getExternalNodeIds());
				default:
					throw newExceptionLinkedEntityIllegal();
				}
			case SITENODE_TYPE_CODE:
				final SiteNodeType siteNodeType = (SiteNodeType) storableObject;
				switch (this.linkedEntityCode) {
				case MAPLIBRARY_CODE:
					return super.conditionTest(siteNodeType.getMapLibraryId());
				default:
					throw newExceptionLinkedEntityIllegal();
				}
			case PHYSICALLINK_TYPE_CODE:
				final PhysicalLinkType physicalLinkType = (PhysicalLinkType) storableObject;
				switch (this.linkedEntityCode) {
				case MAPLIBRARY_CODE:
					return super.conditionTest(physicalLinkType.getMapLibraryId());
				default:
					throw newExceptionLinkedEntityIllegal();
				}
			case SITENODE_CODE:
				final SiteNode siteNode = (SiteNode) storableObject;
				switch (this.linkedEntityCode) {
				case SITENODE_CODE:
					return super.conditionTest(siteNode.getAttachmentSiteNodeId());
				case SITENODE_TYPE_CODE:
					return super.conditionTest(siteNode.getType().getId());
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
	public boolean isNeedMore(final Set<? extends Identifiable> identifiables) {
		return true;
	}
}
