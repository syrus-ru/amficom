/*
 * $Id: TypicalConditionImpl.java,v 1.9 2005/04/02 17:33:49 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Date;
import java.util.Set;

import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.TypicalSort;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.9 $, $Date: 2005/04/02 17:33:49 $
 * @author $Author: arseniy $
 * @module general_v1
 */
class TypicalConditionImpl extends TypicalCondition {

	private TypicalConditionImpl(final int firstInt,
			final int secondInt,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		super(); // First line must invoke superconstructor w/o parameters.
		this.firstInt = firstInt;
		this.secondInt = secondInt;
		this.type = TypicalSort._TYPE_NUMBER_INT;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	private TypicalConditionImpl(final long firstLong,
			final long secondLong,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		super(); // First line must invoke superconstructor w/o parameters.
		this.firstLong = firstLong;
		this.secondLong = secondLong;
		this.type = TypicalSort._TYPE_NUMBER_LONG;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	private TypicalConditionImpl(final double firstDouble,
			final double secondDouble,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		super(); // First line must invoke superconstructor w/o parameters.
		this.firstDouble = firstDouble;
		this.secondDouble = secondDouble;
		this.type = TypicalSort._TYPE_NUMBER_DOUBLE;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	private TypicalConditionImpl(final String value,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		super(); // First line must invoke superconstructor w/o parameters.
		this.value = value;
		this.type = TypicalSort._TYPE_STRING;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	private TypicalConditionImpl(final Date firstDate,
			final Date secondDate,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		super(); // First line must invoke superconstructor w/o parameters.
		this.value = firstDate;
		this.otherValue = secondDate;
		this.type = TypicalSort._TYPE_DATE;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
		
	}

	public boolean isNeedMore(final Set set) {
		boolean more = true;

		if (this.type == TypicalSort._TYPE_STRING && this.operation == OperationSort._OPERATION_EQUALS)
			if (set != null && !set.isEmpty())
				more = false;

		return more;
	}

	public boolean isConditionTrue(final Object object) throws IllegalObjectEntityException {
		Wrapper wrapper;
		if (object instanceof ParameterType)
			wrapper = ParameterTypeWrapper.getInstance();
		else
			if (object instanceof CharacteristicType)
				wrapper = CharacteristicTypeWrapper.getInstance();
			else
				if (object instanceof Characteristic)
					wrapper = CharacteristicWrapper.getInstance();
				else
					throw new IllegalObjectEntityException(ENTITY_NOT_REGISTERED + object.getClass().getName(),
							IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);

		return super.parseCondition(wrapper.getValue(object, this.key));
	}

}
