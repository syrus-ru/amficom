/*
 * $Id: ActionCondition.java,v 1.2 2005/01/28 11:51:51 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.ActionCondition_Transferable;
/**
 * Condition to manipulate different actions (i. e., objects of type {@link Action})
 * and their results (objects of type {@link Result}).
 * Currently implemented for values of <code>entityCode</code>, listed below.
 * <code>ObjectEntities.RESULT_ENTITY_CODE</code>:
 * Find all results, whose actions have parent action of id <code>action_id</code>
 * 
 * <li>NOTE:</li> If you want to find for a given action it's result,
 * use {@link LinkedIdsCondition} with <code>ObjectEntities.RESULT_ENTITY_CODE</code>
 * as <code>entityCode</code>	and one from this:
 * 	identifier of the action as <code>identifier</code>
 * or
 * 	list, containing one element - identifier of the action - as <code>linkedIds</code>
 * 
 * @version $Revision: 1.2 $, $Date: 2005/01/28 11:51:51 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */
public class ActionCondition implements StorableObjectCondition {
	private Short entityCode;
	private Identifier actionId;

	/**
	 * @param entityCode
	 * @param actionId
	 */
	public ActionCondition(Short entityCode, Identifier actionId) {
		this.entityCode = entityCode;
		this.actionId = actionId;
	}

	public ActionCondition(short entityCode, Identifier actionId) {
		this(new Short(entityCode), actionId);
	}

	public ActionCondition(ActionCondition_Transferable act) {
		this.entityCode = new Short(act.entity_code);
		this.actionId = new Identifier(act.action_id);
	}

	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		if (this.entityCode.shortValue() == ObjectEntities.RESULT_ENTITY_CODE) {
			if (object instanceof Result) {
				Action action = ((Result) object).getAction();
				for (Action pa = action.getParentAction(); pa != null; pa = pa.getParentAction()) {
					if (pa.getId().equals(this.actionId)) {
						condition = true;
						break;
					}
				}
			}
		}

		return condition;
	}

	public boolean isNeedMore(List list) throws ApplicationException {
		return true;
	}

	public Short getEntityCode() {
		return this.entityCode;
	}

	public void setEntityCode(Short entityCode) {
		throw new UnsupportedOperationException("Setting entity code not allowed for this condition -- " + this.getClass().getName());
	}

	public Object getTransferable() {
		return new ActionCondition_Transferable(this.entityCode.shortValue(),
				(Identifier_Transferable) this.actionId.getTransferable());
	}

}
