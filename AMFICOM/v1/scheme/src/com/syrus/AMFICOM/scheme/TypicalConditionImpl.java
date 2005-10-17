/*-
 * $Id: TypicalConditionImpl.java,v 1.15 2005/10/17 07:42:15 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Date;
import java.util.Set;

import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.TypicalSort;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: max $
 * @version $Revision: 1.15 $, $Date: 2005/10/17 07:42:15 $
 * @module scheme
 */
final class TypicalConditionImpl extends TypicalCondition {
	private static final long serialVersionUID = 1019929903178052406L;

	@SuppressWarnings("unused")
	private TypicalConditionImpl(final int firstInt,
			final int secondInt,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.firstInt = firstInt;
		this.secondInt = secondInt;
		this.type = TypicalSort._TYPE_NUMBER_INT;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	@SuppressWarnings("unused")
	private TypicalConditionImpl(final long firstLong,
			final long secondLong,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.firstLong = firstLong;
		this.secondLong = secondLong;
		this.type = TypicalSort._TYPE_NUMBER_LONG;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	@SuppressWarnings("unused")
	private TypicalConditionImpl(final double firstDouble,
			final double secondDouble,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.firstDouble = firstDouble;
		this.secondDouble = secondDouble;
		this.type = TypicalSort._TYPE_NUMBER_DOUBLE;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	@SuppressWarnings("unused")
	private TypicalConditionImpl(final String value,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.value = value;
		this.type = TypicalSort._TYPE_STRING;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	@SuppressWarnings("unused")
	private TypicalConditionImpl(final Date firstDate,
			final Date secondDate,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.value = firstDate;
		this.otherValue = secondDate;
		this.type = TypicalSort._TYPE_DATE;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	@SuppressWarnings("unused")
	private TypicalConditionImpl(final Boolean value,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.value = value;
		this.type = TypicalSort._TYPE_BOOLEAN;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	/**
	 * @param identifiables
	 * @see com.syrus.AMFICOM.general.StorableObjectCondition#isNeedMore(Set)
	 */
	@Override
	public boolean isNeedMore(final Set<? extends Identifiable> identifiables) {
		return !(this.type == TypicalSort._TYPE_STRING || this.type == TypicalSort._TYPE_BOOLEAN)
				|| this.operation != OperationSort._OPERATION_EQUALS
				|| identifiables == null
				|| identifiables.isEmpty();
	}

	/**
	 * @param storableObject
	 * @throws IllegalObjectEntityException 
	 * @see com.syrus.AMFICOM.general.StorableObjectCondition#isConditionTrue(StorableObject)
	 */
	@Override
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		StorableObjectWrapper wrapper;
		if (storableObject instanceof SchemeMonitoringSolution) {
			wrapper = SchemeMonitoringSolutionWrapper.getInstance();
		} else if (storableObject instanceof Scheme) {
			wrapper = SchemeWrapper.getInstance();
		} else if (storableObject instanceof SchemeElement) {
			wrapper = SchemeElementWrapper.getInstance();
		} else if (storableObject instanceof SchemeCableLink) {
			wrapper = SchemeCableLinkWrapper.getInstance();
		} else {
			throw new IllegalObjectEntityException(ERROR_ENTITY_NOT_REGISTERED + storableObject.getClass().getName(),
					IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
		return super.parseCondition(wrapper.getValue(storableObject, this.key));
	}
}
