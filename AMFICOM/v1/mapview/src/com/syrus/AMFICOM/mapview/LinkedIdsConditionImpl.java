/**
 * $Id: LinkedIdsConditionImpl.java,v 1.2 2005/03/24 14:00:28 bob Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.mapview;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;

/**
 * @version $Revision: 1.2 $
 * @author $Author: bob $
 * @module map_v1
 */
class LinkedIdsConditionImpl extends com.syrus.AMFICOM.general.LinkedIdsCondition {

	private LinkedIdsConditionImpl(Collection linkedIds, Short entityCode) {
		this.linkedIds = linkedIds;
		this.entityCode = entityCode;
	}

	private LinkedIdsConditionImpl(Identifier identifier, Short entityCode) {
		this.linkedIds = Collections.singletonList(identifier);
		this.entityCode = entityCode;
	}

	private boolean checkDomain(DomainMember domainMember) throws IllegalObjectEntityException {
		boolean condition;
		try {
			Domain dmDomain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainMember.getDomainId(), true);
			condition = false;
			/* if linked ids is domain id */
			for (Iterator it = this.linkedIds.iterator(); it.hasNext() && !condition;) {
				Identifier id = (Identifier) it.next();
				if (id.getMajor() == ObjectEntities.DOMAIN_ENTITY_CODE) {
					Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(id, true);
					if (dmDomain.isChild(domain))
						condition = true;

				}

			}
		} catch (ApplicationException e) {
			throw new IllegalObjectEntityException(e.getMessage(), IllegalObjectEntityException.UNKNOWN_ENTITY_CODE);
		}
		return condition;
	}

	public boolean isConditionTrue(Object object) throws IllegalObjectEntityException {
		boolean condition = false;
		DomainMember domainMember;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.MAPVIEW_ENTITY_CODE:
				domainMember = (MapView) object;
				break;
			default:
				throw new UnsupportedOperationException("LinkedIdsConditionImpl.isConditionTrue | entityCode "
						+ ObjectEntities.codeToString(this.entityCode.shortValue()) + " is unknown for this condition");
		}
		if (domainMember != null)
			condition = this.checkDomain(domainMember);
		return condition;
	}

	public void setEntityCode(Short entityCode) throws IllegalObjectEntityException {
		switch (entityCode.shortValue()) {
			case ObjectEntities.MAPVIEW_ENTITY_CODE:
				this.entityCode = entityCode;
				break;
			default:
				throw new IllegalObjectEntityException("LinkedIdsConditionImpl.setEntityCode | entityCode "
						+ ObjectEntities.codeToString(entityCode.shortValue()) + " is unknown for this condition", IllegalObjectEntityException.UNKNOWN_ENTITY_CODE);
		}
	}

	public boolean isNeedMore(Collection collection) {
		return true;
	}
}
