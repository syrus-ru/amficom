/*
 * $Id: CompoundCondition.java,v 1.44.2.2 2006/03/01 15:40:35 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;

/**
 * Compound condition such as (A & B & C & ... etc), (A | B | C | ... etc) where A, B, C .. are
 * conditions (they can be also compound condition too)
 *
 * @version $Revision: 1.44.2.2 $, $Date: 2006/03/01 15:40:35 $
 * @author $Author: arseniy $
 * @module general
 */
public final class CompoundCondition implements StorableObjectCondition {
	private static final long serialVersionUID = -5517086652959242088L;

	private static final String ERROR_DIFFERENT_ENTITIES = "Unable to create CompoundCondition for conditions for different entities";

	private int operation;

	/**
	 * Collection&lt;StorableObjectCondition&gt;
	 */
	private Set<StorableObjectCondition> conditions;

	private Short entityCode = null;

	private boolean doCompare(final boolean firstResult, final boolean secondResult) {
		switch (this.operation) {
			case CompoundConditionSort._AND:
				return firstResult && secondResult;
			case CompoundConditionSort._OR:
				return firstResult || secondResult;
			default:
				throw new IllegalArgumentException("CompoundCondition.doComapare Unsupported operation " + this.operation);
		}
	}

	public CompoundCondition(final CompoundConditionSort operation,
			final StorableObjectCondition firstCondition,
			final StorableObjectCondition... restConditions) {
		assert operation != null : NON_NULL_EXPECTED;
		assert firstCondition != null : NON_NULL_EXPECTED;
		assert restConditions != null : NON_NULL_EXPECTED;

		this.entityCode = firstCondition.getEntityCode();
		this.operation = operation.value();
		this.conditions = new HashSet<StorableObjectCondition>();
		for (final StorableObjectCondition storableObjectCondition : restConditions) {
			if (storableObjectCondition.getEntityCode().shortValue() != this.entityCode.shortValue()) {
				throw new IllegalArgumentException(ERROR_DIFFERENT_ENTITIES);
			}
			this.conditions.add(storableObjectCondition);
		}
	}

	public CompoundCondition(final StorableObjectCondition firstCondition,
			final CompoundConditionSort operation,
			final StorableObjectCondition secondCondition) {
		this(operation, firstCondition, secondCondition);
	}

	public CompoundCondition(final Set<? extends StorableObjectCondition> conditions, final CompoundConditionSort operation) {
		assert conditions != null : NON_NULL_EXPECTED;
		assert conditions.size() > 1 : "Unable to create CompoundCondition for alone condition, use condition itself";

		Short code = null;
		for (final StorableObjectCondition condition : conditions) {
			final Short conditionEntityCode = condition.getEntityCode();
			if (code == null) {
				this.entityCode = conditionEntityCode;
				code = this.entityCode;
			} else if (code.shortValue() != conditionEntityCode.shortValue()) {
				throw new IllegalArgumentException(ERROR_DIFFERENT_ENTITIES);
			}
		}

		if (this.entityCode == null) {
			throw new IllegalArgumentException("Unable to create CompoundCondition unknown entities");
		}

		this.operation = operation.value();
		this.conditions = new HashSet<StorableObjectCondition>();
		this.conditions.addAll(conditions);
	}

	public void addCondition(final StorableObjectCondition condition) {
		assert (condition != null) : "NULL condition supplied";

		final Short code = condition.getEntityCode();
		if (code.equals(this.entityCode)) {
			this.conditions.add(condition);
		} else {
			throw new IllegalArgumentException("Cannot add condition for entity '" + ObjectEntities.codeToString(code)
					+ "' to set of condition for entity '" + ObjectEntities.codeToString(this.entityCode) + "'");
		}
	}

	public CompoundCondition(final IdlCompoundCondition transferable) throws IllegalDataException {
		this.operation = transferable.sort.value();
		final IdlStorableObjectCondition innerConditions[] = transferable.innerConditions;
		if (innerConditions.length <= 1) {
			throw new IllegalDataException("Unable to create CompoundCondition for " + innerConditions.length + "  condition");
		}
		this.conditions = new HashSet<StorableObjectCondition>(innerConditions.length);
		Short code = null;
		for (int i = 0; i < innerConditions.length; i++) {
			final StorableObjectCondition condition = StorableObjectConditionBuilder.restoreCondition(innerConditions[i]);
			this.conditions.add(condition);
			final Short conditionEntityCode = condition.getEntityCode();
			if (code == null) {
				this.entityCode = conditionEntityCode;
				code = this.entityCode;
			} else if (code.shortValue() != conditionEntityCode.shortValue()) {
				throw new IllegalDataException("Unable to create CompoundCondition for conditions for different entities");
			}
		}
		
		if (this.entityCode == null) {
			throw new IllegalDataException("Unable to create CompoundCondition unknown entities");
		}
	}

	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		boolean firstItem = true;
		boolean result = false;
		for (final StorableObjectCondition condition : this.conditions) {
			if (firstItem) {
				result = condition.isConditionTrue(storableObject);
				firstItem = false;
			} else {
				result = this.doCompare(result, condition.isConditionTrue(storableObject));
			}
		}

		return result;
	}

	public boolean isNeedMore(final Set<? extends Identifiable> identifiables) {
		boolean firstItem = true;
		boolean result = false;

		for (final StorableObjectCondition condition : this.conditions) {
			if (firstItem) {
				result = condition.isNeedMore(identifiables);
				firstItem = false;
			} else {
				result = this.doCompare(result, condition.isNeedMore(identifiables));
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

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	public IdlStorableObjectCondition getIdlTransferable(final ORB orb) {
		return this.getIdlTransferable();
	}

	public IdlStorableObjectCondition getIdlTransferable() {
		final IdlCompoundCondition transferable = new IdlCompoundCondition();
		transferable.sort = CompoundConditionSort.from_int(this.operation);
		transferable.innerConditions = new IdlStorableObjectCondition[this.conditions.size()];
		int i = 0;
		for (final Iterator<StorableObjectCondition> storableObjectConditionIterator = this.conditions.iterator(); storableObjectConditionIterator.hasNext(); i++) {
			final StorableObjectCondition storableObjectCondition = storableObjectConditionIterator.next();
			transferable.innerConditions[i] = storableObjectCondition.getIdlTransferable();
		}
		
		final IdlStorableObjectCondition condition = new IdlStorableObjectCondition();
		condition.compoundCondition(transferable);
		return condition;
	}	

	/**
	 * @return Returns the operation.
	 */
	public int getOperation() {
		return this.operation;
	}

	public Set<StorableObjectCondition> getConditions() {
		return Collections.unmodifiableSet(this.conditions);
	}

	public int getConditionsNumber() {
		return this.conditions.size();
	}
	
	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer(this.getClass().getSimpleName());
		buffer.append(": ");
		String operationString;
		switch (this.operation) {
			case CompoundConditionSort._AND:
				operationString = " AND ";
				break;
			case CompoundConditionSort._OR:
				operationString = " OR " ;
				break;
			default:
				operationString = " ?undef? ";
				break;
		}
		boolean first = true;
		for(StorableObjectCondition condition : this.conditions) {
			if (first) {
				first = false;
			} else {
				buffer.append(operationString);
			}
			buffer.append(OPEN_BRACKET);
			buffer.append(condition.toString());
			buffer.append(CLOSE_BRACKET);
		}
		return buffer.toString();
	}
}
