/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.6 2005/04/13 09:21:39 krupenn Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.6 $
 * @author $Author: krupenn $
 * @module map_v1
 */
final class LinkedIdsConditionImpl extends LinkedIdsCondition {

	private LinkedIdsConditionImpl(final Set linkedIds, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIds = linkedIds;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	private boolean checkDomain(final DomainMember domainMember) {
		boolean condition = false;
		try {
			final Domain dmDomain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainMember.getDomainId(), true);
			for (final Iterator it = this.linkedIds.iterator(); it.hasNext() && !condition;) {
				final Identifier id = (Identifier) it.next();
				if (id.getMajor() == ObjectEntities.DOMAIN_ENTITY_CODE) {
					final Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(id, true);
					if (dmDomain.isChild(domain))
						condition = true;
				}
			}
		}
		catch (final ApplicationException ae) {
			Log.errorException(ae);
		}
		return condition;
	}

	private boolean checkPhysicalLink(final NodeLink nodeLink) {
		boolean condition = false;
		for (final Iterator it = this.linkedIds.iterator(); it.hasNext() && !condition;) {
			final Identifier id = (Identifier) it.next();
			if (id.getMajor() == ObjectEntities.PHYSICAL_LINK_ENTITY_CODE) {
				if (nodeLink.getPhysicalLink().getId().equals(id))
					condition = true;
			}
		}
		return condition;
	}

	private boolean checkNode(final NodeLink nodeLink) {
		boolean condition = false;
		for (final Iterator it = this.linkedIds.iterator(); it.hasNext() && !condition;) {
			final Identifier id = (Identifier) it.next();
			if (id.getMajor() == ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE
					|| id.getMajor() == ObjectEntities.SITE_NODE_ENTITY_CODE) {
				if (nodeLink.getStartNode().getId().equals(id)
						|| nodeLink.getEndNode().getId().equals(id))
					condition = true;
			}
		}
		return condition;
	}

	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.NODE_LINK_ENTITY_CODE:
				NodeLink nodeLink = (NodeLink) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.PHYSICAL_LINK_ENTITY_CODE:
						condition = this.checkPhysicalLink(nodeLink);
						break;
					case ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE:
						// fall through.
						// finding a node link by endpoint is the same for
						// topological node and site node
					case ObjectEntities.SITE_NODE_ENTITY_CODE:
						condition = this.checkNode(nodeLink);
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.MAP_ENTITY_CODE:
				Map map = (Map) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.DOMAIN_ENTITY_CODE:
						condition = this.checkDomain(map);
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			default:
				throw new IllegalObjectEntityException(ENTITY_CODE_NOT_REGISTERED + this.entityCode
						+ ", " + ObjectEntities.codeToString(this.entityCode),
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
		return condition;
	}

	public void setEntityCode(final Short entityCode) throws IllegalObjectEntityException {
		switch (entityCode.shortValue()) {
			case ObjectEntities.MAP_ENTITY_CODE:
				this.entityCode = entityCode;
				break;
			default:
				throw new IllegalObjectEntityException(ENTITY_CODE_NOT_REGISTERED + this.entityCode
						+ ", " + ObjectEntities.codeToString(this.entityCode),
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}

	public boolean isNeedMore(final Set storableObjects) {
		return true;
	}
}
