/**
 * $Id: LinkedIdsConditionImpl.java,v 1.1 2005/03/01 15:35:12 krupenn Exp $
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
import com.syrus.AMFICOM.general.ObjectEntities;

/**
 * @version $Revision: 1.1 $
 * @author $Author: krupenn $
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

	private boolean checkDomain(DomainMember domainMember) throws ApplicationException {
		Domain dmDomain = (Domain) AdministrationStorableObjectPool.getStorableObject(domainMember.getDomainId(), true);
		boolean condition = false;
		/* if linked ids is domain id */
		for (Iterator it = this.linkedIds.iterator(); it.hasNext() && !condition;) {
			Identifier id = (Identifier) it.next();
			if (id.getMajor() == ObjectEntities.DOMAIN_ENTITY_CODE) {
				Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(id, true);
				if (dmDomain.isChild(domain))
					condition = true;

			}

		}
		return condition;
	}

	public boolean isConditionTrue(Object object) throws ApplicationException {
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

	public void setEntityCode(Short entityCode) {
		switch (entityCode.shortValue()) {
			case ObjectEntities.MAPVIEW_ENTITY_CODE:
				this.entityCode = entityCode;
				break;
			default:
				throw new UnsupportedOperationException("LinkedIdsConditionImpl.setEntityCode | entityCode "
						+ ObjectEntities.codeToString(entityCode.shortValue()) + " is unknown for this condition");
		}
	}

	public boolean isNeedMore(Collection collection) {
		return true;
	}
}
