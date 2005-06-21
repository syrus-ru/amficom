/*-
 * $Id: TypicalConditionImpl.java,v 1.12 2005/06/21 12:43:48 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Date;
import java.util.Set;

import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.TypicalSort;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.12 $, $Date: 2005/06/21 12:43:48 $
 * @author $Author: bass $
 * @module general_v1
 */
final class TypicalConditionImpl extends TypicalCondition {
	@SuppressWarnings("unusedPrivate")
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

	@SuppressWarnings("unusedPrivate")
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

	@SuppressWarnings("unusedPrivate")
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

	@SuppressWarnings("unusedPrivate")
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

	@SuppressWarnings("unusedPrivate")
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

	@Override
	public boolean isNeedMore(final Set storableObjects) {
		return this.type != TypicalSort._TYPE_STRING
				|| this.operation != OperationSort._OPERATION_EQUALS
				|| storableObjects == null
				|| storableObjects.isEmpty();
	}

	@Override
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		Wrapper wrapper;
		if (storableObject instanceof ParameterType)
			wrapper = ParameterTypeWrapper.getInstance();
		else
			if (storableObject instanceof CharacteristicType)
				wrapper = CharacteristicTypeWrapper.getInstance();
			else
				if (storableObject instanceof Characteristic)
					wrapper = CharacteristicWrapper.getInstance();
				else
					throw new IllegalObjectEntityException(ENTITY_NOT_REGISTERED + storableObject.getClass().getName(),
							IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);

		return super.parseCondition(wrapper.getValue(storableObject, this.key));
	}

}
