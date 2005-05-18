/*
 * $Id: CompoundCondition.java,v 1.23 2005/05/18 11:07:38 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.CompoundCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.util.corba.JavaSoftORBUtil;

/**
 * Compound condition such as (A & B & C & ... etc), (A | B | C | ... etc) where A, B, C .. are
 * conditions (they can be also compound condition too)
 *
 * @version $Revision: 1.23 $, $Date: 2005/05/18 11:07:38 $
 * @author $Author: bass $
 * @module general_v1
 */
public final class CompoundCondition implements StorableObjectCondition {

	private static final ORB ORB_INSTANCE = JavaSoftORBUtil.getInstance().getORB();

	private int operation;

	/**
	 * Collection&lt;StorableObjectCondition&gt;
	 */
	private Set conditions;

	private Short entityCode = new Short(ObjectEntities.UNKNOWN_ENTITY_CODE);

	private boolean doCompare(boolean firstResult, boolean secondResult) {
		switch (this.operation) {
			case CompoundConditionSort._AND:
				return firstResult && secondResult;
			case CompoundConditionSort._OR:
				return firstResult || secondResult;
			default:
				throw new IllegalArgumentException("CompoundCondition.doComapare Unsupported operation " + this.operation);
		}
	}

	public CompoundCondition(StorableObjectCondition firstCondition,
			CompoundConditionSort operation,
			StorableObjectCondition secondCondition) throws CreateObjectException {
		this(new HashSet(Arrays.asList(new Object[] {firstCondition, secondCondition})), operation);
	}

	public CompoundCondition(Set conditions, CompoundConditionSort operation) throws CreateObjectException {
		if (conditions == null)
			throw new CreateObjectException("Unable to create CompoundCondition for null conditions");

		if (conditions.size() <= 1)
			throw new CreateObjectException("Unable to create CompoundCondition for alone condition, use condition itself");

		short code = ObjectEntities.UNKNOWN_ENTITY_CODE;

		for (Iterator it = conditions.iterator(); it.hasNext();) {
			Object object = it.next();
			if (object instanceof StorableObjectCondition) {
				StorableObjectCondition condition = (StorableObjectCondition) object;
				if (code == ObjectEntities.UNKNOWN_ENTITY_CODE) {
					this.entityCode = condition.getEntityCode();
					code = this.entityCode.shortValue();
				}
				else
					if (code != condition.getEntityCode().shortValue())
						throw new CreateObjectException("Unable to create CompoundCondition for conditions for different entities");
			}
			else
				throw new CreateObjectException("Unable to create CompoundCondition for conditions containing not StorableObjectCondition objects");
		}
		
		if (this.entityCode.shortValue() == ObjectEntities.UNKNOWN_ENTITY_CODE)
			throw new CreateObjectException("Unable to create CompoundCondition unknown entities");

		this.operation = operation.value();
		this.conditions = conditions;
	}

	public void addCondition(StorableObjectCondition condition) throws IllegalDataException {
		assert (condition != null) : "NULL condition supplied";

		Short code = condition.getEntityCode();
		if (code.equals(this.entityCode))
			this.conditions.add(condition);
		else
			throw new IllegalDataException("Cannot add condition for entity '" + ObjectEntities.codeToString(code)
					+ "' to set of condition for entity '" + ObjectEntities.codeToString(this.entityCode) + "'");
	}

	public CompoundCondition(CompoundCondition_Transferable transferable) throws IllegalDataException {
		this.operation = transferable.sort.value();
		Any[] anies = transferable.innerConditions;
		if (anies.length <= 1)
			throw new IllegalDataException("Unable to create CompoundCondition for " + anies.length + "  condition");
		this.conditions = new HashSet(anies.length);
		short code = ObjectEntities.UNKNOWN_ENTITY_CODE;
		for (int i = 0; i < anies.length; i++) {
			StorableObjectCondition condition = StorableObjectConditionBuilder.restoreCondition((StorableObjectCondition_Transferable) anies[i].extract_Value());
			this.conditions.add(condition);
			if (code == ObjectEntities.UNKNOWN_ENTITY_CODE) {
				this.entityCode = condition.getEntityCode();
				code = this.entityCode.shortValue();
			} else
				if (code != condition.getEntityCode().shortValue())
					throw new IllegalDataException("Unable to create CompoundCondition for conditions for different entities");
		}
		
		if (this.entityCode.shortValue() == ObjectEntities.UNKNOWN_ENTITY_CODE)
			throw new IllegalDataException("Unable to create CompoundCondition unknown entities");
	}

	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		boolean firstItem = true;
		boolean result = false;
		for (Iterator it = this.conditions.iterator(); it.hasNext();) {
			StorableObjectCondition condition = (StorableObjectCondition) it.next();
			if (firstItem) {
				result = condition.isConditionTrue(storableObject);
				firstItem = false;
			}
			else {
				result = this.doCompare(result, condition.isConditionTrue(storableObject));
			}
		}

		return result;
	}

	public boolean isNeedMore(final Set storableObjects) {
		boolean firstItem = true;
		boolean result = false;

		for (Iterator it = this.conditions.iterator(); it.hasNext();) {
			StorableObjectCondition condition = (StorableObjectCondition) it.next();
			if (firstItem) {
				result = condition.isNeedMore(storableObjects);
				firstItem = false;
			}
			else {
				result = this.doCompare(result, condition.isNeedMore(storableObjects));
			}
		}
		return result;
	}

	public Short getEntityCode() {
		return this.entityCode;
	}

	public void setEntityCode(final Short entityCode) {
		throw new UnsupportedOperationException("Cannot set entity code " + entityCode + " for this condition");
	}

	public IDLEntity getTransferable() {
		CompoundCondition_Transferable transferable = new CompoundCondition_Transferable();
		transferable.sort = CompoundConditionSort.from_int(this.operation);
		transferable.innerConditions = new org.omg.CORBA.Any[this.conditions.size()];
		int i = 0;
		for (Iterator it = this.conditions.iterator(); it.hasNext(); i++) {
			StorableObjectCondition condition = (StorableObjectCondition) it.next();
			Any any = ORB_INSTANCE.create_any();
			any.insert_Value(StorableObjectConditionBuilder.getConditionTransferable(condition));
			transferable.innerConditions[i] = any;
		}
		return transferable;
	}

	/**
	 * @return Returns the operation.
	 */
	public int getOperation() {
		return this.operation;
	}

	public Set getConditions() {
		return Collections.unmodifiableSet(this.conditions);
	}
}
